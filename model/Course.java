package model;

public class Course {
    private String name;
    private String courseID;
    private String professor;
    private int credits;
    private String grade;

    public Course(String name, String courseID, String professor, int credits, String grade) {
        this.name = name;
        this.courseID = courseID;
        this.professor = professor;
        this.credits = credits;
        this.grade = grade;
    }

    public String getName() { return name; }
    public String getCourseID() { return courseID; }
    public String getProfessor() { return professor; }
    public int getCredits() { return credits; }
    public String getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s | Credits: %d | Grade: %s", name, courseID, professor, credits, grade);
    }
}