package bca;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;

public class Update extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField txtId, txtName, txtEmail, txtAddress, txtPhone;
    JComboBox<String> cmbGroup, cmbPhoneType;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Update frame = new Update();
            frame.setVisible(true);
        });
    }

    public Update() {

        setTitle("UPDATE CONTACT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        getContentPane().setLayout(null);

        addLabel("Contact ID", 50, 60);
        txtId = addText(150, 60);

        addLabel("Name", 50, 100);
        txtName = addText(150, 100);

        addLabel("Email", 50, 140);
        txtEmail = addText(150, 140);

        addLabel("Address", 50, 180);
        txtAddress = addText(150, 180);

        addLabel("Group", 50, 220);
        cmbGroup = new JComboBox<>(
                new String[]{"Family", "Friends", "College", "Work"});
        cmbGroup.setBounds(150, 220, 250, 25);
        add(cmbGroup);

        addLabel("Phone", 50, 260);
        txtPhone = addText(150, 260);

        addLabel("Phone Type", 50, 300);
        cmbPhoneType = new JComboBox<>(
                new String[]{"Personal", "WhatsApp", "Work", "Other"});
        cmbPhoneType.setBounds(150, 300, 250, 25);
        add(cmbPhoneType);

        JButton update = new JButton("UPDATE");
        update.setBounds(150, 350, 100, 30);
        add(update);

        JButton back = new JButton("BACK");
        back.setBounds(270, 350, 100, 30);
        add(back);

        update.addActionListener(e -> updateContact());

        back.addActionListener(e -> {
            new ContactMS().setVisible(true);
            dispose();
        });
    }

    void addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 100, 25);
        add(l);
    }

    JTextField addText(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 250, 25);
        add(t);
        return t;
    }

    void updateContact() {

        if (txtId.getText().isEmpty() ||
            txtName.getText().isEmpty() ||
            txtPhone.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "ID, Name and Phone are required!");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:FREE",
                    "SYSTEM", "Abun");

            con.setAutoCommit(false);

            String g = "SELECT GROUP_ID FROM CONTACT_GROUPS WHERE GROUP_NAME=?";
            PreparedStatement p1 = con.prepareStatement(g);
            p1.setString(1, cmbGroup.getSelectedItem().toString());

            ResultSet r = p1.executeQuery();

            if (!r.next()) {
                JOptionPane.showMessageDialog(this, "Group not found!");
                con.close();
                return;
            }

            int groupId = r.getInt(1);

            String sql = "UPDATE CONTACTS SET NAME=?, EMAIL=?, " +
                         "ADDRESS=?, GROUP_ID=? WHERE CONTACT_ID=?";

            PreparedStatement p2 = con.prepareStatement(sql);
            p2.setString(1, txtName.getText());
            p2.setString(2, txtEmail.getText());
            p2.setString(3, txtAddress.getText());
            p2.setInt(4, groupId);
            p2.setInt(5, id);

            int result = p2.executeUpdate();

            String phone = "UPDATE CONTACT_PHONES SET PHONE_NUMBER=?, " +
                           "PHONE_TYPE=? WHERE CONTACT_ID=?";

            PreparedStatement p3 = con.prepareStatement(phone);
            p3.setString(1, txtPhone.getText());
            p3.setString(2, cmbPhoneType.getSelectedItem().toString());
            p3.setInt(3, id);
            p3.executeUpdate();

            con.commit();

            JOptionPane.showMessageDialog(this,
                    result > 0 ? "Updated Successfully!" :
                    "Contact ID not found!");

            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a number!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage());
        }
    }
}