import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private JTextArea outputArea;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI Components
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton btnViewUsers = new JButton("View All Users");
        JButton btnAddBook = new JButton("Add New Book");
        JButton btnViewBooks = new JButton("View All Books");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnViewUsers);
        buttonPanel.add(btnAddBook);
        buttonPanel.add(btnViewBooks);

        // Add listeners
        btnViewUsers.addActionListener(e -> showAllUsers());
        btnAddBook.addActionListener(e -> openAddBookForm());
        btnViewBooks.addActionListener(e -> showAllBooks());

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void showAllUsers() {
        outputArea.setText(""); // Clear previous
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:BookApp.db")) {
            String query = "SELECT id, name, email, role FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                outputArea.append(
                        "ID: " + rs.getInt("id") +
                                ", Name: " + rs.getString("name") +
                                ", Email: " + rs.getString("email") +
                                ", Role: " + rs.getString("role") + "\n"
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching users: " + e.getMessage());
        }
    }

    private void openAddBookForm() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Author:");
        String categoryName = JOptionPane.showInputDialog(this, "Enter Category Name:");

        if (title != null && author != null && categoryName != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:BookApp.db")) {
                conn.setAutoCommit(false); // start transaction

                int categoryId;
                String selectSql = "SELECT id FROM categories WHERE name = ?";
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setString(1, categoryName);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        categoryId = rs.getInt("id");
                    } else {
                        // Insert category
                        try (PreparedStatement insCat = conn.prepareStatement(
                                "INSERT INTO categories (name) VALUES (?)")) {
                            insCat.setString(1, categoryName);
                            insCat.executeUpdate();
                        }
                        // Get last inserted category ID
                        try (Statement stmt = conn.createStatement();
                             ResultSet keyRs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                            if (keyRs.next()) {
                                categoryId = keyRs.getInt(1);
                            } else {
                                throw new SQLException("Failed to retrieve category ID.");
                            }
                        }
                    }
                }

                // Insert book
                String insBook = "INSERT INTO books (title, author, price, category_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement bookStmt = conn.prepareStatement(insBook)) {
                    bookStmt.setString(1, title);
                    bookStmt.setString(2, author);
                    bookStmt.setDouble(3, 10.0); // default price
                    bookStmt.setInt(4, categoryId);
                    bookStmt.executeUpdate();
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error adding book: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAllBooks() {
        outputArea.setText("");
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:BookApp.db")) {
            String query = "SELECT b.id, b.title, b.author, b.price, c.name AS category_name " +
                    "FROM books b LEFT JOIN categories c ON b.category_id = c.id";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                outputArea.append(
                        "ID: " + rs.getInt("id") +
                                ", Title: " + rs.getString("title") +
                                ", Author: " + rs.getString("author") +
                                ", Price: " + rs.getDouble("price") +
                                ", Category: " + rs.getString("category_name") + "\n"
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error fetching books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
