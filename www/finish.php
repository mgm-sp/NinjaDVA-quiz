<fieldset>
<legend>Your answers:</legend>
<?php
session_start();
$final_token = $_SESSION['final_token'];

$client = new SoapClient("http://quiz-api:8080/?wsdl");
$questions = $client->fetchQuestions()->return->questions->question;
$total_questions = $client->totalQuestions()->return;


if (isset($_GET["state"]) && $_GET["state"] === "success"){
	$storedisabled = "disabled='disabled'";
	$success = "success";
} else {
	$storedisabled = "";
	$success = "";
}


foreach($questions as $q => $question) {
	?><div><?php echo $question->title;?></div>
	<ul>
<?php
	foreach($_SESSION["answers"][$q] as $a => $answer){
		?><li><?php echo htmlspecialchars($answer);?></li><?php
	}
	?></ul><?php
}
?>
<form method="GET" action="get_question.php" style="display: inline;" >
<input type="hidden" name="num" value="<?php echo ($total_questions-1);?>">
<input type="submit" value="Previous Question"/>
</form>
<form method="POST" action="store.php" style="display: inline;">
<input type="hidden" name="store_token" value="<?php echo $final_token;?>">
<input type="submit" value="Confirm" <?php echo $storedisabled ?>/>
</form>
<?php if ($success === "success"){ ?>
<form method="GET" action="new.php" style="display: inline;">
<input type="submit" value="Get New Question"/>
</form>
<?php } ?>
</fieldset>
<?php if (isset($_GET["message"])){ echo htmlspecialchars($_GET["message"]);} ?>
