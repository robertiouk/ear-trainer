package gui.components;


import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel to be displayed for a correct answer.
 * 
 * @author Robertio
 *
 */
public class CorrectPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3657242426673203242L;
	
	public CorrectPane(final AcceptEvent event) {
		setBackground(Colours.DARK_GREEN);
		setBounds(0, Toolkit.getDefaultToolkit().getScreenSize().height/2-200, Toolkit.getDefaultToolkit().getScreenSize().width, 200);
		setLayout(null);
		
		JLabel lblCorrect = new JLabel("Correct");
		lblCorrect.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2-53, 60, 106, 41);
		lblCorrect.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCorrect.setHorizontalAlignment(SwingConstants.CENTER);
		lblCorrect.setForeground(Color.WHITE);
		lblCorrect.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		add(lblCorrect);
		
		Button okButton = new Button("OK", Colours.DARK_GREEN, Colours.LIGHT_GREEN);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				event.accept();
			}
		});
		add(okButton);
	}
}
