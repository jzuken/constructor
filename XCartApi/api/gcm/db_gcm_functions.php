<?php

class DB_Functions
{

    private $db;

    function __construct()
    {
        include_once '../db_connect.php';

        $this->db = new DB_Connect();
        $this->db->connect();
        $this->createGcmTable();
    }

    function __destruct()
    {
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($gcm_regid)
    {

        $result = mysql_query("INSERT INTO xcart_gcm_users(gcm_regid, created_at) VALUES('$gcm_regid', NOW())");
        if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM xcart_gcm_users WHERE id = $id") or die(mysql_error());
            // return user details
            if (mysql_num_rows($result) > 0) {
                return mysql_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public function getAllUsers()
    {
        $result = mysql_query("select * FROM gcm_users");
        return $result;
    }

    private function createGcmTable()
    {
        $sql = "CREATE TABLE IF NOT EXISTS `xcart_gcm_users`
            (
                `id` int(11) NOT NULL AUTO_INCREMENT,
                `gcm_regid` text,
                `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (`id`)
            )";
        mysql_query($sql);
    }
}

?>