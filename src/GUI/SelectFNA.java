package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.log4j.Logger;

import essgenes.ProjectInfo;

public class SelectFNA extends JFrame {

	private Logger logger = Logger.getLogger(SelectFNA.class.getName());
	private static final long serialVersionUID = 5257955296700178709L;
	private final JPanel contentPanel = new JPanel();
	private JTextField gbkPathTxt;
	private JButton browseBtn = new JButton("Browse");
	private JRadioButton downloadRdBtn = new JRadioButton("Download sequence file from NCBI server");
	private JComboBox<String> firstLevelCombo = new JComboBox<String>();
	private JComboBox<String> secondLevelCombo = new JComboBox<String>();
	private JButton downloadBtn = new JButton("Download");
	private JRadioButton previousRdBtn = new JRadioButton("Use previously downloaded file from NCBI");
	private ProjectInfo info = null;
	private Thread secondLevelThread = null;
	private JProgressBar progressBar = new JProgressBar();
	private JFrame parent = null;

	/**
	 * Create the dialog.
	 */
	public SelectFNA(ProjectInfo info, final JFrame parent, final JTextField textfield) {
		setResizable(false);
		this.info = info;
		this.parent = parent;
		setBounds(100, 100, 697, 281);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("(?)");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(SelectFNA.this, "DNA sequence in GenBank format");
			}
		});
		label.setToolTipText("Click me!");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label.setBounds(116, 12, 88, 14);
		contentPanel.add(label);
		
		JLabel label_1 = new JLabel("Sequence file:");
		label_1.setBounds(10, 11, 157, 14);
		contentPanel.add(label_1);
		previousRdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				previousRdBtn.setSelected(true);

				firstLevelCombo.setEnabled(false);
				secondLevelCombo.setEnabled(false);
				downloadBtn.setEnabled(false);

				gbkPathTxt.setEnabled(true);
				browseBtn.setEnabled(true);
			}
		});

		previousRdBtn.setBounds(20, 32, 352, 23);
		contentPanel.add(previousRdBtn);
		
		gbkPathTxt = new JTextField();
		gbkPathTxt.setEditable(false);
		gbkPathTxt.setColumns(10);
		gbkPathTxt.setBounds(30, 63, 508, 20);
		contentPanel.add(gbkPathTxt);
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath()
						.toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setFileFilter(new FileNameExtensionFilter("FASTQ Files (.fna)", "fna"));
				int result = fileChooser.showOpenDialog(parent);

				if(result == JFileChooser.APPROVE_OPTION){
					SelectFNA.this.gbkPathTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}else{
					return;
				}
			}
		});
		
		browseBtn.setBounds(550, 61, 125, 23);
		contentPanel.add(browseBtn);
		downloadRdBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				firstLevelCombo.setEnabled(true);
				secondLevelCombo.setEnabled(true);
				downloadBtn.setEnabled(true);

				gbkPathTxt.setEnabled(false);
				browseBtn.setEnabled(false);

				if (firstLevelCombo.getItemCount() == 0) {
					firstLevelCombo.addItem("Downloading bacterias list, please wait...");

					(new Thread(new Runnable() {
						@Override
						public void run() {
							// Closed
							FTPClient ftp = new FTPClient();
							try {
								ftp.connect("ftp.ncbi.nih.gov");
								ftp.enterLocalPassiveMode();
								ftp.login("anonymous", "");
								FTPFile[] files = ftp.listFiles("/genomes/archive/old_genbank/Bacteria/");

								firstLevelCombo.removeAllItems();
								for (FTPFile t : files) {
									if (!t.getName().contains(".")) {
										firstLevelCombo.addItem(t.getName());
									}
								}
								secondLevelCombo.addItem("Please Choose Your bacteria First");
								ftp.disconnect();
							} catch (IOException e) {
								logger.error(e.getMessage());
								return;
							}
						}
					})).start();
				}
			}
		});

		downloadRdBtn.setBounds(22, 94, 401, 23);
		contentPanel.add(downloadRdBtn);
		firstLevelCombo.addItemListener(new ItemListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					secondLevelCombo.removeAllItems();
					secondLevelCombo.addItem("Retieving Files List, Please Wait");

					if (secondLevelThread != null){
						secondLevelThread.run();
						secondLevelThread.stop();
						secondLevelThread = null;
					}
					
					secondLevelThread = new Thread(new Runnable() {
						private FTPClient ftp = null;
						
						@Override
						public void run() {
							// Closed
							if (ftp != null){
								try {
									ftp.disconnect();
								} catch (IOException e) {
									e.printStackTrace();
								}
								return;
							}
							
							ftp = new FTPClient();
							try {
								ftp.connect("ftp.ncbi.nih.gov");
								ftp.enterLocalPassiveMode();
								ftp.login("anonymous", "");
								String ftpDir = "/genomes/archive/old_genbank/Bacteria/" + firstLevelCombo.getSelectedItem() + "/";
								FTPFile[] files = ftp.listFiles(ftpDir);
								ftp.disconnect();

								secondLevelCombo.removeAllItems();
								for (FTPFile t : files) {
									if (secondLevelThread == null){
										secondLevelCombo.removeAllItems();
										System.out.println("Break");
										break;
									}
									
									if (t.getName().endsWith(".fna")) {
										String fileName = "";
										String bacteriaName = "";
										String lengthString = "";
										String line = "";

										fileName = t.getName().substring(0, t.getName().length() - 4);

										ftp.connect("ftp.ncbi.nih.gov");
										ftp.enterLocalPassiveMode();
										ftp.login("anonymous", "");
										InputStream stream = ftp.retrieveFileStream(ftpDir + fileName + ".gbk");
										BufferedReader ftpBr = new BufferedReader(new InputStreamReader(stream));

										line = ftpBr.readLine();
										lengthString = line.split("\\s+")[2];

										line = ftpBr.readLine();

										String tempLine = ftpBr.readLine();
										if (!tempLine.startsWith("ACCESSION")){
											line = line + " " + tempLine.replaceAll("\\s+", "");
										}

										String[] array = line.split("\\s+");
										for (int i = 1; i < array.length; i++) {
											bacteriaName = bacteriaName + array[i] + " ";
										}

										String itemString = String.format("%s: %s (%s bp)", fileName, bacteriaName, lengthString);
										secondLevelCombo.addItem(itemString);

										ftp.disconnect();
									}
									
									if (secondLevelThread == null){
										secondLevelCombo.removeAllItems();
										break;
									}
								}
								
								if (secondLevelThread != null){
									secondLevelCombo.setEnabled(true);
								}
								
							} catch (Exception e1) {
								logger.error(e1.getMessage());
								return;
							}
						}
					});
					
					secondLevelThread.start();
				}
			}
		});
		
		firstLevelCombo.setEnabled(false);
		firstLevelCombo.setBounds(32, 124, 506, 20);
		contentPanel.add(firstLevelCombo);
		
		secondLevelCombo.setEnabled(false);
		secondLevelCombo.setBounds(32, 155, 506, 20);
		contentPanel.add(secondLevelCombo);
		downloadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				downloadBtn.setText("Wait");
				downloadBtn.setEnabled(false);
				downloadBtn.setVisible(false);
				progressBar.setVisible(true);
				progressBar.setValue(0);

				(new Thread(new Runnable() {
					@Override
					public void run() {
						// Closed
						FTPClient ftp = new FTPClient();
						try {
							ftp.connect("ftp.ncbi.nih.gov");
							ftp.enterLocalPassiveMode();
							ftp.login("anonymous", "");
							String ftpDir = "/genomes/archive/old_genbank/Bacteria/" + firstLevelCombo.getSelectedItem() + "/";
							FTPFile[] files = ftp.listFiles(ftpDir);

							File gbkTemp = null;
							String fileName = (String) secondLevelCombo.getSelectedItem();
							fileName = fileName.substring(0, fileName.indexOf(":"));
							for (FTPFile t : files) {
								if (t.getName().contains(fileName)) {
									if (t.getName().endsWith("fna")) {
										gbkTemp = new File(SelectFNA.this.info.getPath() + t.getName());
										OutputStream fos = new FileOutputStream(gbkTemp);
										final long fileSize = t.getSize();
										CopyStreamAdapter csa = new CopyStreamAdapter() {
											boolean change = true;

											@Override
											public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
												int percent = (int) (totalBytesTransferred * 100 / (double) fileSize);
												if (percent % 5 == 0) {
													if (change) {
														change = false;
														progressBar.setValue(percent);
													}
												} else {
													change = true;
												}
											}
										};
										ftp.setCopyStreamListener(csa);
										ftp.retrieveFile(ftpDir + t.getName(), fos);
										fos.close();
										gbkPathTxt.setText(gbkTemp.getAbsolutePath());
									}
								}
							}
							ftp.disconnect();

							downloadBtn.setText("Download");
							downloadBtn.setVisible(true);
							downloadBtn.setEnabled(true);
							progressBar.setVisible(false);
							firstLevelCombo.setEnabled(true);
							secondLevelCombo.setEnabled(true);

						} catch (IOException e) {
							logger.error(e.getMessage());
							downloadBtn.setEnabled(true);
							downloadBtn.setText("Try again");
							return;
						}
					}
				})).start();
			}
		});
		
		downloadBtn.setEnabled(false);
		downloadBtn.setBounds(550, 154, 125, 23);
		contentPanel.add(downloadBtn);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 197, 665, 2);
		contentPanel.add(separator);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if (gbkPathTxt.getText() == null || gbkPathTxt.getText().equals("")){
							JOptionPane.showMessageDialog(SelectFNA.this, "Please select a previously downloaded Sequence File or download one from NCBI server.");
							return;
						}
						
						textfield.setText(gbkPathTxt.getText());
						SelectFNA.this.setVisible(false);
						SelectFNA.this.dispose();
						
						SelectFNA.this.parent.setVisible(true);
						JOptionPane.showMessageDialog(SelectFNA.this.parent, "FNA file selected.");
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		ButtonGroup group1 = new ButtonGroup();
		group1.add(downloadRdBtn);
		group1.add(previousRdBtn);
		
		previousRdBtn.setSelected(true);
		
		progressBar.setStringPainted(true);
		progressBar.setBounds(550, 161, 125, 14);
		contentPanel.add(progressBar);
		
		setLocationRelativeTo(parent);
	}
	
	public void setFNAPath(String fnaPath){
		gbkPathTxt.setText(fnaPath);
	}

}
