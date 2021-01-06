package com.example.myfirstapp;

public  class ingredients { // do protected if i extend
     protected     Integer id_ingredient;
    protected   String name_ingredient;




public ingredients(Integer id_ingredient){
    this.id_ingredient = id_ingredient;
}
    public ingredients(Integer id_ingredient, String name_ingredient) {
        this.id_ingredient = id_ingredient;
        this.name_ingredient = name_ingredient;
    }

    public Integer getId_ingredient() {
        return id_ingredient;
    }

    public String getName_ingredient() {
        return name_ingredient;
    }


    @Override
    public String toString() {
        return "ingredients{" +
                "id=" + id_ingredient +
                ", name=" + name_ingredient +
                '}';
    }
}
