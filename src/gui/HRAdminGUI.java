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

import javax.swing.JTable;

import dbc.DatabaseController;

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

	
	//Class variables for the content of the frame 
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	
	//Variables for database access
	DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";
	private static final String TABLE_QUERY = "SELECT * FROM hrguys";
	private DefaultTableModel defaultModel = null;
	private JTable table;
	

	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//Set up admin gui
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HRAdminGUI frame = new HRAdminGUI();
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
	public HRAdminGUI() {
		
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
					defaultModel = getModel(TABLE_QUERY);
				} catch (SQLException e1) {
					handleSQLException(e1, true);
				} finally {
					//Invokes JTable.tableChanged()
					defaultModel.fireTableDataChanged();
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
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblCompany = new JLabel("Company:");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		JLabel lblEmailaddress = new JLabel("Email-Address:");
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		
		JLabel lblQuotaLeft = new JLabel("Quota Left:");
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		
		
		//Add button
		JButton btnAdd = new JButton("Add HR guy");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//validate textfield inputs
				
				//get the entered information & add the guy
				
				
				addHRGuy();
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
		
		
		
		//	Organize layout of 2nd tab
		GroupLayout gl_addPanel = new GroupLayout(addPanel);
		gl_addPanel.setHorizontalGroup(
			gl_addPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_addPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_addPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_addPanel.createSequentialGroup()
							.addGroup(gl_addPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_addPanel.createSequentialGroup()
									.addComponent(lblEmailaddress)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
								.addGroup(gl_addPanel.createSequentialGroup()
									.addComponent(lblCompany)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
								.addGroup(gl_addPanel.createSequentialGroup()
									.addComponent(lblName)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_addPanel.createSequentialGroup()
									.addComponent(lblLastName)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)))
							.addContainerGap(437, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
							.addComponent(lblQuotaLeft)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(Alignment.LEADING, gl_addPanel.createSequentialGroup()
							.addComponent(lblPassword)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_addPanel.createSequentialGroup()
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExi, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))))
		);
		gl_addPanel.setVerticalGroup(
			gl_addPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_addPanel.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLastName)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCompany)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmailaddress)
						.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(19)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuotaLeft)
						.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(278, Short.MAX_VALUE))
				.addGroup(gl_addPanel.createSequentialGroup()
					.addContainerGap(488, Short.MAX_VALUE)
					.addGroup(gl_addPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExi)
						.addComponent(btnAdd)))
		);
		addPanel.setLayout(gl_addPanel);
		
		
		
		//database options
		try {
			initDB(PATH_TO_DB, TABLE_QUERY);
		} catch (SQLException e) {
			handleSQLException(e, true);
		} finally {
			//Set up table
			table.setModel(defaultModel);
			scrollPane.setViewportView(table);
		}
	
		
	}
	
	
	/**
	 * Handles any SQLException with some error output and
	 * also a message dialog for the user
	 * @param e THe exception to handle
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
	 * Adds a HR guy with the given information to the database.
	 * 
	 */
	private void addHRGuy() {
		// TODO Auto-generated method stub
		//TODO Test for refresh
	}



	/**
	 * Initializes all the needed options and connections to the database.
	 * @param path Path to the database
	 * @param tableQuery Initial query used to obtain the table model
	 * 
	 */
	private void initDB(String path, String tableQuery) throws SQLException {
	
		dbc = new DatabaseController(path);
		//Model for JTable
		this.defaultModel = this.getModel(tableQuery);
		
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
