<?php


// Create connection
 require_once 'connect.php';


 if($_SERVER['REQUEST_METHOD'] =='POST')
 {
$user_id=$_POST['user_id'];
 $name_meal= $_POST['name_meal'];
  $calories_meal =$_POST['calories'];
 $description_meal=$_POST['description'];
   $diabetics =$_POST['dangerous_for_diabetics'];
    $types_food=$_POST['types_food'];
   $image_data = $_POST['image_path'];
   $size = $_POST['size'];

// find the last id in meals_table db
$sql="SELECT id from meals_table ORDER BY id DESC LIMIT 1 ";
	$response =mysqli_query($conn,$sql);
	if(mysqli_num_rows($response)===1){	
		$row=mysqli_fetch_assoc($response);
			$id_finder = $row['id'];
			$id_finder++;
	}

 

 
$admin_not_confirm_yet=0;
 $ImagePath = "images/".$id_finder.".png"; // the id of the meal 
 $ServerURL = "http://192.168.43.161/android_register_login/$ImagePath";
 $sql = "INSERT INTO meals_table (id,name_meals,calories,id_types,id_user_upload,description,Dangerous_for_diabetics,image_path,confirm_by_admin) VALUES 
 ('$id_finder','$name_meal','$calories_meal','$types_food','$user_id','$description_meal','$diabetics','$ServerURL','$admin_not_confirm_yet')";
 if(mysqli_query($conn, $sql)){
 file_put_contents($ImagePath,base64_decode($image_data));
    $result["success"]=1;
		   $result["message"]=$sql;  
 }   else{
		      $result["success"]=0;
		   $result["message"]=$sql;  
	   }
	   /*
  	  $sql= "INSERT INTO meals_consist(id_meal,id_ingredients,count) VALUES ()"; 	 	
	   if(mysqli_query($conn,$sql)){
		   $result["success"]=1;
		   $result["message"]=$sql; 
	   }   else{
		      $result["success"]=0;
		   $result["message"]=$sql;
		   
	   }
	   */
	    
for($i=0 ;$i<$size;$i++){
 $id_ingredient=$_POST['id_ingredient_'.$i];
$count=$_POST['count_'.$i];
		
		 	  $sql= "INSERT INTO meals_consist(id_meal,id_ingredients,count) VALUES ('$id_finder','$id_ingredient','$count')"; 	 	
	   if(mysqli_query($conn,$sql)){
		   $result["success"]=1;
		   $result["message"]=$sql;
	   }
	   else{
		      $result["success"]=0;
		   $result["message"]=$sql;
	   }

}

 echo json_encode($result);
 }else{
 echo "Not Uploaded";
 echo json_encode("Not Uploaded");
 }


 mysqli_close($conn);
 
?> 


