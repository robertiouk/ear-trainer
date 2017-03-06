package gui;

import gui.appearance.Colours;
import gui.components.Button;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;
import client.command.ClientType;
import client.menu.MenuClient;

public class MainMenu extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3242775953145133019L;
	private static final int MENU_WIDTH = 292;
	private JPanel slidePanel;
	private JLabel titleLabel;
	private JTextPane textPane;
	private ClientType selectedType;
	private MenuClient menu;
	private Map<ClientType, JPanel> selectionLookup;
	
	public MainMenu(final MenuClient menu) {
		this.menu = menu;
		selectionLookup = new HashMap<ClientType, JPanel>();
		getContentPane().setBackground(Colours.DARK_TURQUOISE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setLayout(new MigLayout("", "[grow]", "[grow][grow][grow][grow]"));
		panel.setBackground(Colours.LIGHT_TURQUOISE);
		panel.setBounds(0, 0, MENU_WIDTH, 371);
		panel.setSize(300, Toolkit.getDefaultToolkit().getScreenSize().height-200);
		getContentPane().add(panel);
		
		final JPanel intervalSelect = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		intervalSelect.setBackground(Colours.TURQUOISE_SELECT);
		intervalSelect.setOpaque(false);
		panel.add(intervalSelect, "cell 0 0,aligny center,growx,height 100:150:");
		JLabel interval = new JLabel("");
		interval.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				makeSelection(ClientType.Interval);
				slideIn("Interval", "Identify the difference between two pitches.\n\n" +
						"Intervals may occur from Minor 2nd to Major 13th depending on the setting and in any key.");
			}
		});		
		interval.setBounds(61, 56, 150, 150);
		interval.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Interval.png")));
		interval.setCursor(new Cursor(Cursor.HAND_CURSOR));
		intervalSelect.add(interval, "cell 0 0,alignx center,aligny center");
		
		final JPanel chordSelect = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		chordSelect.setBackground(Colours.TURQUOISE_SELECT);
		chordSelect.setOpaque(false);
		panel.add(chordSelect, "cell 0 1, aligny center, growx, height 100:150:");
		JLabel chord = new JLabel("");
		chord.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				makeSelection(ClientType.Chord);
				slideIn("Chord", "Identify the different types of chord.\n\n" +
						"Chords can contain extended variations including 6, 7 & 9 chords. Chords may also contain combinations of b/# 5/9 extensions in more advanced settings.");
			}
		});
		chord.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Chord.png")));
		chord.setBounds(61, 279, 150, 150);
		chord.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chordSelect.add(chord, "cell 0 0, alignx center, aligny center");
		
		final JPanel cadenceSelect = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		cadenceSelect.setBackground(Colours.TURQUOISE_SELECT);
		cadenceSelect.setOpaque(false);
		panel.add(cadenceSelect, "cell 0 2, aligny center, growx, height 100:150:");
		JLabel cadence = new JLabel("");
		cadence.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {				
				makeSelection(ClientType.Cadence);
				slideIn("Cadence", "Identify the harmony of the final two chords of a four chord progression.\n\n" +
						"There are four main types of cadence.\n" +
						"- Perfect (V-I): V (dominant) chord to the I (key) chord.\n" +
						"- Plagal (IV-I): IV (sub dominant) chord to the I (key) chord.\n" +
						"- Imperfect (I-V): I (key) chord to the V (dominant) chord.\n" +
						"- Interrupted (I-VI): V (dominant) chord to a chord other than the I (tonic) chord. Normally the VIm chord.");
			}
		});
		cadence.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Cadence.png")));
		cadence.setBounds(61, 495, 150, 150);
		cadence.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cadenceSelect.add(cadence, "cell 0 0, alignx center, aligny center");
		
		final JPanel settingsSelect = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		settingsSelect.setBackground(Colours.TURQUOISE_SELECT);
		settingsSelect.setOpaque(false);
		panel.add(settingsSelect, "cell 0 3, aligny center, growx, height 100:150:");
		JLabel settings = new JLabel("");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				makeSelection(ClientType.Settings);
				slideIn("Settings", "Configure the settings for each of the ear training forms.");
			}
		});
		settings.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/SettingsCog.png")));
		settings.setBounds(61, 719, 150, 150);
		settings.setCursor(new Cursor(Cursor.HAND_CURSOR));
		settingsSelect.add(settings, "cell 0 0, alignx center, aligny center");
		
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new MigLayout("", "[][grow]30", "[grow]"));
		actionPanel.setBackground(Colours.DARK_GREY);
		actionPanel.setBounds(0, Toolkit.getDefaultToolkit().getScreenSize().height-200, Toolkit.getDefaultToolkit().getScreenSize().width, 100);
		getContentPane().add(actionPanel);
		
		JLabel back = new JLabel("");
		back.setCursor(new Cursor(Cursor.HAND_CURSOR));
		back.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/exit.png")));
		back.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width-100, 21, 56, 56);
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				menu.exit();
			}
		});
		actionPanel.add(back, "cell 1 0, alignx right, aligny center");
		
		slidePanel = new JPanel();
		slidePanel.setBounds(panel.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().width-panel.getWidth(), panel.getHeight());
		slidePanel.setBackground(Colours.DARK_TURQUOISE);
		getContentPane().add(slidePanel);
		slidePanel.setLayout(new MigLayout("", "25:50[grow][grow]", "[]0:50:[]0:50:[grow]"));
		
		titleLabel = new JLabel("<Title>");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
		slidePanel.add(titleLabel, "cell 0 0");
		
		textPane = new JTextPane();
		textPane.setForeground(Color.WHITE);
		textPane.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		textPane.setBackground(Colours.DARK_TURQUOISE);
		textPane.setEditable(false);
		slidePanel.add(textPane, "cell 0 1");
		
		Button button = new Button("Go", Colours.DARK_BLUE, Colours.LIGHT_BLUE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				openSelected();
			}
		});
		slidePanel.add(button, "cell 1 2,wmin 100,alignx center");
		
		selectionLookup.put(ClientType.Interval, intervalSelect);
		selectionLookup.put(ClientType.Chord, chordSelect);
		selectionLookup.put(ClientType.Cadence, cadenceSelect);
		selectionLookup.put(ClientType.Settings, settingsSelect);
	}
	
	private void makeSelection(final ClientType selected) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Unselect current
				if (selectedType != null) {
					JPanel current = selectionLookup.get(selectedType);
					current.setOpaque(false);
					current.repaint();
				}
				
				// Select current
				JPanel current = selectionLookup.get(selected);
				current.setOpaque(true);
				selectedType = selected;
				current.repaint();
			}		
		});
	}
	
	/**
	 * Slide a panel in from the right.
	 * 
	 * @param title String title for the panel.
	 * @param description String description for the panel.
	 */
	private void slideIn(final String title, final String description) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				slidePanel.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width,  0);
				titleLabel.setText(title);
				textPane.setText(description);
		
				final Timer timer = new Timer(5, null);
				ActionListener action = new ActionListener() {
					int xPos = slidePanel.getLocation().x;
					final int width = xPos;
					int i = 0;
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						double val = Math.sin(Math.toRadians(i));
						xPos = (int) (width * val);
						slidePanel.setLocation(MENU_WIDTH + width - xPos, 0);
						++i;
						
						if (i == 90) {
							timer.stop();
						}
					}
				};
				timer.addActionListener(action);
				timer.start();
			}
			
		});
	}
	
	/**
	 * Open the selected client.
	 */
	private void openSelected() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				switch (selectedType) {
				case Cadence:
					menu.openCadence();
					break;
				case Chord:
					menu.openChord();
					break;
				case Interval:
					menu.openInterval();
					break;
				case MainMenu:
					// Do nothing here
					break;
				case Settings:
					menu.openSettings();
					break;
				default:
					break;		
				}
			}
		});
	}
}
