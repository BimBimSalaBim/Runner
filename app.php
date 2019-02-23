<?php
$myfile = fopen("app.txt", "w") or die("Unable to open file!");
$txt = $_GET['program'];
fwrite($myfile, $txt);
 sleep(5);
 echo "<script> location.href='reset.php'; </script>";
?>
