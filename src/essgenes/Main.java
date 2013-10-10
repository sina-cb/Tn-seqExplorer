/**
 * 
 */
package essgenes;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import GUI.WorkspaceChooser;


/**
 * @author Sina Solaimanpour
 *
 */
public class Main {

	public static void main(String[] args) {

		try {
			
			String OSName = System.getProperty("os.name");

			if(OSName.contains("Windows") || OSName.contains("windows")){
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}else{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
		} 
		catch (UnsupportedLookAndFeelException e) {
			
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}


		WorkspaceChooser ws = new WorkspaceChooser();
		ws.setVisible(true);

	}

}
