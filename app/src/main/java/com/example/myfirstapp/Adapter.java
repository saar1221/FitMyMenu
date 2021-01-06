package com.example.myfirstapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;

public class Adapter extends PagerAdapter {
    private ArrayList<meal> modelMeals;
private LayoutInflater layoutInflater;
private Context context;

    public Adapter(ArrayList<meal> modelMeals, Context context) {
        this.modelMeals = modelMeals;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelMeals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      layoutInflater =LayoutInflater.from(context);
      View view=layoutInflater.inflate(R.layout.model_meal_slide,container,false);
        ImageView imageView;
        TextView title,desc,ingredients,calories,addByUsers;

        imageView =view.findViewById(R.id.image);
        title=view.findViewById(R.id.titel);
        desc =view.findViewById(R.id.desc);
        calories=view.findViewById(R.id.calories);
        ingredients=view.findViewById(R.id.ingredients);
        addByUsers=view.findViewById(R.id.addByUsers);

        
        if(position==0){
            imageView.setImageBitmap(modelMeals.get(position).getImage());
            title.setText("");
            desc.setText("             slide right to left and add some meals");
            calories.setText("");
               ingredients.setText("");
                addByUsers.setText("");
        }
        else {
            imageView.setImageBitmap(modelMeals.get(position).getImage());
            title.setText(modelMeals.get(position).getName_ingredient());
            calories.setText("Calories: " + modelMeals.get(position).getCalories().toString());
               ingredients.setText("Ingredients: "+modelMeals.get(position).sizeIngredients_counts());
            desc.setText("Description:  " + modelMeals.get(position).getDescription());
            addByUsers.setText("Add By Users: " + modelMeals.get(position).getAddByUsers().toString());
      }
        container.addView(view,0);
      return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }



}

