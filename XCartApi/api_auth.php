<?php
require './xcart/top.inc.php';
require './xcart/init.php';

require './xcart/include/login.php';
require './xcart/include/common.php';
require_once './xcart/include/classes/class.XCPasswordHash.php';

//echo htmlspecialchars($_POST['name']);
//echo $_POST['pass'];



$username = trim($_POST['name']);
$password = trim($_POST['pass']);

$usertype = 'P';//TODO: P - in simple mode? in real xcart - A
$query = "SELECT * FROM $sql_tbl[customers] WHERE login='$username' AND usertype='$usertype'";
$user_data = func_query_first($query);
echo empty($user_data['usertype']);
$allow_old_password_format = true;
$password = trim(stripslashes($password));
$right_hash = text_decrypt($crypted);
$t_hasher = new XCPasswordHash();
print_r($t_hasher);
$is_correct = $t_hasher->CheckPassword($password, $right_hash);
echo $is_correct;
print_r($is_correct);
echo '<br>';
