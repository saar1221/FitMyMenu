<?php

 if($_SERVER['REQUEST_METHOD'] =='POST'){
	

	require_once 'connect.php';
	
	$sql="SELECT name FROM ingredients_table ORDER BY name";
	
	$response =mysqli_query($conn,$sql);
	
	$result=array();
	$result['ingredients']=array();
	if(mysqli_num_rows($response) >0)
	{
		while($row=mysqli_fetch_assoc($response)){
			
			$result[]=$row;
			array_push($result['ingredients'],$row);		
		}			
		
				$result['success']="1";
				$result['message']="success";
				echo json_encode($result);
			}
	else
	{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
	}	
				mysqli_close($conn);
	
}

?>