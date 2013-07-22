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
$orders_fields = array(orderid, userid, membership, total, giftcert_discount, giftcert_ids, subtotal, discount, coupon, coupon_discount, shippingid, shipping, tracking, shipping_cost, tax, taxes_applied, date, status, payment_method, flag, notes, details, customer_notes, customer, title, firstname, lastname, company, url, email, language, clickid, membershipid, paymentid, payment_surcharge, tax_number, tax_exempt, init_total, access_key, klarna_order_status);
$discounts_fields = array(discountid, minprice, discount, discount_type, provider);

echo get_response();

function get_response()
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

        default:
            $response = "error";
            break;
    }
    return $response;
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
    global $sql_tbl;
    $query = mysql_query("SELECT * FROM $sql_tbl[orders] ORDER BY date DESC LIMIT 1") or die(mysql_error());
    return get_json($fields, $query);
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
        foreach ($fields as &$value) {
            $order_json[$value] = $row[$value];
        }
        array_push($json_result, $order_json);
    }
    return json_encode($json_result);
}