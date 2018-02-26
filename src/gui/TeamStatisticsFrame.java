package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;

import dbc.DatabaseController;

/**
 * This class provides a frame for showing the statistics of 
 * individual teams. Can be accessed by the HRGuyGUI (tab 3).
 * It contains information about:
 * 		- the team members
 * 		- their invitation's status (survey pending/completed)
 * 		- 
 * @author Jero
 */
public class TeamStatisticsFrame extends JFrame {
	
	
	//auto generated
	private static final long serialVersionUID = 8456004462700979408L;

	//HR information
	private int teamid;		//id of the individually selected team

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
					//Test id is 1 (Finance Team)
					DatabaseController dbc = new DatabaseController(PATH);
					TeamStatisticsFrame frame = new TeamStatisticsFrame(dbc, 1);
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
	 * @param id The id of the
	 */
	public TeamStatisticsFrame(DatabaseController dbc, int teamid) {
		setTitle("Team Statistics");
		
		//Init HR information
		this.teamid = teamid;
		this.dbc = dbc;
		
		//Frame options
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 51, 598, 329);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		//Get Table content
		String query = 
				"SELECT t.name as Team, c.firstname as Firstname, c.name as Lastname, "
				+ "i.status as Status "
				+ "FROM teams t, invitations i, candidates c "
				+ "WHERE i.candidate = c.id AND c.team = t.name AND t.id = " + this.teamid + ";";
		try {
			table.setModel(this.dbc.executeAndBuildTable(query));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		JLabel lblNewLabel = new JLabel("Individual Team Statistics"
				+ " for the Team with id "+this.teamid);
		lblNewLabel.setBounds(12, 13, 300, 25);
		panel.add(lblNewLabel);
		
		
	}
}
