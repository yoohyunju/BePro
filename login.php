<?php
    $con = mysqli_connect("localhost:3307", "beProAdmin", "123456", "bepro");
    if(mysqli_connect_errno()){
        echo("db 연결 실패 : ". mysqli_connect_error());
    }
    mysqli_query($con, "utf8");
    $userEmail = $_POST["userEmail"];
    $userPassword = $_POST["userPassword"];

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE user_email = ? AND user_password = ?");
    mysqli_stmt_bind_param($statement, "ss", $userEmail, $userPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userIDX, $userPassword, $userNickname, $userImg, $userDate, $userUpdate, $userAuthority, $userEmail);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["userIDX"] = $userIDX;
        $response["userPassword"] = $userPassword;
        $response["userNickname"] = $userNickname;
        $response["userImg"] = $userImg;
        $response["userDate"] = $userDate;
        $response["userUpdate"] = $userUpdate;
        $response["userAuthority"] = $userAuthority;
        $response["userEmail"] = $userEmail;
    }

    echo json_encode($response);
?>
