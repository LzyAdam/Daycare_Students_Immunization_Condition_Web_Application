package edu.neu.csye6200.Model;

import edu.neu.csye6200.Helper.FileUtils;
import edu.neu.csye6200.Model.*;

import java.util.*;

public class DayCare
{
    private final static String RULES_CSV_FILEPATH = "src/main/resources/rules.txt";
    private final static String STUDENTS_CSV_FILEPATH = "src/main/resources/students.txt";
    private final static String TEACHERS_CSV_FILEPATH = "src/main/resources/teachers.txt";
    private final static String CLASSROOMS_CSV_FILEPATH = "src/main/resources/classrooms.txt";
    private final static String RELATIONS_CSV_FILEPATH = "src/main/resources/relations.txt";
    private final static String IMMU_RECORDS_CSV_FILEPATH = "src/main/resources/immunization_records.txt";
    private final static String CREDENTIALS_CSV_FILEPATH = "src/main/resources/credentials.txt";

    private final List<Student> students;
    private final List<Teacher> teachers;
    private final List<Classroom> classrooms;
    private final Map<String, String> credentials;

    public DayCare()
    {
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        classrooms = new ArrayList<>();
        credentials = new HashMap<>();
        init();
    }

    private void init()
    {
        // load student classroom rules
        List<String> rulesCSV = FileUtils.readAllLines(RULES_CSV_FILEPATH);
        assert rulesCSV != null;
        Classroom.loadRules(rulesCSV);

        // load students
        List<String> studentsCSV = FileUtils.readAllLines(STUDENTS_CSV_FILEPATH);
        assert studentsCSV != null;
        StudentFactory studentFactory = StudentFactory.getInstance();
        for (String csv : studentsCSV)
        {
            Student student = (Student) studentFactory.creatObj(csv);
            students.add(student);
        }

        // load teachers
        List<String> teachersCSV = FileUtils.readAllLines(TEACHERS_CSV_FILEPATH);
        assert teachersCSV != null;
        TeacherFactory teacherFactory = TeacherFactory.getInstance();
        for (String csv : teachersCSV)
        {
            Teacher teacher = (Teacher) teacherFactory.creatObj(csv);
            teachers.add(teacher);
        }

        // load classrooms
        List<String> classroomsCSV = FileUtils.readAllLines(CLASSROOMS_CSV_FILEPATH);
        assert classroomsCSV != null;
        for (String csv : classroomsCSV)
        {
            Classroom classroom = Classroom.parseCSV(csv);
            if (classroom != null) classrooms.add(classroom);
        }

        // load relations
        List<String> relationsCSV = FileUtils.readAllLines(RELATIONS_CSV_FILEPATH);
        assert relationsCSV != null;
        createRelations(relationsCSV);

        // load immunization records
        List<String> immuRecordsCSV = FileUtils.readAllLines(IMMU_RECORDS_CSV_FILEPATH);
        assert immuRecordsCSV != null;
        createImmuRecords(immuRecordsCSV);

        // load user credentials
        List<String> credentialsCSV = FileUtils.readAllLines(CREDENTIALS_CSV_FILEPATH);
        assert credentialsCSV != null;
        loadCredentials(credentialsCSV);
    }

    private void createRelations(List<String> relationCSV)
    {
        // Format: "[relationType ('student'/'teacher')],[Person],[PersonToBeAssigned]"

        for (String csv : relationCSV)
        {
            try
            {
                Scanner scanner = new Scanner(csv);
                scanner.useDelimiter(",");
                String type = scanner.next();
                if (type.equals("student"))
                {
                    String student = scanner.next();
                    String teacher = scanner.next();
                    Student studentObj = findStudentByName(student);
                    Teacher teacherObj = findTeacherByName(teacher);
                    if (studentObj == null || teacherObj == null)
                        throw new Exception("Student or teacher not found");
                    if (!studentObj.assignTeacher(teacherObj))
                        throw new Exception("Teacher's student list full or age group incorrect");
                    studentObj.assignTeacher(teacherObj);
                }
                else if (type.equals("teacher"))
                {
                    String teacher = scanner.next();
                    String classroom = scanner.next();
                    Teacher teacherObj = findTeacherByName(teacher);
                    Classroom classroomObj = findClassroomByName(classroom);
                    if (teacherObj == null || classroomObj == null)
                        throw new Exception("Teacher or classroom not found");
                    if (!teacherObj.assignClassroom(classroomObj))
                        throw new Exception("Classroom is full or age group incorrect");
                    teacherObj.assignClassroom(classroomObj);
                }
                else
                {
                    throw new Exception("Relation type error");
                }
            } catch (Exception e)
            {
                System.err.printf("Cannot initialize relation - [%s], ", csv);
                System.err.println(e.getMessage());
            }
        }
    }

