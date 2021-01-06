<?php
 
 if($_SERVER['REQUEST_METHOD'] =='POST'){
	 $name=$_POST['name'];
	  $email=$_POST['email'];
	   $password=$_POST['password'];
	   
	   $password =password_hash($password,PASSWORD_DEFAULT);
	   
	   require_once 'connect.php';


	///////------------------------------------------------------------------------beginTransaction ---------------------------------------------------------------------------------------
			$pdo = new PDO('mysql:host=localhost;dbname=users', 'root', '', array(
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_EMULATE_PREPARES => false
));


try{

    //i start here transaction.
    $pdo->beginTransaction();
 
    //Query 1:INSERT INTO users_table the new user name, email, password to database.
$sql = "INSERT INTO users_table(name, email, password) VALUES(?, ?, ?)";
	
	
	
    $stmt = $pdo->prepare($sql);
    $stmt->execute(array($name, $email, $password));
	

		
			//	$sql1 = "SELECT id FROM users_table WHERE email = ?  LIMIT 1";
	//Query :take the user id from the database.
	  $sql = "SELECT id FROM users_table WHERE email='$email'";

if ($stmt = $pdo->prepare($sql)) {

    // execute statement 
    $stmt->execute();

  
$id_user = $stmt->fetchColumn();
$result['id']=$id_user;


}
    //We've got this far without an exception, so commit the changes.
    $pdo->commit();
				$result['success']="1";
    		echo json_encode($result);
		
} 
//Our catch block will handle any exceptions that are thrown.
catch(Exception $e){
    //An exception has occured, which means that one of our database queries
    //failed.
    //Print out the error message.
    echo $e->getMessage();
    //Rollback the transaction.
    $pdo->rollBack();
			$result['success']="0";

		echo json_encode($e);
}
 
	
		
			///////------------------------------------------------------------------------beginTransaction ---------------------------------------------------------------------------------------
	


	   //-------------------------------------------------- prepared statements ----------------------------------------------


/*

$sql = "INSERT INTO users_table(name, email, password) VALUES(?, ?, ?)";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "sss", $name, $email, $password);

if(mysqli_stmt_execute($stmt)) {
	     $result["success_injection1"] ="Records inserted successfully.";
 	   $result["success"]=1;
}else{
	$result["success_injection2"]= "ERROR: Could not prepare query: $sql ";
	   $result["success"]=0;
}
*/

	   	   //--------------------------------------------------prepared statements ----------------------------------------------



/*
	   
	   	 
	   
	   // $sql= "INSERT INTO users_table(name,email,password) VALUES ('".$name."','".$email."','".$password."')"; 
	   // or
	   // $sql= "INSERT INTO users_table(name,email,password) VALUES ('$name','$email','$password')"; 
	   
	   
	   
	   
	  $sql= "INSERT INTO users_table(name,email,password) VALUES ('".$name."','".$email."','".$password."')"; 
	   
	    $result["name"]=$name; 
		$result["email"]=$email; 
		$result["password"]=$password; 
	   
	   if(mysqli_query($conn,$sql)){
		   $result["success"]=1;
		   $result["message"]="success";
		   
	   }
	   else{
		  $result["success"]=0;
		   $result["message"]="error"; 
		   
		   echo json_encode($result);	  
	   }
	
	   
	
	       $sql = "SELECT id FROM users_table WHERE email='$email'";
	    $result_id = mysqli_query($conn, $sql);
		if(mysqli_num_rows($result_id)===1){
		while ($row = mysqli_fetch_assoc($result_id))
			{		
				  $index['id_user'] = $row['id'];
			}
				
				$result["id"]=$index['id_user'];
				$result["success"]=1;
			$result["message"]="success"; 
			
				
		}
		else {
			$result["success"]=0;
			$result["message"]="error"; 
		   
		  	
}	
    */  
   //   echo json_encode($result);
//	  mysqli_stmt_close($stmt);
		   mysqli_close($conn);
 }
    
?>