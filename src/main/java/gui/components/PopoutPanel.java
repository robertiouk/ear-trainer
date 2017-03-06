package gui.components;

import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PopoutPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9059410418178977540L;
	private static int TARGET_WIDTH = 300;
	private static int INCREMENT = 10;
	private AtomicBoolean resizing = new AtomicBoolean(false);
	private AtomicBoolean expanding = new AtomicBoolean(false);
	private Timer timer;

	public PopoutPanel(String caption) {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
		});
		setBackground(Colours.LIGHT_TURQUOISE);
		setLayout(null);
		
		JLabel lblCaption = new JLabel(caption);
		lblCaption.setForeground(Color.WHITE);
		lblCaption.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		lblCaption.setBounds(10, 11, 418, 32);
		add(lblCaption);
		
		timer = new Timer(20, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean condition;
				int newWidth;
				if (expanding.get()) {
					condition = getWidth() < TARGET_WIDTH;
					newWidth = getWidth() + INCREMENT;
				} else {
					condition = getWidth() > 0;
					newWidth = getWidth() - INCREMENT;
				}
				if (condition) {
					int width = getWidth();
					setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - width + INCREMENT, 0, newWidth, Toolkit.getDefaultToolkit().getScreenSize().height);
					if (expanding.get()) {
						condition = getWidth() < TARGET_WIDTH;
						newWidth = getWidth() + INCREMENT;
					} else {
						condition = getWidth() > 0;
						newWidth = getWidth() - INCREMENT;
					}
				} else {
					resizing.set(false);
					timer.stop();
				}
			}			
		});
	}

	public void show() {
		if (!resizing.get()) {
			resize(true);
		}
	}
	
	public void hide() {
		if (!resizing.get()) {
			resize(false);
		}	
	}
	
	private void resize(final boolean expanding) {
		resizing.set(true);
		this.expanding.set(expanding);
		timer.start();
	}
}
