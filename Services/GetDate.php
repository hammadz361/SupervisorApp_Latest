<?php

if($_SERVER["REQUEST_METHOD"] == "POST"){
	require 'connection.php';
	getHH();
}

function getHH(){
	global $link;
	$supervisor = $_POST["supervisor"];

	$statment = odbc_prepare($link, "EXEC fetch_date ?");
	$params = array($supervisor);
	$success = odbc_execute($statment, $params);
	if(!$success)
	{
		echo("odbc_execute failed");
	}

	else{
		
			$date = odbc_result($statment, 'logDate_var');
			$sup = odbc_result($statment, 'supervisor');
			if($date==false)
			{
				$date = "";
			}
			$row1[] = array (  "date" => $date, "supervisor" => $sup);
			$row = array("Date_log" => $row1 );
}
header('Content-Type: application/json');
	echo json_encode($row);
	odbc_close($link);
}

?>