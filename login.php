<?php
    $con = mysqli_connect("localhost:3307", "root", "123456", "test");
    if(mysqli_connect_errno()){
        echo("db ���� ���� : ". mysqli_connect_error());
    }
    mysqli_query($con, "utf8");
    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];

    $statement = mysqli_prepare($con, "SELECT * FROM users WHERE user_id = ? AND user_password = ?");
    mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userPassword, $userEmail);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["userID"] = $userID;
        $response["userPassword"] = $userPassword;
        $response["userEmail"] = $userEmail;
    }

    echo json_encode($response);
?>
