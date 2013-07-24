<?php

/*
 * Development imports
 */
require './xcart/top.inc.php';
require './xcart/init.php';

/*
 * Production imports
 */
//require './top.inc.php';
//require './init.php';
mysql_connect($sql_host, $sql_user, $sql_password)  or die(mysql_error());
mysql_select_db($sql_db) or die(mysql_error());

header('Content-Type: application/json; charset=utf-8');
$users_fields = array(id, username, usertype, invalid_login_attempts, title, firstname, lastname, company, email, url, last_login, first_login, status, activation_key, autolock, suspend_date, referer, ssn, language, change_password, change_password_date, parent, pending_plan_id, activity, membershipid, pending_membershipid, tax_number, tax_exempt, trusted_provider, cookie_access);
$orders_fields = array(orderid, status, total, title, firstname, b_firstname, lastname, b_lastname);
$order_details_fields = array(orderid, productid, price, amount, provider, product_options, itemid, productcode, product);
$discounts_fields = array(discountid, minprice, discount, discount_type, provider);
$dates = array('last_login', 'today', 'week', 'month');

$curtime = XC_TIME + $config['Appearance']['timezone_offset'];
$start_dates[$dates[0]] = $previous_login_date; // Since last login
$start_dates[$dates[1]] = func_prepare_search_date($curtime) - $config['Appearance']['timezone_offset']; // Today
$start_week = $curtime - date('w', $curtime) * 24 * 3600; // Week starts since Sunday
$start_dates[$dates[2]] = func_prepare_search_date($start_week) - $config['Appearance']['timezone_offset']; // Current week
$start_dates[$dates[3]] = mktime(0, 0, 0, date('m', $curtime), 1, date('Y', $curtime)) - $config['Appearance']['timezone_offset']; // Current month
$curtime = XC_TIME;

echo get_response();

function get_response()
{
    global $users_fields, $orders_fields, $discounts_fields, $order_details_fields;
    $request = $_GET['request'];
    switch ($request) {
        case 'users_count':
            $response = get_users_count();
            break;
        case 'users':
            $response = get_users($users_fields);
            break;
        case 'orders':
            $response = get_orders($orders_fields, $_GET['from'], $_GET['to']);
            break;
        case 'last_order':
            $response = get_last_order($orders_fields);
            break;
        case 'discounts':
            $response = get_discounts($discounts_fields);
            break;
        case 'create_discount':
            $response = create_discount($_GET['minprice'], $_GET['discount'], $_GET['discount_type'], $_GET['provider']);
            break;
        case 'update_discount':
            $response = update_discount($_GET['id'], $_GET['minprice'], $_GET['discount'], $_GET['discount_type'], $_GET['provider']);
            break;
        case 'delete_discount':
            $response = delete_discount($_GET['id']);
            break;
        case 'orders_statistic':
            $response = get_orders_statistic();
            break;
        case 'top_products':
            $response = get_top_products_statistic();
            break;
        case 'top_categories':
            $response = get_top_categories_statistic();
            break;
        default:
            $response = "error";
            break;
    }
    return $response;
}

function get_orders_statistic()
{
    global $start_dates, $dates;
    $result_array = array();
    foreach ($dates as $date) {
        $result_array[$date] = get_orders_count($start_dates[$date]);
    }
    return json_encode($result_array);
}

function get_orders_count($start_date)
{
    global $sql_tbl, $curtime;
    $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";
    $orders_count = array();
    $statuses = array('C', 'P', 'F', 'D', 'D', 'I', 'X', 'Q');
    foreach ($statuses as $status) {
        $orders_count[$status] = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[orders] WHERE status='$status' AND $date_condition");
    }
    $orders_count['Total'] = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $date_condition");

    return $orders_count;
}

function get_top_products_statistic()
{
    global $start_dates, $dates;
    $result_array = array();
    foreach ($dates as $date) {
        $result_array[$date] = get_top_products($start_dates[$date]);
    }
    return json_encode($result_array);
}

