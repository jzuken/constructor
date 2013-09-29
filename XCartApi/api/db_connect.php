<?php

class DB_Connect {
 
    function __construct() {
 
    }
 
    function __destruct() {
        // $this->close();
    }
 
    public function connect($host, $user, $password, $database) {
        $con = mysql_connect($host, $user, $password);
        mysql_select_db($database);

        return $con;
    }
 
    public function close() {
        mysql_close();
    }
 
} 
?>