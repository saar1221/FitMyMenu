package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


import static com.example.myfirstapp.Service.deleteOrContactRequestFromUser;

public class NewMealActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button SelectIngredient, sendAdmin, btn_ChangeImg, CaptureImageFromCamera,adminConfirmNextMeal,adminDeleteMeal, adminBackToCategories;
    private EditText descriptionMeal, nameMeal, Calories;

    private TextView Ingredient_for_admin;

    private RadioGroup rgdiabetes, rgtypefood;
    private RadioButton selectedrbdiabetes, selectedrbtypefood;

//    private static ArrayList<ingredients> arrayList_ingredients = new ArrayList<>(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen());
//    private static ArrayList<String> ingredients_name = new ArrayList<>();
//
//    private boolean[] checkedItems;

    private ArrayList<IngredientsAndCount> ingredients_name_id_count = new ArrayList<>();
    private static String temp_name_meal, temp_Calories_meal, temp_descriptionMeal, selectedDiabetes;
    private static int selectedTypesFoodId;

    //// camera zone
    private ProgressDialog progressDialog;
    private Intent intent;
    public static final int RequestPermissionCode = 1;
    private Bitmap bitmap;
    private boolean check = true;
    private static final String TAG = "NewMealActivity";
    private static String ImagePathFieldOnServer = "image_path";
    private static final String ImageUploadPathOnSever = "http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/new_meal_not_yet_confirm.php";
    //// camera zone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        imageView = (ImageView) findViewById(R.id.imageViewNewMeal);
        sendAdmin = (Button) findViewById(R.id.btn_sendAdmin);
        btn_ChangeImg = (Button) findViewById(R.id.btn_ChangeImg);
        SelectIngredient = (Button) findViewById(R.id.btn_SelectIngredient);
        CaptureImageFromCamera = (Button) findViewById(R.id.btn_CaptureImg);
        adminConfirmNextMeal =(Button) findViewById(R.id.btn_adminConfirmNextMeal);
          adminDeleteMeal =(Button) findViewById(R.id.btn_adminDeleteMeal);
        adminBackToCategories=(Button) findViewById(R.id.btn_adminBackToCategories);

        adminDeleteMeal.setVisibility(View.GONE);
        adminConfirmNextMeal.setVisibility(View.GONE);
        adminBackToCategories.setVisibility(View.GONE);

        rgtypefood = (RadioGroup) findViewById(R.id.rgTypeFood2);
        rgdiabetes = (RadioGroup) findViewById(R.id.rgDiabetes2);

        Ingredient_for_admin = (TextView) findViewById(R.id.Ingredient_for_admin);

        nameMeal = (EditText) findViewById(R.id.name_NewMeal);
        descriptionMeal = (EditText) findViewById(R.id.desc_NewMeal);
        Calories = (EditText) findViewById(R.id.calories_NewMeal);

