package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;

public class EmailFrame extends JFrame {
	
	
	//auto generated
	private static final long serialVersionUID = 6075986297413716477L;


	//Email variables
	private String from, to, subject, body, link;
	

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
					String body = "Hello, this is for test purposes.";
					String link = "LINK";
					EmailFrame frame = new EmailFrame(from, to, subject, body, link);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame previewing an email.
	 * @param from Email of the sender
	 * @param to Recevier of the email
	 * @param subject Subject of the email
	 * @param body Body of the email
	 * @param link A unique link contained in the body.
	 */
	public EmailFrame(String from, String to, String subject, String body, String link) {
		super("Email Preview");
		
		//Email
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.link = link;
		
		
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
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textPane.setEditable(false);
		textPane.setText(this.body);
		scrollPane.setViewportView(textPane);
	}
}
