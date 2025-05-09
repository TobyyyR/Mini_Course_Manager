import controller.CourseManager;
import controller.AuthManager;
import model.Course;
import model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuthManager auth = new AuthManager();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Mini NYU Course Manager!");
            System.out.print("Do you want to (l)ogin or (r)egister? ");
            String choice = scanner.nextLine();

            User user = null;
            while (user == null) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (choice.equalsIgnoreCase("r")) {
                    if (auth.register(username, password)) {
                        System.out.println("Registered successfully. Please log in.");
                        choice = "l";
                    } else {
                        System.out.println("Username already exists.");
                    }
                } else {
                    user = auth.login(username, password);
                    if (user == null) {
                        System.out.println("Invalid login. Try again.");
                    }
                }
            }

            System.out.println("Welcome, " + user.getUsername() + "!");

            CourseManager manager = new CourseManager();

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Add Course");
                System.out.println("2. Modify Course");
                System.out.println("3. Remove Course");
                System.out.println("4. Browse Courses");
                System.out.println("5. Show GPA & Credits");
                System.out.println("6. Generate Course Report");
                System.out.println("7. Exit");

                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        System.out.print("Course name: ");
                        String name = scanner.nextLine();
                        System.out.print("Course ID: ");
                        String id = scanner.nextLine();
                        System.out.print("Professor: ");
                        String prof = scanner.nextLine();
                        System.out.print("Credits: ");
                        int credits = Integer.parseInt(scanner.nextLine());
                        System.out.print("Grade (e.g., A, B+, F): ");
                        String grade = scanner.nextLine();
                        try {
                            manager.addCourse(new Course(name, id, prof, credits, grade));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid grade. Course not added.");
                        }
                        break;

                    case "2":
                        System.out.print("Enter Course ID to modify: ");
                        String modID = scanner.nextLine();
                        Course existing = manager.findCourseByID(modID);
                        if (existing == null) {
                            System.out.println("Course not found.");
                            break;
                        }
                        System.out.println("Leave blank to keep existing value.");
                        System.out.print("New name (" + existing.getName() + "): ");
                        String newName = scanner.nextLine();
                        System.out.print("New professor (" + existing.getProfessor() + "): ");
                        String newProf = scanner.nextLine();
                        System.out.print("New credits (" + existing.getCredits() + "): ");
                        String newCreditsStr = scanner.nextLine();
                        System.out.print("New grade (" + existing.getGrade() + "): ");
                        String newGrade = scanner.nextLine();

                        try {
                            Course updated = new Course(
                                newName.isEmpty() ? existing.getName() : newName,
                                modID,
                                newProf.isEmpty() ? existing.getProfessor() : newProf,
                                newCreditsStr.isEmpty() ? existing.getCredits() : Integer.parseInt(newCreditsStr),
                                newGrade.isEmpty() ? existing.getGrade() : newGrade
                            );
                            manager.updateCourse(modID, updated);
                            System.out.println("Course updated.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid update. " + e.getMessage());
                        }
                        break;

                    case "3":
                        System.out.print("Enter Course ID to remove: ");
                        String removeID = scanner.nextLine();
                        manager.removeCourse(removeID);
                        System.out.println("Course removed.");
                        break;

                    case "4":
                        System.out.println("All Courses:");
                        manager.getAllCourses().forEach(System.out::println);
                        break;

                    case "5":
                        System.out.printf("Total Credits: %d%n", manager.getTotalCredits());
                        System.out.printf("GPA: %.2f%n", manager.getGPA());
                        break;

                    case "6":
                        manager.generateReport(user.getUsername());
                        break;

                    case "7":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }
}