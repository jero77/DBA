package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import dbc.DatabaseController;

/**
 * This class provides a frame for previewing how an invitation email 
 * sent from a HR Guy to a survey candidate could look like.
 * The email body contains a link which enables the survey.
 * @author Jero
 *
 */
public class EmailFrame extends JFrame {
	
	
	//auto generated
	private static final long serialVersionUID = 6075986297413716477L;


	//Email variables
	private String from, to, subject, body;
	
	//Database
	private DatabaseController dbc;
	
	//Candidate
	private int candidateID;
	

	//JFrame
	private JPanel contentPane;

	/**
	 * Test Unit.
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Test variables
					String from = "somesender@mail.com";
					String to = "somereceiver@mail.com";
					String subject = "Test";
					String body = "Hello, this is for test purposes without a link.";
					EmailFrame frame = new EmailFrame(null, 1, from, to, subject, body);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame previewing an email.
	 * @param dbc Controller of the database
	 * @param candidateID ID of the candidate
	 * @param from Email of the sender
	 * @param to Receiver of the email
	 * @param subject Subject of the email
	 * @param body Body of the email
	 */
	public EmailFrame(DatabaseController dbc, int candidateID, String from,
			String to, String subject, String body) {
		super("Email Preview");
		
		//Email
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		
		//Candidate
		this.candidateID = candidateID;
		
		//Database
		this.dbc = dbc;
		
		//Frame options
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(12, 0, 56, 25);
		panel.add(lblFrom);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(12, 25, 56, 25);
		panel.add(lblTo);
		
		JLabel lblSubject = new JLabel("SUBJECT:");
		lblSubject.setBounds(12, 63, 56, 25);
		panel.add(lblSubject);
		
		JLabel lblSender = new JLabel(this.from);
		lblSender.setBounds(80, 0, 480, 25);
		panel.add(lblSender);
		
		JLabel lblReceiver = new JLabel(this.to);
		lblReceiver.setBounds(80, 25, 480, 25);
		panel.add(lblReceiver);
		
		JLabel lblSubjectText = new JLabel(this.subject);
		lblSubjectText.setBounds(80, 63, 480, 25);
		panel.add(lblSubjectText);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 52, 548, 2);
		panel.add(separator);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 101, 548, 229);
		panel.add(scrollPane);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//Check if link is still valid (unique usable)
					String query = "SELECT status, firstname, name FROM invitations i, candidates c"
							+ " WHERE i.candidate = " + candidateID + " AND c.id = i.candidate;";
					try {
						ResultSet res = dbc.execute(query);
						res.next();
						
						if (res.getString("status").equals("Pending")) {
							//Show survey
							Survey survey = new Survey(dbc, candidateID,
									res.getString("firstname"), res.getString("name"));
							survey.setVisible(true);
							
						} else {
							JOptionPane.showMessageDialog(null, "The link is not valid anymore!",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		editorPane.setContentType("text/html");
		editorPane.setText(this.body);
		editorPane.setEditable(false);
		scrollPane.setViewportView(editorPane);
	}
}
