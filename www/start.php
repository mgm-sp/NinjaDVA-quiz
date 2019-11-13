<?php
session_start();

#$client = new SoapClient("http://quiz-api:8080/?wsdl");
$_SESSION['final_token'] = bin2hex(random_bytes(32));

header("Location: get_question.php?num=0");
?>

