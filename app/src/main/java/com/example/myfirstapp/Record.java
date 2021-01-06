package com.example.myfirstapp;


import java.util.ArrayList;

public class Record {

    private int id;
    private int age;
    private double weight;
    private double height;
    private ArrayList<Integer> idMeals;
    private double PercentagesOfIdenticalMeals;
    private int clusterNumber;


    public Record(int id, int age, double weight, double height,ArrayList<Integer> idMeals) {
        super();
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.idMeals = idMeals;
    }

    public Record() {
    }

    public Record(int id, int age, double weight, double height, double percentagesOfIdenticalMeals) {
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        PercentagesOfIdenticalMeals = percentagesOfIdenticalMeals;
    }

    public Record(int id, int age, double weight, double height, ArrayList<Integer> idMeals, double percentagesOfIdenticalMeals) {
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.idMeals = idMeals;
        PercentagesOfIdenticalMeals = percentagesOfIdenticalMeals;

    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;}
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) { this.weight = weight; }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public int getClusterNumber() {
        return clusterNumber;
    }
    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }
    public ArrayList<Integer> getIdMeals() { return idMeals; }
    public void setIdMeals(ArrayList<Integer> idMeals) { this.idMeals = idMeals; }


    public double getPercentagesOfIdenticalMeals() {
        return PercentagesOfIdenticalMeals;
    }

    public void setPercentagesOfIdenticalMeals(double percentagesOfIdenticalMeals) {
        PercentagesOfIdenticalMeals = percentagesOfIdenticalMeals;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", idMeals=" + idMeals +
                ", PercentagesOfIdenticalMeals=" + PercentagesOfIdenticalMeals +
                ", clusterNumber=" + clusterNumber +
                '}';
    }
}