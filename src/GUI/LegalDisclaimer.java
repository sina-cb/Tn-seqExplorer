package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXLabel;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LegalDisclaimer extends JFrame {

	private JPanel contentPane;
	JXLabel lblNewLabel = new JXLabel("New label");
	
	public LegalDisclaimer() {
		setTitle("Legal Disclaimer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LegalDisclaimer.class.getResource("/resources/legaldisclaimer.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 722, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		lblNewLabel.setBounds(120, 26, 558, 79);
		contentPane.add(lblNewLabel);
		
		String text = String.format("By using this software, you agree to the following:");
		text = String.format("%s\n\n    (a) This software is distributed \"as is\" with no warranty or support, without even the implied warranty of", text);
		text = String.format("%s\n      merchantability or fitness for a particular purpose.", text);
		text = String.format("%s\n\n    (b) The authors shall not be liable for any claim, damages, or other liability in connection with this software.", text);
		
		lblNewLabel.setText(text);
		lblNewLabel.setLineWrap(true);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(LegalDisclaimer.class.getResource("/resources/legaldisclaimer.png")));
		lblNewLabel_1.setBounds(10, 11, 100, 100);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Agree");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorkspaceChooser ws = new WorkspaceChooser();
				ws.setVisible(true);
				
				LegalDisclaimer.this.setVisible(false);
				LegalDisclaimer.this.dispose();
			}
		});
		btnNewButton.setBounds(604, 116, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(505, 116, 89, 23);
		contentPane.add(btnCancel);
	}
}
