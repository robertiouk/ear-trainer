package gui.settings;

import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

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
 * Frame to hold Cadence settings.
 * 
 * @author Robertio
 *
 */
public class CadenceSettings extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8896564118288446780L;
	private CompositeSetting settings;
	private JSpinner duration;
	private JSpinner pause;
	
	public CadenceSettings(CompositeSetting soundSetting, final ApplySetting applySetting) {
		settings = soundSetting;
		
		getContentPane().setLayout(null);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		displayPanel.setBackground(Colours.DARK_BLUE);
		displayPanel.setLayout(new MigLayout("", "[][][][][75px]", "[][29px][][50px][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]"));

		JScrollPane scrollPane = new JScrollPane(displayPanel);		
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
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) settings.findSetting("duration");
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
		
		JLabel lblNotePause = new JLabel("Pause");
		lblNotePause.setForeground(Color.WHITE);
		lblNotePause.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		displayPanel.add(lblNotePause, "cell 0 2");
		
		pause = new JSpinner();
		pause.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				@SuppressWarnings("unchecked")
				ControlSetting<Integer> intSetting = (ControlSetting<Integer>) settings.findSetting("pause");
				intSetting.setValue((Integer) pause.getValue());
				
				applySetting.apply(settings);
			}		
		});
		pause.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		pause.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayPanel.add(pause, "cell 1 2,width 10:75:500,alignx center");
		
		JLabel lblThePauseBetween = new JLabel("The pause between chords in milliseconds");
		lblThePauseBetween.setForeground(Color.WHITE);
		lblThePauseBetween.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		displayPanel.add(lblThePauseBetween, "cell 0 3,aligny top");

		applySettings();
	}
	
	/**
	 * Apply the settings to the JSpinners.
	 */
	private void applySettings() {
		ControlSetting<?> intSetting = (ControlSetting<?>) settings.findSetting("duration");
		duration.setValue(intSetting.getValue());
		intSetting = (ControlSetting<?>) settings.findSetting("pause");
		pause.setValue(intSetting.getValue());
	}
}
