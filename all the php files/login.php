<?php

 if($_SERVER['REQUEST_METHOD'] =='POST'){
	
	$email=$_POST['email'];
	$password=$_POST['password'];
	$id_findder=0;
	require_once 'connect.php';
	
	
	$result1=array();
	$result1['login']=array();
	
	
	///////------------------------------------------------------------------------prepared statements ---------------------------------------------------------------------------------------


	$sql = "SELECT * FROM users_table WHERE email=? LIMIT 1"; //
$stmt = mysqli_stmt_init($conn);
if(!mysqli_stmt_prepare($stmt, $sql))
{
    $result1["success_injection1"] ="Failed to prepare statement."; 
}
else
{
 $result1["success_injection1"] ="user taking successfully.";
 
 mysqli_stmt_bind_param($stmt, "s", $email);

        mysqli_stmt_execute($stmt);
        $response = mysqli_stmt_get_result($stmt);

		$i=0;
        while ($row = mysqli_fetch_array($response))
        {
			
	if(password_verify($password,$row['password'])){
	
	
			$id_findder=$index['id']=$row['id'];
			$index['name']=$row['name'];
			$index['email']=$row['email'];
			$weight=$index['weight']=$row['weight'];
			$height=$index['height']=$row['height'];
			$age=$index['age']=$row['age'];
			$purpose=$index['purpose']=$row['purpose'];
			$index['level_activity']=$row['level_activity'];
			$diabetes=$index['diabetes']=$row['diabetes'];
			$index['gender']=$row['gender'];
			$index['csv_file']=$row['csv_file'];
			$types_food=$index['id_types_food']=$row['id_types_food'];
		
			array_push($result1['login'],$index);
			
				$result1['success']="1";
				$result1['message user']="success find the user in the database";	

		}else{
			//if the password mistake 
				$result1['success']="mistake";
				$result1['message user']="error password";	
				echo json_encode($result1);						
		}
        }
		if($id_findder==1)
				{
							
				$result1['success']="admin";
			
				echo json_encode($result1);
						require_once 'import_database_admin.php';
				}
//if its not admin thake allergies user
if($id_findder!=0 && $id_findder!=1)
{
				//insert to array the user allergies
		$result1['user_allergies']=array();
				
	$sql="SELECT id_allergies FROM users_allergies WHERE id_user='$id_findder' ";
	$response =mysqli_query($conn,$sql);
	
	
	if(mysqli_num_rows($response)>0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index1['id_allergies']=$row['id_allergies'];
			
			array_push($result1['user_allergies'],$index1);
			}
				$result1['message_user_allergies']="success take the user allergies";						
		}else{
			// its ok if the user dont have allergies so i didnt put the $result1['success']="0";
				$result1['message_user_allergies']="the user doesn't have allergies";	
		}



		//insert to array all the users are the same to the main user its for the algo 
		//	$sql="SELECT * FROM users_table WHERE weight Between $weight-10 AND $weight+10  AND height Between $height-20 And $height+20 AND age Between $age-5 And $age+5 AND purpose LIKE '%$purpose' AND diabetes LIKE '%$diabetes' AND id_types_food=$types_food LIMIT 10";// AND id!= $id_findder
			$sql="SELECT * FROM users_table WHERE weight IS NOT NULL  AND height IS NOT NULL AND  age IS NOT NULL AND purpose LIKE '%$purpose' AND diabetes LIKE '%$diabetes' AND id_types_food=$types_food LIMIT 50";// AND id!= $id_findder

	
	$response =mysqli_query($conn,$sql);

	$result1['users']=array();
	$result5['id_meals']=array();
	
			if(mysqli_query($conn,$sql)){
			while ($row = mysqli_fetch_assoc($response))
			{	  
		$id_temp_users=	$index2['id'] = $row['id'];
			$index2['weight']=$row['weight'];
			$index2['height']=$row['height'];
			$index2['age']=$row['age'];

						//insert to meals all the users are selected the meals cald total				
					$sql1="SELECT DISTINCT id_meal FROM users_menus WHERE id_user=$id_temp_users";
	$response1 =mysqli_query($conn,$sql1);
	if(mysqli_num_rows($response1)>0){	
			while ($row1 = mysqli_fetch_assoc($response1))
			{	
					$index3['id_meal']=$row1['id_meal'];
					
						array_push($result5['id_meals'],$index3);
			}
			

	}else{
		$none_meals['id_meal']=0;
		array_push($result5['id_meals'],$none_meals);
	}
	
	$index2['id_meals']=$result5['id_meals'];
$result5['id_meals']=array();
			array_push($result1['users'],$index2);
		
	     //	find_users_allergies($id_temp_users);
			}
				$result1['success']="1";
				$result1['message_users']="success take meals like user ";	
				echo json_encode($result1);
	
			}else{
		
			//	$result1['success']="0";
				$result1['message_users']="error success take meals like user ";	
				echo json_encode($result1);
		}
		
	
	/*			
				//insert to array all users allergies
					$sql="SELECT * FROM users_allergies";
	$response =mysqli_query($conn,$sql);
	$result1['users_allergies']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index1['id_user'] = $row['id_user'];
			$index1['id_allergies']=$row['id_allergies'];
			
			array_push($result1['users_allergies'],$index1);
			}
				$result1['success']="1";
				$result1['message_users_allergies']="success took all the users allergies";	
					echo json_encode($result1);
		}else{
				$result1['success']="0";
				$result1['message_users_allergies']="error There is a problem with taking users allergies data base";	
				echo json_encode($result1);
		}
	*/	
}else{
	//if the email mistake  
	$result1['success']="mistake";
		echo json_encode($result1);
}	
}


	///////------------------------------------------------------------------------prepared statements---------------------------------------------------------------------------------------

	
		
		

	mysqli_close($conn);
}

	function find_users_allergies($id_user){
		
		
		
		
	}
?>