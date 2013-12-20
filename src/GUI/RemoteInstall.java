package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.Font;

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
	private JTextField txtBwaIndexp;
	private JTextField txtBwaAlnk;
	private JTextField txtBwaSamsef;

	/**
	 * Create the frame.
	 */
	public RemoteInstall() {
		setTitle("Create SAM file manually");
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
		setHandCursor(label_1);
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openBrowserFor("http://sourceforge.net/projects/bio-bwa/files/");
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
		
		JLabel lblyouCanUse = new JLabel("You can use WinSCP as your SSH client");
		setHandCursor(lblyouCanUse);
		lblyouCanUse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openBrowserFor("http://http://winscp.net/eng/index.php");
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
		
		JLabel lblTranferYour = new JLabel("2. Tranfer your files to the remote machine using SFTP. You will need a FNA file and a FASTQ file.");
		lblTranferYour.setBounds(10, 36, 767, 14);
		panel_1.add(lblTranferYour);
		
		JLabel lblYouCanUse = new JLabel("You can use WinSCP as your SSH client");
		setHandCursor(lblYouCanUse);
		lblYouCanUse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openBrowserFor("http://winscp.net/eng/index.php");
			}
		});
		lblYouCanUse.setForeground(Color.BLUE);
		lblYouCanUse.setBounds(20, 61, 406, 14);
		panel_1.add(lblYouCanUse);
		
		JLabel lblAfterTransfering = new JLabel("3. After transferring the files to the remote machine, run the following command using SSH.");
		lblAfterTransfering.setBounds(10, 86, 767, 14);
		panel_1.add(lblAfterTransfering);
		
		JLabel lblChangeFileNames = new JLabel("Change between the parentheses:");
		lblChangeFileNames.setBounds(20, 111, 566, 14);
		panel_1.add(lblChangeFileNames);
		
		txtBwaIndexp = new JTextField();
		txtBwaIndexp.setText("bwa index -p (Name1) (FNA file)");
		txtBwaIndexp.setEditable(false);
		txtBwaIndexp.setBounds(306, 108, 313, 20);
		panel_1.add(txtBwaIndexp);
		txtBwaIndexp.setColumns(10);
		
		JLabel lblAfterRunninng = new JLabel("4. After running the previous command, run the following command.");
		lblAfterRunninng.setBounds(10, 136, 767, 14);
		panel_1.add(lblAfterRunninng);
		
		JLabel label_6 = new JLabel("Change between the parentheses:");
		label_6.setBounds(20, 164, 566, 14);
		panel_1.add(label_6);
		
		txtBwaAlnk = new JTextField();
		txtBwaAlnk.setText("bwa aln -k 2 -n 0.001 -l 18 (Name1) (FASTQ file) > (Name2).sai");
		txtBwaAlnk.setEditable(false);
		txtBwaAlnk.setColumns(10);
		txtBwaAlnk.setBounds(306, 161, 313, 20);
		panel_1.add(txtBwaAlnk);
		
		JLabel lbltheParametersk = new JLabel("* You can modify the parameters ... if you understand their meaning adn you believe that other values may be more appropriate.");
		lbltheParametersk.setBounds(20, 189, 757, 14);
		panel_1.add(lbltheParametersk);
		
		JLabel lblFinalStep = new JLabel("5. Final step to create a SAM file is to run the next command.");
		lblFinalStep.setBounds(10, 214, 767, 14);
		panel_1.add(lblFinalStep);
		
		JLabel label_7 = new JLabel("Change between the parentheses:");
		label_7.setBounds(20, 242, 566, 14);
		panel_1.add(label_7);
		
		txtBwaSamsef = new JTextField();
		txtBwaSamsef.setText("bwa samse -f (Name3).sam (Name1) (Name2).sai (FASTQ file)");
		txtBwaSamsef.setEditable(false);
		txtBwaSamsef.setColumns(10);
		txtBwaSamsef.setBounds(306, 239, 313, 20);
		panel_1.add(txtBwaSamsef);
		
		JLabel lblYouCan_1 = new JLabel("6. Transfer the created SAM file back to your own machine.");
		lblYouCan_1.setBounds(10, 267, 767, 14);
		panel_1.add(lblYouCan_1);
		
		JLabel lblYouCan_2 = new JLabel("7. Use the created SAM file to add new libraries to the project in the \"Manage Libraries\" tab.");
		lblYouCan_2.setBounds(10, 292, 767, 14);
		panel_1.add(lblYouCan_2);
		
		JLabel label_8 = new JLabel("(?)");
		label_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(RemoteInstall.this, "The FASTQ file contains your sequence reads and you should have received it from Illumina or the sequencing facility\n"
						+ "you used. The FNA file contains the genomic DNA sequence in fastA format and you can download it for complete\n"
						+ "prokaryotic genomes from the NCBI ftp server at ftp://ftp.ncbi.nih.gov/genomes/Bacteria/");
			}
		});
		label_8.setToolTipText("Click me!");
		label_8.setForeground(Color.BLUE);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_8.setBounds(561, 31, 88, 25);
		panel_1.add(label_8);
	}
	
	private void setHandCursor(Component a){
		a.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	private void openBrowserFor(String url){
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setParentFrame(JFrame parent){
		this.parentFrame = parent;
		this.setLocationRelativeTo(parentFrame);
	}
}
