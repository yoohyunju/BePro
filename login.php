<?php
    $con = mysqli_connect("localhost:3307", "root", "123456", "test");
    if(mysqli_connect_errno()){
        echo("db 연결 실패 : ". mysqli_connect_error());
    }
    mysqli_query($con, "utf8");
    $userEmail = $_POST["userEmail"];
    $userPassword = $_POST["userPassword"];

    $statement = mysqli_prepare($con, "SELECT * FROM users WHERE user_email = ? AND user_password = ?");
    mysqli_stmt_bind_param($statement, "ss", $userEmail, $userPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userEmail, $userPassword);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["userID"] = $userID;
        $response["userEmail"] = $userEmail;
        $response["userPassword"] = $userPassword;
    }

    echo json_encode($response);
?>
