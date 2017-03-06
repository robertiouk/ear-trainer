package gui.components;


import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public abstract class GameFrame extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7740401877364531174L;
	protected JPanel infoPane;
	protected boolean controlLock;
	protected JPanel correctPane;
	protected JPanel incorrectPane;
	protected PopoutPanel settingsPanel;
	protected List<JLabel> buttonList;
	protected List<JLabel> buttonLabelList;
	private JPanel actionPanel;
	protected JPanel displayPanel;
	protected JScrollPane scrollPane;

	public GameFrame(String title) {
		buttonList = new ArrayList<JLabel>();
		buttonLabelList = new ArrayList<JLabel>();
		
		setBorder(null);
		getContentPane().setLayout(null);
		
		correctPane = new CorrectPane(new AcceptEvent() {				
			@Override
			public void accept() {
				removeInfoPane(true);
			}
		});
		incorrectPane = new IncorrectPane(new AcceptEvent() {				
			@Override
			public void accept() {
				removeInfoPane(false);
			}
		});
		correctPane.setVisible(false);
		incorrectPane.setVisible(false);
		getContentPane().add(correctPane);
		getContentPane().add(incorrectPane);
				
		actionPanel = new JPanel(new MigLayout("","30[]30[grow]30[]30[]30","[grow]"));
		actionPanel.setBackground(Colours.DARK_GREY);
		actionPanel.setBounds(0, Toolkit.getDefaultToolkit().getScreenSize().height-200, Toolkit.getDefaultToolkit().getScreenSize().width, 100);
		getContentPane().add(actionPanel);
		
		settingsPanel = new PopoutPanel(title + " Settings");
		getContentPane().add(settingsPanel);
		
		displayPanel = new JPanel(new MigLayout());
		displayPanel.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		displayPanel.setBackground(Colours.DARK_TURQUOISE);
		
		scrollPane = new JScrollPane(displayPanel);		
		scrollPane.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-200);
		scrollPane.getViewport().setBackground(Colours.DARK_BLUE);
		scrollPane.setBorder(null);
		getContentPane().add(scrollPane);		
	
		JLabel lblNewLabel = new JLabel(title);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
		lblNewLabel.setBounds(24, 11, 274, 54);
		displayPanel.add(lblNewLabel, "cell 0 0, gapx 15");
				
		JLabel play = new JLabel("");
		play.setCursor(new Cursor(Cursor.HAND_CURSOR));
		play.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (!controlLock) {
					play();
				}
			}
		});
		play.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Play.png")));
		play.setBounds(41, 21, 56, 56);
		actionPanel.add(play, "cell 0 0");
		
		JLabel skip = new JLabel("");
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (!controlLock) {
					getNext();
				}
			}
		});
		skip.setCursor(new Cursor(Cursor.HAND_CURSOR));
		skip.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Skip.png")));
		skip.setBounds(139, 21, 56, 56);
		actionPanel.add(skip, "cell 1 0");
				
		JLabel back = new JLabel("");
		back.setCursor(new Cursor(Cursor.HAND_CURSOR));
		back.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Back.png")));
		back.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width-100, 21, 56, 56);
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (!controlLock) {
					exit();
				}
			}
		});
		actionPanel.add(back, "cell 3 0, grow, alignx right, aligny center");
	}
	
	/**
	 * Create a settings panel for the frame.
	 */
	protected void addSettingsPanel() {
		displayPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (!controlLock && arg0.getX() >= Toolkit.getDefaultToolkit().getScreenSize().width-10) {
					settingsPanel.show();
				} else if (!controlLock) {
					settingsPanel.hide();
				}
			}
		});
		
		JLabel settings = new JLabel("");
		settings.setCursor(new Cursor(Cursor.HAND_CURSOR));
		settings.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Settings.png")));
		settings.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width-200, 21, 56, 56);
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (!controlLock) {
					settingsPanel.show();
				}
			}
		});
		actionPanel.add(settings, "cell 2 0, grow, alignx right, aligny center");
		actionPanel.revalidate();
	}
	
	/**
	 * Remove the displayed info pane.
	 */
	protected void removeInfoPane(boolean newInterval) {
		infoPane.setVisible(false);
		
		if (newInterval) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					getNext();
				}
			});			
		}
		setControlLock(false);
	}
	
	/**
	 * Remove the current buttons on screen.
	 */
	protected void clearButtons() {
		for (JLabel button : buttonList) {
			displayPanel.remove(button);
		}
		for (JLabel label : buttonLabelList) {
			displayPanel.remove(label);
		}
	}
	
	/**
	 * Set the control lock variable.
	 * 
	 * @param lock Boolean.
	 */
	protected void setControlLock(boolean lock) {
		controlLock = lock;
	}
	
	protected abstract void play();
	protected abstract void getNext();
	protected abstract void exit();
	protected abstract void buildButtons();
}
