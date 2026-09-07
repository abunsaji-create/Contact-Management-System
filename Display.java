package bca;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Display extends JFrame {

    private JTable table;

    private final String url = "jdbc:oracle:thin:@localhost:1521/FREE";
    private final String username = "SYSTEM";
    private final String password = "Abun";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Display d = new Display();
            d.setVisible(true);
        });
    }

    public Display() {
        setTitle("View Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 370);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 245, 250));
        setContentPane(panel);

        JLabel title = new JLabel("View Data");
        title.setFont(new Font("Tahoma", Font.BOLD, 16));
        title.setForeground(new Color(25, 55, 90));
        title.setBounds(285, 15, 100, 25);
        panel.add(title);

        JButton contacts = new JButton("Contacts table");
        JButton groups = new JButton("Groups table");
        JButton phones = new JButton("Phones table");
        JButton back = new JButton("Back");

        JButton[] buttons = {contacts, groups, phones, back};

        int y = 75;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(25, y, 115, 25);
            buttons[i].setFocusPainted(false);
            panel.add(buttons[i]);
            y += (i == 2) ? 65 : 45;
        }

        table = new JTable();
        table.setRowHeight(20);
        table.setFont(new Font("Tahoma", Font.PLAIN, 11));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(160, 65, 460, 210);
        panel.add(scroll);

        contacts.addActionListener(e ->
            loadTable(
                "SELECT CONTACT_ID, NAME, EMAIL, ADDRESS, GROUP_ID " +
                "FROM CONTACTS ORDER BY CONTACT_ID",
                new String[]{"CONTACT ID", "NAME", "EMAIL", "ADDRESS", "GROUP ID"}
            )
        );

        groups.addActionListener(e ->
            loadTable(
                "SELECT GROUP_ID, GROUP_NAME, DESCRIPTION " +
                "FROM CONTACT_GROUPS ORDER BY GROUP_ID",
                new String[]{"GROUP ID", "GROUP NAME", "DESCRIPTION"}
            )
        );

        phones.addActionListener(e ->
            loadTable(
                "SELECT PHONE_ID, CONTACT_ID, PHONE_NUMBER, PHONE_TYPE " +
                "FROM CONTACT_PHONES ORDER BY PHONE_ID",
                new String[]{"PHONE ID", "CONTACT ID", "PHONE NUMBER", "PHONE TYPE"}
            )
        );

        back.addActionListener(e -> {
            new ContactMS().setVisible(true);
            dispose();
        });

        loadTable(
            "SELECT CONTACT_ID, NAME, EMAIL, ADDRESS, GROUP_ID " +
            "FROM CONTACTS ORDER BY CONTACT_ID",
            new String[]{"CONTACT ID", "NAME", "EMAIL", "ADDRESS", "GROUP ID"}
        );
    }

    private void loadTable(String sql, String[] columns) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                url, username, password
            );

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                private static final long serialVersionUID = 1L;

                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[count];

                for (int i = 0; i < count; i++)
                    row[i] = rs.getObject(i + 1);

                model.addRow(row);
            }

            table.setModel(model);

            rs.close();
            pst.close();
            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}