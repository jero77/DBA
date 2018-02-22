package gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import java.awt.Font;


import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JTable;

import dbc.DatabaseController;
import dbc.PasswordHash;

import javax.swing.JScrollPane;


import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * This class provides a GUI for the HR admins.
 * It provides the following functionality:
 * 		- add new HR guys to the database
 * 		- yield an overview over the HR guys added by an HR admin
 * 		- 
 * @author Jero
 *
 */
public class HRAdminGUI extends JFrame {

	
	private JPanel contentPane;
	
	
	//Class variables for the form
	private JTextField tfID;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfCompany;
	private JTextField tfEmail;
	private JTextField tfQuota;
	private JTextField tfPassword;
	
	
	private enum TextFields {
		ID, FIRSTNAME, LASTNAME, COMPANY, EMAIL, QUOTA, PASSWORD
	}
	
	
	
	
	//Variables for database access
	DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	private static final String TABLE_QUERY = 
			"SELECT id, firstname, name, company, emailaddress, quotaleft,"
			+ " password FROM hrguys JOIN added_hrguy";
	private String personalQuery = null;
	private DefaultTableModel defaultModel = null;
	private JTable table;
	
	
	
	//Variables for the admin
	private String adminID = null;
	

	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//Set up admin gui
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//test id is "adm1"
					HRAdminGUI frame = new HRAdminGUI("adm1");
					frame.setVisible(true);
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the frame and initialize database access.
	 */
	public HRAdminGUI(String adminID) {
		
		
		//Admin options
		this.adminID = adminID;
		
		
		
		
		//Handle closing event
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
		
		
		//Frame options
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	//custom close operation
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//	Tabs
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		//	1st Tab
		//TODO Add for every HR guy how many invitations he has sent out
		//TODO Add for very HR guy how much quota he has left
		
		JPanel listPanel = new JPanel();
		listPanel.setToolTipText("Display a list of all the HR guys added by you");
		tabbedPane.addTab("List HR guys", null, listPanel, null);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		//	scrollable Table
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane();
		listPanel.add(scrollPane, BorderLayout.CENTER);
		
		
		//	Button panel
		JPanel buttonPanel = new JPanel();
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Refresh the table
				try {
					table.setModel(getModel(personalQuery));
				} catch (SQLException e1) {
					handleSQLException(e1, true);
				}
			}
		});
		refreshButton.setToolTipText("Refresh the table");
		refreshButton.setHorizontalAlignment(SwingConstants.RIGHT);
		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		//Exit Button
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		btnExit.setToolTipText("Exit the program");
		btnExit.setHorizontalAlignment(SwingConstants.RIGHT);
		buttonPanel.add(btnExit);
		buttonPanel.add(refreshButton);
		
		
		
		//	2nd Tab
		JPanel addPanel = new JPanel();
		tabbedPane.addTab("Add HR guy", null, addPanel, "Opens a form to add a new HR guy to the database");
		
		//	Form to add HR guy
		JLabel lblName = new JLabel("First Name:");
		
		tfFirstName = new JTextField();
		tfFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name:");
		
		tfLastName = new JTextField();
		tfLastName.setColumns(10);
		
		JLabel lblCompany = new JLabel("Company:");
		
		tfCompany = new JTextField();
		tfCompany.setColumns(10);
		
		JLabel lblEmailaddress = new JLabel("Email-Address:");
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		
		JLabel lblQuotaLeft = new JLabel("Quota Left:");
		
		tfQuota = new JTextField();
		tfQuota.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		
		
		//Add button
		JButton btnAdd = new JButton("Add HR guy");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Get the textfields inputs (positions in array defined by enum)
				String fields[] = getInputs();
				
				//validate textfield inputs		
				if (!validInputs(fields)) {
					String error = "An error occured!\n";
					error += "Check if all the textfields contain valid information\n";
					error += "and try to add again.";
					JOptionPane.showMessageDialog(null, error);
				}
				else {	
					
					try {
						addHRGuy(fields);
						String success = "A new HR guy was added succesfully!";
						JOptionPane.showMessageDialog(null, success);
					} catch (SQLException e) {
						handleSQLException(e, true);
					} 
				}
			}

		});
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		
		//Exit button
		JButton btnExi = new JButton("Exit");
		btnExi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}
		});
		btnExi.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblId = new JLabel("ID:");
		
		tfID = new JTextField();
		tfID.setColumns(10);
		
		JLabel lblNoteTheChoosen = new JLabel("Note: The choosen ID will be used by the HR guy to login in, so it must be unique!");
		
		
		
		//	Organize layout of 2nd tab
		GroupLayout gl_addPanel = new GroupLayout(addPanel);
		gl_addPanel.setHorizontalGroup(
			gl_addPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_addPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_addPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_addPanel.createSequentialGroup()
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExi, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addGap(3))
						.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
							.addComponent(lblPassword)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfPassword, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
							.addComponent(lblQuotaLeft)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfQuota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_addPanel.createSequentialGroup()
							.addComponent(lblLastName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfLastName, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(437, Short.MAX_VALUE))
						.addGroup(gl_addPanel.createSequentialGroup()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfFirstName, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(437, Short.MAX_VALUE))
						.addGroup(gl_addPanel.createSequentialGroup()
							.addComponent(lblId)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNoteTheChoosen)
							.addContainerGap(132, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
							.addGroup(gl_addPanel.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
									.addComponent(lblEmailaddress)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_addPanel.createSequentialGroup()
									.addComponent(lblCompany)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(tfCompany, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)))
							.addGap(381))))
		);
		gl_addPanel.setVerticalGroup(
			gl_addPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_addPanel.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblId)
						.addComponent(tfID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNoteTheChoosen))
					.addGap(27)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(tfFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLastName)
						.addComponent(tfLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCompany)
						.addComponent(tfCompany, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblEmailaddress)
						.addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuotaLeft)
						.addComponent(tfQuota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(tfPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(169)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExi)
						.addComponent(btnAdd))
					.addGap(22))
		);
		addPanel.setLayout(gl_addPanel);
		
		
		
		//database options
		try {
			initDB(PATH_TO_DB, TABLE_QUERY);

			//Set up table
			table.setModel(defaultModel);
			scrollPane.setViewportView(table);
			
		} catch (SQLException e) {
			handleSQLException(e, true);
		} 
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
		JOptionPane.showMessageDialog(this, "An error occured!\n"+e.getMessage());
	}


	
	/**
	 * Gets the inputs of all the Textfields of the add-form according to
	 * the enumeration TextFields.
	 * @return Inputs in the form (2nd tab of the frame)
	 */
	private String[] getInputs() {
		String[] inputs = new String[TextFields.values().length];
		
		//ID
		inputs[TextFields.ID.ordinal()] = tfID.getText();
		//First Name
		inputs[TextFields.FIRSTNAME.ordinal()] = tfFirstName.getText();
		//Last Name
		inputs[TextFields.LASTNAME.ordinal()] = tfLastName.getText();
		//Company
		inputs[TextFields.COMPANY.ordinal()] = tfCompany.getText();
		//Email
		inputs[TextFields.EMAIL.ordinal()] = tfEmail.getText();
		//Quota
		inputs[TextFields.QUOTA.ordinal()] = tfQuota.getText();
		//Password
		inputs[TextFields.PASSWORD.ordinal()] = tfPassword.getText();
		
		return inputs;
	}
	
	
	
	
	/**
	 * Validates the inputs of the text fields of the add-form.
	 * Right now it only checks whether one of the fields is empty.
	 * @return Returns whether the inputs are valid or not
	 */
	private boolean validInputs(String[] fields) {
		
		for (String str : fields) {
			if (str.equals(""))
				return false;
		}
		return true;
	}
	
	

	/**
	 * Adds a HR guy with the given information to the database.
	 * @param fields The information about the new guy obtained by the method
	 * getInputs() and validated by the method validInputs(String[] fields)
	 * @throws SQLException 
	 * @see getInputs(), validInputs(String fields[]) 
	 */
	private void addHRGuy(String[] fields) throws SQLException {
		
		//Insert a new hrguy into the hrguy table
		String query = "INSERT INTO hrguys VALUES (";
		for (TextFields tf : TextFields.values()) {
			
			if (tf.equals(TextFields.QUOTA)) {			
				//Number, not a string
				query += fields[tf.ordinal()] + ", ";
				
			} else if (tf.equals(TextFields.PASSWORD)) {
				//Save hash of the password
				fields[tf.ordinal()] = PasswordHash.hashPassword(fields[tf.ordinal()]);
				query += "'" + fields[tf.ordinal()] + "'";
			} else {
				query += "'"+fields[tf.ordinal()] + "', ";
			}
		}
		query += ");";
		System.out.println(query);
		dbc.executeUpdate(query);

		
		//Insert the relation that this admin added the guy
		query = "INSERT INTO added_hrguy VALUES (";
		query += "'" + this.adminID + "', '" + fields[TextFields.ID.ordinal()] + "'";
		query += ");";
		
		dbc.executeUpdate(query);	
	}



	/**
	 * Initializes all the needed options and connections to the database.
	 * @param path Path to the database
	 * @param tableQuery Initial query used to obtain the personal query
	 * 
	 */
	private void initDB(String path, String tableQuery) throws SQLException {
		dbc = new DatabaseController(path);
		
		//Personalized query - filter hrguys (only the ones added by this admin)
		this.personalQuery = tableQuery;
		this.personalQuery += " ON hrguys.id = added_hrguy.guy";
		this.personalQuery += " WHERE added_hrguy.admin = '"+this.adminID+"'";
		
		//Model for JTable
		this.defaultModel = this.getModel(personalQuery);
	}


	/**
	 * Get the model for the JTable via the DatabaseController dbc
	 * @param tableQuery The query to be executed resulting in a DefaultTableModel
	 * @return The model for the JTable
	 * @throws SQLException 
	 */
	private DefaultTableModel getModel(String tableQuery) throws SQLException {
		return dbc.executeAndBuildTable(tableQuery);
	}
	
	
	
	/**
	 * Exits the Admin-GUI in a safe way after confirmed by the user via
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
