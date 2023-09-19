<?php
header('Access-Control-Allow-Origin: *');  
if($_SERVER["REQUEST_METHOD"] == "POST"){
	require 'connection.php';
	getHH();
}

function getHH(){
	global $link;
	$supervisor = $_POST["supervisor"];

	$statment = odbc_prepare($link,"EXEC fetch_HH_web ?");
	$params = array($supervisor);
	$success = odbc_execute($statment, $params);
	if(!$success)
	{
		echo("odbc_execute failed");
	}

	else{
		while($row = odbc_fetch_row($statment))
		{
			$hh_id = odbc_result($statment, 'HH_id');
			$hh_status = odbc_result($statment, 'HH_status');
			$cluster = odbc_result($statment, 'Cluster');
			$cluster_name = odbc_result($statment, 'Cluster_name');
			$enum = odbc_result($statment, 'Enum');
			$time = odbc_result($statment, 'Timestamp');
			$loc = odbc_result($statment, 'Location');
			$rev = odbc_result($statment, 'Revist');
			//$temp_row[] = array ($hh_id , $hh_status, $cluster, $cluster_name);
			 $row1[] = array ( "hh_id" => $hh_id, "hh_status" => $hh_status, "cluster" => $cluster, "cluster_name" => $cluster_name, "Enumerator" => $enum, "timestamp" => $time, "Location" => $loc, "Revisit" => $rev);
		}
		$row = array("HH_list_web" => $row1 );
	
}
header('Content-Type: application/json');
	echo json_encode($row);
	odbc_close($link);
}

?>