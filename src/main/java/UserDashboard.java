import javax.swing.*;

public class UserDashboard extends JFrame {
    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Welcome User! You can view books here."));
        setVisible(true);
    }
}
