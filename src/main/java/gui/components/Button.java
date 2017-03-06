package gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Button extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3937648340267231037L;
	
	public Button(String caption, final Color backgroundColour, final Color highlightColour) {
		setBackground(backgroundColour);
		setBorder(new LineBorder(Color.WHITE, 2));
		setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2-43, 136, 86, 38);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(backgroundColour);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(highlightColour);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
	
			}
		});
		JLabel lblOk = new JLabel(caption);
		lblOk.setBounds(0, 0, getWidth(), getHeight());
		lblOk.setForeground(Color.WHITE);
		lblOk.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblOk.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblOk);	
	}
}
