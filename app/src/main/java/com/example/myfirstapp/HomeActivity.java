package com.example.myfirstapp;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static com.example.myfirstapp.Service.deleteOrContactRequestFromUser;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeSlide,tvObjective,tvtextalgo,tvFood, tvRemaining;
    private LinearLayout homeLayoutTop,homeLayoutDown;
    private RelativeLayout welcomeSlidelayout,algolayoutbtn,algolayout,btnAddMenuLayout;
    private Animation slideUp,slideUp1;
    private Button btn_algoritem,btn_addMenu;

    private ArrayList<meal> modelMeals = new ArrayList<>();
    final private users user_confirm = ServiceDataBaseHolder.getUser_confirm();

    private boolean flag_change_color_algo=false;
//    private static  String FILE_NAME= "ingredientsDoesntLike";
//    private static  String FILE_NAME_MEAL_SELECTED_TRY="mealSelected";
//    private static  String USER_ID_TXT= ServiceDataBaseHolder.getUser_confirm().getId()+".txt"; ;
    private static final String TAG = "HomeActivity";
//
//    FILE_NAME= "ingredientsDoesntLike";
//    FILE_NAME +=USER_ID_TXT;






    // ---------------------start  new image slide ------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        homeLayoutTop=(LinearLayout)findViewById(R.id.homeLayoutTop);
        homeLayoutDown=(LinearLayout) findViewById(R.id.homeLayoutDown);

        algolayout=(RelativeLayout) findViewById(R.id.algolayout);
        algolayoutbtn=(RelativeLayout)findViewById(R.id.algolayoutbtn);
        welcomeSlidelayout=(RelativeLayout)findViewById(R.id.welcomeSlidelayout);
        btnAddMenuLayout=(RelativeLayout)findViewById(R.id.btnAddMenuLayout);

        btn_algoritem=(Button)findViewById(R.id.btn_algoritem);
        btn_addMenu=(Button)findViewById(R.id.btn_addMenu);

        welcomeSlide=(TextView)findViewById(R.id.welcomeSlide);
        tvObjective=(TextView)findViewById(R.id.tvObjective);
        tvFood=(TextView)findViewById(R.id.tvFood);
        tvRemaining =(TextView)findViewById(R.id.tvRemaining);
        tvtextalgo=(TextView)findViewById(R.id.textalgo);

        // make the slide up for the message bar and all layout in this activity (page)
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        slideUp1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup1);
        welcomeSlidelayout.startAnimation(slideUp);


        //this is slide welcome bar include user name
        welcomeSlide.setText("Welcome!\n" +user_confirm.getName());

        homeLayoutTop.setVisibility(TextView.INVISIBLE);
        homeLayoutDown.setVisibility(TextView.INVISIBLE);
        btnAddMenuLayout.setVisibility(View.INVISIBLE);

        tvtextalgo.setVisibility(TextView.INVISIBLE);
        btn_algoritem.setVisibility(TextView.INVISIBLE);
        algolayout.setVisibility(TextView.INVISIBLE);

        /**
         *  update the Calories in the menu bar
         *  objective
         *  food_selected
         * remaining
         * update color food_selected and remaining if the user exaggerate with the food
         */
        tvObjective.setText(Service.updateMenuBarObjective());
        tvFood.setText(Service.updateCaloriesFood());
        tvRemaining.setText(Service.updateRemaining(tvObjective.getText().toString().replace("cal", ""),tvFood.getText().toString().replace("cal", "")));
        Service.updateFoodColor(HomeActivity.this);

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                welcomeSlidelayout.setVisibility(TextView.INVISIBLE);
                welcomeSlide.setText("");

                tvtextalgo.setVisibility(TextView.VISIBLE);
                algolayoutbtn.setVisibility(TextView.VISIBLE);
                btn_algoritem.setVisibility(TextView.VISIBLE);
                algolayout.setVisibility(TextView.VISIBLE);

                homeLayoutTop.setVisibility(TextView.VISIBLE);
                homeLayoutDown.setVisibility(TextView.VISIBLE);
                btnAddMenuLayout.setVisibility(Button.VISIBLE);

                homeLayoutTop.startAnimation(slideUp1);
                homeLayoutDown.startAnimation(slideUp1);
                btn_addMenu.startAnimation(slideUp1);
            }
        },100);//3000




        setMealsInLayout(ServiceDataBaseHolder.getMeals_are_filter());

        btn_algoritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_change_color_algo)
                {
                    setMealsInLayout(ServiceDataBaseHolder.getMeals_are_filter());
                    updateAlgoColor(!flag_change_color_algo);
                    flag_change_color_algo=false;
                }else
                {
                    /**
                     * select many cluster i want (2)
                     * initiate the 2 clusters
                     * take all the meals are we get from the same user (after algo ) and insert tame in home start view meals
                     * change the color button
                     */
                    int clusterNumber =2;
                    Kmeans algo = new Kmeans();
                    algo.genereateRecord();
                    algo.initiateClusterAndCenterPoint(clusterNumber);
                    setMealsInLayout( Service.returnMealsLikeMainUser(algo.bringRecordData()));
                    updateAlgoColor(!flag_change_color_algo);
                    flag_change_color_algo=true;
//                    algo.printRecordInformation();
//                    algo.printClusterInformation();
                }



            }
        });
    }
    public void setMealsInLayout(ArrayList<meal> mealsToShow)
    {
        /**---------------------insert image food slide ------------------------------
         *  insert the meals from ServiceDataBaseHolder in array modelMeals to display this on create
         *  but in a random way i call random number between 1 to meals are confirm size and sent that to function to check if the meal is add to temp array until its full
         *  open adapter and send the modelMeals to be in the layout that i build "model_meal_slide"
         * and put he model_meal_slide(layout) on viewPager
         * make ViewPager onPageSelected Listener
         */

        Random random = new Random();
        ArrayList<Integer>  index_meal_add_to_random_array=new ArrayList<>();
        boolean flag_stop = true;

        modelMeals.clear();
  //  index_meal_add_to_random_array.add(0);
  //  modelMeals.add(0, ServiceDataBaseHolder.getLogo_meal());


        while (flag_stop)
        {

            int  randomNum = random.nextInt(mealsToShow.size());
            if(check_num_in_array(index_meal_add_to_random_array,randomNum))
            {
                index_meal_add_to_random_array.add(randomNum);
                modelMeals.add(mealsToShow.get(randomNum));
            }
            if(index_meal_add_to_random_array.size()==mealsToShow.size())
            {
                flag_stop=false;
            }
        }
  modelMeals.add(0, ServiceDataBaseHolder.getLogo_meal());

        Adapter adapter = new Adapter(modelMeals, this);
        ViewPager viewPager = findViewById(R.id.MealShowViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(50,0,50,0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(final int position) {
                btn_addMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position==0)
                        {
                            Toast.makeText(HomeActivity.this,"Stop trying to take my logo ;)",Toast.LENGTH_SHORT).show();
                        }
                        else {
/**
 *  insert the meal in array selected meals from databaseconstractors to display it in user menu
 *   AddMealToDB send the meal to the db for more Statistical calculations
 *  after this its upload the calories in the menu bar and color
 * make message toast include name meal
 */
                            ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().add(modelMeals.get(position));
                            Service.AddMealToDB(modelMeals.get(position).getIdMeal(),HomeActivity.this);
                            tvFood.setText(Service.updateCaloriesFood());
                            tvRemaining.setText(Service.updateRemaining(tvObjective.getText().toString().replace("cal", ""),tvFood.getText().toString().replace("cal", "")));
                            Service.updateFoodColor(HomeActivity.this);
                            Service.saveMealSelectedToTextFile("mealSelected"+user_confirm.getId()+".txt",modelMeals.get(position).getIdMeal(),HomeActivity.this);
                            Toast.makeText(HomeActivity.this,modelMeals.get(position).getName_ingredient()+" is add ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // ---------------------end  new image slide ------------------------------
        index_meal_add_to_random_array.clear();

    }


    //func for put random meals so i check if the random num(check_num) is include all ready in the array
    public boolean check_num_in_array(ArrayList<Integer> arrays_int,Integer check_num)
    {
    for (int i=0;i<arrays_int.size();i++)
    {
        if(arrays_int.get(i).equals(check_num))
        {
            return false;
        }
    }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }
    /////---------------------------------------------------- its show the menu icon and tool  in menu bar ----------------------------------------------------------------------

    // Listener on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            //  item1 its the new meal icon in the option menu
            case R.id.item1: {
                startActivity(new Intent(HomeActivity.this,NewMealActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
                return true;
            }

            case R.id.item2:
            {
                startActivity(new Intent(HomeActivity.this,MenuActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
                return true;
            }

            case R.id.item3:
            {
                Intent intent = new Intent(HomeActivity.this,ObjectiveActivity.class);
                intent.putExtra("user_mode", "update");
                startActivity(intent);
                return true;
            }

            case R.id.item4:
            {
                deleteOrContactRequestFromUser(HomeActivity.this);
                return true;
            }

            case R.id.item5:
            {
                //clean the user data to login a new one
                Service.userLogout();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
                return true;
            }
                default:
                return super.onOptionsItemSelected(item);
        }
    }





    //change the color and text button algo
    public void updateAlgoColor(boolean b){
        if (!b) {
            btn_algoritem.setBackgroundResource(R.drawable.round2);
            btn_algoritem.setText("Click Here");
        } else {
            btn_algoritem.setBackgroundResource(R.drawable.round5);
            btn_algoritem.setText("You have already used the algorithm you can click again to cancel");
        }
    }






/*                                              need to delete all     need to delete all     need to delete all
//////////////////////////                                   creat csv file                      need to delete all need to delete all need to delete all need to delete all need to delete all             \\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    //        creat and exportCSVFile  need to delete all

    public void WriteCSVFile(){
        List<allergies> array_allergies = (List<allergies>) Arrays.asList(
                new allergies(1,  "s"),
                new allergies(2, "Adam Johnson"),
                new allergies(3, "Katherin Carter"),
                new allergies(4, "Jack London"),
                new allergies(5, "Jason Bourne"));

        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter("meals_for_24_hours.csv");

            fileWriter.append(CSV_HEADER);
            fileWriter.append('\n');

            for (allergies temp_allergies : array_allergies) {
                fileWriter.append(String.valueOf(temp_allergies.getId()));
                fileWriter.append(',');
                fileWriter.append(temp_allergies.getName());
                fileWriter.append(',');
                fileWriter.append('\n');
            }

            Log.d(TAG, "indexxxxx     \"Write CSV successfully!\"    xxxxxx    \n");
        } catch (Exception e) {
            Log.d(TAG, "indexxxxx   \"Writing CSV error!   xxxxxx    \n");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "indexxxxx   Flushing/closing error!  xxxxxx    \n"+e.toString());
            }
        }
    }



//---------------------------------------------------------------------------csv file try --------------------------------------------------------------------------------------------------------------------------


    public void removeTextMenuFile(String fileName) {
        String FILE_NAME_MEAL_SELECTED_TRY = "" + fileName;
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME_MEAL_SELECTED_TRY, MODE_PRIVATE);
            String nothing ="";
            //or text ="      " and put text
            fos.write(nothing.getBytes());

            Log.d(TAG, "indexxxxx    delete the meal     xxxxxx    \n");
            //  fos.write(text.toString().getBytes());
            Toast.makeText(this, "delete to " + getFilesDir() + "/" + FILE_NAME_MEAL_SELECTED_TRY, Toast.LENGTH_LONG).show();
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



    public void enlikoahhh(){
        try {
// OPTION 1: if the file is in the sd
//        File csvfile = new File(Environment.getExternalStorageDirectory() + "/data.csv");
//        Log.d(TAG, "indexxxxx  csvfile1   xxxxxx    \n"+csvfile);
// END OF OPTION 1

// OPTION 2: pack the file with the app
         //    "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer)
            String csvfileString = this.getApplicationInfo().dataDir + File.separatorChar +"files/"+"example.csv";
            File csvfile = new File(csvfileString);






            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsoluteFile()));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Log.d(TAG, "indexxxxx  nextLine[0] + nextLine[1]   xxxxxx    \n"+nextLine[0] +nextLine[1]);
            }


//        int noOfRow =   Service.count_num_row(csvfileString);
//        Log.d(TAG, "indexxxxx  noOfRow   xxxxxx    \n"+noOfRow);
//        for (int i = 0; i < noOfRow; i++) {
//            String[] row = reader.readNext();
//if(row[1].equals("1"))
//{
//    Log.d(TAG, "indexxxxx  need to delete  xxxxxx    \n");
//
//}
//            Log.d(TAG, "indexxxxx  row[i]   xxxxxx    \n"+row[0]+"  "+row[1]);
//        }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
    }


    public void writeDataLineByLine(int id_meal)
    {
        String csvfileString = this.getApplicationInfo().dataDir + File.separatorChar +"files/"+"example.csv";
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(csvfileString);
        try {

            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "Time_upload", "id_meal" };
            writer.writeNext(header);

            //take the date and pars it to string
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa",Locale.ENGLISH);
            Date today_date = Calendar.getInstance().getTime();
            String dateTime = dateFormat.format(today_date);

            // add the time and id meal to csv
            String[] data1 = { dateTime+"ss", ""+id_meal };
            writer.writeNext(data1);
            String[] data2 = { dateTime+"sds", "s"+id_meal };
            writer.writeNext(data2);
            String[] data3 = { dateTime+"sssd", "ss"+id_meal };
            writer.writeNext(data3);
            String[] data4 = { dateTime+"sass", "dd"+id_meal };
            writer.writeNext(data4);
            writer.writeNext(data1);


            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }







//    public void saveIngredientsDoesntLikeInTextFile(ArrayList<String> ingredients_selected_items) {
//        StringBuilder text = new StringBuilder();
//        for(int i =0;i<ingredients_selected_items.size();i++) {
//             text.append(ingredients_selected_items.get(i)).append("\n");
//        }
//        FileOutputStream fos = null;
//        try {
//
//            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//            fos.write(text.toString().getBytes());
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,Toast.LENGTH_LONG).show();
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


//    public void loadIngredientsDoesntLikeFromTextFile() {
//        FileInputStream fis = null;
//        try {
//            fis = openFileInput(FILE_NAME);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String text;
//
//
//
//            while ((text = br.readLine()) != null) {
//                sb.append(text).append("\n");
//                Log.d(TAG, "indexxxxx        text         xxxxxx    \n"+text);
//
//            }
//
//            if( sb.append("").toString().isEmpty())
//            {
//                Log.d(TAG, "indexxxxx      dont  selected  Ingredients      xxxxxx    \n");
//                btn_algoritem.setBackgroundResource(R.drawable.round2);
//                btn_algoritem.setText("Select Ingredients You Doesn't like !");
//            }else
//            {
//                Log.d(TAG, "indexxxxx        selected Ingredients        xxxxxx    \n");
//                btn_algoritem.setBackgroundResource(R.drawable.round5);
//                btn_algoritem.setText("You have already used the algorithm you can click again");
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
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
//    }





//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




*/




}





