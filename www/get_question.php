<?php
session_start();

$client = new SoapClient("http://quiz-api:8080/?wsdl");

if (isset($_GET["num"])) {
	$question_num = (int)$_GET["num"];
} else {
	$question_num = 0;
}
$total_questions = $client->totalQuestions()->return;

$question = $client->fetchQuestion(["questionNumber" => $question_num])->return;

if(isset($question)){
	if (empty($_SESSION['csrf_token'])) {
		$_SESSION['csrf_token'] = bin2hex(random_bytes(32));
	}
	$csrf_token = $_SESSION['csrf_token'];
?>
<form method='POST' action='answer.php'>
	<fieldset>
    <input style="overflow: visible !important; height: 0 !important; width: 0 !important; margin: 0 !important; border: 0 !important; padding: 0 !important; display: block !important;" type="submit" name="next_question" value="Next Question"/>
		<legend><?php print $question->title; ?></legend>
<?php
foreach ($question->answers->answer as $i => $value) {
?>
		<div>
			<input type="<?php echo $question->type?>" id="answer_<?php echo $i?>" name="answer[]"
<?php if ($question->type == "text"){?>placeholder<?php } else { ?>value<?php } ?>="<?php echo $value ?>"
<?php
 	if (isset($_SESSION["answers"][$question_num])) {
		switch ($question->type){
		case "text":
			?>value="<?php echo htmlspecialchars($_SESSION["answers"][$question_num][$i]) ?>"<?php
			break;
		case "checkbox":
		case "radio":
			if (in_array($value,$_SESSION["answers"][$question_num])) { ?>checked<?php };
			break;
		}
	}
?>
			 />
			<label for="answer_<?php echo $i ?>"><?php echo $value ?></label>
		</div>
<?php } ?>

		<div>
			<input type="hidden" name="session_riding" value="<?php echo $csrf_token ?>">
			<input type="hidden" name="question_num" value="<?php echo $question_num ?>">
		<?php if ($question_num != 0){ ?>
			<input type="submit" name="next_question" value="Previous Question"/>
		<?php } ?>
			<input type="submit" name="next_question" value="Next Question"/>
		</div>
	</fieldset>
</form>

<?php
} else {
	echo "No question number ".htmlspecialchars($question_num)." available!";
}
?>