// its open if the id is 1 (admin);
        if (ServiceDataBaseHolder.getUser_confirm().getId() == 1) {
            adminDeleteMeal.setVisibility(View.VISIBLE);
            adminConfirmNextMeal.setVisibility(View.VISIBLE);
            adminBackToCategories.setVisibility(View.VISIBLE);
            sendAdmin.setVisibility(View.GONE);



            if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty()){
                initializeOnAdminUploadUserMeal();
            }



            adminConfirmNextMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty()) {
                        //get meal not confirm in index 0
                        int  first_Index_meal_not_confirm =0;
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setNameMeal(nameMeal.getText().toString());
                        double temp_Calories_meal = Double.parseDouble(Calories.getText().toString());
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setCalories(temp_Calories_meal);
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setDescription(descriptionMeal.getText().toString().trim());

                        selectedrbdiabetes = (RadioButton) findViewById(rgdiabetes.getCheckedRadioButtonId());
                        selectedDiabetes = selectedrbdiabetes.getText().toString().trim();

                        if (!selectedDiabetes.isEmpty()) {
                            String num = selectedrbdiabetes.getText().toString().trim();
                            if ("No".equals(num)) {
                                ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setDangerousForDiabetics(false);
                            } else {
                                ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setDangerousForDiabetics(true);
                            }
                        }

                        selectedrbtypefood = (RadioButton) findViewById(rgtypefood.getCheckedRadioButtonId());
                        String selectedTypesFood = selectedrbtypefood.getText().toString().trim();
                        for (int i = 0; i < ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().size(); i++) {
                            if (selectedTypesFood.equals(ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().get(i).getName())) {
                                selectedTypesFoodId = ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().get(i).getId();
                                ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm).setTyeps(selectedTypesFoodId);
                            }
                        }
                        Log.d(TAG, "indexxxxx         ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(finalIndex_meal)       xxxxxx    \n" + ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm));
                        //Need to create a function just for converting images
                        //     ImageUploadToServerFunction();
                        Service.ServiceAdmin.requestAdminUploadMeal("http://" + ServiceDataBaseHolder.getIP_PATH() + "/android_register_login/request_admin.php", NewMealActivity.this, "UPDATE_MEAL", ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm));
                        Log.d(TAG, "indexxxxx                  ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm)  \n" + ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm) + "                      ");
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().remove(first_Index_meal_not_confirm);
                        if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty()){
                            Log.d(TAG, "indexxxxx                     first_Index_meal_not_confirm   2 \n" + first_Index_meal_not_confirm + "                      i  ;");
                            //initialize new meal to see if have.
                          initializeOnAdminUploadUserMeal();
                        }else
                        {
                            returnToAdminCategory();
                        }
                    }else
                    {
                        returnToAdminCategory();
                    }
                }
            });

            adminBackToCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnToAdminCategory();
                }
            });

            adminDeleteMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty())
                    {
                        int  first_Index_meal_not_confirm  =0;
                        Service.ServiceAdmin.requestAdminUploadMeal("http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/request_admin.php" , NewMealActivity.this ,"DELETE_MEAL",   ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(first_Index_meal_not_confirm) );
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().remove(first_Index_meal_not_confirm);
                        if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty()){
                        initializeOnAdminUploadUserMeal();
                        }
                        else
                        {
                            returnToAdminCategory();
                        }
                    }else
                    {
                        returnToAdminCategory();
                    }
                }
            });
        }


        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                EnableRuntimePermissionToAccessCamera();
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);

            }
        });

        btn_ChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   its take image from user phone his library
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, RequestPermissionCode);


            }


        });
        SelectIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildIngredientForMeal();
            }
        });

        sendAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here i  toke all the data meal and send it in to json array in json file
                temp_name_meal = nameMeal.getText().toString();
                temp_Calories_meal = Calories.getText().toString();
                temp_descriptionMeal = descriptionMeal.getText().toString().trim();

                selectedrbdiabetes = (RadioButton) findViewById(rgdiabetes.getCheckedRadioButtonId());
                 if (rgdiabetes.getCheckedRadioButtonId() != -1)
                {
                    selectedDiabetes = selectedrbdiabetes.getText().toString().trim();
                    if (!selectedDiabetes.isEmpty()) {
                        String num = selectedrbdiabetes.getText().toString().trim();

                        if ("No".equals(num)) {
                            selectedDiabetes = "0";
                        } else {
                            selectedDiabetes = "1";
                        }
                    }

                }

                selectedrbtypefood = (RadioButton) findViewById(rgtypefood.getCheckedRadioButtonId());
                if (rgtypefood.getCheckedRadioButtonId() != -1)
                {
                String selectedTypesFood = selectedrbtypefood.getText().toString().trim();
                for (int i = 0; i < ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().size(); i++) {
                    if (selectedTypesFood.equals(ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().get(i).getName())) {
                        selectedTypesFoodId = ServiceDataBaseHolder.getTypesFood_arraylist_from_splashScreen().get(i).getId();
                    }
                }
                }
                if(temp_name_meal.isEmpty()||temp_Calories_meal.isEmpty()||temp_descriptionMeal.isEmpty())
                {
                    nameMeal.setError("Insert all the Data");
                }
                else
                {
                    Log.d(TAG, "indexxxxx        selecteddiabetes       xxxxxx    \n" + selectedDiabetes);
                    ImageUploadToServerFunction();
                    Log.d(TAG, "indexxxxx        temp_descriptionMeal       xxxxxx    \n" + temp_descriptionMeal);
                }

            }


        });
    }
