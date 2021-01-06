<?php 
 if($_SERVER['REQUEST_METHOD'] =='POST'){
	 
	 $weight=$_POST['weight'];
	 $height = $_POST['height'];
	 $age = $_POST['age'];
	 $purpose = $_POST['purpose'];
	  $activity = $_POST['activity'];
	 $diabetes = $_POST['diabetes'];
	 $typefood = $_POST['typefood'];
	$id_finder=  $id= $_POST['id'];
	  $gender=$_POST['gender'];
	    $user_mode=$_POST['user_mode'];
	$selectedAllergies=$_POST['selectedAllergies'];
	 
	// echo json_encode($selectedAllergies);
	 if($selectedAllergies==="[]")
	 {
		$size_array=0; 
		  $try="   empty  ";
	 }
	 else{
	//delete the chars "[]" from $selectedAllergies and split the arraylist to string array
  $selectedAllergies = preg_split( "/[,]+/",  $selectedAllergies ); 
	 //	 echo json_encode($selectedAllergies);
	$selectedAllergies = preg_replace("/[^A-Za-z0-9]/",'', $selectedAllergies);
		// echo json_encode($selectedAllergies);
		$try="   not empty  ";
	$size_array=count($selectedAllergies);
	 }






	

  
	   require_once 'connect.php';


/*
		###	activity can be 1 to 5 
		###	this explain the value in the data base on table users_table the colom level_activity
        1-Little or no activity - office work at a desk
        2-Little activity - 1-3 times a week
        3-Average activity - 3-5 times a week
        4-Intensive activity - daily
        5-Intensive activity is combined with physical work

*/

	   // find the id typefood
	    $sql = "SELECT id FROM types_table WHERE name='$typefood'";	
		$result_typefood_id= mysqli_query($conn, $sql);
	
	if(mysqli_num_rows($result_typefood_id)===1){
				$row = mysqli_fetch_assoc($result_typefood_id);
			
				$id_typefood= $index['id_typefood'] =  (int)$row['id'];
			}

	   /// update the user parameters in users_table
	   $sql= "UPDATE users_table SET weight='$weight', height='$height',age='$age' ,purpose='$purpose',level_activity='$activity', diabetes='$diabetes' , gender='$gender' ,id_types_food=$id_typefood WHERE id='$id_finder' ";
	    if(mysqli_query($conn,$sql)){
		   $result["success"]=1; 
		     $result["qury"]=$sql;
		   $result["message"]="success";
		   
		 //  echo json_encode($result);	  
	   }
	   else{
		  $result["success"]=0;
		    $result["qury"]=$sql;
		   $result["message"]="error";
		   echo json_encode($result);	
	   }	   
	


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    



				// here its new user or he is updateing hes data

 if($user_mode==="update") //else $user_mode = new    
  {				
	//delete all the meals the user ever eat becuse he change his data so all the meals he selected not relevant
		 $sql ="DELETE FROM users_menus WHERE id ='$id_finder'";
		 if(mysqli_query($conn,$sql)){

		     $result["qury"]=$sql;
		   $result["message"]="success"; 
	   }
	   else{
		
		    $result["qury"]=$sql;
		   $result["message"]="error the user dont have meals";
	   }	   
			// delete user allergies if he have olds
			 $sql ="DELETE FROM users_allergies WHERE id_user ='$id_finder'";
	 if(mysqli_query($conn,$sql)){
	
		     $result["qury"]=$sql;
		   $result["message"]="success";
	   }
	   else{
		  $result["success"]=0;
		    $result["qury"]=$sql;
		   $result["message"]="error the user dont have allergies to delete";
		   echo json_encode($result);	
	   }	   
			// call function that add add user allergies
					insert_allergies_user($size_array,$selectedAllergies,$id_finder,$conn);			
  }
 
  else{   //else $user_mode = new
	
					insert_allergies_user($size_array,$selectedAllergies,$id_finder,$conn);	  
  }	  

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  	
	

 

	 
 
 /*
  $temparray=array();
  
// take the allergies array and insert into user_allergies table in db insert the id use and id allergies he or she have :)
			for($i=0;$i<8;$i++)
			{
			$temp=$selectedAllergies[$i];
		
	  $sql = "SELECT id FROM allergies_table WHERE name_allergies Like %$temp'";
	      $result_id_allergeis = mysqli_query($conn, $sql);
		while ($row = mysqli_fetch_assoc($result_id_allergeis))
			{		
				 array_push($temparray, $row['id']);
			}
			}	
			for($i=0;$i<count($temparray);$i++){
					 $sql= "INSERT INTO users_allergies(id_user,id_allergies) VALUES ('$id','".$temparray[$i]."')"; 	
					 mysqli_query($conn, $sql);
			}
 
*/
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
 	
 // Takes the user with all the data
	
 			$result['user']=array();

 $sql="SELECT * FROM users_table WHERE id='$id'";
	$response =mysqli_query($conn,$sql);
	
	if(mysqli_num_rows($response)===1){	
		$row=mysqli_fetch_assoc($response);
		
			$index1['id'] = $row['id'];
			$index1['name']=$row['name'];
			$index1['email']=$row['email'];
			$index1['weight']=$row['weight'];
			$index1['height']=$row['height'];
			$index1['age']=$row['age'];
			$index1['purpose']=$row['purpose'];
			$index1['level_activity']=$row['level_activity'];
			$index1['diabetes']=$row['diabetes'];
			$index1['gender']=$row['gender'];
			$index1['csv_file']=$row['csv_file'];

			array_push($result['user'],$index1);
			
				$result['success']="1";
				$result['message']=$sql;	
				echo json_encode($result);
				
	}
	else{
				$result['success']="0";
				$result["qury"]="SELECT * FROM users_table WHERE id='$id'";
				$result['message']="error";	
		
				echo json_encode($result);
		}

	
   mysqli_close($conn);

 }
 
    function insert_allergies_user($size_array,$selectedAllergies,$id_finder,$conn)
	{

		// take the allergies array and insert into user_allergies table in db insert the id use and id allergies he or she have :)
	for($j=0;$j<$size_array;$j++)
		{
					$temp_name_allergies=$selectedAllergies[$j];

	  $sql = "SELECT id FROM allergies_table WHERE name_allergies Like '%$temp_name_allergies'";
	      $result_id_allergeis = mysqli_query($conn, $sql);
		while ($row = mysqli_fetch_assoc($result_id_allergeis))
			{		
				$id_allergies=(int)$row['id'];
								
			 $sql= "INSERT INTO users_allergies(id_user,id_allergies) VALUES ('$id_finder','$id_allergies')"; 	
					 if(mysqli_query($conn, $sql))
					 {
					$result['success']="1";
					$result['message']=$sql;	
					}
					 else{	
					$result['message']=$sql;
					$result['message']="error ";	
					 } 
			}
		}
	}
?>