package com.example.myfirstapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

public  class meal{
    Integer idMeal;
    String nameMeal;
    String description;
    Double calories;
    Integer tyeps;
    Integer idUserUpload;
    Boolean dangerousForDiabetics;
    Bitmap image;
    ArrayList<IngredientsAndCount> ingredientsCounts;
    Boolean adminConfirm;
    Integer addByUsers;

public meal(){}



    public meal(Integer idMeal, String nameMeal, String description, Double calories, Integer tyeps, Integer idUserUpload, Boolean dangerousForDiabetics, Boolean adminConfirm, Integer addByUsers) {
        this.idMeal = idMeal;
        this.nameMeal = nameMeal;
        this.description = description;
        this.calories = calories;
        this.tyeps = tyeps;
        this.idUserUpload = idUserUpload;
        this.dangerousForDiabetics = dangerousForDiabetics;
        this.adminConfirm = adminConfirm;
        this.addByUsers = addByUsers;
    }

    public meal(Integer idMeal, String nameMeal, String description, Double calories, Integer tyeps, Integer idUserUpload, Boolean dangerousForDiabetics, Bitmap image, ArrayList<IngredientsAndCount> ingredientsCounts, Boolean adminConfirm) {
        this.idMeal = idMeal;
        this.nameMeal = nameMeal;
        this.description = description;
        this.calories = calories;
        this.tyeps = tyeps;
        this.idUserUpload = idUserUpload;
        this.dangerousForDiabetics = dangerousForDiabetics;
        this.image = image;
        this.ingredientsCounts = ingredientsCounts;
        this.adminConfirm = adminConfirm;
    }





    public ArrayList<IngredientsAndCount> getIngredientsCounts() {
        return ingredientsCounts;
    }

// ------------------------------------     אתה צריך לעשות את זה כמו פה כי אני לא חוזר לאותה נקודה בזיכרון ודורס נתונים אני יוצר פה מקום חדש בזיכרון------------------------------------------------------------------------------------
    public void setIngredientsCounts(ArrayList<IngredientsAndCount> ingredientsCounts) {
        this.ingredientsCounts = new ArrayList<>(ingredientsCounts) ;
    }
public Integer sizeIngredients_counts (){
    return   getIngredientsCounts().size();
}
    public String getName_ingredient() {
        return nameMeal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Integer getTyeps() {
        return tyeps;
    }

    public void setTyeps(Integer tyeps) {
        this.tyeps = tyeps;
    }

    public Boolean getDangerousForDiabetics() {
        return dangerousForDiabetics;
    }

    public void setDangerousForDiabetics(Boolean dangerousForDiabetics) {
        this.dangerousForDiabetics = dangerousForDiabetics;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Integer getIdMeal() {
        return idMeal;
    }

    public String getNameMeal() {
        return nameMeal;
    }

    public void setNameMeal(String nameMeal) {
        this.nameMeal = nameMeal;
    }

    // print all the Ingredients names in the selected meal
    public String toStringIngredients ( ){
        StringBuilder tempString= new StringBuilder();
        for(int k = 0; k< ingredientsCounts.size(); k++){
            for(int i=0 ;i<ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().size();i++) {
                if (ingredientsCounts.get(k).getId_ingredient().equals(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i).getId_ingredient())) {
                    tempString.append("\n").append(ServiceDataBaseHolder.getIngredients_arraylist_from_splashScreen().get(i).getName_ingredient()).append("-").append(ingredientsCounts.get(k).getCount());
                }
            }
        }
        return tempString.toString();
    }

    public Boolean getAdminConfirm() {
        return adminConfirm;
    }


    public Integer getAddByUsers() {
        return addByUsers;
    }

    public void setAddByUsers(Integer addByUsers) {
        this.addByUsers = addByUsers;
    }

    @Override
    public String toString() {
        return "meal{" +
                "idMeal=" + idMeal +
                ", nameMeal='" + nameMeal + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", tyeps=" + tyeps +
                ", idUserUpload=" + idUserUpload +
                ", dangerousForDiabetics=" + dangerousForDiabetics +
                ", image=" + image +
                ", ingredientsCounts=" + ingredientsCounts +
                ", adminConfirm=" + adminConfirm +
                ", addByUsers=" + addByUsers +
                '}';
    }
}
//    public meal(Integer id_meal, String name_meal, String description, Double calories, Integer tyeps, Integer idUserUpload, Boolean dangerousForDiabetics, Bitmap image) {
//        this.id_meal = id_meal;
//        this.name_meal = name_meal;
//        this.description = description;
//        this.calories = calories;
//        this.tyeps = tyeps;
//        this.idUserUpload = idUserUpload;
//        this.dangerousForDiabetics = dangerousForDiabetics;
//        this.image = image;
//    }
//    public meal(String name_meal, Bitmap image, String description, Double calories) {
//        this.name_meal = name_meal;
//        this.image = image;
//        this.description = description;
//        this.calories = calories;
//    }
//public void AddIngredientsAndCount(IngredientsAndCount add_temp)
//{
//
//    this.ingredients_counts=new ArrayList<>();
//
//    this.ingredients_counts.add(new IngredientsAndCount(add_temp));
//}
//    public Integer getId_ingredient() {
//        return id_meal;
//    }
//
//    public void setId_ingredient(Integer id_ingredient) {
//        this.id_meal = id_ingredient;
//    }
//public void setId_meal(Integer id_meal) {
//    this.id_meal = id_meal;
//}
//    public void setName_ingredient(String name_ingredient) {
//        this.name_meal = name_ingredient;
//    }
//
//    public Integer getIdUserUpload() {
//        return idUserUpload;
//    }
//
//    public void setIdUserUpload(Integer idUserUpload) {
//        this.idUserUpload = idUserUpload;
//    }
//public void setAdmin_confirm(Boolean admin_confirm) {
//    this.admin_confirm = admin_confirm;
//}