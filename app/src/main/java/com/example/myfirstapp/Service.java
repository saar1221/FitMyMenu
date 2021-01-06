package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
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
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Service implements Serializable {

    private static  Bitmap bitmap=null;
    private static  String URL_ADD_MEAL_SELECTED_TO_DB ="http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/users_menus.php";
    /**
     *  Calculated the  user_confirm BMR and set the it on the menu bar (tvObjective)
     *call two functions that update the menu bar calories
     */


        //call function that calculate the Objective food he need to eat
        public static Double CalculatedBMR(users userData){

            double weight=userData.getWeight();
            double height= userData.getHeight();
            Integer age=userData.getAge();
            String  purpose=userData.getPurpose();
            Integer activity =userData.getLevel_activity();
            double calories = 0.0;

//      parametes for male
            final double s_male=5.0;

//      parametes for female
            final double s_female=-161.0;

            // if the user a Male   else female
            if(userData.getGender().equals("Male"))
            {
                calories=s_male+(10 * weight)+(6.25 * height)-(5 * age);
            }
            else
            {
                calories=s_female+(10 * weight)+(6.25 * height)-(5 * age);
            }


            if(activity.equals(2))
                calories=calories*1.375;
            else  if(activity.equals(3))
                calories=calories*1.55;
            else  if(activity.equals(4))
                calories=calories*1.725;
            else  if(activity.equals(5))
                calories=calories*1.9;
            else
                calories=calories*1.2;



            if(purpose.equals("Gain Weight"))
                calories=calories+300.0;
            else  if(purpose.equals("Losing Weight"))
                calories= calories-300.0;
                //   purpose.equals("Stable Weight")
            else
                calories= calories-00.0;
            
            //call function that calculate the food he selected to eat and the calories remain
            return calories;
        }

    //calculate the Calories all the meals are selected and update the menu bar
        public static Double CalculatedFoodCalories(ArrayList<meal> arrayCalories )
        {
            double tempOldCalories = 0.0;
            for(int i=0;i<arrayCalories.size();i++) {
                String replaced = arrayCalories.get(i).getCalories().toString().replace("calories:", "");
                tempOldCalories=tempOldCalories + Double.parseDouble(replaced);
            }
            return tempOldCalories;
        }


    //convert the CaloriesFood to String and update the menu bar
    public static String updateCaloriesFood() {
        double caloriesFood;
        String tempFoodCaloris="";
        if(!ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().isEmpty())
        {
            caloriesFood =  Service.CalculatedFoodCalories(ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist());
        }
        else
        {
            caloriesFood = 0;
        }
        DecimalFormat dec = new DecimalFormat("# cal");
        tempFoodCaloris = dec.format(caloriesFood);
        return  tempFoodCaloris;

    }
//convert the objective to String and update the menu bar
    public static String updateMenuBarObjective(){
        double  caloris=Service.CalculatedBMR(ServiceDataBaseHolder.getUser_confirm());
        DecimalFormat dec = new DecimalFormat("# cal");
        return dec.format(caloris);
    }

// update the remaining in the bar home activity and menu activity
    public static String updateRemaining(String objective, String food_selected){
        Double tempObjective = Double.valueOf(objective);
        Double tempFood = Double.valueOf(food_selected);
        Double remining =tempObjective -tempFood;
        DecimalFormat dec = new DecimalFormat("# cal");

        return dec.format(remining);
    }

    public static void setMealsSelectedInBarMenuFromTextFile(Integer id_meal) {
        for (int j = 0; j < ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().size(); j++) {
            if (ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(j).getIdMeal().equals(id_meal))
                ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().add(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(j));
        }
    }

    // set user_confirm and call all the filter functions
    public static void filterMealsForUserCallAllFunction(users user, Context ctx){
        if(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(0).getIdMeal()==1)
            ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().remove(0);
        /// insert all the meals are confirm
        filterMealsAreConfirm();
        /// filter all the meals are confirm if he have or not Diabetes
        filterMealsForUserDiabetes (user);
        //filter all the meals types food like the user
        filterMealsForUserTypesFood(ctx,user);
        //filter all the meals are danger for the user
        filterIfTheUserHaveAllergies(ctx,user);
    }

// remove all the meals that admin not confirm yet
    private static void filterMealsAreConfirm() {
        ArrayList<meal> temp_meals_confirm=new ArrayList<>();
        for(int i=0 ;i<ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().size();i++) {
            if(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(i).getAdminConfirm())
                temp_meals_confirm.add(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(i));
        }
        ServiceDataBaseHolder.setMeals_are_filter(temp_meals_confirm);
    }

//filter the meals are not needed for the user other types food else if the user select eat all
    private static void filterMealsForUserTypesFood(Context ctx,users user) {
        ArrayList<meal> temp_meals_types_food=new ArrayList<>();
        int id_types_meal;
        int id_types_user=user.getId_types_food();
        for(int i=1 ;i<ServiceDataBaseHolder.getMeals_are_filter().size();i++)
        {
            id_types_meal = ServiceDataBaseHolder.getMeals_are_filter().get(i).getTyeps();
    if(id_types_user == 6){
        i=ServiceDataBaseHolder.getMeals_are_filter().size();
    }
    else if(id_types_user == 5 && id_types_meal ==5)
    {
        temp_meals_types_food.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
    }
    else if(id_types_user == 4 && id_types_meal ==4)
    {
        temp_meals_types_food.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
    }
    else if(id_types_user == 3 && id_types_meal ==3)
    {
        temp_meals_types_food.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
    }
    else if(id_types_user == 2 && id_types_meal ==2)
    {
        temp_meals_types_food.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
    }
    else if(id_types_user == 1 && id_types_meal ==1)
    {
        temp_meals_types_food.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
    }
        }
        if(!temp_meals_types_food.isEmpty())
        ServiceDataBaseHolder.setMeals_are_filter(temp_meals_types_food);
    }

//function check if the user have diabetics so remove all the meals are dangerous for diabetics ijd not bring all the meals
    public static void filterMealsForUserDiabetes(users user)
    {
        ArrayList<meal> meals_have_or_dosent_have_diabetes=new ArrayList<>();
        for(int i=0 ;i<ServiceDataBaseHolder.getMeals_are_filter().size();i++)
        {
            if(user.getDiabetes().equals(true)&& !ServiceDataBaseHolder.getMeals_are_filter().get(i).getDangerousForDiabetics().equals(true))
            {
                meals_have_or_dosent_have_diabetes.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
            }
            else if(user.getDiabetes().equals(false)&& ServiceDataBaseHolder.getMeals_are_filter().get(i).getDangerousForDiabetics().equals(false) )
            {
                meals_have_or_dosent_have_diabetes.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
            }
            else if(user.getDiabetes().equals(false)&& ServiceDataBaseHolder.getMeals_are_filter().get(i).getDangerousForDiabetics().equals(true) )
            {
                meals_have_or_dosent_have_diabetes.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
            }

        }
        ServiceDataBaseHolder.setMeals_are_filter(meals_have_or_dosent_have_diabetes);
    }

    /**
     * A function that will filter out all meals that endanger the user if he has allergies
     * She will take from the allergy system xml all the ingredients that endanger him and show him them without the endangered doses
     */
    private static void filterIfTheUserHaveAllergies(Context ctx, users user) {
       ArrayList<String> ingredient_name_are_dangers = new ArrayList<>();
        ArrayList<meal> temp_meals_are_filter=new ArrayList<>();


        if(!user.getUser_allergies().isEmpty())
        {
            for(int i =0;i<ServiceDataBaseHolder.getAllergies_arraylist_from_splashScreen().size();i++)
        {
            String temp_name_allergies=  ServiceDataBaseHolder.getAllergies_arraylist_from_splashScreen().get(i).getName();
            for(int j=0;j<user.getUser_allergies().size();j++)
            {
                if(user.getUser_allergies().get(j).getName().equals(temp_name_allergies))
                {
                    switch(user.getUser_allergies().get(j).getName()) {
                        case "Lactose":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Lactose)));
                            break;
                        case "Gluten":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Gluten)));
                            break;
                        case "Sesame":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Sesame)));
                            break;
                        case "Soy":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Soy)));
                            break;
                        case "Nuts":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Nuts)));
                            break;
                        case "Egg":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Egg)));
                            break;
                        case "Fish":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Fish)));
                            break;
                        case "Sugar":
                            ingredient_name_are_dangers.addAll(Arrays.asList(ctx.getResources().getStringArray(R.array.Sugar)));
                            break;
                    }

                }
            }
          
        }
