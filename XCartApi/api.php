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
            $response = get_users(
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['size']),
                mysql_real_escape_string($_GET['sort'])
            );
            break;
        case 'last_order':
            $response = get_last_order();
            break;
        case 'discounts':
            $response = get_discounts();
            break;
        case 'create_discount':
            $response = create_discount(
                mysql_real_escape_string($_GET['minprice']),
                mysql_real_escape_string($_GET['discount']),
                mysql_real_escape_string($_GET['discount_type']),
                mysql_real_escape_string($_GET['provider']),
                mysql_real_escape_string($_GET['membership_id'])
            );
            break;
        case 'update_discount':
            $response = update_discount(
                mysql_real_escape_string($_GET['id']),
                mysql_real_escape_string($_GET['minprice']),
                mysql_real_escape_string($_GET['discount']),
                mysql_real_escape_string($_GET['discount_type']),
                mysql_real_escape_string($_GET['provider']),
                mysql_real_escape_string($_GET['membership_id'])
            );
            break;
        case 'delete_discount':
            $response = delete_discount(
                mysql_real_escape_string($_GET['id'])
            );
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
        case 'reviews':
            $response = get_reviews(
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['size'])
            );
            break;
        case 'delete_review':
            $response = delete_rewiew(
                mysql_real_escape_string($_GET['id'])
            );
            break;
        case 'add_rewiews':
            global $sql_tbl;
            for($i = 0; $i<20; $i++){
                $text = mysql_real_escape_string("Welcome to the World of Tanks Wiki.
Here you'll find detailed information on all of the tanks and other armored vehicles in World of Tanks. This wiki can help you become acquainted with the statistics, tactics, and general capabilities of the various tanks you own, plan to own, or encounter. In addition to the tank pages, you can find information about the equipment you can add to your tanks, skills your crew can learn, game mechanics, lingo, and more.
Please feel free to take in some of the 23,509 pages and 2,337 articles that have been written by players for players. If you wish to add to the Wiki to keep it growing and expanding you can register here to join, however no registration is required to read the Wiki.");
                $query = "
                INSERT INTO $sql_tbl[product_reviews] (remote_ip, email, message, productid)
                VALUES('123','Alex777', '$text', 17546)
               ";
                //echo $query;
            mysql_query($query) or die(mysql_error());
    }
            break;
        case 'user_orders':
            $response = get_orders_for_user(
                mysql_real_escape_string($_GET['user_id']),
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['size'])
            );
            break;
//        case 'add_fake':
//            for ($i = 0; $i < 1000; $i++) {
//                global $sql_tbl;
//                $login = 'login'.$i;
//                $name = 'username'.$i;
//                $query = mysql_query("
//                INSERT INTO $sql_tbl[customers]
//                SET $sql_tbl[customers].login='$name', $sql_tbl[customers].username='$login', $sql_tbl[customers].usertype='C'
//                ") or die(mysql_error());
//            }
//            break;
        case "sales":
            $response = get_sales(
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['until'])
            );
            break;
        case "sales_graph":
            $response = get_sales_graph(
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['until']),
                mysql_real_escape_string($_GET['w']),
                mysql_real_escape_string($_GET['h'])
            );
            break;
        case "products":
            $response = get_products(
                mysql_real_escape_string($_GET['search_word']),
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['size'])
            );
            break;
        case 'update_product':
            $response = update_product(
                mysql_real_escape_string($_GET['id']),
                mysql_real_escape_string($_GET['price'])
            );
            break;
        case 'delete_product':
            $response = delete_product(
                mysql_real_escape_string($_GET['id'])
            );
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

    $orders_count['gross_total'] = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition"));
    $orders_count['total_paid'] = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE (status='P' OR status='C') AND $date_condition"));

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
    $online = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[users_online]");
    $array = array(
        'registered' => $count,
        'online' => $online);
    return $array;
}

