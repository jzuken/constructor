<?php

$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_POST["regId"])) {
    $gcm_regid = $_POST["regId"];

    include_once './db_gcm_functions.php';
    include_once './GCM.php';


    $db = new DB_Functions();
    $gcm = new GCM();

    $res = $db->storeUser($gcm_regid);

    $registatoin_ids = array($gcm_regid);
    $message = array("test" => "test");

    $result = $gcm->send_notification($registatoin_ids, $message);

    echo $result;
} else {
    // user details missing
}
?>