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

header('Content-type: text/xml');

$request = $_GET['request'];
mysql_connect($sql_host, $sql_user, $sql_password)  or die(mysql_error());
mysql_select_db($sql_db) or die(mysql_error());

$users_fields = array(id, login, username, usertype, password, signature, invalid_login_attempts, title, firstname, lastname, company, email, url, last_login, first_login, status, activation_key, autolock, suspend_date, referer, ssn, language, cart, change_password, change_password_date, parent, pending_plan_id, activity, membershipid, pending_membershipid, tax_number, tax_exempt, trusted_provider, cookie_access);
$orders_fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, url, email, language, clickid, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);

switch ($request) {
    case 'users_count':
        $xml = get_users_count();
        break;
    case 'users':
        $xml = get_users($users_fields);
        break;
    case 'orders':
        $xml = get_orders($orders_fields, $_REQUEST['from'], $_REQUEST['to']);
        break;
    case 'last_order':
        $xml = get_last_order($orders_fields);
        break;
    case 'discounts':
        $xml = get_discounts();
        break;
    default:
        $xml = new SimpleXMLElement('<error/>');
        break;
}
echo $xml->asXML();

function get_users_count()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[customers];";
    $query = mysql_query($sql) or die(mysql_error());
    $count = mysql_num_rows($query);
    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    return $xml;
}

function get_users($fields)
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[customers];";
    $query = mysql_query($sql) or die(mysql_error());
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

function get_orders($fields, $from, $to)
{
    if(is_null($from)){
        $from = 0;
    }
    if(is_null($to)){
        $to = XC_TIME;
    }
    global $sql_tbl;
    $date_condition = "date BETWEEN $from AND $to";
    $sql_orders = "SELECT * FROM $sql_tbl[orders] WHERE $date_condition;";
    $query = mysql_query($sql_orders) or die(mysql_error());
    $xml = new SimpleXMLElement('<orders/>');
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild('order');
        foreach ($fields as &$value) {
            $order->addChild($value, $row[$value]);
        }
    }
    return $xml;
}

function get_last_order($fields)
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[orders] ORDER BY date DESC LIMIT 1";
    $query = mysql_query($sql) or die(mysql_error());
    $xml = new SimpleXMLElement('<orders/>');
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild('order');
        foreach ($fields as &$value) {
            $order->addChild($value, $row[$value]);
        }
    }
    return $xml;
}

function get_discounts()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[discounts]";
    $query = mysql_query($sql) or die(mysql_error());
    $xml = new SimpleXMLElement('<discounts/>');
    while ($row = mysql_fetch_array($query)) {
        $discount = $xml->addChild('discount');
        $fields = array(discountid, minprice, discount, discount_type, provider);
        foreach ($fields as &$value) {
            $discount->addChild($value, $row[$value]);
        }
    }
    return $xml;
}