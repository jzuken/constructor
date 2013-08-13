<?php
//require './xcart/top.inc.php';
//require './xcart/init.php';
//
//require './xcart/include/login.php';
//require_once './xcart/include/classes/class.XCPasswordHash.php';

require './top.inc.php';
require './init.php';

require './include/login.php';
require_once './include/classes/class.XCPasswordHash.php';

$username = trim($_POST['name']);
$password = trim($_POST['pass']);

$usertype = 'P'; //TODO: P - in simple mode? in real xcart - A
$query = "SELECT * FROM $sql_tbl[customers] WHERE login='$username' AND usertype='$usertype'";
$user_data = func_query_first($query);
$allow_old_password_format = true;
$password = trim(stripslashes($password));
$right_hash = text_decrypt($user_data['password']);
$t_hasher = new XCPasswordHash();
$is_correct = $t_hasher->CheckPassword($password, $right_hash);
if($is_correct){
    echo '<p>correct!</p>';
}else{
    echo '<p>WRONG!</p>';
}