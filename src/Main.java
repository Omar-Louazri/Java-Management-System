import com.packages.ConsoleUtils;
import com.packages.LoginSystem.LoginSystem;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        callMenu(); // Start the menu
    }

    static void callMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) { // Loop to keep showing the menu
            // Clear console (fixes "cls" issue)
            ConsoleUtils.clearScreen();
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1.Login");
            System.out.println("2.Register");
            System.out.println("3.Report an issue");
            System.out.println("4.Exit");
            System.out.print("Choose an option: ");

            // Validate input as an integer
            if (!scanner.hasNextInt()) {
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
                    ConsoleUtils.clearScreen();
                    System.out.println("Register a new user:");
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your grade: ");
                    String grade = scanner.nextLine();
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
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Pause before reloading menu
        }
    }


}
