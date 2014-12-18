package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXLabel;
import org.jfree.chart.ChartPanel;

import CustomGUIComponent.IntegerInputVerifier;
import essgenes.Messages;
import essgenes.MyFileUtil;
import essgenes.PlotData;
import essgenes.PrepareFiles;
import essgenes.ProjectInfo;
import essgenes.StatisticsHelper;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final String ProgTitle = "Tn-seq explorer";
	public static final String ProgVersion = "v1.3";

	private JButton bowtieSamCreateBtn = new JButton("Create SAM file");
	private boolean if_initialize_step_1 = true;
	private JLabel label_10 = new JLabel("(?)");
	private JLabel lblReads = new JLabel("reads.");
	private JButton btnAnotherPlot = new JButton("Distribution of reads per unique insertion");
	private JLabel lblWindowLength = new JLabel("Window length:");
	private JLabel lblRemoveUniqueInsertions = new JLabel("Remove unique insertions with no more than");
	private JLabel errorMsgLbl = new JLabel("Please enter a valid Integer value in the red fields.");
	private JButton applySequenceBtn = new JButton("Apply");
	private JLabel lblPleaseEnterAn = new JLabel("Please enter a valid Integer value.");
	private JLabel winlenLbl = new JLabel("Testing Winlen = 500");
	private JButton btnOptimal = new JButton("Recommend optimal Window Length");
	private JLabel sequenceLengthLbl = new JLabel("0");
	private JTextField sequenceLenTxt;
	private JLabel libraryCountLbl = new JLabel("0");
	private JTextField samFilePathTxt;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JButton browseForSamBtn = new JButton("Browse");
	private JButton extractInsBtn = new JButton("Extract");
	private JButton cancelLibSaveBtn = new JButton("Clear the form");
	private JLabel extractInsLbl = new JLabel("Extract insertion positions from the \"SAM\" file");
	private JLabel sortUniLbl = new JLabel("Sort unique insertions by the number of reads");
	private JLabel sortInsLbl = new JLabel("Sort the extracted insertion positions");
	private JLabel countUniLbl = new JLabel("Count unique insertions and sort by position");
	private JButton removeLibBtn = new JButton("Remove");
	private JButton renameLibBtn = new JButton("Rename");
	private JComboBox<String> libraryComboBox = new JComboBox<String>();
	private JLabel loadingLbl = new JLabel("");
	private JTextField pttFileTxt;
	private JTextField rntFileTxt;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JComboBox<String> ftpFirstLevelCombo = new JComboBox<String>();
	private JComboBox<String> ftpSecondLevelCombo = new JComboBox<String>();
	private JButton downloadBtn = new JButton("Download");
	private JButton pttBrowseBtn = new JButton("Browse");
	private JButton rntBrowseBtn = new JButton("Browse");
	private JButton btnPrepareGeneFile = new JButton("Prepare gene file");
	private JRadioButton ownRadioBtn = new JRadioButton("Use previously downloaded files from NCBI");
	private JRadioButton databaseRadioBtn = new JRadioButton("Download annotation from NCBI FTP server");
	private JLabel geneFileNameLbl = new JLabel("NONE");
	private JComboBox<String> dataTableCombo = new JComboBox<String>();
	private JButton dataTableRenameBtn = new JButton("Rename");
	private JButton dataTableRemoveBtn = new JButton("Remove");
	private JButton openAsSpreadsheetBtn = new JButton("Open as spreadsheet");
	private JButton addNewDataTableBtn = new JButton("Create new");
	private JButton replaceXlsBtn = new JButton("Replace");
	private JLabel dataTableCountLbl = new JLabel("0");
	private JLabel doneLbl1 = new JLabel("");
	private JLabel doneLbl2 = new JLabel("");
	private JLabel doneLbl3 = new JLabel("");
	private JLabel doneLbl4 = new JLabel("");
	private JButton tableCancelChangeBtn = new JButton("Cancel");
	private JRadioButton imgRadioBtn = new JRadioButton("Use previously downloaded file from IMG");
	private JTextField imgFileTxt;
	private JButton imgBrowseBtn = new JButton("Browse");
	private JComboBox<String> scaffoldCombo = new JComboBox<String>();
	private JRadioButton alreadyInstalledRadio = new JRadioButton("Already installed");
	private JRadioButton useScriptsRadio = new JRadioButton("Use the shell scripts to install them");
	private JButton bwaInstallBtn = new JButton("Install BWA (v0.7.5a)");	
	private JLabel infoLbl = new JLabel("* You have to provide the chromosome length and gene annotation to start a new project.");
	private JComboBox<String> plotLibraryCombo = new JComboBox<String>();
	private JTextField winLenTxt;
	private JTextField winStepTxt;
	private JButton plotBtn = new JButton("Plot");
	private JLabel plotWaitLbl = new JLabel("Please wait...");
	private JButton addNewIndicesBtn = new JButton("Add new data to the selected table");
	private JButton remoteHelpBtn = new JButton("Create SAM file manually");
	private JButton maxNumInsBtn = new JButton("Find essential regions");
	private JRadioButton countOnlyUniqueRadio = new JRadioButton("Count only unique insertions");
	private JRadioButton countAllReadsRadio = new JRadioButton("Count all sequence reads");

	protected ProjectInfo projectInfo = new ProjectInfo();
	private Logger logger = Logger.getLogger(MainFrame.class.getName());
	private boolean hasSeqNum = false;
	private boolean hasGeneFile = false;
	private JTextField fastqFilePath;
	private JTextField newSamNameTxt;
	private JTextField samFilePath;
	private JTextField maxNumInsTxt;
	private Thread second_thread = null;
	private JTextField removeUniqueInsertionsTxt;
	private JTextField bowtieFastqTxt;
	private JTextField bowtieFnaTxt;
	private JTextField bowtieSamLocTxt;
	private JTextField bowtieSamFilenameTxt;
	private JTextField fnaFilePath;

	/**
	 * Create the frame.
	 */
	public MainFrame(String projectPath) {
		setResizable(false);
		this.projectInfo.setPath(projectPath);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 859, 732);
		setLocationRelativeTo(null);	
		getContentPane().setLayout(null);

		tabbedPane.setBounds(12, 13, 828, 659);
		getContentPane().add(tabbedPane);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {

				int selectedTab = tabbedPane.getSelectedIndex();

				if (selectedTab == 0){

				}

				if (selectedTab == 1){
					initiateLibraryComboBox();
				}

				if(selectedTab == 2){
					initiateDataTablesComboBox();
				}

				if(selectedTab == 3){
					initializeBWAPanel();
				}
			}
		});

		ButtonGroup numberOfReads = new ButtonGroup();
		
				JPanel panelMain = new JPanel();
				tabbedPane.addTab("Main", null, panelMain, null);
				panelMain.setLayout(null);
				
						JLabel lblSequenceLength = new JLabel("Sequence Length (bp):");
						lblSequenceLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
						lblSequenceLength.setBounds(12, 31, 212, 20);
						panelMain.add(lblSequenceLength);
						
								sequenceLenTxt = new JTextField();
								sequenceLenTxt.addFocusListener(new FocusAdapter() {
									@Override
									public void focusGained(FocusEvent arg0) {
										highlightAllTheText((JTextField) arg0.getComponent());
									}
								});
								sequenceLenTxt.setToolTipText("Enter sequence length to be used in this project");
								sequenceLenTxt.setBounds(231, 33, 150, 22);
								panelMain.add(sequenceLenTxt);
								sequenceLenTxt.setColumns(10);
								
								sequenceLenTxt.setInputVerifier(new IntegerInputVerifier(lblPleaseEnterAn));
								
										applySequenceBtn.setToolTipText("Apply the entered sequence length");
										applySequenceBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
										applySequenceBtn.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent arg0) {
												applySequenceNumber();
											}
										});
										applySequenceBtn.setBounds(391, 31, 97, 25);
										panelMain.add(applySequenceBtn);
										
												JSeparator separator = new JSeparator();
												separator.setBounds(0, 62, 823, 2);
												panelMain.add(separator);
												
														JLabel lblProjectInformation = new JLabel("Project Information:");
														lblProjectInformation.setFont(new Font("Tahoma", Font.PLAIN, 15));
														lblProjectInformation.setBounds(12, 550, 144, 20);
														panelMain.add(lblProjectInformation);
														
																JLabel lblLibrariesCount = new JLabel("Libraries count:");
																lblLibrariesCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
																lblLibrariesCount.setBounds(166, 555, 163, 22);
																panelMain.add(lblLibrariesCount);
																
																		libraryCountLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
																		libraryCountLbl.setBounds(317, 550, 51, 33);
																		panelMain.add(libraryCountLbl);
																		
																				JLabel lblDataTablesCount = new JLabel("Data tables count:");
																				lblDataTablesCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
																				lblDataTablesCount.setBounds(166, 592, 163, 22);
																				panelMain.add(lblDataTablesCount);
																				
																						dataTableCountLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
																						dataTableCountLbl.setBounds(317, 587, 51, 33);
																						panelMain.add(dataTableCountLbl);
																						
																								ftpFirstLevelCombo.setBounds(12, 110, 695, 22);
																								
																										panelMain.add(ftpFirstLevelCombo);
																										ftpSecondLevelCombo.setEnabled(false);
																										
																												ftpSecondLevelCombo.setBounds(12, 145, 695, 22);
																												panelMain.add(ftpSecondLevelCombo);
																												downloadBtn.addActionListener(new ActionListener() {
																													public void actionPerformed(ActionEvent arg0) {

																														String temp = (String) ftpSecondLevelCombo.getSelectedItem();
																														temp = temp.substring(temp.indexOf("(") + 1);
																														temp = temp.substring(0, temp.indexOf(")"));
																														temp = temp.replaceAll(" bp", "");

																														int tempInt = Integer.parseInt(temp);
																														if (projectInfo.getSequenceLen() != 0 && projectInfo.getSequenceLen() != tempInt){

																															String msg = String.format("The sequence length in GenBank is %d.\nDo you want to use the GenBank length or the length you supplied manually?", tempInt);
																															int answer = JOptionPane.showOptionDialog(MainFrame.this, 
																																	msg, 
																																	"Warning", 
																																	JOptionPane.YES_NO_OPTION, 
																																	JOptionPane.WARNING_MESSAGE, 
																																	null, 
																																	new String[]{"GenBank", "Manual"}, // this is the array
																																	"default");

																															if (answer == JOptionPane.YES_OPTION){
																																sequenceLenTxt.setText(tempInt + "");
																																applySequenceNumber();

																															}else{
																																//Do Nothing
																															}

																														}else if (projectInfo.getSequenceLen() == 0 && projectInfo.getSequenceLen() != tempInt){
																															sequenceLenTxt.setText(tempInt + "");
																															applySequenceNumber();
																														}

																														downloadBtn.setText("Downloading");

																														(new Thread(new Runnable(){
																															@Override
																															public void run() {
																																FTPClient ftp = new FTPClient();
																																try {
																																	ftp.connect("ftp.ncbi.nih.gov");
																																	ftp.enterLocalPassiveMode();
																																	ftp.login("anonymous", "");
																																	String ftpDir = "/genomes/Bacteria/" + ftpFirstLevelCombo.getSelectedItem() + "/";
																																	FTPFile[] files = ftp.listFiles(ftpDir);

																																	for (FTPFile t : files){
																																		String fileName = (String)ftpSecondLevelCombo.getSelectedItem();
																																		fileName = fileName.substring(0, fileName.indexOf(":"));

																																		if(t.getName().contains(fileName)){
																																			if(t.getName().endsWith("ptt")){
																																				File pttTemp = new File(projectInfo.getPath() + t.getName());
																																				FileOutputStream fos = new FileOutputStream(pttTemp);
																																				ftp.retrieveFile(ftpDir + t.getName(), fos);
																																				pttFileTxt.setText(pttTemp.getAbsolutePath());
																																			}else if(t.getName().endsWith("rnt")){
																																				File rntTemp = new File(projectInfo.getPath() + t.getName());
																																				FileOutputStream fos = new FileOutputStream(rntTemp);
																																				ftp.retrieveFile(ftpDir + t.getName(), fos);
																																				rntFileTxt.setText(rntTemp.getAbsolutePath());
																																			}

																																		}
																																	}

																																	downloadBtn.setText("Download");
																																	downloadBtn.setEnabled(true);
																																	ftpFirstLevelCombo.setEnabled(true);
																																	ftpSecondLevelCombo.setEnabled(true);

																																} catch (IOException e) {
																																	logger.error(e.getMessage());
																																	return;
																																}

																																prepareGeneFile();
																															}
																														})).start();

																													}
																												});
																												
																														downloadBtn.setBounds(716, 144, 97, 25);
																														panelMain.add(downloadBtn);
																														
																																JLabel lblPttFile = new JLabel("PTT File:");
																																lblPttFile.setBounds(12, 235, 79, 16);
																																panelMain.add(lblPttFile);
																																
																																		JLabel lblRntFile = new JLabel("RNT File:");
																																		lblRntFile.setBounds(12, 267, 79, 16);
																																		panelMain.add(lblRntFile);
																																		
																																				pttFileTxt = new JTextField();
																																				pttFileTxt.setBounds(85, 232, 622, 22);
																																				panelMain.add(pttFileTxt);
																																				pttFileTxt.setColumns(10);
																																				
																																						rntFileTxt = new JTextField();
																																						rntFileTxt.setColumns(10);
																																						rntFileTxt.setBounds(85, 264, 622, 22);
																																						panelMain.add(rntFileTxt);
																																						pttBrowseBtn.addActionListener(new ActionListener() {
																																							public void actionPerformed(ActionEvent arg0) {

																																								Path currentRelativePath = Paths.get("");
																																								String location = currentRelativePath.toAbsolutePath()
																																										.toString();
																																								JFileChooser fileChooser = new JFileChooser(location);
																																								fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
																																								fileChooser.setFileFilter(new FileNameExtensionFilter("PTT Files (.ptt)", "ptt"));
																																								int result = fileChooser.showOpenDialog(MainFrame.this);

																																								if(result == JFileChooser.APPROVE_OPTION){
																																									pttFileTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
																																								}else{
																																									return;
																																								}

																																							}
																																						});
																																						
																																								pttBrowseBtn.setBounds(716, 231, 97, 25);
																																								panelMain.add(pttBrowseBtn);
																																								rntBrowseBtn.addActionListener(new ActionListener() {
																																									public void actionPerformed(ActionEvent arg0) {
																																										Path currentRelativePath = Paths.get("");
																																										String location = currentRelativePath.toAbsolutePath()
																																												.toString();
																																										JFileChooser fileChooser = new JFileChooser(location);
																																										fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
																																										fileChooser.setFileFilter(new FileNameExtensionFilter("RNT Files (.rnt)", "rnt"));
																																										int result = fileChooser.showOpenDialog(MainFrame.this);

																																										if(result == JFileChooser.APPROVE_OPTION){
																																											rntFileTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
																																										}else{
																																											return;
																																										}
																																									}
																																								});
																																								
																																										rntBrowseBtn.setBounds(716, 263, 97, 25);
																																										panelMain.add(rntBrowseBtn);
																																										
																																												JLabel lblOr = new JLabel("Or");
																																												lblOr.setFont(new Font("Tahoma", Font.BOLD, 16));
																																												lblOr.setBounds(12, 168, 51, 35);
																																												panelMain.add(lblOr);
																																												btnPrepareGeneFile.setToolTipText("Process files and create the .gene file");
																																												btnPrepareGeneFile.addActionListener(new ActionListener() {
																																													public void actionPerformed(ActionEvent arg0) {				

																																														prepareGeneFile();
																																													}
																																												});
																																												
																																														btnPrepareGeneFile.setFont(new Font("Tahoma", Font.BOLD, 13));
																																														btnPrepareGeneFile.setBounds(339, 450, 182, 25);
																																														panelMain.add(btnPrepareGeneFile);
																																														
																																																JSeparator separator_3 = new JSeparator();
																																																separator_3.setBounds(52, 187, 771, 2);
																																																panelMain.add(separator_3);
																																																
																																																		buttonGroup.add(databaseRadioBtn);
																																																		databaseRadioBtn.setToolTipText("Automatically download from the NCBI FTP Server");
																																																		databaseRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
																																																		databaseRadioBtn.setBounds(10, 76, 404, 25);
																																																		panelMain.add(databaseRadioBtn);	
																																																		
																																																				databaseRadioBtn.addActionListener(new ActionListener() {
																																																					@Override
																																																					public void actionPerformed(ActionEvent arg0) {
																																																						ftpFirstLevelCombo.setEnabled(true);
																																																						ftpSecondLevelCombo.setEnabled(true);
																																																						downloadBtn.setEnabled(true);
																																																		
																																																						pttBrowseBtn.setEnabled(false);
																																																						pttFileTxt.setEnabled(false);
																																																						rntBrowseBtn.setEnabled(false);
																																																						rntFileTxt.setEnabled(false);
																																																		
																																																						ftpFirstLevelCombo.removeAllItems();
																																																						ftpFirstLevelCombo.addItem("Retrieving Bacterias List, Please Wait...");
																																																						ftpSecondLevelCombo.setEnabled(false);
																																																						downloadBtn.setEnabled(false);
																																																		
																																																						pttFileTxt.setText("");
																																																						rntFileTxt.setText("");
																																																		
																																																						imgFileTxt.setEnabled(false);
																																																						imgBrowseBtn.setEnabled(false);
																																																						scaffoldCombo.setEnabled(false);
																																																		
																																																						(new Thread(new Runnable(){
																																																							@Override
																																																							public void run() {
																																																								FTPClient ftp = new FTPClient();
																																																								try {
																																																									ftp.connect("ftp.ncbi.nih.gov");
																																																									ftp.enterLocalPassiveMode();
																																																									ftp.login("anonymous", "");
																																																									FTPFile[] files = ftp.listFiles("/genomes/Bacteria/");
																																																		
																																																									ftpFirstLevelCombo.removeAllItems();
																																																									for(FTPFile t : files){
																																																										if(!t.getName().contains(".")){
																																																											ftpFirstLevelCombo.addItem(t.getName());
																																																										}
																																																										ftpSecondLevelCombo.addItem("Please Choose Your bacteria First");
																																																									}
																																																		
																																																								} catch (IOException e) {
																																																									logger.error(e.getMessage());
																																																									return;
																																																								}						
																																																							}
																																																						})).start();
																																																		
																																																					}
																																																				});
																																																				
																																																						ftpFirstLevelCombo.addItemListener(new ItemListener(){
																																																							@SuppressWarnings("deprecation")
																																																							@Override
																																																							public void itemStateChanged(ItemEvent e) {
																																																				
																																																								if(e.getStateChange() == ItemEvent.SELECTED){
																																																									ftpSecondLevelCombo.removeAllItems();
																																																									ftpSecondLevelCombo.addItem("Retieving Files List, Please Wait");
																																																				
																																																									if (second_thread != null){
																																																										second_thread.run();
																																																										second_thread.stop();
																																																										second_thread = null;
																																																									}
																																																									
																																																									second_thread = new Thread(new Runnable(){
																																																										private FTPClient ftp = null;
																																																										@Override
																																																										public void run() {
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
																																																												String ftpDir = "/genomes/Bacteria/" + ftpFirstLevelCombo.getSelectedItem() + "/";
																																																												FTPFile[] files = ftp.listFiles(ftpDir);
																																																												ftp.disconnect();
																																																				
																																																												ftpSecondLevelCombo.removeAllItems();
																																																												for(FTPFile t : files){
																																																													if(t.getName().endsWith(".ptt")){
																																																														String fileName = "";
																																																														String bacteriaName = "";
																																																														String lengthString = "";
																																																														String line = "";
																																																				
																																																														fileName = t.getName().substring(0, t.getName().length() - 4);
																																																				
																																																														ftp.connect("ftp.ncbi.nih.gov");
																																																														ftp.enterLocalPassiveMode();
																																																														ftp.login("anonymous", "");
																																																														InputStream stream = ftp.retrieveFileStream(ftpDir + fileName + ".ptt");
																																																														BufferedReader ftpBr = new BufferedReader(new InputStreamReader(stream));
																																																				
																																																														line = ftpBr.readLine();
																																																														bacteriaName = line.substring(0, line.indexOf(" -"));
																																																														lengthString = line.substring(line.indexOf("..") + 2);
																																																				
																																																														String itemString = String.format("%s: %s (%s bp)", fileName, bacteriaName, lengthString);
																																																														ftpSecondLevelCombo.addItem(itemString);
																																																				
																																																														ftp.disconnect();
																																																													}
																																																												}
																																																				
																																																												//ftp.abort();
																																																				
																																																												ftpSecondLevelCombo.setEnabled(true);
																																																											} catch (Exception e1) {
																																																												logger.error(e1.getMessage());
																																																												return;
																																																											}							
																																																										}
																																																									});	
																																																									
																																																									second_thread.start();
																																																								}
																																																							}
																																																						});
																																																						
																																																								ftpSecondLevelCombo.addItemListener(new ItemListener() {
																																																									@Override
																																																									public void itemStateChanged(ItemEvent e) {
																																																										if(e.getStateChange() == ItemEvent.SELECTED){
																																																											downloadBtn.setEnabled(true);
																																																										}	
																																																									}
																																																								});
																																																								
																																																										buttonGroup.add(ownRadioBtn);
																																																										ownRadioBtn.setToolTipText("Use existing files from NCBI FTP server");
																																																										ownRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
																																																										ownRadioBtn.setBounds(12, 201, 402, 25);
																																																										panelMain.add(ownRadioBtn);
																																																										ownRadioBtn.setSelected(true);
																																																										
																																																												ftpFirstLevelCombo.setEnabled(false);
																																																												ftpSecondLevelCombo.setEditable(false);
																																																												downloadBtn.setEnabled(false);
																																																												
																																																														ownRadioBtn.addActionListener(new ActionListener() {
																																																												
																																																															@Override
																																																															public void actionPerformed(ActionEvent arg0) {
																																																																ftpFirstLevelCombo.setEnabled(false);
																																																																ftpSecondLevelCombo.setEnabled(false);
																																																																downloadBtn.setEnabled(false);
																																																												
																																																																pttBrowseBtn.setEnabled(true);
																																																																pttFileTxt.setEnabled(true);
																																																																rntBrowseBtn.setEnabled(true);
																																																																rntFileTxt.setEnabled(true);
																																																												
																																																																ftpFirstLevelCombo.setEnabled(false);
																																																																ftpSecondLevelCombo.setEnabled(false);
																																																																downloadBtn.setEnabled(false);
																																																												
																																																																ftpFirstLevelCombo.removeAllItems();
																																																																ftpSecondLevelCombo.removeAllItems();
																																																												
																																																																imgFileTxt.setEnabled(false);
																																																																imgBrowseBtn.setEnabled(false);
																																																																scaffoldCombo.setEnabled(false);
																																																															}
																																																														});
																																																														
																																																																rntFileTxt.setEnabled(false);
																																																																pttFileTxt.setEnabled(false);
																																																																
																																																																		JLabel lblGenesFileName = new JLabel("Gene annotation:");
																																																																		lblGenesFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
																																																																		lblGenesFileName.setBounds(377, 592, 163, 22);
																																																																		panelMain.add(lblGenesFileName);
																																																																		
																																																																				geneFileNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
																																																																				geneFileNameLbl.setBounds(521, 587, 186, 33);
																																																																				panelMain.add(geneFileNameLbl);
																																																																				
																																																																						JLabel label = new JLabel("Or");
																																																																						label.setFont(new Font("Tahoma", Font.BOLD, 16));
																																																																						label.setBounds(12, 294, 51, 35);
																																																																						panelMain.add(label);
																																																																						
																																																																								JSeparator separator_4 = new JSeparator();
																																																																								separator_4.setBounds(52, 313, 771, 2);
																																																																								panelMain.add(separator_4);
																																																																								
																																																																										buttonGroup.add(imgRadioBtn);
																																																																										imgRadioBtn.setToolTipText("use existing files from IMG server");
																																																																										imgRadioBtn.addActionListener(new ActionListener() {
																																																																											public void actionPerformed(ActionEvent arg0) {
																																																																												ftpFirstLevelCombo.setEnabled(false);
																																																																												ftpSecondLevelCombo.setEnabled(false);
																																																																												downloadBtn.setEnabled(false);

																																																																												pttBrowseBtn.setEnabled(false);
																																																																												pttFileTxt.setEnabled(false);
																																																																												rntBrowseBtn.setEnabled(false);
																																																																												rntFileTxt.setEnabled(false);

																																																																												ftpFirstLevelCombo.setEnabled(false);
																																																																												ftpSecondLevelCombo.setEnabled(false);
																																																																												downloadBtn.setEnabled(false);

																																																																												ftpFirstLevelCombo.removeAllItems();
																																																																												ftpSecondLevelCombo.removeAllItems();

																																																																												imgFileTxt.setEnabled(true);
																																																																												imgBrowseBtn.setEnabled(true);
																																																																												scaffoldCombo.setEnabled(false);
																																																																											}
																																																																										});
																																																																										imgRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
																																																																										imgRadioBtn.setBounds(12, 322, 402, 25);
																																																																										panelMain.add(imgRadioBtn);
																																																																										
																																																																												JLabel lblImgFile = new JLabel("IMG file:");
																																																																												lblImgFile.setBounds(12, 382, 79, 16);
																																																																												panelMain.add(lblImgFile);
																																																																												
																																																																														imgFileTxt = new JTextField();
																																																																														imgFileTxt.setToolTipText("Path to the IMG file, use the browse button");
																																																																														imgFileTxt.setEditable(false);
																																																																														imgFileTxt.setEnabled(false);
																																																																														imgFileTxt.setColumns(10);
																																																																														imgFileTxt.setBounds(148, 379, 559, 22);
																																																																														panelMain.add(imgFileTxt);
																																																																														imgBrowseBtn.setToolTipText("Select the file downloaded from IMG server");
																																																																														imgBrowseBtn.setEnabled(false);
																																																																														imgBrowseBtn.addActionListener(new ActionListener() {
																																																																															public void actionPerformed(ActionEvent e) {
																																																																																Path currentRelativePath = Paths.get("");
																																																																																String location = currentRelativePath.toAbsolutePath().toString();
																																																																																JFileChooser fileChooser = new JFileChooser(location);
																																																																																fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
																																																																																fileChooser.setFileFilter(new FileNameExtensionFilter("XLS File (.xls)", "xls"));
																																																																																int result = fileChooser.showOpenDialog(MainFrame.this);

																																																																																if (result == JFileChooser.APPROVE_OPTION){
																																																																																	imgFileTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
																																																																																	try {
																																																																																		PrepareFiles.extractScaffolds(fileChooser.getSelectedFile().getAbsolutePath(), projectInfo, MainFrame.this.scaffoldCombo);
																																																																																	} catch (IOException e1) {
																																																																																		logger.error(e1.getMessage());
																																																																																		return;
																																																																																	}
																																																																																}
																																																																															}
																																																																														});
																																																																														
																																																																																imgBrowseBtn.setBounds(716, 378, 97, 25);
																																																																																panelMain.add(imgBrowseBtn);
																																																																																
																																																																																		JLabel lblSelectYourScaffold = new JLabel("Select a scaffold:");
																																																																																		lblSelectYourScaffold.setBounds(12, 412, 112, 16);
																																																																																		panelMain.add(lblSelectYourScaffold);
																																																																																		scaffoldCombo.setToolTipText("Select the scaffold within the IMG file");
																																																																																		scaffoldCombo.setEnabled(false);
																																																																																		
																																																																																				scaffoldCombo.setBounds(148, 410, 559, 20);
																																																																																				panelMain.add(scaffoldCombo);
																																																																																				
																																																																																						JSeparator separator_7 = new JSeparator();
																																																																																						separator_7.setBounds(0, 493, 823, 2);
																																																																																						panelMain.add(separator_7);
																																																																																						
																																																																																								JLabel lblyouCanDownload = new JLabel("You can download the IMG file by clicking on this link");
																																																																																								lblyouCanDownload.setBounds(12, 354, 695, 16);
																																																																																								panelMain.add(lblyouCanDownload);
																																																																																								infoLbl.setForeground(Color.RED);
																																																																																								
																																																																																										infoLbl.setFont(new Font("Tahoma", Font.ITALIC, 11));
																																																																																										infoLbl.setBounds(12, 6, 665, 14);
																																																																																										panelMain.add(infoLbl);
																																																																																										
																																																																																												JLabel lblNeedHelp = new JLabel("(?)");
																																																																																												lblNeedHelp.addMouseListener(new MouseAdapter() {
																																																																																													@Override
																																																																																													public void mouseClicked(MouseEvent arg0) {
																																																																																														JOptionPane.showMessageDialog(MainFrame.this, "This will guide you to finding the protein-coding (.ptt file) and RNA (.rnt file) gene\n"
																																																																																																+ "annotation at the FTP server of the National Center for Biotechnology Information\n"
																																																																																																+ "(ftp://ftp.ncbi.nih.gov/genomes/)");
																																																																																													}
																																																																																												});
																																																																																												lblNeedHelp.setToolTipText("Click me!");
																																																																																												lblNeedHelp.setForeground(Color.BLUE);
																																																																																												lblNeedHelp.setFont(new Font("Tahoma", Font.PLAIN, 11));
																																																																																												lblNeedHelp.setBounds(433, 85, 88, 14);
																																																																																												panelMain.add(lblNeedHelp);
																																																																																												
																																																																																														JLabel lblhelp = new JLabel("(?)");
																																																																																														lblhelp.addMouseListener(new MouseAdapter() {
																																																																																															@Override
																																																																																															public void mouseClicked(MouseEvent e) {
																																																																																																JOptionPane.showMessageDialog(MainFrame.this, "If you previously downloaded the .ppt and .rnt files from NCBI you can select them here.\n"
																																																																																																		+ "You can also manually edit the .ptt and .rnt files before creating a new project as long as you\n"
																																																																																																		+ "follow the appropriate format of the file.");
																																																																																															}
																																																																																														});
																																																																																														lblhelp.setToolTipText("Click me!");
																																																																																														lblhelp.setForeground(Color.BLUE);
																																																																																														lblhelp.setFont(new Font("Tahoma", Font.PLAIN, 11));
																																																																																														lblhelp.setBounds(433, 207, 88, 14);
																																																																																														panelMain.add(lblhelp);
																																																																																														
																																																																																																JLabel label_2 = new JLabel("(?)");
																																																																																																label_2.addMouseListener(new MouseAdapter() {
																																																																																																	@Override
																																																																																																	public void mouseClicked(MouseEvent e) {
																																																																																																		JOptionPane.showMessageDialog(MainFrame.this, "Alternatively, you can use annotation downloaded from Integrated Microbial Genomes (http://img.jgi.doe.gov/).\n"
																																																																																																				+ "To download the IMG annotation, find the desired genome in the IMG database (finished genomes only),\n"
																																																																																																				+ "select 'Export gene information' (near the bottom of the page), and save the .xls file from the follwoing page.");
																																																																																																	}
																																																																																																});
																																																																																																label_2.setToolTipText( "Click me!");
																																																																																																label_2.setForeground(Color.BLUE);
																																																																																																label_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
																																																																																																label_2.setBounds(433, 329, 88, 14);
																																																																																																panelMain.add(label_2);
																																																																																																
																																																																																																		JLabel label_1 = new JLabel("(?)");
																																																																																																		label_1.addMouseListener(new MouseAdapter() {
																																																																																																			@Override
																																																																																																			public void mouseClicked(MouseEvent e) {
																																																																																																				JOptionPane.showMessageDialog(MainFrame.this, "The IMG annotation may include data for multiple scaffolds (e.g., chromosomes or plasmids)\n"
																																																																																																						+ "Select the one you wish to use.");
																																																																																																			}
																																																																																																		});
																																																																																																		label_1.setToolTipText("Click me!");
																																																																																																		label_1.setForeground(Color.BLUE);
																																																																																																		label_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
																																																																																																		label_1.setBounds(120, 412, 88, 14);
																																																																																																		panelMain.add(label_1);
																																																																																																		
																																																																																																				JLabel lblSequqenceLength = new JLabel("Sequqence length (bp):");
																																																																																																				lblSequqenceLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
																																																																																																				lblSequqenceLength.setBounds(378, 555, 204, 22);
																																																																																																				panelMain.add(lblSequqenceLength);
																																																																																																				
																																																																																																						sequenceLengthLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
																																																																																																						sequenceLengthLbl.setBounds(575, 550, 178, 33);
																																																																																																						panelMain.add(sequenceLengthLbl);
																																																																																																						
																																																																																																						lblPleaseEnterAn.setForeground(Color.RED);
																																																																																																						lblPleaseEnterAn.setBounds(498, 37, 293, 14);
																																																																																																						lblPleaseEnterAn.setVisible(false);
																																																																																																						panelMain.add(lblPleaseEnterAn);

		JPanel panelInitialize = new JPanel();
		tabbedPane.addTab("Manage Libraries", null, panelInitialize, null);
		panelInitialize.setLayout(null);

		JLabel lblBrowseFoThe = new JLabel("Add new library to the project:");
		lblBrowseFoThe.setBounds(12, 149, 799, 16);
		panelInitialize.add(lblBrowseFoThe);

		samFilePathTxt = new JTextField();
		samFilePathTxt.setToolTipText("Path to the SAM file");
		samFilePathTxt.setBounds(12, 178, 581, 22);
		panelInitialize.add(samFilePathTxt);
		samFilePathTxt.setColumns(10);
		browseForSamBtn.setToolTipText("Use this to browse for the SAM file");

		browseForSamBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setFileFilter(new FileNameExtensionFilter("SAM Files (.sam) | Insertion Positions (.INSPO)", "sam", "inspo"));
				int result = fileChooser.showOpenDialog(MainFrame.this);

				if (result == JFileChooser.APPROVE_OPTION){
					MainFrame.this.samFilePathTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
					MainFrame.this.extractInsBtn.setEnabled(true);
					MainFrame.this.samFilePathTxt.setEnabled(false);
					MainFrame.this.browseForSamBtn.setEnabled(false);

					if(fileChooser.getSelectedFile().getName().contains(".inspo")){
						doneLbl1.setVisible(true);
						loadingLbl.setBounds(22, 239, 30, 16);
						extractInsLbl.setForeground(Color.BLACK);
					}
				}
			}
		});
		browseForSamBtn.setBounds(603, 176, 99, 25);
		panelInitialize.add(browseForSamBtn);

		extractInsLbl.setForeground(Color.LIGHT_GRAY);
		extractInsLbl.setBounds(54, 219, 411, 16);
		panelInitialize.add(extractInsLbl);
		extractInsBtn.setToolTipText("Process and extract the SAM file");
		extractInsBtn.setEnabled(false);
		extractInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.this.extractInsLbl.setForeground(Color.BLACK);
				MainFrame.this.extractInsBtn.setEnabled(false);
				MainFrame.this.extractInsBtn.setText("Wait");

				if (samFilePathTxt.getText() == null || samFilePathTxt.getText().equals("")){
					JOptionPane.showMessageDialog(MainFrame.this, "Select an input file and try again.");
					setToDefaultState();
					return;
				}

				Integer uniqueInsertionsLimit = 0;
				try{
					uniqueInsertionsLimit = Integer.parseInt(removeUniqueInsertionsTxt.getText());
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter a valid number for fileds highlighted with red.");
					clearTheForm();
					return;
				}
				if(samFilePathTxt.getText().endsWith(".inspo")){
					String newPath = projectInfo.getPath() + PrepareFiles.prepareFileName(samFilePathTxt.getText(), ".inspo");

					File oldFile = new File(samFilePathTxt.getText());
					File newFile = new File(newPath);

					FileChannel destination;
					FileChannel source;
					try {
						destination = new FileOutputStream(newFile).getChannel();
						source = new FileInputStream(oldFile).getChannel();

						if(destination != null && source != null){
							destination.transferFrom(source, 0, source.size());
						}

						if(source != null){
							source.close();
						}

						if(destination != null){
							destination.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					loadingLbl.setBounds(22, 239, 30, 16);
					loadingLbl.setVisible(true);
					samFilePathTxt.setText(newPath);
					SortInsertions run = new SortInsertions(uniqueInsertionsLimit);
					Thread runThread = new Thread(run);
					runThread.start();
				}else{
					loadingLbl.setBounds(22, 219, 30, 16);
					loadingLbl.setVisible(true);
					ExtractInsertions run = new ExtractInsertions(uniqueInsertionsLimit);
					Thread runThread = new Thread(run);
					runThread.start();
				}
			}
		});

		extractInsBtn.setBounds(712, 177, 99, 25);
		panelInitialize.add(extractInsBtn);

		sortInsLbl.setForeground(Color.LIGHT_GRAY);
		sortInsLbl.setBounds(54, 238, 411, 16);
		panelInitialize.add(sortInsLbl);

		countUniLbl.setForeground(Color.LIGHT_GRAY);
		countUniLbl.setBounds(54, 257, 411, 16);
		panelInitialize.add(countUniLbl);

		sortUniLbl.setForeground(Color.LIGHT_GRAY);
		sortUniLbl.setBounds(54, 276, 411, 16);
		panelInitialize.add(sortUniLbl);
		cancelLibSaveBtn.setToolTipText("Clear everything and start over");
		cancelLibSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				clearTheForm();

			}
		});

		cancelLibSaveBtn.setBounds(658, 286, 153, 25);
		panelInitialize.add(cancelLibSaveBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 322, 821, 2);
		panelInitialize.add(separator_2);

		JLabel lblRemoveExistingLibraries = new JLabel("Edit existing libraries:");
		lblRemoveExistingLibraries.setBounds(10, 337, 313, 16);
		panelInitialize.add(lblRemoveExistingLibraries);
		libraryComboBox.setToolTipText("Select a library to edit");

		libraryComboBox.setBounds(12, 366, 581, 22);
		panelInitialize.add(libraryComboBox);
		removeLibBtn.setToolTipText("Remove the selected library");

		removeLibBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedLib = (String) libraryComboBox.getSelectedItem();

				BufferedReader br = null;
				BufferedWriter bw = null;
				File tempCopy = null;
				try {
					tempCopy = new File(projectInfo.getPath() + "temp.pro");
					br = new BufferedReader(new FileReader(
							projectInfo.getFile()));
					bw = new BufferedWriter(new FileWriter(
							tempCopy));

					String line = br.readLine();
					bw.write(line + "\n");

					line = br.readLine();
					bw.write(line + "\n");

					line = br.readLine();
					bw.write(line + "\n");

					line = br.readLine();
					while(line != null){
						if (selectedLib.compareTo(line.substring(0, line.length() - 6)) == 0){					
							File temp = new File(projectInfo.getPath() + selectedLib + ".inspo");
							temp.delete();

							temp = new File(projectInfo.getPath() + selectedLib + ".inspos");
							temp.delete();

							temp = new File(projectInfo.getPath() + selectedLib + ".inspou");
							temp.delete();

							temp = new File(projectInfo.getPath() + selectedLib + ".inspous");
							temp.delete();

							temp = new File(projectInfo.getPath() + selectedLib + ".data");
							temp.delete();

							br.readLine();
							br.readLine();
							br.readLine();
							line = br.readLine();

						}else{
							bw.write(line + "\n");

							line = br.readLine();
							bw.write(line + "\n");

							line = br.readLine();
							bw.write(line + "\n");

							line = br.readLine();
							bw.write(line + "\n");

							line = br.readLine();
						}

					}

				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				} finally {
					try {
						br.close();
						bw.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
						return;
					}
				}

				File pro = projectInfo.getFile();
				pro.delete();
				tempCopy.renameTo(pro);
				initiateLibraryComboBox();
				libraryCountLbl.setText((Integer.parseInt(libraryCountLbl.getText()) - 1) + "");
			}
		});
		removeLibBtn.setBounds(714, 365, 97, 25);
		panelInitialize.add(removeLibBtn);
		renameLibBtn.setToolTipText("Rename the selected library");

		renameLibBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = JOptionPane.showInputDialog(MainFrame.this, "Enter the New Name");
				String selectedLib = (String) libraryComboBox.getSelectedItem();

				renameLibrary(newName, selectedLib);
			}
		});
		renameLibBtn.setBounds(605, 365, 97, 25);
		panelInitialize.add(renameLibBtn);

		loadingLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));
		loadingLbl.setBounds(14, 219, 30, 16);
		panelInitialize.add(loadingLbl);

		doneLbl1.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl1.setBounds(22, 219, 23, 14);
		panelInitialize.add(doneLbl1);

		doneLbl2.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl2.setBounds(21, 239, 23, 14);
		panelInitialize.add(doneLbl2);

		doneLbl3.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl3.setBounds(21, 258, 23, 14);
		panelInitialize.add(doneLbl3);

		doneLbl4.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl4.setBounds(21, 277, 23, 14);
		panelInitialize.add(doneLbl4);

		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(0, 136, 821, 2);
		panelInitialize.add(separator_9);

		JLabel lblChooseALibrary = new JLabel("Choose a library:");
		lblChooseALibrary.setBounds(12, 458, 286, 14);
		panelInitialize.add(lblChooseALibrary);
		plotLibraryCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				reloadWinInfo();
			}
		});
		plotLibraryCombo.setToolTipText("Select a libray to plot");

		plotLibraryCombo.setBounds(12, 483, 402, 20);
		panelInitialize.add(plotLibraryCombo);
		plotBtn.setToolTipText("Plot the selected library");

		plotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotDataMethod();
			}
		});
		plotBtn.setBounds(691, 516, 87, 23);
		panelInitialize.add(plotBtn);

		winLenTxt = new JTextField();
		winLenTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				highlightAllTheText((JTextField) arg0.getComponent());
			}
		});
		winLenTxt.setToolTipText("Enter the window length");
		winLenTxt.setColumns(10);
		winLenTxt.setBounds(424, 483, 56, 20);
		winLenTxt.setInputVerifier(new IntegerInputVerifier(errorMsgLbl));
		panelInitialize.add(winLenTxt);

		winStepTxt = new JTextField();
		winStepTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				highlightAllTheText((JTextField) arg0.getComponent());
			}
		});
		winStepTxt.setToolTipText("Enter window step size");
		winStepTxt.setColumns(10);
		winStepTxt.setBounds(490, 483, 65, 20);
		winStepTxt.setInputVerifier(new IntegerInputVerifier(errorMsgLbl));
		panelInitialize.add(winStepTxt);

		lblWindowLength.setBounds(392, 468, 123, 14);
		panelInitialize.add(lblWindowLength);

		JLabel label_5 = new JLabel("Step:");
		label_5.setBounds(490, 468, 108, 14);
		panelInitialize.add(label_5);

		JSeparator separator_10 = new JSeparator();
		separator_10.setBounds(0, 420, 821, 2);
		panelInitialize.add(separator_10);

		JLabel lblPlotTheDistribution = new JLabel("Plot the distribution of the number of unique insertions or all sequence reads per window for a selected library.");
		lblPlotTheDistribution.setBounds(10, 433, 801, 14);
		panelInitialize.add(lblPlotTheDistribution);

		plotWaitLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));
		plotWaitLbl.setBounds(12, 606, 183, 14);
		panelInitialize.add(plotWaitLbl);

		JLabel label_3 = new JLabel("(?)");
		label_3.setToolTipText("Click me!");
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, 
						"This is the same plot as shown in Figure 1 of Sarmiento et al., PNAS 110:4726-4731, 2013. A strong peak at zero separated from a wider peak for larger\n"
								+ "numbers of insertions indicates that the window size is suitable for detecting essential genes in the given library. Larger window sizes provide better\n"
								+ "distinction between essential and non-essential genes but they can miss essential genes shorter than the window length." );
			}
		});
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_3.setBounds(781, 520, 30, 14);
		panelInitialize.add(label_3);

		maxNumInsTxt = new JTextField();
		maxNumInsTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				highlightAllTheText((JTextField) arg0.getComponent());
			}
		});
		maxNumInsTxt.setToolTipText("Enter the window length");
		maxNumInsTxt.setText("3");
		maxNumInsTxt.setColumns(10);
		maxNumInsTxt.setBounds(426, 551, 118, 20);
		maxNumInsTxt.setInputVerifier(new IntegerInputVerifier(errorMsgLbl));
		panelInitialize.add(maxNumInsTxt);

		JLabel lblMaxNumberOf = new JLabel("Max number of insertions/reads:");
		lblMaxNumberOf.setBounds(233, 554, 183, 14);
		panelInitialize.add(lblMaxNumberOf);
		maxNumInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				maxNumInsrtions();
			}
		});

		maxNumInsBtn.setBounds(563, 550, 215, 23);
		panelInitialize.add(maxNumInsBtn);
		countOnlyUniqueRadio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				maxNumInsTxt.setText("3");
			}
		});
		countOnlyUniqueRadio.setSelected(true);

		countOnlyUniqueRadio.setBounds(12, 516, 249, 23);
		panelInitialize.add(countOnlyUniqueRadio);
		countAllReadsRadio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				maxNumInsTxt.setText("10");
			}
		});

		countAllReadsRadio.setBounds(12, 542, 215, 23);
		panelInitialize.add(countAllReadsRadio);
		numberOfReads.add(countOnlyUniqueRadio);
		numberOfReads.add(countAllReadsRadio);

		JLabel label_7 = new JLabel("(?)");
		label_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "This will identify chromosomal segments with low transposon insertion density,\n"
						+ "possibly indicating that these segments may be essential. Specifically, it will list\n"
						+ "starts and ends of the regions comprised of overlapping windows with no more\n"
						+ "than the specified number of insertions or reads.");
			}
		});
		label_7.setToolTipText("Click me!");
		label_7.setForeground(Color.BLUE);
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_7.setBounds(781, 554, 30, 14);
		panelInitialize.add(label_7);

		JXLabel samDescLbl = new JXLabel("New label");
		samDescLbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		samDescLbl.setText("You can add and manage transposon insertion mutant libraries here. You need the .sam file from the Barrows-Wheeler aligner. (bwa, http://bio-bwa.sourceforge.net/) to add a library to your project. SAM stands for Sequence Alignment/Map format. It is a TAB-delimited text format file that contains the alignment of the sequence reads to the genome. If the Barrows-Wheeler Aligner is installed on this computer you may be able to run it from this application. To add new library, provide the name for the library, navigate to the .sam file using the 'Browse' button, and then click 'Extract'.");
		samDescLbl.setBounds(12, 11, 799, 94);
		panelInitialize.add(samDescLbl);
		samDescLbl.setLineWrap(true);

		JLabel label_8 = new JLabel("(?)");
		label_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(MainFrame.this, "This will process the SAM file to extract number number of sequence reads\n"
						+ "corresponding to every possible transposon location in the genome." );
			}
		});
		label_8.setToolTipText("Click me!");
		label_8.setForeground(Color.BLUE);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_8.setBounds(12, 111, 30, 14);
		panelInitialize.add(label_8);

		btnOptimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				findOptimalWinLength();
			}
		});
		btnOptimal.setBounds(565, 482, 210, 23);
		panelInitialize.add(btnOptimal);

		JLabel label_9 = new JLabel("(?)");
		label_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(MainFrame.this, "The program will attempt to find the optimal window size by analyzing the distribution of insertions\n"
						+ "among the sequence windows. Depending on the character of the data, this procedure may not always\n"
								+ "work and you should verify that the function should be bimodal, separating windows overlapping with\n"
										+ "essential and nonessential genes. You can manually change the window size and explore the distribution\n"
										+ "for different window sizes using the 'Plot' button below. This feature is intended to be used only with\n"
										+ "unique insertions.");
			}
		});
		label_9.setToolTipText("Click me!");
		label_9.setForeground(Color.BLUE);
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_9.setBounds(781, 486, 30, 14);
		panelInitialize.add(label_9);
		winlenLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));
		winlenLbl.setVisible(false);
		
		winlenLbl.setBounds(563, 486, 215, 14);
		panelInitialize.add(winlenLbl);
		
		errorMsgLbl.setForeground(Color.RED);
		errorMsgLbl.setBounds(279, 606, 455, 14);
		errorMsgLbl.setVisible(false);
		panelInitialize.add(errorMsgLbl);
		
		lblRemoveUniqueInsertions.setBounds(475, 214, 336, 14);
		panelInitialize.add(lblRemoveUniqueInsertions);
		
		removeUniqueInsertionsTxt = new JTextField();
		removeUniqueInsertionsTxt.setText("0");
		removeUniqueInsertionsTxt.setBounds(710, 211, 38, 20);
		panelInitialize.add(removeUniqueInsertionsTxt);
		removeUniqueInsertionsTxt.setColumns(10);
		removeUniqueInsertionsTxt.setInputVerifier(new IntegerInputVerifier(errorMsgLbl));

		lblReads.setBounds(760, 215, 46, 14);
		panelInitialize.add(lblReads);
		
		label_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(MainFrame.this, "Some unique insertions may be represented by a single read (or only a\n"
						+ "few reads). These could result from sequence reads that were incorrectly\n"
						+ "mapped to the genome, or from other artifacts. You can use this\n"
						+ "parameter to remove such unique insertions and the associated reads\n"
						+ "from the data.");
			}
		});
		label_10.setToolTipText("Click me!");
		label_10.setForeground(Color.BLUE);
		label_10.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_10.setBounds(797, 215, 30, 14);
		panelInitialize.add(label_10);
		
		btnAnotherPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				anotherPlot();
			}
		});
		btnAnotherPlot.setBounds(551, 584, 227, 23);
		panelInitialize.add(btnAnotherPlot);
		
		JLabel label_11 = new JLabel("(?)");
		label_11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(MainFrame.this, "Assuming that unique insertions represent different mutants, if each mutant had\n"
						+ "an equal chance to be represented by a sequence read in the sequencing experiment,\n"
						+ "the number of reads per unique insertion would follow approximately a normal\n"
						+ "distribution. However, this is rarely the case with real data. This option allows you\n"
						+ "to investigate how the distribution of reads differs from the random expectation.");
			}
		});
		label_11.setToolTipText("Click me!");
		label_11.setForeground(Color.BLUE);
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_11.setBounds(781, 590, 30, 14);
		panelInitialize.add(label_11);
		loadingLbl.setVisible(false);
		doneLbl1.setVisible(false);
		doneLbl2.setVisible(false);
		doneLbl3.setVisible(false);
		doneLbl4.setVisible(false);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Manage Data Tables", null, panel, null);
		panel.setLayout(null);

		JLabel lblAddNewData = new JLabel("Create new data table:");
		lblAddNewData.setBounds(10, 55, 290, 16);
		panel.add(lblAddNewData);
		addNewDataTableBtn.setToolTipText("Add new data table to the project, after clicking just enter the name of the table.");

		addNewDataTableBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent arg0) {

				String name = JOptionPane.showInputDialog(MainFrame.this, "Enter New Table Name:");

				if(name == null || name.compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter table name!");
					return;
				}

				File newTable = new File(projectInfo.getPath() + name + ".table.xls"); //REPLACE
				if (newTable.exists()){
					JOptionPane.showMessageDialog(MainFrame.this, "A table with this name already exists. Try another name.");
					return;
				}

				if(hasGeneFile){

					File tempGene = projectInfo.getGeneFile();

					try {
						FileChannel destination = new FileOutputStream(newTable).getChannel();
						FileChannel source = new FileInputStream(tempGene).getChannel();

						if(destination != null && source != null){
							destination.transferFrom(source, 0, source.size());
						}

						if(source != null){
							source.close();
						}

						if(destination != null){
							destination.close();
						}

						JOptionPane.showMessageDialog(MainFrame.this, "Table was created successfully!");
						initiateDataTablesComboBox();
						dataTableCombo.setSelectedIndex(0);
						dataTableCountLbl.setText((Integer.parseInt(dataTableCountLbl.getText()) + 1) + "");

					} catch (IOException e) {
						logger.error(e.getMessage());
						return;
					}
				}else{
					JOptionPane.showMessageDialog(MainFrame.this, "*.gene File does not exists. The Table can't be created.");
				}

			}
		});
		addNewDataTableBtn.setBounds(187, 52, 147, 23);
		panel.add(addNewDataTableBtn);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(0, 86, 823, 8);
		panel.add(separator_5);

		JLabel lblOldLibraries = new JLabel("Manage existing tables:");
		lblOldLibraries.setBounds(10, 105, 311, 16);
		panel.add(lblOldLibraries);
		dataTableCombo.setToolTipText("Just select the data table you want to edit");

		dataTableCombo.setBounds(10, 132, 433, 20);
		panel.add(dataTableCombo);
		dataTableRenameBtn.setToolTipText("Rename the data table in the project");
		dataTableRenameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String newName = JOptionPane.showInputDialog(MainFrame.this, "Please enter the new name:");
				if(newName == null || newName.compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "You must enter the new name!");
					return;
				}

				String selectedTable = (String)dataTableCombo.getSelectedItem();
				File oldFile = new File(projectInfo.getPath() + selectedTable + ".table.xls"); //REPLACE
				File newFile = new File(projectInfo.getPath() + newName + ".table.xls"); //REPLACE

				boolean done = oldFile.renameTo(newFile);

				if(!done){
					JOptionPane.showMessageDialog(MainFrame.this, "Rename could not take part!");
					return;
				}

				initiateDataTablesComboBox();
			}
		});

		dataTableRenameBtn.setBounds(675, 131, 138, 23);
		panel.add(dataTableRenameBtn);
		dataTableRemoveBtn.setToolTipText("Remove the selected data table from the project");
		dataTableRemoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File table = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".table.xls"); //REPLACE

				if(table.exists()){
					if(!table.delete()){
						JOptionPane.showMessageDialog(MainFrame.this, "Deletion could not take part!!!");
					}else{
						initiateDataTablesComboBox();
						dataTableCountLbl.setText((Integer.parseInt(dataTableCountLbl.getText()) - 1) + "");
					}
				}else{
					JOptionPane.showMessageDialog(MainFrame.this, "The selected file does not exists.");
				}
			}
		});

		dataTableRemoveBtn.setBounds(675, 165, 138, 23);
		panel.add(dataTableRemoveBtn);
		openAsSpreadsheetBtn.setToolTipText("Open the data table in a seperate editor so that you can edit it on your own.");
		openAsSpreadsheetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					prepareXlsFileAndOpen();
				} catch (IOException e1) {
					logger.error(e1.getMessage());
					return;
				}

			}
		});

		openAsSpreadsheetBtn.setBounds(453, 132, 212, 23);
		panel.add(openAsSpreadsheetBtn);
		replaceXlsBtn.setToolTipText("Replace editted data table with the original one");

		replaceXlsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replaceXlsFile();
			}
		});
		replaceXlsBtn.setEnabled(false);
		tableCancelChangeBtn.setToolTipText("Cancel editing and ignore the changes");
		tableCancelChangeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				File xlsFile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + " - temp.table.xls"); //REPLACE
				if(xlsFile.delete()){

					for (int i = 0; i < tabbedPane.getTabCount(); i++){
						tabbedPane.setEnabledAt(i, true);
					}
					infoLbl.setVisible(false);

					addNewDataTableBtn.setEnabled(true);
					openAsSpreadsheetBtn.setEnabled(true);
					dataTableRemoveBtn.setEnabled(true);
					dataTableRenameBtn.setEnabled(true);
					dataTableCombo.setEnabled(true);
					addNewIndicesBtn.setEnabled(true);
					replaceXlsBtn.setEnabled(false);
					tableCancelChangeBtn.setEnabled(false);

				}else{
					JOptionPane.showMessageDialog(MainFrame.this, "Please close the spreadsheet application.", "Could not delete the temp file", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		tableCancelChangeBtn.setEnabled(false);
		replaceXlsBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		replaceXlsBtn.setBounds(453, 166, 101, 23);
		panel.add(replaceXlsBtn);
		tableCancelChangeBtn.setEnabled(false);

		tableCancelChangeBtn.setBounds(564, 166, 101, 23);
		panel.add(tableCancelChangeBtn);

		JLabel lblNewLabel_3 = new JLabel("Create and manage spreadsheets with gene information.");
		lblNewLabel_3.setBounds(10, 11, 475, 14);
		panel.add(lblNewLabel_3);

		JLabel label_6 = new JLabel("(?)");
		label_6.setToolTipText("Click me!");
		label_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, ""
						+ "Once you create a table you can subsequently calculate essentiality indices (EI) for all genes using different libraries and different\n"
						+ "parameters, as well as perform other forms of data analysis. Each form of data analysis will add a new column of data to the existing\n"
						+ "table. You can also open the spreadsheet in Excel or other external software and use its functions to analyze the data. If you use\n"
						+ "external software make sure that you do not alter the format of the table and save the file as tab-delimited text." );
			}
		});
		label_6.setForeground(Color.BLUE);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_6.setBounds(453, 11, 30, 14);
		panel.add(label_6);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(0, 39, 823, 8);
		panel.add(separator_6);
		addNewIndicesBtn.setToolTipText("You can add extra data to your data tables in this subsection");

		addNewIndicesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AddMoreIndices addFrame = new AddMoreIndices((String) dataTableCombo.getSelectedItem(), projectInfo, MainFrame.this);
				addFrame.setVisible(true);

			}
		});
		addNewIndicesBtn.setBounds(10, 165, 433, 23);
		panel.add(addNewIndicesBtn);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmOpenProject = new JMenuItem("Open Project");
		mntmOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorkspaceChooser ws = new WorkspaceChooser();
				ws.setVisible(true);

				MainFrame.this.setVisible(false);
				MainFrame.this.dispose();
			}
		});
		mnMenu.add(mntmOpenProject);

		JSeparator separator_1 = new JSeparator();
		mnMenu.add(separator_1);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnMenu.add(mntmExit);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("BWA", null, panel_3, null);
		panel_3.setLayout(null);

		JLabel lblNewLabel = new JLabel("1. If you already have the 'bwa' and 'samtools' installed on your machine you can use this part.");
		lblNewLabel.setBounds(10, 11, 697, 14);
		panel_3.add(lblNewLabel);

		JLabel lblIfYouDont = new JLabel("2. If you don't have them, you can use the Shell Scripts below to autommatically install them");
		lblIfYouDont.setBounds(10, 37, 745, 14);
		panel_3.add(lblIfYouDont);
		alreadyInstalledRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bwaInstallBtn.setEnabled(false);

				alreadyInstalledRadio.setSelected(true);
			}
		});

		alreadyInstalledRadio.setSelected(true);
		alreadyInstalledRadio.setBounds(10, 85, 191, 23);
		panel_3.add(alreadyInstalledRadio);
		useScriptsRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bwaInstallBtn.setEnabled(true);
			}
		});

		useScriptsRadio.setBounds(10, 111, 292, 23);
		panel_3.add(useScriptsRadio);
		bwaInstallBtn.setToolTipText("Install BWA program");
		bwaInstallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				installBWA();

			}
		});

		bwaInstallBtn.setBounds(357, 111, 203, 23);
		panel_3.add(bwaInstallBtn);

		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(0, 165, 823, 2);
		panel_3.add(separator_8);
		reloadProjectFromFile();
		hasGeneFile = findGeneFile();
		if(!hasSeqNum  || !hasGeneFile){
			for (int i = 1; i < tabbedPane.getTabCount(); i++){
				tabbedPane.setEnabledAt(i, false);
			}
		}else{
			infoLbl.setVisible(false);
		}

		ButtonGroup bwaGroup = new ButtonGroup();
		bwaGroup.add(alreadyInstalledRadio);
		bwaGroup.add(useScriptsRadio);

		JLabel lblNewLabel_1 = new JLabel(" on your machine .");
		lblNewLabel_1.setBounds(20, 58, 246, 15);
		panel_3.add(lblNewLabel_1);
		remoteHelpBtn.setToolTipText("Help for creating a SAM file on your remote Linux machine");

		remoteHelpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remoteInstallHelp();
			}
		});
		remoteHelpBtn.setBounds(10, 374, 256, 25);
		panel_3.add(remoteHelpBtn);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 179, 803, 183);
		panel_3.add(panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 461, 85, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
						
								JLabel lblSelectThefastq = new JLabel("Select the 'FASTQ' File:");
								GridBagConstraints gbc_lblSelectThefastq = new GridBagConstraints();
								gbc_lblSelectThefastq.anchor = GridBagConstraints.WEST;
								gbc_lblSelectThefastq.insets = new Insets(0, 0, 5, 5);
								gbc_lblSelectThefastq.gridx = 0;
								gbc_lblSelectThefastq.gridy = 0;
								panel_5.add(lblSelectThefastq, gbc_lblSelectThefastq);
								
										fastqFilePath = new JTextField();
										GridBagConstraints gbc_fastqFilePath = new GridBagConstraints();
										gbc_fastqFilePath.fill = GridBagConstraints.HORIZONTAL;
										gbc_fastqFilePath.insets = new Insets(0, 0, 5, 5);
										gbc_fastqFilePath.gridx = 1;
										gbc_fastqFilePath.gridy = 0;
										panel_5.add(fastqFilePath, gbc_fastqFilePath);
										fastqFilePath.setText("");
										fastqFilePath.setEnabled(true);
										fastqFilePath.setEditable(false);
										fastqFilePath.setColumns(10);
										
												JButton fastqBrowseBtn = new JButton("Browse");
												GridBagConstraints gbc_fastqBrowseBtn = new GridBagConstraints();
												gbc_fastqBrowseBtn.fill = GridBagConstraints.HORIZONTAL;
												gbc_fastqBrowseBtn.insets = new Insets(0, 0, 5, 0);
												gbc_fastqBrowseBtn.gridx = 2;
												gbc_fastqBrowseBtn.gridy = 0;
												panel_5.add(fastqBrowseBtn, gbc_fastqBrowseBtn);
												fastqBrowseBtn.setToolTipText("Select a FASTQ file");
														
																JLabel lblSelectThefna = new JLabel("Select the 'FNA' File:");
																GridBagConstraints gbc_lblSelectThefna = new GridBagConstraints();
																gbc_lblSelectThefna.anchor = GridBagConstraints.WEST;
																gbc_lblSelectThefna.insets = new Insets(0, 0, 5, 5);
																gbc_lblSelectThefna.gridx = 0;
																gbc_lblSelectThefna.gridy = 1;
																panel_5.add(lblSelectThefna, gbc_lblSelectThefna);
																
																fnaFilePath = new JTextField();
																fnaFilePath.setEditable(false);
																GridBagConstraints gbc_fnaFilePath = new GridBagConstraints();
																gbc_fnaFilePath.insets = new Insets(0, 0, 5, 5);
																gbc_fnaFilePath.fill = GridBagConstraints.HORIZONTAL;
																gbc_fnaFilePath.gridx = 1;
																gbc_fnaFilePath.gridy = 1;
																panel_5.add(fnaFilePath, gbc_fnaFilePath);
																fnaFilePath.setColumns(10);
														
																JButton fnaBrowseBtn = new JButton("Browse");
																GridBagConstraints gbc_fnaBrowseBtn = new GridBagConstraints();
																gbc_fnaBrowseBtn.fill = GridBagConstraints.HORIZONTAL;
																gbc_fnaBrowseBtn.insets = new Insets(0, 0, 5, 0);
																gbc_fnaBrowseBtn.gridx = 2;
																gbc_fnaBrowseBtn.gridy = 1;
																panel_5.add(fnaBrowseBtn, gbc_fnaBrowseBtn);
																fnaBrowseBtn.setToolTipText("Select a FNA file");
																fnaBrowseBtn.addActionListener(new ActionListener() {
																	public void actionPerformed(ActionEvent arg0) {
																		Path currentRelativePath = Paths.get("");
																		String location = currentRelativePath.toAbsolutePath()
																				.toString();
																		JFileChooser fileChooser = new JFileChooser(location);
																		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
																		fileChooser.setFileFilter(new FileNameExtensionFilter("FNA Files (.fna)", "fna"));
																		int result = fileChooser.showOpenDialog(MainFrame.this);

																		if (result == JFileChooser.APPROVE_OPTION){
																			fnaFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
																		}else{
																			return;
																		}
																	}
																});
														
														JButton bwaFnaDownloadBtn = new JButton("Download");
														GridBagConstraints gbc_bwaFnaDownloadBtn = new GridBagConstraints();
														gbc_bwaFnaDownloadBtn.fill = GridBagConstraints.HORIZONTAL;
														gbc_bwaFnaDownloadBtn.insets = new Insets(0, 0, 5, 0);
														gbc_bwaFnaDownloadBtn.gridx = 2;
														gbc_bwaFnaDownloadBtn.gridy = 2;
														panel_5.add(bwaFnaDownloadBtn, gbc_bwaFnaDownloadBtn);
												
														JLabel lblCreateSamFile = new JLabel("Create SAM file in:");
														GridBagConstraints gbc_lblCreateSamFile = new GridBagConstraints();
														gbc_lblCreateSamFile.anchor = GridBagConstraints.WEST;
														gbc_lblCreateSamFile.insets = new Insets(0, 0, 5, 5);
														gbc_lblCreateSamFile.gridx = 0;
														gbc_lblCreateSamFile.gridy = 3;
														panel_5.add(lblCreateSamFile, gbc_lblCreateSamFile);
														
																samFilePath = new JTextField();
																GridBagConstraints gbc_samFilePath = new GridBagConstraints();
																gbc_samFilePath.fill = GridBagConstraints.HORIZONTAL;
																gbc_samFilePath.insets = new Insets(0, 0, 5, 5);
																gbc_samFilePath.gridx = 1;
																gbc_samFilePath.gridy = 3;
																panel_5.add(samFilePath, gbc_samFilePath);
																samFilePath.setText("");
																samFilePath.setEnabled(true);
																samFilePath.setEditable(false);
																samFilePath.setColumns(10);
																
																		JButton newSamBrowseBtn = new JButton("Browse");
																		GridBagConstraints gbc_newSamBrowseBtn = new GridBagConstraints();
																		gbc_newSamBrowseBtn.fill = GridBagConstraints.HORIZONTAL;
																		gbc_newSamBrowseBtn.insets = new Insets(0, 0, 5, 0);
																		gbc_newSamBrowseBtn.gridx = 2;
																		gbc_newSamBrowseBtn.gridy = 3;
																		panel_5.add(newSamBrowseBtn, gbc_newSamBrowseBtn);
																		newSamBrowseBtn.setToolTipText("Select a location to save the new SAM file");
																		
																				JLabel lblEnterNewSam = new JLabel("Enter new SAM file name:");
																				GridBagConstraints gbc_lblEnterNewSam = new GridBagConstraints();
																				gbc_lblEnterNewSam.anchor = GridBagConstraints.WEST;
																				gbc_lblEnterNewSam.insets = new Insets(0, 0, 5, 5);
																				gbc_lblEnterNewSam.gridx = 0;
																				gbc_lblEnterNewSam.gridy = 4;
																				panel_5.add(lblEnterNewSam, gbc_lblEnterNewSam);
																				
																						newSamNameTxt = new JTextField();
																						GridBagConstraints gbc_newSamNameTxt = new GridBagConstraints();
																						gbc_newSamNameTxt.fill = GridBagConstraints.HORIZONTAL;
																						gbc_newSamNameTxt.insets = new Insets(0, 0, 5, 5);
																						gbc_newSamNameTxt.gridx = 1;
																						gbc_newSamNameTxt.gridy = 4;
																						panel_5.add(newSamNameTxt, gbc_newSamNameTxt);
																						newSamNameTxt.addFocusListener(new FocusAdapter() {
																							@Override
																							public void focusGained(FocusEvent arg0) {
																								highlightAllTheText((JTextField) arg0.getComponent());
																							}
																						});
																						newSamNameTxt.setToolTipText("Created SAM file name");
																						newSamNameTxt.setText("");
																						newSamNameTxt.setEnabled(true);
																						newSamNameTxt.setColumns(10);
																								
																										JLabel label_4 = new JLabel("(?)");
																										GridBagConstraints gbc_label_4 = new GridBagConstraints();
																										gbc_label_4.anchor = GridBagConstraints.WEST;
																										gbc_label_4.insets = new Insets(0, 0, 0, 5);
																										gbc_label_4.gridx = 0;
																										gbc_label_4.gridy = 5;
																										panel_5.add(label_4, gbc_label_4);
																										label_4.addMouseListener(new MouseAdapter() {
																											@Override
																											public void mouseClicked(MouseEvent arg0) {
																												JOptionPane.showMessageDialog(MainFrame.this, "The FASTQ file contains your sequence reads and you should have received it from Illumina or the sequencing facility\n"
																														+ "you used. The FNA file contains the genomic DNA sequence in fastA format and you can download it for complete\n"
																														+ "prokaryotic genomes from the NCBI ftp server at ftp://ftp.ncbi.nih.gov/genomes/Bacteria/");
																											}
																										});
																										label_4.setToolTipText("Click me!");
																										label_4.setForeground(Color.BLUE);
																										label_4.setFont(new Font("Tahoma", Font.PLAIN, 11));
																						
																								JButton btnRun = new JButton("Create SAM file");
																								GridBagConstraints gbc_btnRun = new GridBagConstraints();
																								gbc_btnRun.gridx = 2;
																								gbc_btnRun.gridy = 5;
																								panel_5.add(btnRun, gbc_btnRun);
																								btnRun.setToolTipText("Create the SAM file");
																								btnRun.addActionListener(new ActionListener() {
																									public void actionPerformed(ActionEvent e) {
																										try {
																											createSamFile();
																										} catch (IOException | InterruptedException e1) {
																											e1.printStackTrace();
																										}
																									}
																								});
																		newSamBrowseBtn.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent arg0) {
																				Path currentRelativePath = Paths.get("");
																				String location = currentRelativePath.toAbsolutePath()
																						.toString();
																				JFileChooser fileChooser = new JFileChooser(location);
																				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
																				int result = fileChooser.showOpenDialog(MainFrame.this);

																				if (result == JFileChooser.APPROVE_OPTION){
																					samFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
																				}else{
																					return;
																				}
																			}
																		});
												fastqBrowseBtn.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														Path currentRelativePath = Paths.get("");
														String location = currentRelativePath.toAbsolutePath()
																.toString();
														JFileChooser fileChooser = new JFileChooser(location);
														fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
														fileChooser.setFileFilter(new FileNameExtensionFilter("FASTQ Files (.fastq)", "fastq"));
														int result = fileChooser.showOpenDialog(MainFrame.this);

														if (result == JFileChooser.APPROVE_OPTION){
															fastqFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
														}else{
															return;
														}
													}
												});
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Bowtie", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(12, 12, 801, 75);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JXLabel lblNewLabel_2 = new JXLabel("New label");
		lblNewLabel_2.setBounds(0, 0, 757, 72);
		panel_2.add(lblNewLabel_2);
		lblNewLabel_2.setText("Title goes here!");
		lblNewLabel_2.setLineWrap(true);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(12, 113, 799, 192);
		panel_1.add(panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JLabel lblFastqFile = new JLabel("FastQ File");
		GridBagConstraints gbc_lblFastqFile = new GridBagConstraints();
		gbc_lblFastqFile.anchor = GridBagConstraints.WEST;
		gbc_lblFastqFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblFastqFile.gridx = 0;
		gbc_lblFastqFile.gridy = 0;
		panel_4.add(lblFastqFile, gbc_lblFastqFile);
		
		bowtieFastqTxt = new JTextField();
		bowtieFastqTxt.setEditable(false);
		GridBagConstraints gbc_bowtieFastqTxt = new GridBagConstraints();
		gbc_bowtieFastqTxt.insets = new Insets(0, 0, 5, 5);
		gbc_bowtieFastqTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieFastqTxt.gridx = 1;
		gbc_bowtieFastqTxt.gridy = 0;
		panel_4.add(bowtieFastqTxt, gbc_bowtieFastqTxt);
		bowtieFastqTxt.setColumns(10);
		
		JButton bowtieFastqBrowse = new JButton("Browse");
		bowtieFastqBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setFileFilter(new FileNameExtensionFilter("FASTQ Files (.fastq)", "fastq"));
				int result = fileChooser.showOpenDialog(MainFrame.this);

				if (result == JFileChooser.APPROVE_OPTION){
					bowtieFastqTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}else{
					return;
				}
			}
		});
		GridBagConstraints gbc_bowtieFastqBrowse = new GridBagConstraints();
		gbc_bowtieFastqBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieFastqBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_bowtieFastqBrowse.gridx = 2;
		gbc_bowtieFastqBrowse.gridy = 0;
		panel_4.add(bowtieFastqBrowse, gbc_bowtieFastqBrowse);
		
		JLabel lblFna = new JLabel("FNA");
		GridBagConstraints gbc_lblFna = new GridBagConstraints();
		gbc_lblFna.anchor = GridBagConstraints.WEST;
		gbc_lblFna.insets = new Insets(0, 0, 5, 5);
		gbc_lblFna.gridx = 0;
		gbc_lblFna.gridy = 1;
		panel_4.add(lblFna, gbc_lblFna);
		
		bowtieFnaTxt = new JTextField();
		bowtieFnaTxt.setEditable(false);
		GridBagConstraints gbc_bowtieFnaTxt = new GridBagConstraints();
		gbc_bowtieFnaTxt.insets = new Insets(0, 0, 5, 5);
		gbc_bowtieFnaTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieFnaTxt.gridx = 1;
		gbc_bowtieFnaTxt.gridy = 1;
		panel_4.add(bowtieFnaTxt, gbc_bowtieFnaTxt);
		bowtieFnaTxt.setColumns(10);
		
		JButton bowtieFnaBrowse = new JButton("Browse");
		bowtieFnaBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setFileFilter(new FileNameExtensionFilter("FNA Files (.fna)", "fna"));
				int result = fileChooser.showOpenDialog(MainFrame.this);

				if (result == JFileChooser.APPROVE_OPTION){
					bowtieFnaTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}else{
					return;
				}
			}
		});
		GridBagConstraints gbc_bowtieFnaBrowse = new GridBagConstraints();
		gbc_bowtieFnaBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieFnaBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_bowtieFnaBrowse.gridx = 2;
		gbc_bowtieFnaBrowse.gridy = 1;
		panel_4.add(bowtieFnaBrowse, gbc_bowtieFnaBrowse);
		
		JButton bowtieFnaDownload = new JButton("Download");
		GridBagConstraints gbc_bowtieFnaDownload = new GridBagConstraints();
		gbc_bowtieFnaDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieFnaDownload.insets = new Insets(0, 0, 5, 0);
		gbc_bowtieFnaDownload.gridx = 2;
		gbc_bowtieFnaDownload.gridy = 2;
		panel_4.add(bowtieFnaDownload, gbc_bowtieFnaDownload);
		
		JLabel lblSamFile = new JLabel("SAM file location");
		GridBagConstraints gbc_lblSamFile = new GridBagConstraints();
		gbc_lblSamFile.anchor = GridBagConstraints.WEST;
		gbc_lblSamFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSamFile.gridx = 0;
		gbc_lblSamFile.gridy = 3;
		panel_4.add(lblSamFile, gbc_lblSamFile);
		
		bowtieSamLocTxt = new JTextField();
		bowtieSamLocTxt.setEditable(false);
		GridBagConstraints gbc_bowtieSamLocTxt = new GridBagConstraints();
		gbc_bowtieSamLocTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieSamLocTxt.insets = new Insets(0, 0, 5, 5);
		gbc_bowtieSamLocTxt.gridx = 1;
		gbc_bowtieSamLocTxt.gridy = 3;
		panel_4.add(bowtieSamLocTxt, gbc_bowtieSamLocTxt);
		bowtieSamLocTxt.setColumns(10);
		
		JButton bowtieSamBrowseBtn = new JButton("Browse");
		bowtieSamBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(MainFrame.this);

				if (result == JFileChooser.APPROVE_OPTION){
					bowtieSamLocTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}else{
					return;
				}
			}
		});
		GridBagConstraints gbc_bowtieSamBrowseBtn = new GridBagConstraints();
		gbc_bowtieSamBrowseBtn.fill = GridBagConstraints.BOTH;
		gbc_bowtieSamBrowseBtn.insets = new Insets(0, 0, 5, 0);
		gbc_bowtieSamBrowseBtn.gridx = 2;
		gbc_bowtieSamBrowseBtn.gridy = 3;
		panel_4.add(bowtieSamBrowseBtn, gbc_bowtieSamBrowseBtn);
		
		JLabel lblSamFilename = new JLabel("SAM filename");
		GridBagConstraints gbc_lblSamFilename = new GridBagConstraints();
		gbc_lblSamFilename.anchor = GridBagConstraints.WEST;
		gbc_lblSamFilename.insets = new Insets(0, 0, 5, 5);
		gbc_lblSamFilename.gridx = 0;
		gbc_lblSamFilename.gridy = 4;
		panel_4.add(lblSamFilename, gbc_lblSamFilename);
		
		bowtieSamFilenameTxt = new JTextField();
		GridBagConstraints gbc_bowtieSamFilenameTxt = new GridBagConstraints();
		gbc_bowtieSamFilenameTxt.anchor = GridBagConstraints.WEST;
		gbc_bowtieSamFilenameTxt.insets = new Insets(0, 0, 5, 5);
		gbc_bowtieSamFilenameTxt.gridx = 1;
		gbc_bowtieSamFilenameTxt.gridy = 4;
		panel_4.add(bowtieSamFilenameTxt, gbc_bowtieSamFilenameTxt);
		bowtieSamFilenameTxt.setColumns(15);
		
		bowtieSamCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BowtieCreateSam();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_bowtieSamCreateBtn = new GridBagConstraints();
		gbc_bowtieSamCreateBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_bowtieSamCreateBtn.gridx = 2;
		gbc_bowtieSamCreateBtn.gridy = 5;
		panel_4.add(bowtieSamCreateBtn, gbc_bowtieSamCreateBtn);
		
		JSeparator separator_11 = new JSeparator();
		separator_11.setBounds(12, 99, 823, 2);
		panel_1.add(separator_11);
	}

	private void BowtieCreateSam() throws IOException{
		
		final String OSName = System.getProperty("os.name");
		
		File bowtie_index = null;
		File bowtie_align = null;
		
		if(OSName.contains("Windows") || OSName.contains("windows")){
			JOptionPane.showMessageDialog(this, "Bowtie is not suported on Microsoft Windows, yet!");
			return;
			
			/*JOptionPane.showMessageDialog(this, "Bowtie requires Perl and Python to execute. \n"
					+ "Please make sure that perl and python are installed on your system and are\n"
					+ "available in your environment path. Otherwise, this section of the program\n"
					+ "will fail.");
			bowtie_index = new File("bowtie-bin\\win-64\\bowtie2-build");
			bowtie_align = new File("bowtie-bin\\win-64\\bowtie2");*/
		}else{
			bowtie_index = new File("./bowtie-bin/linux/bowtie2-build");
			bowtie_align = new File("./bowtie-bin/linux/bowtie2");
		}
		
		String bowtie_align_options = "-q"; /* -L 18*/

		if (bowtie_align == null || bowtie_index == null){
			JOptionPane.showMessageDialog(this, "Bowtie is not supported on this platform within Tn-SeqExplorer.\n"
					+ "Please use either Microsoft Window (64bit) or any distribution of Linux operating systems.");
			return;
		}
		
		if (bowtie_index.exists() && bowtie_align.exists()){
			//Do the magic here! :)

			String fnaPath = bowtieFnaTxt.getText();
			String fastQPath = bowtieFastqTxt.getText();
			String samLoc = bowtieSamLocTxt.getText();
			String samName = bowtieSamFilenameTxt.getText();

			if (fnaPath == null || fnaPath.equals("")){
				JOptionPane.showMessageDialog(this, "Please fill out all the fields.");
			}

			if (fastQPath == null || fastQPath.equals("")){
				JOptionPane.showMessageDialog(this, "Please fill out all the fields.");
			}

			if (samLoc == null || samLoc.equals("")){
				JOptionPane.showMessageDialog(this, "Please fill out all the fields.");
			}

			if (samName == null || samName.equals("")){
				JOptionPane.showMessageDialog(this, "Please fill out all the fields.");
			}

			if (!samLoc.endsWith(File.separator)){
				samLoc = samLoc + File.separator; 
			}

			String index_file = samLoc + samName + "_index";
			String sam_file = samLoc + samName;

			final String script_index = String.format("%s %s %s", bowtie_index.getAbsolutePath(), fnaPath, index_file);
			final String script_align = String.format("%s %s -x %s -U %s -S %s", bowtie_align.getAbsolutePath(), bowtie_align_options, index_file, fastQPath, sam_file);

			JOptionPane.showMessageDialog(this, "Creating the SAM file might take a few minutes, please be patient!");
			bowtieSamCreateBtn.setText("Please wait...");
			bowtieSamCreateBtn.setEnabled(false);
			final Icon tempIcon = bowtieSamCreateBtn.getIcon();
			bowtieSamCreateBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));

			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						Process index_p = null;
						if(OSName.contains("Windows") || OSName.contains("windows")){
							System.err.println(script_index);
							File modifyPath = new File("bowtie-bin\\win-64\\modify_path.bat");
							String temp = String.format("cmd /c echo OFF && start %s && perl %s", modifyPath.getAbsolutePath(), script_index);
							System.err.println(temp);
							index_p = Runtime.getRuntime().exec(temp);
						}else{
							index_p = Runtime.getRuntime().exec(script_index);
						}
						
						boolean process_exited = false;
						while(!process_exited){
							try{
								index_p.exitValue();
								process_exited = true;
							}catch(Exception e){
								process_exited = false;
							}
						}

						Process align_p = null;
						if(OSName.contains("Windows") || OSName.contains("windows")){
							System.err.println(script_align);
							align_p = Runtime.getRuntime().exec("perl " + script_align);
						}else{
							align_p = Runtime.getRuntime().exec(script_align);
						}

						process_exited = false;
						while(!process_exited){
							try{
								align_p.exitValue();
								process_exited = true;
							}catch(Exception e){
								process_exited = false;
							}
						}		

						bowtieSamCreateBtn.setIcon(tempIcon);
						bowtieSamCreateBtn.setText("Create SAM file");
						bowtieSamCreateBtn.setEnabled(true);

						JOptionPane.showMessageDialog(MainFrame.this, "SAM file created successfully!");
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}).start();

		}else{
			JOptionPane.showMessageDialog(null, "Bowtie binary files don't exist.\n"
					+ "Please download bowtie2 binary files from \"http://bowtie-bio.sourceforge.net/bowtie2/index.shtml\"\n"
					+ "and put them into a folder called bowtie-bin/linux/ right beside the Tn-SeqExplorer executable jar file.");
		}
	}
	
	private void findOptimalWinLength(){
		if (countAllReadsRadio.isSelected()){
			int res = JOptionPane.showConfirmDialog(MainFrame.this, "This feature cannot be used when counting all reads.\n"
					+ "Do you want to continue with unique insertions?", "feature cannot be used", JOptionPane.YES_NO_OPTION);

			if (res == JOptionPane.YES_OPTION){
				countOnlyUniqueRadio.setSelected(true);
			}else{
				return;
			}
		}

		(new Thread(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(MainFrame.this, "The calculation might take a few minutes to complete.\nPlease be patient.");
			}
		})).start();

		plotWaitLbl.setVisible(true);

		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {					
					Pair<Integer, Integer> result = StatisticsHelper.findOptimalLength((String) plotLibraryCombo.getSelectedItem(), projectInfo, countOnlyUniqueRadio.isSelected(), MainFrame.this);

					changeWinLenTo("Plotting...");
					
					final String title;
					if (countOnlyUniqueRadio.isSelected()){
						String temp = String.format("Library Name: %s (Counting unique insertions) | Window Length: %d | Window Steps: %d", (String) plotLibraryCombo.getSelectedItem(), result.getFirst(), result.getSecond());
						title = temp;
					} else{
						String temp = String.format("Library Name: %s (Counting all reads) | Window Length: %d | Window Steps: %d", (String) plotLibraryCombo.getSelectedItem(), result.getFirst(), result.getSecond());
						title = temp;
					}

					ChartPanel panel = new ChartPanel(PlotData.plotData((String) plotLibraryCombo.getSelectedItem(), result.getFirst(), result.getSecond(), title, projectInfo, countOnlyUniqueRadio.isSelected()));

					ExtendedPlotViewer frame = new ExtendedPlotViewer(MainFrame.this);	
					frame.setWinInfo(result.getFirst(), result.getSecond());
					frame.setDataFile((String) plotLibraryCombo.getSelectedItem());
					frame.setPlotName(title);
					frame.setIfSelected(countOnlyUniqueRadio.isSelected());
					frame.setProjectInfo(projectInfo);
					frame.setCurrentInfo(result.getFirst(), result.getSecond());
					frame.setVisible(true);
					frame.addPlot(panel);	

					plotWaitLbl.setVisible(false);
					(MainFrame.this).makeWinlenLblVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		})).start();
	}

	public void setWinInfoAndSave(int winLen, int winStep){
		winLenTxt.setText(winLen + "");
		winStepTxt.setText(winStep + "");

		try{
			//Save the results to a file
			File winLenFile = new File(projectInfo.getPath() + (String)plotLibraryCombo.getSelectedItem() + ".data");
			BufferedWriter bw = new BufferedWriter(new FileWriter(winLenFile));
			bw.write("WinLen = " + winLen + "\n");
			bw.write("WinStep = " + winStep + "\n");
			bw.close();
		}catch(IOException e){
			logger.fatal(e.getMessage());
		}
	}

	public boolean checkIfApplied(int winLen, int winStep){

		if (winLenTxt.getText().equals("") || winStepTxt.getText().equals("")){
			return false;
		}

		if (Integer.parseInt(winLenTxt.getText()) != winLen){
			return false;
		}

		if (Integer.parseInt(winStepTxt.getText()) != winStep){
			return false;
		}

		return true;

	}

	private void highlightAllTheText(final JTextField textField){
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				textField.selectAll();		
			}
		});	
	}

	private void maxNumInsrtions(){
		if(winLenTxt.getText() == null || winLenTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window length", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(winStepTxt.getText() == null || winStepTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window step length", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(maxNumInsTxt.getText() == null || maxNumInsTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the maximum number of insertions", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		

		plotWaitLbl.setVisible(true);

		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int len = Integer.parseInt(winLenTxt.getText());
					int step = Integer.parseInt(winStepTxt.getText());
					int maxNumIns = Integer.parseInt(maxNumInsTxt.getText());
					boolean ifOnlyInsertions = countOnlyUniqueRadio.isSelected();
					
					String result = PrepareFiles.maxNumberOfInsertions((String) plotLibraryCombo.getSelectedItem(), len, step, maxNumIns, ifOnlyInsertions, MainFrame.this.projectInfo);

					if (result.compareTo(Messages.failMsg) != 0){
						JOptionPane.showMessageDialog(null, "Success");
					}else{
						logger.error("There was some error while processing Max Number of Insertions");
						JOptionPane.showMessageDialog(MainFrame.this, "There was some error while processing Max Number of Insertions", "Error", JOptionPane.ERROR_MESSAGE);
					}

					plotWaitLbl.setVisible(false);
				}catch(IOException e){
					logger.error(e.getStackTrace());
				}catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter valid values for Window Length, Step and Max number of insertions fields.");
					plotWaitLbl.setVisible(false);
				}
			}
		})).start();
	}

	private void installBWA() {
		Process p = null;
		BufferedReader br = null;
		try {
			p = Runtime.getRuntime().exec("uname -a");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while((line = br.readLine()) != null){
				if (line.toLowerCase().contains("ubuntu")){
					installBWAUbuntu();
				}else{
					installBWARedHat();
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void installBWAUbuntu(){
		URL programSource = MainFrame.class.getResource("/resources/bwa-0.7.5a.tar.bz2");
		File programDest = new File(projectInfo.getPath() + "bwa-0.7.5a.tar.bz2");

		URL shellSource = MainFrame.class.getResource("/resources/install-bwa-ubuntu.sh");
		File shellDest = new File(projectInfo.getPath() + "shell.sh");

		JOptionPane.showMessageDialog(null, "In order to install the library, you should enter your machine's root password.\n"
				+ "The password is only used to install the library.", "Root Password", JOptionPane.WARNING_MESSAGE);

		try {
			FileUtils.copyURLToFile(programSource, programDest);
			FileUtils.copyURLToFile(shellSource, shellDest);

			String shellPath = shellDest.getAbsolutePath().replaceAll("\\s+","\\\\ ");
			String cmd[] = {"gnome-terminal", "-x", "bash", "-c", 
					"echo 'Please Enter Your Root Password';"
							+ "pwd;"
							+ "su -m root -c 'sh " + shellPath + "';"
							+ "echo;"
							+ "echo;"
							+ "echo 'Press Any Key To Continue...';"
							+ "read"};

			//Path currentRelativePath = Paths.get("");
			//String location = currentRelativePath.toAbsolutePath().toString();
			String location = shellDest.getAbsolutePath().substring(0, shellPath.indexOf("/shell.sh"));
			File dir = new File(location);
			Process child = Runtime.getRuntime().exec(cmd, null, dir);
			child.waitFor();
		} catch (IOException | InterruptedException e1) {
			logger.error(e1.getMessage());
			return;
		}
	}

	private void installBWARedHat(){
		URL programSource = MainFrame.class.getResource("/resources/bwa-0.7.5a.tar.bz2");
		File programDest = new File(projectInfo.getPath() + "bwa-0.7.5a.tar.bz2");

		URL shellSource = MainFrame.class.getResource("/resources/install-bwa-redhat.sh");
		File shellDest = new File(projectInfo.getPath() + "shell.sh");

		JOptionPane.showMessageDialog(null, "In order to install the library, you should enter your machine's root password.\n"
				+ "The password is only used to install the library.", "Root Password", JOptionPane.WARNING_MESSAGE);

		try {
			FileUtils.copyURLToFile(programSource, programDest);
			FileUtils.copyURLToFile(shellSource, shellDest);
			
			String shellPath = shellDest.getAbsolutePath().replaceAll("\\s+","\\\\ ");
			String cmd[] = {"gnome-terminal", "-x", "bash", "-c", 
					"echo 'Please Enter Your Root Password';"
							+ "pwd;"
							+ "su -m root -c 'sh " + shellPath + "';"
							+ "echo;"
							+ "echo;"
							+ "echo 'Press Any Key To Continue...';"
							+ "read"};

			//Path currentRelativePath = Paths.get("");
			//String location = currentRelativePath.toAbsolutePath().toString();
			String location = shellDest.getAbsolutePath().substring(0, shellPath.indexOf("/shell.sh"));
			File dir = new File(location);
			Process child = Runtime.getRuntime().exec(cmd, null, dir);
			child.waitFor();
		} catch (IOException | InterruptedException e1) {
			logger.error(e1.getMessage());
			return;
		}
	}

	private void renameLibrary(String newName, String selectedLib) {
		if(newName != null && newName.compareTo("") != 0){

			BufferedReader br = null;
			BufferedWriter bw = null;
			File tempCopy = null;
			try {
				tempCopy = new File(projectInfo.getPath() + "temp.pro");
				br = new BufferedReader(new FileReader(
						projectInfo.getFile()));
				bw = new BufferedWriter(new FileWriter(
						tempCopy));

				String line = br.readLine();
				bw.write(line + "\n");

				line = br.readLine();
				bw.write(line + "\n");

				line = br.readLine();
				bw.write(line + "\n");

				line = br.readLine();
				while(line != null){
					if (selectedLib.compareTo(line.substring(0, line.length() - 6)) == 0){					
						File temp = new File(projectInfo.getPath() + selectedLib + ".inspo");
						temp.renameTo(new File(projectInfo.getPath() + newName + ".inspo"));
						bw.write(newName + ".inspo" + "\n");

						temp = new File(projectInfo.getPath() + selectedLib + ".inspos");
						temp.renameTo(new File(projectInfo.getPath() + newName + ".inspos"));
						bw.write(newName + ".inspos" + "\n");

						temp = new File(projectInfo.getPath() + selectedLib + ".inspou");
						temp.renameTo(new File(projectInfo.getPath() + newName + ".inspou"));
						bw.write(newName + ".inspou" + "\n");

						temp = new File(projectInfo.getPath() + selectedLib + ".inspous");
						temp.renameTo(new File(projectInfo.getPath() + newName + ".inspous"));
						bw.write(newName + ".inspous" + "\n");

						temp = new File(projectInfo.getPath() + selectedLib + ".data");
						temp.renameTo(new File(projectInfo.getPath() + newName + ".data"));

						br.readLine();
						br.readLine();
						br.readLine();
						line = br.readLine();

					}else{
						bw.write(line + "\n");

						line = br.readLine();
						bw.write(line + "\n");

						line = br.readLine();
						bw.write(line + "\n");

						line = br.readLine();
						bw.write(line + "\n");

						line = br.readLine();
					}

				}

			} catch (IOException e) {
				logger.error(e.getMessage());
				return;
			} finally {
				try {
					br.close();
					bw.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
			}

			File pro = projectInfo.getFile();
			pro.delete();
			tempCopy.renameTo(pro);
			initiateLibraryComboBox();
		}
	}

	private void createSamFile() throws IOException, InterruptedException{

		String fnafile = fnaFilePath.getText();
		String fastqFile = fastqFilePath.getText();
		String samName = newSamNameTxt.getText();
		String samPath = samFilePath.getText();

		if (fnafile == null || fnafile.compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please select a FNA file.", "FNA File is needed", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (fastqFile == null || fastqFile.compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please select a FASTQ file.", "FASTQ File is needed", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (samName == null || samName.compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please enter the new SAM file's name.", "SAM file name is required", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (samPath == null || samPath.compareTo("") == 0){
			int result = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want the new SAM file to be created in your project folder?", "No location selected for SAM file", JOptionPane.YES_NO_OPTION);

			if(result == JOptionPane.YES_OPTION){
				samPath = projectInfo.getPath();
			}else{
				return;				
			}
		}else{
			samPath = samPath + "/";
		}

		String name1 = PrepareFiles.prepareOutputFilePath(fastqFile, projectInfo.getPath(), "");
		String saiName = PrepareFiles.prepareOutputFilePath(fastqFile, projectInfo.getPath(), ".sai");
		samName = samPath + samName + ".sam";

		File shellScript = new File(projectInfo.getPath() + "temp.sh");
		BufferedWriter bw = new BufferedWriter(new FileWriter(shellScript));

		String cmdTemp = String.format("bwa index -p \"%s\" \"%s\"\n", name1, fnafile);
		bw.write(cmdTemp);

		cmdTemp = String.format("bwa aln -k 2 -n 0.001 -l 18 \"%s\" \"%s\" > \"%s\"\n", name1, fastqFile, saiName);
		bw.write(cmdTemp);

		cmdTemp = String.format("bwa samse -f \"%s\" \"%s\" \"%s\" \"%s\"\n", samName, name1, saiName, fastqFile);
		bw.write(cmdTemp);
		bw.close();

		Thread.sleep(500);

		String cmd[] = {"gnome-terminal", "-x", "bash", "-c", 
				"echo 'Please wait till the SAM file gets created';"
						+ "sh \"" + shellScript.getAbsolutePath() + "\";"
						+ "echo 'Press Any Key To Continue...';"
						+ "read"};

		Path currentRelativePath = Paths.get("");
		String location = currentRelativePath.toAbsolutePath()
				.toString();
		File dir = new File(location);
		Process child = Runtime.getRuntime().exec(cmd, null, dir);
		child.waitFor();

		/*shellScript.delete();

		File toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".amb"));
		toDelete.delete();

		toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".ann"));
		toDelete.delete();

		toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".bwt"));
		toDelete.delete();

		toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".pac"));
		toDelete.delete();

		toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".sa"));
		toDelete.delete();

		toDelete = new File(projectInfo.getPath() + PrepareFiles.prepareFileName(fastqFile, ".sai"));
		toDelete.delete();*/
	}

	private void setSequenceLengthText(int num){
		String output = "" + num;
		int counter = 0;
		for (int index = output.length() - 1; index > 0; index--){
			counter++;
			if (counter % 3 == 0){
				counter = 0;
				output = output.substring(0,index) + "," + output.substring(index);
			}
		}

		sequenceLengthLbl.setText(output);
	}

	private void remoteInstallHelp() {
		RemoteInstall remote = new RemoteInstall();
		remote.setParentFrame(this);
		remote.setVisible(true);
	}

	private void clearTheForm(){
		/*File tempFile = null;
		if (samFilePathTxt.getText() != null && samFilePathTxt.getText().compareTo("") != 0){
			tempFile = new File(samFilePathTxt.getText());
		}

		if(tempFile != null && tempFile.exists()){
			tempFile = new File(PrepareFiles.prepareOutputFilePath(samFilePathTxt.getText(), projectInfo.getPath(), ".inspo"));
			if(tempFile.exists()){
				tempFile.delete();
			}

			tempFile = new File(PrepareFiles.prepareOutputFilePath(samFilePathTxt.getText(), projectInfo.getPath(), ".inspos"));
			if(tempFile.exists()){
				tempFile.delete();
			}

			tempFile = new File(PrepareFiles.prepareOutputFilePath(samFilePathTxt.getText(), projectInfo.getPath(), ".inspou"));
			if(tempFile.exists()){
				tempFile.delete();
			}

			tempFile = new File(PrepareFiles.prepareOutputFilePath(samFilePathTxt.getText(), projectInfo.getPath(), ".inspous"));
			if(tempFile.exists()){
				tempFile.delete();
			}
		}
		 */
		setToDefaultState();
	}

	private void anotherPlot(){
		plotWaitLbl.setVisible(true);

		final String title = String.format("Library Name: %s, Distribution of reads per unique insertion", (String) plotLibraryCombo.getSelectedItem());

		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {	
					ChartPanel panel = new ChartPanel(PlotData.anotherPlot((String) plotLibraryCombo.getSelectedItem(), title, projectInfo, countOnlyUniqueRadio.isSelected()));

					PlotViewer frame = new PlotViewer();					
					frame.setPlotName(title);
					frame.setVisible(true);
					frame.addPlot(panel);

					plotWaitLbl.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		})).start();
	}
	
	private void plotDataMethod(){

		if(winLenTxt.getText() == null || winLenTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window length", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(winStepTxt.getText() == null || winStepTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window step length", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int len = 0;
		int step = 0;
		
		try{
			len = Integer.parseInt(winLenTxt.getText());
			step = Integer.parseInt(winStepTxt.getText());
			plotWaitLbl.setVisible(true);
			int tempLen = 0;
			int tempStep = 0;
			
			File winInfo = new File(projectInfo.getPath() + (String)plotLibraryCombo.getSelectedItem() + ".data");
			if (winInfo.exists()){
				BufferedReader br = new BufferedReader(new FileReader(winInfo));
				String line = br.readLine();
				tempLen = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
				line = br.readLine();
				tempStep = Integer.parseInt(line.substring(line.indexOf("=") + 2, line.length()));
				br.close();

				if (tempLen != len || tempStep != step){
					int result = JOptionPane.showConfirmDialog(this, "Window length and window step differes from the applied values. \nDo you want to apply the new values?");

					if (result == JOptionPane.YES_OPTION){
						setWinInfoAndSave(len, step);
					}else if (result == JOptionPane.NO_OPTION){

					}else{
						return;
					}
				}
			}
		}catch (IOException e){
			logger.fatal(e.getMessage());
		}catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(MainFrame.this, "Please enter a valid window length and step value.");
			return;
		}

		final String title;
		if (countOnlyUniqueRadio.isSelected()){
			String temp = String.format("Library Name: %s (Counting unique insertions) | Window Length: %d | Window Steps: %d", (String) plotLibraryCombo.getSelectedItem(),
					len, step);
			title = temp;
		} else{
			String temp = String.format("Library Name: %s (Counting all reads) | Window Length: %d | Window Steps: %d", (String) plotLibraryCombo.getSelectedItem(),
					len, step);
			title = temp;
		}

		final int threadLen = len;
		final int threadStep = step;
		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {	
					ChartPanel panel = new ChartPanel(PlotData.plotData((String) plotLibraryCombo.getSelectedItem(), threadLen, threadStep, title, projectInfo, countOnlyUniqueRadio.isSelected()));

					PlotViewer frame = new PlotViewer();					
					frame.setPlotName(title);
					frame.setVisible(true);
					frame.addPlot(panel);

					plotWaitLbl.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		})).start();

	}

	private void applySequenceNumber(){
		String seqLen = sequenceLenTxt.getText();
		if (seqLen == null || seqLen.compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please Enter Your Sequence Number First!");
			return;
		}

		int seqLenInt;
		try{
			seqLenInt = Integer.parseInt(seqLen);
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(MainFrame.this, "Please enter a valid integer number.");
			return;
		}

		if (MainFrame.this.hasSeqNum){
			BufferedReader br = null;
			BufferedWriter bw = null;
			File temp = null;
			try{
				temp = new File("temp");
				br = new BufferedReader(new FileReader(projectInfo.getFile()));
				bw = new BufferedWriter(new FileWriter(temp));

				String line = br.readLine();

				while(line != null){

					if(!line.contains(Messages.projectSequenceLen)){
						bw.write(line + "\n");
					}else{
						bw.write(Messages.projectSequenceLen + seqLenInt + "\n");
						projectInfo.setSequenceLen(seqLenInt);
					}

					line = br.readLine();
				}

				br.close();
				bw.close();

				projectInfo.getFile().delete();
				temp.renameTo(projectInfo.getFile());

			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}finally{
				try{
					if (bw != null){
						bw.close();
					}
					if (br != null){
						br.close();
					}
				}catch(IOException e){
					logger.error(e.getMessage());
					return;
				}
			}
		}else{
			BufferedWriter bw = null;
			try{
				bw = new BufferedWriter(new FileWriter(projectInfo.getFile(), true));
				bw.append(Messages.projectSequenceLen + seqLen + "\n");
				projectInfo.setSequenceLen(seqLenInt);
			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}finally{
				try{
					if(bw != null){
						bw.close();
					}
				}catch(IOException e){
					logger.error(e.getMessage());
					return;
				}
			}
		}

		hasSeqNum = true;
		JOptionPane.showMessageDialog(MainFrame.this, "New sequence length has been applied!");
		setSequenceLengthText(Integer.parseInt(seqLen));

		if (hasGeneFile){
			for (int i = 1; i < tabbedPane.getTabCount(); i++){
				tabbedPane.setEnabledAt(i, true);
			}
			infoLbl.setVisible(false);
		}
	}

	private void prepareGeneFile(){
		if(imgRadioBtn.isSelected()){
			int choice = JOptionPane.YES_OPTION;
			if(hasGeneFile){
				choice = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to replace the existing Gene File?", "Gene file already exists", JOptionPane.YES_NO_OPTION);
			}

			if(choice == JOptionPane.YES_OPTION){
				if (imgFileTxt.getText() == null || imgFileTxt.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please select your IMG file.", "No IMG File", JOptionPane.WARNING_MESSAGE);
					return;
				}else if (scaffoldCombo.getSelectedItem() == null || ((String)scaffoldCombo.getSelectedItem()).compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "No scaffold selected to process.", "No Scaffold", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					PrepareFiles.processSelectedScaffold(imgFileTxt.getText(), (String) scaffoldCombo.getSelectedItem(), projectInfo);

					if (projectInfo.getGeneFile() != null){
						JOptionPane.showMessageDialog(MainFrame.this, "Genes file was created successfully.");
						geneFileNameLbl.setText(scaffoldCombo.getSelectedItem() + ".genes");
					}else{
						geneFileNameLbl.setText("ERROR");
						hasGeneFile = false;

						for (int i = 1; i < tabbedPane.getTabCount(); i++){
							tabbedPane.setEnabledAt(i, false);
						}
					}
				} catch (IOException e) {
					geneFileNameLbl.setText("ERROR");
					logger.error(e.getMessage());
					return;
				}
			}
		}else{
			String pttPath = pttFileTxt.getText();
			if (pttPath != null && pttPath.compareTo("") != 0){

				if (projectInfo.getSequenceLen() <= 0){
					JOptionPane.showMessageDialog(this, "Please apply the sequence length first and try again");
					return;
				}

				geneFileNameLbl.setText("Preparing...");

				(new Thread(new Runnable(){
					public void run() {
						int choice = JOptionPane.YES_OPTION;
						if(hasGeneFile){
							choice = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to replace the existing Gene File?", "Gene file already exists", JOptionPane.YES_NO_OPTION);
						}

						if(choice == JOptionPane.YES_OPTION){
							createGeneFile();

							if (hasGeneFile){
								geneFileNameLbl.setText(PrepareFiles.prepareFileName(projectInfo.getGeneFile().getAbsolutePath(), ""));
								JOptionPane.showMessageDialog(MainFrame.this, "Genes File Created Successfully!");
								PrepareFiles.deleteAllOtherGenesFiles(MainFrame.this.projectInfo.getGeneFile().getName(), MainFrame.this.projectInfo);

								if (hasSeqNum){
									for (int i = 1; i < tabbedPane.getTabCount(); i++){
										tabbedPane.setEnabledAt(i, true);
									}
									infoLbl.setVisible(false);
								}

								if(projectInfo.getGeneFile() != null){
									geneFileNameLbl.setText(projectInfo.getGeneFile().getName());
								}else{
									geneFileNameLbl.setText("Not Avail.");
								}
							}else{
								JOptionPane.showMessageDialog(MainFrame.this, "There was an error in creating your Genes File.");
								geneFileNameLbl.setText("ERROR");

								for (int i = 1; i < tabbedPane.getTabCount(); i++){
									tabbedPane.setEnabledAt(i, false);
								}
								hasGeneFile = false;
							}
						}else{
							geneFileNameLbl.setText(projectInfo.getGeneFile().getName());
						}
					}
				})).start();					
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "No PTT file was selected!", "No PTT File", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@SuppressWarnings("resource")
	private void replaceXlsFile(){

		File xlsFile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + " - temp.table.xls"); //REPLACE
		File tablefile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".table.xls"); //REPLACE

		if(!xlsFile.exists()){
			logger.fatal("The XLS file could not be found!!!");
		}else{
			if(xlsFile.canWrite()){
				try{
					FileChannel destination = new FileOutputStream(tablefile).getChannel();
					FileChannel source = new FileInputStream(xlsFile).getChannel();
					destination.transferFrom(source, 0, source.size());

					source.close();
					destination.close();

					if(!xlsFile.delete()){
						JOptionPane.showMessageDialog(MainFrame.this, "Please close the spreadsheet application.", "Could not write into the file", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}catch(IOException e){
					logger.error(e.getMessage());
					return;
				}catch(NullPointerException e){
					logger.error(e.getMessage());
					return;
				}
			}else{
				logger.fatal("The XLS file could not be written on!!!");
				JOptionPane.showMessageDialog(MainFrame.this, "Please close the spreadsheet application.", "Could not write into the file", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		BufferedWriter bw = null;
		BufferedReader br = null;

		xlsFile = new File("temp.table.xls"); //REPLACE

		try{
			bw = new BufferedWriter(new FileWriter(xlsFile));
			br = new BufferedReader(new FileReader(tablefile));

			String tempLine = br.readLine();
			if (tempLine.startsWith("start_coord")){
				bw.write("\t\t\t\t\t\t\t\n");
				bw.write("\t\t\t\t\t\t\t\n");
				bw.write("\t\t\t\t\t\t\t\n");
				bw.write("\t\t\t\t\t\t\t\n");
			}

			//bw.write("\n");
			//bw.write("\n");
			//bw.write("\n");
			//bw.write("\n");

			br.close();
			br = new BufferedReader(new FileReader(tablefile));
			tempLine = br.readLine();
			while(tempLine != null){
				bw.write(tempLine + "\n");
				tempLine = br.readLine();
			}

			bw.close();
			br.close();

			if(tablefile.delete()){
				if(xlsFile.renameTo(tablefile)){
					JOptionPane.showMessageDialog(MainFrame.this, "Replaced Successfully!");
				}
			}

		}catch(IOException e){
			logger.error(e.getMessage());
			return;
		}finally{
			try{
				bw.close();
				br.close();				
			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}
		}

		for (int i = 0; i < tabbedPane.getTabCount(); i++){
			tabbedPane.setEnabledAt(i, true);
		}
		infoLbl.setVisible(false);

		addNewDataTableBtn.setEnabled(true);
		openAsSpreadsheetBtn.setEnabled(true);
		dataTableRemoveBtn.setEnabled(true);
		dataTableRenameBtn.setEnabled(true);
		addNewIndicesBtn.setEnabled(true);
		dataTableCombo.setEnabled(true);
		replaceXlsBtn.setEnabled(false);
		tableCancelChangeBtn.setEnabled(false);
	}

	@SuppressWarnings("resource")
	private void prepareXlsFileAndOpen() throws IOException{

		String selectedItem = (String)dataTableCombo.getSelectedItem();

		String warningMsg = String.format("You can make changes to the table such as manually add or remove genes (lines), change gene coordinates, or remove\n"
				+ "previously added data columns, as long as the overall format is maintained. If you make changes to the table save it ...");

		JOptionPane.showMessageDialog(MainFrame.this, warningMsg, "Warning", JOptionPane.WARNING_MESSAGE);

		File selectedFile = new File(projectInfo.getPath() + selectedItem + ".table.xls"); //REPLACE
		File xlsFile = new File(projectInfo.getPath() + selectedItem + " - temp.table.xls"); //REPLACE

		FileChannel destination = new FileOutputStream(xlsFile).getChannel();
		FileChannel source = new FileInputStream(selectedFile).getChannel();

		if(destination != null && source != null){
			destination.transferFrom(source, 0, source.size());
		}

		if(destination != null){
			destination.close();
		}

		if(source != null){
			source.close();
		}

		Desktop.getDesktop().open(xlsFile);

		for (int i = 0; i < tabbedPane.getTabCount(); i++){
			tabbedPane.setEnabledAt(i, false);
		}

		addNewDataTableBtn.setEnabled(false);
		openAsSpreadsheetBtn.setEnabled(false);
		dataTableRemoveBtn.setEnabled(false);
		dataTableRenameBtn.setEnabled(false);
		dataTableCombo.setEnabled(false);
		addNewIndicesBtn.setEnabled(false);
		replaceXlsBtn.setEnabled(true);
		tableCancelChangeBtn.setEnabled(true);

	}

	private void initializeBWAPanel(){
		String OSName = System.getProperty("os.name");
		
		if(OSName.contains("Windows") || OSName.contains("windows")){
			for (Component c : ((JPanel)tabbedPane.getSelectedComponent()).getComponents()){
				c.setEnabled(false);
			}

			remoteHelpBtn.setEnabled(true);

			JOptionPane.showMessageDialog(null, "Full functionality of this tab is only available when you are using Linux!!!\n"
					+ "You can still use the 'Create SAM file manually' button to guide you through installing and running BWA on a remote Linux server.\n"
					+ "\nYou will need:\n"
					+ "  (i) an user account on a remote Linux server\n"
					+ "  (ii) SSH and SFTP client installed on your computer");

		}else{
			bwaInstallBtn.setEnabled(false);
			alreadyInstalledRadio.setSelected(true);
		}

	}

	private void initiatePlotLibraryComboBox(){

		BufferedReader br = null;

		if(plotLibraryCombo != null){
			plotLibraryCombo.removeAllItems();
		}

		try{
			br = new BufferedReader(new FileReader(projectInfo.getFile()));

			String line = br.readLine();
			br.readLine();
			br.readLine();

			while(line != null){
				line = br.readLine();

				if (line != null)
					plotLibraryCombo.addItem(line.substring(0, line.length() - 6));

				line = br.readLine();
				line = br.readLine();
				line = br.readLine();
			}

			if (plotLibraryCombo.getItemCount() == 0){
				plotBtn.setEnabled(false);
				maxNumInsBtn.setEnabled(false);
				maxNumInsTxt.setEnabled(false);
				winLenTxt.setEnabled(false);
				winStepTxt.setEnabled(false);
			}else{
				plotBtn.setEnabled(true);
				maxNumInsBtn.setEnabled(true);
				maxNumInsTxt.setEnabled(true);
				winLenTxt.setEnabled(true);
				winStepTxt.setEnabled(true);
			}

		}catch(IOException e){
			logger.error(e.getMessage());
			return;
		}finally{
			try{
				br.close();
			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}
		}		
	}

	private void initiateDataTablesComboBox(){

		File projectDir = new File(projectInfo.getPath());
		File[] tableFiles = projectDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return (name.endsWith(".table.xls") || name.endsWith(".TABLE.XLS")); //REPLACE
			}
		});

		if (tableFiles != null && tableFiles.length != 0){
			dataTableRenameBtn.setEnabled(true);
			dataTableRemoveBtn.setEnabled(true);
			dataTableCombo.setEnabled(true);
			openAsSpreadsheetBtn.setEnabled(true);
			addNewIndicesBtn.setEnabled(true);

			dataTableCombo.removeAllItems();
			for (File t : tableFiles){
				dataTableCombo.addItem(t.getName().substring(0, t.getName().length() - 10));
			}
		}else{
			dataTableCombo.removeAllItems();
			dataTableCombo.setEnabled(false);
			dataTableCombo.addItem("No existing data table.");
			dataTableRenameBtn.setEnabled(false);
			addNewIndicesBtn.setEnabled(false);
			dataTableRemoveBtn.setEnabled(false);
			openAsSpreadsheetBtn.setEnabled(false);
		}

	}

	private boolean findGeneFile(){

		File projectDir = new File(projectInfo.getPath());
		File[] matched = projectDir.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File arg0, String name) {
				return (name.endsWith(".genes") || name.endsWith(".Genes")); 
			}

		});

		if (matched.length != 1){
			return false;
		}

		projectInfo.setGeneFile(matched[0]);
		geneFileNameLbl.setText(projectInfo.getGeneFile().getName());
		return true;
	}

	private void createGeneFile(){
		String pttPath = pttFileTxt.getText();
		String rntPath = rntFileTxt.getText();

		String geneFilePath = PrepareFiles.createGeneFile(pttPath, rntPath, projectInfo.getPath(), projectInfo); 
		if(geneFilePath.compareTo(Messages.failMsg) != 0){
			hasGeneFile = true;
			projectInfo.setGeneFile(new File(geneFilePath));
		}else{
			hasGeneFile = false;
		}

	}

	private void initiateLibraryComboBox(){

		BufferedReader br = null;

		plotWaitLbl.setVisible(false);

		if(libraryComboBox != null){
			libraryComboBox.removeAllItems();
		}

		try{
			br = new BufferedReader(new FileReader(projectInfo.getFile()));

			String line = br.readLine();
			br.readLine();
			br.readLine();

			while(line != null){
				line = br.readLine();

				if (line != null)
					libraryComboBox.addItem(line.substring(0, line.length() - 6));

				line = br.readLine();
				line = br.readLine();
				line = br.readLine();
			}

			br.close();

			if (libraryComboBox.getItemCount() == 0){
				renameLibBtn.setEnabled(false);
				removeLibBtn.setEnabled(false);
				btnOptimal.setEnabled(false);
			}else{
				renameLibBtn.setEnabled(true);
				removeLibBtn.setEnabled(true);
				btnOptimal.setEnabled(true);
			}

		}catch(IOException e){
			logger.error(e.getMessage());
			return;
		}finally{
			try{
				br.close();
			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}
		}

		initiatePlotLibraryComboBox();

		reloadWinInfo();
		
		//GUI Difference in Linux and Windows Should be checked again!
		String OSName = System.getProperty("os.name");
		if(!(OSName.contains("Windows") || OSName.contains("windows"))){
			if (if_initialize_step_1){
				lblRemoveUniqueInsertions.setBounds(450, 214, 336, 14);
				lblWindowLength.setBounds((int) lblWindowLength.getBounds().getX() - 20, (int) lblWindowLength.getBounds().getY(), (int) lblWindowLength.getBounds().getWidth(), (int) lblWindowLength.getBounds().getHeight());
	
				lblReads.setBounds((int) lblReads.getBounds().getX() - 20, (int) lblReads.getBounds().getY() + 20, (int) lblReads.getBounds().getWidth(), (int) lblReads.getBounds().getHeight());
				removeUniqueInsertionsTxt.setBounds((int) removeUniqueInsertionsTxt.getBounds().getX() - 20, (int) removeUniqueInsertionsTxt.getBounds().getY() + 20, 
						(int) removeUniqueInsertionsTxt.getBounds().getWidth(), (int) removeUniqueInsertionsTxt.getBounds().getHeight());
				label_10.setBounds((int) label_10.getBounds().getX(), (int) label_10.getBounds().getY() + 20, (int) label_10.getBounds().getWidth(), (int) label_10.getBounds().getHeight());
				
				btnAnotherPlot.setSize(btnAnotherPlot.getWidth() + 60, btnAnotherPlot.getHeight());
				btnAnotherPlot.setText(btnAnotherPlot.getText().replace("unique ", ""));
				btnAnotherPlot.setBounds((int) btnAnotherPlot.getBounds().getX() - 80, (int) btnAnotherPlot.getBounds().getY(), (int) btnAnotherPlot.getBounds().getWidth(), (int) btnAnotherPlot.getBounds().getHeight());
				
				if_initialize_step_1 = false;
			}
		}	
		
	}

	private void reloadWinInfo(){

		try{
			File winInfo = new File(projectInfo.getPath() + (String)plotLibraryCombo.getSelectedItem() + ".data");
			if (winInfo.exists()){
				BufferedReader br = new BufferedReader(new FileReader(winInfo));
				String line = br.readLine();
				winLenTxt.setText(line.substring(line.indexOf("=") + 2, line.length()));
				line = br.readLine();
				winStepTxt.setText(line.substring(line.indexOf("=") + 2, line.length()));
				br.close();
			}else{
				winLenTxt.setText("");
				winStepTxt.setText("");
			}
		}catch (IOException e){
			logger.fatal(e.getMessage());
		}

	}

	private void reloadProjectFromFile(){
		String pathTemp = projectInfo.getPath() + "project.pro";
		projectInfo.createFile(pathTemp);
		if(!projectInfo.getFile().exists()){
			JOptionPane.showMessageDialog(MainFrame.this, "There was an error finding your project file!"
					+ "\nIt may be deleted."
					+ "\n\nRerun the application and try other projects...");
			logger.fatal("System has to be exited, because the project file was not found!!!");
			System.exit(-1);
		}

		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(projectInfo.getFile()));
			String line = br.readLine();
			projectInfo.setName(line.substring(Messages.projectName.length()));

			setTitle(ProgTitle + " " + ProgVersion);

			line = br.readLine();
			//projectInfo.setPath(pathTemp);

			line = br.readLine();
			if (line == null){
				hasSeqNum = false;
				return;
			}else{
				hasSeqNum = true;
			}

			projectInfo.setSequenceLen(Integer.parseInt(line.substring(Messages.projectSequenceLen.length())));
			MainFrame.this.sequenceLenTxt.setText(projectInfo.getSequenceLen() + "");
			setSequenceLengthText(projectInfo.getSequenceLen());

			line = br.readLine();
			int libCount = 0;
			while(line != null){
				line = br.readLine();
				line = br.readLine();
				line = br.readLine();
				line = br.readLine();

				libCount++;
			}

			libraryCountLbl.setText(libCount + "");

			File projectDir = new File(projectInfo.getPath());
			File[] matches = projectDir.listFiles(new FilenameFilter() {
				public boolean accept(File arg0, String arg1) {
					return (arg1.endsWith(".table.xls")); //REPLACE
				}
			});
			dataTableCountLbl.setText(matches.length + "");

		}catch(IOException e){
			logger.error(e.getMessage());
			return;
		}finally{
			try{
				br.close();
			}catch(IOException e){
				logger.error(e.getMessage());
				return;
			}
		}

	}

	private void setToDefaultState(){
		samFilePathTxt.setText("");
		browseForSamBtn.setEnabled(true);

		extractInsBtn.setEnabled(false);
		extractInsBtn.setText("Extract");

		extractInsLbl.setForeground(Color.gray);
		sortInsLbl.setForeground(Color.gray);
		countUniLbl.setForeground(Color.gray);
		sortUniLbl.setForeground(Color.gray);

		doneLbl1.setVisible(false);
		doneLbl2.setVisible(false);
		doneLbl3.setVisible(false);
		doneLbl4.setVisible(false);
		loadingLbl.setVisible(false);
	}

	// 1
	private class ExtractInsertions implements Runnable{
		private int uniqueInsertionsLimit;
		public ExtractInsertions(int uniqueInsertionsLimit) {
			this.uniqueInsertionsLimit = uniqueInsertionsLimit;
		}
		
		@Override
		public void run() {
			String result = PrepareFiles.findTheLocationsInTheSamFile(MainFrame.this.samFilePathTxt.getText(), 
					projectInfo.getPath(),
					projectInfo.getSequenceLen());
			if(result.compareTo(Messages.successMsg) == 0){
				MainFrame.this.extractInsLbl.setForeground(Color.black);
				MainFrame.this.sortInsLbl.setForeground(Color.BLUE);

				doneLbl1.setVisible(true);
				loadingLbl.setBounds(22, 239, 30, 16);

				SortInsertions run = new SortInsertions(uniqueInsertionsLimit);
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with extracting insertions from your file.");
			}
		}
	}

	// Step 2 - Creating Inspos File
	private class SortInsertions implements Runnable{
		
		private int uniqueInsertionsLimit;
		public SortInsertions(int uniqueInsertionsLimit) {
			this.uniqueInsertionsLimit = uniqueInsertionsLimit;
		}
		
		@Override
		public void run() {
			String result = PrepareFiles.sortTheLocationsFile(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath());
			if(result.compareTo(Messages.successMsg) == 0){
				String tailLine = MyFileUtil.tail(new File(PrepareFiles.prepareOutputFilePath(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath(), ".inspos")));

				if (Integer.parseInt(tailLine) > projectInfo.getSequenceLen()){
					File inspos = new File(PrepareFiles.prepareOutputFilePath(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath(), ".inspos"));
					File inspo = new File(PrepareFiles.prepareOutputFilePath(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath(), ".inspo"));

					if(inspo.delete()){
						if(inspos.delete()){
							JOptionPane.showMessageDialog(MainFrame.this, ""
									+ "ERROR: insertions were found at positions above the provided sequence length. The library\n"
									+ "was not created.\n"
									+ "\n"
									+ "Possible reasons and solutions:\n"
									+ "- The sequence length is incorrect. Verify the sequence length and if necessary correct it in the\n"
									+ "'Main' tab.\n"
									+ "- You have used a wrong DNA sequence when running the Barrows-WheelerAligner to locate\n"
									+ "the insertions and generate the .sam file. Run BWA with the correct sequence.\n"
									+ "- Some of the input files are not formatted properly. Verify that all input files, including the\n\n"
									+ "DNA sequence used for BWA, have the correct format."
									+ "This error can also occur if you did not push 'apply' to enter the sequence length prior to continuing", "ERROR", JOptionPane.ERROR_MESSAGE);

							clearTheForm();
							return;
						}
					}

					return;
				}

				MainFrame.this.sortInsLbl.setForeground(Color.black);
				MainFrame.this.countUniLbl.setForeground(Color.BLUE);

				doneLbl2.setVisible(true);
				loadingLbl.setBounds(22, 258, 30, 16);

				CountUnique run = new CountUnique(uniqueInsertionsLimit);
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with sorting extracted insertions in your file.");
			}
		}
	}

	// Step 3 - Create InspoU File
	private class CountUnique implements Runnable{
		
		private int uniqueInsertionsLimit;
		
		public CountUnique(int uniqueInsertionsLimit){
			this.uniqueInsertionsLimit = uniqueInsertionsLimit;
		}
		
		@Override
		public void run() {
			String result = PrepareFiles.countUniqueLocations(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath(), uniqueInsertionsLimit);
			if(result.compareTo(Messages.successMsg) == 0){

				MainFrame.this.sortUniLbl.setForeground(Color.BLUE);
				MainFrame.this.countUniLbl.setForeground(Color.black);

				doneLbl3.setVisible(true);
				loadingLbl.setBounds(22, 277, 30, 16);

				SortByNumberOfInsertions run = new SortByNumberOfInsertions();
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with sorting extracted insertions in your file.");
			}
		}
	}


	// Step 4 - Creating InspoUs File
	private class SortByNumberOfInsertions implements Runnable{
		@Override
		public void run() {
			String result = PrepareFiles.countUniqueLocationsSortedByNumbers(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath());
			if(result.compareTo(Messages.successMsg) == 0){
				MainFrame.this.sortUniLbl.setForeground(Color.black);
				MainFrame.this.browseForSamBtn.setEnabled(false);	
				BufferedWriter bw = null;

				try{
					bw = new BufferedWriter(new FileWriter(projectInfo.getFile(), true));

					bw.write(PrepareFiles.prepareFileName(samFilePathTxt.getText(), ".inspo") + "\n");
					bw.write(PrepareFiles.prepareFileName(samFilePathTxt.getText(), ".inspos") + "\n");
					bw.write(PrepareFiles.prepareFileName(samFilePathTxt.getText(), ".inspou") + "\n");
					bw.write(PrepareFiles.prepareFileName(samFilePathTxt.getText(), ".inspous") + "\n");

					bw.close();

					doneLbl4.setVisible(true);
					loadingLbl.setVisible(false);

					String libraryName = JOptionPane.showInputDialog(MainFrame.this, "Enter the library's name:", 
							PrepareFiles.prepareFileName(samFilePathTxt.getText(), ""));
					renameLibrary(libraryName, PrepareFiles.prepareFileName(samFilePathTxt.getText(), ""));

					JOptionPane.showMessageDialog(MainFrame.this, "Library added to the project.");
					libraryCountLbl.setText((Integer.parseInt(libraryCountLbl.getText()) + 1) + "");

				}catch(IOException e){
					logger.error(e.getMessage());
					return;
				}finally{
					try{
						bw.close();
						initiateLibraryComboBox();
					}catch(IOException e){
						logger.error(e.getMessage());
						return;
					}
				}
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with sorting extracted insertions in your file.");
			}
		}
	}
	
	public void changeWinLenTo(String txt){
		winlenLbl.setText(txt);
	}
	
	public void makeWinlenLblVisible(boolean isVisible){
		btnOptimal.setVisible(!isVisible);
		winlenLbl.setVisible(isVisible);
	}
}
