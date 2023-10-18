package edu.neu.csye6200.Model;

import java.util.*;

public class Classroom
{

    private static List<Integer> roomAgeGroupRules;
    private static Map<String, Integer> ageGroupMap;
    private final List<Teacher> teachers;
    private String name;
    private int ageGroup;

    public Classroom(String name, int ageGroup)
    {
        this.name = name;
        this.ageGroup = ageGroup;
        this.teachers = new ArrayList<>();
    }

    public static List<Integer> getRoomAgeGroupRules()
    {
        return roomAgeGroupRules;
    }

    private static void setRoomAgeGroupRules(List<Integer> roomAgeGroupRules)
    {
        Classroom.roomAgeGroupRules = roomAgeGroupRules;
    }

    private static void setAgeGroupMap(Map<String, Integer> ageGroupMap)
    {
        Classroom.ageGroupMap = ageGroupMap;
    }

    public static Classroom parseCSV(String csv)
    {
        // Format: "[name],[ageRange]"

        try
        {
            Scanner scanner = new Scanner(csv);
            scanner.useDelimiter(",");
            String name = scanner.next();
            String ageRange = scanner.next();
            int ageGroup = getAgeGroupFromAgeRange(ageRange);
            if (ageGroup == -1)
            {
                System.err.printf("Classroom [%s] creation error, age group [%s] not found%n", name, ageRange);
                return null;
            }
            return new Classroom(name, ageGroup);
        } catch (Exception e)
        {
            System.err.printf("Classroom CSV creation error - [%s]\n", csv);
            return null;
        }
    }

    public static int getAgeGroupFromAgeRange(String ageRange)
    {
        if (ageGroupMap == null) return -1;
        return ageGroupMap.getOrDefault(ageRange, -1);
    }

    public static int getAgeGroupByAge(int age)
    {
        for (String ageRange : ageGroupMap.keySet())
        {
            int splitIndex = ageRange.indexOf('-');
            int from = Integer.parseInt(ageRange.substring(0, splitIndex));
            int to = Integer.parseInt(ageRange.substring(splitIndex + 1));
            if (age >= from && age <= to) return ageGroupMap.get(ageRange);
        }
        return -1;
    }

    public static String getAgeRangeByAgeGroup(int ageGroup)
    {
        for (String ageRange : ageGroupMap.keySet())
        {
            if (ageGroupMap.get(ageRange) == ageGroup) return ageRange;
        }
        return null;
    }

    public static void loadRules(List<String> rules)
    {
        // Format: "[ageRange (e.g. '6-12')],[teacherCapacity],[classroomCapacity]

        Map<String, Integer> ageMap = new HashMap<>();
        List<Integer> roomRules = new ArrayList<>();
        List<Integer> teacherRules = new ArrayList<>();
        for (String rule : rules)
        {
            try
            {
                Scanner scanner = new Scanner(rule);
                scanner.useDelimiter(",");
                String ageRange = scanner.next();
                if (!ageRange.matches("[0-9]*-[0-9]*"))
                    throw new Exception("Rule's age range invalid");
                int groupSize = scanner.nextInt();
                int roomSize = scanner.nextInt();
                ageMap.put(ageRange, ageMap.size());
                teacherRules.add(groupSize);
                roomRules.add(roomSize);
            } catch (Exception e)
            {
                System.err.printf("Rule cannot be loaded - [%s] %s\n", rule, e.getMessage());
            }
        }
        setAgeGroupMap(ageMap);
        setRoomAgeGroupRules(roomRules);
        Teacher.setAgeGroupRules(teacherRules);
    }

    public int getNumOfTeachers()
    {
        return teachers.size();
    }

    public String toCSV()
    {
        return String.format("%s,%s",
                this.name,
                getAgeRangeByAgeGroup(this.ageGroup));
    }

    // ========= setters and getters ==========

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Teacher> getTeachers()
    {
        return teachers;
    }

    public int getAgeGroup()
    {
        return ageGroup;
    }

    public  Map<String, Integer> getAgeGroupMap() {
        return ageGroupMap;
    }

    public void setAgeGroup(int ageGroup)
    {
        this.ageGroup = ageGroup;
    }
    
//    public String getAgeRule(Teacher t){
//        int age = t.getAgeGroup();
//         for (String ageRange : ageGroupMap.keySet())
//        {
//            int splitIndex = ageRange.indexOf('-');
//            int from = Integer.parseInt(ageRange.substring(0, splitIndex));
//            int to = Integer.parseInt(ageRange.substring(splitIndex + 1)); 
//            if (age >= from && age <= to){
//            String result = String.valueOf(from) + "-" + String.valueOf(to);
//            return result;
//            }
//    }
//         return null;
//    }
}