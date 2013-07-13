<?php

/*
 * Development imports
 */
require './xcart/top.inc.php';
require './xcart/init.php';

/*
 * Production imports
 */
//require '../top.inc.php';
//require '../init.php';

mysql_connect($sql_host, $sql_user, $sql_password)  or die(mysql_error());
mysql_select_db($sql_db) or die(mysql_error());


$type = $_GET['type'];
if (is_null($type)) {
    $type = "xml";
}
if ($type == "xml") {
    header('Content-type: text/xml');
}

$users_fields = array(id, login, username, usertype, password, signature, invalid_login_attempts, title, firstname, lastname, company, email, url, last_login, first_login, status, activation_key, autolock, suspend_date, referer, ssn, language, cart, change_password, change_password_date, parent, pending_plan_id, activity, membershipid, pending_membershipid, tax_number, tax_exempt, trusted_provider, cookie_access);
$orders_fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, url, email, language, clickid, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);
$discounts_fields = array(discountid, minprice, discount, discount_type, provider);

echo get_response($type);

function get_response($type)
{
    global $users_fields, $orders_fields, $discounts_fields;
    $request = $_GET['request'];
    switch ($request) {
        case 'users_count':
            $response = get_users_count();
            break;
        case 'users':
            $response = get_users($users_fields);
            break;
        case 'orders':
            $response = get_orders($orders_fields, $_GET['from'], $_GET['to'], $type);
            break;
        case 'last_order':
            $response = get_last_order($orders_fields, $type);
            break;
        case 'discounts':
            $response = get_discounts($discounts_fields);
            break;
        default:
            $response = new SimpleXMLElement('<error/>');
            break;
    }
    return $response;
}

function get_users_count()
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[customers]");
    $count = mysql_num_rows($query);
    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    return $xml;
}

function get_users($fields)
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[customers]");
    $count = mysql_num_rows($query);
    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    while ($row = mysql_fetch_array($query)) {
        $user = $xml->addChild('user');
        foreach ($fields as &$value) {
            $user->addChild($value, $row[$value]);
        }
    }

    return $xml;
}

function get_orders($fields, $from, $to, $type)
{
    if (is_null($from)) {
        $from = 0;
    }
    if (is_null($to)) {
        $to = XC_TIME;
    }
    global $sql_tbl;
    $date_condition = "date BETWEEN $from AND $to";
    $query = mysql_query("SELECT * FROM $sql_tbl[orders] WHERE $date_condition");

    switch ($type) {
        case "json":
            $response = get_json($fields, $query);
            break;
        default:
            $response = get_xml($fields, $query, 'order');
            break;
    }
    return $response;
}

function get_last_order($fields, $type)
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[orders] ORDER BY date DESC LIMIT 1");

    switch ($type) {
        case "json":
            $response = get_json($fields, $query);
            break;
        default:
            $response = get_xml($fields, $query, 'order');
            break;
    }
    return $response;
}


function get_discounts($fields)
{
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[discounts]") or die(mysql_error());
    $xml = new SimpleXMLElement('<discounts/>');
    while ($row = mysql_fetch_array($query)) {
        $discount = $xml->addChild('discount');
        foreach ($fields as &$value) {
            $discount->addChild($value, $row[$value]);
        }
    }
    return $xml;
}

function get_json($fields, $query)
{
    $json_result = array();
    while ($row = mysql_fetch_array($query)) {
        $order_json = array();
        foreach ($fields as &$value) {
            $order_json[$value] = $row[$value];
        }
        array_push($json_result, $order_json);
    }
    return json_encode($json_result);
}

function get_xml($fields, $query, $row_title)
{
    $xml = new SimpleXMLElement('<root/>');
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild($row_title);
        foreach ($fields as &$value) {
            $order->addChild($value, $row[$value]);
        }
    }
    return $xml->asXML();
}