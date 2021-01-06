package com.example.myfirstapp;


public class IngredientsAndCount extends ingredients{
   private   String count;

    public IngredientsAndCount(Integer id_ingredients,String name_ingredient, String count) {
        super(id_ingredients,name_ingredient);
        this.count = count;
    }
    public IngredientsAndCount(Integer id_ingredients,String count) {
        super(id_ingredients);
        this.count = count;
    }



    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "IngredientsAndCount{" +
                "count='" + count + '\'' +
                ", id_ingredient=" + id_ingredient + '\'' +
                ", name_ingredient='" + name_ingredient + '\'' +
                '}';
    }
}
