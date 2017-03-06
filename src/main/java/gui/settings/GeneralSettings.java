package gui.settings;

import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.sound.midi.MidiUnavailableException;
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

/**
 * Frame to hold general settings.
 * 
 * @author Robertio
 *
 */
public class GeneralSettings extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 450428445608015526L;
	private CompositeSetting settings;
	//private JComboBox<String> instrument;
	private JSpinner lowestNote;
	private JSpinner highestNote;

	public GeneralSettings(final CompositeSetting generalSetting, final ApplySetting applySetting) throws MidiUnavailableException {
		getContentPane().setLayout(null);
		settings = generalSetting;
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		displayPanel.setBackground(Colours.DARK_BLUE);
		displayPanel.setLayout(new MigLayout("", "[][]", "[][29px][][29px]"));

		JScrollPane scrollPane = new JScrollPane(displayPanel);		
		scrollPane.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width-338, Toolkit.getDefaultToolkit().getScreenSize().height-100);
		scrollPane.getViewport().setBackground(Colours.DARK_BLUE);
		scrollPane.setBorder(null);
		getContentPane().add(scrollPane);

		setBorder(null);
		
		/*JLabel label = new JLabel("Instrument");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(label, "cell 0 0, width 200:200:200");
		
		Instrument[] instruments = MidiSystem.getSynthesizer().getDefaultSoundbank().getInstruments();
		instrument = new JComboBox<String>();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		final Map<String, Integer> instrumentList = new HashMap<String, Integer>();
		int index = 0;
		for (Instrument current : instruments) {
			model.addElement(current.getName());
			instrumentList.put(current.getName(), index);
			++index;
		}
		instrument.setModel(model);
		instrument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) generalSetting.findSetting("instrument");
				int instrumentIndex = instrumentList.get(instrument.getModel().getSelectedItem());
				intSetting.setValue(instrumentIndex);
				
				applySetting.apply(generalSetting);
			}
		});
		instrument.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(instrument, "cell 1 0, alignx center aligny center,width 200:200:500");
		
		JLabel lblTheInstrumentUsed = new JLabel("The instrument used to play all sounds.");
		lblTheInstrumentUsed.setForeground(Color.WHITE);
		lblTheInstrumentUsed.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblTheInstrumentUsed, "cell 0 1 2 1,aligny top");*/
		
		JLabel label2 = new JLabel("Lowest Note");
		label2.setForeground(Color.WHITE);
		label2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(label2, "cell 0 0,width 200:200:200");
		
		lowestNote = new JSpinner();
		lowestNote.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) generalSetting.findSetting("lowest_note");
				intSetting.setValue((Integer) lowestNote.getValue());
				
				applySetting.apply(generalSetting);
			}	
		});
		lowestNote.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		lowestNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(lowestNote, "cell 1 0,width 10:75:500,hmin 25,aligny center");
		
		JLabel lblTheLowestPossible = new JLabel("The lowest possible note that can be played.");
		lblTheLowestPossible.setForeground(Color.WHITE);
		lblTheLowestPossible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblTheLowestPossible, "cell 0 1 2 1,aligny top");
		
				JLabel label3 = new JLabel("Highest Note");
				label3.setForeground(Color.WHITE);
				label3.setFont(new Font("Segoe UI", Font.PLAIN, 24));
				displayPanel.add(label3, "cell 0 2,wmin 200");
		
		highestNote = new JSpinner();
		highestNote.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) generalSetting.findSetting("highest_note");
				intSetting.setValue((Integer) highestNote.getValue());
				
				applySetting.apply(generalSetting);
			}			
		});
		highestNote.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(0)));
		highestNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(highestNote, "cell 1 2,width 10:75:500,hmin 25,aligny center");
		
		JLabel highestPossible = new JLabel("The highest possible note that can be played.");
		highestPossible.setForeground(Color.WHITE);
		highestPossible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(highestPossible, "cell 0 3 2 1,aligny top");
		
		applySettings();
	}
	
	/**
	 * Apply the settings to the JSpinners.
	 */
	private void applySettings() {
		//ControlSetting<?> intSetting = (ControlSetting<?>) settings.findSetting("instrument");
		//instrument.setSelectedIndex((Integer)intSetting.getValue());
		
		ControlSetting<?> intSetting = (ControlSetting<?>) settings.findSetting("lowest_note");
		lowestNote.setValue(intSetting.getValue());
		
		intSetting = (ControlSetting<?>) settings.findSetting("highest_note");
		highestNote.setValue(intSetting.getValue());
	}
}
