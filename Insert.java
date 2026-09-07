package bca;

import java.awt.EventQueue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Insert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JTextField txtPhone;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Insert frame = new Insert();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Insert() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(" ADD CONTACT");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(168, 0, 117, 32);
		contentPane.add(lblNewLabel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setBounds(52, 47, 45, 14);
		contentPane.add(lblName);
		
		txtName = new JTextField();
		txtName.setBounds(107, 44, 86, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEmail.setBounds(52, 105, 45, 14);
		contentPane.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(107, 102, 86, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAddress.setBounds(52, 163, 61, 14);
		contentPane.add(lblAddress);
		
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGroup.setBounds(239, 47, 46, 14);
		contentPane.add(lblGroup);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(107, 160, 86, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		JComboBox cmbGroup = new JComboBox();
		cmbGroup.setModel(new DefaultComboBoxModel(new String[] {"Family", "Friends", "College", "Work"}));
		cmbGroup.setBounds(311, 43, 76, 22);
		contentPane.add(cmbGroup);
		
		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPhone.setBounds(239, 105, 46, 14);
		contentPane.add(lblPhone);
		
		txtPhone = new JTextField();
		txtPhone.setBounds(311, 102, 86, 20);
		contentPane.add(txtPhone);
		txtPhone.setColumns(10);
		
		JLabel lblPhoneType = new JLabel("Phone Type:");
		lblPhoneType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPhoneType.setBounds(239, 163, 86, 14);
		contentPane.add(lblPhoneType);
		
		JComboBox cmbPhoneType = new JComboBox();
		cmbPhoneType.setModel(new DefaultComboBoxModel(new String[] {"Personal", "WhatsApp", "Work", "Other"}));
		cmbPhoneType.setBounds(311, 159, 86, 22);
		contentPane.add(cmbPhoneType);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        String name = txtName.getText();
		        String email = txtEmail.getText();
		        String address = txtAddress.getText();
		        String group = cmbGroup.getSelectedItem().toString();
		        String phone = txtPhone.getText();
		        String phoneType = cmbPhoneType.getSelectedItem().toString();

		        if (name.isEmpty() || phone.isEmpty()) {
		            JOptionPane.showMessageDialog(null,
		                    "Name and Phone are required!");
		            return;
		        }

		        String url = "jdbc:oracle:thin:@localhost:1521/FREE";
		        String username = "SYSTEM";
		        String password = "Abun";

		        Connection con = null;

		        try {
		            con = java.sql.DriverManager.getConnection(url, username, password);

		            // Get GROUP_ID from GROUP_NAME
		            String groupSQL =
		                    "SELECT GROUP_ID FROM CONTACT_GROUPS WHERE GROUP_NAME = ?";

		            PreparedStatement groupStmt = con.prepareStatement(groupSQL);
		            groupStmt.setString(1, group);

		            ResultSet rs = groupStmt.executeQuery();

		            int groupId = 0;

		            if (rs.next()) {
		                groupId = rs.getInt("GROUP_ID");
		            }

		            rs.close();
		            groupStmt.close();

		            // Generate new CONTACT_ID
		            String contactIdSQL =
		                    "SELECT NVL(MAX(CONTACT_ID), 0) + 1 FROM CONTACTS";

		            PreparedStatement idStmt = con.prepareStatement(contactIdSQL);
		            ResultSet idRs = idStmt.executeQuery();

		            int contactId = 1;

		            if (idRs.next()) {
		                contactId = idRs.getInt(1);
		            }

		            idRs.close();
		            idStmt.close();

		            // Insert into CONTACTS
		            String contactSQL =
		                    "INSERT INTO CONTACTS " +
		                    "(CONTACT_ID, NAME, EMAIL, ADDRESS, GROUP_ID) " +
		                    "VALUES (?, ?, ?, ?, ?)";

		            PreparedStatement contactStmt =
		                    con.prepareStatement(contactSQL);

		            contactStmt.setInt(1, contactId);
		            contactStmt.setString(2, name);
		            contactStmt.setString(3, email);
		            contactStmt.setString(4, address);
		            contactStmt.setInt(5, groupId);

		            contactStmt.executeUpdate();
		            contactStmt.close();

		            // Generate new PHONE_ID
		            String phoneIdSQL =
		                    "SELECT NVL(MAX(PHONE_ID), 0) + 1 FROM CONTACT_PHONES";

		            PreparedStatement phoneIdStmt =
		                    con.prepareStatement(phoneIdSQL);

		            ResultSet phoneIdRs = phoneIdStmt.executeQuery();

		            int phoneId = 1;

		            if (phoneIdRs.next()) {
		                phoneId = phoneIdRs.getInt(1);
		            }

		            phoneIdRs.close();
		            phoneIdStmt.close();

		            // Insert into CONTACT_PHONES
		            String phoneSQL =
		                    "INSERT INTO CONTACT_PHONES " +
		                    "(PHONE_ID, CONTACT_ID, PHONE_NUMBER, PHONE_TYPE) " +
		                    "VALUES (?, ?, ?, ?)";

		            PreparedStatement phoneStmt =
		                    con.prepareStatement(phoneSQL);

		            phoneStmt.setInt(1, phoneId);
		            phoneStmt.setInt(2, contactId);
		            phoneStmt.setString(3, phone);
		            phoneStmt.setString(4, phoneType);

		            phoneStmt.executeUpdate();
		            phoneStmt.close();

		            JOptionPane.showMessageDialog(null,
		                    "Contact Added Successfully!");

		            // Clear fields
		            txtName.setText("");
		            txtEmail.setText("");
		            txtAddress.setText("");
		            txtPhone.setText("");

		            con.close();

		        } catch (SQLException ex) {
		            JOptionPane.showMessageDialog(null,
		                    "Database Error: " + ex.getMessage());
		        }
		    }
		});
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAdd.setBounds(176, 191, 89, 23);
		contentPane.add(btnAdd);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ContactMS home = new ContactMS();
				home.setVisible(true);
				dispose();
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBack.setBounds(176, 227, 89, 23);
		contentPane.add(btnBack);

	}
}
