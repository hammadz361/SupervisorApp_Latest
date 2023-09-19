<?php

if($_SERVER["REQUEST_METHOD"] == "POST"){
	require 'connection.php';
	login();
}

function login(){
	global $link;
	//echo $link;
	$username = $_POST["username"];
	$password = $_POST["password"];

	$statment = odbc_prepare($link, "EXEC loginApp ?,?");
	$params = array($username,$password);
	$success = odbc_execute($statment, $params);
	if(!$success)
	{
		echo("odbc_execute failed");
	}
	else
	{
		$code = odbc_result($statment, 'supervisor_code');
		$shah = odbc_result($statment, 'supervisor_name');
		if($code==false)
		{
			 $row1 = array ( "status" => "403", "user" => "", "code" => "");
			 $row = array( "result" => $row1);
		}
		else
		{
		//$code = odbc_result($statment, 'supervisor_code');
		//$shah = odbc_result($statment, 'supervisor_name');
			 $row1 = array ( "status" => "200", "user" => $shah, "code" => $code);
			 $row = array( "result" => $row1);
		}
	header('Content-Type: application/json');
	echo json_encode($row);
	
	}
	odbc_close($link);
}

?>