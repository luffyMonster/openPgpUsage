package frame;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import app.App;
import utils.KeyGenerate;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextField;

import com.didisoft.pgp.PGPException;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;

import java.io.File;
import java.io.IOException;


public class ConfigurationPanel extends JPanel {
	private JTextField txtEmail;
	private JPasswordField txtPassword;
	private JTextField txtKeySize;
	private JTextField txtPrivateFolder;
	private JTextField txtFolder;
	
	public String getPrivateKeyFile() {
		return txtPrivateFolder.getText() + "/privatekey.skr";
	}
	public String getPublicFolder( ) {
		return txtFolder.getText();
	}
	public String getEmail() {
		return txtEmail.getText();
	}
	
	public char[] getPassword() {
		return txtPassword.getPassword();
	}
	/**
	 * Create the panel.
	 */
	public ConfigurationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblEmail = DefaultComponentFactory.getInstance().createLabel("Email");
		panel.add(lblEmail, "2, 2, fill, fill");
		
		txtEmail = new JTextField();
		txtEmail.setText("demoptitd1401@gmail.com");
		panel.add(txtEmail, "4, 2, fill, fill");
		txtEmail.setColumns(30);
		
		JLabel lblPassword = DefaultComponentFactory.getInstance().createLabel("Password");
		panel.add(lblPassword, "2, 4, fill, fill");
		
		txtPassword = new JPasswordField();
		txtPassword.setEchoChar('*');
		txtPassword.setToolTipText("");
		txtPassword.setText("$12345678");
		panel.add(txtPassword, "4, 4, fill, fill");
		
		JLabel lblPrivateKey = new JLabel(" Private Folder");
		panel.add(lblPrivateKey, "2, 6, fill, fill");
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, "4, 6, fill, center");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		txtPrivateFolder = new JTextField();
		txtPrivateFolder.setEditable(false);
		panel_2.add(txtPrivateFolder, "1, 1, fill, fill");
		txtPrivateFolder.setColumns(10);
		
		JButton btnChoosePrivate = new JButton("Choose");
		panel_2.add(btnChoosePrivate, "2, 1, fill, fill");
		
		JLabel lblKeyFolder = new JLabel("Public Key folder");
		panel.add(lblKeyFolder, "2, 8, fill, fill");
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, "4, 8, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JButton btnChooseFolder = new JButton("Choose");
		panel_3.add(btnChooseFolder, "2, 1");
		JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.setCurrentDirectory(new File("."));
		btnChooseFolder.addActionListener((e)->{
			int returnVal = f.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            txtFolder.setText(f.getSelectedFile().getAbsolutePath());
	        }
		});
		txtFolder = new JTextField();
		txtFolder.setEditable(false);
		panel_3.add(txtFolder, "1, 1, fill, fill");
//		txtFolder.setColumns(10);
		
		JLabel lblKeySize = new JLabel("Key Size");
		panel.add(lblKeySize, "2, 10, fill, fill");
		
		txtKeySize = new JTextField();
		txtKeySize.setText("1024");
		panel.add(txtKeySize, "4, 10, fill, default");
		txtKeySize.setColumns(10);
		
		JButton btnGenerateKey = new JButton("Generate Key");
		panel.add(btnGenerateKey, "4, 14");
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		btnChoosePrivate.addActionListener((e)->{
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            txtPrivateFolder.setText(file.getAbsolutePath());
	        }
		});
		
        
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnGenerateKey.addActionListener((e)->{
			try {
				KeyGenerate.make(getEmail(), getPublicFolder()+"/"+getEmail()+"/", txtPrivateFolder.getText() + "/", Integer.parseInt(txtKeySize.getText()));
				JOptionPane.showMessageDialog(this, "Key generate successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (PGPException e1) {
				JOptionPane.showMessageDialog(this, "Key generate Failure." + e1.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Key generate Failure." + e1.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Key generate Failure." + e1.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Setting");
		lblNewLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

	}
}
