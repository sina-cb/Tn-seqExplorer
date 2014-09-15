package CompareUtilities;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CompareMain {

	public static final boolean DEBUG = true;
	
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

		Compare compareFrame = new Compare();
		compareFrame.setVisible(true);

	}

}