function get_users($from, $size, $sort)
{
    global $sql_tbl;
    if (!($from)) {
        $from = 0;
    }
    if (!($size)) {
        $size = 20;
    }
    if (!($sort)) {
        $sort = 'none';
    }
    switch ($sort) {
        case 'login_date':
            $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                ORDER BY $sql_tbl[customers].last_login desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        case 'order_date':
            $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider, orders.date,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                INNER JOIN (SELECT userid, MAX(date) as 'date' FROM $sql_tbl[orders] GROUP BY userid) as orders
                ON $sql_tbl[customers].id = orders.userid
                ORDER BY orders.date desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        case 'orders':
            $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                ORDER BY orders_count desc
                LIMIT $from, $size
                ") or die(mysql_error());
            break;
        default:
            $query = mysql_query("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
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

function get_orders_for_user($user_id, $from, $size)
{
    global $sql_tbl;
    if (!$user_id) {
        $user_id = 0;
    }
    if (!$from) {
        $from = 0;
    }
    if (!$size) {
        $size = 20;
    }
    $order_query = mysql_query("
        SELECT userid, orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
        FROM $sql_tbl[orders]
        WHERE userid=$user_id
        LIMIT $from, $size
        ") or die(mysql_error());
    $result_array = array();
    while ($row = mysql_fetch_assoc($order_query)) {
        $row['details'] = get_order_details($row[userid]);
        array_push($result_array, $row);
    }
    return array_to_json($result_array);
}

function get_last_order()
{
    global $sql_tbl;
    $order_query = mysql_query("
        SELECT orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
        FROM $sql_tbl[orders]
        ORDER BY date DESC LIMIT 1
        ") or die(mysql_error());
    $result_array = array();
    $row = mysql_fetch_assoc($order_query);
    foreach ($row as $key => $value) {
        $result_array[$key] = $value;
    }
    $order_id = $row['orderid'];
    $order_details = get_order_details($order_id);
    $result_array['details'] = $order_details;
    return array_to_json($result_array);
}

function get_order_details($id)
{
    if (!($id)) {
        return 'error';
    }
    global $sql_tbl;
    $query = mysql_query("SELECT productid, price, amount, provider, product_options, itemid, productcode, product FROM $sql_tbl[order_details] WHERE orderid=$id") or die(mysql_error());
    $order_details_array = array();
    while ($row = mysql_fetch_assoc($query)) {
        array_push($order_details_array, $row);
    }

    return $order_details_array;
}

function get_discounts($fields)
{
    global $sql_tbl;
    $query = mysql_query("
        SELECT $sql_tbl[discounts].discountid, minprice, discount, discount_type, provider, ifnull($sql_tbl[discount_memberships].membershipid, 'none') as 'membershipid'
        FROM $sql_tbl[discounts]
        LEFT JOIN $sql_tbl[discount_memberships]
        ON $sql_tbl[discounts].discountid = $sql_tbl[discount_memberships].discountid
        ") or die(mysql_error());
    return get_json($query);
}

function create_discount($minprice, $discount, $discount_type, $provider, $membership_id)
{
    global $sql_tbl;
    if (!($minprice) || !($discount) || !($discount_type) || !($provider)) {
        return "error";
    }
    $result;
    if ($membership_id != 1 && $membership_id != 2) {
        $result = mysql_query("
        INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider)
        VALUES ($minprice, $discount, '$discount_type', $provider)
        ") or die(mysql_error());
    } else {
        mysql_query("
            BEGIN;") or die(mysql_error());
        $result = mysql_query("
            INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider)
            VALUES ($minprice, $discount, '$discount_type', $provider);
            ") or die(mysql_error());
        mysql_query("
            INSERT INTO $sql_tbl[discount_memberships] (discountid, membershipid)
            VALUES (LAST_INSERT_ID(), $membership_id);
            ") or die(mysql_error());
        mysql_query("
            COMMIT;
            ") or die(mysql_error());
    }
    $answer = array(
        upload_status => (string)$result,
        upload_type => 'create',
        upload_data => 'discount',
        id => 'none'
    );
    return array_to_json($answer);
}

function update_discount($id, $minprice, $discount, $discount_type, $provider, $membership_id)
{
    global $sql_tbl;
    if (!($id) || !($minprice) || !($discount) || !($discount_type) || !($provider)) {
        return "error";
    }
    $result;
    if ($membership_id != 1 && $membership_id != 2) {
        mysql_query("
            DELETE FROM $sql_tbl[discount_memberships]
            WHERE discountid=$id
            ") or die(mysql_error());
        $result = mysql_query("
            UPDATE $sql_tbl[discounts]
            SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider
            WHERE discountid=$id
            ") or die(mysql_error());
    } else {
        mysql_query("
            BEGIN;") or die(mysql_error());
        $result = mysql_query("
             UPDATE $sql_tbl[discounts]
             SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider
             WHERE discountid=$id
          ") or die(mysql_error());
        mysql_query("
            DELETE FROM $sql_tbl[discount_memberships]
            WHERE discountid=$id
            ") or die(mysql_error());
        mysql_query("
            INSERT INTO $sql_tbl[discount_memberships] (discountid, membershipid)
            VALUES ($id, $membership_id);
            ") or die(mysql_error());
        mysql_query("
            COMMIT;
            ") or die(mysql_error());
    }

    $answer = array(
        upload_status => (string)$result,
        upload_type => 'update',
        upload_data => 'discount',
        id => $id
    );
    return array_to_json($answer);
}

function delete_discount($id)
{
    global $sql_tbl;
    if (!($id)) {
        return "error";
    }
    //TODO: fix sql injections
    $result = mysql_query("DELETE FROM $sql_tbl[discounts] WHERE discountid=$id") or die(mysql_error());
    $answer = array(
        upload_status => (string)$result,
        upload_type => 'delete',
        upload_data => 'discount',
        id => $id
    );
    return array_to_json($answer);
}

function get_reviews($from, $size)
{
    global $sql_tbl;
    if (!($size)) {
        $size = 20;
    }
    if (!$from) {
        $from = 0;
    }
    $query = mysql_query("
        SELECT $sql_tbl[product_reviews].review_id, $sql_tbl[product_reviews].productid, $sql_tbl[product_reviews].email, $sql_tbl[product_reviews].message, $sql_tbl[products_lng_current].product
        FROM $sql_tbl[product_reviews]
        INNER JOIN $sql_tbl[products_lng_current]
        ON $sql_tbl[product_reviews].productid = $sql_tbl[products_lng_current].productid
        LIMIT $from, $size
        ") or die(mysql_error());
    return get_json($query);
}

function delete_rewiew($id)
{
    global $sql_tbl;
    if (!($id)) {
        return "error";
    }
    //TODO: fix sql injections
    $result = mysql_query("DELETE FROM $sql_tbl[product_reviews] WHERE review_id=$id") or die(mysql_error());
    $answer = array(
        upload_status => (string)$result,
        upload_type => 'delete',
        upload_data => 'review',
        id => $id
    );
    return array_to_json($answer);
}

function get_sales($from, $until)
{
    global $sql_tbl, $config;
    $start_time = func_prepare_search_date($from) - $config['Appearance']['timezone_offset'];
    $end_time = func_prepare_search_date($until) - $config['Appearance']['timezone_offset'];

    $sales_array = array();
    for ($day_start = $start_time; $day_start < $end_time; $day_start += 24 * 3600) {
        $day_end = $day_start + 24 * 3600;
        $date_condition = "$sql_tbl[orders].date>='$day_start' AND $sql_tbl[orders].date<='$day_end'";
        $sales_array[(string)$day_start] = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition"));
    }
    return array_to_json($sales_array);
}

function get_sales_graph($from, $until, $width, $height)
{
    global $sql_tbl, $config;
    include "./libchart/libchart/classes/libchart.php";

    if(!$width){
        $width = 480;
    }
    if(!$height){
        $height = 300;
    }

    $start_time = func_prepare_search_date($from) - $config['Appearance']['timezone_offset'];
    $end_time = func_prepare_search_date($until) - $config['Appearance']['timezone_offset'];

    $chart = new LineChart($width, $height);
    $serie1 = new XYDataSet();

    $sales_array = array();
    for ($day_start = $start_time; $day_start < $end_time; $day_start += 24 * 3600) {
        $day_end = $day_start + 24 * 3600;
        $date_condition = "$sql_tbl[orders].date>='$day_start' AND $sql_tbl[orders].date<='$day_end'";
        $price = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition"));
        $serie1->addPoint(new Point(date("d, m", $day_start), $price));
    }

    $dataSet = new XYSeriesDataSet();
    $chart->setDataSet($serie1);
    $chart->setTitle("Sales");
    echo is_writable("./var/charts");
    //print_r($chart);
    $chart->render("./var/charts/tmp.png");
    $file = './var/charts/tmp.png';
    //header('Content-Type: image/png');
    //header('Content-Length: ' . filesize($file));
    echo file_get_contents($file);
}

function get_products($word, $from, $size)
{
    global $sql_tbl;
    if (!$size) {
        $size = 20;
    }
    if (!$from) {
        $from = 0;
    }
    $like = '%'.$word.'%';
    $query = mysql_query("
        SELECT $sql_tbl[products].*, $sql_tbl[products_lng_current].* FROM $sql_tbl[products]
        INNER JOIN $sql_tbl[products_lng_current]
        ON $sql_tbl[products_lng_current].productid = $sql_tbl[products].productid
        WHERE $sql_tbl[products_lng_current].product LIKE '$like'
        LIMIT $from, $size
        ") or die(mysql_error());
    $products_array = array();
    while ($row = mysql_fetch_assoc($query)) {
        array_push($products_array, $row);
    }
    return array_to_json($products_array);
}

function update_product($product_id, $price)
{
    global $sql_tbl;
    $result = mysql_query("
        UPDATE $sql_tbl[products]
        SET list_price=$price
        WHERE productid=$product_id
        ") or die(mysql_error());
    $answer = array(
        upload_status => (string)$result,
        upload_type => 'update',
        upload_data => 'product',
        id => $product_id
    );
    return array_to_json($answer);
}

function delete_product($product_id)
{
    global $sql_tbl;
    $result = mysql_query("
        DELETE FROM $sql_tbl[products]
        WHERE productid=$product_id
        ") or die(mysql_error());
    $answer = array(
        upload_status => (string)$result,
        upload_type => 'delete',
        upload_data => 'product',
        id => $product_id
    );
    return array_to_json($answer);
}

function get_json($query)
{
    $json_result = array();
    while ($row = mysql_fetch_assoc($query)) {
        array_push($json_result, $row);
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