    public void createImmuRecords(List<String> CSVs)
    {
        // Format: "[studentName],[vaccineType],[doseNumber],[injectionDate(yyyy-MM-dd)]"

        for (String csv : CSVs)
        {
            try
            {
                Scanner scanner = new Scanner(csv);
                scanner.useDelimiter(",");

                String studentName = scanner.next();
                Student student = findStudentByName(studentName);
                if (student == null)
                    throw new Exception("cannot find student by name!");
                if (!student.createImmuRecordFromCSV(scanner.nextLine()))
                    throw new Exception("cannot create immunization object!");
            } catch (Exception e)
            {
                System.err.printf("Cannot load immunization record - [%s] %s\n",
                        csv, e.getMessage() != null ? e.getMessage() : "");
            }
        }
    }

    public void loadCredentials(List<String> credentialsCSV)
    {
        // Format: "[username],[password]"

        for (String csv : credentialsCSV)
        {
            if (!csv.matches("[0-9a-zA-Z]*,[0-9a-zA-Z]*"))
            {
                System.err.println("Cannot load credential - " + csv);
                continue;
            }
            String username = csv.substring(0, csv.indexOf(","));
            String password = csv.substring(csv.indexOf(",") + 1);
            credentials.put(username, password);
        }
    }

    public void updateDB()
    {
        // Comment out for development purpose

//        // update students
//        List<String> studentsCSV = new ArrayList<>();
//        for (Student student : students) studentsCSV.add(student.toCSV());
//        FileUtils.writeToFile(STUDENTS_CSV_FILEPATH, studentsCSV);
//
//        // update teachers
//        List<String> teachersCSV = new ArrayList<>();
//        for (Teacher teacher : teachers) teachersCSV.add(teacher.toCSV());
//        FileUtils.writeToFile(TEACHERS_CSV_FILEPATH, teachersCSV);
//
//        // update classrooms
//        List<String> classroomsCSV = new ArrayList<>();
//        for (Classroom room : classrooms) classroomsCSV.add(room.toCSV());
//        FileUtils.writeToFile(CLASSROOMS_CSV_FILEPATH, classroomsCSV);
//
//        // update relations
//        List<String> relations = generateRelations();
//        FileUtils.writeToFile(RELATIONS_CSV_FILEPATH, relations);
//
        // update immunization records
        List<String> immuRecords = generateImmuRecords();
        FileUtils.writeToFile(IMMU_RECORDS_CSV_FILEPATH, immuRecords);
    }

    private List<String> generateRelations()
    {
        List<String> relations = new ArrayList<>();
        for (Student student : students)
        {
            if (student.getAssignedTeacher() != null)
            {
                String relation = String.format("student,%s,%s",
                        student.getName(),
                        student.getAssignedTeacher().getName());
                relations.add(relation);
            }
        }

        for (Teacher teacher : teachers)
        {
            if (teacher.getClassroom() != null)
            {
                String relation = String.format("teacher,%s,%s",
                        teacher.getName(),
                        teacher.getClassroom().getName());
                relations.add(relation);
            }
        }
        return relations;
    }

    private List<String> generateImmuRecords()
    {
        List<String> immuRecords = new ArrayList<>();
        for (Student student : students)
        {
            for (Immunization immunization : student.getImmunizations())
            {
                immuRecords.add(String.format("%s,%s", student.getName(), immunization.toCSV()));
            }
        }
        return immuRecords;
    }

    public boolean validateUser(String username, String password)
    {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    @Override
    public String toString()
    {
        return "ApplicationContext{" +
                "students=" + students +
                ", teachers=" + teachers +
                ", classrooms=" + classrooms +
                '}';
    }

    public Student findStudentByName(String name)
    {
        for (Student student : students)
        {
            if (student.getName().equals(name)) return student;
        }
        return null;
    }

    public boolean addStudent(Student student)
    {
        if (findStudentByName(student.getName()) != null)
            return false;
        students.add(student);
        return true;
    }

    public boolean deleteStudentByName(String studentName)
    {
        Student student = findStudentByName(studentName);
        if (student == null) return false;
        student.getAssignedTeacher().getStudents().remove(student);
        return students.remove(student);
    }

    public Teacher findTeacherByName(String name)
    {
        for (Teacher teacher : teachers)
        {
            if (teacher.getName().equals(name)) return teacher;
        }
        return null;
    }

    public boolean addTeacher(Teacher teacher)
    {
        if (findTeacherByName(teacher.getName()) != null)
            return false;
        teachers.add(teacher);
        return true;
    }

    public boolean deleteTeacherByName(String teacherName)
    {
        Teacher teacher = findTeacherByName(teacherName);
        if (teacher == null) return false;
        teacher.getClassroom().getTeachers().remove(teacher);
        return teachers.remove(teacher);
    }

    public Classroom findClassroomByName(String name)
    {
        for (Classroom classroom : classrooms)
        {
            if (classroom.getName().equals(name)) return classroom;
        }
        return null;
    }

    public boolean addClassroom(Classroom room)
    {
        if (findClassroomByName(room.getName()) != null)
            return false;
        classrooms.add(room);
        return true;
    }

    public boolean deleteClassroomByName(String classroomName)
    {
        Classroom room = findClassroomByName(classroomName);
        if (room == null) return false;
        return classrooms.remove(room);
    }

    public List<Student> getStudents()
    {
        return students;
    }

    public List<Teacher> getTeachers()
    {
        return teachers;
    }

    public List<Classroom> getClassrooms()
    {
        return classrooms;
    }
}
