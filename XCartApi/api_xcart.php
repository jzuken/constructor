<?php

require_once './api_settings.php';

header('Content-type: text/xml');

mysql_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD) or die("<p>" . mysql_error() . "</p>");
mysql_select_db(DATABASE_NAME);

$request = $_REQUEST['request'];
$xml = new SimpleXMLElement('<error/>');
switch ($request) {
    case 'users_count':
        $xml = get_users_count();
        break;
    case 'users':
        $xml = get_users();
        break;
    case 'orders':
        if ($_REQUEST['from'] && $_REQUEST['to']) {
            $xml = get_orders($_REQUEST['from'], $_REQUEST['to']);
        } else {
            $xml = get_orders();
        }
        break;
    case 'last_order':
        $xml = get_last_order();
        break;
    case 'discounts':
        $xml = get_discounts();
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

function get_users()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[customers];";
    $query = mysql_query($sql) or die(mysql_error());
    $count = mysql_num_rows($query);
    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    while ($row = mysql_fetch_array($query)) {
        $user = $xml->addChild('user');
        $fields = array(id, login, username, usertype, password, signature, invalid_login_attempts, title, firstname, lastname, company, email, url, last_login, first_login, status, activation_key, autolock, suspend_date, referer, ssn, language, cart, change_password, change_password_date, parent, pending_plan_id, activity, membershipid, pending_membershipid, tax_number, tax_exempt, trusted_provider, cookie_access);
        foreach ($fields as &$value) {
            $user->addChild($value, $row[$value]);
        }
    }

    return $xml;
}

function get_orders($from = 0, $to = PHP_INT_MAX)
{
    global $sql_tbl;
    $date_condition = "date BETWEEN $from AND $to";
    $sql_orders = "SELECT * FROM $sql_tbl[orders] WHERE $date_condition;";
    //$total_gross = "SELECT SUM(total) FROM $sql_tbl[orders] WHERE $date_condition;";
    $query = mysql_query($sql_orders) or die(mysql_error());
    $xml = new SimpleXMLElement('<orders/>');
    //$xml->addAttribute('total_gross', $total_gross);
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild('order');
        $fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, b_title, b_firstname, b_lastname, b_address, b_city, b_county, b_state, b_country, b_zipcode, b_zip4, b_phone, b_fax, s_title, s_firstname, s_lastname, s_address, s_city, s_county, s_state, s_country, s_zipcode, s_phone, s_fax, s_zip4, url, email, language, clickid, extra, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);
        foreach ($fields as &$value) {
            $order->addChild($value, $row[$value]);
        }
    }
    return $xml;
}

function get_last_order()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[orders] ORDER BY date DESC LIMIT 1";
    $query = mysql_query($sql) or die(mysql_error());
    $xml = new SimpleXMLElement('<orders/>');
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild('order');
        $fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, b_title, b_firstname, b_lastname, b_address, b_city, b_county, b_state, b_country, b_zipcode, b_zip4, b_phone, b_fax, s_title, s_firstname, s_lastname, s_address, s_city, s_county, s_state, s_country, s_zipcode, s_phone, s_fax, s_zip4, url, email, language, clickid, extra, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);
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