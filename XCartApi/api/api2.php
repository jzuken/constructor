<?php

include_once './db_api_functions.php';
include_once './json_printer.php';

$db = new DbApiFunctions();
$pr = new JsonPrinter();

if (isset($_GET["request"])) {

    switch ($_GET['request']) {

        case 'login':
            $username = get_post_parameter('name');
            $password = get_post_parameter('pass');
            $udid = get_post_parameter('udid');

            $array = $db->login($username, $password, $udid);

            $pr->print_array_json($array);
            break;

        case 'dashboard':
            $array = $db->get_dashboard_data();
            $last_orders = $db->get_orders_list(0, 3, 'today', null);
            $array['last_orders'] = $last_orders;
            $pr->print_array_json($array);
            break;

        case 'last_orders':
            $array = $db->get_orders_list(
                (int)get_get_parameter('from', 0),
                (int)get_get_parameter('size', 5),
                get_get_parameter('date', null),
                get_get_parameter('status', null)
            );
            $pr->print_array_json($array);
            break;

        case 'order_info':
            $array = $db->get_order_info(
                (int)get_get_parameter('id', 0)
            );
            $pr->print_array_json($array);
            break;

        case 'change_tracking':
            $array = $db->change_tracking_number(
                (int)get_get_parameter('tracking_number', null),
                (int)get_get_parameter('order_id', -1)
            );
            $pr->print_array_json($array);
            break;

        case 'change_status':
            $array = $db->change_status(
                get_get_parameter('status', "Q"),
                (int)get_get_parameter('order_id', -1)
            );
            $pr->print_array_json($array);
            break;

        case 'products':
            $array = $db->get_products(
                (int)get_get_parameter('from', 0),
                (int)get_get_parameter('size', 5),
                get_get_parameter('low_stock', null)
            );
            $pr->print_array_json($array);
            break;

        case 'product_info':
            $array = $db->get_product_info(
                (int)get_get_parameter('id', 0)
            );
            $pr->print_array_json($array);
            break;

        case 'update_product_price':
            $array = $db->update_product_price(
                (int)get_get_parameter('id', 0),
                (float)get_get_parameter('price')
            );
            $pr->print_array_json($array);
            break;

        case 'reviews':
            $array = $db->get_reviews(
                (int)get_get_parameter('from', 0),
                (int)get_get_parameter('size', 20)
            );
            $pr->print_array_json($array);
            break;

        case 'delete_review':
            $array = $db->delete_review(
                get_get_parameter('id', null)
            );
            $pr->print_array_json($array);
            break;

        case 'users':
            $array = $db->get_users(
                (int)get_get_parameter('from', 0),
                (int)get_get_parameter('size', 20)
            );
            $pr->print_array_json($array);
            break;

        case 'user_info':
            $array = $db->get_user_info(
                (int)get_get_parameter('id', -1)
            );
            $pr->print_array_json($array);
            break;
    }

}

function check_session($sid)
{
    $sql = "SELECT COUNT(*) FROM xcart_mobile_session WHERE sid = '$sid'";
    $result = get_first_cell($sql);
    return $result > 0;
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