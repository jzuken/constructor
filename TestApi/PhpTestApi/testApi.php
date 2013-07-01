<?php
$dbhost = "localhost";
$dbuser = "root";
$dbpass = "master91";
$dbname = "xcart";
mysql_connect($dbhost, $dbuser, $dbpass);
mysql_select_db($dbname);


$sql = "SELECT * FROM xcart_variants";
$q = mysql_query($sql) or die(mysql_error());
$xml = "<variants>";
while ($r = mysql_fetch_array($q)) {
    $xml .= "<variant>\n";
    $xml .= "<variantid>" . $r['variantid'] . "</variantid>\n";
    $xml .= "<productid>" . $r['productid'] . "</productid>\n";
    $xml .= "<avail>" . $r['avail'] . "</avail>\n";
    $xml .= "<weight>" . $r['weight'] . "</weight>\n";
    $xml .= "<productcode>" . $r['productcode'] . "</productcode>\n";
    $xml .= "<def>" . $r['def'] . "</def>\n";
    $xml .= "<is_product_row>" . $r['is_product_row'] . "</is_product_row>\n";
    $xml .= "</variant>\n";
}
$xml .= "</variants>";
$sxe = new SimpleXMLElement($xml);
echo $sxe

?>