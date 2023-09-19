<?php

if($_SERVER["REQUEST_METHOD"] == "POST"){
	require 'connection.php';
	getHH();
}

function getHH(){
	global $link;
	$hh_id = $_POST["hh_id"];
	$hh_status =$_POST["hh_status"];
	$hh_result =$_POST["hh_result"];
	$timestamp =$_POST["timestamp"];
	$loc =$_POST["loc"];
	$cluster =$_POST["cluster"];
	$enum =$_POST["enum"];
	$supervisor = $_POST["supervisor"];
	$revisit = $_POST["revist"];

	$statment = odbc_prepare($link, "EXEC update_HH_App ?,?,?,?,?,?,?,?,?");
	$params = array($hh_id, $hh_status, $hh_result, $timestamp,$loc,  $enum, $supervisor, $cluster, $revisit);
	
	$success = odbc_execute($statment, $params);
	if(!$success)
	{
		echo("odbc_execute failed");
	}

	else
	{
		$temp_row = "Updated Successfully";
		header('Content-Type: application/json');
		echo json_encode($temp_row);
		odbc_close($link);
	}
}

?>