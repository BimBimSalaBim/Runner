<?php
$myfile = fopen("app.txt", "w") or die("Unable to open file!");
$txt = '';
fwrite($myfile, $txt);
?>
