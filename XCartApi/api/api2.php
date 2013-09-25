<?php

include_once './db_api_functions.php';
include_once './json_printer.php';

$db = new DbApiFunctions();
$pr = new JsonPrinter();

if (isset($_GET["request"])) {

    switch ($_GET['request']) {

        case 'dashboard':
            $array = $db->get_dashboard_data();
            $pr->print_array_json($array);
            break;

        case 'last_orders':
            $array = $db->get_last_orders(
                get_get_parameter('from', 0),
                get_get_parameter('size', 5)
            );
            $pr->print_array_json($array);
            break;

    }

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