package CustomGUIComponent;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IntegerInputVerifier extends InputVerifier{

	JLabel errorLabel = null;

	public IntegerInputVerifier(JLabel errorMsgLbl) {
		errorLabel = errorMsgLbl;
	}

	@Override
	public boolean verify(JComponent input) {
		JTextField textField = (JTextField) input;
		boolean isInteger = true;

		try{
			int a = Integer.parseInt(textField.getText());
			if (a < 0){
				isInteger = false;
			}
		}catch(NumberFormatException e){
			isInteger = false;
		}

		if (isInteger){
			textField.setBackground(new Color(228, 252, 230)/*GREEN*/);
			errorLabel.setVisible(false);
		}else{
			textField.setBackground(new Color(255, 177, 194)/*RED*/);
			errorLabel.setVisible(true);
		}

		return true;
	}

}
