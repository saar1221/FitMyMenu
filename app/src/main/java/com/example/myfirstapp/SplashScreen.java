package com.example.myfirstapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.*;

import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static final String URL_IMPORT_DB = "http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/import_database.php";//192.168.1.25
    private static final String TAG = "SplashScreen";
    private static String temp_checker;
    private static Boolean temp_bool_Dangerous_for_diabetics_checker;

    private static Animation slideDown, slideDown1;
    private  RelativeLayout layout_splash1;
    private static   ProgressDialog p;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ////        // i can delete it -------
//        //temp activity to login need to delete is its only to get fast loading page
//       new PrefetchData().execute();
//        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
//        startActivity(i);
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        finish();
////        // i can delete it -------

        /**
         * Showing splashscreen Animation and sound while making network calls to download necessary like
         * data before launching the app Will use AsyncTask to make http call
         */
//        final MediaPlayer mediaPlayer = MediaPlayer.create(SplashScreen.this, R.raw.sound1);
//       mediaPlayer.start();
   //
        slideDown = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.text);
        slideDown1= AnimationUtils.loadAnimation(SplashScreen.this,R.anim.text1);
        layout_splash1 =(RelativeLayout)findViewById(R.id.layout_splash);
        layout_splash1.setAnimation(slideDown);



        Handler handler1 =new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_splash1.setAnimation(slideDown1);
            }
        },1650);


        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             //   mediaPlayer.stop();
                new PrefetchData().execute();
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
                // close this activity

            }
        },2800);

    }

    // making http calls using string request (volley)
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            p = new ProgressDialog(SplashScreen.this,R.anim.text1);
//            p.setMessage("Please wait...It is downloading");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /**
             * here i make http call here This call will download required data
             * before launching the app
             * example:
             * 1. Downloading and storing in SQLite
             * 2. Downloading images
             * 3. Fetching and parsing the  json that id get from php
             * 4. Sending device information to server
             * 5. etc.,
             */
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IMPORT_DB,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<ingredients> ingredients = new ArrayList<>();
                                ArrayList<typesFood> types_food = new ArrayList<>();
                                ArrayList<allergies> allergies = new ArrayList<>();
                                ArrayList<meal> meals = new ArrayList<>();
                                 ArrayList<IngredientsAndCount> IngredientsAndCount = new ArrayList<> ();


                         //      Log.d(TAG, "xxxxxx        response        xxxxxx    \n" + response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");


                                if (success.equals("1")) {

//                                    JSONArray jsonArray_users_menus = jsonObject.getJSONArray("users_menus");
//                                    // insert all the ingredients parameters from database into "ingredients array list"
//                                    for (int i = 0; i < jsonArray_users_menus.length(); i++) {
//                                        JSONObject object = jsonArray_users_menus.getJSONObject(i);
//                                        ingredients ingredientss = new ingredients();
//                                    }


                                    JSONArray jsonArray_ingredients = jsonObject.getJSONArray("ingredients");
                                    // insert all the ingredients parameters from database into "ingredients array list"
                                    for (int i = 0; i < jsonArray_ingredients.length(); i++) {
                                        JSONObject object = jsonArray_ingredients.getJSONObject(i);
                                        ingredients ingredientss = new ingredients(object.getInt("id"), object.getString("name"));
                                        ingredients.add(ingredientss);
                                    }

                                    JSONArray jsonArray_types_food = jsonObject.getJSONArray("types");
                                    // insert all the types_food parameters from database into "types_food array list"
                                    for (int i = 0; i < jsonArray_types_food.length(); i++) {
                                        JSONObject object = jsonArray_types_food.getJSONObject(i);
                                        typesFood types_foodd = new typesFood(object.getInt("id"), object.getString("name"));
                                        types_food.add(types_foodd);
                                    }

                                    JSONArray jsonArray_allergies = jsonObject.getJSONArray("allergies");
                                    // insert all the allergies parameters from database into "allergies array list"
                                    for (int i = 0; i < jsonArray_allergies.length(); i++) {
                                        JSONObject object = jsonArray_allergies.getJSONObject(i);
                                        allergies allergiess = new allergies(object.getInt("id"), object.getString("name"));
                                        allergies.add(allergiess);
                                    }

                                    JSONArray jsonArray_meals = jsonObject.getJSONArray("meals");
                                    // insert all the meals parameters from database into "meals array list"
                                    for (int i = 0; i < jsonArray_meals.length(); i++) {
                                        JSONObject object = jsonArray_meals.getJSONObject(i);


                                        temp_checker = object.getString("Dangerous_for_diabetics");
                                        if (temp_checker.equals("1"))
                                            temp_bool_Dangerous_for_diabetics_checker = true;
                                        else
                                            temp_bool_Dangerous_for_diabetics_checker = false;

                                        temp_checker = object.getString("confirm_by_admin");
                                        if (temp_checker.equals("1")) {

                                            String transfer_ip = "   http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/images/"+object.getInt("id") +".png";
                                            Service.getImage(transfer_ip, SplashScreen.this,i,object.getInt("id"),"USER" );
                                            meal temp_meals = new meal(object.getInt("id"), object.getString("name"), object.getString("description"), object.getDouble("calories")
                                                    , object.getInt("id_types"), object.getInt("id_user_upload"), temp_bool_Dangerous_for_diabetics_checker, true,object.getInt("total"));
                                     Log.d(TAG, "indexxxxx        temp_meals        xxxxxx    \n" +temp_meals);

                                            meals.add(temp_meals);

                                        }
                                    }


                                    for(int j=0;j<meals.size();j++) {
                                    JSONArray jsonArray_meals_consist = jsonObject.getJSONArray("meals_consist");
                                    // insert all the allergies parameters from database into "allergies array list"
                                    for (int i = 0; i <jsonArray_meals_consist.length(); i++) {
                                        JSONObject object = jsonArray_meals_consist.getJSONObject(i);
                                        if(meals.get(j).getIdMeal()==object.getInt("id_meal")) {

                                            for(int k=0;k<ingredients.size();k++) {
                                                if(ingredients.get(k).getId_ingredient()==object.getInt("id_ingredients"))
                                                IngredientsAndCount.add(new IngredientsAndCount(object.getInt("id_ingredients"),ingredients.get(k).getName_ingredient(), object.getString("count")));
                                            }
                                        }
                                    }

                                        meals.get(j).setIngredientsCounts(IngredientsAndCount);
                                        IngredientsAndCount.clear();

//                               Log.d(TAG, "xxxxxx             meals.get(j)        xxxxxx    \n" +      meals.get(j));
                                    }

                               }



//                                 Log.d(TAG, "xxxxxx        ingredients        xxxxxx    \n"+ingredients);
//                                Log.d(TAG, " xxxxxx       allergies        xxxxxx    \n" + allergies);
//                                Log.d(TAG, "xxxxxx        types_food        xxxxxx    \n" + types_food);
//                                Log.d(TAG, "xxxxxx        meals        xxxxxx    \n" + meals);


                                // sent all the data into the  ServiceDataBaseHolder And stores them there
                                ServiceDataBaseHolder.setIngredients_arraylist_from_splashScreen(ingredients);
                                ServiceDataBaseHolder.setAllergies_arraylist_from_splashScreen(allergies);
                                ServiceDataBaseHolder.setTypesFood_arraylist_from_splashScreen(types_food);
                                ServiceDataBaseHolder.setMeals_arraylist_from_splashScreen(meals);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "       JSONException        xxxxxx    \n" + e);
                                Toast.makeText(SplashScreen.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SplashScreen.this, "Error" + error.toString() +"doesn't", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "        onErrorResponse        xxxxxx    \n" + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(SplashScreen.this);
            requestQueue.add(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//               p.hide();



            // After completing http call
            // will close this activity and start login activity


        }

    }


    }




