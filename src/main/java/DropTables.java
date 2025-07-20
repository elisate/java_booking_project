import java.sql.Connection;
import java.sql.Statement;

public class DropTables {
    public static void drop() {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {

            // Drop in this order to avoid FK constraint issues
            stmt.execute("DROP TABLE IF EXISTS books;");
            stmt.execute("DROP TABLE IF EXISTS categories;");
            stmt.execute("DROP TABLE IF EXISTS users;");

            System.out.println("✅ All tables dropped successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error dropping tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: Main method to run directly
    public static void main(String[] args) {
        drop();
    }
}
