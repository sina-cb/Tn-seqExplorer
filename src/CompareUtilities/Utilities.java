package CompareUtilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import CustomGUIComponent.FolderChooser;

public class Utilities {

	public static String browseForFolder(JFrame parent){
		Path currentRelativePath = Paths.get("");
		String location = currentRelativePath.toAbsolutePath().toString();

		String OSName = System.getProperty("os.name");

		JFileChooser fileChooser = null;
		int result = -1;
		if(OSName.contains("Windows") || OSName.contains("windows")){
			fileChooser = new FolderChooser();
			fileChooser.setCurrentDirectory(new File(location));
			fileChooser.setDialogTitle("Select a folder to store the new project in it");
			result = fileChooser.showSaveDialog(parent);					
		}else{
			JOptionPane.showMessageDialog(parent, "Select a folder to store the new project in it");
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(location));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setDialogTitle("Select a folder to store the new project in it");
			result = fileChooser.showSaveDialog(parent);
		}

		if (result == JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile().getAbsolutePath() + File.separator;
		}

		return null;
	}
	
	public static void browseForInputFile(JTextField text, String ext, String extInfo, JFrame parent){

		Path currentRelativePath = Paths.get("");
		String location = currentRelativePath.toAbsolutePath()
				.toString();
		JFileChooser fileChooser = new JFileChooser(location);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setFileFilter(new FileNameExtensionFilter(extInfo, ext));
		int result = fileChooser.showOpenDialog(parent);

		if(result == JFileChooser.APPROVE_OPTION){
			text.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}else{
			return;
		}

	}
	
}
