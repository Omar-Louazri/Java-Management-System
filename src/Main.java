import com.packages.ConsoleUtils;
import com.packages.LoginSystem.LoginSystem;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Clear console (fixes "cls" issue)
        ConsoleUtils.clearScreen();
        callMenu(); // Start the menu
    }

    static void callMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) { // Loop to keep showing the menu
            
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1.Login");
            System.out.println("2.Register");
            System.out.println("3.Report an issue");
            System.out.println("4.Exit");
            System.out.print("Choose an option: ");

            // Validate input as an integer
            if (!scanner.hasNextInt()) {
                ConsoleUtils.clearScreen();
                System.out.println("ERROR: Invalid input! Please enter a number (1-4).");
                scanner.next(); // Consume invalid input
                continue; // Restart loop
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    ConsoleUtils.clearScreen();
                    LoginSystem.login(scanner);
                    break;

                case 2:
                    System.out.println("Register a new user:");
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your grade: ");
                    int grade_raw = 0;
                    try {
                        grade_raw = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        ConsoleUtils.clearScreen();
                        System.out.println("Invalid grade! Please enter a valid grade (1-12).");
                        scanner.nextLine(); // Consume invalid input
                        continue;
                    }
                    String grade = "";
                    if (grade_raw <= 0 || grade_raw > 12) {
                        System.out.println("Invalid grade! Please enter a valid grade (1-12).");
                        continue;
                    }
                    if (grade_raw <= 0 || grade_raw > 12) {
                        System.out.println("Invalid grade! Please enter a valid grade (1-12).");
                        continue;
                    }else if (grade_raw == 1) {
                        grade = "1st";
                    }else if (grade_raw == 2) {
                        grade = "2nd";
                    }else if (grade_raw == 3) {
                        grade = "3rd";
                    }else if (grade_raw > 3) {
                        grade = grade_raw + "th";
                    }
                    int id = LoginSystem.register(name,grade);
                    System.out.println("Registration successfully made to the record! \n" +
                                        "Welcome, " + name + "!" +
                                        "Your id is " + id + ".");

                    break;

                case 3:
                    ConsoleUtils.clearScreen();
                    System.out.println("Report an Issue:");
                    System.out.print("Describe the issue: ");
                    String issue = scanner.nextLine();
                    System.out.println("Issue n°23: " + issue);
                    System.out.println("Thank you! Your issue has been recorded.");
                    break;

                case 4:
                    ConsoleUtils.clearScreen();
                    System.out.println("Disconnected from the system. See you soon!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Pause before reloading menu
            
        }
    }


}
