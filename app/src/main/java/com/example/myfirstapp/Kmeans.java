package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Kmeans {
    ArrayList<Record> dataUsersLikeMeRecord = new ArrayList<Record>();
    Record findMainUserRecord = new Record();
    ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    Map<Cluster, List<Record>> clusterRecords = new HashMap<Cluster, List<Record>>();



    public void genereateRecord() {

        for(int i=0;i<ServiceDataBaseHolder.getUsers_like_me_for_algo().size();i++)
        {
            if(ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getId()==ServiceDataBaseHolder.getUser_confirm().getId())
            {
                //find the main user
                findMainUserRecord =ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i);
                //insert 100 if he have meals or if he dont have any meals
                findMainUserRecord.setPercentagesOfIdenticalMeals(100.0);


            }
        }
/**
 * Initializes the first 2 clusters in the data of the main user but changes the percentagesOfIdenticalMeals
 *  so that it creates 2 different points for me and from them I start to center the points in the algorithm
 */
        Record record1 = new Record(1, findMainUserRecord.getAge(), findMainUserRecord.getWeight(), findMainUserRecord.getHeight(),100);
        dataUsersLikeMeRecord.add(record1);
        record1 = new Record(2,findMainUserRecord.getAge(), findMainUserRecord.getWeight(), findMainUserRecord.getHeight(),0);
        dataUsersLikeMeRecord.add(record1);

        for(int i=0;i<ServiceDataBaseHolder.getUsers_like_me_for_algo().size();i++)
        {
            //insert to array all the users like the main user, include the main user
            if(ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i)!=findMainUserRecord) {
                Record record = new Record(ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getId(), ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getAge(), ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getWeight(), ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getHeight(), ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getIdMeals(), PercentagesOfIdenticalMeals(ServiceDataBaseHolder.getUsers_like_me_for_algo().get(i).getIdMeals()));
                dataUsersLikeMeRecord.add(record);
            }
        }
// add the main user to the list in the end of the list
        dataUsersLikeMeRecord.add(findMainUserRecord);

    }

    public void initiateClusterAndCenterPoint(int clusterNumber) {
        int counter = 1;
        Iterator<Record> iterator = dataUsersLikeMeRecord.iterator();

        Record record = null;
        while(iterator.hasNext()) {
            record = iterator.next();
              System.out.println(record);
            //for the first 2 iterator its initializeCluster 1 and 2
            if(counter <= clusterNumber) {
                record.setClusterNumber(counter);
                initializeCluster(counter, record);
                counter++;

            }
            //else all the next array well check distance
            else {


                System.out.println(record);
                System.out.println("------- cluster information -----");
                for(Cluster cluster : clusters) {
                    System.out.println(cluster);
                }
           //     find the min distance between the 2 clusters
                System.out.println("**************************");

                //temp max distance to get inside the if and than i take the minimum value distance
                double minDistance = Integer.MAX_VALUE;
                Cluster whichCluster = null;


                //run on the etch cluster and check the minimum value of distance
                for(Cluster cluster : clusters) {
                    double distance = cluster.calculateDistance(record);
                    System.out.println("       distance    "+distance);
                    if(minDistance > distance) {
                        minDistance = distance;
                        whichCluster = cluster;
                    }
                }

                // update the record cluster number  and the new centers points after get the min distance
                record.setClusterNumber(whichCluster.getClusterNumber());
                whichCluster.updateCenterPoints(record);
                //here set the
                clusterRecords.get(whichCluster).add(record);
            }
            System.out.println("----- clusters information -----");
            for(Cluster cluster : clusters) {
                System.out.println(cluster);
            }
            System.out.println("*********************");

        }

    }



    /**
     * @param clusterNumber -get 1 or 2 or more its up to me how many clusters i want and send from(HomeActivity)
     * @param record- in the record i get age ,weight , height of the main user and PercentagesOfIdenticalMeals i selected to  initialize 0.0 and 100.0
     */
    private void initializeCluster(int clusterNumber, Record record) {
        Cluster cluster = new Cluster(clusterNumber,record.getAge(),record.getWeight(),record.getHeight(),record.getPercentagesOfIdenticalMeals());
        clusters.add(cluster);
        ArrayList<Record> clusterRecord = new ArrayList<Record>();
        clusterRecord.add(record);
        clusterRecords.put(cluster, clusterRecord);
    }

    public void printRecordInformation() {
        System.out.println("****** Each Record INFORMATIN *********");
        for(Record record : dataUsersLikeMeRecord) {
            System.out.println(record);
        }
    }

    public ArrayList<Integer> bringRecordData() {
        //hold all the meals id are in the same cluster
        ArrayList<Integer> id_meals_in_clustr_selected=new ArrayList<>();
        int temp_cluster=0;
        // run all the records after all the data  set and check where the main id is.. in which cluster
        for(Record record : dataUsersLikeMeRecord) {
    if(record.getId()==findMainUserRecord.getId())
    {
        //find the cluster
        temp_cluster=record.getClusterNumber();
    }
        }
        for(Record record : dataUsersLikeMeRecord) {
            if(record.getClusterNumber()==temp_cluster&& record.getIdMeals()!=null)
            {
                //insert all meals id to array
                id_meals_in_clustr_selected.addAll(record.getIdMeals());

            }
        }
       System.out.println("  all id meals in array  "+id_meals_in_clustr_selected);
        // do distinct to the id meals
        LinkedHashSet<Integer> unique = new LinkedHashSet<Integer>(id_meals_in_clustr_selected);
        return new ArrayList<>(unique);
    }



    public void printClusterInformation() {
        System.out.println("****** FINAL CLUSTER INFORMATIN *********");
        for (Map.Entry<Cluster, List<Record>> entry : clusterRecords.entrySet())  {
            System.out.println("Key = " + entry.getKey()+"\n" +
                    ", Value = " + entry.getValue()+"\n");
        }
    }

    /**
     *
     * @param users_meals_array hold all the id meals from 1 user
     * @return i return the Percentage of the user if he have identical meals
     */
    public double PercentagesOfIdenticalMeals(ArrayList<Integer> users_meals_array) {
        double resultPercentages=0;
        int aSimilarMealCounter=0;
        if(users_meals_array.size()<=findMainUserRecord.getIdMeals().size())
        {
            for(int i=0;i<users_meals_array.size();i++)
            {
                for(int j=0;j<findMainUserRecord.getIdMeals().size();j++)
                {
                    if(users_meals_array.get(i).equals(findMainUserRecord.getIdMeals().get(j)))
                    {
                        aSimilarMealCounter++;
                    }
                }
            }
            resultPercentages=(double)aSimilarMealCounter*100/users_meals_array.size();
        }
        else {
            for(int i=0;i<findMainUserRecord.getIdMeals().size();i++)
            {
                for(int j=0;j<users_meals_array.size();j++)
                {
                    if(findMainUserRecord.getIdMeals().get(i).equals(users_meals_array.get(j)))
                    {
                        aSimilarMealCounter++;
                    }
                }
            }
            resultPercentages=(double)aSimilarMealCounter*100/findMainUserRecord.getIdMeals().size();
        }
        return resultPercentages;
    }
}