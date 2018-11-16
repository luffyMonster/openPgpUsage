package app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import frame.ConfigurationPanel;
import frame.NewEmailPanel;
import frame.ReceivedEmailPanel;

import javax.swing.JTabbedPane;

public class App extends JFrame {

	private JPanel contentPane;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	public Properties properties;
	public ConfigurationPanel config;
	public NewEmailPanel newEmail;
	public ReceivedEmailPanel receivedEmail;

	private static App instance;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = instance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private App() {
	}

	/**
	 * Create the frame.
	 */
	public static App instance() {
		if (instance == null) {
			instance = new App();
			instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			instance.setBounds(100, 100, 450, 300);
			instance.contentPane = new JPanel();
			instance.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			instance.contentPane.setLayout(new BorderLayout(0, 0));
			instance.setContentPane(instance.contentPane);
			instance.contentPane.add(instance.tabbedPane, BorderLayout.CENTER);

			instance.config = new ConfigurationPanel();
			instance.tabbedPane.addTab("Configuration", null, instance.config, null);

			instance.newEmail = new NewEmailPanel();
			instance.tabbedPane.addTab("New Email", null, instance.newEmail, null);

			instance.receivedEmail = new ReceivedEmailPanel();
			instance.tabbedPane.addTab("Received Emails", null, instance.receivedEmail, null);

			
//			instance.tabbedPane.addTab("Web of Trust", null, new JPanel(), null);
			instance.properties = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream("config.properties");
				// load a properties file
				instance.properties.load(input);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}

}
