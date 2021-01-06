package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.myfirstapp.Service.deleteOrContactRequestFromUser;

public class MenuActivity extends AppCompatActivity implements Serializable {
    private static ArrayList<meal> mMenuList;
    private RecyclerView mRecyclerView;
    private MyMenuAdapter mAdapter;
    private int oldPositionSelectedMeal = 0;
    private TextView tvObjective, tvFood, tvRemaining;


    private static final String TAG = "MenuActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        tvObjective = (TextView) findViewById(R.id.tvObjective);
        tvFood = (TextView) findViewById(R.id.tvFood);
        tvRemaining = (TextView) findViewById(R.id.tvRemaining);
        RelativeLayout homeLayoutTop = (RelativeLayout) findViewById(R.id.menueslide);
        homeLayoutTop.setVisibility(TextView.VISIBLE);


        tvObjective.setText(Service.updateMenuBarObjective());
        tvFood.setText(Service.updateCaloriesFood());
        tvRemaining.setText(Service.updateRemaining(tvObjective.getText().toString().replace("cal", ""),tvFood.getText().toString().replace("cal", "")));
        Service.updateFoodColor(MenuActivity.this);

        createMealsViewList();
        buildRecyclerView();

    }



    /////---------------------------------------------------- its show the menu icon and tool  in menu bar ----------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.item1:
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
                //nice anim slide left to right the page
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            case R.id.item2:
                Toast.makeText(this, "Change my details selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MenuActivity.this, ObjectiveActivity.class);
                intent.putExtra("user_mode", "update");
                startActivity(intent);
                //nice anim slide left to right the page
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            case R.id.item3:
                deleteOrContactRequestFromUser(MenuActivity.this);
                return true;
            case R.id.item4:
                //clean the user data to login a new one
                Service.userLogout();
                startActivity(new Intent(MenuActivity.this,LoginActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    ///----------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void removeItemMeal(int position) {
        //remove the meal from his array meals his selected to eat the function in models meal
        //need to take from user info in the user info i have all the data i need like food he selected


        Service.removeMealFromTextMenuFile("mealSelected"+ServiceDataBaseHolder.getUser_confirm().getId()+".txt",mMenuList.get(position).getIdMeal(),MenuActivity.this);
        //make function remove from text file the row whit the id_meal
        ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().remove(mMenuList.get(position));
        mMenuList.remove(position);
        Log.d(TAG, "xxxxxx    removeItem position     xxxxxx    \n" + "    " + position);

        mAdapter.setFlag_layout(false);
        mAdapter.notifyDataSetChanged();
        createMealsViewList();
        buildRecyclerView();

        tvFood.setText(Service.updateCaloriesFood());
        tvRemaining.setText(Service.updateRemaining(tvObjective.getText().toString().replace("cal", ""),tvFood.getText().toString().replace("cal", "")));
        Service.updateFoodColor(MenuActivity.this);

    }




    public void createMealsViewList() {

        if (ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().isEmpty()) {
            mMenuList = new ArrayList<>();
            Toast.makeText(MenuActivity.this, "YOU DONT HAVE ANY MEAL!", Toast.LENGTH_SHORT).show();
        } else {
            mMenuList = new ArrayList<>(ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist());
            Service.CalculatedFoodCalories(mMenuList);
        }

    }

    public void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.recyclerViewMyMenu);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyMenuAdapter(mMenuList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                checkLayoutClick(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItemMeal(position);
            }// remove from RecyclerView
        });
    }

    private void checkLayoutClick(int position) {
      //  Log.d(TAG, "indexxxxx         position CLICK       xxxxxx    " + position);

        if (mAdapter.getFlag_layout().equals(false)) {
         //   Log.d(TAG, "indexxxxx        open      xxxxxx    " + position);
            mAdapter.setFlag_layout(true);
            oldPositionSelectedMeal = position;
            mAdapter.notifyItemChanged(position);


      //      Log.d(TAG, "indexxxxx        getMeals_are_selected_to_my_menu_arraylist     xxxxxx    " + ServiceDataBaseHolder.getMeals_are_selected_to_my_menu_arraylist().size());
            }

         else if (mAdapter.getFlag_layout().equals(true) && oldPositionSelectedMeal == position) {
        //    Log.d(TAG, "indexxxxx        close     xxxxxx    " + position + "   click on old      " + oldPositionSelectedMeal);
            mAdapter.setFlag_layout(false);
           mAdapter.notifyItemChanged(position);

        }
         else if (mAdapter.getFlag_layout().equals(true) && oldPositionSelectedMeal != position) {
          //  Log.d(TAG, "indexxxxx        close     xxxxxx    " + position + "   click on old" + oldPositionSelectedMeal + "  get flag :" + mAdapter.getFlag_layout().toString());
            mAdapter.setFlag_layout(false);
           mAdapter.notifyItemChanged(oldPositionSelectedMeal);
        }

    }


}

