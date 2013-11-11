package GUI;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class AboutFrame extends JFrame {

	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public AboutFrame() {
		setTitle("About");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTnseqExplorer = new JLabel("");
		lblTnseqExplorer.setIcon(new ImageIcon(AboutFrame.class.getResource("/resources/info.png")));
		lblTnseqExplorer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTnseqExplorer.setBounds(0, 22, 424, 250);
		contentPane.add(lblTnseqExplorer);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
