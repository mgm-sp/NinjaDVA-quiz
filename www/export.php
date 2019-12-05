<textarea>
<?php
$client = new SoapClient("http://quiz-api:8080/?wsdl");

echo $client->exportAllQuestions()->return;

?>
</textarea>
