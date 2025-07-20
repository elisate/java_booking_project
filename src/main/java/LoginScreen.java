import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginScreen extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Login - Book App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Buttons
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(loginBtn, gbc);
        gbc.gridx = 1;
        panel.add(registerBtn, gbc);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterScreen();
        });

        add(panel);
        setVisible(true);
    }

    private void login() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch full user info
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, name, email, role FROM users WHERE email = ? AND password = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                String role = rs.getString("role");

                User loggedInUser = new User(id, name, userEmail, role);

                JOptionPane.showMessageDialog(this, "Login successful as " + role);
                dispose();

                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboard();
                } else {
                    new UserDashboard(loggedInUser);  // Pass user to dashboard!
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // For quick testing
    public static void main(String[] args) {
        new LoginScreen();
    }
}
