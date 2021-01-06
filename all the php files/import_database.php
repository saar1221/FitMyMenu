<?php

 if($_SERVER['REQUEST_METHOD'] =='POST'){
	

	require_once 'connect.php';
	

	
		//insert to array all ingredients
		$sql="SELECT * FROM ingredients_table ORDER BY name";
	$response =mysqli_query($conn,$sql);
	$result['ingredients']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index1['id'] = $row['id'];
			$index1['name']=$row['name'];
			
			array_push($result['ingredients'],$index1);
			}
				$result['success']="1";
				$result['message']="success";	
			
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
		
		
		//insert to array all types food
			$sql="SELECT * FROM types_table ";
	$response =mysqli_query($conn,$sql);
	$result['types']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index2['id'] = $row['id'];
			$index2['name']=$row['name'];
			array_push($result['types'],$index2);
			}
				$result['success']="1";
				$result['message']="success";		
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
		
		
		//insert to array all allergies
					$sql="SELECT * FROM allergies_table ";
	$response =mysqli_query($conn,$sql);
	$result['allergies']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index3['id'] = $row['id'];
			$index3['name']=$row['name_allergies'];

			array_push($result['allergies'],$index3);
			}
				$result['success']="1";
				$result['message']="success";	
						
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
		

		
					//insert to array all the meals
					$sql="SELECT * FROM meals_table WHERE confirm_by_admin = 1  ";
	$response =mysqli_query($conn,$sql);
	$result['meals']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{	
			
			$id_meal_temp=$index4['id'] = $row['id'];
			
					//insert to meals all the users are selected the meals cald total				
					$sql1="SELECT  COUNT( id_user ) as total FROM users_menus WHERE id_meal=$id_meal_temp";
	$response1 =mysqli_query($conn,$sql1);
	
	if(mysqli_num_rows($response1)>=0){	
			while ($row1 = mysqli_fetch_assoc($response1))
			{	
					$index4['total']=$row1['total'];
			}
		}
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
	$result['meals_consist']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{			  
			$index5['id_meal'] = $row['id_meal'];
			$index5['id_ingredients']=$row['id_ingredients'];
			$index5['count']=$row['count'];
			
			array_push($result['meals_consist'],$index5);
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