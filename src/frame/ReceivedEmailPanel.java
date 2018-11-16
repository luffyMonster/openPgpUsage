package frame;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.Font;
import com.jgoodies.forms.layout.FormLayout;
import com.didisoft.pgp.PGPException;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import app.App;
import controller.EmailReceive;
import utils.EmailUtil;

import com.jgoodies.forms.layout.FormSpecs;

import javax.mail.Message;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class ReceivedEmailPanel extends JPanel {
	private JTable table;
	Message[] ms;
	JTextArea textArea;
	String privateKeyPassword = "abcdef";
	App app = App.instance();
	/**
	 * Create the panel.
	 */
	public ReceivedEmailPanel() {
		setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Received Email");
		lblNewLabel.setFont(new Font("Ubuntu", Font.BOLD, 19));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("67dlu:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		JLabel lblInbox = new JLabel("Inbox");
		panel.add(lblInbox, "2, 2");

		JButton btnSync = new JButton("Synchronize");
		btnSync.addActionListener((event) -> {
			App app = App.instance();
			lblInbox.setText("Inbox: " + app.config.getEmail());
			EmailReceive receiver = new EmailReceive();
			try {
				
				String[] tile = new String[] { "No", "Send At", "Received At", "From", "Subject", "Type"};
				DefaultTableModel model = new DefaultTableModel();
				
				ms = receiver.receiveEmail(app.config.getEmail(), new String(app.config.getPassword()));
				ArrayList<ArrayList<String>> arrArr = EmailUtil.getListDetailMessage(ms);
				
		        Object[][] data = new Object[arrArr.size()][1];
		        int j = 0;
		        for(ArrayList<String> arr: arrArr){         
		            Object[] ob = new Object[arr.size()];
		            for(int k = 0; k<arr.size(); k++){               
		                ob[k] = arr.get(k);          
		            }
		            data[j]=ob;
		            j++;
		        }
		        model = new DefaultTableModel(data, tile);
				table.setModel(model);
			} catch (Exception e1) {
				e1.printStackTrace();
				String message = e1.getMessage();
				if (message == null || message == "" ) {
					message = "Received failed!";
				}
				JOptionPane.showMessageDialog(this, message, "Failure!",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		
		panel.add(btnSync, "4, 2, right, default");
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "1, 4, 4, 1, default, fill");
		table = new JTable() {
			  @Override
		       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		           Component component = super.prepareRenderer(renderer, row, column);
		           int rendererWidth = component.getPreferredSize().width;
		           TableColumn tableColumn = getColumnModel().getColumn(column);
		           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
		           return component;
		        }
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText("Waiting...");
				int row = table.getSelectedRow();
				if (ms != null && ms.length > row) {
					try {
						textArea.setText(EmailUtil.getContent(ms[row], app.config.getPrivateKeyFile(), privateKeyPassword, findPublicKey(ms[row].getFrom()[0].toString() )));
					}  catch (Exception ex) {
						textArea.setText("Descryption error: \n" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		table.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {

	            },
	            new String[] { "No", "Send At", "Received At", "From", "Subject", "Type"}
	        ));
		
		JScrollBar scrollBar = new JScrollBar();
		scrollPane.setRowHeaderView(scrollBar);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2, "2, 6, 3, 1, fill, fill");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_2.setViewportView(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setRowHeaderView(textArea);

	}
	
	private String findPublicKey(String user) {
		String path = app.config.getPublicFolder() + "/" + user + "/publickey.pkr";
		File file = new File(path);
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		return "";
	}
}