// i is the meal
            Log.d(ctx.toString(), "indexxxxx         ServiceDataBaseHolder.getMeals_are_filter().size()      xxxxxx    \n" +ServiceDataBaseHolder.getMeals_are_filter().size()+"\n" );
            for(int i=0 ;i<ServiceDataBaseHolder.getMeals_are_filter().size();i++)
            {
                boolean flag_if_danger=false;

//
                //j is the Ingredients in the meal consist
                for(int j = 0; j<ServiceDataBaseHolder.getMeals_are_filter().get(i).getIngredientsCounts().size(); j++)
                {
                    //k its the in Ingredients are dangers  in ingredient_name_are_dangers array from allergies.xml
             for(int k=0;k<ingredient_name_are_dangers.size();k++ )
             {
                 // if the danger Ingredients are include the meal consist Ingredients
                if(ingredient_name_are_dangers.get(k).equals(ServiceDataBaseHolder.getMeals_are_filter().get(i).getIngredientsCounts().get(j).name_ingredient))
                  {
                      //flag equal true have danger in the meal
                      flag_if_danger=true;
                }
             }
                }
                //if after i Search the Ingredients are danger in the meal adn didnt find insert to temp_meals_are_filter
                if(!flag_if_danger)
                {
                    temp_meals_are_filter.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
                }
            }
            ServiceDataBaseHolder.setMeals_are_filter(temp_meals_are_filter);
        }

    }
