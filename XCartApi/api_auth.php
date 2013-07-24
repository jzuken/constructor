<?php echo htmlspecialchars($_POST['name']); ?>
<?php echo $_POST['pass']; ?>
<?php
echo
list($is_password_correct, $password_has_old_format) = func_is_password_correct($_POST['pass'], 'abcd', true);
echo $is_password_correct;