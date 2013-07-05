<?php

require_once './api_settings.php';

header('Content-type: text/xml');

mysql_connect(DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD) or die("<p>" . mysql_error() . "</p>");
mysql_select_db(DATABASE_NAME);

$request = $_REQUEST['request'];
switch ($request) {
    case 'users_count':
        getUsersCount();
        break;
}

function getUsersCount()
{
    global $sql_tbl;
    $sql = "SELECT * FROM $sql_tbl[customers];";
    $query = mysql_query($sql) or die(mysql_error());
    $count = mysql_num_rows($query);

    $xml = new SimpleXMLElement('<users/>');
    $xml->addAttribute('count', $count);
    echo $xml->asXML();
}