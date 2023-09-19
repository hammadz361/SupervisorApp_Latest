<?php
header('Access-Control-Allow-Origin: *');  
if($_SERVER["REQUEST_METHOD"] == "GET"){
	require 'connection.php';
	getHH();
}

function getHH(){
	global $link;

	$statment = odbc_prepare($link, "EXEC fetch_Sup_web");
	$success = odbc_execute($statment);
	if(!$success)
	{
		echo("odbc_execute failed");
	}

	else{
		while($row = odbc_fetch_row($statment))
		{
			$name = odbc_result($statment, 'Supervisor_name');
			$code = odbc_result($statment, 'Supervisor_code');
			$cname = odbc_result($statment, 'Cluster_Name');
			$ccode = odbc_result($statment, 'Cluster_Code');
			
			$row1[] = array ("Supervisor" => $name, "code" => $code, "cluster_name" => $cname, "cluster_code" => $ccode);
		}

		$row = array("Sup_list" => $row1 );
}
header('Content-Type: application/json');
	echo json_encode($row);
	odbc_close($link);
}

?>