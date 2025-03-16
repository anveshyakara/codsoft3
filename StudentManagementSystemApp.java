import java.io.*;
import java.util.*;

public class StudentManagementSystemApp {

    // Student class to represent individual students
    public static class Student implements Serializable {
        private String name;
        private String rollNumber;
        private String grade;
        private int age;
        private String address;

        // Constructor
        public Student(String name, String rollNumber, String grade, int age, String address) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.grade = grade;
            this.age = age;
            this.address = address;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public String getGrade() {
            return grade;
        }

        public int getAge() {
            return age;
        }

        public String getAddress() {
            return address;
        }

        // Method to display student information
        @Override
        public String toString() {
            return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade +
                   ", Age: " + age + ", Address: " + address;
        }
    }

    // StudentManagementSystem class to manage a list of students
    public static class StudentManagementSystem {
        private List<Student> students;

        public StudentManagementSystem() {
            students = new ArrayList<>();
        }

        // Method to add a student
        public void addStudent(Student student) {
            students.add(student);
        }

        // Method to remove a student by roll number
        public void removeStudent(String rollNumber) {
            students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        }

        // Method to search for a student by roll number
        public Student searchStudent(String rollNumber) {
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    return student;
                }
            }
            return null; // Return null if student not found
        }

        // Method to display all students
        public void displayAllStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println(student);
                }
            }
        }
    }

    // StudentManagementSystemWithFile class for file handling (serialization)
    public static class StudentManagementSystemWithFile extends StudentManagementSystem {
        private final String filename = "students.ser";

        public StudentManagementSystemWithFile() {
            super();
            loadData();
        }

        // Load student data from file
        public void loadData() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                List<Student> loadedStudents = (List<Student>) ois.readObject();
                for (Student student : loadedStudents) {
                    super.addStudent(student);
                }
            } catch (FileNotFoundException e) {
                // No data file found, it's okay
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Save student data to file
        public void saveData() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(super.students);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Override addStudent to save data to file after adding a student
        @Override
        public void addStudent(Student student) {
            super.addStudent(student);
            saveData();
        }

        // Override removeStudent to save data to file after removing a student
        @Override
        public void removeStudent(String rollNumber) {
            super.removeStudent(rollNumber);
            saveData();
        }
    }

    // Main class to interact with the user
    public static class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            StudentManagementSystem system = new StudentManagementSystemWithFile();

            while (true) {
                System.out.println("\nStudent Management System");
                System.out.println("1. Add Student");
                System.out.println("2. Remove Student");
                System.out.println("3. Search Student");
                System.out.println("4. Display All Students");
                System.out.println("5. Exit");

                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (choice == 1) {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter roll number: ");
                    String rollNumber = scanner.nextLine();

                    System.out.print("Enter grade: ");
                    String grade = scanner.nextLine();

                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();

                    Student student = new Student(name, rollNumber, grade, age, address);
                    system.addStudent(student);
                    System.out.println("Student added successfully!");

                } else if (choice == 2) {
                    System.out.print("Enter the roll number of the student to remove: ");
                    String rollNumber = scanner.nextLine();
                    system.removeStudent(rollNumber);
                    System.out.println("Student with roll number " + rollNumber + " removed.");

                } else if (choice == 3) {
                    System.out.print("Enter roll number to search: ");
                    String rollNumber = scanner.nextLine();
                    Student student = system.searchStudent(rollNumber);
                    if (student != null) {
                        System.out.println(student);
                    } else {
                        System.out.println("Student not found.");
                    }

                } else if (choice == 4) {
                    system.displayAllStudents();

                } else if (choice == 5) {
                    System.out.println("Exiting the system.");
                    break;

                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }

            scanner.close();
        }
    }
}
