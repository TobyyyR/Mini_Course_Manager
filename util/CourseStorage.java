package util;

import model.Course;
import java.io.*;
import java.util.*;

public class CourseStorage {
    private static final String FILE_PATH = "courses.txt";

    public static void saveCourses(List<Course> courses) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Course c : courses) {
                out.printf("%s|%s|%s|%d|%s%n",
                    c.getName(), c.getCourseID(), c.getProfessor(), c.getCredits(), c.getGrade());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Course c = new Course(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]), parts[4]);
                    courses.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
}