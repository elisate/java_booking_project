import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterScreen extends JFrame {
    JTextField nameField, emailField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;

    public RegisterScreen() {
        setTitle("Register - Book App");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleBox = new JComboBox<>(new String[]{"user", "admin"});
        panel.add(roleBox, gbc);

        // Register Button
        JButton registerBtn = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> register());

        add(panel);
        setVisible(true);
    }

    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
            new LoginScreen();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Email already used or error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
