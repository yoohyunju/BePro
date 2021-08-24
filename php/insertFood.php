<?php
  include('dbcon.php');
  mysqli_query($conn, "utf8");

  //android 코드의 변수에 담긴 값을 POST로 값 전달받음
  $foodName = $_POST["foodName"]; //식품명
  $foodNum = $_POST["foodTotal"]; //개수
  //$foodRemainDate = $_POST["mFoodRemain"] //남은 기한

  //DB에 삽입할 준비
  $statement = mysqli_prepare($conn, "INSERT INTO food (food_name, food_number) VALUES (?,?)"); // AND food_expirationDate = ? 얘는 나중에 추가
  mysqli_stmt_bind_param($statement, "ss", $foodName, $foodNum);
  mysqli_stmt_execute($statement);

  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $foodName, $foodNum);

  $response = array();
  $response["success"] = true;


/*
  $response["success"] = false;

  while(mysqli_stmt_fetch($statement)){
    $response["success"] = true;
    $response["foodIdx"] = $foodIdx;
    $response["foodName"] = $foodName;
    $response["foodNum"] = $foodNum;
  }
*/

  echo json_encode($response);





?>
