package bca;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class ContactMS extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtHi;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContactMS frame = new ContactMS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ContactMS() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtHi = new JTextField();
		txtHi.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtHi.setText("CONTACT MANAGEMENT SYSTEM");
		txtHi.setBounds(104, 29, 257, 23);
		contentPane.add(txtHi);
		txtHi.setColumns(10);

		// ADD CONTACT
		JButton btnAdd = new JButton("Add Contact");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Insert insert = new Insert();
				insert.setVisible(true);
				dispose();
			}
		});

		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAdd.setBounds(168, 75, 120, 29);
		contentPane.add(btnAdd);

		// VIEW CONTACTS
		JButton btnView = new JButton("View Contacts");
		btnView.setFont(new Font("Tahoma", Font.BOLD, 11));

		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Display display = new Display();
				display.setVisible(true);
				dispose();
			}
		});

		btnView.setBounds(168, 120, 120, 29);
		contentPane.add(btnView);

		// UPDATE CONTACT
		JButton btnUpdate = new JButton("Update Contact");
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 11));

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Update update = new Update();
				update.setVisible(true);
				dispose();
			}
		});

		btnUpdate.setBounds(168, 165, 120, 29);
		contentPane.add(btnUpdate);

		// DELETE CONTACT
		JButton btnDelete = new JButton("Delete Contact");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Delete delete = new Delete();
				delete.setVisible(true);
				dispose();
			}
		});

		btnDelete.setBounds(168, 210, 120, 29);
		contentPane.add(btnDelete);

		// EXIT
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});

		btnExit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnExit.setBounds(168, 255, 120, 29);
		contentPane.add(btnExit);
	}
}