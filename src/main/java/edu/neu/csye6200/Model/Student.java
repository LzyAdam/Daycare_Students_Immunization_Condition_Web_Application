package edu.neu.csye6200.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Student extends Person
{
    private int age;
    private String parentName;
    private String parentAddress;
    private String parentPhoneNum;
    private LocalDate registrationDate;
    private Teacher assignedTeacher;
    private List<Immunization> immunizations;

    public Student(String name, int age, String parentName, String parentAddress, String parentPhoneNum, LocalDate registrationDate)
    {
        super(name);
        this.parentName = parentName;
        this.parentAddress = parentAddress;
        this.parentPhoneNum = parentPhoneNum;
        this.registrationDate = registrationDate;
        this.age = age;
        this.immunizations = new ArrayList<>();
    }

    public boolean assignTeacher(Teacher teacher)
    {
        if (teacher.getNumOfStudents() < Teacher.getAgeGroupRules().get(teacher.getAgeGroup()) &&
                Classroom.getAgeGroupByAge(this.age) == teacher.getAgeGroup())
        {
            if (teacher != this.assignedTeacher)
            {
                if (this.assignedTeacher != null)
                    this.assignedTeacher.getStudents().remove(this);
                setAssignedTeacher(teacher);
                teacher.getStudents().add(this);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean createImmuRecord(String type, int dose, LocalDate date)
    {
        Immunization immunization = new Immunization(type, dose, date);
        immunizations.add(immunization);
        return true;
    }

    public boolean createImmuRecordFromCSV(String csv)
    {
        try
        {
            Scanner scanner = new Scanner(csv);
            scanner.useDelimiter(",");

            String type = scanner.next();
            int dose = scanner.nextInt();
            String dateStr = scanner.next();
            LocalDate date = LocalDate.parse(dateStr);

            Immunization immunization = new Immunization(type, dose, date);
            immunizations.add(immunization);
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return name;
    }

    public String toCSV()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s,%s,%s,%s,%s,%s",
                this.name,
                this.age,
                this.parentName,
                this.parentAddress,
                this.parentPhoneNum,
                this.registrationDate.format(formatter));
    }

    // ========= setters and getters ==========

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getParentAddress()
    {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress)
    {
        this.parentAddress = parentAddress;
    }

    public String getParentPhoneNum()
    {
        return parentPhoneNum;
    }

    public void setParentPhoneNum(String parentPhoneNum)
    {
        this.parentPhoneNum = parentPhoneNum;
    }

    public LocalDate getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate)
    {
        this.registrationDate = registrationDate;
    }

    public Teacher getAssignedTeacher()
    {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher)
    {
        this.assignedTeacher = assignedTeacher;
    }

    public List<Immunization> getImmunizations()
    {
        return immunizations;
    }

    public void setImmunizations(List<Immunization> immunizations)
    {
        this.immunizations = immunizations;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }
}