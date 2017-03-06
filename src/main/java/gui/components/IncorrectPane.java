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
 * Panel to be displayed for an incorrect answer.
 * 
 * @author Robertio
 *
 */
public class IncorrectPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5019026026331265496L;
	
	public IncorrectPane(final AcceptEvent event) {
		setBackground(Colours.DARK_RED);
		setBounds(0, Toolkit.getDefaultToolkit().getScreenSize().height/2-200, Toolkit.getDefaultToolkit().getScreenSize().width, 200);
		setLayout(null);
		
		JLabel lblIncorrect = new JLabel("Incorrect. Please try again");
		lblIncorrect.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2 - 184, 60, 369, 41);
		lblIncorrect.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblIncorrect.setHorizontalAlignment(SwingConstants.CENTER);
		lblIncorrect.setForeground(Color.WHITE);
		lblIncorrect.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		add(lblIncorrect);
		
		Button okButton = new Button("OK", Colours.DARK_RED, Colours.LIGHT_RED);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				event.accept();
			}
		});
		add(okButton);
	}

}
