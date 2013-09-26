<?php

require_once '../xcart/top.inc.php';
require_once '../xcart/init.php';


class DbApiFunctions
{
    private $db;
    private $start_dates;
    private $curtime;


    function __construct()
    {
        include_once './db_connect.php';

        global $config, $previous_login_date;

        $this->db = new DB_Connect();
        $this->db->connect();

        $this->curtime = XC_TIME + $config['Appearance']['timezone_offset'];
        $this->start_dates['last_login'] = $previous_login_date; // Since last login
        $this->start_dates['today'] = func_prepare_search_date($this->curtime) - $config['Appearance']['timezone_offset']; // Today
        $start_week = $this->curtime - date('w', $this->curtime) * 24 * 3600; // Week starts since Sunday
        $this->start_dates['week'] = func_prepare_search_date($start_week) - $config['Appearance']['timezone_offset']; // Current week
        $this->start_dates['month'] = mktime(0, 0, 0, date('m', $this->curtime), 1, date('Y', $this->curtime)) - $config['Appearance']['timezone_offset']; // Current month
        $this->curtime = XC_TIME;
    }

    function __destruct()
    {
    }

    public function login($username, $password, $udid)
    {
        require_once '../xcart/include/login.php';
        require_once '../xcart/include/classes/class.XCPasswordHash.php';

//    require '../include/login.php';
//    require_once '../include/classes/class.XCPasswordHash.php';

        global $sql_tbl;

        $sql = "CREATE TABLE IF NOT EXISTS xcart_mobile_session
            (
                udid VARCHAR(255),
                sid VARCHAR(255),
                date int(11),
                PRIMARY KEY(udid)
            )";

        db_query($sql) or die(mysql_error());

        $usertype = 'P'; //TODO: P - in simple mode? in real xcart - A
        $user_data = func_query_first("SELECT * FROM $sql_tbl[customers] WHERE login='$username' AND usertype='$usertype'");

        if (!$user_data) {
            return array('error' => "Incorrect login");
        }

        $password = trim(stripslashes($password));
        $right_hash = text_decrypt($user_data['password']);
        $t_hasher = new XCPasswordHash();
        $is_correct = $t_hasher->CheckPassword($password, $right_hash);

        $answer = array();
        $answer['upload_type'] = 'login';

        if ($is_correct) {
            $sid = uniqid('', true);
            db_query("UPDATE $sql_tbl[customers] SET last_login='" . XC_TIME . "' WHERE id='$user_data[id]'") or die(mysql_error());
            db_query("
            INSERT INTO xcart_mobile_session (sid, date, udid)
            VALUES('$sid'," . time() . ",'$udid')
            ON DUPLICATE KEY UPDATE sid='$sid', date=" . time()
            ) or die(mysql_error());

            $answer['upload_status'] = 'login success';
            $answer['sid'] = $sid;
        } else {
            return array('error' => "Incorrect pass");
        }

        return $answer;
    }

    public function get_dashboard_data()
    {
        global $sql_tbl;

        $start_date = $this->start_dates['today'];

        $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$this->curtime'";

        $today_sales_sql = "SELECT SUM(total) FROM $sql_tbl[orders] WHERE (status='P' OR status='C') AND $date_condition";
        $today_sales = price_format($this->get_first_cell($today_sales_sql));

        $today_visitors_sql = "SELECT COUNT(*) FROM $sql_tbl[customers] WHERE last_login >= '$start_date'";
        $today_visitors = $this->get_first_cell($today_visitors_sql);

        $today_sold_sql = "SELECT COUNT(*) FROM $sql_tbl[orders] WHERE (status='P' OR status='C') AND $date_condition";
        $today_sold = $this->get_first_cell($today_sold_sql);

        return array(
            'today_sales' => $today_sales,
            'low_stock' => '0',
            'today_visitors' => $today_visitors,
            'today_sold' => $today_sold,
            'reviews_today' => '0'
        );
    }

    public function get_orders_list($from, $size, $date, $status)
    {
        global $sql_tbl;

        $date_condition = "";
        $status_condition = "";

        if ($date) {
            $start_date = $this->start_dates[$date];
            $date_condition = "AND $sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$this->curtime'";
        }

        if ($status) {
            $status_condition = "AND status = '$status'";
        }

        $order_query = mysql_query
        ("
          SELECT orderid, status, total, title, firstname,lastname, date,
          (
            SELECT COUNT(*)
            FROM $sql_tbl[order_details]
            WHERE $sql_tbl[order_details].orderid=$sql_tbl[orders].orderid
          )
          AS items

          FROM $sql_tbl[orders]
          WHERE 1 $date_condition $status_condition
          ORDER BY date DESC LIMIT $from, $size
        ") or die(mysql_error());

        $orders = array();

        while ($row = mysql_fetch_assoc($order_query)) {
            $row['date'] = gmdate("m-d-Y", $row['date']);
            array_push($orders, $row);
        }

        return $orders;
    }

    public function get_order_info($id)
    {
        global $sql_tbl;

        $order_query = mysql_query
        ("
          SELECT orderid, status, total, title, firstname,lastname, company, b_title, b_firstname, b_lastname, b_address, b_city, b_county, b_state, b_country, b_zipcode, b_zip4, b_phone, b_fax, s_title, s_firstname, s_lastname, s_address, s_city, s_county, s_state, s_country, s_zipcode, s_phone, s_fax, s_zip4, shippingid, shipping, tracking, payment_method, date
          FROM $sql_tbl[orders]
          WHERE orderid = $id
        ") or die(mysql_error());

        $order = mysql_fetch_assoc($order_query);
        $order['date'] = gmdate("m-d-Y", $order['date']);
        $order_id = $order['orderid'];
        $order['details'] = $this->get_order_details($order_id);

        return $order;
    }

    public function get_order_details($id)
    {
        global $sql_tbl;

        $query = mysql_query
        ("
          SELECT productid, price, amount, provider, product_options, itemid, productcode, product
          FROM $sql_tbl[order_details]
          WHERE orderid=$id
        ") or die(mysql_error());

        $order_details_array = array();
        while ($row = mysql_fetch_assoc($query)) {
            array_push($order_details_array, $row);
        }

        return $order_details_array;
    }

    // Util functions

    private function get_first_cell($query)
    {
        $result = mysql_query($query);
        $row = mysql_fetch_row($result);
        return $row[0];
    }

}