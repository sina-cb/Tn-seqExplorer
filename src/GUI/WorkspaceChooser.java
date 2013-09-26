package GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import essgenes.Messages;

@SuppressWarnings("serial")
public class WorkspaceChooser extends JFrame {

	private Logger logger = Logger.getLogger(WorkspaceChooser.class.getName());
	private ArrayList<String> projectPaths = new ArrayList<>();
	private ArrayList<String> projectNames = new ArrayList<>();
	private JComboBox<String> recentProjects = new JComboBox<String>();

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public WorkspaceChooser() {
		setTitle("Create/Open Your Project");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 549, 225);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		recentProjects.setBounds(12, 101, 299, 22);
		contentPane.add(recentProjects);

		trimRecentProjects();

		getRecentProjects();
		for (String temp : projectNames){
			recentProjects.addItem(temp);
		}

		JLabel lblChooseOrCreate = new JLabel("Select your project:");
		lblChooseOrCreate.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblChooseOrCreate.setBounds(12, 13, 455, 28);
		contentPane.add(lblChooseOrCreate);

		JLabel lblYouCanHave = new JLabel("You can have multiple projects in this application.");
		lblYouCanHave.setBounds(12, 54, 507, 16);
		contentPane.add(lblYouCanHave);

		JButton browseBtn = new JButton("Browse");
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(WorkspaceChooser.this);

				if (result == JFileChooser.APPROVE_OPTION){
					File dir = fileChooser.getSelectedFile();
					File[] matchingFiles = dir.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return (name.endsWith(".pro") || name.endsWith(".Pro")); 
						}
					});
					if (matchingFiles.length < 1){
						JOptionPane.showMessageDialog(WorkspaceChooser.this, "There is no project found in this directory. "
								+ "You can try to create one!");
						return;
					}

					String projectName = "";
					String projectPath = "";
					BufferedReader br = null;
					try{
						br = new BufferedReader(new FileReader(matchingFiles[0]));
						String line = br.readLine();
						projectName = line.substring(Messages.projectName.length());

						line = br.readLine();
						projectPath = line.substring(Messages.projectPath.length());
					}catch(IOException e){
						logger.error(e.getMessage());
						return;
					}finally{
						try {
							br.close();
						} catch (IOException e) {
							logger.error(e.getMessage());
						}
					}

					WorkspaceChooser.this.recentProjects.addItem(projectName);
					WorkspaceChooser.this.recentProjects.setSelectedIndex(WorkspaceChooser.this.recentProjects.getItemCount() - 1);

					addToRecentProjects(projectName, projectPath);

					projectNames.add(projectName);
					projectPaths.add(projectPath);

				}
			}
		});
		browseBtn.setBounds(323, 100, 97, 25);
		contentPane.add(browseBtn);

		JButton newBtn = new JButton("New");
		newBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path currentRelativePath = Paths.get("");
				String location = currentRelativePath.toAbsolutePath().toString();
				JFileChooser fileChooser = new JFileChooser(location);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(WorkspaceChooser.this);

				if (result == JFileChooser.APPROVE_OPTION){
					File dir = fileChooser.getSelectedFile();
					File[] matchedFiles = dir.listFiles(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String name) {
							return (name.endsWith(".pro") || name.endsWith(".Pro"));
						}
					});

					if (matchedFiles.length != 0){
						JOptionPane.showMessageDialog(WorkspaceChooser.this, "There is already another project existing "
								+ "in this directory, choose another one please.");
						return;
					}

					String projectPath = dir.getAbsolutePath();
					String seperator = "\\";
					if(projectPath.contains("/")){
						seperator = "/";
					}
					projectPath = projectPath + seperator;
					
					String projectName = JOptionPane.showInputDialog(WorkspaceChooser.this, "Enter Project Name:");
					while(projectName == null || projectName.compareTo("") == 0){
						projectName = JOptionPane.showInputDialog("Project Name:");
					}

					BufferedWriter bw = null;
					try{
						File projectFile = new File(projectPath + "project.pro");
						boolean ifCreated = projectFile.createNewFile();

						if(!ifCreated){
							logger.error("We could not create the project file!!!");
							return;
						}else{
							bw = new BufferedWriter(new FileWriter(projectFile));

							bw.write(Messages.projectName + projectName + "\n");
							bw.write(Messages.projectPath + projectPath + "\n");
						}

						bw.close();

						WorkspaceChooser.this.recentProjects.addItem(projectName);
						WorkspaceChooser.this.recentProjects.setSelectedIndex(WorkspaceChooser.this.recentProjects.getItemCount() - 1);

						projectPaths.add(projectPath);
						projectNames.add(projectName);

						addToRecentProjects(projectName, projectPath);

					}catch(IOException e){
						logger.error(e.getMessage());
						return;
					}finally{
						try {
							bw.close();
						} catch (IOException e) {
							logger.error(e.getMessage());
							return;
						}
					}

				}
			}
		});
		newBtn.setBounds(432, 100, 97, 25);
		contentPane.add(newBtn);

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		cancelBtn.setBounds(323, 153, 97, 25);
		contentPane.add(cancelBtn);

		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String selected =  (String) WorkspaceChooser.this.recentProjects.getSelectedItem();
				for(int i = 0; i < projectNames.size(); i++){
					if (selected.compareTo(projectNames.get(i)) == 0){
						selected = projectPaths.get(i);
					}
				}

				MainFrame mainFrame = new MainFrame(selected);
				mainFrame.setVisible(true);

				WorkspaceChooser.this.setVisible(false);
				WorkspaceChooser.this.dispose();
			}
		});
		okBtn.setBounds(432, 153, 97, 25);
		contentPane.add(okBtn);
		
		JLabel lblNowYouCan = new JLabel("Now you can choose an existing project or create a new one.");
		lblNowYouCan.setBounds(12, 72, 497, 16);
		contentPane.add(lblNowYouCan);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 138, 543, 2);
		contentPane.add(separator);
	}

	private void trimRecentProjects(){



	}

	private void addToRecentProjects(String projectName, String projectPath){
		File projects = new File("projects.dat");
		BufferedReader br = null;
		BufferedWriter bw = null;
		boolean exists = false;

		try {
			br = new BufferedReader(new FileReader(projects));
			String line = br.readLine();

			while(line != null){
				int tempIndex = line.indexOf(Messages.separatorString);
				//String tempName = line.substring(0, tempIndex);
				String tempPath = line.substring(tempIndex + Messages.separatorString.length());

				if (projectPath.compareTo(tempPath) == 0){
					exists = true;
					break;
				}

				line = br.readLine();
			}

			br.close();

			bw = new BufferedWriter(new FileWriter(projects, true));
			if (!exists){
				bw.append(projectName + Messages.separatorString + projectPath + "\n");
			}

			bw.close();

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private void getRecentProjects(){

		File projects = new File("projects.dat");
		BufferedReader br = null;

		try {
			if(!projects.exists()){
				projects.createNewFile();
			}
			
			br = new BufferedReader(new FileReader(projects));
			String line = br.readLine();

			while(line != null){
				int tempIndex = line.indexOf(Messages.separatorString);
				String tempName = line.substring(0, tempIndex);
				String tempPath = line.substring(tempIndex + Messages.separatorString.length());

				projectPaths.add(tempPath);
				projectNames.add(tempName);

				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
