package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminCategoryActivity extends AppCompatActivity {
    private static  String URL_REQUEST_ADMIN="http://192.168.67.123/android_register_login/request_admin.php";
    private EditText email_or_ingredient;
    private Button delete_user, add_ingredient ,  upload_new_meal;
    private static final String TAG = "AdminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
 ServiceDataBaseHolder.getUser_confirm().setId(1);
        email_or_ingredient=findViewById(R.id.email_or_ingredients);
        delete_user=findViewById(R.id.btn_delete_user);
        add_ingredient=findViewById(R.id.add_ingredient);
        upload_new_meal=findViewById(R.id.upload_new_meal);



        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail =email_or_ingredient.getText().toString().trim();

                if(!mEmail.isEmpty()) {
                    Service.ServiceAdmin.requestAdmin(URL_REQUEST_ADMIN , AdminCategoryActivity.this ,"DELETE_USER",mEmail );
                    email_or_ingredient.setText("");
                }
                else{
                    email_or_ingredient.setError("Please Insert Email");
            }
        }});

        add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mIngredient =email_or_ingredient.getText().toString().trim();

                if(!mIngredient.isEmpty()) {
                    Service.ServiceAdmin.requestAdmin(URL_REQUEST_ADMIN , AdminCategoryActivity.this ,"ADD_INGREDIENT",mIngredient );
                    email_or_ingredient.setText("");
                }
                else{
                    email_or_ingredient.setError("Please Insert Ingredient");
                }
            }});

        upload_new_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().isEmpty()) {
                    startActivity(new Intent(AdminCategoryActivity.this, NewMealActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else
                {
                    Toast.makeText(AdminCategoryActivity.this, " dont have more meals to confirm", Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.d(TAG, "indexxxxx        getMeals_arraylist_from_splashScreen        xxxxxx    \n\n" + ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            //  item1 its the camera icon in the option menu
            case R.id.item1: {
                //clean he user to login anew one
        Service.userLogout();
                startActivity(new Intent(AdminCategoryActivity.this,SplashScreen.class));
                //nice anim slide left to right the page
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}