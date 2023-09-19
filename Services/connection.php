<?php


$server = '.';
$database = 'Supervisor_App';
$user = 'sa';
$password = '1one';
// Connect to MSSql
$link = odbc_connect("DRIVER={SQL Server Native Client 11.0}; Server=$server; Database=$database;", $user, $password);

if (!$link) {
    die('Something went wrong while connecting to MSSQL');
	echo "no connection ";
}
?>