<h1>Admin Area</h1>
<?php
if ( isset( $_POST['function'] ) ) {
	include( $_POST['function'] );
	?><form method='GET'><input type='submit' value='Back' /></form><?php
	die();
} else {
?>
Please choose a function:
<form method='POST'>
<select name='function'>
  <option value="view.php">View results</option>
	<option value="export.php">Export questions</option>
	<option value="import.php">Import questions</option>
</select>
<input type='submit' />
</form>

<?php
}
?>
