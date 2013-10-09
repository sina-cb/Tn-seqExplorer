package CustomGUIComponent;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import sun.swing.FilePane;

public class FolderChooser extends JFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public FolderChooser(){
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		remove(getComponent(0));
		JPanel folderView = (JPanel)getComponent(1);
		JToolBar toolbar = (JToolBar)getComponent(0);
		
		toolbar.remove(toolbar.getComponent(0));
		toolbar.remove(toolbar.getComponent(0));
		Component tree = toolbar.getComponent(0);
		toolbar.remove(toolbar.getComponent(1));
		toolbar.remove(toolbar.getComponent(2));
		toolbar.remove(toolbar.getComponent(2));
		toolbar.remove(toolbar.getComponent(2));
		
		folderView.remove(folderView.getComponent(0));
		
		FilePane folders = (FilePane)folderView.getComponent(0);
		folders.setViewType(FilePane.VIEWTYPE_DETAILS);
		
		JPanel buttonsPanel = (JPanel)folderView.getComponent(1);
		buttonsPanel.remove(buttonsPanel.getComponent(0));
		buttonsPanel.remove(buttonsPanel.getComponent(0));
		buttonsPanel.remove(buttonsPanel.getComponent(0));
		buttonsPanel.remove(buttonsPanel.getComponent(0));
		
		JPanel buttons = (JPanel)buttonsPanel.getComponent(0);
		buttons.setLayout(new FlowLayout());
		buttons.revalidate();
		
		JButton saveBtn = (JButton)buttons.getComponent(1);
		JButton cancelBtn = (JButton)buttons.getComponent(3);
		
		revalidate();
	}

}
