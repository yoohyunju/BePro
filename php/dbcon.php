<?php

  $db_host = 'localhost'; //DB server address
  $db_user = 'beProAdmin'; //mysql id
  $db_password = '123456'; //mysql password
  $db_name = 'bePro'; //DB name

  //DB 접속
  $conn = mysqli_connect($db_host, $db_user, $db_password, $db_name);
  if(mysqli_connect_errno()){
    echo "DB conn error";
    exit;
  } else {
    //echo "DB conn success";
  }

  mysqli_set_charset($conn, "utf8");



 ?>
