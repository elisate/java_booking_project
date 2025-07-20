import java.sql.Connection;
import java.sql.Statement;

public class CreateTables {
    public static void create() {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {

//            // Drop tables if they exist (for resetting)
//            stmt.execute("DROP TABLE IF EXISTS books;");
//            stmt.execute("DROP TABLE IF EXISTS categories;");
//            stmt.execute("DROP TABLE IF EXISTS users;");

            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL CHECK (role IN ('admin', 'user')));");

            // Create categories table
            stmt.execute("CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE);");

            // Create books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "price REAL NOT NULL," +
                    "category_id INTEGER," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id));");

            System.out.println("Tables created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
