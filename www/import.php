<?php
$client = new SoapClient("http://quiz-api:8080/?wsdl");

if ( basename($_SERVER["SCRIPT_NAME"]) === "admin.php") {
?>
<form action="import.php" method="post" enctype="multipart/form-data">
<input type="file" name="questions" accept="application/json,.json" /><br />
<button>Import</button>
</form>
<?php
} else {
	try {
		echo $client->importAllQuestions(["questionJSON" => file_get_contents($_FILES["questions"]["tmp_name"])])->return;
	} catch (Exception $e) {
		echo $e->getMessage();
	}

}

?>
