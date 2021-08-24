<?php
  //에러 출력
  error_reporting(E_ALL);
  ini_set('display_errors', '1');

  include('dbcon.php');
  mysqli_query($conn, "utf8");
  //mysqli_query($conn, "set names utf8");
  //header("Content-Type:text/html; charset=UTF-8");

  $sql = "SELECT * FROM food";
  //$nowDateSql = "SELECT DATE_FORMAT(NOW(), '%Y-%m-%d')"; //현재 날짜 가져오는 쿼리

  $result = mysqli_query($conn, $sql);//DB 서버에 쿼리 전송
  //$dateResult = mysqli_query($conn, $nowDateSql);

  $response = array(); //연관 배열 선언

  while($row = mysqli_fetch_array($result)){
    array_push($response, array(
      //key => value
      'friIdx'=>$row['fri_idx'],
      'foodIdx'=>$row['food_idx'],
      'foodName'=>$row['food_name'],
      'foodNum'=>$row['food_number'],
      'foodRegistrant'=>$row['food_registrant'],
      'foodExp'=>$row['food_expirationDate'],
      'foodMemo'=>$row['food_memo'],
      'foodDate'=>$row['food_date'],
      'foodUpdate'=>$row['food_update']
    ));

  }

  echo json_encode($response); //JSON 파싱
  mysqli_close($conn); //DB 접속 종료
?>
