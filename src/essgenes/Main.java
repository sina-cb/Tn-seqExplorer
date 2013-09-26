/**
 * 
 */
package essgenes;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import GUI.WorkspaceChooser;


/**
 * @author Sina Solaimanpour
 *
 */
public class Main {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
