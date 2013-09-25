<?php

require '../xcart/top.inc.php';
require '../xcart/init.php';


$curtime = XC_TIME + $config['Appearance']['timezone_offset'];
$start_dates['last_login'] = $previous_login_date; // Since last login
$start_dates['today'] = func_prepare_search_date($curtime) - $config['Appearance']['timezone_offset']; // Today
$start_week = $curtime - date('w', $curtime) * 24 * 3600; // Week starts since Sunday
$start_dates['week'] = func_prepare_search_date($start_week) - $config['Appearance']['timezone_offset']; // Current week
$start_dates['month'] = mktime(0, 0, 0, date('m', $curtime), 1, date('Y', $curtime)) - $config['Appearance']['timezone_offset']; // Current month
$curtime = XC_TIME;

class DbApiFunctions
{

    private $db;

    function __construct()
    {
        include_once './db_connect.php';

        $this->db = new DB_Connect();
        $this->db->connect();
    }

    function __destruct()
    {
    }

    public function get_dashboard_data()
    {
        global $sql_tbl, $curtime, $start_dates;

        $start_date = $start_dates['today'];

        $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";

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

    function get_last_orders($from, $size)
    {
        global $sql_tbl;

        $order_query = mysql_query
        ("
          SELECT orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
          FROM $sql_tbl[orders]
          ORDER BY date DESC LIMIT $from, $size
        ") or die(mysql_error());

        $result_array = mysql_fetch_assoc($order_query);
        $result_array['date'] = gmdate("m-d-Y", $result_array['date']);
        $order_id = $result_array['orderid'];
        $result_array['details'] = get_order_details($order_id);

        return $result_array;
    }

    private function get_first_cell($query)
    {
        $result = mysql_query($query);
        $row = mysql_fetch_row($result);
        return $row[0];
    }

}