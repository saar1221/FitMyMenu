package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {

    private EditText email,password;
    private Button btn_login;
    private ProgressBar loading;
    private static  String URL_LOGIN="http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/login.php";
    private static  String URL_LOGIN_ADMIN="http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/import_database_admin.php";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // take the array list of all the data on the user type food diabetics allergies and send it to filtering


        loading=findViewById(R.id.loading);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btn_login=findViewById(R.id.btn_login);
        TextView link_register = findViewById(R.id.link_regist);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail =email.getText().toString().trim();
                String mPass =password.getText().toString().trim();
                /**check if have in the edit text value
                 * if not send alert else run login
                 */
                if(!mEmail.isEmpty() || !mPass.isEmpty()) {
                    Login(mEmail,mPass);
                }
                else{
                    email.setError("Please Insert Email");
                    password.setError("Please Insert Password");
                }
            }
        });

        link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nice anim slide left to right the page and move to RegisterActivity
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }

    /**
     * @param email -email user
     * @param password -password user
     * here i make the login send the email and password to the DB and check if the connection success
     * if success return success="1" and bring all information we will need on the user
     * user_allergies  ,  use info     , user_meals       , users_like_him for algo
     */
    private void Login(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //
                            int temp_id_user;
                            final int id_admin=1;
                            String temp_string;
                            boolean temp_boolean;
                            boolean flag_if_user_fill_all_data=false;
                            String success_admin;
                            ArrayList<Record> users_like_me =new ArrayList<>();

                           Log.d(TAG, "xxxxxx        response        xxxxxx    \n"+ response );
                            JSONObject jsonObject =new JSONObject(response);
                            String success=jsonObject.getString( "success");
                            JSONArray jsonArrayLogin =jsonObject.getJSONArray("login");


                            if(success.equals("admin")) {
                                //if admin login
                            JSONObject object_admin = jsonArrayLogin.getJSONObject(0);
                            if(id_admin==object_admin.getInt("id"));
                            {
                                users admin = new users();
                                admin.setId(id_admin);
                                ServiceDataBaseHolder.setUser_confirm(admin);
                                success_admin=  Service.ServiceAdmin.bringMyAllDataBase(URL_LOGIN_ADMIN,LoginActivity.this);
                                Log.d(TAG, "indexxxxx        success_admin        xxxxxx    \n"+ success_admin );
                                if(success_admin.equals("success"))
                                {
                                    /** make the delay of 0.5 sec to take all the database upload
                                     * success_admin.equals("success") its mean the all the database is upload successful to the app
                                     * and its open new Activity for admin
                                     */
                                Handler handler1 =new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, AdminCategoryActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        },2000);//2000
                                }

                            }
                            }

                       else if(success.equals("1"))
                        {
                            JSONArray jsonArrayUsers =jsonObject.getJSONArray("users");
                            for(int i=0;i<jsonArrayLogin.length();i++){
                                JSONObject object = jsonArrayLogin.getJSONObject(i);

                                //get the id from php login if we get the id so the login success
                                temp_id_user=object.getInt("id");

                                ///  double  and boolean can not be null so i convert to temp parameters and push the data in user constractor
                                temp_string=object.getString("csv_file");
                                if(temp_string.equals("1"))
                                    temp_boolean=true;
                                else
                                    temp_boolean=false;

                                temp_string= object.getString("diabetes");
                                if(temp_string.equals("Yes"))
                                    temp_boolean=true;
                                else if(temp_string.equals("No"))
                                    temp_boolean=false;
                                else
                                    temp_boolean=false;

                                //If the user has not yet filled in all his details
                                //open the next page objective activity to fill up more information
                                if(object.getString("weight").equals("null")||object.getString("height").equals("null")||object.getString("age").equals("null")||object.getString("id_types_food").equals("null")||object.getString("gender").equals("null"))
                                {
                                    users temp_user = new users(temp_id_user,object.getString("name"),object.getString("email"),0.0,0.0,0,object.getString("purpose"),1,temp_boolean,object.getString("gender"),temp_boolean,1);
                                    ServiceDataBaseHolder.setUser_confirm(temp_user);
                                    Intent  intent =new Intent(LoginActivity.this, ObjectiveActivity.class);
                                    intent.putExtra("user_mode", "update");
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    Toast.makeText(LoginActivity.this, "Fill in More details", Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    //build the user and put him in ServiceDataBaseHolder.setUser_confirm
                                    users temp_user = new users(temp_id_user,object.getString("name"),object.getString("email"),object.getDouble("weight"),object.getDouble("height"),object.getInt("age"),object.getString("purpose"),object.getInt("level_activity"),temp_boolean,object.getString("gender"),temp_boolean,object.getInt("id_types_food"));
                                    ServiceDataBaseHolder.setUser_confirm(temp_user);
                                    flag_if_user_fill_all_data=true;
                                   // Log.d(TAG, "xxxxxx        temp_user        xxxxxx    \n"+ temp_user );
                                }




                         }


                            // insert all the user_allergies parameters from database into the user confirm
                            ArrayList<allergies> temp_user_allergies =new ArrayList<>();
                            JSONArray jsonArrayUserAllergies = jsonObject.getJSONArray("user_allergies");
                            for(int j=0;j<ServiceDataBaseHolder.getAllergies_arraylist_from_splashScreen().size();j++) {
                                for (int i = 0; i < jsonArrayUserAllergies.length(); i++) {
                                    JSONObject object = jsonArrayUserAllergies.getJSONObject(i);
                                        if(ServiceDataBaseHolder.getAllergies_arraylist_from_splashScreen().get(j).getId()== object.getInt("id_allergies"))
                                        {
                                             temp_user_allergies.add (new allergies(object.getInt("id_allergies"), ServiceDataBaseHolder.getAllergies_arraylist_from_splashScreen().get(j).getName()));
                                        }
                                    }
                                }

                            ServiceDataBaseHolder.getUser_confirm().setUser_allergies(temp_user_allergies);
                            Log.d(TAG, "xxxxxx         ServiceDataBaseHolder.getUser_confirm()        xxxxxx    \n" +   ServiceDataBaseHolder.getUser_confirm());



                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);

 //If the user has filled in all his details flag_if_user_fill_all_data=true
                if(flag_if_user_fill_all_data)
{
    for (int i = 0; i < jsonArrayUsers.length(); i++) {
        JSONObject object = jsonArrayUsers.getJSONObject(i);
        //get all the users is like the user plus minus his weight height age and exactly his purpose diabetes and  types his food
        ArrayList<Integer> id_meals_array = new ArrayList<>();

        JSONArray array_id_mels = object.getJSONArray("id_meals");
        for (int j = 0; j < array_id_mels.length(); j++) {

            JSONObject object1 = array_id_mels.getJSONObject(j);
            id_meals_array.add(object1.getInt("id_meal"));
        }
        users_like_me.add(new Record(object.getInt("id"), object.getInt("age"), object.getDouble("weight"), object.getDouble("height"), id_meals_array));
        Log.d(TAG, "indexxxxx       users_like_me       xxxxxx    \n" + users_like_me);

    }
/**
 *set user like him in  ServiceDataBaseHolder.setUsers_like_me_for_algo to use next
 *load meals the user selected if day not pass (24 hours)
 * call function filter all the meals he dont need (if he have allergies or diabetes)
 * open next page home activity
 */
    ServiceDataBaseHolder.setUsers_like_me_for_algo(users_like_me);
    Service.loadMealSelectedFromTextFile("mealSelected" + ServiceDataBaseHolder.getUser_confirm().getId() + ".txt", LoginActivity.this);
    Service.filterMealsForUserCallAllFunction(ServiceDataBaseHolder.getUser_confirm(), LoginActivity.this);
    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
}


                        }
                        else if( success.equals("mistake")){
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this,"inaccurate email or password",Toast.LENGTH_LONG).show();
                        }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "xxxxxx        JSONException        xxxxxx    \n"+ e );
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }





}
