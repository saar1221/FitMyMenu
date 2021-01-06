package com.example.myfirstapp;


import java.util.ArrayList;

public class Cluster {

    private int ageCenterPoint;
    private double weightCenterPoint;
    private double heightCenterPoint;
    private double IdenticalMealsCenterPoint;
    private int clusterNumber;



    public Cluster(int clusterNumber, int ageCenterPoint, double weightCenterPoint, double heightCenterPoint, double IdenticalMealsCenterPoint) {
        super();
        this.clusterNumber = clusterNumber;
        this.ageCenterPoint = ageCenterPoint;
        this.weightCenterPoint = weightCenterPoint;
        this.heightCenterPoint = heightCenterPoint;
        this.IdenticalMealsCenterPoint = IdenticalMealsCenterPoint;
    }

    public int getAgeCenterPoint() {
        return ageCenterPoint;
    }
    public void setAgeCenterPoint(int ageCenterPoint) {
        this.ageCenterPoint = ageCenterPoint;
    }
    public double getWeightCenterPoint() {
        return weightCenterPoint;
    }
    public void setWeightCenterPoint(double weightCenterPoint) { this.weightCenterPoint = weightCenterPoint; }
    public double getHeightCenterPoint() {
        return heightCenterPoint;
    }
    public void setHeightCenterPoint(double heightCenterPoint) {this.heightCenterPoint = heightCenterPoint; }
    public double getIdenticalMealsCenterPoint() { return IdenticalMealsCenterPoint; }
    public void setIdenticalMealsCenterPoint(double identicalMealsCenterPoint) { IdenticalMealsCenterPoint = identicalMealsCenterPoint; }
    public int getClusterNumber() {
        return clusterNumber;
    }
    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    // Euclidean distance calculation
    public double calculateDistance(Record record) {
        return Math.sqrt(Math.pow((getAgeCenterPoint() - record.getAge()), 2) + Math.pow((getWeightCenterPoint() - record.getWeight()),2) + Math.pow((getHeightCenterPoint() - record.getHeight()), 2)+ Math.pow((getIdenticalMealsCenterPoint() - record.getPercentagesOfIdenticalMeals()), 2));
    }

    public void updateCenterPoints(Record record) {
        setAgeCenterPoint((getAgeCenterPoint()+record.getAge())/2);
        setWeightCenterPoint((getWeightCenterPoint()+record.getWeight())/2);
        setHeightCenterPoint((getHeightCenterPoint()+record.getHeight())/2);
        setIdenticalMealsCenterPoint((getIdenticalMealsCenterPoint()+record.getPercentagesOfIdenticalMeals())/2);
    }


    @Override
    public String toString() {
        return "Cluster{" +
                "ageCenterPoint=" + ageCenterPoint +
                ", weightCenterPoint=" + weightCenterPoint +
                ", heightCenterPoint=" + heightCenterPoint +
                ", IdenticalMealsCenterPoint=" + IdenticalMealsCenterPoint +
                ", clusterNumber=" + clusterNumber +
                '}';
    }
}