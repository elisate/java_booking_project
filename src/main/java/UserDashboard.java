import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {
    private User user;  // Assume you have a User class holding id, name, email, role
    private JTable booksTable;
    private DefaultTableModel tableModel;

    public UserDashboard(User user) {
        this.user = user;

        setTitle("User Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Panel to display logged-in user info
        JPanel userInfoPanel = new JPanel(new GridLayout(3, 1));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Logged-in User Info"));
        userInfoPanel.add(new JLabel("Name: " + user.getName()));
        userInfoPanel.add(new JLabel("Email: " + user.getEmail()));
        userInfoPanel.add(new JLabel("Role: " + user.getRole()));

        add(userInfoPanel, BorderLayout.NORTH);

        // Table for books
        tableModel = new DefaultTableModel();
        booksTable = new JTable(tableModel);
        tableModel.setColumnIdentifiers(new String[] { "ID", "Title", "Author", "Price", "Category" });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Books"));

        add(scrollPane, BorderLayout.CENTER);

        // Load books from DB and show in table
        loadBooks();

        setVisible(true);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);  // Clear existing data

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:BookApp.db")) {
            String sql = "SELECT b.id, b.title, b.author, b.price, c.name AS category_name " +
                    "FROM books b LEFT JOIN categories c ON b.category_id = c.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getString("category_name")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }
}
