package com.example.myfirstapp;


public  class allergies{
  private  Integer id;
    private String name;

    public allergies(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "allergies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
