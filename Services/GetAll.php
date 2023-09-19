<?php

if($_SERVER["REQUEST_METHOD"] == "POST"){
	require 'connection.php';
	getHH();
}

function getHH(){
	global $link;
	$supervisor = $_POST["supervisor"];

	$statment = odbc_prepare($link,"EXEC fetch_HH_App ?");
	$statment2 = odbc_prepare($link,"EXEC fetch_cluster_App ?");
	$statment3 = odbc_prepare($link, "EXEC fetch_enums_App ?");
	
	$params = array($supervisor);
	$success = odbc_execute($statment, $params);
	$success2 = odbc_execute($statment2, $params);
	$success3 = odbc_execute($statment3, $params);
	if(!$success && !$success2 && !$success3)
	{
		echo("odbc_execute failed");
	}

	else{
		while($roww = odbc_fetch_row($statment))
		{
			$hh_id = odbc_result($statment, 'HH_id');
			$hh_status = odbc_result($statment, 'HH_status');
			$hh_result = odbc_result($statment, 'HH_result');
			$en = odbc_result($statment, 'enum');
			$timestamp = odbc_result($statment, 'Timestamp');
			$loc = odbc_result($statment, 'Location');
			$revisit = odbc_result($statment, 'Revist');
			$cluster = odbc_result($statment, 'Cluster');
			$cluster_name = odbc_result($statment, 'Cluster_name');
			//$temp_row[] = array ($hh_id , $hh_status, $cluster, $cluster_name);
			 $row1[] = array ( "hh_id" => $hh_id, "hh_status" => $hh_status, "hh_result" => $hh_result, "cluster" => $cluster, "cluster_name" => $cluster_name, "Enumr" => $en, "timestamp" => $timestamp, "loc" => $loc, "revisit" => $revisit);
		}
		
	while($row = odbc_fetch_row($statment2))
		{
			$cluster_code = odbc_result($statment2, 'Cluster_code');
			$cluster_name = odbc_result($statment2, 'Cluster_name');
			//$temp_row[] = array ($hh_id , $hh_status, $cluster, $cluster_name);
			 $row11[] = array ( "cluster_name" => $cluster_name, "cluster_code" => $cluster_code);
		}
		
	while($row = odbc_fetch_row($statment3))
		{
			$name = odbc_result($statment3, 'enum_name');
			$code = odbc_result($statment3, 'enum_code');
			$row2[] = array (  "enum" => $name, "code" => $code);
		}
		$row = array("CC_list" => $row11 , "HH_list" => $row1 , "enum_list" => $row2);		
}
header('Content-Type: application/json');
	echo json_encode($row);
	odbc_close($link);
}

?>