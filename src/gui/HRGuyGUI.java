package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dbc.DatabaseController;

import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class HRGuyGUI extends JFrame {

	private JPanel contentPane;
	
	
	//Database variables
	private DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfEmail;
	private JTextField tfTeam;

	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HRGuyGUI frame = new HRGuyGUI(new DatabaseController(PATH_TO_DB));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	

	/**
	 * Create the frame, gets a db-controller from the login.
	 */
	public HRGuyGUI(DatabaseController dbc) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
		setTitle("HRGuy Interface");
		
		this.dbc = dbc;
		
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel invitationsTab = new JPanel();
		invitationsTab.setToolTipText("");
		tabbedPane.addTab("Invitations", null, invitationsTab, "Form to invite a survey candidate and overview about your invitations");
		invitationsTab.setLayout(null);
		
		JPanel invitePanel = new JPanel();
		invitePanel.setBounds(12, 13, 365, 487);
		invitationsTab.add(invitePanel);
		invitePanel.setLayout(null);
		
		JLabel lblInviteACandidate = new JLabel("Invite a candidate:");
		lblInviteACandidate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblInviteACandidate.setBounds(117, 13, 150, 25);
		invitePanel.add(lblInviteACandidate);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFirstName.setBounds(12, 55, 100, 25);
		invitePanel.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLastName.setBounds(12, 93, 100, 25);
		invitePanel.add(lblLastName);
		
		JLabel lblEmailAddress = new JLabel("Email Address:");
		lblEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEmailAddress.setBounds(12, 131, 100, 25);
		invitePanel.add(lblEmailAddress);
		
		JLabel lblTeamName = new JLabel("Team Name:");
		lblTeamName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTeamName.setBounds(12, 169, 100, 25);
		invitePanel.add(lblTeamName);
		
		tfFirstName = new JTextField();
		tfFirstName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfFirstName.setBounds(127, 57, 226, 25);
		invitePanel.add(tfFirstName);
		tfFirstName.setColumns(10);
		
		tfLastName = new JTextField();
		tfLastName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfLastName.setColumns(10);
		tfLastName.setBounds(127, 95, 226, 25);
		invitePanel.add(tfLastName);
		
		tfEmail = new JTextField();
		tfEmail.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfEmail.setColumns(10);
		tfEmail.setBounds(127, 131, 226, 25);
		invitePanel.add(tfEmail);
		
		tfTeam = new JTextField();
		tfTeam.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfTeam.setColumns(10);
		tfTeam.setBounds(127, 169, 226, 25);
		invitePanel.add(tfTeam);
		
		JButton btnInvite = new JButton("Invite");
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Get the textfields inputs
				String fields[] = new String[4];
				fields[1] = tfFirstName.getText();
				fields[0] = tfLastName.getText();
				fields[2] = tfEmail.getText();
				fields[3] = tfTeam.getText();
				
				//validate textfield inputs		
				/*if (!validInputs(fields)) {
					String error = "An error occured!\n";
					error += "Check if all the textfields contain valid information\n";
					error += "and try to add again.";
					JOptionPane.showMessageDialog(null, error);
				}
				else {	
					
					try {
						//addHRGuy(fields);
						String success = "A new HR guy was added succesfully!";
						JOptionPane.showMessageDialog(null, success);
					} catch (SQLException e) {
						handleSQLException(e, true);
					} 
				}*/
			}
		});
		btnInvite.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnInvite.setBounds(141, 225, 97, 25);
		invitePanel.add(btnInvite);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Clear all text fields
				tfFirstName.setText("");
				tfLastName.setText("");
				tfEmail.setText("");
				tfTeam.setText("");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnClear.setBounds(256, 226, 97, 25);
		invitePanel.add(btnClear);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBounds(390, 13, 365, 487);
		invitationsTab.add(rightPanel);
		rightPanel.setLayout(null);
		
		JLabel lblInvitationsSent = new JLabel("Invitations sent:");
		lblInvitationsSent.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblInvitationsSent.setBounds(12, 51, 120, 25);
		rightPanel.add(lblInvitationsSent);
		
		JLabel lblQuotaLeft = new JLabel("Quota left:");
		lblQuotaLeft.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuotaLeft.setBounds(12, 95, 120, 25);
		rightPanel.add(lblQuotaLeft);
		
		JLabel lblSentValue = new JLabel("");
		lblSentValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSentValue.setBounds(144, 13, 70, 25);
		rightPanel.add(lblSentValue);
		
		JLabel lblLeftValue = new JLabel("");
		lblLeftValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLeftValue.setBounds(144, 51, 70, 25);
		rightPanel.add(lblLeftValue);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExit.setBounds(268, 462, 97, 25);
		rightPanel.add(btnExit);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRefresh.setBounds(159, 462, 97, 25);
		rightPanel.add(btnRefresh);
		
		JLabel lblYourInvitations = new JLabel("Your Invitations & Quota:");
		lblYourInvitations.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblYourInvitations.setBounds(103, 13, 189, 25);
		rightPanel.add(lblYourInvitations);
		
		JPanel quotaTab = new JPanel();
		tabbedPane.addTab("Quota", null, quotaTab, "Manage your quota");
		tabbedPane.setEnabledAt(1, true);
		
		JPanel dataTab = new JPanel();
		tabbedPane.addTab("Personal Data", null, dataTab, "Manage your personal data");
		
		JPanel teamTab = new JPanel();
		tabbedPane.addTab("Team", null, teamTab, "Team statistics");
	}
	
	
	/**
	 * Handles any SQLException with some error output and
	 * also a message dialog for the user
	 * @param e The exception to handle
	 * @param showDialog Determines if a dialog is shown or not
	 */
	private void handleSQLException(SQLException e, boolean showDialog) {
		System.err.println(e.getErrorCode());
		System.err.println(e.getSQLState());
		System.err.println(e.getMessage());
		
		//show an error message
		if (showDialog)
			JOptionPane.showMessageDialog(this, "An error occured!\n"+e.getMessage());
	}
	
	
	/**
	 * Exits the HRGuy-GUI in a safe way after confirmed by the user via
	 * a closing dialog
	 */
	private void exit() {

		//Exit dialog
		int confirm = JOptionPane.showOptionDialog(null,
				"Are you sure to close the application?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
	             null, null, null);
		
		//Do not close yet
		if (confirm == JOptionPane.NO_OPTION)
			return;
		
		
		//Close database connection
		try {
			dbc.close();
		} catch (SQLException e) {
			handleSQLException(e, false);
			System.exit(-1);
		}
		
		//Exit frame
		this.dispose();
	}
}
