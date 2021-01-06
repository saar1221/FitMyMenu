package com.example.myfirstapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceDataBaseHolder implements Serializable {

    private static ArrayList<ingredients> ingredients_arraylist_from_splashScreen = new ArrayList<>();
    private static ArrayList<allergies> allergies_arraylist_from_splashScreen = new ArrayList<>();
    private static ArrayList<typesFood> typesFood_arraylist_from_splashScreen = new ArrayList<>();


    private static  meal logo_meal=new meal();
    private static ArrayList<meal> meals_are_filter = new ArrayList<>();
    private static ArrayList<meal> meals_arraylist_from_splashScreen = new ArrayList<>();
    private static ArrayList<meal> meals_are_selected_to_my_menu_arraylist = new ArrayList<>();



    private static users user_confirm = new users();
    private static  ArrayList<ingredients> selected_ingredients_doesnt_like_from_text_file_user = new ArrayList<>();
    private static  ArrayList<Record> users_like_me_for_algo = new ArrayList<>();

//for admin
    private static ArrayList<meal> meals_not_confirm_yet_by_admin = new ArrayList<>();


    public static meal getLogo_meal() {
        return logo_meal;
    }




    public static ArrayList<ingredients> getSelected_ingredients_doesnt_like_from_text_file_user() {
        return selected_ingredients_doesnt_like_from_text_file_user;
    }

    public static void setSelected_ingredients_doesnt_like_from_text_file_user(ArrayList<ingredients> selected_ingredients_doesnt_like_from_text_file_user) {
        ServiceDataBaseHolder.selected_ingredients_doesnt_like_from_text_file_user = selected_ingredients_doesnt_like_from_text_file_user;
    }


    public static void setUser_confirm(users user_confirm) {
        ServiceDataBaseHolder.user_confirm = user_confirm;
    }

    public static users getUser_confirm() {
        return user_confirm;
    }


    public static ArrayList<meal> getMeals_are_filter() {
        return meals_are_filter;
    }

    public static void setMeals_are_filter(ArrayList<meal> meals_are_filter) {
        ServiceDataBaseHolder.meals_are_filter = meals_are_filter;
    }

    public static ArrayList<meal> getMeals_are_selected_to_my_menu_arraylist() {
        return meals_are_selected_to_my_menu_arraylist;
    }

    public static void setMeals_are_selected_to_my_menu_arraylist(ArrayList<meal> meals_are_selected_to_my_menu_arraylist) {
        ServiceDataBaseHolder.meals_are_selected_to_my_menu_arraylist = meals_are_selected_to_my_menu_arraylist;
    }


    public static ArrayList<meal> getMeals_arraylist_from_splashScreen() {
        return meals_arraylist_from_splashScreen;
    }

    public static void setMeals_arraylist_from_splashScreen(ArrayList<meal> meals_arraylist_from_splashScreen) {
        ServiceDataBaseHolder.meals_arraylist_from_splashScreen = meals_arraylist_from_splashScreen;
    }


    public static ArrayList<typesFood> getTypesFood_arraylist_from_splashScreen() {
        return typesFood_arraylist_from_splashScreen;
    }

    public static void setTypesFood_arraylist_from_splashScreen(ArrayList<typesFood> typesFood_arraylist_from_splashScreen) {
        ServiceDataBaseHolder.typesFood_arraylist_from_splashScreen = typesFood_arraylist_from_splashScreen;
    }

    public static ArrayList<allergies> getAllergies_arraylist_from_splashScreen() {
        return allergies_arraylist_from_splashScreen;
    }

    public static void setAllergies_arraylist_from_splashScreen(ArrayList<allergies> allergies_arraylist_from_splashScreen) {
        ServiceDataBaseHolder.allergies_arraylist_from_splashScreen = allergies_arraylist_from_splashScreen;
    }


    public static ArrayList<ingredients> getIngredients_arraylist_from_splashScreen() {
        return ingredients_arraylist_from_splashScreen;
    }

    public static void setIngredients_arraylist_from_splashScreen(ArrayList<ingredients> ingredients_arraylist_from_splashScreen) {
        ServiceDataBaseHolder.ingredients_arraylist_from_splashScreen = ingredients_arraylist_from_splashScreen;
    }

    public static void setLogo_meal(meal logo_meal) {
        ServiceDataBaseHolder.logo_meal = logo_meal;
    }



    public static ArrayList<meal> getMeals_not_confirm_yet_by_admin() {
        return meals_not_confirm_yet_by_admin;
    }

    public static void setMeals_not_confirm_yet_by_admin(ArrayList<meal> meals_not_confirm_yet_by_admin) {
        ServiceDataBaseHolder.meals_not_confirm_yet_by_admin = meals_not_confirm_yet_by_admin;
    }

    public static ArrayList<Record> getUsers_like_me_for_algo() {
        return users_like_me_for_algo;
    }

    public static void setUsers_like_me_for_algo(ArrayList<Record> users_like_me_for_algo) {
        ServiceDataBaseHolder.users_like_me_for_algo = users_like_me_for_algo;
    }

    public static String getIP_PATH() {
        //192.168.1.165  //192.168.245.123
        return "192.168.1.165";
    }
}