<?php

 if($_SERVER['REQUEST_METHOD'] =='POST'){
	
	$request=$_POST['request'];
	
	
		$result=array();
	$result['request']=array();
	
	require_once 'connect.php';
	
	if($request === "DELETE_USER")
	{
		$email_or_ingredient=$_POST['email_or_ingredient'];
		  $sql = "SELECT id FROM users_table WHERE  email like '%$email_or_ingredient'";
	    $result_id = mysqli_query($conn, $sql);
		if(mysqli_num_rows($result_id)===1){
		while ($row = mysqli_fetch_assoc($result_id))
			{		
				  $id_user = $row['id'];
			}
					$sql="DELETE FROM users_table WHERE email like '%$email_or_ingredient' AND id =  $id_user";
		if(mysqli_query($conn,$sql))
		{
			
				    $result["success"]=1;
				 $result["requestAdmin"]="DELETE_USER";
		}			
				
		}
		else {
			$result["success"]=0;
			$result["requestAdmin"]="DELETE_USER";	  	
}	
		
	
	}
	
	else if($request === "ADD_INGREDIENT")
		
		{
			$email_or_ingredient=$_POST['email_or_ingredient'];
			
					$sql="INSERT INTO ingredients_tablename VALUES $email_or_ingredient";
		if(mysqli_query($conn,$sql))
		{
			
				 	    $result["success"]=1;
				 $result["requestAdmin"]="ADD_INGREDIENT";
		}else{
			    $result["success"]=0;
				 $result["requestAdmin"]="ADD_INGREDIENT";
		}
			
		}


	else if($request === "UPDATE_MEAL")
		{
			 $id_meal=$_POST['id_meal'];
			 $name_meal= $_POST['name_meal'];
  $calories_meal =$_POST['calories'];
      $types_food=$_POST['types_food'];
 $description_meal=$_POST['description'];
   $diabetics =$_POST['dangerous_for_diabetics'];

	if($diabetics)
	{
		$diabetics=1;
	}else{
		$diabetics=0;
	}

 
	
$sql= "UPDATE meals_table SET name_meals='$name_meal', calories='$calories_meal',id_types='$types_food' ,description='$description_meal', dangerous_for_diabetics='$diabetics' ,confirm_by_admin=1  WHERE id='$id_meal' ";	
	if(mysqli_query($conn,$sql))	
{
			
				 	    $result["success"]=1;
				 $result["requestAdmin"]="UPDATE_MEAL";
		}else{
			    $result["success"]=0;
				 $result["requestAdmin"]="UPDATE_MEAL";
		}
			

			}	
			else if($request === "DELETE_MEAL")
		{
 $id_meal=$_POST['id_meal'];
 		  $result["id_meal"]=$id_meal;
					$sql="DELETE FROM meals_table WHERE  id =$id_meal";
		if(mysqli_query($conn,$sql))
		{
			
				    $result["success"]=1;
				 $result["requestAdmin"]="DELETE_MEAL";
		}			
						else {
			$result["success"]=0;
			$result["requestAdmin"]="DELETE_MEAL"; 	  	
		}	

					$sql="DELETE FROM meals_consist WHERE  id_meal =$id_meal";
		if(mysqli_query($conn,$sql))
		{
//delete the image from the server folder
unlink('C:/wamp64/www/android_register_login/images/'.$id_meal.'.png' );// http://192.168.0.2/android_register_login/images/4.png
				    $result["success"]=1;
				 $result["requestAdmin"]="DELETE_MEAL";
		}			
						else {
			$result["success"]=0;
			$result["requestAdmin"]="DELETE_MEAL"; 	  	
}	
		}

		
			
		
	
			echo json_encode($result);
	
/*	
	/////////////////////////// example 
	
	if(isset($_GET['del']))
{
	$id=intval($_GET['del']);
	$adn="delete from user where id=?";
	$stmt= $mysqli->prepare($adn);
	$stmt->bind_param(i,$id);
	$rs=$stmt->execute();
	if(rs==true)
	{

	}
}
	/////////////////////////// example 
	///////------------------------------------------------------------------------prepared statements ---------------------------------------------------------------------------------------


	$sql = "SELECT * FROM users_table WHERE email=? LIMIT 1"; //
$stmt = mysqli_stmt_init($conn);
if(!mysqli_stmt_prepare($stmt, $sql))
{
    $result["success_injection1"] ="Failed to prepare statement."; 
}
else
{
 $result["success_injection1"] ="user taking successfully.";
 
 mysqli_stmt_bind_param($stmt, "s", $email);


 
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);

		$i=0;
        while ($row = mysqli_fetch_array($result))
        {
			$id_findder=$row['id'];

			array_push($result1['request'],$index);
			
				$result1['success']="1";
			
				$result1['message user']="success find the user in the database";	

		}
	
		else{
			//if the password mistake 
				$result1['success']="mistake";
				//$result['success']="0";
				$result1['message user']="error password";	
				echo json_encode($result1);
								
		}
		
        }
   
	
			
    


*/


		

	mysqli_close($conn);
}
?>