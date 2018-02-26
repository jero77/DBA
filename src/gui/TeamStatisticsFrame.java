package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;

import dbc.DatabaseController;

public class TeamStatisticsFrame extends JFrame {
	
	
	//HR information
	private String id;		//id of the user
	private String team;	//team name

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
					//Test id is 'test', team is 'Finance'
					DatabaseController dbc = new DatabaseController(PATH);
					TeamStatisticsFrame frame = 
							new TeamStatisticsFrame(dbc, "test", "Finance");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TeamStatisticsFrame(DatabaseController dbc, String id, String team) {
		setTitle("Team Statistics - " + team);
		
		//Init HR information
		this.id = id;
		this.team = team;
		this.dbc = dbc;
		
		//Frame options
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				+ "WHERE i.hrguy = '" + this.id + "' AND i.candidate = c.id "
				+ "AND c.team = t.name AND t.name = '" + this.team + "';";
		try {
			table.setModel(dbc.executeAndBuildTable(query));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		JLabel lblNewLabel = new JLabel("Individual Team Statistics for the Team");
		lblNewLabel.setBounds(12, 13, 231, 25);
		panel.add(lblNewLabel);
		
		JLabel lblTeam = new JLabel(this.team);
		lblTeam.setBounds(245, 13, 116, 25);
		panel.add(lblTeam);
		
		
	}
}