//get array id_meals from the user algo and send back all the meals
public static ArrayList<meal>returnMealsLikeMainUser(ArrayList<Integer>id_meals_array)
{
    ArrayList<meal> final_meals_most_popular_like_use =new ArrayList<>();
    for(int i =0 ;i<ServiceDataBaseHolder.getMeals_are_filter().size();i++)
    {
        for(int j=0;j<id_meals_array.size();j++)
        {
            if(ServiceDataBaseHolder.getMeals_are_filter().get(i).getIdMeal().equals(id_meals_array.get(j)))
            {
                final_meals_most_popular_like_use.add(ServiceDataBaseHolder.getMeals_are_filter().get(i));
            }
        }
    }
    return final_meals_most_popular_like_use;
}

//same meals and time in  the user phone
    public static void saveMealSelectedToTextFile(String FILE_NAME_MEAL_SELECTED , Integer id_meal, Context ctx ) {
        List<String> old_meals_in_text =new  ArrayList<>();
        old_meals_in_text.clear();
        // From Date to String
        //Displaying current date and time in 12 hour format with AM/PM
        //   dateFormat.getTimeZone();  dose't support on israel time so i put Locale.ENGLISH
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa",Locale.ENGLISH);
        FileInputStream fis = null;
        try {
            fis = ctx.openFileInput(FILE_NAME_MEAL_SELECTED);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text_line;

            while ((text_line = br.readLine()) != null) {
                text_line =sb.append(text_line).append("\n").toString();
                sb.delete(0,26); // delete the "\n" if id_ meal
                old_meals_in_text.add(text_line);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder text = new StringBuilder();
        try {

            Date today_date = Calendar.getInstance().getTime();
            String dateTime = dateFormat.format(today_date);
            text.append(dateTime).append(" ,").append(id_meal).append("\n");
            //  Log.d(TAG, "indexxxxx    dateTime      xxxxxx    \n"+dateTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            //  removeTextMenuFile("mealSelected"+ServiceDataBaseHolder.getUser_confirm().getId()+".txt");
            fos = ctx.openFileOutput(FILE_NAME_MEAL_SELECTED, Context.MODE_PRIVATE);

            for (int i = 0; i < old_meals_in_text.size(); i++) {
                fos.write(old_meals_in_text.get(i).getBytes());
                Log.d(ctx.toString(), "indexxxxx    old_meals_in_text.get(i)      xxxxxx    \n" + old_meals_in_text.get(i));
            }
            old_meals_in_text.clear();
            Log.d(ctx.toString(), "indexxxxx   old_meals_in_text  clear     xxxxxx    \n" +old_meals_in_text);

            fos.write(text.toString().getBytes());
            //   Toast.makeText(ctx, "Saved to " + ctx.getFilesDir() + "/" + FILE_NAME_MEAL_SELECTED, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// load meals from text file in the user phone

    public static  void loadMealSelectedFromTextFile(String FILE_NAME_MEAL_SELECTED , Context ctx) {

        FileInputStream fis = null;
        try {
            fis = ctx.openFileInput(FILE_NAME_MEAL_SELECTED);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                Log.d(ctx.toString(), "indexxxxx    text    xxxxxx    \n"+text);
                //     From text to String and string to Date
                String dtStart =  sb.append(text).toString();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa", Locale.ENGLISH);

                try {
                    Date old_date = dateFormat.parse(dtStart);
                    Calendar today = Calendar.getInstance();

                    Calendar thatDay = Calendar.getInstance();
                    thatDay.set(Calendar.DAY_OF_MONTH,old_date.getDate());
                    thatDay.set(Calendar.MONTH,old_date.getMonth()); // 0-11 so 1 less
                    thatDay.set(Calendar.YEAR,2020);
                    thatDay.set(Calendar.SECOND, old_date.getSeconds());
                    thatDay.set(Calendar.MINUTE, old_date.getMinutes());
                    thatDay.set(Calendar.HOUR_OF_DAY, old_date.getHours());

                    long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();

// 86400000 MILLISECOND its 24 hours
                    if(diff>=86400000 )//its no ok A day has passed need to delete the meal id and time from text
                    {
                        //call to remove the id meal and time
                        FileOutputStream fos = null;
                        try {
                            fos = ctx.openFileOutput(FILE_NAME_MEAL_SELECTED, Context.MODE_PRIVATE);
                            String nothing= "";
                            fos.write(nothing.getBytes());

                        //    Log.d(ctx.toString(), "indexxxxx    delete the meal     xxxxxx    \n");
                           //   fos.write(text.toString().getBytes());
                         //   Toast.makeText(ctx, "delete to " + ctx.getFilesDir() + "/" + FILE_NAME_MEAL_SELECTED, Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.d(ctx.toString(), "indexxxxx        A day has passed      xxxxxx    \n");
                    }
                    else {
                        Log.d(ctx.toString(), "indexxxxx       A day didn't passed       xxxxxx    \n");
                 //    removeTextMenuFile("mealSelected"+ServiceDataBaseHolder.getUser_confirm().getId()+".txt",ctx);
                        int take_id_meal= Integer.parseInt(text.substring(21));//22
                        Service.setMealsSelectedInBarMenuFromTextFile(take_id_meal);
                        Log.d(ctx.toString(), "indexxxxx      take_id_meal     xxxxxx    \n"+take_id_meal);
                    }


                } catch (ParseException | java.text.ParseException e) {
                    e.printStackTrace();
                }
            }

            if( sb.append("").toString().isEmpty())
            {
                Log.d(ctx.toString(), "indexxxxx      dont  HAVE  MEAL      xxxxxx    \n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (fis != null) {

                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    public static void removeMealFromTextMenuFile(String FILE_NAME_MEAL_SELECTED ,Integer id_meal_delete,Context ctx) {
        List<String> old_meals_in_text =new  ArrayList<>();
        old_meals_in_text.clear();
        // From Date to String
        //Displaying current date and time in 12 hour format with AM/PM
        //   dateFormat.getTimeZone();  dose't support on israel time so i put Locale.ENGLISH
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa",Locale.ENGLISH);
        FileInputStream fis = null;
        try {
            fis = ctx.openFileInput(FILE_NAME_MEAL_SELECTED);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
int index_id_meal=0;
            while ((text = br.readLine()) != null) {
                //     From text to String and string to Date
                String dtStart =  sb.append(text.trim()).toString();
                Log.d(ctx.toString(), "indexxxxx   iech line  xxxxxx    \n"+dtStart);
                 sb.delete(0,26);
                old_meals_in_text.add(dtStart);

            }
            String take_date_and_time= "";
            for (int i=0 ;i<old_meals_in_text.size();i++)
            {
                int take_id_meal= Integer.parseInt(old_meals_in_text.get(i).substring(21));
                Log.d(ctx.toString(), "indexxxxx   take_date_and_time    \n"+take_date_and_time);
                if(take_id_meal== id_meal_delete)
                {
                    take_date_and_time= old_meals_in_text.get(i).substring(0,20).trim();
                    index_id_meal=i;
                        Log.d(ctx.toString(), "indexxxxx  id_meal_delete  xxxxxx    \n"+id_meal_delete +"   id meal in thext   "+ take_id_meal);

                }
                Log.d(ctx.toString(), "indexxxxx    old_meals_in_text  + take_id_meal xxxxxx    \n"+old_meals_in_text.get(i) + take_id_meal);
            }
            //call function that delete the meal from db send her id meal and time
            deleteMealFromDB(id_meal_delete ,take_date_and_time,ctx);
            old_meals_in_text.remove(index_id_meal);
            for (int i=0 ;i<old_meals_in_text.size();i++) {

                Log.d(ctx.toString(), "indexxxxx   old_meals_in_text   \n"+old_meals_in_text.get(i)+"\n" );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            //  removeTextMenuFile("mealSelected"+ServiceDataBaseHolder.getUser_confirm().getId()+".txt");
            fos = ctx.openFileOutput(FILE_NAME_MEAL_SELECTED, Context.MODE_PRIVATE);

            for (int i = 0; i < old_meals_in_text.size(); i++) {
                String temp=old_meals_in_text.get(i)+"\n";
                fos.write(temp.getBytes());
                Log.d(ctx.toString(), "indexxxxx    old_meals_in_text.get(i)      xxxxxx    \n" + old_meals_in_text.get(i));
            }
            old_meals_in_text.clear();
            Log.d(ctx.toString(), "indexxxxx   old_meals_in_text  clear     xxxxxx    \n" +old_meals_in_text);

         //   Toast.makeText(ctx, "Saved to " + ctx.getFilesDir() + "/" + FILE_NAME_MEAL_SELECTED, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {

                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // open new intent to send mail the admin
    public static void deleteOrContactRequestFromUser(final Context ctx ) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"admin@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "delete Or Contact ? if its not your user app email right you email in the explain");
                i.putExtra(Intent.EXTRA_TEXT   , "explain");
                try {
                    ctx.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ctx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }


            //send post request to delete meal from DB send is_meal and time
    private static void deleteMealFromDB(final Integer id_meal,final String date_and_time,final Context ctx) {
        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL_ADD_MEAL_SELECTED_TO_DB,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                           Log.d(ctx.toString(), "indexxxxx        response        xxxxxx    "+response);
                            JSONObject jsonObject =new JSONObject(response);
                            String success=jsonObject.getString( "success");


                            if(success.equals("1")){
                           //     Toast.makeText(ctx,"delete meal from DB Success!",Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx,"delete meal Error! " +e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx,"delete meal Error! " +error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("id_meal",id_meal.toString());
                params.put("id_user",ServiceDataBaseHolder.getUser_confirm().getId().toString());
                params.put("date_and_time",date_and_time);
                params.put("call_func","delete");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }


    //send post request to add the meal to DB send is_meal and make time upload send it to
    public static void AddMealToDB(final Integer id_meal,final Context ctx){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa",Locale.ENGLISH);

        Date today_date = Calendar.getInstance().getTime();
        final String date_and_time = dateFormat.format(today_date);

        Log.d(ctx.toString(), "indexxxxx   date_and_time     xxxxxx    \n" +date_and_time );

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL_ADD_MEAL_SELECTED_TO_DB,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.d(ctx.toString(), "indexxxxx        response        xxxxxx    "+response);
                            JSONObject jsonObject =new JSONObject(response);
                            String success=jsonObject.getString( "success");

                            if(success.equals("1")){
                           //     Toast.makeText(ctx,"add meal TO DB Success!",Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx,"add meal Error! " +e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx,"add meal Error! " +error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("id_meal",id_meal.toString());
                params.put("id_user",ServiceDataBaseHolder.getUser_confirm().getId().toString());
                params.put("date_and_time",date_and_time);
                params.put("call_func","add");
                return params;
            }
        };


        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);


    }

    /// A function that converts all the url of the images to bitmap
   public static Bitmap getImage(String urlToImage, final Context ctx ,final Integer index_meal_image, final Integer  id_meal ,final String permission ) {
        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ProgressDialog loading;
            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;

                String urlToImage = params[0];

                try {
                    url = new URL(urlToImage);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ctx, "", "", false, false);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap ) {
                super.onPostExecute(bitmap);
                if(permission.equals("USER"))
                {
                    //the logo is  id_meal=1
                if( id_meal==1 )
                {
                    ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(0).setImage(bitmap);
                    ServiceDataBaseHolder.setLogo_meal(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(0));

                }else {

                    if(ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().size()!=(index_meal_image))
                    {
                        ServiceDataBaseHolder.getMeals_arraylist_from_splashScreen().get(index_meal_image).setImage(bitmap);
                    }
                }
                }
                if(permission.equals("ADMIN") )
                {
                    if(ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().size()!=(index_meal_image))
                    {
                        ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin().get(index_meal_image).setImage(bitmap);
                    }
                }
                loading.dismiss();

            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
        return bitmap;
    }

    //clear the user data to the next user if log in
    public static void userLogout() {
        ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().clear();
        ServiceDataBaseHolder.setUser_confirm(new users());
    }

    //fix color to menu bar black or if the user passed the daily objective change to red
    public static void updateFoodColor(Context context) {
        TextView tvObjective = (TextView) ((Activity)context).findViewById(R.id.tvObjective);
        TextView tvFood = (TextView) ((Activity)context).findViewById(R.id.tvFood);
        TextView tvRemaining = (TextView) ((Activity)context).findViewById(R.id.tvRemaining);

        String replaced = tvObjective.getText().toString().replace("cal", "");
        String replaced2 = tvFood.getText().toString().replace("cal", "");
        String replaced3 = tvRemaining.getText().toString().replace("cal", "");

        double tempObjective = Double.parseDouble(replaced);
        double tempFood = Double.parseDouble(replaced2);
        double tempRemaining = Double.parseDouble(replaced3);
        if (tempFood > tempObjective) {
            tvFood.setTextColor(Color.RED);
            Toast.makeText(context, "Be careful you passed the daily objective ", Toast.LENGTH_LONG).show();
        }
        if (tempRemaining < 0) {
            tvRemaining.setTextColor(Color.RED);
        } else {
            tvFood.setTextColor(Color.BLACK);
            tvRemaining.setTextColor(Color.BLACK);
        }
    }


    public static class ServiceAdmin {
        private static final String TAG = "ServiceAdmin";
        private static ArrayList<meal> meals = new ArrayList<>();
        private static ArrayList<IngredientsAndCount> IngredientsAndCount_for_admin = new ArrayList<>();


        static String success = "success";

        /**
         * if the admin login bring all data the admin need
         *meals are not confirm yes
         *meals_consist
         */
        public static String bringMyAllDataBase(String URL, final Context context) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String temp_string="";
                                  Log.d(TAG, "indexxxxx        response admin       xxxxxx    \n" + response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");


                                if (success.equals("1")) {
/*
                                // insert all the users parameters from database into "users array list"
                                JSONArray jsonArray_users = jsonObject.getJSONArray("users");
                                for (int i = 0; i < jsonArray_users.length(); i++) {
                                    JSONObject object = jsonArray_users.getJSONObject(i);

                                    ///  double  and boolean can not be null so i convert to temp parameters and push the data
                                    temp_string = object.getString("csv_file");
                                    if (temp_string.equals("1"))
                                        temp_boolean = true;
                                    else
                                        temp_boolean = false;

                                    temp_string = object.getString("weight");
                                    if (temp_string.equals("null"))
                                        temp_weight = 0.0;
                                    else
                                        temp_weight = object.getDouble("weight");

                                    temp_string = object.getString("height");
                                    if (temp_string.equals("null"))
                                        temp_height = 0.0;
                                    else
                                        temp_height = object.getDouble("height");

                                    temp_string = object.getString("age");
                                    if (temp_string.equals("null"))
                                        temp_age = 0;
                                    else
                                        temp_age = object.getInt("age");

                                    temp_string = object.getString("diabetes");
                                    if (temp_string.equals("Yes"))
                                        temp_boolean = true;
                                    else
                                        temp_boolean = false;

                                    users temp_user = new users(object.getInt("id"), object.getString("name"), object.getString("email"), temp_weight, temp_height, temp_age, object.getString("purpose"), object.getInt("level_activity"), temp_boolean, object.getString("gender"), temp_boolean, object.getInt("id_types_food"));
                                    ServiceDataBaseHolder.getUsers_arraylist_from_splashScreen().add(temp_user);

                                }


                                JSONArray jsonArray_users_allergies = jsonObject.getJSONArray("users_allergies");
                                for (int i = 0; i < jsonArray_users_allergies.length(); i++) {
                                    JSONObject object = jsonArray_users_allergies.getJSONObject(i);
                                    userAllergies temp_users_allergirs = new userAllergies(object.getInt("id_user"), object.getInt("id_allergies"));
                                    ServiceDataBaseHolder.getUsers_allergies_arraylist_for_admin().add(temp_users_allergirs);
                                }

*/
                                   boolean temp_bool_Dangerous_for_diabetics;

                                    JSONArray jsonArray_meals = jsonObject.getJSONArray("meals");
                                    // insert all the meals parameters from database into "meals array list"
                                    for (int i = 0; i < jsonArray_meals.length(); i++) {
                                        JSONObject object = jsonArray_meals.getJSONObject(i);


                                        temp_string = object.getString("Dangerous_for_diabetics");
                                        if (temp_string.equals("1"))
                                            temp_bool_Dangerous_for_diabetics = true;
                                        else
                                            temp_bool_Dangerous_for_diabetics = false;

                                        temp_string = object.getString("confirm_by_admin");
                                        boolean admin_confirm = true;
                                        if (temp_string.equals("0")) {
                                            admin_confirm = false;


                                            String transfer_ip = "   http://"+ServiceDataBaseHolder.getIP_PATH()+"/android_register_login/images/"+object.getInt("id") +".png";
                                       //     transfer_ip = transfer_ip.replace("192.168.239.123", "192.168.67.123");

                                            Service.getImage(transfer_ip, context,i,object.getInt("id"),"ADMIN");

                                        }


                                        meal mealss = new meal(object.getInt("id"), object.getString("name"), object.getString("description"), object.getDouble("calories"), object.getInt("id_types"), object.getInt("id_user_upload"), temp_bool_Dangerous_for_diabetics, admin_confirm,0);  //,object.getString("image_path") need to see what platform i take the image path
                                        meals.add(mealss);


                                    }


                                    for (int j = 0; j < meals.size(); j++) {
                                        JSONArray jsonArray_meals_consist = jsonObject.getJSONArray("meals_consist_admin");
                                        // insert all the IngredientsAndCount parameters from database into "IngredientsAndCount_for_admin array list"
                                        for (int i = 0; i < jsonArray_meals_consist.length(); i++) {
                                            JSONObject object1 = jsonArray_meals_consist.getJSONObject(i);
                                            if (meals.get(j).getIdMeal() == object1.getInt("id_meal")) {
                                                IngredientsAndCount temp_IngredientsAndCount  = new IngredientsAndCount( object1.getInt("id_ingredients"), object1.getString("count"));
                                                IngredientsAndCount_for_admin.add(temp_IngredientsAndCount);
                                                Log.d(TAG, "indexxxxx           temp_IngredientsAndCount      xxxxxx    \n" +temp_IngredientsAndCount);
                                        }

                                        }
if(!IngredientsAndCount_for_admin.isEmpty()){
    for(int m = 0; m< IngredientsAndCount_for_admin.size(); m++) {
        for (int k = 0; k < ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size(); k++) {
            if (IngredientsAndCount_for_admin.get(m).getId_ingredient().equals(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(k).getId_ingredient()))
            {
                IngredientsAndCount temp = new IngredientsAndCount(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(k).getId_ingredient(),ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(k).getName_ingredient(), IngredientsAndCount_for_admin.get(m).getCount());
                IngredientsAndCount_for_admin.set(m,temp);
                Log.d(TAG, "indexxxxx           IngredientsAndCount_for_admin      xxxxxx    \n" +IngredientsAndCount_for_admin);

            }
    }

    }

}

                                        meals.get(j).setIngredientsCounts(IngredientsAndCount_for_admin);
                                        IngredientsAndCount_for_admin.clear();
                                        Log.d(TAG, "indexxxxx             meals.get(j)        xxxxxx    \n" + meals.get(j));
                                    }

                                    ServiceDataBaseHolder.setMeals_not_confirm_yet_by_admin(meals);
                                    Log.d(TAG, "indexxxxx          ServiceDataBaseHolder.getUsers_types_arraylist_for_admin()      xxxxxx    \n"+    ServiceDataBaseHolder.getMeals_not_confirm_yet_by_admin() );
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "indexxxxx        JSONException        xxxxxx    \n" + e);
                                success = e.toString();

                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    success = error.toString();
                    // Toast.makeText(context,"Error"+error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

            return success;
        }


        /**
         * admin send request to the php. file admin request what he want :
         * delete user or
         * add ingredients
         * is success or not he will get Toast (message)
         */
        public static void requestAdmin(String URL, final Context context ,final String request ,final String email_or_ingredient) {
            StringRequest stringRequest =new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Log.d(TAG, "indexxxxx        response        xxxxxx    \n"+ response );
                                JSONObject jsonObject =new JSONObject(response);
                                String success=jsonObject.getString( "success");
                                String requestAdmin=jsonObject.getString( "requestAdmin");


                                if(success.equals("1")&&requestAdmin.equals("DELETE_USER"))
                                {
                                    Toast.makeText(context," user "+ email_or_ingredient +" has deleted Successfully",Toast.LENGTH_LONG).show();
                                }
                                else if(success.equals("0")&&requestAdmin.equals("DELETE_USER"))
                                {
                                    Toast.makeText(context,"User does not exist",Toast.LENGTH_LONG).show();
                                }
                                else if(success.equals("1")&&requestAdmin.equals("ADD_INGREDIENT"))
                                {
                                    Toast.makeText(context,"Adding an ingredient Successfully",Toast.LENGTH_LONG).show();
                                }
                                else if(success.equals("0")&&requestAdmin.equals("ADD_INGREDIENT"))
                                {
                                    Toast.makeText(context,"Already has the ingredient",Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "indexxxxx        JSONException        xxxxxx    \n"+ e );

                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"Error"+error.toString(),Toast.LENGTH_LONG).show();

                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params =new HashMap<>();
                    params.put("request",request);
                    params.put("email_or_ingredient",email_or_ingredient);
                    return params;

                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);




        }

        /**
         * admin send request to the php to confirm upload meal or delete meal
         * is success or not he will get Toast (message)
         */
        public static void requestAdminUploadMeal(String URL, final Context context ,final String request ,final meal meal_confirm_by_admin) {

            StringRequest stringRequest =new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Log.d(TAG, "indexxxxx        response        xxxxxx    \n"+ response );
                                JSONObject jsonObject =new JSONObject(response);
                                String success=jsonObject.getString( "success");
                                String requestAdmin=jsonObject.getString( "requestAdmin");


                                if(success.equals("1")&&requestAdmin.equals("UPDATE_MEAL"))
                                {
                                    Toast.makeText(context," UPDATE MEAL Successfully",Toast.LENGTH_LONG).show();
                                }
                                else if (success.equals("0")&&requestAdmin.equals("UPDATE_MEAL"))
                                {
                                    Toast.makeText(context,"UPDATE MEAL Fails",Toast.LENGTH_LONG).show();
                                }

                                else if (success.equals("1")&&requestAdmin.equals("DELETE_MEAL"))
                                {
                                    Toast.makeText(context," DELETE_MEAL Successfully",Toast.LENGTH_LONG).show();
                                }
                                else if (success.equals("0")&&requestAdmin.equals("DELETE_MEAL"))
                                {
                                    Toast.makeText(context," DELETE_MEAL Fails",Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "indexxxxx        JSONException        xxxxxx    \n"+ e );

                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"Error"+error.toString(),Toast.LENGTH_LONG).show();

                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params =new HashMap<>();

                    params.put("request",request);
                    params.put("id_meal",meal_confirm_by_admin.getIdMeal().toString());
                    params.put("name_meal",meal_confirm_by_admin.getNameMeal());
                    params.put("calories",meal_confirm_by_admin.getCalories().toString());
                    params.put("types_food",meal_confirm_by_admin.getTyeps().toString());
                    params.put("description",meal_confirm_by_admin.getDescription());
                    params.put("dangerous_for_diabetics",meal_confirm_by_admin.getDangerousForDiabetics().toString());
                    return params;

                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);




        }

    }



}

//// star  ------------------   call function to bring or save the Ingredients Doesnt Like From Text File ----------------------------------------------------------------------------------------------------------------------------
///**
// * the function check if the user have text file in his phone that collect all the ingredients he doesn't like
// */
// Service.loadIngredientsDoesntLikeFromTextFile("ingredientsDoesntLike"+ServiceDataBaseHolder.getUser_confirm().getId()+".txt",LoginActivity.this);
// updateAlgoColor(Service.loadIngredientsDoesntLikeFromTextFile(FILE_NAME ,HomeActivity.this));



//    public static void saveIngredientsDoesntLikeInTextFile(ArrayList<Integer> ingredients_selected_items , String FILE_NAME, Context ctx) {
//        StringBuilder text = new StringBuilder();
//        for (int i = 0; i < ingredients_selected_items.size(); i++) {
//            text.append(ingredients_selected_items.get(i)).append("\n");
//        }
//        FileOutputStream fos = null;
//        try {
//
//            fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            fos.write(text.toString().getBytes());
//            Toast.makeText(ctx, "Saved to " + ctx.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(ctx, "Something went wrong", Toast.LENGTH_LONG).show();
//
//        } finally {
//            if (fos != null) {
//
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

//    public static boolean loadIngredientsDoesntLikeFromTextFile (String FILE_NAME, Context ctx){
//        boolean tempbol = false;
//        ArrayList<Integer> ingredients_doesnt_like_id = new ArrayList<>();
//        FileInputStream fis = null;
//        try {
//            fis = ctx.openFileInput(FILE_NAME);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String text1;
//
//            while ((text1 = br.readLine()) != null) {
//                sb.append(text1);
//                ingredients_doesnt_like_id.add(Integer.parseInt(text1));
//                Log.d(ctx.toString(), "indexxxxx        text         xxxxxx    \n" + text1);
//            }
//
//            if (sb.toString().isEmpty()) {
//                Log.d(ctx.toString(), "indexxxxx      dont  selected  Ingredients      xxxxxx    \n");
//                tempbol= false;
//
//            } else {
//                ArrayList<ingredients> ingredientsFinalToUpload =new ArrayList<>();
//
//                for(int j=0;j<ingredients_doesnt_like_id.size();j++)
//                {
//                    for(int i=0;i<ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size();i++)
//                    {
//                        if(Integer.parseInt(String.valueOf(ingredients_doesnt_like_id.get(j))) ==(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i).getId_ingredient()))
//                        {
//                            ingredientsFinalToUpload.add(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i));
//                        }
//                    }
//                }
//                ServiceDataBaseHolder.setSelected_ingredients_doesnt_like_from_text_file_user(ingredientsFinalToUpload);
//                Log.d(ctx.toString(), "indexxxxx              xxxxxx    \n"+  ServiceDataBaseHolder.getSelected_ingredients_doesnt_like_from_text_file_user());
//                tempbol=  true;
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//            if (fis != null) {
//
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return tempbol;
//    }

//// end  ------------------   call function to bring or save the Ingredients Doesnt Like From Text File ----------------------------------------------------------------------------------------------------------------------------

//    public static void removeTextMenuFile(String FILE_NAME_MEAL_SELECTED ,Context ctx) {
//
//        FileOutputStream fos = null;
//        try {
//            fos = ctx.openFileOutput(FILE_NAME_MEAL_SELECTED, Context.MODE_PRIVATE);
//            String nothing ="";
//            //or text ="      " and put text
//            fos.write(nothing.getBytes());
//
//            Log.d(ctx.toString(), "indexxxxx    delete the meal     xxxxxx    \n");
//            //  fos.write(text.toString().getBytes());
//            Toast.makeText(ctx, "delete to " + ctx.getFilesDir() + "/" + FILE_NAME_MEAL_SELECTED, Toast.LENGTH_LONG).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }



