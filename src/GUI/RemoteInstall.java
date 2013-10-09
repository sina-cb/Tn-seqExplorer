package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RemoteInstall extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame parentFrame = null;
	
	private JPanel contentPane;
	private JTextField txtTar;
	private JTextField txtCdBwaa;
	private JTextField txtMake;
	private JTextField txtusrsbinalternativesinstallusrbinbwa;
	private JTextField txtWgetFileurl;

	/**
	 * Create the frame.
	 */
	public RemoteInstall() {
		setTitle("Do it manually");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 818, 577);
		this.setLocationRelativeTo(parentFrame);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 792, 526);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Install BWA on your remote machine", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblDownloadBwa = new JLabel("2. Download BWA source code from  their website");
		lblDownloadBwa.setBounds(20, 86, 680, 14);
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
		label_1.setBounds(40, 111, 244, 14);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("Steps to install BWA on your remote machine:");
		label_2.setBounds(10, 11, 633, 14);
		panel.add(label_2);
		
		JLabel lblConnectTo = new JLabel("1. Connect to your remote machine using SSH. ");
		lblConnectTo.setBounds(20, 36, 690, 14);
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
		lblyouCanUse.setBounds(40, 61, 406, 14);
		panel.add(lblyouCanUse);
		
		JLabel lblYouCan = new JLabel("- You can download the file using 'wget' command like this, ");
		lblYouCan.setBounds(30, 139, 680, 14);
		panel.add(lblYouCan);
		
		JLabel lblUncompressThe = new JLabel("3. Uncompress the downloaded file using the example command below:");
		lblUncompressThe.setBounds(20, 195, 747, 14);
		panel.add(lblUncompressThe);
		
		txtTar = new JTextField();
		txtTar.setText("tar -jxf bwa-0.7.5a.tar.bz2");
		txtTar.setEditable(false);
		txtTar.setBounds(159, 220, 151, 20);
		panel.add(txtTar);
		txtTar.setColumns(10);
		
		JLabel lblExample = new JLabel("- Example:");
		lblExample.setBounds(30, 223, 78, 14);
		panel.add(lblExample);
		
		JLabel lblOpenThe = new JLabel("4. Open the uncompressed folder using the example below:");
		lblOpenThe.setBounds(20, 248, 436, 14);
		panel.add(lblOpenThe);
		
		JLabel label = new JLabel("- Example:");
		label.setBounds(30, 276, 78, 14);
		panel.add(label);
		
		txtCdBwaa = new JTextField();
		txtCdBwaa.setText("cd bwa-0.7.5a");
		txtCdBwaa.setEditable(false);
		txtCdBwaa.setColumns(10);
		txtCdBwaa.setBounds(159, 273, 151, 20);
		panel.add(txtCdBwaa);
		
		JLabel lblCompileThe = new JLabel("5. Compile the code using the command below:");
		lblCompileThe.setBounds(20, 301, 436, 14);
		panel.add(lblCompileThe);
		
		JLabel label_3 = new JLabel("- Example:");
		label_3.setBounds(30, 329, 78, 14);
		panel.add(label_3);
		
		txtMake = new JTextField();
		txtMake.setText("make");
		txtMake.setEditable(false);
		txtMake.setColumns(10);
		txtMake.setBounds(159, 326, 151, 20);
		panel.add(txtMake);
		
		JLabel lblInstallThe = new JLabel("6. Install the application using the example command below:");
		lblInstallThe.setBounds(20, 354, 536, 14);
		panel.add(lblInstallThe);
		
		JLabel label_4 = new JLabel("- Example:");
		label_4.setBounds(30, 382, 78, 14);
		panel.add(label_4);
		
		txtusrsbinalternativesinstallusrbinbwa = new JTextField();
		txtusrsbinalternativesinstallusrbinbwa.setText("/usr/sbin/alternatives --install /usr/bin/bwa bwa (Uncompressed Directory) 20000");
		txtusrsbinalternativesinstallusrbinbwa.setEditable(false);
		txtusrsbinalternativesinstallusrbinbwa.setColumns(10);
		txtusrsbinalternativesinstallusrbinbwa.setBounds(159, 379, 401, 20);
		panel.add(txtusrsbinalternativesinstallusrbinbwa);
		
		JLabel lblInTheExample = new JLabel("- In the example command above, you only have to replace the (Uncompressed Directory)");
		lblInTheExample.setBounds(30, 407, 737, 14);
		panel.add(lblInTheExample);
		
		JLabel lblPathOfThe = new JLabel("with the complete path of the uncompressed folder");
		lblPathOfThe.setBounds(40, 432, 466, 14);
		panel.add(lblPathOfThe);
		
		JLabel lblBwaIs = new JLabel("7. BWA is installed");
		lblBwaIs.setBounds(20, 457, 436, 14);
		panel.add(lblBwaIs);
		
		txtWgetFileurl = new JTextField();
		txtWgetFileurl.setEditable(false);
		txtWgetFileurl.setText("wget FileURL");
		txtWgetFileurl.setBounds(159, 164, 151, 20);
		panel.add(txtWgetFileurl);
		txtWgetFileurl.setColumns(10);
		
		JLabel label_5 = new JLabel("- Example:");
		label_5.setBounds(40, 167, 78, 14);
		panel.add(label_5);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Run BWA on your remote machine", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblAfter = new JLabel("1. Make sure you have the BWA program installed");
		lblAfter.setBounds(10, 11, 767, 14);
		panel_1.add(lblAfter);
		
		JLabel lblTranferYour = new JLabel("2. Tranfer your files to the remote machine");
		lblTranferYour.setBounds(10, 36, 767, 14);
		panel_1.add(lblTranferYour);
	}
	
	public void setParentFrame(JFrame parent){
		this.parentFrame = parent;
		this.setLocationRelativeTo(parentFrame);
	}
}
