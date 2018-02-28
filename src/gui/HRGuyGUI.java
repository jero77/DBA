package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbc.DatabaseController;
import exceptions.OutOfQuotaException;


/**
 * This class provides a graphical user interface for the HR guys.
 * The following functionality is implemented:
 * 		- Invite survey candidates (send out invitation with link)
 * 		- Show amount of quota (requested and left)
 * 		- Show amount of invitations sent
 * 		- Request more Quota from admin
 * 		- Manage personal information (change data)
 * 		- List HR Guy's Teams
 * 		- Link to Team statistics
 * @author Jero
 *
 */
public class HRGuyGUI extends JFrame {

	//auto generated
	private static final long serialVersionUID = 4477612229249482358L;
	
	
	/**
	 * Nested class for JList-Elements.
	 * @author Jero
	 *
	 */
	class TeamStatistic {
		//Class variables (database columns)
		int id;
		String name;
		int members;
		//Constructor
		TeamStatistic (int id, String name, int members) {
			this.id = id;
			this.name = name;
			this.members = members;
		}

		public String toString() {
			return "Team " + name + " has " + members + " members";
		}
	}
	
	
	
	//GUI variables
	private JPanel contentPane;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfEmail;
	private JTextField tfTeam;
	private JLabel lblLeftValue, lblSentValue, lblReqValue;
	private JLabel showFirstname, showLastname, showCompany, showEmail;
	
	//Survey
	private static final String PATH_TO_SURVEY = "C:/Users/Jero/git/DBA/html/survey.html";
	
	//Database variables
	private DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	
	//Personal information
	private String id, name, firstname, company, email;
	private int quotaleft = 0;
	private int invitationssent = 0;
	private JTextField tfRequest;
	private JList<TeamStatistic> list;
		
	
	
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
		//Personal identifier
		this.id = guyID;

		
		
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
		
		
		//Tab 1 - Invitations and Quota
		JPanel invitationsTab = new JPanel();
		invitationsTab.setToolTipText("Send an invitation, view your invitations and quota, or request more quota.");
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
		
		lblSentValue = new JLabel("");
		lblSentValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSentValue.setBounds(144, 51, 70, 25);
		rightPanel.add(lblSentValue);
		
		lblLeftValue = new JLabel("");
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
		
		
		//Tab 2 - Manage Personal Data
		JPanel dataTab = new JPanel();
		dataTab.setToolTipText("View and manage your personal data.");
		tabbedPane.addTab("Personal Data", null, dataTab, "Manage your personal data");
		dataTab.setLayout(null);
		
		JLabel lblYourPersonalData = new JLabel("Your Personal Data:");
		lblYourPersonalData.setBounds(155, 13, 125, 25);
		dataTab.add(lblYourPersonalData);
		
		JLabel lblDataFirstname = new JLabel("Firstname:");
		lblDataFirstname.setBounds(12, 51, 62, 25);
		dataTab.add(lblDataFirstname);
		
		JLabel lblDataLastname = new JLabel("Lastname:");
		lblDataLastname.setBounds(12, 89, 62, 25);
		dataTab.add(lblDataLastname);
		
		JLabel lblCompany = new JLabel("Company:");
		lblCompany.setBounds(12, 127, 62, 25);
		dataTab.add(lblCompany);
		
		JLabel lblEmailaddress = new JLabel("Emailaddress:");
		lblEmailaddress.setBounds(12, 165, 81, 25);
		dataTab.add(lblEmailaddress);
		
		showFirstname = new JLabel("");
		showFirstname.setBounds(86, 51, 194, 25);
		dataTab.add(showFirstname);
		
		showLastname = new JLabel("");
		showLastname.setBounds(86, 89, 194, 25);
		dataTab.add(showLastname);
		
		showCompany = new JLabel("");
		showCompany.setBounds(86, 127, 194, 25);
		dataTab.add(showCompany);
		
		showEmail = new JLabel("");
		showEmail.setBounds(105, 165, 194, 25);
		dataTab.add(showEmail);
		
		JButton btnUpdateData = new JButton("Update Data");
		btnUpdateData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//update dialog opens where the user can change his data
					updateDialog();					
					
