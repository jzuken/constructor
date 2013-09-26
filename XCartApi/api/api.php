<?php

/*
 * Development imports
 */
require '../xcart/top.inc.php';
require '../xcart/init.php';

/*
 * Production imports
 */
//require '../top.inc.php';
//require '../init.php';

mysql_connect($sql_host, $sql_user, $sql_password) or die(mysql_error());
mysql_select_db($sql_db) or die(mysql_error());



$curtime = XC_TIME + $config['Appearance']['timezone_offset'];
$start_dates['last_login'] = $previous_login_date; // Since last login
$start_dates['today'] = func_prepare_search_date($curtime) - $config['Appearance']['timezone_offset']; // Today
$start_week = $curtime - date('w', $curtime) * 24 * 3600; // Week starts since Sunday
$start_dates['week'] = func_prepare_search_date($start_week) - $config['Appearance']['timezone_offset']; // Current week
$start_dates['month'] = mktime(0, 0, 0, date('m', $curtime), 1, date('Y', $curtime)) - $config['Appearance']['timezone_offset']; // Current month
$curtime = XC_TIME;

process_response();

function process_response()
{
    switch ($_GET['request']) {

        case 'login':
            login();
            break;

        case 'users':
            get_users();
            break;

        case 'last_order':
            get_last_order();
            break;

        case 'discounts':
            get_discounts();
            break;

        case 'create_discount':
            create_discount();
            break;

        case 'update_discount':
            update_discount();
            break;

        case 'delete_discount':
            delete_discount();
            break;

        case 'orders_statistic':
            get_orders_statistic();
            break;

        case 'top_products':
            get_top_products_statistic();
            break;

        case 'top_categories':
            get_top_categories_statistic();
            break;

        case 'reviews':
             get_reviews();
            break;

        case 'delete_review':
             delete_rewiew();
            break;

        case 'user_orders':
             get_orders_for_user();
            break;

        case "sales":
             get_sales();
            break;

        case "sales_graph":
            get_sales_graph(
                mysql_real_escape_string($_GET['from']),
                mysql_real_escape_string($_GET['until']),
                mysql_real_escape_string($_GET['w']),
                mysql_real_escape_string($_GET['h'])
            );
            break;

        case "products":
            get_products();
            break;

        case 'update_product':
            update_product();
            break;

        case 'delete_product':
            delete_product();
            break;

        case 'config':
            header('Content-Type: application/json; charset=utf-8');
            $file = file_get_contents('https://dl.dropboxusercontent.com/u/10802739/test.json');
            echo $file;
            echo md5($file);
            break;

        // new api

        case 'dashboard':

        default:
            print_error_message();
            break;
    }
}

//TODO: move sql to db_api class

function login()
{
    require_once '../xcart/include/login.php';
    require_once '../xcart/include/classes/class.XCPasswordHash.php';

//    require '../include/login.php';
//    require_once '../include/classes/class.XCPasswordHash.php';

    global $sql_tbl;

    $username = get_post_parameter('name');
    $password = get_post_parameter('pass');
    $udid = get_post_parameter('udid');

    $sql = "CREATE TABLE IF NOT EXISTS xcart_mobile_session
            (
                udid VARCHAR(255),
                sid VARCHAR(255),
                date int(11),
                configmd5 VARCHAR(32),
                PRIMARY KEY(udid)
            )";

        db_query($sql) or die(mysql_error());

    $usertype = 'P'; //TODO: P - in simple mode? in real xcart - A
    $user_data = func_query_first("SELECT * FROM $sql_tbl[customers] WHERE login='$username' AND usertype='$usertype'");

    if(!$user_data){
        print_error_message("Incorrect login");
        return;
    }

    $password = trim(stripslashes($password));
    $right_hash = text_decrypt($user_data['password']);
    $t_hasher = new XCPasswordHash();
    $is_correct = $t_hasher->CheckPassword($password, $right_hash);

    $answer = array(
        upload_type => 'login',
    );

    if ($is_correct) {
        $sid = uniqid('', true);
        db_query("UPDATE $sql_tbl[customers] SET last_login='" . XC_TIME . "' WHERE id='$user_data[id]'") or die(mysql_error());
        db_query("
            INSERT INTO xcart_mobile_session (sid, date, udid, configmd5)
            VALUES('$sid'," . time() . ",'$udid', '$config_md5')
            ON DUPLICATE KEY UPDATE sid='$sid', date=" . time()
        ) or die(mysql_error());

        $answer['upload_status'] = 'login success';
        $answer['sid'] = $sid;
    } else {
        print_error_message("Incorrect pass");
        return;
    }

    print_array_json($answer);
}

