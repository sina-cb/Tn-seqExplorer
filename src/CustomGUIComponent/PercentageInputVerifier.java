package CustomGUIComponent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PercentageInputVerifier extends InputVerifier{

	JLabel errorLabel = null;
	Component actionComponent = null;
	
	public PercentageInputVerifier(JLabel errorMsgLbl, Component actionComponent) {
		errorLabel = errorMsgLbl;
		this.actionComponent = actionComponent;
	}

	@Override
	public boolean verify(JComponent input) {
	
		JTextField textField = (JTextField) input;
		boolean valid = true;
		
		String text = textField.getText();
		text = text.replace("%", "");
		
		try{
			Double.parseDouble(text);
		}catch(NumberFormatException e){
			valid = false;
		}
		
		if (valid){
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
