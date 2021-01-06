<?php 
if($_SERVER['REQUEST_METHOD'] =='POST'){
	require_once 'connect.php';
	
	

			$result=array();
							//insert to array all the meals
					$sql="SELECT * FROM meals_table WHERE confirm_by_admin = 0 " ;
	$response =mysqli_query($conn,$sql);
	$result['meals']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{	
			$index4['id'] = $row['id'];
			$index4['name']=$row['name_meals'];
			$index4['calories'] = $row['calories'];
			$index4['id_types']=$row['id_types'];
			$index4['id_user_upload'] = $row['id_user_upload'];
			$index4['description'] = $row['description'];
			$index4['Dangerous_for_diabetics'] = $row['Dangerous_for_diabetics'];
			$index4['image_path'] = $row['image_path'];
				$index4['confirm_by_admin'] = $row['confirm_by_admin'];
			
			array_push($result['meals'],$index4);
			}
				$result['success']="1";
				$result['message']="success";	
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
		
	
		//insert to array all meals_consist
					$sql="SELECT * FROM meals_consist ";
	$response =mysqli_query($conn,$sql);
	$result['meals_consist_admin']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index5['id_meal'] = $row['id_meal'];
			$index5['id_ingredients']=$row['id_ingredients'];
			$index5['count']=$row['count'];
			
			array_push($result['meals_consist_admin'],$index5);
			}
				$result['success']="1";
				$result['message']="success";	
									//send all the arrays to android in json encode array
				echo json_encode($result);
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
		

		mysqli_close($conn);
		}
?> 
