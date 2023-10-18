package edu.neu.csye6200.Model;

import java.time.LocalDate;
import java.util.Scanner;

public class TeacherFactory extends PersonFactory
{

    private static TeacherFactory singleton = null;

    public TeacherFactory()
    {
    }

    public static TeacherFactory getInstance()
    {
        if (singleton == null)
            singleton = new TeacherFactory();
        return singleton;
    }

    @Override
    public Person creatObj(String csv)
    {
        // Format: "[name],[ageGroup(e.g. '6-12')],[reviewDate(yyyy-MM-dd)]
        try
        {
            Scanner scanner = new Scanner(csv);
            scanner.useDelimiter(",");

            String name = scanner.next();
            int ageGroup = Classroom.getAgeGroupFromAgeRange(scanner.next());
            LocalDate reviewDate = LocalDate.parse(scanner.next());

            return new Teacher(name, ageGroup, reviewDate);
        } catch (Exception e)
        {
            System.err.printf("Teacher CSV creation error - [%s]\n", csv);
            return null;
        }
    }
}