package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.net.URL;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;

import essgenes.Messages;
import essgenes.MyFileUtil;
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
	private JButton addNewDataTableBtn = new JButton("Add new");
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
	private JButton samtoolsInstallBtn = new JButton("Install SamTools");		
	private JButton bwaInstallBtn = new JButton("Install BWA");	
	private JLabel infoLbl = new JLabel("* You have to provide the chromosome length and gene annotation to start a new project.");
	private JComboBox<String> plotLibraryCombo = new JComboBox<String>();
	private JTextField winLenTxt;
	private JTextField winStepTxt;
	private JButton plotBtn = new JButton("Plot");
	private JLabel plotWaitLbl = new JLabel("Please wait");
	private JButton addNewIndicesBtn = new JButton("Add new essentiality indices to a data table");

	protected ProjectInfo projectInfo = new ProjectInfo();
	private Logger logger = Logger.getLogger(MainFrame.class.getName());
	private boolean hasSeqNum = false;
	private boolean hasGeneFile = false;

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
					initializeBWAPanel();
				}
			}
		});

		JPanel panelMain = new JPanel();
		tabbedPane.addTab("Main", null, panelMain, null);
		panelMain.setLayout(null);

		JLabel lblSequenceLength = new JLabel("Sequence Length:");
		lblSequenceLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSequenceLength.setBounds(12, 31, 173, 20);
		panelMain.add(lblSequenceLength);

		sequenceLenTxt = new JTextField();
		sequenceLenTxt.setToolTipText("Enter sequence length to be used in this project");
		sequenceLenTxt.setBounds(157, 32, 150, 22);
		panelMain.add(sequenceLenTxt);
		sequenceLenTxt.setColumns(10);

		JButton applySequenceBtn = new JButton("Apply");
		applySequenceBtn.setToolTipText("Apply the entered sequence length");
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
					infoLbl.setVisible(false);
				}
			}
		});
		applySequenceBtn.setBounds(317, 30, 97, 25);
		panelMain.add(applySequenceBtn);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 62, 717, 2);
		panelMain.add(separator);

		JLabel lblProjectInformation = new JLabel("Project Information:");
		lblProjectInformation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProjectInformation.setBounds(12, 510, 144, 20);
		panelMain.add(lblProjectInformation);

		JLabel lblLibrariesCount = new JLabel("Libraries count:");
		lblLibrariesCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLibrariesCount.setBounds(12, 541, 163, 22);
		panelMain.add(lblLibrariesCount);

		libraryCountLbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		libraryCountLbl.setBounds(157, 536, 51, 33);
		panelMain.add(libraryCountLbl);

		JLabel lblDataTablesCount = new JLabel("Data tables count:");
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
						
						prepareGeneFile();
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
		btnPrepareGeneFile.setToolTipText("Process files and create the .gene file");
		btnPrepareGeneFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				

				prepareGeneFile();
			}
		});

		btnPrepareGeneFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPrepareGeneFile.setBounds(285, 450, 182, 25);
		panelMain.add(btnPrepareGeneFile);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(52, 187, 665, 2);
		panelMain.add(separator_3);

		buttonGroup.add(databaseRadioBtn);
		databaseRadioBtn.setToolTipText("Automatically download from the NCBI FTP Server");
		databaseRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		databaseRadioBtn.setBounds(10, 76, 360, 25);
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
		ownRadioBtn.setToolTipText("Use existing files from NCBI FTP server");
		ownRadioBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ownRadioBtn.setBounds(12, 201, 358, 25);
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
		imgRadioBtn.setBounds(12, 322, 358, 25);
		panelMain.add(imgRadioBtn);

		JLabel lblImgFile = new JLabel("IMG file:");
		lblImgFile.setBounds(12, 382, 79, 16);
		panelMain.add(lblImgFile);

		imgFileTxt = new JTextField();
		imgFileTxt.setToolTipText("Path to the IMG file, use the browse button");
		imgFileTxt.setEditable(false);
		imgFileTxt.setEnabled(false);
		imgFileTxt.setColumns(10);
		imgFileTxt.setBounds(148, 379, 452, 22);
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

		imgBrowseBtn.setBounds(610, 378, 97, 25);
		panelMain.add(imgBrowseBtn);

		JLabel lblSelectYourScaffold = new JLabel("Select a scaffold:");
		lblSelectYourScaffold.setBounds(12, 412, 112, 16);
		panelMain.add(lblSelectYourScaffold);
		scaffoldCombo.setToolTipText("Select the scaffold within the IMG file");
		scaffoldCombo.setEnabled(false);

		scaffoldCombo.setBounds(148, 410, 452, 20);
		panelMain.add(scaffoldCombo);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(0, 486, 717, 2);
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
				JOptionPane.showMessageDialog(MainFrame.this, "This will guide you to finding the protein-coding (.ptt file) and RNA-coding (.rnt file)\n"
						+ "annotation at the FTP server of the National Center for Biotechnology Information\n"
						+ "(ftp://ftp.ncbi.nih.gov/genomes/)");
			}
		});
		lblNeedHelp.setToolTipText("Click me!");
		lblNeedHelp.setForeground(Color.BLUE);
		lblNeedHelp.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNeedHelp.setBounds(371, 85, 88, 14);
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
		lblhelp.setBounds(371, 207, 88, 14);
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
		label_2.setBounds(371, 329, 88, 14);
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
		label_1.setBounds(104, 413, 88, 14);
		panelMain.add(label_1);

		JPanel panelInitialize = new JPanel();
		tabbedPane.addTab("Manage Libraries", null, panelInitialize, null);
		panelInitialize.setLayout(null);

		JLabel lblBrowseFoThe = new JLabel("Add new library to the project:");
		lblBrowseFoThe.setBounds(14, 124, 311, 16);
		panelInitialize.add(lblBrowseFoThe);

		samFilePathTxt = new JTextField();
		samFilePathTxt.setToolTipText("Path to the SAM file");
		samFilePathTxt.setBounds(14, 153, 477, 22);
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
						loadingLbl.setBounds(22, 214, 30, 16);
						extractInsLbl.setForeground(Color.BLACK);
					}
				}
			}
		});
		browseForSamBtn.setBounds(501, 152, 99, 25);
		panelInitialize.add(browseForSamBtn);

		extractInsLbl.setForeground(Color.LIGHT_GRAY);
		extractInsLbl.setBounds(56, 194, 411, 16);
		panelInitialize.add(extractInsLbl);
		extractInsBtn.setToolTipText("Process and extract the SAM file");
		extractInsBtn.setEnabled(false);
		extractInsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.this.extractInsLbl.setForeground(Color.BLACK);
				MainFrame.this.extractInsBtn.setEnabled(false);
				MainFrame.this.extractInsBtn.setText("Wait");

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
					loadingLbl.setBounds(22, 214, 30, 16);
					loadingLbl.setVisible(true);
					samFilePathTxt.setText(newPath);
					SortInsertions run = new SortInsertions();
					Thread runThread = new Thread(run);
					runThread.start();
				}else{
					loadingLbl.setBounds(22, 194, 30, 16);
					loadingLbl.setVisible(true);
					ExtractInsertions run = new ExtractInsertions();
					Thread runThread = new Thread(run);
					runThread.start();
				}
			}
		});

		extractInsBtn.setBounds(610, 152, 99, 25);
		panelInitialize.add(extractInsBtn);

		sortInsLbl.setForeground(Color.LIGHT_GRAY);
		sortInsLbl.setBounds(56, 213, 411, 16);
		panelInitialize.add(sortInsLbl);

		countUniLbl.setForeground(Color.LIGHT_GRAY);
		countUniLbl.setBounds(56, 232, 411, 16);
		panelInitialize.add(countUniLbl);

		sortUniLbl.setForeground(Color.LIGHT_GRAY);
		sortUniLbl.setBounds(56, 251, 411, 16);
		panelInitialize.add(sortUniLbl);
		cancelLibSaveBtn.setToolTipText("Clear everything and start over");
		cancelLibSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				clearTheForm();

			}
		});

		cancelLibSaveBtn.setBounds(587, 261, 122, 25);
		panelInitialize.add(cancelLibSaveBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(2, 297, 717, 2);
		panelInitialize.add(separator_2);

		JLabel lblRemoveExistingLibraries = new JLabel("Edit existing libraries:");
		lblRemoveExistingLibraries.setBounds(12, 312, 313, 16);
		panelInitialize.add(lblRemoveExistingLibraries);
		libraryComboBox.setToolTipText("Select a library to edit");

		libraryComboBox.setBounds(14, 341, 479, 22);
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
		removeLibBtn.setBounds(612, 340, 97, 25);
		panelInitialize.add(removeLibBtn);
		renameLibBtn.setToolTipText("Rename the selected library");

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
		renameLibBtn.setBounds(503, 340, 97, 25);
		panelInitialize.add(renameLibBtn);

		loadingLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));
		loadingLbl.setBounds(16, 194, 30, 16);
		panelInitialize.add(loadingLbl);

		doneLbl1.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl1.setBounds(24, 194, 23, 14);
		panelInitialize.add(doneLbl1);

		doneLbl2.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl2.setBounds(23, 214, 23, 14);
		panelInitialize.add(doneLbl2);

		doneLbl3.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl3.setBounds(23, 233, 23, 14);
		panelInitialize.add(doneLbl3);

		doneLbl4.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/done.gif")));
		doneLbl4.setBounds(23, 252, 23, 14);
		panelInitialize.add(doneLbl4);
		
		JLabel lblNewLabel_2 = new JLabel("You can add and manage transposon insertion mutant libraries here. You need the .sam file from the Barrows-Wheeler aligner.");
		lblNewLabel_2.setBounds(10, 11, 697, 14);
		panelInitialize.add(lblNewLabel_2);
		
		JLabel lblbwaHttpbiobwasourceforgenet = new JLabel("(bwa; http://bio-bwa.sourceforge.net/) to add a library to your project. If the Barrows-Wheeler Aligner is installed on this computer you may");
		lblbwaHttpbiobwasourceforgenet.setBounds(10, 36, 697, 14);
		panelInitialize.add(lblbwaHttpbiobwasourceforgenet);
		
		JLabel lblBeAbleTo = new JLabel(" be able to run it from this application. To add new library, provide the name for the library, navigate to the .sam file using the 'Browse' button,");
		lblBeAbleTo.setBounds(8, 61, 699, 14);
		panelInitialize.add(lblBeAbleTo);
		
		JLabel lblAndThenClick = new JLabel(" and then click 'Extract'.");
		lblAndThenClick.setBounds(10, 86, 697, 14);
		panelInitialize.add(lblAndThenClick);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(2, 111, 717, 2);
		panelInitialize.add(separator_9);
		
		JLabel lblChooseALibrary = new JLabel("Choose a library to plot:");
		lblChooseALibrary.setBounds(14, 433, 286, 14);
		panelInitialize.add(lblChooseALibrary);
		plotLibraryCombo.setToolTipText("Select a libray to plot");
		
		plotLibraryCombo.setBounds(14, 458, 400, 20);
		panelInitialize.add(plotLibraryCombo);
		plotBtn.setToolTipText("Plot the selected library");
		
		plotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotDataMethod();
			}
		});
		plotBtn.setBounds(622, 457, 62, 23);
		panelInitialize.add(plotBtn);
		
		winLenTxt = new JTextField();
		winLenTxt.setToolTipText("Enter the window length");
		winLenTxt.setText("1000");
		winLenTxt.setColumns(10);
		winLenTxt.setBounds(424, 458, 89, 20);
		panelInitialize.add(winLenTxt);
		
		winStepTxt = new JTextField();
		winStepTxt.setToolTipText("Enter window step size");
		winStepTxt.setText("100");
		winStepTxt.setColumns(10);
		winStepTxt.setBounds(523, 458, 89, 20);
		panelInitialize.add(winStepTxt);
		
		JLabel label_4 = new JLabel("Window Len:");
		label_4.setBounds(410, 443, 89, 14);
		panelInitialize.add(label_4);
		
		JLabel label_5 = new JLabel("Step:");
		label_5.setBounds(509, 443, 89, 14);
		panelInitialize.add(label_5);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setBounds(2, 395, 717, 2);
		panelInitialize.add(separator_10);
		
		JLabel lblPlotTheDistribution = new JLabel("Plot the distribution of the number of unique insertions per window for a selected library.");
		lblPlotTheDistribution.setBounds(12, 408, 695, 14);
		panelInitialize.add(lblPlotTheDistribution);
		
		plotWaitLbl.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/load.gif")));
		plotWaitLbl.setBounds(328, 517, 99, 14);
		panelInitialize.add(plotWaitLbl);
		
		JLabel label_3 = new JLabel("(?)");
		label_3.setToolTipText("Click me!");
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, 
						"This is the same plot as shown in Figure 1 of Sarmiento et al., PNAS 110:4726–4731, 2013. A strong peak at zero separated from a wider peak for larger\n"
						+ "numbers of insertions indicates that the window size is suitable for detecting essential genes in the given library. Larger window sizes provide better\n"
						+ "distinction between essential and non-essential genes but they can miss essential genes shorter than the window length." );
			}
		});
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_3.setBounds(689, 461, 30, 14);
		panelInitialize.add(label_3);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Manage Data Tables", null, panel, null);
		panel.setLayout(null);

		JLabel lblAddNewData = new JLabel("Add new data table to your project:");
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
		addNewDataTableBtn.setBounds(316, 52, 113, 23);
		panel.add(addNewDataTableBtn);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(0, 86, 717, 8);
		panel.add(separator_5);

		JLabel lblOldLibraries = new JLabel("Manage existing tables:");
		lblOldLibraries.setBounds(10, 105, 311, 16);
		panel.add(lblOldLibraries);
		dataTableCombo.setToolTipText("Just select the data table you want to edit");

		dataTableCombo.setBounds(10, 132, 338, 20);
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

		dataTableRenameBtn.setBounds(519, 131, 89, 23);
		panel.add(dataTableRenameBtn);
		dataTableRemoveBtn.setToolTipText("Remove the selected data table from the project");
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

		dataTableRemoveBtn.setBounds(618, 131, 89, 23);
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

		openAsSpreadsheetBtn.setBounds(362, 131, 147, 23);
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

				File xlsFile = new File(projectInfo.getPath() + dataTableCombo.getSelectedItem() + ".xls");
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
		replaceXlsBtn.setBounds(362, 165, 147, 23);
		panel.add(replaceXlsBtn);
		tableCancelChangeBtn.setEnabled(false);

		tableCancelChangeBtn.setBounds(519, 165, 89, 23);
		panel.add(tableCancelChangeBtn);
		
		JLabel lblNewLabel_3 = new JLabel("Create and manage spreadsheets with gene information.");
		lblNewLabel_3.setBounds(10, 11, 300, 14);
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
		label_6.setBounds(318, 11, 30, 14);
		panel.add(label_6);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(0, 39, 717, 8);
		panel.add(separator_6);
		addNewIndicesBtn.setToolTipText("You can add extra data to your data tables in this subsection");
		
		addNewIndicesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AddMoreIndices addFrame = new AddMoreIndices((String) dataTableCombo.getSelectedItem(), projectInfo, MainFrame.this);
				addFrame.setVisible(true);
				
				
				
			}
		});
		addNewIndicesBtn.setBounds(362, 199, 290, 23);
		panel.add(addNewIndicesBtn);
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
				samtoolsInstallBtn.setEnabled(false);

				alreadyInstalledRadio.setSelected(true);
			}
		});

		alreadyInstalledRadio.setSelected(true);
		alreadyInstalledRadio.setBounds(10, 85, 191, 23);
		panel_3.add(alreadyInstalledRadio);
		useScriptsRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bwaInstallBtn.setEnabled(true);
				samtoolsInstallBtn.setEnabled(true);
			}
		});

		useScriptsRadio.setBounds(10, 111, 351, 23);
		panel_3.add(useScriptsRadio);
		samtoolsInstallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL programSource = MainFrame.class.getResource("/resources/samtools-0.1.19.tar.bz2");
				File programDest = new File("samtools-0.1.19.tar.bz2");

				URL shellSource = MainFrame.class.getResource("/resources/install-samstools.sh");
				File shellDest = new File("shell.sh");

				try {
					FileUtils.copyURLToFile(programSource, programDest);
					FileUtils.copyURLToFile(shellSource, shellDest);

					String cmd[] = {"gnome-terminal", "-x", "bash", "-c", "echo 'Please Enter Your Root Password';"
							+ "su -m root -c 'sh shell.sh';"
							+ "echo;"
							+ "echo;"
							+ "echo 'Press Any Key To Continue...';"
							+ "read"};

					Path currentRelativePath = Paths.get("");
					String location = currentRelativePath.toAbsolutePath()
							.toString();
					File dir = new File(location);
					Process child = Runtime.getRuntime().exec(cmd, null, dir);
					child.waitFor();
				} catch (IOException | InterruptedException e1) {
					logger.error(e1.getMessage());
					return;
				}finally{
					shellDest.delete();
					programDest.delete();
				}
			}
		});

		samtoolsInstallBtn.setBounds(503, 111, 138, 23);
		panel_3.add(samtoolsInstallBtn);
		bwaInstallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL programSource = MainFrame.class.getResource("/resources/bwa-0.7.5a.tar.bz2");
				File programDest = new File("bwa-0.7.5a.tar.bz2");

				URL shellSource = MainFrame.class.getResource("/resources/install-bwa.sh");
				File shellDest = new File("shell.sh");

				try {
					FileUtils.copyURLToFile(programSource, programDest);
					FileUtils.copyURLToFile(shellSource, shellDest);
					String cmd[] = {"gnome-terminal", "-x", "bash", "-c", 
							"echo 'Please Enter Your Root Password';"
									+ "su -m root -c 'sh shell.sh';"
									+ "echo;"
									+ "echo;"
									+ "echo 'Press Any Key To Continue...';"
									+ "read"};

					Path currentRelativePath = Paths.get("");
					String location = currentRelativePath.toAbsolutePath()
							.toString();
					File dir = new File(location);
					Process child = Runtime.getRuntime().exec(cmd, null, dir);
					child.waitFor();
				} catch (IOException | InterruptedException e1) {
					logger.error(e1.getMessage());
					return;
				}finally{
					shellDest.delete();
					programDest.delete();
				}
			}
		});

		bwaInstallBtn.setBounds(369, 111, 124, 23);
		panel_3.add(bwaInstallBtn);

		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(0, 160, 717, 5);
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
	
	private void plotDataMethod(){
		if(winLenTxt.getText() == null || winLenTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window length", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		if(winStepTxt.getText() == null || winStepTxt.getText().compareTo("") == 0){
			JOptionPane.showMessageDialog(MainFrame.this, "Please provide the window step length", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		final int len = Integer.parseInt(winLenTxt.getText());
		final int step = Integer.parseInt(winStepTxt.getText());
		final String title = String.format("Library Name: %s | Window Length: %d | Window Steps: %d", (String) plotLibraryCombo.getSelectedItem(),
				len, step);
		
		plotWaitLbl.setVisible(true);
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {					
					ChartPanel panel = new ChartPanel(PlotData.plotData((String) plotLibraryCombo.getSelectedItem(), len, step, title, projectInfo));
					
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

		String warningMsg = String.format("Please after editing the table save it as tab delimited file and "
				+ "replace the %s.xls file\nin the project path. After saving the file you MUST press the \"Replace\" button to\n"
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

			JOptionPane.showMessageDialog(null, "This tab is only available when you are using Linux!!!");

		}else{
			bwaInstallBtn.setEnabled(false);
			samtoolsInstallBtn.setEnabled(false);

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
			addNewIndicesBtn.setEnabled(true);

			dataTableCombo.removeAllItems();
			for (File t : tableFiles){
				dataTableCombo.addItem(t.getName().substring(0, t.getName().length() - 6));
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

		initiatePlotLibraryComboBox();
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

			setTitle("Essential Genes Finder - " + projectInfo.getName());

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
		loadingLbl.setVisible(false);
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
				loadingLbl.setBounds(22, 214, 30, 16);

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
									+ "- Some of the input files are not formatted properly. Verify that all input files, including the\n"
									+ "DNA sequence used for BWA, have the correct format.", "ERROR", JOptionPane.ERROR_MESSAGE);
							
							clearTheForm();
							return;
						}
					}
					
					return;
				}
				
				MainFrame.this.sortInsLbl.setForeground(Color.black);
				MainFrame.this.countUniLbl.setForeground(Color.BLUE);

				doneLbl2.setVisible(true);
				loadingLbl.setBounds(22, 233, 30, 16);

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
				loadingLbl.setBounds(22, 252, 30, 16);

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
