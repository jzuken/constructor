<?php

require_once './api_settings.php';

header('Content-type: text/xml');

mysql_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD) or die("<p>" . mysql_error() . "</p>");
mysql_select_db(DATABASE_NAME);

$request = $_REQUEST['request'];
switch ($request) {
    case 'users_count':
        get_users_count();
        break;
    case 'orders':
        get_orders($_REQUEST['from'], $_REQUEST['to']);
        break;
}

function get_users_count()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[customers];";
    $query = mysql_query($sql) or die(mysql_error());
    $count = mysql_num_rows($query);

    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    echo $xml->asXML();
}

function get_orders($from, $to)
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[orders] WHERE date BETWEEN $from AND $to;";
    $query = mysql_query($sql) or die(mysql_error());
    $xml = new SimpleXMLElement('<orders/>');
    while ($row = mysql_fetch_array($query)) {
        $order = $xml->addChild('order');
        $fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, b_title, b_firstname, b_lastname, b_address, b_city, b_county, b_state, b_country, b_zipcode, b_zip4, b_phone, b_fax, s_title, s_firstname, s_lastname, s_address, s_city, s_county, s_state, s_country, s_zipcode, s_phone, s_fax, s_zip4, url, email, language, clickid, extra, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);
        foreach ($fields as &$value) {
            $order->addChild($value, $row[$value]);
        }
    }
    echo $xml->asXML();
}