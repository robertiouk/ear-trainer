package gui.settings;

import gui.appearance.Colours;
import gui.components.AcceptEvent;
import gui.components.ToggleSwitch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;

/**
 * Frame to hold interval settings.
 * 
 * @author Robertio
 *
 */
public class IntervalSettings extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7330948111199948651L;
	private CompositeSetting settings;
	private Map<ToggleSwitch, String> customLookup;
	private Map<ToggleSwitch, JLabel> labelLookup;
	private JSpinner duration;
	private JSpinner pause;
	private ApplySetting applySetting;
	private JPanel displayPanel;

	public IntervalSettings(CompositeSetting soundSetting, CompositeSetting customSetting, final ApplySetting applySetting) {
		getContentPane().setLayout(null);
		
		customLookup = new HashMap<ToggleSwitch, String>();
		labelLookup = new HashMap<ToggleSwitch, JLabel>();
		this.applySetting = applySetting;
		buildSetting(soundSetting, customSetting);
		
		displayPanel = new JPanel();
		displayPanel.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		displayPanel.setBackground(Colours.DARK_BLUE);
		displayPanel.setLayout(new MigLayout("", "[187px][][]", "[42px][29px][][][][][][][][][][][][][][][][][][][][][][]"));		

		JScrollPane scrollPane = new JScrollPane(displayPanel);		
		scrollPane.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width-338, Toolkit.getDefaultToolkit().getScreenSize().height-100);
		scrollPane.getViewport().setBackground(Colours.DARK_BLUE);
		scrollPane.setBorder(null);
		getContentPane().add(scrollPane);
		customLookup = new HashMap<ToggleSwitch, String>();
		labelLookup = new HashMap<ToggleSwitch, JLabel>();
		this.applySetting = applySetting;
		buildSetting(soundSetting, customSetting);		
		
		JLabel lblNewLabel = new JLabel("Duration");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblNewLabel, "flowx,cell 0 0,grow,width 200:200:200");
		
		duration = new JSpinner();
		duration.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				CompositeSetting sound = (CompositeSetting) settings.findSetting("sound");
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) sound.findSetting("duration");
				intSetting.setValue((Integer) duration.getValue());
				
				applySetting.apply(settings);
			}
		});
		duration.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		duration.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(duration, "cell 1 0,alignx center aligny center,width 10:75:500");
		
		JLabel lblNewLabel_1 = new JLabel("The speed with which the note was released");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblNewLabel_1, "cell 0 1 2 1, aligny top");
		
		JLabel lblPause = new JLabel("Pause");
		lblPause.setForeground(Color.WHITE);
		lblPause.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblPause, "cell 0 2");
		
		pause = new JSpinner();
		pause.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				CompositeSetting sound = (CompositeSetting) settings.findSetting("sound");
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) sound.findSetting("pause");
				intSetting.setValue((Integer) pause.getValue());
				
				applySetting.apply(settings);
			}		
		});
		pause.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		pause.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(pause, "cell 1 2,width 10:75:500,alignx center");
		
		JLabel lblThePauseBetween = new JLabel("The pause between intervals in milliseconds");
		lblThePauseBetween.setForeground(Color.WHITE);
		lblThePauseBetween.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblThePauseBetween, "cell 0 3 2 1, aligny top");
		
		JLabel lblNewLabel_2 = new JLabel("");
		displayPanel.add(lblNewLabel_2, "cell 0 4, height 25:50:50");
		
		JLabel lblCustomIntervals = new JLabel("Custom Intervals");
		lblCustomIntervals.setForeground(Color.WHITE);
		lblCustomIntervals.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblCustomIntervals, "cell 0 5, height 50:50:50");
		
		JLabel lblMinornd = new JLabel("Minor 2nd");
		lblMinornd.setForeground(Color.WHITE);
		lblMinornd.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinornd, "cell 0 6");
				
		JLabel lblMajornd = new JLabel("Major 2nd");
		lblMajornd.setForeground(Color.WHITE);
		lblMajornd.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajornd, "cell 0 7");
				
		JLabel lblMinorrd = new JLabel("Minor 3rd");
		lblMinorrd.setForeground(Color.WHITE);
		lblMinorrd.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorrd, "cell 0 8");
		
		JLabel lblMajorrd = new JLabel("Major 3rd");
		lblMajorrd.setForeground(Color.WHITE);
		lblMajorrd.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorrd, "cell 0 9");
				
		JLabel lblPerfectth = new JLabel("Perfect 4th");
		lblPerfectth.setForeground(Color.WHITE);
		lblPerfectth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblPerfectth, "cell 0 10");
		
		JLabel lblPerfectth_1 = new JLabel("Diminished 5th");
		lblPerfectth_1.setForeground(Color.WHITE);
		lblPerfectth_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblPerfectth_1, "cell 0 11");
		
		JLabel lblPerfectth_2 = new JLabel("Perfect 5th");
		lblPerfectth_2.setForeground(Color.WHITE);
		lblPerfectth_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblPerfectth_2, "cell 0 12");
		
		JLabel lblMinorth = new JLabel("Minor 6th");
		lblMinorth.setForeground(Color.WHITE);
		lblMinorth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorth, "cell 0 13");
		
		JLabel lblMajorth = new JLabel("Major 6th");
		lblMajorth.setForeground(Color.WHITE);
		lblMajorth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth, "cell 0 14");
		
		JLabel lblMinorth_1 = new JLabel("Minor 7th");
		lblMinorth_1.setForeground(Color.WHITE);
		lblMinorth_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorth_1, "cell 0 15");
		
		JLabel lblMajorth_1 = new JLabel("Major 7th");
		lblMajorth_1.setForeground(Color.WHITE);
		lblMajorth_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth_1, "cell 0 16");
		
		JLabel lblOctave = new JLabel("Octave");
		lblOctave.setForeground(Color.WHITE);
		lblOctave.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblOctave, "cell 0 17");
		
		JLabel lblMinorth_2 = new JLabel("Minor 9th");
		lblMinorth_2.setForeground(Color.WHITE);
		lblMinorth_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorth_2, "cell 0 18");
		
		JLabel lblMajorth_2 = new JLabel("Major 9th");
		lblMajorth_2.setForeground(Color.WHITE);
		lblMajorth_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth_2, "cell 0 19,aligny baseline");
		
		JLabel lblAugmentedth = new JLabel("Augmented 9th");
		lblAugmentedth.setForeground(Color.WHITE);
		lblAugmentedth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblAugmentedth, "cell 0 20");
		
		JLabel lblMajorth_3 = new JLabel("Major 10th");
		lblMajorth_3.setForeground(Color.WHITE);
		lblMajorth_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth_3, "cell 0 21");
		
		JLabel lblPerfectth_3 = new JLabel("Perfect 11th");
		lblPerfectth_3.setForeground(Color.WHITE);
		lblPerfectth_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblPerfectth_3, "cell 0 22");
		
		JLabel lblMajorth_4 = new JLabel("Major 13th");
		lblMajorth_4.setForeground(Color.WHITE);
		lblMajorth_4.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth_4, "cell 0 23");
				
		setBorder(null);
		
		buildSwitches();
		applySettings();
	}
	
	/**
	 * Compile given settings into a single settings holder
	 * 
	 * @param soundSetting CompositeSetting<Integer>
	 * @param customSetting CompositeSetting<Integer>
	 */
	private void buildSetting(CompositeSetting soundSetting, CompositeSetting customSetting) {
		settings = new CompositeSetting("setting");
		CompositeSetting tempCustom = new CompositeSetting("custom");
		for (Setting currentSetting : customSetting) {
			ControlSetting<?> intSetting = (ControlSetting<?>) currentSetting;
			tempCustom.addSetting(new ControlSetting<Integer>((Integer) intSetting.getValue(), intSetting.getName()));
		}
		settings.addSetting(tempCustom);
		
		CompositeSetting tempSound = new CompositeSetting("sound");
		for (Setting currentSetting : soundSetting) {
			ControlSetting<?> intSetting = (ControlSetting<?>) currentSetting;
			tempSound.addSetting(new ControlSetting<Integer>((Integer) intSetting.getValue(), intSetting.getName()));
		}
		settings.addSetting(tempSound);
	}
	
	/**
	 * Build toggle switches for custom intervals.
	 */
	private void buildSwitches() {
		int startRow = 6;
		final CompositeSetting custom = (CompositeSetting) settings.findSetting("custom");
		for (Setting setting : custom) {
			ControlSetting<?> intSetting = (ControlSetting<?>) setting;
			boolean value = false;
			String status = "Off";
			if ((Integer) intSetting.getValue() == 1) {
				value = true;
				status = "On";
			}
			final ToggleSwitch toggleSwitch = new ToggleSwitch(value);
			toggleSwitch.setBackground(Colours.LIGHT_TURQUOISE);
			toggleSwitch.addClickEvent(new AcceptEvent() {
				@Override
				public void accept() {
					@SuppressWarnings("unchecked")
					ControlSetting<Integer> intSetting = (ControlSetting<Integer>) custom.findSetting(customLookup.get(toggleSwitch));
					if (toggleSwitch.getState()) {
						intSetting.setValue(0);
						labelLookup.get(toggleSwitch).setText("Off");
					} else {
						intSetting.setValue(1);
						labelLookup.get(toggleSwitch).setText("On");
					}
					applySetting.apply(settings);
				}
			});
			displayPanel.add(toggleSwitch, "cell 1 " + Integer.toString(startRow));
			
			JLabel label = new JLabel(status);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			displayPanel.add(label, "cell 2 " + Integer.toString(startRow) + ", alignx center");
			
			customLookup.put(toggleSwitch, intSetting.getName());
			labelLookup.put(toggleSwitch, label);
		    ++startRow;
		}
	}
	
	/**
	 * Apply the settings to the JSpinners.
	 */
	private void applySettings() {
		CompositeSetting sound = (CompositeSetting) settings.findSetting("sound");
		ControlSetting<?> intSetting = (ControlSetting<?>) sound.findSetting("duration");
		duration.setValue(intSetting.getValue());
		intSetting = (ControlSetting<?>) sound.findSetting("pause");
		pause.setValue(intSetting.getValue());
	}
}
