package gui.settings;

import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import client.menu.SettingsClient;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;

public class Settings extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4755471415460680864L;
	
	private final Color LABEL_BACK_COLOUR = new Color(0, 157, 176);
	private List<JLabel> labels;
	private JDesktopPane desktopPane;
	private JInternalFrame currentFrame;
	private AtomicBoolean moving;
	private final static String DIFFICULTY = "difficulty";
	private final static String CUSTOM = "custom";
	private final static String SOUND = "sound";
	private final static String INTERVAL = "interval";
	private final static String CHORD = "chord";
	private final static String CADENCE = "cadence";
	private final static String GENERAL = "general";
	private SettingsClient client;
	private GeneralSettings generalSettings;
	
	public Settings(final SettingsClient client) throws InstantiationException {
		labels = new ArrayList<JLabel>();
		moving = new AtomicBoolean(false);
		this.client = client;
		
		getContentPane().setBackground(Colours.DARK_BLUE);
		getContentPane().setLayout(null);
		setBorder(null);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(null);
		menuPanel.setBounds(0, 0, 318, Toolkit.getDefaultToolkit().getScreenSize().height);
		menuPanel.setBackground(Colours.LIGHT_BLUE);
		getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		Setting intervalDifficulty = client.getSetting(DIFFICULTY).findSetting(CUSTOM);
		Setting soundSettings = client.getSetting(SOUND).findSetting(INTERVAL);
		final IntervalSettings intervalSettings = new IntervalSettings((CompositeSetting)soundSettings, (CompositeSetting)intervalDifficulty, 
			new ApplySetting() {
			@Override
			public void apply(Setting setting) {
				applyIntervalSettings((CompositeSetting) setting);
			}		
		});
		
		soundSettings = client.getSetting(SOUND).findSetting(CHORD);
		Setting chordDifficulty = client.getSetting(DIFFICULTY).findSetting(CHORD).findSetting(CUSTOM);
		final ChordSettings chordSettings = new ChordSettings((CompositeSetting)soundSettings, (CompositeSetting)chordDifficulty,
				new ApplySetting() {
					@Override
					public void apply(Setting setting) {
						applyChordSettings((CompositeSetting) setting);
					}			
		});
		
		soundSettings = client.getSetting(SOUND).findSetting(CADENCE);
		final CadenceSettings cadenceSettings = new CadenceSettings((CompositeSetting)soundSettings, new ApplySetting() {
			@Override
			public void apply(Setting setting) {
				applyCadenceSettings((CompositeSetting) setting);
			}			
		});
		
		soundSettings = client.getSetting(GENERAL);		
		try {
			generalSettings = new GeneralSettings((CompositeSetting)soundSettings, new ApplySetting() {
				@Override
				public void apply(Setting setting) {
					applyGeneralSettings((CompositeSetting) setting);
				}
			});
		} catch (MidiUnavailableException e1) {
			throw new InstantiationException("Could not load general settings: " + e1);
		}
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setForeground(Color.WHITE);
		lblSettings.setFont(new Font("Segoe UI", Font.PLAIN, 40));
		lblSettings.setBounds(20, 11, 170, 54);
		menuPanel.add(lblSettings);
		
		final JLabel intervalLabel = new JLabel("     Interval");
		intervalLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				selectLabel(intervalLabel);
				loadPanel(intervalSettings);
			}
		});
		intervalLabel.setBackground(LABEL_BACK_COLOUR);
		intervalLabel.setForeground(Color.WHITE);
		intervalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		intervalLabel.setBounds(0, 111, 318, 32);
		menuPanel.add(intervalLabel);
		
		final JLabel chordLabel = new JLabel("     Chord");
		chordLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				selectLabel(chordLabel);
				loadPanel(chordSettings);
			}
		});
		chordLabel.setBackground(LABEL_BACK_COLOUR);
		chordLabel.setForeground(Color.WHITE);
		chordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		chordLabel.setBounds(0, 172, 318, 32);
		menuPanel.add(chordLabel);
		
		final JLabel cadenceLabel = new JLabel("     Cadence");
		cadenceLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				selectLabel(cadenceLabel);
				loadPanel(cadenceSettings);
			}
		});
		cadenceLabel.setBackground(LABEL_BACK_COLOUR);
		cadenceLabel.setForeground(Color.WHITE);
		cadenceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		cadenceLabel.setBounds(0, 235, 318, 32);
		menuPanel.add(cadenceLabel);
		
		final JLabel generalLabel = new JLabel("     General");
		generalLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				selectLabel(generalLabel);
				loadPanel(generalSettings);
			}
		});
		generalLabel.setBackground(LABEL_BACK_COLOUR);
		generalLabel.setForeground(Color.WHITE);
		generalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		generalLabel.setBounds(0, 296, 318, 32);
		menuPanel.add(generalLabel);
		
		labels.add(intervalLabel);
		labels.add(chordLabel);
		labels.add(cadenceLabel);
		labels.add(generalLabel);
		
		JLabel back = new JLabel("");
		back.setCursor(new Cursor(Cursor.HAND_CURSOR));
		back.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Back.png")));
		back.setBounds(252, 11, 56, 56);
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				client.exit();
			}
		});
		menuPanel.add(back);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(Colours.DARK_BLUE);
		desktopPane.setBounds(menuPanel.getWidth(), 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		desktopPane.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		getContentPane().add(desktopPane);
		
		desktopPane.add(intervalSettings);
		desktopPane.add(chordSettings);
		desktopPane.add(cadenceSettings);
		desktopPane.add(generalSettings);
	}
	
	/**
	 * Select a given label.
	 * 
	 * @param label JLabel.
	 */
	private void selectLabel(JLabel label) {
		for (JLabel current : labels) {
			if (label != current) {
				current.setOpaque(false);
			} else {
				current.setOpaque(true);
			}
			current.repaint();
		}		
	}
	
	/**
	 * Slide in the selected panel from the right.
	 * 
	 * @param panel JInternalFrame.
	 */
	private void loadPanel(final JInternalFrame panel) {;
		if (currentFrame != null) {
			currentFrame.setVisible(false);
		}
		panel.setVisible(true);
		panel.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width,  0);
		panel.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		((javax.swing.plaf.basic.BasicInternalFrameUI)panel.getUI()).setNorthPane(null);
		currentFrame = panel;
		
		final Timer timer = new Timer(5, null);
		ActionListener action = new ActionListener() {
			final static int PADDING = 20;
			int xPos = panel.getLocation().x - PADDING;
			final int width = xPos;
			int i = 0;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				double val = Math.sin(Math.toRadians(i));
				xPos = (int) (width * val);
				panel.setLocation(width - xPos + PADDING, 0);
				++i;
				
				if (i == 90) {
					timer.stop();
					moving.set(false);
				}
			}
		};
		timer.addActionListener(action);
		timer.start();
		moving.set(true);
	}
	
	/**
	 * Apply the settings made for intervals.
	 * 
	 * @param settings CompositeSetting to apply.
	 */
	private void applyIntervalSettings(CompositeSetting settings) {
		Setting intervalDifficulty = client.getSetting(DIFFICULTY).findSetting(CUSTOM);
		Setting soundSettings = client.getSetting(SOUND).findSetting(INTERVAL);	
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "duration");
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "pause");
		
		CompositeSetting intervals = (CompositeSetting) settings.findSetting(CUSTOM);
		for (Setting setting : intervals) {
			applyIntegerSetting(settings, (CompositeSetting) intervalDifficulty, setting.getName());
		}
		
		client.applySetting(soundSettings);
		client.applySetting(intervalDifficulty);
	}
	
	/**
	 * Apply the settings made for chords.
	 * 
	 * @param settings CompositeSetting to apply.
	 */
	private void applyChordSettings(CompositeSetting settings) {
		Setting chordDifficulty = client.getSetting(DIFFICULTY).findSetting(CHORD).findSetting(CUSTOM);
		Setting soundSettings = client.getSetting(SOUND).findSetting(CHORD);
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "duration");
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "note_pause");
		
		CompositeSetting chords = (CompositeSetting) settings.findSetting(CUSTOM);
		for (Setting setting : chords) {
			applyIntegerSetting(settings, (CompositeSetting) chordDifficulty, setting.getName());
		}
		
		client.applySetting(soundSettings);
		client.applySetting(chordDifficulty);
	}
	
	/**
	 * Apply the settings made for cadences.
	 * 
	 * @param settings CompositeSettings to apply.
	 */
	private void applyCadenceSettings(CompositeSetting settings) {
		Setting soundSettings = client.getSetting(SOUND).findSetting(CADENCE);
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "duration");
		applyIntegerSetting(settings, (CompositeSetting) soundSettings, "pause");
		
		client.applySetting(soundSettings);
	}
	
	/**
	 * Apply the general settings made.
	 * 
	 * @param settings CompositeSettings to apply.
	 */
	private void applyGeneralSettings(CompositeSetting settings) {
		Setting generalSettings = client.getSetting(GENERAL);
		applyIntegerSetting(settings, (CompositeSetting) generalSettings, "instrument");
		applyIntegerSetting(settings, (CompositeSetting) generalSettings, "lowest_note");
		applyIntegerSetting(settings, (CompositeSetting) generalSettings, "highest_note");
		
		client.applySetting(generalSettings);
	}
	
	/**
	 * Apply an individual integer setting.
	 * 
	 * @param settings CompositeSettings to apply.
	 * @param actualCategory CompositeSetting to setting to apply to.
	 * @param settingName String name of control setting.
	 */
	@SuppressWarnings("unchecked")
	private void applyIntegerSetting(CompositeSetting settings, CompositeSetting actualCategory, String settingName) {
		ControlSetting<Integer> controlSetting = (ControlSetting<Integer>) settings.findSetting(settingName);
		
		ControlSetting<Integer> actualControl = (ControlSetting<Integer>) actualCategory.findSetting(settingName);
		actualControl.setValue((Integer) controlSetting.getValue());
	}
}