function get_dashboard_data(){


}

function get_orders_statistic()
{
    global $start_dates;

    $result_array = array();
    foreach ($start_dates as $key => $date) {
        $result_array[$key] = get_orders_count($date);
    }

    print_array_json($result_array);
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

    print_array_json($result_array);
}

function get_top_products($start_date)
{
    global $sql_tbl, $curtime;

    $max_top_sellers = 10;
    $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";

    $ordered_products = func_query
    ("
        SELECT $sql_tbl[order_details].productid, $sql_tbl[products].productcode, $sql_tbl[products_lng_current].product,
        SUM($sql_tbl[order_details].amount) AS count
        FROM $sql_tbl[orders]
        INNER JOIN $sql_tbl[order_details] USING (orderid)
        INNER JOIN $sql_tbl[products] ON $sql_tbl[order_details].productid=$sql_tbl[products].productid
        INNER JOIN $sql_tbl[products_lng_current] ON $sql_tbl[products_lng_current].productid=$sql_tbl[products].productid
        WHERE $date_condition
        AND $sql_tbl[orders].status NOT IN ('F','D')
        GROUP BY $sql_tbl[order_details].productid
        ORDER BY count DESC LIMIT 0, $max_top_sellers
    ");

    return $ordered_products;
}

function get_top_categories_statistic()
{
    global $start_dates;

    $result_array = array();
    foreach ($start_dates as $key => $date) {
        $result_array[$key] = is_array_check(get_top_categories($date));
    }

    print_array_json($result_array);
}

function get_top_categories($start_date)
{
    global $sql_tbl, $curtime;

    $max_top_sellers = 10;
    $date_condition = "$sql_tbl[orders].date>='$start_date' AND $sql_tbl[orders].date<='$curtime'";

    $categories = func_query
    ("
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
    ") ;

    return $categories;
}

function get_users()
{
    global $sql_tbl;

    $from = (int)get_get_parameter('from', 0);
    $size = (int)get_get_parameter('size', 20);
    $sort = get_get_parameter('sort', 'none');
    $sid = get_get_parameter('sid', '');

//    $sql = "SELECT COUNT(*) FROM xcart_mobile_session WHERE sid = '$sid'";
//    $result = get_first_cell($sql);
//    if ($result == 0) {
//        $answer = array(
//            upload_status => 'auth error',
//            upload_type => 'get',
//            upload_data => 'users',
//            id => '0'
//        );
//        print_array_json($answer);
//        return;
//    }

    switch ($sort) {

        case 'login_date':
            $query = mysql_query
            ("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                ORDER BY $sql_tbl[customers].last_login desc
                LIMIT $from, $size
            ") or die(mysql_error());
            break;

        case 'order_date':
            $query = mysql_query
            ("
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
            $query = mysql_query
            ("
                SELECT $sql_tbl[customers].id, $sql_tbl[customers].login, $sql_tbl[customers].username, $sql_tbl[customers].usertype, $sql_tbl[customers].invalid_login_attempts, $sql_tbl[customers].title, $sql_tbl[customers].firstname, $sql_tbl[customers].lastname, $sql_tbl[customers].company, $sql_tbl[customers].email, $sql_tbl[customers].url, $sql_tbl[customers].last_login, $sql_tbl[customers].first_login, $sql_tbl[customers].status, $sql_tbl[customers].language, $sql_tbl[customers].activity, $sql_tbl[customers].trusted_provider,
                (SELECT COUNT(*) FROM $sql_tbl[orders] WHERE $sql_tbl[orders].userid = $sql_tbl[customers].id) as 'orders_count'
                FROM $sql_tbl[customers]
                ORDER BY orders_count desc
                LIMIT $from, $size;
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
        $row[last_login] = gmdate("m-d-Y", $row['last_login']);
        array_push($users_array, $row);
    }

    $json_array = array(
        'users_count' => get_users_count(),
        'users' => $users_array
    );

    print_array_json($json_array);
}

function get_orders_for_user()
{
    global $sql_tbl;

    $user_id = (int)get_get_parameter('user_id', 0);
    $from = (int)get_get_parameter('from', 0);
    $size = (int)get_get_parameter('size', 20);

    $order_query = mysql_query
    ("
        SELECT userid, orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
        FROM $sql_tbl[orders]
        WHERE userid=$user_id
        LIMIT $from, $size
    ") or die(mysql_error());

    $result_array = array();
    while ($row = mysql_fetch_assoc($order_query)) {
        $row['details'] = get_order_details($row[orderid]);
        $row['date'] = gmdate("m-d-Y", $row['date']);
        array_push($result_array, $row);
    }

    print_array_json($result_array);
}

function get_last_order()
{
    global $sql_tbl;

    $order_query = mysql_query
    ("
        SELECT orderid, status, total, title, firstname, b_firstname, lastname, b_lastname, date
        FROM $sql_tbl[orders]
        ORDER BY date DESC LIMIT 1
    ") or die(mysql_error());

    $result_array = mysql_fetch_assoc($order_query);
    $result_array['date'] = gmdate("m-d-Y", $result_array['date']);
    $order_id = $result_array['orderid'];
    $result_array['details'] = get_order_details($order_id);

    print_array_json($result_array);
}

function get_order_details($id)
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

function get_discounts()
{
    global $sql_tbl;

    $query = mysql_query
    ("
        SELECT $sql_tbl[discounts].discountid, minprice, discount, discount_type, provider, ifnull($sql_tbl[discount_memberships].membershipid, 'none') as 'membershipid'
        FROM $sql_tbl[discounts]
        LEFT JOIN $sql_tbl[discount_memberships]
        ON $sql_tbl[discounts].discountid = $sql_tbl[discount_memberships].discountid
    ") or die(mysql_error());

    print_array_json(get_json_array($query));
}

function create_discount()
{
    global $sql_tbl;

    $minprice = (float)get_get_parameter('minprice', null);
    $discount = (float)get_get_parameter('discount', null);
    $discount_type = get_get_parameter('discount_type');
    $provider = get_get_parameter('provider');
    $membership_id = (int)get_get_parameter('membership_id');

    if (
        !$minprice ||
        !$discount ||
        !$discount_type ||
        !$provider
    ) {
        print_error_messge('Some parameters are not found');
        return;
    }

    if ($membership_id != 1 && $membership_id != 2) {
        $result = mysql_query
        ("
          INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider)
          VALUES ($minprice, $discount, '$discount_type', $provider)
        ") or die(mysql_error());
    } else {
        mysql_query("BEGIN;") or die(mysql_error());

        $result = mysql_query
        ("
            INSERT INTO $sql_tbl[discounts] (minprice, discount, discount_type, provider)
            VALUES ($minprice, $discount, '$discount_type', $provider);
        ") or die(mysql_error());

        mysql_query
        ("
            INSERT INTO $sql_tbl[discount_memberships] (discountid, membershipid)
            VALUES (LAST_INSERT_ID(), $membership_id);
        ") or die(mysql_error());

        mysql_query("COMMIT;") or die(mysql_error());
    }
    $answer = array(
        'upload_status' => (string)$result,
        'upload_type' => 'create',
        'upload_data' => 'discount',
        'id' => 'none'
    );
    print_array_json($answer);
}

function update_discount()
{
    global $sql_tbl;

    $id = (int)get_get_parameter('id', null);
    $minprice = (float)get_get_parameter('minprice', null);
    $discount = (float)get_get_parameter('discount', null);
    $discount_type = get_get_parameter('discount_type', 'percent');
    $provider = get_get_parameter('provider', 1);
    $membership_id = (int)get_get_parameter('membership_id', 0);

    if (
        !$id ||
        !$minprice ||
        !$discount ||
        !$discount_type ||
        !$provider
    ) {
        print_error_messge("Some parameters are not found");
        return;
    }

    if (
        $membership_id != 1 &&
        $membership_id != 2
    ) {
        mysql_query
        ("
            DELETE FROM $sql_tbl[discount_memberships]
            WHERE discountid=$id
        ") or die(mysql_error());

        $result = mysql_query
        ("
            UPDATE $sql_tbl[discounts]
            SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider
            WHERE discountid=$id
        ") or die(mysql_error());
    } else {
        mysql_query("BEGIN;") or die(mysql_error());

        $result = mysql_query
        ("
             UPDATE $sql_tbl[discounts]
             SET minprice=$minprice, discount=$discount, discount_type='$discount_type', provider=$provider
             WHERE discountid=$id
        ") or die(mysql_error());

        mysql_query
        ("
            DELETE FROM $sql_tbl[discount_memberships]
            WHERE discountid=$id
        ") or die(mysql_error());

        mysql_query
        ("
            INSERT INTO $sql_tbl[discount_memberships] (discountid, membershipid)
            VALUES ($id, $membership_id);
        ") or die(mysql_error());

        mysql_query("COMMIT;") or die(mysql_error());
    }

    $answer = array(
        'upload_status' => (string)$result,
        'upload_type' => 'update',
        'upload_data' => 'discount',
        'id' => $id
    );

    print_array_json($answer);
}

function delete_discount()
{
    global $sql_tbl;

    $id = (int)get_get_parameter('id', null);

    if (!$id) {
        print_error_messge("Id is not found");
        return;
    }

    //TODO: fix sql injections
    $result = mysql_query("DELETE FROM $sql_tbl[discounts] WHERE discountid=$id") or die(mysql_error());
    $answer = array(
        'upload_status' => (string)$result,
        'upload_type' => 'delete',
        'upload_data' => 'discount',
        'id' => $id
    );

    print_array_json($answer);
}

function get_reviews()
{
    global $sql_tbl;

    $from = (int)get_get_parameter('from', 0);
    $size = (int)get_get_parameter('size', 20);

    $query = mysql_query
    ("
        SELECT $sql_tbl[product_reviews].review_id, $sql_tbl[product_reviews].productid, $sql_tbl[product_reviews].email, $sql_tbl[product_reviews].message, $sql_tbl[products_lng_current].product
        FROM $sql_tbl[product_reviews]
        INNER JOIN $sql_tbl[products_lng_current]
        ON $sql_tbl[product_reviews].productid = $sql_tbl[products_lng_current].productid
        LIMIT $from, $size
    ") or die(mysql_error());

    print_array_json(get_json_array($query));
}

function delete_rewiew()
{
    global $sql_tbl;

    $id = get_get_parameter('id', null);

    if (!($id)) {
        print_error_messge("Id is not found");
        return;
    }

    //TODO: fix sql injections
    $result = mysql_query("DELETE FROM $sql_tbl[product_reviews] WHERE review_id=$id") or die(mysql_error());
    $answer = array(
        'upload_status' => (string)$result,
        'upload_type' => 'delete',
        'upload_data' => 'review',
        'id' => $id
    );

    print_array_json($answer);
}

function get_sales()
{
    global $sql_tbl, $config;

    $from = (int)get_get_parameter('from', 0);
    $until = (int)get_get_parameter('until', 0);

    $start_time = func_prepare_search_date($from) - $config['Appearance']['timezone_offset'];
    $end_time = func_prepare_search_date($until) - $config['Appearance']['timezone_offset'];

    $sales_array = array();
    for ($day_start = $start_time; $day_start < $end_time; $day_start += 24 * 3600) {
        $day_end = $day_start + 24 * 3600;
        $date_condition = "$sql_tbl[orders].date>='$day_start' AND $sql_tbl[orders].date<='$day_end'";
        $sales_array[(string)$day_start] = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition"));
    }

    print_array_json($sales_array);
}

//function get_sales_graph($from, $until, $width, $height)
//{
//    global $sql_tbl, $config;
//    include "./libchart/libchart/classes/libchart.php";
//
//    if (!$width) {
//        $width = 480;
//    }
//    if (!$height) {
//        $height = 300;
//    }
//
//    $start_time = func_prepare_search_date($from) - $config['Appearance']['timezone_offset'];
//    $end_time = func_prepare_search_date($until) - $config['Appearance']['timezone_offset'];
//
//    $chart = new LineChart($width, $height);
//    $serie1 = new XYDataSet();
//
//    $sales_array = array();
//    for ($day_start = $start_time; $day_start < $end_time; $day_start += 24 * 3600) {
//        $day_end = $day_start + 24 * 3600;
//        $date_condition = "$sql_tbl[orders].date>='$day_start' AND $sql_tbl[orders].date<='$day_end'";
//        $price = price_format(get_first_cell("SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition"));
//        $serie1->addPoint(new Point(date("d, m", $day_start), $price));
//    }
//
//    $dataSet = new XYSeriesDataSet();
//    $chart->setDataSet($serie1);
//    $chart->setTitle("Sales");
//    echo is_writable("./var/charts");
//    //print_r($chart);
//    $chart->render("./var/charts/tmp.png");
//    $file = './var/charts/tmp.png';
//    //header('Content-Type: image/png');
//    //header('Content-Length: ' . filesize($file));
//    echo file_get_contents($file);
//}

function get_products()
{
    global $sql_tbl;

    $from = (int)get_get_parameter('from', 0);
    $size = (int)get_get_parameter('size', 20);
    $word = get_get_parameter('search_word', '');

    $like = '%' . $word . '%';

    $query = mysql_query
    ("
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

    print_array_json($products_array);
}

function update_product()
{
    global $sql_tbl;

    $product_id = (int)get_get_parameter('id', 0);
    $price = (float)get_get_parameter('price');

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

    print_array_json($answer);
}

function delete_product()
{
    global $sql_tbl;

    $product_id = (int)get_get_parameter('id', 0);

    $result = mysql_query("
        DELETE FROM $sql_tbl[products]
        WHERE productid=$product_id
        ") or die(mysql_error());

    $answer = array(
        'upload_status' => (string)$result,
        'upload_type' => 'delete',
        'upload_data' => 'product',
        'id' => $product_id
    );

    print_array_json($answer);
}

function get_users_count()
{
    global $sql_tbl;

    $count = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[customers]");
    $online = get_first_cell("SELECT COUNT(*) FROM $sql_tbl[users_online]");

    $array = array(
        'registered' => $count,
        'online' => $online
    );

    return $array;
}

function print_array_json($array)
{
    header('Content-Type: application/json; charset=utf-8');
    echo indent(json_encode($array));
}

function print_error_message($message = 'error')
{
    header('Content-Type: application/json; charset=utf-8');
    print_array_json(
        array(
            'error_message' => $message
        )
    );
}

function get_json_array($query)
{
    $json_result = array();
    while ($row = mysql_fetch_assoc($query)) {
        array_push($json_result, $row);
    }

    return $json_result;
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

function get_get_parameter($name, $default = '')
{
    if (!$_GET[$name]) {
        return $default;
    }
    return mysql_real_escape_string($_GET[$name]);
}

function get_post_parameter($name, $default = '')
{
    if (!$_POST[$name]) {
        return $default;
    }
    return mysql_real_escape_string($_POST[$name]);
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