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

import net.miginfocom.swing.MigLayout;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Frame to hold chord settings.
 * 
 * @author Robertio
 *
 */
public class ChordSettings extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4148914153550096391L;
	private JPanel displayPanel;
	private JScrollPane scrollPane;
	private CompositeSetting settings;
	private Map<ToggleSwitch, String> customLookup;
	private Map<ToggleSwitch, JLabel> labelLookup;
	private ApplySetting applySetting;
	JSpinner duration;
	JSpinner notePause;

	public ChordSettings(CompositeSetting soundSetting, CompositeSetting customSetting, final ApplySetting applySetting) {
		customLookup = new HashMap<ToggleSwitch, String>();
		labelLookup = new HashMap<ToggleSwitch, JLabel>();
		this.applySetting = applySetting;
		buildSetting(soundSetting, customSetting);
		
		getContentPane().setLayout(null);
		
		displayPanel = new JPanel();
		displayPanel.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		displayPanel.setBackground(Colours.DARK_BLUE);
		displayPanel.setLayout(new MigLayout("", "[][][][][75px]", "[][29px][][50px][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]"));

		scrollPane = new JScrollPane(displayPanel);		
		scrollPane.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width-338, Toolkit.getDefaultToolkit().getScreenSize().height-100);
		scrollPane.getViewport().setBackground(Colours.DARK_BLUE);
		scrollPane.setBorder(null);
		getContentPane().add(scrollPane);
		
		setBorder(null);
		
		JLabel label = new JLabel("Duration");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(label, "cell 0 0, width 200:200:200");
		
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
		duration.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		duration.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(duration, "cell 1 0, alignx center aligny center,width 10:75:500");
		
		JLabel label_2 = new JLabel("The speed with which the note was released");
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(label_2, "cell 0 1 2 1,aligny top");
		
		JLabel lblNotePause = new JLabel("Note Pause");
		lblNotePause.setForeground(Color.WHITE);
		lblNotePause.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblNotePause, "cell 0 2");
		
		notePause = new JSpinner();
		notePause.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				CompositeSetting sound = (CompositeSetting) settings.findSetting("sound");
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) sound.findSetting("note_pause");
				intSetting.setValue((Integer) notePause.getValue());
				
				applySetting.apply(settings);
			}		
		});
		notePause.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		notePause.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(notePause, "cell 1 2,width 10:75:500,alignx center");
		
		JLabel lblThePauseBetween = new JLabel("The pause between chord notes in milliseconds");
		lblThePauseBetween.setForeground(Color.WHITE);
		lblThePauseBetween.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblThePauseBetween, "cell 0 3,aligny top");
		
		JLabel lblCustomChrods = new JLabel("Custom Chords");
		lblCustomChrods.setForeground(Color.WHITE);
		lblCustomChrods.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblCustomChrods, "cell 0 4,height 50:50:50");
		
		JLabel lblMajor = new JLabel("Major");
		lblMajor.setForeground(Color.WHITE);
		lblMajor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajor, "cell 0 5");
		
		JLabel lblMinor = new JLabel("Minor");
		lblMinor.setForeground(Color.WHITE);
		lblMinor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinor, "cell 0 6");
		
		JLabel lblSuspendedth = new JLabel("Suspended 4");
		lblSuspendedth.setForeground(Color.WHITE);
		lblSuspendedth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblSuspendedth, "cell 0 7");
		
		JLabel lblAugmentedth = new JLabel("Augmented 5");
		lblAugmentedth.setForeground(Color.WHITE);
		lblAugmentedth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblAugmentedth, "cell 0 8");
		
		JLabel lblMinorth = new JLabel("Minor 6");
		lblMinorth.setForeground(Color.WHITE);
		lblMinorth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorth, "cell 0 9");
		
		JLabel lblMajorth = new JLabel("Major 6");
		lblMajorth.setForeground(Color.WHITE);
		lblMajorth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajorth, "cell 0 10");
		
		JLabel lblMinorth_1 = new JLabel("Minor 7");
		lblMinorth_1.setForeground(Color.WHITE);
		lblMinorth_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorth_1, "cell 0 11");
		
		JLabel lblMinorb = new JLabel("Minor 7b5");
		lblMinorb.setForeground(Color.WHITE);
		lblMinorb.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorb, "cell 0 12");
		
		JLabel lblMinor_1 = new JLabel("Minor 7#5");
		lblMinor_1.setForeground(Color.WHITE);
		lblMinor_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinor_1, "cell 0 13");
		
		JLabel lblDominant = new JLabel("Dominant 7");
		lblDominant.setForeground(Color.WHITE);
		lblDominant.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant, "cell 0 14");
		
		JLabel lblDominantb = new JLabel("Dominant 7b5");
		lblDominantb.setForeground(Color.WHITE);
		lblDominantb.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantb, "cell 0 15");
		
		JLabel lblDominant_1 = new JLabel("Dominant 7#5");
		lblDominant_1.setForeground(Color.WHITE);
		lblDominant_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_1, "cell 0 16");
		
		JLabel lblMajor_1 = new JLabel("Major 7");
		lblMajor_1.setForeground(Color.WHITE);
		lblMajor_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajor_1, "cell 0 17");
		
		JLabel lblDiminished = new JLabel("Diminished 7");
		lblDiminished.setForeground(Color.WHITE);
		lblDiminished.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDiminished, "cell 0 18");
		
		JLabel lblMinor_2 = new JLabel("Minor 9");
		lblMinor_2.setForeground(Color.WHITE);
		lblMinor_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinor_2, "cell 0 19");
		
		JLabel lblMinorb_1 = new JLabel("Minor 7b9");
		lblMinorb_1.setForeground(Color.WHITE);
		lblMinorb_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorb_1, "cell 0 20");
		
		JLabel lblMinorbb = new JLabel("Minor 7b5b9");
		lblMinorbb.setForeground(Color.WHITE);
		lblMinorbb.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorbb, "cell 4 5, width 200::");
		
		JLabel lblMinorb_2 = new JLabel("Minor 7#5b9");
		lblMinorb_2.setForeground(Color.WHITE);
		lblMinorb_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorb_2, "cell 4 6");
		
		JLabel lblMinorb_3 = new JLabel("Minor 9b5");
		lblMinorb_3.setForeground(Color.WHITE);
		lblMinorb_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinorb_3, "cell 4 7");
		
		JLabel lblMinor_3 = new JLabel("Minor 9#5");
		lblMinor_3.setForeground(Color.WHITE);
		lblMinor_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMinor_3, "cell 4 8");
		
		JLabel lblDominant_2 = new JLabel("Dominant 9");
		lblDominant_2.setForeground(Color.WHITE);
		lblDominant_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_2, "cell 4 9");
		
		JLabel lblDominantb_1 = new JLabel("Dominant 7b9");
		lblDominantb_1.setForeground(Color.WHITE);
		lblDominantb_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantb_1, "cell 4 10");
		
		JLabel lblDominant_3 = new JLabel("Dominant 7#9");
		lblDominant_3.setForeground(Color.WHITE);
		lblDominant_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_3, "cell 4 11");
		
		JLabel lblDominantb_2 = new JLabel("Dominant 7b5#9");
		lblDominantb_2.setForeground(Color.WHITE);
		lblDominantb_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantb_2, "cell 4 12");

		JLabel lblDominantb_x = new JLabel("Dominant 7#5b9");
		lblDominantb_x.setForeground(Color.WHITE);
		lblDominantb_x.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantb_x, "cell 4 13");

		JLabel lblDominant_4 = new JLabel("Dominant 7#5#9");
		lblDominant_4.setForeground(Color.WHITE);
		lblDominant_4.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_4, "cell 4 14");
		
		JLabel lblDominantbb = new JLabel("Dominant 7b5b9");
		lblDominantbb.setForeground(Color.WHITE);
		lblDominantbb.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantbb, "cell 4 15");
		
		JLabel lblDominantb_3 = new JLabel("Dominant 9b5");
		lblDominantb_3.setForeground(Color.WHITE);
		lblDominantb_3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominantb_3, "cell 4 16");
		
		JLabel lblDominant_5 = new JLabel("Dominant 9#5");
		lblDominant_5.setForeground(Color.WHITE);
		lblDominant_5.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_5, "cell 4 17");
		
		JLabel lblMajor_2 = new JLabel("Major 9");
		lblMajor_2.setForeground(Color.WHITE);
		lblMajor_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblMajor_2, "cell 4 18");
		
		JLabel lblDominant_6 = new JLabel("Dominant 11");
		lblDominant_6.setForeground(Color.WHITE);
		lblDominant_6.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_6, "cell 4 19");

		JLabel lblDominant_7 = new JLabel("Dominant 13");
		lblDominant_7.setForeground(Color.WHITE);
		lblDominant_7.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		displayPanel.add(lblDominant_7, "cell 4 20");
		
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
	 * Build toggle switches for custom chords.
	 */
	private void buildSwitches() {
		int startRow = 5;
		int startCol = 1;
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
			displayPanel.add(toggleSwitch, "cell " + Integer.toString(startCol) + " " + Integer.toString(startRow));
			
			JLabel label = new JLabel(status);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			displayPanel.add(label, "cell " + Integer.toString(startCol+1) + " " + Integer.toString(startRow) + ", alignx left, width 200::");
			
			customLookup.put(toggleSwitch, intSetting.getName());
			labelLookup.put(toggleSwitch, label);
			
			if (startRow == 20) {
				startRow = 5;
				startCol += 4;
			} else {
				++startRow;		 	
			}		    
		}
	}
	
	/**
	 * Apply the settings to the JSpinners.
	 */
	private void applySettings() {
		CompositeSetting sound = (CompositeSetting) settings.findSetting("sound");
		ControlSetting<?> intSetting = (ControlSetting<?>) sound.findSetting("duration");
		duration.setValue(intSetting.getValue());
		intSetting = (ControlSetting<?>) sound.findSetting("note_pause");
		notePause.setValue(intSetting.getValue());
	}
}
