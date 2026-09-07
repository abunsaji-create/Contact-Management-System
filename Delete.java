package bca;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtId;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Delete frame = new Delete();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Delete() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("DELETE CONTACT");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitle.setBounds(175, 30, 160, 25);
		contentPane.add(lblTitle);

		JLabel lblId = new JLabel("Contact ID");
		lblId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblId.setBounds(80, 90, 100, 25);
		contentPane.add(lblId);

		txtId = new JTextField();
		txtId.setBounds(180, 90, 200, 25);
		contentPane.add(txtId);
		txtId.setColumns(10);

		JButton btnDelete = new JButton("DELETE");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDelete.setBounds(140, 160, 100, 30);
		contentPane.add(btnDelete);

		JButton btnBack = new JButton("BACK");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBack.setBounds(260, 160, 100, 30);
		contentPane.add(btnBack);

		// DELETE BUTTON
		btnDelete.addActionListener(e -> deleteContact());

		// BACK BUTTON
		btnBack.addActionListener(e -> {
			ContactMS home = new ContactMS();
			home.setVisible(true);
			dispose();
		});
	}

	private void deleteContact() {

		if (txtId.getText().trim().isEmpty()) {

			JOptionPane.showMessageDialog(null,
					"Please enter Contact ID!");
			return;
		}

		try {

			int id = Integer.parseInt(txtId.getText().trim());

			int choice = JOptionPane.showConfirmDialog(
					null,
					"Are you sure you want to delete Contact ID "
							+ id + "?",
					"Confirm Delete",
					JOptionPane.YES_NO_OPTION);

			if (choice != JOptionPane.YES_OPTION) {
				return;
			}

			// Oracle connection
			String url = "jdbc:oracle:thin:@localhost:1521:FREE";
			String user = "SYSTEM";
			String password = "Abun";

			Connection con =
					DriverManager.getConnection(url, user, password);

			// Disable auto commit
			con.setAutoCommit(false);

			// Delete phone record first
			String phoneSql =
					"DELETE FROM CONTACT_PHONES WHERE CONTACT_ID=?";

			PreparedStatement ps1 =
					con.prepareStatement(phoneSql);

			ps1.setInt(1, id);
			ps1.executeUpdate();

			// Delete contact record
			String contactSql =
					"DELETE FROM CONTACTS WHERE CONTACT_ID=?";

			PreparedStatement ps2 =
					con.prepareStatement(contactSql);

			ps2.setInt(1, id);

			int result = ps2.executeUpdate();

			// Save changes
			con.commit();

			if (result > 0) {

				JOptionPane.showMessageDialog(null,
						"Contact Deleted Successfully!");

				txtId.setText("");

			} else {

				JOptionPane.showMessageDialog(null,
						"Contact ID not found!");
			}

			ps1.close();
			ps2.close();
			con.close();

		} catch (NumberFormatException ex) {

			JOptionPane.showMessageDialog(null,
					"Contact ID must be a number!");

		} catch (SQLException ex) {

			JOptionPane.showMessageDialog(null,
					"Database Error: " + ex.getMessage());
		}
	}
}