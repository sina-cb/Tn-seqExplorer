package GUI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class RemoteInstall extends JFrame {

	private JFrame parentFrame = null;
	
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public RemoteInstall() {
		setTitle("Do it manually");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 805, 477);
		this.setLocationRelativeTo(parentFrame);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 779, 427);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Install BWA on your remote machine", null, panel, null);
		panel.setLayout(null);
		
		JLabel label = new JLabel("1. Download BWA source code from ");
		label.setBounds(35, 199, 185, 14);
		panel.add(label);
		
		JLabel label_1 = new JLabel("BWA website");
		label_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://sourceforge.net/projects/bio-bwa/files/"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		label_1.setForeground(Color.BLUE);
		label_1.setBounds(230, 199, 78, 14);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("Steps to install BWA on your remote machine:");
		label_2.setBounds(10, 11, 219, 14);
		panel.add(label_2);
		
		JLabel lblConnectTo = new JLabel("1. Connect to your remote machine using SSH. ");
		lblConnectTo.setBounds(20, 36, 245, 14);
		panel.add(lblConnectTo);
		
		JLabel lblyouCanUse = new JLabel("(You can use PuTTY as your SSH client)");
		lblyouCanUse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblyouCanUse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.putty.org/"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblyouCanUse.setForeground(Color.BLUE);
		lblyouCanUse.setBounds(252, 36, 204, 14);
		panel.add(lblyouCanUse);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Run BWA on your remote machine", null, panel_1, null);
	}
	
	public void setParentFrame(JFrame parent){
		this.parentFrame = parent;
		this.setLocationRelativeTo(parentFrame);
	}
}
