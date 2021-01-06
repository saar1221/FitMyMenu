package com.example.myfirstapp;


import java.io.Serializable;
import java.util.ArrayList;

public   class users implements Serializable {

    Integer id;
    String name;
    String email;
    Double weight;
    Double height;
    Integer age;
    String purpose;
    Integer level_activity;
    Boolean diabetes;
    String gender;
    Boolean csv_file;
   Integer id_types_food;
   ArrayList<allergies> user_allergies;

    public users() {

    }
  public users(Integer id, Double weight, Double height, Integer age) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }



    public users(Integer id, String name, String email, Double weight, Double height, Integer age, String purpose, Integer level_activity, Boolean diabetes, String gender, Boolean csv_file, Integer id_types_food) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.purpose = purpose;
        this.level_activity = level_activity;
        this.diabetes = diabetes;
        this.gender = gender;
        this.csv_file = csv_file;
        this.id_types_food = id_types_food;
    }







    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Boolean getDiabetes() {
        return diabetes;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getLevel_activity() {
        return level_activity;
    }

    public void setLevel_activity(Integer level_activity) {
        this.level_activity = level_activity;
    }

    public Integer getId_types_food() {
        return id_types_food;
    }

    public ArrayList<allergies> getUser_allergies() {
        return user_allergies;
    }


    public void setUser_allergies(ArrayList<allergies> user_allergies) {
        this.user_allergies = new ArrayList<>(user_allergies);
    }

    @Override
    public String toString() {
        return "users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", purpose='" + purpose + '\'' +
                ", level_activity=" + level_activity +
                ", diabetes=" + diabetes +
                ", gender='" + gender + '\'' +
                ", csv_file=" + csv_file +
                ", id_types_food=" + id_types_food +
                ", user_allergies=" + user_allergies +
                '}';
    }
}

//    public users(Integer id, String name, String email, Double weight, Double height, Integer age, String purpose, Integer level_activity, Boolean diabetes, String gender, Boolean csv_file) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.weight = weight;
//        this.height = height;
//        this.age = age;
//        this.purpose = purpose;
//        this.level_activity = level_activity;
//        this.diabetes = diabetes;
//        this.gender = gender;
//        this.csv_file = csv_file;
//    }