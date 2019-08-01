<?php

if( isset($_POST['sqlQuery']) ){

  $mysqli = new mysqli("sql1.5v.pl", "XXXXX", "XXXXX", "XXXXX");
  $result = $mysqli->query($_POST['sqlQuery']);
  $rows = array();
  while($r = mysqli_fetch_assoc($result)) {
      $rows[] = $r;
  }
  $rows = json_encode($rows);
  echo $rows;

}
?>
