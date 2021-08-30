<?php
  //에러 출력
  error_reporting(E_ALL);
  ini_set('display_errors', '1');

  include('dbcon.php');
  mysqli_set_charset($conn, "utf8");
  
  //일단은 품목명, 개수, 기한만 수정 가능하게 하였음.
  //안드에서 POST로 보낸 값을 받아서 변수에 저장
  $foodIdx = $_POST['foodIdx'];
  $foodName = $_POST['foodName'];
  $foodNum = $_POST['foodNum'];
  //$foodExp = $_POST['foodExp'];

  $statement = mysqli_prepare($conn, "UPDATE food SET food_name = ?, food_number = ? WHERE food_idx = ?");

  $bind = mysqli_stmt_bind_param($statement, "ssi", $foodName, $foodNum, $foodIdx); //"ssi": foodName, foodNum은 String, foodIdx는 int

  mysqli_stmt_execute($statement);
  $response = array();

  //DB 테이블 update 및 접속 종료
  echo json_encode($response);
  mysqli_stmt_close($statement);
  mysqli_close($conn);

 ?>
