package controller;

import model.Course;
import java.util.*;

public class CourseManager {
    private Map<String, List<Course>> userCourses; // Map username to their courses
    private String currentUser;

    public CourseManager() {
        this.userCourses = new HashMap<>();
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
        if (!userCourses.containsKey(username)) {
            userCourses.put(username, new ArrayList<>());
        }
    }

    public void addCourse(Course course) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        userCourses.get(currentUser).add(course);
    }

    public void updateCourse(String courseId, Course updatedCourse) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        List<Course> courses = userCourses.get(currentUser);
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseID().equals(courseId)) {
                courses.set(i, updatedCourse);
                return;
            }
        }
        throw new IllegalArgumentException("Course not found");
    }

    public void removeCourse(String courseId) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        List<Course> courses = userCourses.get(currentUser);
        courses.removeIf(course -> course.getCourseID().equals(courseId));
    }

    public List<Course> getAllCourses() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return new ArrayList<>(userCourses.get(currentUser));
    }

    public Course findCourseByID(String courseId) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return userCourses.get(currentUser).stream()
                .filter(course -> course.getCourseID().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    public double getGPA() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        List<Course> courses = userCourses.get(currentUser);
        if (courses.isEmpty()) return 0.0;

        double totalPoints = 0;
        int totalCredits = 0;

        for (Course course : courses) {
            // Skip Pass/Fail courses in GPA calculation
            if (course.getGrade().equalsIgnoreCase("P")) {
                continue;
            }
            double gradePoints = getGradePoints(course.getGrade());
            totalPoints += gradePoints * course.getCredits();
            totalCredits += course.getCredits();
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public int getTotalCredits() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return userCourses.get(currentUser).stream()
                .mapToInt(Course::getCredits)
                .sum();
    }

    public void generateReport(String username) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        List<Course> courses = userCourses.get(currentUser);
        StringBuilder report = new StringBuilder();
        report.append("Course Report for ").append(username).append("\n");
        report.append("================================\n\n");
        
        for (Course course : courses) {
            report.append(course.toString()).append("\n");
        }
        
        report.append("\nSummary:\n");
        report.append("Total Credits: ").append(getTotalCredits()).append("\n");
        report.append("GPA: ").append(String.format("%.2f", getGPA())).append("\n");

        // Save to file
        try {
            java.io.FileWriter writer = new java.io.FileWriter("report_" + username + ".txt");
            writer.write(report.toString());
            writer.close();
            System.out.println("Report saved to: report_" + username + ".txt");
        } catch (java.io.IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }

    private double getGradePoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "F": return 0.0;
            case "P": return 0.0; // Pass grade doesn't affect GPA
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }
}