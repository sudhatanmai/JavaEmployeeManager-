import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeDBApp {

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeDB"; // Change DB name if needed
    static final String USER = "root"; // Your MySQL username
    static final String PASS = "root"; // Your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("✅ Connected to database!");

            while (true) {
                System.out.println("\n==== Employee Database Menu ====");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter position: ");
                        String position = sc.nextLine();
                        System.out.print("Enter salary: ");
                        double salary = sc.nextDouble();
                        addEmployee(conn, name, position, salary);
                        break;

                    case 2:
                        viewEmployees(conn);
                        break;

                    case 3:
                        System.out.println("👋 Exiting...");
                        return;

                    default:
                        System.out.println("❌ Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection conn, String name, String position, double salary) throws SQLException {
        String sql = "INSERT INTO Employees (name, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            System.out.println("✅ Employee added successfully!");
        }
    }

    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Employees";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Position: %s | Salary: %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary"));
            }
        }
    }
}
