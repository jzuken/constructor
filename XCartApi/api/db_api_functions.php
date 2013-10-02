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

        global $config, $sql_host, $sql_user, $sql_password, $sql_db;

        $this->db = new DB_Connect();
        $this->db->connect($sql_host, $sql_user, $sql_password, $sql_db);

        $this->curtime = XC_TIME + $config['Appearance']['timezone_offset'];
        $this->start_dates['all'] = 0; // Since last login
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

        $low_stock_sql = "SELECT COUNT(*)
                          FROM $sql_tbl[products]
                          WHERE avail <= low_avail_limit";
        $low_stock = $this->get_first_cell($low_stock_sql);

        $today_visitors_sql = "SELECT COUNT(*) FROM $sql_tbl[customers] WHERE last_login >= '$start_date'";
        $today_visitors = $this->get_first_cell($today_visitors_sql);

        $today_sold_sql = "SELECT COUNT(*) FROM $sql_tbl[orders] WHERE (status='P' OR status='C') AND $date_condition";
        $today_sold = $this->get_first_cell($today_sold_sql);

        return array(
            'today_sales' => $today_sales,
            'low_stock' => $low_stock,
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
          SELECT orderid, status, total, title, firstname, lastname, company, b_title, b_firstname, b_lastname, b_address, b_city, b_county, b_state, b_country, b_zipcode, b_zip4, b_phone, b_fax, s_title, s_firstname, s_lastname, s_address, s_city, s_county, s_state, s_country, s_zipcode, s_phone, s_fax, s_zip4, shippingid, shipping, tracking, payment_method, date
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

    public function get_products($from, $size, $low_stock)
    {
        global $sql_tbl;

        $low_stock_condition ="";
        if($low_stock){
            $low_stock_condition = "AND avail <= low_avail_limit";
        }

        $query = mysql_query
        ("
          SELECT $sql_tbl[products].productid, $sql_tbl[products].productcode, $sql_tbl[products].list_price, $sql_tbl[products].avail, $sql_tbl[products_lng_current].product FROM $sql_tbl[products]
          INNER JOIN $sql_tbl[products_lng_current]
          ON $sql_tbl[products_lng_current].productid = $sql_tbl[products].productid
          WHERE 1 $low_stock_condition
          LIMIT $from, $size
        ") or die(mysql_error());

        $products_array = array();
        while ($row = mysql_fetch_assoc($query)) {
            array_push($products_array, $row);
        }

        return $products_array;
    }

    public function get_product_info($id)
    {
        global $sql_tbl;

        $query = mysql_query
        ("
          SELECT $sql_tbl[products].*, $sql_tbl[products_lng_current].* FROM $sql_tbl[products]
          INNER JOIN $sql_tbl[products_lng_current]
          ON $sql_tbl[products_lng_current].productid = $sql_tbl[products].productid
          WHERE $sql_tbl[products].productid = $id
        ") or die(mysql_error());

        $product_info = mysql_fetch_assoc($query);

        $image_query = mysql_query
        ("
          SELECT image_path FROM $sql_tbl[images_P]
          WHERE $sql_tbl[images_P].id = $product_info[productid]
        ") or die(mysql_error());

        $image_info = mysql_fetch_assoc($image_query);

        global $xcart_http_host;
        $url = "http://" . $xcart_http_host . '/xcart' . str_replace("./", "/", $image_info[image_path]);

        $product_info["url"] = "$url";

        return $product_info;
    }

    public function get_reviews($from, $size)
    {
        global $sql_tbl;

        $query = mysql_query
        ("
          SELECT $sql_tbl[product_reviews].review_id, $sql_tbl[product_reviews].productid, $sql_tbl[product_reviews].email, $sql_tbl[product_reviews].message, $sql_tbl[products_lng_current].product
          FROM $sql_tbl[product_reviews]
          INNER JOIN $sql_tbl[products_lng_current]
          ON $sql_tbl[product_reviews].productid = $sql_tbl[products_lng_current].productid
          LIMIT $from, $size
        ") or die(mysql_error());

        $array = array();
        while ($row = mysql_fetch_assoc($query)) {
            array_push($array, $row);
        }

        return $array;
    }

    public function delete_review($id)
    {
        global $sql_tbl;

        if (!$id) {
            return array("error" => "Id is not found");
        }

        $result = mysql_query("DELETE FROM $sql_tbl[product_reviews] WHERE review_id=$id") or die(mysql_error());
        $answer = array(
            'upload_status' => (string)$result,
            'upload_type' => 'delete',
            'upload_data' => 'review',
            'id' => $id
        );

        return $answer;
    }


    public function change_tracking_number($tracking_number, $id)
    {
        global $sql_tbl;

        if (!$tracking_number) {
            return array("error" => "tracking_number is not found");
        }

        $result = mysql_query
        ("
          UPDATE  $sql_tbl[orders]
          SET tracking='$tracking_number'
          WHERE orderid=$id
        ") or die(mysql_error());

        $answer = array(
            'upload_status' => (string)$result,
            'upload_type' => 'update',
            'upload_data' => 'tracking_number',
            'id' => $id
        );

        return $answer;
    }

    public function change_status($status, $id)
    {
        global $sql_tbl;

        if (!$status) {
            return array("error" => "status is not found");
        }

        $result = mysql_query
        ("
          UPDATE  $sql_tbl[orders]
          SET status='$status'
          WHERE orderid=$id
        ") or die(mysql_error());

        $answer = array(
            'upload_status' => (string)$result,
            'upload_type' => 'update',
            'upload_data' => 'status',
            'id' => $id
        );

        return $answer;
    }


    public function update_product_price($product_id, $price)
    {
        global $sql_tbl;

        $result = mysql_query
        ("
          UPDATE $sql_tbl[products]
          SET list_price=$price
          WHERE productid=$product_id
        ") or die(mysql_error());

        $answer = array(
            'upload_status' => (string)$result,
            'upload_type' => 'update',
            'upload_data' => 'product',
            'id' => $product_id
        );

        return $answer;
    }

   public function get_users($from, $size)
{
    global $sql_tbl;

    $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].email, $sql_tbl[customers].last_login,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'total_orders'
                FROM $sql_tbl[customers]
                LIMIT $from, $size
                ") or die(mysql_error());

    $users_array = array();
    while ($row = mysql_fetch_assoc($query)) {
        $row[last_login] = gmdate("m-d-Y", $row['last_login']);
        array_push($users_array, $row);
    }

    return $users_array;
}

    public function get_user_info($id)
    {
        global $sql_tbl;

        $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[address_book].*
                FROM $sql_tbl[customers]
                INNER JOIN $sql_tbl[address_book]
                ON $sql_tbl[address_book].userid = $sql_tbl[customers].id
                WHERE $sql_tbl[customers].id = $id
                ") or die(mysql_error());

        $user_info  = mysql_fetch_assoc($query);
        $user_info[last_login] = gmdate("m-d-Y", $user_info['last_login']);

        return $user_info;
    }

    // Util functions

    private function get_first_cell($query)
    {
        $result = mysql_query($query);
        $row = mysql_fetch_row($result);
        return $row[0];
    }

}