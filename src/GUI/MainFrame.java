package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;

import essgenes.AddColumns;
import essgenes.Messages;
import essgenes.PlotData;
import essgenes.PrepareFiles;
import essgenes.ProjectInfo;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JTextField sequenceLenTxt;
	private JLabel libraryCountLbl = new JLabel("0");
	private JTextField samFilePathTxt;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JButton browseForSamBtn = new JButton("Browse");
	private JButton extractInsBtn = new JButton("Extract");
	private JButton cancelLibSaveBtn = new JButton("New Library");
	private JLabel extractInsLbl = new JLabel("Extract insertion positions from the \"SAM\" file");
	private JLabel sortUniLbl = new JLabel("Sort unique insertion based on number of occurrence");
	private JLabel sortInsLbl = new JLabel("Sort the extracted insertion positions");
	private JLabel countUniLbl = new JLabel("Count unique insertions and sort it by positio");
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
	private JButton btnPrepareGeneFile = new JButton("Prepare Gene File");
	private JRadioButton ownRadioBtn = new JRadioButton("Select Your Own Gene:");
	private JRadioButton databaseRadioBtn = new JRadioButton("Select Your Gene From a Database:");
	private JLabel geneFileNameLbl = new JLabel("NONE");
	private JComboBox<String> dataTableCombo = new JComboBox<String>();
	private JButton dataTableRenameBtn = new JButton("Rename");
	private JButton dataTableRemoveBtn = new JButton("Remove");
	private JButton openAsSpreadsheetBtn = new JButton("Open as Spreadsheet");
	private JButton addNewDataTableBtn = new JButton("Add New");
	private JButton replaceXlsBtn = new JButton("Replace");
	private JLabel dataTableCountLbl = new JLabel("0");
	private JButton plotBtn = new JButton("Plot");
	private JComboBox<String> plotLibraryCombo = new JComboBox<String>();
	private JPanel chartPanel = new JPanel();
	private JComboBox<String> addLibraryCombo = new JComboBox<String>();
	private JComboBox<String> addTableCombo = new JComboBox<String>();
	private JButton addMoreColumnsBtn = new JButton("Add");
	private final JPanel panel_1 = new JPanel();
	private JTextField winLenTxt;
	private JTextField winStepTxt;
	private JTextField addWinLenTxt;
	private JTextField addStepTxt;
	private JTextField adjustStartTxt;
	private JTextField adjustEndTxt;
	private JTextField addSeqLenTxt;
	private JLabel doneLbl1 = new JLabel("");
	private JLabel doneLbl2 = new JLabel("");
	private JLabel doneLbl3 = new JLabel("");
	private JLabel doneLbl4 = new JLabel("");
	private JButton tableCancelChangeBtn = new JButton("Cancel");
	private JRadioButton imgRadioBtn = new JRadioButton("Select IMG File:");
	private JTextField imgFileTxt;
	private JButton imgBrowseBtn = new JButton("Browse");
	private JComboBox<String> scaffoldCombo = new JComboBox<String>();
	private JTextField bwaExeTxt;
	private JTextField samtoolsExeTxt;
	private JButton bwaBrowseBtn = new JButton("Browse");
	private JRadioButton alreadyInstalledRadio = new JRadioButton("Already installed");
	private JRadioButton useScriptsRadio = new JRadioButton("Use the shell scripts");
	private JRadioButton pointToExeRadio = new JRadioButton("Point to executables");
	private JButton samtoolsInstallBtn = new JButton("Install SamTools");		
	private JButton bwaInstallBtn = new JButton("Install BWA");		
	private JButton samtoolsBrowseBtn = new JButton("Browse");

	protected ProjectInfo projectInfo = new ProjectInfo();
	private Logger logger = Logger.getLogger(MainFrame.class.getName());
	private boolean hasSeqNum = false;
	private boolean hasGeneFile = false;
	private boolean downloadOption = false;
	
	/**
	 * Create the frame.
	 */
	public MainFrame(String projectPath) {
		setResizable(false);
		this.projectInfo.setPath(projectPath);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 681);
		setLocationRelativeTo(null);	
		getContentPane().setLayout(null);

		tabbedPane.setBounds(12, 13, 722, 608);
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
					initiatePlotLibraryComboBox();
				}

				if(selectedTab == 4){
					initializeAddPanel();
				}
				
				if(selectedTab == 5){
					initializeBWAPanel();
				}
			}
		});

		JPanel panelMain = new JPanel();
		tabbedPane.addTab("Main", null, panelMain, null);
		panelMain.setLayout(null);

		JLabel lblSequenceLength = new JLabel("Sequence Length:");
		lblSequenceLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSequenceLength.setBounds(12, 13, 173, 20);
		panelMain.add(lblSequenceLength);

		sequenceLenTxt = new JTextField();
		sequenceLenTxt.setBounds(163, 13, 150, 22);
		panelMain.add(sequenceLenTxt);
		sequenceLenTxt.setColumns(10);

		JButton applySequenceBtn = new JButton("Apply");
		applySequenceBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		applySequenceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String seqLen = sequenceLenTxt.getText();
				if (seqLen == null || seqLen.compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please Enter Your Sequence Number First!");
					return;
				}

				int seqLenInt;
				try{
					seqLenInt = Integer.parseInt(seqLen);
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(MainFrame.this, "Please Enter Your Sequence Number Correctly!");
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
				JOptionPane.showMessageDialog(MainFrame.this, "New Sequence Length Has Been Applied!");

				if (hasGeneFile){
					for (int i = 1; i < tabbedPane.getTabCount(); i++){
						tabbedPane.setEnabledAt(i, true);
					}
				}
			}
		});
		applySequenceBtn.setBounds(325, 12, 97, 25);
		panelMain.add(applySequenceBtn);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 62, 717, 2);
		panelMain.add(separator);

		JLabel lblProjectInformation = new JLabel("Project Information:");
		lblProjectInformation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProjectInformation.setBounds(12, 520, 144, 20);
		panelMain.add(lblProjectInformation);

		JLabel lblLibrariesCount = new JLabel("Libraries Count:");
		lblLibrariesCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLibrariesCount.setBounds(12, 541, 163, 22);
		panelMain.add(lblLibrariesCount);

		libraryCountLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		libraryCountLbl.setBounds(157, 536, 51, 33);
		panelMain.add(libraryCountLbl);

		JLabel lblDataTablesCount = new JLabel("Data Tables Count:");
		lblDataTablesCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDataTablesCount.setBounds(207, 541, 163, 22);
		panelMain.add(lblDataTablesCount);

		dataTableCountLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dataTableCountLbl.setBounds(371, 536, 51, 33);
		panelMain.add(dataTableCountLbl);

		ftpFirstLevelCombo.setBounds(12, 110, 588, 22);

		panelMain.add(ftpFirstLevelCombo);
		ftpSecondLevelCombo.setEnabled(false);

		ftpSecondLevelCombo.setBounds(12, 145, 588, 22);
		panelMain.add(ftpSecondLevelCombo);
		downloadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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

							downloadBtn.setText("Downloaded");
							downloadBtn.setEnabled(false);
							ftpFirstLevelCombo.setEnabled(false);
							ftpSecondLevelCombo.setEnabled(false);

						} catch (IOException e) {
							logger.error(e.getMessage());
							return;
						}						
					}
				})).start();

			}
		});

		downloadBtn.setBounds(610, 144, 97, 25);
		panelMain.add(downloadBtn);

		JLabel lblPttFile = new JLabel("PTT File:");
		lblPttFile.setBounds(12, 235, 79, 16);
		panelMain.add(lblPttFile);

		JLabel lblRntFile = new JLabel("RNT File:");
		lblRntFile.setBounds(12, 267, 79, 16);
		panelMain.add(lblRntFile);

		pttFileTxt = new JTextField();
		pttFileTxt.setBounds(85, 232, 515, 22);
		panelMain.add(pttFileTxt);
		pttFileTxt.setColumns(10);

		rntFileTxt = new JTextField();
		rntFileTxt.setColumns(10);
		rntFileTxt.setBounds(85, 264, 515, 22);
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

		pttBrowseBtn.setBounds(610, 231, 97, 25);
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

		rntBrowseBtn.setBounds(610, 263, 97, 25);
		panelMain.add(rntBrowseBtn);

		JLabel lblOr = new JLabel("Or");
		lblOr.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOr.setBounds(12, 168, 51, 35);
		panelMain.add(lblOr);
		btnPrepareGeneFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				

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
							JOptionPane.showMessageDialog(MainFrame.this, "Genes file was created successfully.");
							geneFileNameLbl.setText(scaffoldCombo.getSelectedItem() + ".genes");
						} catch (IOException e) {
							geneFileNameLbl.setText("ERROR");
							logger.error(e.getMessage());
							return;
						}
					}
				}else{
					String pttPath = pttFileTxt.getText();
					if (pttPath != null && pttPath.compareTo("") != 0){
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
										geneFileNameLbl.setText(PrepareFiles.prepareFileName(projectInfo.getGeneFile().getAbsolutePath(), ".genes"));
										JOptionPane.showMessageDialog(MainFrame.this, "Genes File Created Successfully");
										PrepareFiles.deleteAllOtherGenesFiles(MainFrame.this.projectInfo.getGeneFile().getName(), MainFrame.this.projectInfo);

										if (hasSeqNum){
											for (int i = 1; i < tabbedPane.getTabCount(); i++){
												tabbedPane.setEnabledAt(i, true);
											}
										}

										if(projectInfo.getGeneFile() != null){
											geneFileNameLbl.setText(projectInfo.getGeneFile().getName());
										}else{
											geneFileNameLbl.setText("Not Avail.");
										}
									}else{
										JOptionPane.showMessageDialog(MainFrame.this, "There was an error craeting your Genes File.");
										geneFileNameLbl.setText("ERROR");
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
		});

		btnPrepareGeneFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPrepareGeneFile.setBounds(285, 450, 182, 25);
		panelMain.add(btnPrepareGeneFile);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(52, 187, 665, 2);
		panelMain.add(separator_3);

		buttonGroup.add(databaseRadioBtn);
		databaseRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		databaseRadioBtn.setBounds(10, 76, 410, 25);
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

				downloadOption = true;
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
			@Override
			public void itemStateChanged(ItemEvent e) {

				if(e.getStateChange() == ItemEvent.SELECTED){
					ftpSecondLevelCombo.removeAllItems();
					ftpSecondLevelCombo.addItem("Retieving Files List, Please Wait");

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

										String itemString = String.format("%s: %s (%s)", fileName, bacteriaName, lengthString);
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
					})).start();	
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
		ownRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ownRadioBtn.setBounds(12, 201, 410, 25);
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

				downloadOption = false;

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

		JLabel lblGenesFileName = new JLabel("Genes File Name:");
		lblGenesFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblGenesFileName.setBounds(427, 541, 163, 22);
		panelMain.add(lblGenesFileName);

		geneFileNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		geneFileNameLbl.setBounds(582, 536, 123, 33);
		panelMain.add(geneFileNameLbl);

		JLabel label = new JLabel("Or");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setBounds(12, 294, 51, 35);
		panelMain.add(label);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(52, 313, 665, 2);
		panelMain.add(separator_4);

		buttonGroup.add(imgRadioBtn);
		imgRadioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ftpFirstLevelCombo.setEnabled(false);
				ftpSecondLevelCombo.setEnabled(false);
				downloadBtn.setEnabled(false);

				pttBrowseBtn.setEnabled(false);
				pttFileTxt.setEnabled(false);
				rntBrowseBtn.setEnabled(false);
				rntFileTxt.setEnabled(false);

				downloadOption = false;

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
		imgRadioBtn.setBounds(12, 322, 410, 25);
		panelMain.add(imgRadioBtn);

		JLabel lblImgFile = new JLabel("IMG File:");
		lblImgFile.setBounds(12, 382, 79, 16);
		panelMain.add(lblImgFile);

		imgFileTxt = new JTextField();
		imgFileTxt.setEditable(false);
		imgFileTxt.setEnabled(false);
		imgFileTxt.setColumns(10);
		imgFileTxt.setBounds(138, 379, 462, 22);
		panelMain.add(imgFileTxt);
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

		imgBrowseBtn.setBounds(610, 378, 97, 25);
		panelMain.add(imgBrowseBtn);

		JLabel lblSelectYourScaffold = new JLabel("Select Your Scaffold:");
		lblSelectYourScaffold.setBounds(12, 412, 123, 16);
		panelMain.add(lblSelectYourScaffold);
		scaffoldCombo.setEnabled(false);

		scaffoldCombo.setBounds(138, 410, 462, 20);
		panelMain.add(scaffoldCombo);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(0, 507, 717, 2);
		panelMain.add(separator_7);
		
		JLabel lblyouCanDownload = new JLabel("You can download the IMG file by clicking on this link");
		lblyouCanDownload.setBounds(12, 354, 695, 16);
		panelMain.add(lblyouCanDownload);

		JPanel panelInitialize = new JPanel();
		tabbedPane.addTab("Manage Libraries", null, panelInitialize, null);
		panelInitialize.setLayout(null);

		JLabel lblBrowseFoThe = new JLabel("Add New Library to the Project:");
		lblBrowseFoThe.setBounds(12, 13, 311, 16);
		panelInitialize.add(lblBrowseFoThe);

		samFilePathTxt = new JTextField();
		samFilePathTxt.setBounds(12, 42, 477, 22);
		panelInitialize.add(samFilePathTxt);
		samFilePathTxt.setColumns(10);

		browseForSamBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setFileFilter(new FileNameExtensionFilter("SAM Files (.sam)", "sam"));
				int result = fileChooser.showOpenDialog(MainFrame.this);

				if (result == JFileChooser.APPROVE_OPTION){
					MainFrame.this.samFilePathTxt.setText(fileChooser.getSelectedFile().getAbsolutePath());
					MainFrame.this.extractInsBtn.setEnabled(true);
					MainFrame.this.samFilePathTxt.setEnabled(false);
					MainFrame.this.browseForSamBtn.setEnabled(false);
				}
			}
		});
		browseForSamBtn.setBounds(499, 41, 99, 25);
		panelInitialize.add(browseForSamBtn);

		extractInsLbl.setForeground(Color.LIGHT_GRAY);
		extractInsLbl.setBounds(54, 83, 411, 16);
		panelInitialize.add(extractInsLbl);
		extractInsBtn.setEnabled(false);
		extractInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.this.extractInsLbl.setForeground(Color.BLUE);
				MainFrame.this.extractInsBtn.setEnabled(false);
				MainFrame.this.extractInsBtn.setText("Wait");

				loadingLbl.setBounds(22, 83, 30, 16);
				loadingLbl.setVisible(true);

				ExtractInsertions run = new ExtractInsertions();
				Thread runThread = new Thread(run);
				runThread.start();
			}
		});

		extractInsBtn.setBounds(608, 41, 99, 25);
		panelInitialize.add(extractInsBtn);

		sortInsLbl.setForeground(Color.LIGHT_GRAY);
		sortInsLbl.setBounds(54, 102, 411, 16);
		panelInitialize.add(sortInsLbl);

		countUniLbl.setForeground(Color.LIGHT_GRAY);
		countUniLbl.setBounds(54, 121, 411, 16);
		panelInitialize.add(countUniLbl);

		sortUniLbl.setForeground(Color.LIGHT_GRAY);
		sortUniLbl.setBounds(54, 140, 411, 16);
		panelInitialize.add(sortUniLbl);
		cancelLibSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				File tempFile = null;
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

				setToDefaultState();

			}
		});

		cancelLibSaveBtn.setBounds(585, 77, 122, 25);
		panelInitialize.add(cancelLibSaveBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 186, 717, 2);
		panelInitialize.add(separator_2);

		JLabel lblRemoveExistingLibraries = new JLabel("Edit Existing Libraries:");
		lblRemoveExistingLibraries.setBounds(10, 201, 313, 16);
		panelInitialize.add(lblRemoveExistingLibraries);

		libraryComboBox.setBounds(12, 230, 479, 22);
		panelInitialize.add(libraryComboBox);

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
		removeLibBtn.setBounds(610, 229, 97, 25);
		panelInitialize.add(removeLibBtn);

		renameLibBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = JOptionPane.showInputDialog(MainFrame.this, "Enter the New Name");
				String selectedLib = (String) libraryComboBox.getSelectedItem();

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
		});
		renameLibBtn.setBounds(501, 229, 97, 25);
		panelInitialize.add(renameLibBtn);

		loadingLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/GUI/resources/load.gif")));
		loadingLbl.setBounds(14, 83, 30, 16);
		panelInitialize.add(loadingLbl);

		doneLbl1.setIcon(new ImageIcon(MainFrame.class.getResource("/GUI/resources/done.gif")));
		doneLbl1.setBounds(22, 83, 23, 14);
		panelInitialize.add(doneLbl1);

		doneLbl2.setIcon(new ImageIcon(MainFrame.class.getResource("/GUI/resources/done.gif")));
		doneLbl2.setBounds(21, 103, 23, 14);
		panelInitialize.add(doneLbl2);

		doneLbl3.setIcon(new ImageIcon(MainFrame.class.getResource("/GUI/resources/done.gif")));
		doneLbl3.setBounds(21, 122, 23, 14);
		panelInitialize.add(doneLbl3);

		doneLbl4.setIcon(new ImageIcon(MainFrame.class.getResource("/GUI/resources/done.gif")));
		doneLbl4.setBounds(21, 141, 23, 14);
		panelInitialize.add(doneLbl4);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Manage Data Tables", null, panel, null);
		panel.setLayout(null);

		JLabel lblAddNewData = new JLabel("Add New Data Table To Your Project:");
		lblAddNewData.setBounds(10, 15, 290, 16);
		panel.add(lblAddNewData);

		addNewDataTableBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent arg0) {

				String name = JOptionPane.showInputDialog(MainFrame.this, "Enter New Table Name:");

				if(name == null || name.compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter table name!");
					return;
				}

				File newTable = new File(projectInfo.getPath() + name + ".table");
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
		addNewDataTableBtn.setBounds(318, 12, 113, 23);
		panel.add(addNewDataTableBtn);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(0, 43, 717, 8);
		panel.add(separator_5);

		JLabel lblOldLibraries = new JLabel("Manage Existing Libraries:");
		lblOldLibraries.setBounds(10, 62, 311, 16);
		panel.add(lblOldLibraries);

		dataTableCombo.setBounds(20, 89, 328, 20);
		panel.add(dataTableCombo);
		dataTableRenameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String newName = JOptionPane.showInputDialog(MainFrame.this, "Please enter the new name:");
				if(newName == null || newName.compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "You must enter the new name!");
					return;
				}

				String selectedTable = (String)dataTableCombo.getSelectedItem();
				File oldFile = new File(projectInfo.getPath() + selectedTable + ".table");
				File newFile = new File(projectInfo.getPath() + newName + ".table");

				boolean done = oldFile.renameTo(newFile);

				if(!done){
					JOptionPane.showMessageDialog(MainFrame.this, "Rename could not take part!");
					return;
				}

				initiateDataTablesComboBox();
			}
		});

		dataTableRenameBtn.setBounds(519, 88, 89, 23);
		panel.add(dataTableRenameBtn);
		dataTableRemoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File table = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".table");

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

		dataTableRemoveBtn.setBounds(618, 88, 89, 23);
		panel.add(dataTableRemoveBtn);
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

		openAsSpreadsheetBtn.setBounds(362, 88, 147, 23);
		panel.add(openAsSpreadsheetBtn);

		replaceXlsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replaceXlsFile();
			}
		});
		replaceXlsBtn.setEnabled(false);
		tableCancelChangeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				File xlsFile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".xls");
				if(xlsFile.delete()){

					for (int i = 0; i < tabbedPane.getTabCount(); i++){
						tabbedPane.setEnabledAt(i, true);
					}

					addNewDataTableBtn.setEnabled(true);
					openAsSpreadsheetBtn.setEnabled(true);
					dataTableRemoveBtn.setEnabled(true);
					dataTableRenameBtn.setEnabled(true);
					dataTableCombo.setEnabled(true);
					replaceXlsBtn.setEnabled(false);
					tableCancelChangeBtn.setEnabled(false);

				}else{
					JOptionPane.showMessageDialog(MainFrame.this, "Please close the spreadsheet application.", "Could not delete the temp file", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		tableCancelChangeBtn.setEnabled(false);
		replaceXlsBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		replaceXlsBtn.setBounds(362, 122, 147, 23);
		panel.add(replaceXlsBtn);
		tableCancelChangeBtn.setEnabled(false);

		tableCancelChangeBtn.setBounds(519, 122, 89, 23);
		panel.add(tableCancelChangeBtn);
		loadingLbl.setVisible(false);
		doneLbl1.setVisible(false);
		doneLbl2.setVisible(false);
		doneLbl3.setVisible(false);
		doneLbl4.setVisible(false);

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

		JMenu mnPlotter = new JMenu("Plotter");
		menuBar.add(mnPlotter);

		JMenuItem mntmPlot = new JMenuItem("Plot 1");
		mntmPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(null, "I want to plot");

			}
		});
		mnPlotter.add(mntmPlot);

		tabbedPane.addTab("Plotting", null, panel_1, null);
		panel_1.setLayout(null);

		JLabel lblChooseALibrary = new JLabel("Choose a Library to Plot:");
		lblChooseALibrary.setBounds(10, 11, 286, 14);
		panel_1.add(lblChooseALibrary);

		plotLibraryCombo.setBounds(10, 36, 400, 20);
		panel_1.add(plotLibraryCombo);
		plotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(winLenTxt.getText() == null || winLenTxt.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter a valid Window Length");
				}

				if(winStepTxt.getText() == null || winStepTxt.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(MainFrame.this, "Please enter a valid Step Size");
				}

				int len = Integer.parseInt(winLenTxt.getText());
				int step = Integer.parseInt(winStepTxt.getText());
				ChartPanel panel = new ChartPanel(PlotData.plotData((String) plotLibraryCombo.getSelectedItem(), len, step, projectInfo));

				chartPanel.setLayout(new BorderLayout());
				chartPanel.add(panel, BorderLayout.CENTER);
				chartPanel.validate();
			}
		});

		plotBtn.setBounds(618, 35, 89, 23);
		panel_1.add(plotBtn);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(0, 67, 717, 9);
		panel_1.add(separator_6);

		chartPanel.setBounds(10, 87, 697, 482);
		panel_1.add(chartPanel);

		winLenTxt = new JTextField();
		winLenTxt.setText("1000");
		winLenTxt.setBounds(420, 36, 89, 20);
		panel_1.add(winLenTxt);
		winLenTxt.setColumns(10);

		winStepTxt = new JTextField();
		winStepTxt.setText("100");
		winStepTxt.setColumns(10);
		winStepTxt.setBounds(519, 36, 89, 20);
		panel_1.add(winStepTxt);

		JLabel lblWindowLen = new JLabel("Window Len:");
		lblWindowLen.setBounds(406, 21, 89, 14);
		panel_1.add(lblWindowLen);

		JLabel lblStep = new JLabel("Step:");
		lblStep.setBounds(505, 21, 89, 14);
		panel_1.add(lblStep);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Add", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblChooseYourLibrary = new JLabel("Choose your library:");
		lblChooseYourLibrary.setBounds(10, 11, 137, 14);
		panel_2.add(lblChooseYourLibrary);

		addLibraryCombo.setBounds(157, 8, 451, 20);
		panel_2.add(addLibraryCombo);

		JLabel lblChooseYourTable = new JLabel("Choose your table:");
		lblChooseYourTable.setBounds(10, 42, 137, 14);
		panel_2.add(lblChooseYourTable);

		addTableCombo.setBounds(157, 39, 451, 20);
		panel_2.add(addTableCombo);

		addWinLenTxt = new JTextField();
		addWinLenTxt.setText("1000");
		addWinLenTxt.setBounds(157, 70, 86, 20);
		panel_2.add(addWinLenTxt);
		addWinLenTxt.setColumns(10);

		addStepTxt = new JTextField();
		addStepTxt.setText("100");
		addStepTxt.setBounds(400, 70, 86, 20);
		panel_2.add(addStepTxt);
		addStepTxt.setColumns(10);

		JLabel lblWindowLength = new JLabel("Window Length:");
		lblWindowLength.setBounds(10, 73, 137, 14);
		panel_2.add(lblWindowLength);

		JLabel lblStep_1 = new JLabel("Step:");
		lblStep_1.setBounds(253, 73, 137, 14);
		panel_2.add(lblStep_1);

		adjustStartTxt = new JTextField();
		adjustStartTxt.setText("%-20");
		adjustStartTxt.setBounds(157, 101, 86, 20);
		panel_2.add(adjustStartTxt);
		adjustStartTxt.setColumns(10);

		JLabel lblAdjustStart = new JLabel("Adjust Start:");
		lblAdjustStart.setBounds(10, 104, 137, 14);
		panel_2.add(lblAdjustStart);

		adjustEndTxt = new JTextField();
		adjustEndTxt.setText("%-20");
		adjustEndTxt.setBounds(400, 101, 86, 20);
		panel_2.add(adjustEndTxt);
		adjustEndTxt.setColumns(10);

		JLabel lblAdjustEnd = new JLabel("Adjust End:");
		lblAdjustEnd.setBounds(253, 104, 137, 14);
		panel_2.add(lblAdjustEnd);

		addSeqLenTxt = new JTextField();
		addSeqLenTxt.setEditable(false);
		addSeqLenTxt.setBounds(157, 132, 86, 20);
		panel_2.add(addSeqLenTxt);
		addSeqLenTxt.setColumns(10);

		JLabel lblSequenceLength_1 = new JLabel("Sequence Length:");
		lblSequenceLength_1.setBounds(10, 135, 137, 14);
		panel_2.add(lblSequenceLength_1);
		addMoreColumnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String libraryName = (String) addLibraryCombo.getSelectedItem();
				String tableName = (String) addTableCombo.getSelectedItem();
				int windowLen = Integer.parseInt(addWinLenTxt.getText());
				int step = Integer.parseInt(addStepTxt.getText());
				String adjStart = adjustStartTxt.getText();
				String adjEnd = adjustEndTxt.getText();
				int seqLen = projectInfo.getSequenceLen();

				try {
					if (AddColumns.add(libraryName, tableName, windowLen, step, adjStart, adjEnd, seqLen, projectInfo).compareTo(Messages.successMsg) == 0){
						JOptionPane.showMessageDialog(MainFrame.this, "Data added");
					}else{
						JOptionPane.showMessageDialog(MainFrame.this, "There was some problem, data was not added!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					logger.error(e.getMessage());
					return;
				}
			}
		});

		addMoreColumnsBtn.setBounds(618, 131, 89, 23);
		panel_2.add(addMoreColumnsBtn);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("BWA", null, panel_3, null);
		panel_3.setLayout(null);
		
		bwaExeTxt = new JTextField();
		bwaExeTxt.setEnabled(false);
		bwaExeTxt.setEditable(false);
		bwaExeTxt.setBounds(207, 229, 399, 20);
		panel_3.add(bwaExeTxt);
		bwaExeTxt.setColumns(10);
		
		bwaBrowseBtn.setBounds(618, 227, 89, 23);
		panel_3.add(bwaBrowseBtn);
		
		JLabel lblNewLabel = new JLabel("1. If you already have the 'bwa' and 'samtools' installed on your machine you can use this part.");
		lblNewLabel.setBounds(10, 11, 697, 14);
		panel_3.add(lblNewLabel);
		
		JLabel lblIfYouDont = new JLabel("2. If you don't have them, you can use the Shell Scripts below to autommatically install them");
		lblIfYouDont.setBounds(10, 37, 745, 14);
		panel_3.add(lblIfYouDont);
		
		JLabel lblYouCanAlso = new JLabel("3. You can also, point to the 'bwa' and 'samtools' executable files on you machine using the");
		lblYouCanAlso.setBounds(10, 80, 745, 14);
		panel_3.add(lblYouCanAlso);
		alreadyInstalledRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bwaInstallBtn.setEnabled(false);
				samtoolsInstallBtn.setEnabled(false);
				
				bwaBrowseBtn.setEnabled(false);
				samtoolsBrowseBtn.setEnabled(false);
				
				bwaExeTxt.setEnabled(false);
				samtoolsExeTxt.setEnabled(false);
				
				alreadyInstalledRadio.setSelected(true);
			}
		});
		
		alreadyInstalledRadio.setSelected(true);
		alreadyInstalledRadio.setBounds(10, 150, 191, 23);
		panel_3.add(alreadyInstalledRadio);
		useScriptsRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bwaInstallBtn.setEnabled(true);
				samtoolsInstallBtn.setEnabled(true);
				
				bwaBrowseBtn.setEnabled(false);
				samtoolsBrowseBtn.setEnabled(false);
				
				bwaExeTxt.setEnabled(false);
				samtoolsExeTxt.setEnabled(false);
			}
		});
	
		useScriptsRadio.setBounds(10, 176, 191, 23);
		panel_3.add(useScriptsRadio);
		pointToExeRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bwaInstallBtn.setEnabled(false);
				samtoolsInstallBtn.setEnabled(false);
				
				bwaBrowseBtn.setEnabled(true);
				samtoolsBrowseBtn.setEnabled(true);
				
				bwaExeTxt.setEnabled(true);
				samtoolsExeTxt.setEnabled(true);
			}
		});
	
		pointToExeRadio.setBounds(10, 202, 191, 23);
		panel_3.add(pointToExeRadio);
		samtoolsInstallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					String cmd[] = {"gnome-terminal", "-x", "bash", "-c", "echo 'Please Enter Your Root Password';"
							+ "su -m root -c 'sh install-samstools.sh';"
							+ "echo;"
							+ "echo;"
							+ "echo 'Press Any Key To Continue...';"
							+ "read"};
					
					Path currentRelativePath = Paths.get("");
					String location = currentRelativePath.toAbsolutePath()
							.toString() + "/Linux/";
					File dir = new File(location);
					Process child = Runtime.getRuntime().exec(cmd, null, dir);
					child.waitFor();
				} catch (IOException | InterruptedException e1) {
					logger.error(e1.getMessage());
					return;
				}
			}
		});

		samtoolsInstallBtn.setBounds(341, 176, 124, 23);
		panel_3.add(samtoolsInstallBtn);
		bwaInstallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String cmd[] = {"gnome-terminal", "-x", "bash", "-c", "echo 'Please Enter Your Root Password';"
							+ "su -m root -c 'sh install-bwa.sh';"
							+ "echo;"
							+ "echo;"
							+ "echo 'Press Any Key To Continue...';"
							+ "read"};
					
					Path currentRelativePath = Paths.get("");
					String location = currentRelativePath.toAbsolutePath()
							.toString() + "/Linux/";
					File dir = new File(location);
					Process child = Runtime.getRuntime().exec(cmd, null, dir);
					child.waitFor();
				} catch (IOException | InterruptedException e1) {
					logger.error(e1.getMessage());
					return;
				}
			}
		});

		bwaInstallBtn.setBounds(207, 176, 124, 23);
		panel_3.add(bwaInstallBtn);
		
		JLabel lblBwaExecutable = new JLabel("BWA Executable:");
		lblBwaExecutable.setBounds(20, 232, 109, 14);
		panel_3.add(lblBwaExecutable);
		
		JLabel lblSamtoolsExecutable = new JLabel("SamTools Executable:");
		lblSamtoolsExecutable.setBounds(20, 264, 158, 14);
		panel_3.add(lblSamtoolsExecutable);
		
		samtoolsExeTxt = new JTextField();
		samtoolsExeTxt.setEnabled(false);
		samtoolsExeTxt.setEditable(false);
		samtoolsExeTxt.setColumns(10);
		samtoolsExeTxt.setBounds(207, 261, 399, 20);
		panel_3.add(samtoolsExeTxt);

		samtoolsBrowseBtn.setBounds(618, 260, 89, 23);
		panel_3.add(samtoolsBrowseBtn);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(0, 304, 717, 5);
		panel_3.add(separator_8);
		reloadProjectFromFile();
		hasGeneFile = findGeneFile();
		if(!hasSeqNum  || !hasGeneFile){
			for (int i = 1; i < tabbedPane.getTabCount(); i++){
				tabbedPane.setEnabledAt(i, false);
			}
		}
		
		ButtonGroup bwaGroup = new ButtonGroup();
		bwaGroup.add(alreadyInstalledRadio);
		bwaGroup.add(pointToExeRadio);
		bwaGroup.add(useScriptsRadio);
		
		JLabel lblNewLabel_1 = new JLabel(" on your machine .");
		lblNewLabel_1.setBounds(20, 58, 246, 15);
		panel_3.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(" browse buttons.");
		lblNewLabel_2.setBounds(20, 101, 532, 15);
		panel_3.add(lblNewLabel_2);
	}

	@SuppressWarnings("resource")
	private void replaceXlsFile(){

		File xlsFile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".xls");
		File tablefile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".table");

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

		xlsFile = new File("temp.table");

		try{
			bw = new BufferedWriter(new FileWriter(xlsFile));
			br = new BufferedReader(new FileReader(tablefile));

			bw.write("\n");
			bw.write("\n");
			bw.write("\n");
			bw.write("\n");

			String tempLine = br.readLine();
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

		addNewDataTableBtn.setEnabled(true);
		openAsSpreadsheetBtn.setEnabled(true);
		dataTableRemoveBtn.setEnabled(true);
		dataTableRenameBtn.setEnabled(true);
		dataTableCombo.setEnabled(true);
		replaceXlsBtn.setEnabled(false);
		tableCancelChangeBtn.setEnabled(false);
	}

	@SuppressWarnings("resource")
	private void prepareXlsFileAndOpen() throws IOException{

		String selectedItem = (String)dataTableCombo.getSelectedItem();

		String warningMsg = String.format("Please after editing the table save it as tab delimited file and "
				+ "replace the %s.xls file in the project path. After saving the file you MUST press the \"Replace\" button to "
				+ "let the application know about the changes.", selectedItem);

		JOptionPane.showMessageDialog(MainFrame.this, warningMsg, "Warning", JOptionPane.WARNING_MESSAGE);

		File selectedFile = new File(projectInfo.getPath() + selectedItem + ".table");
		File xlsFile = new File(projectInfo.getPath() + selectedItem + ".xls");

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

		Desktop.getDesktop().edit(xlsFile);

		for (int i = 0; i < tabbedPane.getTabCount(); i++){
			tabbedPane.setEnabledAt(i, false);
		}

		addNewDataTableBtn.setEnabled(false);
		openAsSpreadsheetBtn.setEnabled(false);
		dataTableRemoveBtn.setEnabled(false);
		dataTableRenameBtn.setEnabled(false);
		dataTableCombo.setEnabled(false);
		replaceXlsBtn.setEnabled(true);
		tableCancelChangeBtn.setEnabled(true);

	}

	private void initializeBWAPanel(){
		
		String OSName = System.getProperty("os.name");
		
		if(OSName.contains("Windows") || OSName.contains("windows")){
			for (Component c : ((JPanel)tabbedPane.getSelectedComponent()).getComponents()){
				c.setEnabled(false);
			}
			
			JOptionPane.showMessageDialog(null, "This tab is only available when you are using Linux!!!");
			
		}else{
			bwaInstallBtn.setEnabled(false);
			samtoolsInstallBtn.setEnabled(false);
			
			bwaBrowseBtn.setEnabled(false);
			samtoolsBrowseBtn.setEnabled(false);
			
			bwaExeTxt.setEnabled(false);
			samtoolsExeTxt.setEnabled(false);
			
			alreadyInstalledRadio.setSelected(true);
		}
		
	}
	
	private void initializeAddPanel(){

		//Initializing Libraries ComboBox
		BufferedReader br = null;

		if(addLibraryCombo != null){
			addLibraryCombo.removeAllItems();
		}

		try{
			br = new BufferedReader(new FileReader(projectInfo.getFile()));

			String line = br.readLine();
			br.readLine();
			br.readLine();

			while(line != null){
				line = br.readLine();

				if (line != null)
					addLibraryCombo.addItem(line.substring(0, line.length() - 6));

				line = br.readLine();
				line = br.readLine();
				line = br.readLine();
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

		//Initializing Tables ComboBox
		File projectDir = new File(projectInfo.getPath());
		File[] tableFiles = projectDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return (name.endsWith(".table") || name.endsWith(".Table"));
			}
		});

		if (tableFiles != null && tableFiles.length != 0){
			addTableCombo.removeAllItems();
			for (File t : tableFiles){
				addTableCombo.addItem(t.getName().substring(0, t.getName().length() - 6));
			}
		}

		addSeqLenTxt.setText(projectInfo.getSequenceLen() + "");
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
			}else{
				plotBtn.setEnabled(true);
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
				return (name.endsWith(".table") || name.endsWith(".Table"));
			}
		});

		if (tableFiles != null && tableFiles.length != 0){
			dataTableRenameBtn.setEnabled(true);
			dataTableRemoveBtn.setEnabled(true);
			dataTableCombo.setEnabled(true);
			openAsSpreadsheetBtn.setEnabled(true);

			dataTableCombo.removeAllItems();
			for (File t : tableFiles){
				dataTableCombo.addItem(t.getName().substring(0, t.getName().length() - 6));
			}
		}else{
			dataTableCombo.removeAllItems();
			dataTableCombo.setEnabled(false);
			dataTableCombo.addItem("No existing data table.");
			dataTableRenameBtn.setEnabled(false);
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

		String geneFilePath = PrepareFiles.createGeneFile(pttPath, rntPath, projectInfo.getPath()); 
		if(geneFilePath.compareTo(Messages.failMsg) != 0){
			hasGeneFile = true;
			projectInfo.setGeneFile(new File(geneFilePath));
		}else{
			hasGeneFile = false;
		}

	}

	private void initiateLibraryComboBox(){

		BufferedReader br = null;

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

			if (libraryComboBox.getItemCount() == 0){
				renameLibBtn.setEnabled(false);
				removeLibBtn.setEnabled(false);
			}else{
				renameLibBtn.setEnabled(true);
				removeLibBtn.setEnabled(true);
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

	private void reloadProjectFromFile(){		
		projectInfo.createFile(projectInfo.getPath() + "project.pro");
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

			setTitle("Essential Genes Finder - " + projectInfo.getName());

			line = br.readLine();
			projectInfo.setPath(line.substring(Messages.projectPath.length()));

			line = br.readLine();
			if (line == null){
				hasSeqNum = false;
				return;
			}else{
				hasSeqNum = true;
			}

			projectInfo.setSequenceLen(Integer.parseInt(line.substring(Messages.projectSequenceLen.length())));
			MainFrame.this.sequenceLenTxt.setText(projectInfo.getSequenceLen() + "");

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
					return (arg1.endsWith(".table"));
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
	}

	// 1
	private class ExtractInsertions implements Runnable{
		@Override
		public void run() {
			String result = PrepareFiles.findTheLocationsInTheSamFile(MainFrame.this.samFilePathTxt.getText(), 
					projectInfo.getPath(),
					projectInfo.getSequenceLen());
			if(result.compareTo(Messages.successMsg) == 0){
				MainFrame.this.extractInsLbl.setForeground(Color.black);
				MainFrame.this.sortInsLbl.setForeground(Color.BLUE);

				doneLbl1.setVisible(true);
				loadingLbl.setBounds(22, 102, 30, 16);

				SortInsertions run = new SortInsertions();
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with extracting insertions from your file.");
			}
		}
	}

	// 2
	private class SortInsertions implements Runnable{
		@Override
		public void run() {
			String result = PrepareFiles.sortTheLocationsFile(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath());
			if(result.compareTo(Messages.successMsg) == 0){
				MainFrame.this.sortInsLbl.setForeground(Color.black);
				MainFrame.this.countUniLbl.setForeground(Color.BLUE);

				doneLbl2.setVisible(true);
				loadingLbl.setBounds(22, 121, 30, 16);

				CountUnique run = new CountUnique();
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with sorting extracted insertions in your file.");
			}
		}
	}

	// 3
	private class CountUnique implements Runnable{
		@Override
		public void run() {
			String result = PrepareFiles.countUniqueLocations(MainFrame.this.samFilePathTxt.getText(), projectInfo.getPath());
			if(result.compareTo(Messages.successMsg) == 0){
				MainFrame.this.sortUniLbl.setForeground(Color.BLUE);
				MainFrame.this.countUniLbl.setForeground(Color.black);

				doneLbl3.setVisible(true);
				loadingLbl.setBounds(22, 140, 30, 16);

				SortByNumberOfInsertions run = new SortByNumberOfInsertions();
				Thread runThread = new Thread(run);
				runThread.start();
			}else{
				JOptionPane.showMessageDialog(MainFrame.this, "There was some error with sorting extracted insertions in your file.");
			}
		}
	}


	// 4
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

					doneLbl4.setVisible(true);
					loadingLbl.setVisible(false);
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
}