public void returnToAdminCategory()
    {
        Toast.makeText(NewMealActivity.this, "back to menu dont have more meals to confirm", Toast.LENGTH_LONG).show();
        startActivity(new Intent(NewMealActivity.this, AdminCategoryActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_meal_menu, menu);
        return true;
    }


  //menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item1:
                startActivity(new Intent(NewMealActivity.this, MenuActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            case R.id.item2:
                startActivity(new Intent(NewMealActivity.this, HomeActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            case R.id.item3:
                Toast.makeText(this, "Change my details selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewMealActivity.this, ObjectiveActivity.class);
                intent.putExtra("user_mode", "update");
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            case R.id.item4:
                deleteOrContactRequestFromUser(NewMealActivity.this);
                return true;

            case R.id.item5:
                //clean the user data to login a new one
                Service.userLogout();
                startActivity(new Intent(NewMealActivity.this,LoginActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void BuildIngredientForMeal() {
        ingredients_name_id_count.clear();
        final String[] ingredients_items_name = new String[ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size()];
        final Integer[] ingredients_items_id = new Integer[ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size()];

        boolean[] checkedItems = new boolean[ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size()];
        final ArrayList<Integer> ingredients_selected_index = new ArrayList<>();


        for (int i = 0; i < ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size(); i++) {
            ingredients_items_name[i] = ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i).getName_ingredient();
            ingredients_items_id[i] = ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i).getId_ingredient();
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(NewMealActivity.this,R.style.AlertDialogCustom);

        builder.setTitle("Select Ingredients You Have In Your Meal");

        builder.setMultiChoiceItems(ingredients_items_name, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                // user checked or unchecked a box

                if (isChecked) {
                    if (!ingredients_selected_index.contains(position)) {
                        ingredients_selected_index.add(position);
                    } else {
                        ingredients_selected_index.remove(position);
                    }
                }
            }
        });
        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {


                //insert to array the ingredients are selected by the user
                for (int i = 0; i < ingredients_selected_index.size(); i++) {
                    ingredients_name_id_count.add(new IngredientsAndCount(ingredients_items_id[ingredients_selected_index.get(i)], ingredients_items_name[ingredients_selected_index.get(i)], ""));
                }


                if (!ingredients_name_id_count.isEmpty()) {

                    //user insert how many  ingredient he put for example 1 apple ,400g , 3 tsp , 1cup ,100ml....
                    for (int i = 0; i < ingredients_name_id_count.size(); i++) {
                        builder.setTitle("How many " + ingredients_name_id_count.get(i).getName_ingredient() + " do you have in your meal?  <like 100g , 1tsp , 1tbp , 100ml>");
                        builder.setMessage("");
                        final EditText userInput = new EditText(NewMealActivity.this);
                        userInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        userInput.setRawInputType(Configuration.KEYBOARDHIDDEN_NO);
                        builder.setView(userInput);


                        ///// put alert text to get how  many count for each ingredient
                        final int final_temp = i;
                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userInputValue = userInput.getText().toString();
                                ingredients_name_id_count.get(final_temp).setCount(userInputValue);

                                Log.d(TAG, "indexxxxx        name_ingredients_and_count        xxxxxx    \n" + ingredients_name_id_count + " all ");
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ingredients_name_id_count.get(final_temp).setCount("0");
                                Log.d(TAG, "indexxxxx       the  default is 0     xxxxxx    \n");
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        builder.show();

                    }


                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }




    // Star activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // resultCode his library
            case 1:
                if (resultCode == RESULT_OK && data != null) {

                    Uri selectedImage = data.getData();

                    //convert uri to bitmap
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(selectedImage.toString()));
                    } catch (Exception e) {
                        Log.d(TAG, "indexxxxx       convert uri to bitmap fails       xxxxxx    \n" + e.toString());   //handle exception
                    }

                    imageView.setImageBitmap(bitmap);
                }

                break;
// resultCode camera
            case 7:
                if (resultCode == RESULT_OK && data != null) {

                    Bundle extras = data.getExtras();
                  //  bitmap = (Bitmap) extras.get("data");
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                }

                break;
        }
    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(NewMealActivity.this, Manifest.permission.CAMERA)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(NewMealActivity.this, "CAMERA permission allows ", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(NewMealActivity.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    // Upload captured image online on server function.
        public void ImageUploadToServerFunction() {
            ByteArrayOutputStream byteArrayOutputStreamObject;
            byteArrayOutputStreamObject = new ByteArrayOutputStream();

            // Converting bitmap image to jpeg format, so by default image will upload in jpeg format. //PNG change
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);


        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(NewMealActivity.this, "Meal is Uploading", "Please Wait", false, false);

            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Log.d(TAG, "indexxxxx       onPostExecute       xxxxxx    \n" + string);
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(NewMealActivity.this, "your meal upload to admin  send you mail to final confirm " + string, Toast.LENGTH_LONG).show();
                Log.d(TAG, "indexxxxx       onPostExecute end       xxxxxx    \n");
                // Setting image as transparent after done uploading.
                imageView.setImageResource(android.R.color.transparent);

//                    Intent i = new Intent(NewMealActivity.this, HomeActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


            }

            @Override
            protected String doInBackground(Void... params) {
                Log.d(TAG, "indexxxxx       in        xxxxxx    \n");


                NewMealActivity.ImageProcessClass imageProcessClass = new ImageProcessClass();

                Date today = new Date();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa", Locale.ENGLISH);
                String today_date = dateFormat.format(today);


                HashMap<String, String> HashMapParams = new HashMap<>();
                //  meal try_send_array=new  meal(temp_name_meal,temp_descriptionMeal,55.0);
                //   HashMapParams.put("try_send_array", try_send_array.toString());

                HashMapParams.put("user_id", "" + ServiceDataBaseHolder.getUser_confirm().getId());
                HashMapParams.put("name_meal", temp_name_meal);
                HashMapParams.put("calories", temp_Calories_meal);
                HashMapParams.put("description", temp_descriptionMeal);
                HashMapParams.put("dangerous_for_diabetics", selectedDiabetes);
                HashMapParams.put("types_food", "" + selectedTypesFoodId);
                Log.d(TAG, "indexxxxx        ingredients_name_id_count.toString()        xxxxxx    \n" + ingredients_name_id_count.toString() + "    all ");


                for (int i = 0; i < ingredients_name_id_count.size(); i++) {
                    HashMapParams.put("id_ingredient_" + i, "" + ingredients_name_id_count.get(i).getId_ingredient());
                    HashMapParams.put("count_" + i, ingredients_name_id_count.get(i).getCount());
                }
                HashMapParams.put("size", "" + ingredients_name_id_count.size());
                HashMapParams.put("date_upload", today_date);
                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);


                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
                Log.d(TAG, "indexxxxx       FinalData       xxxxxx    \n" + FinalData);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject = new StringBuilder();


            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                    Log.d(TAG, "indexxxxx       stringBuilderObject  1   xxxxxx    \n"+stringBuilderObject.toString());
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                 Log.d(TAG, "indexxxxx       stringBuilderObject    2  xxxxxx    \n"+stringBuilderObject.toString());
                stringBuilderObject.append("=");
                     Log.d(TAG, "indexxxxx       stringBuilderObject    2  xxxxxx    \n"+stringBuilderObject.toString());
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                     Log.d(TAG, "indexxxxx       stringBuilderObject    3 xxxxxx    \n"+stringBuilderObject.toString());
            }
            return stringBuilderObject.toString();
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
//
//        switch (RC) {
//
//            case RequestPermissionCode:
//
//                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
//
//                //    Toast.makeText(NewMealActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//
//                } else {
//             //       Toast.makeText(NewMealActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }

    public void initializeOnAdminUploadUserMeal() {

        int firstIndexInArray =0;
        StringBuilder temp_Ingredient_for_admin = new StringBuilder();

                nameMeal.setText(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getNameMeal());
                descriptionMeal.setText(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getDescription());
                String temp_string_calories = ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getCalories().toString();
                Calories.setText(temp_string_calories);
                imageView.setImageBitmap(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getImage());
                for (int j = 0; j < ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getIngredientsCounts().size(); j++) {

                    temp_Ingredient_for_admin.append(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getIngredientsCounts().get(j).toString()).append("\n");
                    Log.d(TAG, "indexxxxx                   (ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getIngredientsCounts().get(j).toString())    \n"+             (ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getIngredientsCounts().get(j).toString()) + "                      ;");

                }
                Ingredient_for_admin.setText(temp_Ingredient_for_admin.toString());
                Log.d(TAG, "indexxxxx                     ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(i).getIngredientsCounts().size()    \n"+              ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getIngredientsCounts().size() + "                      ;");



                if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getDangerousForDiabetics())
                        ((RadioButton)rgdiabetes.getChildAt(1)).setChecked(true);
                else {
                    ((RadioButton)rgdiabetes.getChildAt(0)).setChecked(true);
                }

                switch(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(firstIndexInArray).getTyeps()) {
                    case 1:
                        ((RadioButton)rgtypefood.getChildAt(0)).setChecked(true);
                        break;
                    case 2:
                        ((RadioButton)rgtypefood.getChildAt(1)).setChecked(true);
                        break;
                    case 3:
                        ((RadioButton)rgtypefood.getChildAt(2)).setChecked(true);
                        break;
                    case 4:
                        ((RadioButton)rgtypefood.getChildAt(3)).setChecked(true);
                        break;
                    case 5:
                        ((RadioButton)rgtypefood.getChildAt(4)).setChecked(true);
                        break;
                    case 6:
                        ((RadioButton)rgtypefood.getChildAt(5)).setChecked(true);
                        break;
                    default:
                        break;
                }


    }


    }



