package bca;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField uname;
    private JTextField pass;

    private final String url =
            "jdbc:oracle:thin:@localhost:1521/FREE";
    private final String dbUser = "SYSTEM";
    private final String dbPassword = "Abun";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    login frame = new login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public login() {

        setTitle("Login - Contact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 320);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 245, 250));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("CONTACT MANAGEMENT SYSTEM");
        title.setFont(new Font("Tahoma", Font.BOLD, 17));
        title.setForeground(new Color(25, 55, 90));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(65, 30, 320, 30);
        contentPane.add(title);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblUsername.setBounds(95, 90, 90, 25);
        contentPane.add(lblUsername);

        uname = new JTextField();
        uname.setBounds(190, 90, 150, 25);
        contentPane.add(uname);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblPassword.setBounds(95, 130, 90, 25);
        contentPane.add(lblPassword);

        // Normal text field so password can be seen
        pass = new JTextField();
        pass.setBounds(190, 130, 150, 25);
        contentPane.add(pass);

        // Normal button - no custom color
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnLogin.setBounds(175, 180, 100, 30);
        contentPane.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String u = uname.getText().trim();
                String p = pass.getText();

                if (u.isEmpty() || p.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter username and password"
                    );
                    return;
                }

                String sql =
                        "SELECT * FROM LOGIN " +
                        "WHERE USERNAME=? AND PASSWORD=?";

                try {
                    Class.forName(
                            "oracle.jdbc.driver.OracleDriver"
                    );

                    Connection con =
                            DriverManager.getConnection(
                                    url,
                                    dbUser,
                                    dbPassword
                            );

                    PreparedStatement pst =
                            con.prepareStatement(sql);

                    pst.setString(1, u);
                    pst.setString(2, p);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {

                        JOptionPane.showMessageDialog(
                                null,
                                "Login Successful"
                        );

                        ContactMS home = new ContactMS();
                        home.setVisible(true);
                        dispose();

                    } else {

                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid username or password"
                        );

                        pass.setText("");
                    }

                    rs.close();
                    pst.close();
                    con.close();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Database Error: " + ex.getMessage()
                    );

                    ex.printStackTrace();
                }
            }
        });

        getRootPane().setDefaultButton(btnLogin);
    }
}