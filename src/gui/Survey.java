package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;

import dbc.DatabaseController;

public class Survey extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField tfFirstname;
	private JTextField tfLastname;
	private JTextField tfAge;
	private JTextField tfWorkExp;
	
	private JSlider sliderQoS, sliderProd, sliderExp;
	
	
	private DatabaseController dbc;
	
	//candidate info
	private int candidateID;
	private String firstname, lastname;
	

	/**
	 * Create the frame.
	 * @param dbc Controller of the database
	 * @param candidateID ID of the candidate (uniqueness)
	 * @param firstname Firstname of the candidate
	 * @param lastname Lastname of the candidate
	 */
	public Survey(DatabaseController dbc, int candidateID, String firstname,
			String lastname) {
		setTitle("Survey");
		
		this.dbc = dbc;
		
		//candidate information
		this.candidateID = candidateID;
		this.firstname = firstname;
		this.lastname = lastname;
		
		//Frame options
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 13, 548, 402);
		panel.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(
				new Dimension(scrollPane.getWidth(), 1000));
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblFirstname = new JLabel("Firstname:");
		lblFirstname.setBounds(12, 13, 69, 25);
		panel_1.add(lblFirstname);
		
		JLabel lblLastname = new JLabel("Lastname:");
		lblLastname.setBounds(12, 51, 69, 25);
		panel_1.add(lblLastname);
		
		tfFirstname = new JTextField(this.firstname);
		tfFirstname.setBounds(93, 14, 180, 22);
		panel_1.add(tfFirstname);
		tfFirstname.setColumns(10);
		
		tfLastname = new JTextField(this.lastname);
		tfLastname.setColumns(10);
		tfLastname.setBounds(93, 52, 180, 22);
		panel_1.add(tfLastname);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(12, 89, 69, 25);
		panel_1.add(lblAge);
		
		tfAge = new JTextField();
		tfAge.setColumns(10);
		tfAge.setBounds(93, 90, 69, 22);
		panel_1.add(tfAge);
		
		JLabel lblHowLongWere = new JLabel("How long is your work experience? (in terms of full years)");
		lblHowLongWere.setBounds(12, 146, 343, 25);
		panel_1.add(lblHowLongWere);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 131, 522, 2);
		panel_1.add(separator);
		
		tfWorkExp = new JTextField();
		tfWorkExp.setColumns(10);
		tfWorkExp.setBounds(367, 147, 69, 22);
		panel_1.add(tfWorkExp);
		
		JLabel lblPleaseGiveYourself = new JLabel("Please give yourself a rating (5=\"excellent\" - 1=\"poor\") for the following categories:");
		lblPleaseGiveYourself.setBounds(12, 184, 488, 25);
		panel_1.add(lblPleaseGiveYourself);
		
		JLabel lblLevelOfProductivity = new JLabel("Level of Productivity");
		lblLevelOfProductivity.setBounds(12, 222, 140, 25);
		panel_1.add(lblLevelOfProductivity);
		
		JLabel lblLevelOfQuality = new JLabel("Quality of Service");
		lblLevelOfQuality.setBounds(12, 279, 140, 25);
		panel_1.add(lblLevelOfQuality);
		
		JLabel lblLevelOfExceeding = new JLabel("<html>Level of Exceeding<br>Customer Expectations</html>");
		lblLevelOfExceeding.setBounds(12, 333, 140, 40);
		panel_1.add(lblLevelOfExceeding);
		
		sliderProd = new JSlider();
		sliderProd.setPaintTicks(true);
		sliderProd.setSnapToTicks(true);
		sliderProd.setPaintLabels(true);
		sliderProd.setMinorTickSpacing(1);
		sliderProd.setMajorTickSpacing(1);
		sliderProd.setValue(3);
		sliderProd.setMinimum(1);
		sliderProd.setMaximum(5);
		sliderProd.setBounds(149, 222, 154, 44);
		panel_1.add(sliderProd);
		
		sliderQoS = new JSlider();
		sliderQoS.setValue(3);
		sliderQoS.setSnapToTicks(true);
		sliderQoS.setPaintTicks(true);
		sliderQoS.setPaintLabels(true);
		sliderQoS.setMinorTickSpacing(1);
		sliderQoS.setMinimum(1);
		sliderQoS.setMaximum(5);
		sliderQoS.setMajorTickSpacing(1);
		sliderQoS.setBounds(149, 279, 154, 44);
		panel_1.add(sliderQoS);
		
		sliderExp = new JSlider();
		sliderExp.setValue(3);
		sliderExp.setSnapToTicks(true);
		sliderExp.setPaintTicks(true);
		sliderExp.setPaintLabels(true);
		sliderExp.setMinorTickSpacing(1);
		sliderExp.setMinimum(1);
		sliderExp.setMaximum(5);
		sliderExp.setMajorTickSpacing(1);
		sliderExp.setBounds(149, 329, 154, 44);
		panel_1.add(sliderExp);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setActionCommand("submit");
		btnSubmit.addActionListener(this);
		btnSubmit.setBounds(463, 425, 97, 25);
		panel.add(btnSubmit);
		
		
	}

	/**
	 * Submit all the values entered to the database.
	 * @throws SQLException 
	 */
	private void submit() throws SQLException, IllegalArgumentException {
		
		//Get all the values
		String fname = "", lname = "";
		int age = 0, workExp = 0;
		int selfProd = 0, selfQoS = 0, selfExp = 0;		//selfrating values
		
		//Mandatory fields
		fname = tfFirstname.getText();
		lname = tfLastname.getText();
		
		if (fname.equals("") || lname.equals(""))
			throw new IllegalArgumentException("Firstname and Lastname are"
					+ " mandatory fields!");
		
		//Non-mandatory fields
		try {
			age = Integer.parseInt(tfAge.getText());
		} catch (NumberFormatException e) {
			age = -1;
		}
		
		try {
			workExp = Integer.parseInt(tfWorkExp.getText());
		} catch (NumberFormatException e) {
			workExp = -1;
		}
		
		//slider values are default value (3) if untouched
		selfProd = sliderProd.getValue();
		selfQoS = sliderQoS.getValue();
		selfExp = sliderExp.getValue();
		
		
		//Create insert query for the values which are definitely set
		String query = "INSERT INTO surveyresults (candidate, lastname, firstname,"
				+ "productivity, quality_of_service, exceeding_expectations) "
				+ "VALUES (" + this.candidateID + ", '" + lname + "', '" + fname 
				+ "', " + selfProd + ", " + selfQoS + ", " + selfExp + ");";
		dbc.executeUpdate(query);
		
		
		//Check the other values: age, workExp
		if (age >= 0) {
			query = "UPDATE surveyresults SET age = " + age + " "
					+ "WHERE candidate = " + this.candidateID + ";";
			dbc.executeUpdate(query);
		}
		
		if (workExp >= 0) {
			query = "UPDATE surveyresults SET working_experience = " + workExp
					+ " WHERE candidate = " + this.candidateID + ";";
			dbc.executeUpdate(query);
		}
			
		
		//set invitation status to completed (link is invalid now)
		query = "UPDATE invitations SET status = 'Completed' WHERE "
				+ "candidate = " + this.candidateID + ";";
		dbc.executeUpdate(query);
	}

	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("submit")) {
			try {
				submit();
				this.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), 
						"Error", JOptionPane. ERROR_MESSAGE);
				ex.printStackTrace();
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), 
						"Error", JOptionPane. ERROR_MESSAGE);
			}
		}
	}

}
