<?php
$client = new SoapClient("http://quiz-api:8080/?wsdl");

if ( basename($_SERVER["SCRIPT_NAME"]) === "admin.php") {
	header("Location: export.php");
} else {
	header("Content-type: application/json");
	header('Content-disposition: inline; filename="questions.json"');
	echo $client->exportAllQuestions()->return;
}

?>
