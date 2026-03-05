package csci2040u.bytecouncil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginFrame extends JFrame {
    
    public loginFrame() {
        // Set window title
        setTitle("Login");
        
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set window size
        setSize(400, 200);
        
        // Center window on screen
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        
        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(usernameLabel, constraints);
        
        // Username field
        JTextField usernameField = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, constraints);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(passwordLabel, constraints);
        
        // Password field
        JPasswordField passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, constraints);
        
        // Login button
        JButton loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20, 10, 10, 10);
        panel.add(loginButton, constraints);
        
        // Action listener to login button (does nothing rn but will bring up main window)
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Button does nothing when pressed
            }
        });
        
        // Add panel to frame
        add(panel);
        
        // Make window visible
        setVisible(true);
    }
    
    public static void main(String[] args) {
        // Create and display the login window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new loginFrame();
            }
        });
    }
}