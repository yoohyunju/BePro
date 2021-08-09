<?php
    $con = mysqli_connect("localhost:3307", "beProAdmin", "123456", "bepro");
    if(mysqli_connect_errno()){
        echo("db 연결 실패 : ". mysqli_connect_error());
    }
    mysqli_set_charset($con, "utf8");

    $userNICK = $_POST["userNick"];
    $userPWD = $_POST["userPassword"];
    $userEMAIL = $_POST["userEmail"];
    $userTYPE = $_POST["userType"];
    
    $statement = mysqli_prepare($con, "INSERT INTO user (user_nickname, user_password, user_email, user_type) VALUES (?,?,?,?)");
    $bind = mysqli_Stmt_bind_param($statement, "ssss", $userNICK, $userPWD, $userEMAIL, $userTYPE);
    
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>