package edu.neu.csye6200.Model;

import java.time.LocalDate;
import java.util.Scanner;

public class StudentFactory extends PersonFactory
{

    private static StudentFactory singleton = null;

    private StudentFactory()
    {
    }

    public static StudentFactory getInstance()
    {
        if (singleton == null)
            singleton = new StudentFactory();
        return singleton;
    }

    @Override
    public Person creatObj(String csv)
    {
        // Format: "[name],[age],[parentName],[parentAddress],[parentPhoneNum],[registrationDate(yyyy-MM-dd)]"

        try
        {
            Scanner scanner = new Scanner(csv);
            scanner.useDelimiter(",");

            String name = scanner.next();
            int age = scanner.nextInt();
            String parentName = scanner.next();
            String parentAddress = scanner.next();
            String parentPhone = scanner.next();
            LocalDate registrationDate = LocalDate.parse(scanner.next());

            return new Student(name, age, parentName, parentAddress, parentPhone, registrationDate);
        } catch (Exception e)
        {
            System.err.printf("Student CSV creation error - [%s]\n", csv);
            return null;
        }
    }
}