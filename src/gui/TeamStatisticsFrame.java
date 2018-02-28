package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;

import dbc.DatabaseController;

import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * This class provides a frame for showing the statistics of 
 * individual teams. Can be accessed by the HRGuyGUI (tab 3).
 * It contains information about:
 * 		- the team members
 * 		- their invitation's status (survey pending/completed)
 * 		- some more statistics
 * @author Jero
 */
public class TeamStatisticsFrame extends JFrame {
	
	//auto generated
	private static final long serialVersionUID = 8456004462700979408L;

	//HR information
	private int teamid;			//id of the individually selected team
	private String guyid; 		//id of the HR guy
	private String teamname; 	//name of the team

	//Database
	private DatabaseController dbc;
	private static final String PATH = "C:/SQLite/db/dba/HRD.db";
	
	//Frame variables
	private JPanel contentPane;
	private JTable table;

	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Test id is 1 (Finance Team) of test user
					DatabaseController dbc = new DatabaseController(PATH);
					TeamStatisticsFrame frame = new TeamStatisticsFrame(dbc, 1, "Finance", "test");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param dbc Controller for the database
	 * @param teamid The ID of the team
	 * @param teamname Name of the team
	 * @param guyid ID of the HR Guy viewing statistics
	 */
	public TeamStatisticsFrame(DatabaseController dbc, 
			int teamid, String teamname, String guyid) {
		setTitle("Team Statistics");
		
		//Init HR information
		this.teamid = teamid;
		this.dbc = dbc;
		this.guyid = guyid;
		this.teamname = teamname;
		
		//Frame options
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 51, 598, 225);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		//Get Table content
		
		String query = 
				"SELECT c.firstname as Firstname, c.name as Lastname, "
				+ "i.status as Status "
				+ "FROM teams t, invitations i, candidates c "
				+ "WHERE i.candidate = c.id AND i.hrguy = '" + this.guyid + "' "
				+ "AND c.team = t.name AND t.id = " + this.teamid + ";";
		try {
			DefaultTableModel model = this.dbc.executeAndBuildTable(query);
			table.setModel(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		JLabel lblNewLabel = new JLabel("Individual Team Statistics"
				+ " for the Team " + this.teamname);
		lblNewLabel.setBounds(12, 13, 300, 25);
		panel.add(lblNewLabel);
		
		
		
		
		
		//Some aggregate information
		double avgAge = -1;
		int minAge = -1, maxAge = -1;
		String subquery = "SELECT c.id "		//get candidates id's of this team
				+ "FROM teams t, invitations i, candidates c "
				+ "WHERE c.id = i.candidate AND i.hrguy = t.hrguy "
				+ "AND t.hrguy = '" + this.guyid + "' AND t.name = c.team "
				+ "AND t.id = " + this.teamid;
		
		//Get average, minimal and maximal age
		query = "SELECT avg(age) as avg, min(age) as min, max(age) as max "
				+ "FROM surveyresults WHERE candidate in (" + subquery + ");";
		
		try {
			ResultSet res = dbc.execute(query);
			res.next();
			avgAge = res.getDouble("avg");
			minAge = res.getInt("min");
			maxAge = res.getInt("max");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		JLabel lblAvgAge = new JLabel("Average Age: " + avgAge);
		lblAvgAge.setBounds(12, 289, 115, 25);
		panel.add(lblAvgAge);
		
		JLabel lblMinAge = new JLabel("Minimal Age: " + minAge);
		lblMinAge.setBounds(12, 327, 115, 25);
		panel.add(lblMinAge);
		
		JLabel lblMaxAge = new JLabel("Maximal Age:" + maxAge);
		lblMaxAge.setBounds(12, 365, 115, 25);
		panel.add(lblMaxAge);
		
		JLabel lblSelfratingValues = new JLabel("Selfrating average values:");
		lblSelfratingValues.setBounds(150, 289, 154, 25);
		panel.add(lblSelfratingValues);
		
		
		//Get averages of the self rating values
		double avgProd = -1, avgQoS = -1, avgExp = -1;
		query = "SELECT avg(productivity), avg(quality_of_service), avg(exceeding_expectations) "
				+ "FROM surveyresults WHERE candidate in (" + subquery +");";
		
		try {
			ResultSet res = dbc.execute(query);
			res.next();
			avgProd = res.getDouble(1);
			avgQoS = res.getDouble(2);
			avgExp = res.getDouble(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		JLabel lblLevelOfProductivity = new JLabel("Level of Productivity: "+avgProd);
		lblLevelOfProductivity.setBounds(316, 289, 159, 25);
		panel.add(lblLevelOfProductivity);
		
		JLabel lblQualityOfService = new JLabel("Quality of Service: "+avgQoS);
		lblQualityOfService.setBounds(492, 289, 141, 25);
		panel.add(lblQualityOfService);
		
		JLabel lblExceeding = new JLabel("Level of Exceeding Customer Expectations: "+avgExp);
		lblExceeding.setBounds(316, 327, 294, 25);
		panel.add(lblExceeding);
		
		
		
		//Get working experience average, minimum and maximum
		double avg = -1, min = -1, max = -1;
		query = "SELECT avg(working_experience), min(working_experience), "
				+ "max(working_experience) FROM surveyresults WHERE candidate in ("
				+ subquery + ");";
		
		try {
			ResultSet res = dbc.execute(query);
			res.next();
			avg = res.getDouble(1);
			min = res.getInt(2);
			max = res.getInt(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JLabel lblAverageWorkExperience = new JLabel("Avg Working Exper.: "+avg);
		lblAverageWorkExperience.setBounds(133, 405, 147, 25);
		panel.add(lblAverageWorkExperience);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(128, 296, 2, 134);
		panel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(150, 388, 460, 2);
		panel.add(separator_1);
		
		JLabel lblMinWorkingExperience = new JLabel("Min Working Exper.: "+min);
		lblMinWorkingExperience.setBounds(292, 405, 147, 25);
		panel.add(lblMinWorkingExperience);
		
		JLabel lblMaxWorkingExper = new JLabel("Max Working Exper.: "+max);
		lblMaxWorkingExper.setBounds(451, 405, 159, 25);
		panel.add(lblMaxWorkingExper);
		
		
	}
}
