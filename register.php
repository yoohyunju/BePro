<?php
    $con = mysqli_connect("localhost:3307", "root", "123456", "test");
    if(mysqli_connect_errno()){
        echo("db 연결 실패 : ". mysqli_connect_error());
    }
    mysqli_set_charset($con, "utf8");

    $userID = $_POST["userID"];
    $userPWD = $_POST["userPassword"];
    $userEMAIL = $_POST["userEmail"];

    $statement = mysqli_prepare($con, "INSERT INTO users (user_id, user_password, user_email) VALUES (?,?,?)");
    $bind = mysqli_Stmt_bind_param($statement, "sss", $userID, $userPWD, $userEMAIL);
    
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>