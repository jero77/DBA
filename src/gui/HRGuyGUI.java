package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dbc.DatabaseController;

public class HRGuyGUI extends JFrame {

	private JPanel contentPane;
	
	
	//Database variables
	private DatabaseController dbc = null;
	private static final String PATH_TO_DB = "C:/SQLite/db/dba/HRD.db";

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
		
		this.dbc = dbc;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
