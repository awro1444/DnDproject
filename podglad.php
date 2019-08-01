<form action="podglad.php" method="post">
Nazwa tabeli:<input type="text" name="tabela"/><br/>
<input type="submit" value="Podgląd"/><br/>
</form>

<?php

if( isset($_POST['tabela']) )
{
  $c = mysqli_connect("sql1.5v.pl", "XXXXX", "XXXXX", "XXXXX") or die('brak połączenia z serwerem');
  mysqli_query($c, 'SET NAMES UTF-8');
  $rs = mysqli_query($c, 'SELECT * FROM '.$_POST['tabela']) or die('Błąd - nie można pobrać rekordów z tabeli');

  echo '<table border="1"><tr><th>id</th><th>name</th><th>level</th><th>class</th><th>race</th></tr>';

  while($rec = mysqli_fetch_array($rs))
  echo '<tr><td>'.$rec['id'].'</td><td>'.$rec['name'].'</td>
  <td>'.$rec['level'].'</td><td>'.$rec['class'].'</td><td>'.$rec['race'].'</td></tr>';

  echo '</table><hr>';

  $rs = mysqli_query($c, 'SELECT * FROM raceTable') or die('Błąd - nie można pobrać rekordów z tabeli ras');
  echo '<table border="1"><tr><th>id</th><th>raceName</th></tr>';

  while($rec = mysqli_fetch_array($rs))
  echo '<tr><td>'.$rec['id'].'</td><td>'.$rec['raceName'].'</td></tr>';

  echo '</table>';

}

?>
