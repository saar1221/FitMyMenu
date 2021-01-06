<?php
 if($_SERVER['REQUEST_METHOD'] =='POST'){
	 
	     require_once 'connect.php';
	 
	 $id_user=$_POST['id_user'];
	 $id_meal = $_POST['id_meal'];
	 $date_and_time = $_POST['date_and_time'];
	 $call_func=$_POST['call_func'];
	 

	$result=array();

			
	
	 if($call_func==="delete")
	 {
		 	
	$sql="SELECT * FROM users_menus WHERE id_user = '$id_user' AND id_meal = '$id_meal' ";
	$response =mysqli_query($conn,$sql);
	$result['menu_user']=array();
	
	if(mysqli_num_rows($response)>=0){	
			while ($row = mysqli_fetch_assoc($response))
			{	
if(strcmp($date_and_time, $row['date_and_time']) === 0 )	
{
	$id_findder = $row['id'];
			$index1['id'] = $row['id'];
			$index1['id_user']=$row['id_user'];
			$index1['id_meal']=$row['id_meal'];
			$index1['date_and_time']=$row['date_and_time'];
			
			array_push($result['menu_user'],$index1);
			
			 $sqldelete ="DELETE FROM users_menus WHERE id ='$id_findder'";
			if(mysqli_query($conn,$sqldelete))
			{
			$result['success']="1";
				$result['message']="success";	
			}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);	
			}
			
}
		
			}
				
			
		}else{
				$result['success']="0";
				$result['message']="error";	
				echo json_encode($result);
		}
			
			
			
		/*
			////--------------------------------------------------------------------------------------
		 	  $sql= "SELECT id FROM users_menus WHERE id_user = '$id_user' AND id_meal = '$id_meal' ";
			
	    $result = mysqli_query($conn, $sql);
	
		  	if(mysqli_num_rows($result)===1){
		$row = mysqli_fetch_assoc($result);
					
				$index_id= $row['id'];
		echo  "   index_id  ".$row['id'];
		
			 $result["success"]=1;
		   $result["qury"]=$sql;
		   $result["message"]="success";
			}else{
 $result["success"]=0;
		   $result["qury"]=$sql;
		   $result["message"]="error";
			}
			
			*/
/*
  $sqldelete ="DELETE FROM users_menus WHERE id ='$index_id'";

if (mysqli_query($conn, $sqldelete))
{
	//		$result["success"]=1;
		//   $result["qury"]="$sqldelete";
//$result["message"]="success";
		  echo " get in ";
		
} 
	   else{
		   $result["success"]=0;
		   $result["qury"]=$sqldelete;
		   $result["message"]="error";
	   }
	   */
	   	$result['success']="1";
	 	   echo json_encode($result);

	 }
	 if($call_func==="add"){
		 $sql= "INSERT INTO users_menus(id_user,id_meal,date_and_time) VALUES ('$id_user','$id_meal','$date_and_time')"; 	 	
	   if(mysqli_query($conn,$sql)){
		   $result["success"]=1;
		   $result["qury"]="$sql";
		   $result["message"]="success";
		
		  
	   }
	   else{
		  $result["success"]=0;
		   $result["qury"]=$sql;
		   $result["message"]="error";
		   	 
	   }
	   	   echo json_encode($result);

	 }
	 
	 
	 

	     mysqli_close($conn); 
 }	
	
   	 
	   
	 
?>