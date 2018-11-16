package frame;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import app.App;
import controller.EmailSender;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewEmailPanel extends JPanel {
	private JTextField txtTo;
	private JTextField txtTitle;
	private JTextArea txtBody;
	private JCheckBox chkEncrypt;
	private JCheckBox chkSign;
	/**
	 * Create the panel.
	 */
	public NewEmailPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Send");
		
		App app = App.instance();
		btnNewButton.addActionListener((e)-> {
				int pgpMode = EmailSender.NO_ENCRYPT_AND_SIGN;
				if (chkSign.isSelected() && chkEncrypt.isSelected()) {
					pgpMode = EmailSender.SIGN_AND_ENCRYPT_MODE;
				} else if (chkSign.isSelected()) {
					pgpMode = EmailSender.ONLY_SIGN_MODE;
				} else if (chkEncrypt.isSelected()) {
					pgpMode = EmailSender.ONLY_ENCRYPT_MODE;
				}
				EmailSender sender = new EmailSender(app.config.getEmail(), app.config.getPassword());
				try {
					sender.send(txtTo.getText(), txtTitle.getText(), txtBody.getText(), pgpMode, app.config.getPrivateKeyFile(), app.config.getPublicFolder());
					JOptionPane.showMessageDialog(this, "Send email successfully!", "Successfully!", JOptionPane.INFORMATION_MESSAGE);
					txtTo.setText("");
					txtBody.setText("");
					txtTitle.setText("");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(this, "Send failed! " + e2.getMessage(), "Failure!", JOptionPane.ERROR_MESSAGE); 
				}
				
		});
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("71px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("337px:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("19px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("19px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("15px:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("15px:grow"),}));
		
		JLabel lblTo = DefaultComponentFactory.getInstance().createLabel("To");
		panel_1.add(lblTo, "2, 2, right, center");
		
		txtTo = new JTextField();
		panel_1.add(txtTo, "4, 2, fill, top");
		txtTo.setColumns(10);
		
		JLabel lblTitle = DefaultComponentFactory.getInstance().createLabel("Title");
		panel_1.add(lblTitle, "2, 4, right, center");
		
		txtTitle = new JTextField();
		panel_1.add(txtTitle, "4, 4, fill, top");
		txtTitle.setColumns(10);
		
		JLabel lblBody = DefaultComponentFactory.getInstance().createLabel("Body");
		panel_1.add(lblBody, "2, 6, right, top");
		
		txtBody = new JTextArea();
		panel_1.add(txtBody, "4, 6, fill, fill");
		
		JLabel lblPgpMode = DefaultComponentFactory.getInstance().createLabel("PGP Mode");
		panel_1.add(lblPgpMode, "2, 8, left, top");
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "4, 8, center, center");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("92px"),
				ColumnSpec.decode("114px"),
				ColumnSpec.decode("144px"),},
			new RowSpec[] {
				RowSpec.decode("31px"),
				RowSpec.decode("23px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		chkEncrypt = new JCheckBox("Encrypt");
		panel_2.add(chkEncrypt, "2, 1, fill, center");
		
		chkSign = new JCheckBox("Sign");
		panel_2.add(chkSign, "2, 2, left, center");
		
		JLabel lblNewLabel = new JLabel("Email Sender");
		lblNewLabel.setFont(new Font("Ubuntu", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

	}

}
