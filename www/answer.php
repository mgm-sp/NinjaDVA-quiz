<?php
$return = "/";
if (isset($_POST["next_question"])){
	session_start();
	if (!isset($_SESSION["answers"])){
		$_SESSION["answers"] = [];
	}

	if (!empty($_POST['session_riding'])) {
		if (hash_equals($_SESSION['csrf_token'], $_POST['session_riding'])) {
			$question_num = (int) $_POST["question_num"];
			$_SESSION["answers"][$question_num] = $_POST["answer"];

			$client = new SoapClient("http://quiz-api:8080/?wsdl");
			$total_questions = $client->totalQuestions()->return;
			$next = $question_num + ($_POST["next_question"] == "Next Question" ? 1 : -1);
			if ($total_questions == $next) {
				$return = "/finish.php";
			} else {
				$return = "get_question.php?num=$next";
			}
		} else {
			$return = "get_question.php?num=$question_num";
		}
	}

}
header("Location: $return");
?>
