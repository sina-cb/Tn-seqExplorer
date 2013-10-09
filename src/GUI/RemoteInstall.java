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
	private JTextField txtTar;
	private JTextField txtCdBwaa;
	private JTextField txtMake;
	private JTextField txtusrsbinalternativesinstallusrbinbwa;

	/**
	 * Create the frame.
	 */
	public RemoteInstall() {
		setTitle("Do it manually");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 611, 487);
		this.setLocationRelativeTo(parentFrame);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 581, 437);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Install BWA on your remote machine", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblDownloadBwa = new JLabel("2. Download BWA source code from ");
		lblDownloadBwa.setBounds(20, 61, 185, 14);
		panel.add(lblDownloadBwa);
		
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
		label_1.setBounds(215, 61, 78, 14);
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
		
		JLabel lblYouCan = new JLabel("- You can download the file using 'wget' command like this:  wget FileURL");
		lblYouCan.setBounds(30, 86, 369, 14);
		panel.add(lblYouCan);
		
		JLabel lblUncompressThe = new JLabel("3. Uncompress the downloaded file using the example command below:");
		lblUncompressThe.setBounds(20, 111, 436, 14);
		panel.add(lblUncompressThe);
		
		txtTar = new JTextField();
		txtTar.setText("tar -jxf bwa-0.7.5a.tar.bz2");
		txtTar.setEditable(false);
		txtTar.setBounds(101, 136, 151, 20);
		panel.add(txtTar);
		txtTar.setColumns(10);
		
		JLabel lblExample = new JLabel("- Example:");
		lblExample.setBounds(30, 139, 78, 14);
		panel.add(lblExample);
		
		JLabel lblOpenThe = new JLabel("4. Open the uncompressed folder using the example below:");
		lblOpenThe.setBounds(20, 164, 436, 14);
		panel.add(lblOpenThe);
		
		JLabel label = new JLabel("- Example:");
		label.setBounds(30, 192, 78, 14);
		panel.add(label);
		
		txtCdBwaa = new JTextField();
		txtCdBwaa.setText("cd bwa-0.7.5a");
		txtCdBwaa.setEditable(false);
		txtCdBwaa.setColumns(10);
		txtCdBwaa.setBounds(101, 189, 151, 20);
		panel.add(txtCdBwaa);
		
		JLabel lblCompileThe = new JLabel("5. Compile the code using the command below:");
		lblCompileThe.setBounds(20, 217, 436, 14);
		panel.add(lblCompileThe);
		
		JLabel label_3 = new JLabel("- Example:");
		label_3.setBounds(30, 245, 78, 14);
		panel.add(label_3);
		
		txtMake = new JTextField();
		txtMake.setText("make");
		txtMake.setEditable(false);
		txtMake.setColumns(10);
		txtMake.setBounds(101, 242, 151, 20);
		panel.add(txtMake);
		
		JLabel lblInstallThe = new JLabel("6. Install the application using the example command below:");
		lblInstallThe.setBounds(20, 270, 379, 14);
		panel.add(lblInstallThe);
		
		JLabel label_4 = new JLabel("- Example:");
		label_4.setBounds(30, 298, 78, 14);
		panel.add(label_4);
		
		txtusrsbinalternativesinstallusrbinbwa = new JTextField();
		txtusrsbinalternativesinstallusrbinbwa.setText("/usr/sbin/alternatives --install /usr/bin/bwa bwa (Uncompressed Directory) 20000");
		txtusrsbinalternativesinstallusrbinbwa.setEditable(false);
		txtusrsbinalternativesinstallusrbinbwa.setColumns(10);
		txtusrsbinalternativesinstallusrbinbwa.setBounds(101, 295, 401, 20);
		panel.add(txtusrsbinalternativesinstallusrbinbwa);
		
		JLabel lblInTheExample = new JLabel("- In the example command above, you only have to replace the (Uncompressed Directory)");
		lblInTheExample.setBounds(30, 323, 536, 14);
		panel.add(lblInTheExample);
		
		JLabel lblPathOfThe = new JLabel("with the complete path of the uncompressed folder");
		lblPathOfThe.setBounds(40, 348, 270, 14);
		panel.add(lblPathOfThe);
		
		JLabel lblBwaIs = new JLabel("7. BWA is installed");
		lblBwaIs.setBounds(20, 373, 379, 14);
		panel.add(lblBwaIs);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Run BWA on your remote machine", null, panel_1, null);
	}
	
	public void setParentFrame(JFrame parent){
		this.parentFrame = parent;
		this.setLocationRelativeTo(parentFrame);
	}
}
