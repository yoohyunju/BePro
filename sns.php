<?php
    $con = mysqli_connect("localhost:3307", "beProAdmin", "123456", "bepro");
    if(mysqli_connect_errno()){
        echo("db 연결 실패 : ". mysqli_connect_error());
    }
    mysqli_query($con, "utf8");
    $userEmail = $_POST["userEmail"];
    $snsName = $_POST["snsName"];

    $statement = mysqli_prepare($con, "select * from user where user_type in (?) and user_email in (?)");
    mysqli_stmt_bind_param($statement, "ss", $snsName, $userEmail);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userPassword, $userNickname, $userImg, $userDate, $userUpdate, $userAuthority, $userEmail, $userType, $userIDX);

    $response = array();
    $response["success"] = true;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = false;
        $response["userPassword"] = $userPassword;
        $response["userNickname"] = $userNickname;
        $response["userImg"] = $userImg;
        $response["userDate"] = $userDate;
        $response["userUpdate"] = $userUpdate;
        $response["userAuthority"] = $userAuthority;
        $response["userEmail"] = $userEmail;
        $response["userType"] = $userType;
        $response["userIDX"] = $userIDX;
    }

    echo json_encode($response);
?>