function get_top_products($start_date)
{
    global $sql_tbl, $curtime;
    $max_top_sellers = 10;
    $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";

    $ordered_products = func_query("
    SELECT $sql_tbl[order_details].productid, $sql_tbl[products].productcode, $sql_tbl[products_lng_current].product,
           SUM($sql_tbl[order_details].amount) AS count
     FROM $sql_tbl[orders]
     INNER JOIN $sql_tbl[order_details] USING (orderid)
     INNER JOIN $sql_tbl[products] ON $sql_tbl[order_details].productid=$sql_tbl[products].productid
     INNER JOIN $sql_tbl[products_lng_current] ON $sql_tbl[products_lng_current].productid=$sql_tbl[products].productid
     WHERE $date_condition
       AND $sql_tbl[orders].status NOT IN ('F','D')
     GROUP BY $sql_tbl[order_details].productid
     ORDER BY count DESC LIMIT 0, $max_top_sellers");
    //print_r($ordered_products);
    return $ordered_products;
}

function get_top_categories_statistic()
{
    global $start_dates, $dates;
    $result_array = array();
    foreach ($dates as $date) {
        $result_array[$date] = get_top_categories($start_dates[$date]);
    }
    return json_encode($result_array);
}

function get_top_categories($start_date)
{
    global $sql_tbl, $curtime;
    $max_top_sellers = 10;
    $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";

    $categories = func_query("
    SELECT $sql_tbl[products_categories].categoryid, $sql_tbl[categories].category, SUM($sql_tbl[order_details].amount) as count
          FROM $sql_tbl[order_details]
         INNER JOIN $sql_tbl[orders]
            ON $sql_tbl[order_details].orderid    = $sql_tbl[orders].orderid
         INNER JOIN $sql_tbl[products_categories]
            ON $sql_tbl[order_details].productid  = $sql_tbl[products_categories].productid
            AND $sql_tbl[products_categories].main = 'Y'
         INNER JOIN $sql_tbl[categories]
            ON $sql_tbl[categories].categoryid = $sql_tbl[products_categories].categoryid
         WHERE $date_condition
         GROUP BY $sql_tbl[products_categories].categoryid
         ORDER BY count DESC LIMIT 0, $max_top_sellers
        ");
    //print_r($categories);
    return $categories;
}

function get_first_cell($query)
{
    $result = mysql_query($query);
    $row = mysql_fetch_row($result);
    return $row[0];
}

function get_users_count()
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[customers]") or die(mysql_error());
    $count = mysql_num_rows($query);
    $json_result = array('count' => $count);
    return json_encode($json_result);
}

function get_users($fields)
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[customers]") or die(mysql_error());
    return get_json($fields, $query);
}

function get_orders($fields, $from, $to)
{
    if (is_null($from)) {
        $from = 0;
    }
    if (is_null($to)) {
        $to = XC_TIME;
    }
    global $sql_tbl;
    $date_condition = "date BETWEEN $from AND $to";
    $query = mysql_query("SELECT * FROM $sql_tbl[orders] WHERE $date_condition") or die(mysql_error());
    return get_json($fields, $query);
}

function get_last_order($fields)
{
    global $sql_tbl, $order_details_fields;
    $order_query = mysql_query("
        SELECT orderid, status, total, title, firstname, b_firstname, lastname, b_lastname
        FROM $sql_tbl[orders]
        ORDER BY date DESC LIMIT 1
        ") or die(mysql_error());
    $result_array = array();
    $row = mysql_fetch_array($order_query);
    foreach ($fields as $value) {
        $result_array[$value] = $row[$value];
    }
    $order_id = $row['orderid'];
    $order_details = get_order_details($order_details_fields, $order_id);
    $result_array[details] = $order_details;
    return json_encode($result_array);
}

function get_order_details($fields, $id)
{
    if (is_null($id)) {
        return 'error';
    }
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[order_details] WHERE orderid=$id") or die(mysql_error());
    $row = mysql_fetch_array($query);
    $order_details_array = array();
    while ($row = mysql_fetch_array($query)) {
        $product_details = array();
        foreach ($fields as $value) {
            $product_details[$value] = $row[$value];
        }
        array_push($order_details_array, $product_details);
    }

    return $order_details_array;
}

function get_discounts($fields)
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[discounts]") or die(mysql_error());
    return get_json($fields, $query);
}

function create_discount($minprice, $discount, $discount_type, $provider)
{
    global $sql_tbl;
    if (is_null($minprice) || is_null($discount) || is_null($discount_type) || is_null($provider)) {
        return "error";
    }
    $query = mysql_query("INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider) VALUES ($minprice, $discount, '$discount_type', $provider)") or die(mysql_error());
    echo db_query($query);
    return "success";
}

function update_discount($id, $minprice, $discount, $discount_type, $provider)
{
    global $sql_tbl;
    if (is_null($id) || is_null($minprice) || is_null($discount) || is_null($discount_type) || is_null($provider)) {
        return "error";
    }
    $query = mysql_query("UPDATE $sql_tbl[discounts] SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider") or die(mysql_error());
    echo db_query($query);
    return "success";
}

function delete_discount($id)
{
    global $sql_tbl;
    if (is_null($id)) {
        return "error";
    }
    $query = mysql_query("DELETE FROM $sql_tbl[discounts] WHERE discountid=$id") or die(mysql_error());
    echo db_query($query);
    return "success";
}

function get_json($fields, $query)
{
    $json_result = array();
    while ($row = mysql_fetch_array($query)) {
        $order_json = array();
        foreach ($fields as $value) {
            $order_json[$value] = $row[$value];
        }
        array_push($json_result, $order_json);
    }
    return json_encode($json_result);
}