package util;

import model.Course;
import java.util.*;

public class GPAUtils {
    private static final Map<String, Double> gradeMap = new HashMap<>();

    static {
        gradeMap.put("A", 4.0);
        gradeMap.put("A-", 3.7);
        gradeMap.put("B+", 3.3);
        gradeMap.put("B", 3.0);
        gradeMap.put("B-", 2.7);
        gradeMap.put("C+", 2.3);
        gradeMap.put("C", 2.0);
        gradeMap.put("C-", 1.7);
        gradeMap.put("D", 1.0);
        gradeMap.put("F", 0.0);
        gradeMap.put("NP", 0.0);
        gradeMap.put("P", null); 
    }

    public static double calculateGPA(List<Course> courses) {
        double totalPoints = 0.0;
        int totalCredits = 0;
        for (Course c : courses) {
            Double gpaValue = gradeMap.get(c.getGrade());
            if (gpaValue != null) {
                totalPoints += gpaValue * c.getCredits();
                totalCredits += c.getCredits();
            }
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    public static int calculateTotalCredits(List<Course> courses) {
        return courses.stream().mapToInt(Course::getCredits).sum();
    }

    public static boolean isValidGrade(String grade) {
        return gradeMap.containsKey(grade);
    }

    public static boolean isPassingGrade(String grade) {
        Double gpaValue = gradeMap.get(grade);
        return gpaValue == null || gpaValue >= 2.0;
    }
}