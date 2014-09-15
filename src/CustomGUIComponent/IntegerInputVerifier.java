package CustomGUIComponent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IntegerInputVerifier extends InputVerifier{

	JLabel errorLabel = null;
	Component actionComponent = null;
	
	public IntegerInputVerifier(JLabel errorMsgLbl, Component actionComponent) {
		errorLabel = errorMsgLbl;
		this.actionComponent = actionComponent;
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
			if (actionComponent != null){
				actionComponent.setEnabled(true);
			}
		}else{
			textField.setBackground(new Color(255, 177, 194)/*RED*/);
			errorLabel.setVisible(true);
			if (actionComponent != null){
				actionComponent.setEnabled(false);
			}
		}
			
		return true;
	}

}
