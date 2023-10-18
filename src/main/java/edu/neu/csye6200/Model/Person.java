package edu.neu.csye6200.Model;

public abstract class Person
{
    protected String name;

    public Person(String name)
    {
        this.name = name;
    }

    public abstract String toCSV();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}