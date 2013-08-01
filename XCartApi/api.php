<?php

/*
 * Development imports
 */
//require './xcart/top.inc.php';
//require './xcart/init.php';

/*
 * Production imports
 */
require './top.inc.php';
require './init.php';

mysql_connect($sql_host, $sql_user, $sql_password)  or die(mysql_error());
mysql_select_db($sql_db) or die(mysql_error());

header('Content-Type: application/json; charset=utf-8');
$curtime = XC_TIME + $config['Appearance']['timezone_offset'];
$start_dates['last_login'] = $previous_login_date; // Since last login
$start_dates['today'] = func_prepare_search_date($curtime) - $config['Appearance']['timezone_offset']; // Today
$start_week = $curtime - date('w', $curtime) * 24 * 3600; // Week starts since Sunday
$start_dates['week'] = func_prepare_search_date($start_week) - $config['Appearance']['timezone_offset']; // Current week
$start_dates['month'] = mktime(0, 0, 0, date('m', $curtime), 1, date('Y', $curtime)) - $config['Appearance']['timezone_offset']; // Current month
$curtime = XC_TIME;

echo get_response();

function get_response()
{
    $request = $_GET['request'];
    switch ($request) {
        case 'users_count':
            $response = get_users_count();
            break;
        case 'users':
            $response = get_users($_GET['from'], $_GET['size'], $_GET['sort']);
            break;
        case 'last_order':
            $response = get_last_order();
            break;
        case 'discounts':
            $response = get_discounts();
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
    global $start_dates;
    $result_array = array();
    foreach ($start_dates as $key => $date) {
        $result_array[$key] = get_orders_count($date);
    }
    return array_to_json($result_array);
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
    global $start_dates;
    $result_array = array();
    foreach ($start_dates as $key => $date) {
        $result_array[$key] = is_array_check(get_top_products($date));
    }
    return array_to_json($result_array);
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
    return $ordered_products;
}

function get_top_categories_statistic()
{
    global $start_dates;
    $result_array = array();
    foreach ($start_dates as $key => $date) {
        $result_array[$key] = is_array_check(get_top_categories($date));
    }
    return array_to_json($result_array);
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
    return $categories;
}

/**
 * @param $date
 * @return array|string
 */
function is_array_check($data)
{
    if (!is_array($data)) {
        return 'none';
    }
    return $data;
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
    $count = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[customers]");
    $array = array('registered' => $count);
    return $array;
}

function get_users($from, $size, $sort)
{
    global $sql_tbl;
    if (is_null($from)) {
        $from = 0;
    }
    if (is_null($size)) {
        $size = 20;
    }
    if (is_null($sort)) {
        $sort = 'none';
    }
    switch ($sort) {
        case 'login_date':
            $query = mysql_query("
                SELECT $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider
                FROM $sql_tbl[customers]
                ORDER BY $sql_tbl[customers].last_login desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        case 'order_date':
            $query = mysql_query("
                SELECT $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider, orders.date
                FROM $sql_tbl[customers]
                INNER JOIN (SELECT userid, MAX(date) as 'date' FROM $sql_tbl[orders] GROUP BY userid) as orders
                ON $sql_tbl[customers].id = orders.userid
                ORDER BY orders.date desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        case 'orders':
            $query = mysql_query("
                SELECT $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                ORDER BY orders_count desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        default:
            $query = mysql_query("
                SELECT $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider
                FROM $sql_tbl[customers]
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
    }

    $users_array = array();
    while ($row = mysql_fetch_assoc($query)) {
        array_push($users_array, $row);
    }
    $json_array = array(
        'users_count' => get_users_count(),
        'users' => $users_array);
    return array_to_json($json_array);
}

function get_last_order()
{
    global $sql_tbl;
    $orders_fields = array(orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date);
    $order_query = mysql_query("
        SELECT orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
        FROM $sql_tbl[orders]
        ORDER BY date DESC LIMIT 1
        ") or die(mysql_error());
    $result_array = array();
    $row = mysql_fetch_array($order_query);
    foreach ($orders_fields as $value) {
        $result_array[$value] = $row[$value];
    }
    $order_id = $row['orderid'];
    $order_details = get_order_details($order_id);
    $result_array[details] = $order_details;
    return array_to_json($result_array);
}

function get_order_details($id)
{
    if (is_null($id)) {
        return 'error';
    }
    global $sql_tbl;
    $order_details_fields = array(productid, price, amount, provider, product_options, itemid, productcode, product);
    $query = mysql_query("SELECT * FROM $sql_tbl[order_details] WHERE orderid=$id") or die(mysql_error());
    $order_details_array = array();
    while ($row = mysql_fetch_array($query)) {
        $product_details = array();
        foreach ($order_details_fields as $value) {
            $product_details[$value] = $row[$value];
        }
        array_push($order_details_array, $product_details);
    }

    return $order_details_array;
}

function get_discounts($fields)
{
    global $sql_tbl;
    $discounts_fields = array(discountid, minprice, discount, discount_type, provider);
    $query = mysql_query("
        SELECT *
        FROM $sql_tbl[discounts]
        ") or die(mysql_error());
    return get_json($discounts_fields, $query);
}

function create_discount($minprice, $discount, $discount_type, $provider)
{
    global $sql_tbl;
    if (is_null($minprice) || is_null($discount) || is_null($discount_type) || is_null($provider)) {
        return "error";
    }
    $query = mysql_query("
        INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider)
        VALUES ($minprice, $discount, '$discount_type', $provider)
        ") or die(mysql_error());
    return "success";
}

function update_discount($id, $minprice, $discount, $discount_type, $provider)
{
    global $sql_tbl;
    if (is_null($id) || is_null($minprice) || is_null($discount) || is_null($discount_type) || is_null($provider)) {
        return "error";
    }
    $query = mysql_query("
        UPDATE $sql_tbl[discounts]
        SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider
        WHERE discountid=$id
        ") or die(mysql_error());
    return "success";
}

function delete_discount($id)
{
    global $sql_tbl;
    if (is_null($id)) {
        return "error";
    }
    $query = mysql_query("DELETE FROM $sql_tbl[discounts] WHERE discountid=$id") or die(mysql_error());
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
    return array_to_json($json_result);
}

function array_to_json($array)
{
    return indent(json_encode($array));
}

/**
 * Indents a flat JSON string to make it more human-readable.
 *
 * @param string $json The original JSON string to process.
 *
 * @return string Indented version of the original JSON string.
 */
function indent($json)
{
    $result = '';
    $pos = 0;
    $strLen = strlen($json);
    $indentStr = '  ';
    $newLine = "\n";
    $prevChar = '';
    $outOfQuotes = true;

    for ($i = 0; $i <= $strLen; $i++) {

        // Grab the next character in the string.
        $char = substr($json, $i, 1);

        // Are we inside a quoted string?
        if ($char == '"' && $prevChar != '\\') {
            $outOfQuotes = !$outOfQuotes;

            // If this character is the end of an element,
            // output a new line and indent the next line.
        } else if (($char == '}' || $char == ']') && $outOfQuotes) {
            $result .= $newLine;
            $pos--;
            for ($j = 0; $j < $pos; $j++) {
                $result .= $indentStr;
            }
        }

        // Add the character to the result string.
        $result .= $char;

        // If the last character was the beginning of an element,
        // output a new line and indent the next line.
        if (($char == ',' || $char == '{' || $char == '[') && $outOfQuotes) {
            $result .= $newLine;
            if ($char == '{' || $char == '[') {
                $pos++;
            }

            for ($j = 0; $j < $pos; $j++) {
                $result .= $indentStr;
            }
        }

        $prevChar = $char;
    }

    return $result;
}