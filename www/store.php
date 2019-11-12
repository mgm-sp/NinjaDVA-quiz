<?php
session_start();
if (!empty($_POST['store_token']) && hash_equals($_SESSION['final_token'], $_POST['store_token'])) {
	$client = new SoapClient("http://quiz-api:8080/?wsdl", array('trace' => true));
	$answers = [];
	foreach($_SESSION["answers"] as $i => $a){
		$answers[] = ["answer" => $a];
	}
	$ret = $client->store(["studentId" => $_SESSION['final_token'], "answers" => $answers])->return;
	$result= urlencode($ret) . "&state=success";
} else {
	$result= urlencode("Something went wrong!");
}
header("Location: finish.php?message=$result");
?>