					//refresh
					refresh();
				} catch (SQLException e) {
					handleSQLException(e, true);
				}
			}

			
		});
		btnUpdateData.setBounds(105, 231, 119, 25);
		dataTab.add(btnUpdateData);
		
		
		//Tab 3 - Teams and link to individual team statistics
		JPanel teamTab = new JPanel();
		teamTab.setToolTipText("View team statistics for your teams. Access individual team statistics from here.");
		tabbedPane.addTab("Team", null, teamTab, "Team statistics");
		teamTab.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 51, 743, 407);
		teamTab.add(scrollPane);
		
		JLabel lblTeams = new JLabel("Teams:");
		lblTeams.setBounds(12, 13, 56, 25);
		teamTab.add(lblTeams);		
		
		JButton btnOpenTeamStatistics = new JButton("Open Team Statistics");
		btnOpenTeamStatistics.setEnabled(false);
		btnOpenTeamStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Open individual team statistics for the selected team
				TeamStatistic ts = list.getSelectedValue();
				
				TeamStatisticsFrame tsf = 					//id = hrguy id
						new TeamStatisticsFrame(dbc, ts.id, ts.name, id);	
				tsf.setVisible(true);
			}
		});
		btnOpenTeamStatistics.setBounds(589, 475, 166, 25);
		teamTab.add(btnOpenTeamStatistics);


		list = new JList<TeamStatistic>();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				//disable or enable button for individual team statistics
				if (arg0.getValueIsAdjusting() == false) {
					if (list.getSelectedIndex() == -1) {
						btnOpenTeamStatistics.setEnabled(false);
					} else {
						btnOpenTeamStatistics.setEnabled(true);
					}
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(list);
		
		JLabel lblHintSelection = new JLabel("(Hint: Select a Team from the list and open individual statistics via the button)");
		lblHintSelection.setBounds(33, 475, 472, 25);
		teamTab.add(lblHintSelection);
			
		
		//finish	
		try {
			refresh();
		} catch (SQLException e1) {
			handleSQLException(e1, false);
		}
	}
	
	
	
	/**
	 * Opens a JOptionPane to let the HR Guy update his personal information
	 * after he clicked the according button on the frame.
	 * @throws SQLException 
	 */
	private void updateDialog() throws SQLException {
		//Panel with some textfields and a label
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JTextField fname, lname, comp, mail;
		
		//Init textfields with current Values
		fname = new JTextField(this.firstname);
		lname = new JTextField(this.name);
		comp = new JTextField(this.company);
		mail = new JTextField(this.email);
		
		//Add to the panel
		p.add(new JLabel("Change your personal data by editing the textfields."));
		p.add(fname);
		p.add(lname);
		p.add(comp);
		p.add(mail);
		
		//dialog
		int result = JOptionPane.showConfirmDialog(null, p, "Update personal data", 
						JOptionPane.OK_CANCEL_OPTION);
		
		//obtain results (create & execute query) if okay was pressed
		if (result == JOptionPane.OK_OPTION) {
			String query = "UPDATE hrguys SET firstname = '" + fname.getText() +"', "
					+ "name = '" + lname.getText() + "', company = '" + comp.getText()
					+ "', emailaddress = '" + mail.getText() + "' "
					+ "WHERE id = '" + this.id + "';";
		
			dbc.executeUpdate(query);
		}
			
	}
	
	
	
	
	
	
	
	/**
	 * Creates a quota request in the database. Also updates the
	 * value on the GUI for the amount of quota requested in total.
	 * @param amount The amount of quota requested
	 * @throws SQLException 
	 */
	private void request(int amount) throws SQLException {
		String query = "INSERT INTO quotaRequest (hrguy, amount) "
				+ "VALUES ('" + this.id + "', " + amount + ");";
		dbc.executeUpdate(query);
		this.tfRequest.setText("");
		
		refresh();
	}


	/**
	 * Loads the personal information of the user with the given id.
	 * @param id ID of the user
	 * @throws SQLException
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
		this.company = res.getString("company");
		this.quotaleft = res.getInt("quotaleft");
		
	}




	/**
	 * Sends out an invitation to the candidate. An invitation can only be sent
	 * if the guy has quota left. Furthermore, adds a candidate, the invitation
	 * and the team (if not exists yet for this HR Guy) to the database. 
	 * @param fields Content of the textfields
	 * @throws SQLException 
	 * @throws OutOfQuotaException 
	 */
	private void sendInvitation(String[] fields) 
			throws SQLException, OutOfQuotaException  {
		
		String query;
		
		//Check quota constraint
		refresh();
		if (this.quotaleft == 0) {	//can never be less than zero
			String message = "You have not enough quota at the moment.\n";
			message += "Request some more quota via the Quota-Tab";
			throw new OutOfQuotaException(message);
		}
		
		//Get email information
		String from = this.email;
		String to = fields[2];
		String subject = "HRD Inc. - Survey Invitation";
		String name = fields[1] + " " + fields[0];
		String body = createBody(name, "file:///" + PATH_TO_SURVEY);
		
		
		//Add a candidate
		int candidateID;
		query = "INSERT INTO candidates (name, firstname, email, team) "
				+ "VALUES ('" + fields[0] + "', '" + fields[1] + "', "
				+ "'" + fields[2] + "', '" + fields[3] + "');";
		dbc.executeUpdate(query);
		
		query = "SELECT last_insert_rowid();";
		ResultSet key = dbc.execute(query);
		if (key.next())
			candidateID = key.getInt("last_insert_rowid()");
		else
			throw new SQLException("Creation failed, no rows affected!");
		
		//Add the invitation (status ending) which triggers insert into teams
		query = "INSERT INTO invitations (hrguy, candidate, status) "
				+ "VALUES ('" + this.id + "', "+ candidateID +", 'Pending');";
		dbc.executeUpdate(query);	//Uniqueness checked by database triggers
		

		//Decrease this guy's quota by one
		this.quotaleft--;
		query = "UPDATE hrguys SET quotaleft = "
			+ this.quotaleft + " WHERE id = '" + this.id + "';";
		dbc.executeUpdate(query);

		
		//Preview the invitation email
		EmailFrame preview = new EmailFrame(dbc, candidateID, from, to, subject, body);
		preview.setVisible(true);
		
		refresh();
	}


	/**
	 * Creates the body of an email sent to invite a survey candidate
	 * in HTML style.
	 * @param name Name of the receiver
	 * @param link Unique link to the survey
	 * @return Body that got created
	 */
	private String createBody(String name, String link) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("Hello "+name+",<br>");
		sb.append("I would like to invite you as potential new coworker.<br>");
		sb.append("Therefor, you need to complete a survey. The survey can ");
		sb.append("be accessed via the following link:<br>");
		sb.append("<a href=\"" + link + "\">Link to Survey</a><br>");
		sb.append("Thank you.</body></html>");
		return sb.toString();
	}




	/**
	 * Updates all the information from the database shown in the GUI:
	 * 		- Reload the current amount of quota left, invitations sent and 
	 * 			quota requested by this user
	 * 		- Reload the personal information
	 * 		- Reload team information
	 * 
	 * Updates the GUI afterwards.
	 * @throws SQLException 
	 */
	private void refresh() throws SQLException {
		
		//personal information
		loadPersonalInformation(this.id);
		this.showFirstname.setText(this.firstname);
		this.showLastname.setText(this.name);
		this.showCompany.setText(this.company);
		this.showEmail.setText(this.email);
		
		//invitations
		String query = "SELECT count(*) FROM invitations WHERE hrguy = '"
				+ this.id + "';";
		ResultSet res = dbc.execute(query);
		res.next();
		this.invitationssent = res.getInt("count(*)");
		this.lblSentValue.setText(""+this.invitationssent);
		
		//Quota left
		query = "SELECT quotaleft FROM hrguys WHERE id ="
			+ " '" + this.id + "';";
		res = dbc.execute(query);
		res.next();
		this.quotaleft = res.getInt("quotaleft");
		this.lblLeftValue.setText(""+this.quotaleft);
		
		//Quota requested 
		query = "SELECT sum(amount) FROM quotaRequest WHERE "
				+ "hrguy = '" + this.id +"' GROUP BY hrguy;";
		res = dbc.execute(query);
		if (res.next())		//User has quota requested?
			this.lblReqValue.setText(""+res.getInt("sum(amount)"));
		else
			this.lblReqValue.setText("0");
		
		
		//Team information
		DefaultListModel<TeamStatistic> model = getListModel();
		list.setModel(model);
	}



	/**
	 * Get a model for the JList showing information about the teams
	 * of the HR Guy (user). Uses nested class as list elements.
	 * @return A model for the JList
	 * @throws SQLException
	 */
	private DefaultListModel<TeamStatistic> getListModel() throws SQLException {
		
		//get Teams of this HR Guy together with the number of members
		String query = "SELECT t.id as id, t.name as team, count(*) as members "
				+ "FROM teams t, candidates c, invitations i "
				+ "WHERE t.hrguy = '" + this.id + "' AND t.hrguy = i.hrguy "
				+ "AND i.candidate = c.id AND c.team = t.name "
				+ "GROUP BY t.name;";
		ResultSet res = dbc.execute(query);
		
		DefaultListModel<TeamStatistic> model = new DefaultListModel<TeamStatistic>();
		while (res.next()) {
			int id = res.getInt("id");
			String name = res.getString("team");
			int members = res.getInt("members");
			
			model.addElement(new TeamStatistic(id, name, members));
		}
		
		return model;
	}




	/**
	 * Validates the inputs of the text fields of the invite-form.
	 * Right now it only checks whether one of the fields is empty.
	 * @param fields Content of the textfields
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
		System.exit(0);
	}
}
