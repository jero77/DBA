package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dbc.DatabaseController;
import exceptions.OutOfQuotaException;

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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class HRGuyGUI extends JFrame {

	//GUI variables
	private JPanel contentPane;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfEmail;
	private JTextField tfTeam;
	private JLabel lblLeftValue;
	private JLabel lblSentValue;
	private JLabel lblReqValue;
	
	//Database variables
	private DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	
	//Personal information
	private String id, name, firstname, email;
	private int quotaleft = 0;
	private int invitationssent = 0;
	private JTextField tfRequest;
	
	
	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Test user has id test and password test (0 quota)
					HRGuyGUI frame = new HRGuyGUI(new DatabaseController(PATH_TO_DB), "test");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	

	/**
	 * Create the frame.
	 * @param dbc DatabaseController for access to the database
	 * @param guyID ID of the user of this interface (validated by login)
	 */
	public HRGuyGUI(DatabaseController dbc, String guyID) {

		//Database
		this.dbc = dbc;
		
		//Personal information
		this.id = guyID;
		try {
			loadPersonalInformation(id);
		} catch (SQLException e) {
			handleSQLException(e, false);
		}
		
		
		
		//Frame options
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();		//custom close
			}
		});
		setTitle("HRGuy Interface");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		
		//Tab 1
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
				//Get the textfields inputs: name, firstname, email, team
				String fields[] = new String[4];
				fields[1] = tfFirstName.getText();
				fields[0] = tfLastName.getText();
				fields[2] = tfEmail.getText();
				fields[3] = tfTeam.getText();
				
				//validate textfield inputs		
				if (!validInputs(fields)) {
					String error = "An error occured!\n"
						+ "Check if all the textfields contain valid information\n"
						+ "and try to invite again.";
					JOptionPane.showMessageDialog(null, error, 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else {	
					//sent an invitation
					
					try {
						sendInvitation(fields);
					} catch (SQLException e) {
						handleSQLException(e, true);
					} catch (OutOfQuotaException e) {
						String error = "An error occured!\n" + e.getLocalizedMessage();
						JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
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
		btnClear.setBounds(256, 225, 97, 25);
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
		
		lblSentValue = new JLabel(""+this.invitationssent);
		lblSentValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSentValue.setBounds(144, 51, 70, 25);
		rightPanel.add(lblSentValue);
		
		lblLeftValue = new JLabel(""+this.quotaleft);
		lblLeftValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLeftValue.setBounds(144, 95, 70, 25);
		rightPanel.add(lblLeftValue);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//get current amount of quota & update the label
				try {
					refresh();
				} catch (SQLException e) {
					handleSQLException(e, true);
				}
			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRefresh.setBounds(144, 194, 97, 25);
		rightPanel.add(btnRefresh);
		
		JLabel lblYourInvitations = new JLabel("Your Invitations & Quota:");
		lblYourInvitations.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblYourInvitations.setBounds(103, 13, 189, 25);
		rightPanel.add(lblYourInvitations);
		
		JLabel lblRequestQuota = new JLabel("Request Quota:");
		lblRequestQuota.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRequestQuota.setBounds(121, 265, 120, 25);
		rightPanel.add(lblRequestQuota);
		
		JLabel lblAdditionalQuota = new JLabel("Amount of Quota:");
		lblAdditionalQuota.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAdditionalQuota.setBounds(12, 303, 120, 25);
		rightPanel.add(lblAdditionalQuota);
		
		tfRequest = new JTextField();
		tfRequest.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfRequest.setColumns(10);
		tfRequest.setBounds(144, 303, 97, 25);
		rightPanel.add(tfRequest);
		
		JButton btnRequest = new JButton("Request");
		btnRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Request quota
				String text = tfRequest.getText();
				int amount = 0;
				String error = "Please enter a positive Integer in the Textfield!";
				String success = "Quota-Request was submitted successfully.";
				
				try {
					//amount must be parsable and also it must be bigger than 0
					amount = Integer.parseInt(text);
					if (amount <= 0) {
						JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						//amount is valid and amount > 0
						request(amount);
						JOptionPane.showMessageDialog(null, success);
					}
				} catch (NumberFormatException ex) {			
					JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
				} catch (SQLException ex) {
					handleSQLException(ex, true);
				}
				
				
			}
		});
		btnRequest.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRequest.setBounds(268, 303, 97, 25);
		rightPanel.add(btnRequest);
		
		JLabel lblQuotaRequested = new JLabel("Quota requested:");
		lblQuotaRequested.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblQuotaRequested.setBounds(12, 133, 120, 25);
		rightPanel.add(lblQuotaRequested);
		
		lblReqValue = new JLabel("0");
		lblReqValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblReqValue.setBounds(144, 133, 70, 25);
		rightPanel.add(lblReqValue);
		
		//Tab 2 TODO
		JPanel dataTab = new JPanel();
		tabbedPane.addTab("Personal Data", null, dataTab, "Manage your personal data");
		
		
		//Tab 3 TODO
		JPanel teamTab = new JPanel();
		tabbedPane.addTab("Team", null, teamTab, "Team statistics");
	}
	
	
	/**
	 * Creates a quota request in the database. Also updates the
	 * value on the GUI for the amount of quota requested in total.
	 * @param amount The amount of quota requested
	 * @throws SQLException 
	 */
	private void request(int amount) throws SQLException {
		String query = "INSERT INTO quotaRequest VALUES ('"
				+ this.id + "', " + amount + ");";
		dbc.executeUpdate(query);
		this.tfRequest.setText("");
		
		refresh();
	}


	/**
	 * Loads the personal information of the user with the given id.
	 * @param id ID of the user
	 */
	private void loadPersonalInformation(String id) throws SQLException{
		//Query DB
		String query = "SELECT * FROM hrguys WHERE id = ";
		query += "'" + id + "';";
		ResultSet res = dbc.execute(query);
		res.next();
		
		//Store information locally: name, firstname, email, quotaleft
		this.name = res.getString("name");
		this.firstname = res.getString("firstname");
		this.email = res.getString("emailaddress");
		this.quotaleft = res.getInt("quotaleft");
		
	}




	/**
	 * Send out an invitation to the candidate. An invitation can only be sent
	 * if the guy has quota left (>0)
	 * @param fields
	 * @throws SQLException 
	 * @throws OutOfQuotaException 
	 */
	private void sendInvitation(String[] fields) 
			throws SQLException, OutOfQuotaException  {
		
		//Check quota constraint
		refresh();
		if (this.quotaleft == 0) {	//can never be less than zero
			String message = "You have not enough quota at the moment.\n";
			message += "Request some more quota via the Quota-Tab";
			throw new OutOfQuotaException(message);
		}
		
		String from = this.email;
		String to = fields[2];
		String subject = "HRD Inc. - Survey Invitation";
		
		//body			first name		  last name
		String body = "Hello " + fields[1] + " " + fields[0] + ",\n"
				+ "I would like to invite you as potential new coworker.\n"
				+ "Therefor, you need to complete a survey. The survey can "
				+ "be accessed via this link: ";
		//TODO link
		
		
		//TODO display the invitation email approp.
		
		
		JOptionPane.showMessageDialog(this, from+" -> "+to+":\n"+subject+
				"\n"+body+"linklinklink");
		

		//Decrease this guy's quota by one
		this.quotaleft--;
		String query = "UPDATE hrguys SET quotaleft = "
			+ this.quotaleft + " WHERE id = '" + this.id + "';";
		dbc.executeUpdate(query);
		
		//Update label
		this.lblLeftValue.setText(""+this.quotaleft);
	}



	/**
	 * Check the current amount of quota left, invitations sent and 
	 * quota requested by this user & update the GUI afterwards.
	 * @throws SQLException 
	 */
	private void refresh() throws SQLException {
		//TODO invitations
		String query = "";
		
		//Quota left
		query = "SELECT quotaleft FROM hrguys WHERE id ="
			+ " '" + this.id + "';";
		ResultSet res = dbc.execute(query);
		res.next();
		this.quotaleft = res.getInt("quotaleft");
		this.lblLeftValue.setText(""+this.quotaleft);
		
		//Quota requested 
		query = "SELECT sum(amount) FROM quotaRequest WHERE "
				+ "hrguy = '" + this.id +"' GROUP BY hrguy;";
		res = dbc.execute(query);
		if (res.next())		//User has quota requested?
			this.lblReqValue.setText(""+res.getInt("sum(amount)"));
	}




	/**
	 * Validates the inputs of the text fields of the invite-form.
	 * Right now it only checks whether one of the fields is empty.
	 * @return Returns whether the inputs are valid or not
	 */
	private boolean validInputs(String[] fields) {
		//fields should be: name, firstname, email, team
		for (String str : fields) {
			if (str.equals(""))
				return false;
		}
		return true;
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
