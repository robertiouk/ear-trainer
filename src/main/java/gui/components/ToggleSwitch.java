package gui.components;

import gui.appearance.Colours;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * Windows 8 style toggle switch;
 * 
 * @author Robertio
 *
 */
public class ToggleSwitch extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2972031220570482232L;
	private static final int SWITCH_WIDTH = 10;
	private static final int COMPONENT_WIDTH = 70;
	private static final int SELECT_HEIGHT = 14;
	private static final int BORDER_WIDTH = 3;
	
	private AtomicBoolean state;
	private JPanel toggleSwitch;
	private JPanel selected;
	private AcceptEvent event;
	private Timer timer;
	
	public ToggleSwitch(boolean initialValue) {
		setBackground(new Color(0, 0, 128));
		MouseAdapter clickEvent = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				toggle();
				if (event != null) {
					event.accept();
				}
			}
		};
		state = new AtomicBoolean(initialValue);
		
		addMouseListener(clickEvent);
		setForeground(Color.WHITE);
		setBorder(new LineBorder(new Color(255, 255, 255), BORDER_WIDTH));
		setPreferredSize(new Dimension(COMPONENT_WIDTH, 20));
		setLayout(null);
				
		toggleSwitch = new JPanel();
		toggleSwitch.setBorder(null);
		int startX;
		if (initialValue) {
			startX = COMPONENT_WIDTH - SWITCH_WIDTH;
		} else {
			startX = 0;
		}
		toggleSwitch.setBounds(startX, 0, SWITCH_WIDTH, 20);
		toggleSwitch.setBackground(Colours.DARK_GREY);
		toggleSwitch.addMouseListener(clickEvent);
		add(toggleSwitch);
		
		selected = new JPanel();
		selected.setBackground(new Color(0, 100, 0));
		selected.setBorder(null);
		selected.setBounds(BORDER_WIDTH, BORDER_WIDTH, startX, SELECT_HEIGHT);
		selected.addMouseListener(clickEvent);
		add(selected);

		timer = new Timer(10, null);
		timer.addActionListener(new ActionListener() {
			private static final int increment = 5;
			
			private boolean conditionMet() {
				if (state.get()) {
					return toggleSwitch.getLocation().x > 0;
				} else {
					return toggleSwitch.getLocation().x < COMPONENT_WIDTH - SWITCH_WIDTH;					
				}
			}
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int location;
				boolean condition = conditionMet();
				
				if (condition) {
					location = toggleSwitch.getLocation().x;
					
					if (state.get()) {
						location -= increment;
					} else {
						location += increment;					
					}
					
					toggleSwitch.setLocation(location, 0);
					selected.setBounds(BORDER_WIDTH, BORDER_WIDTH, location, SELECT_HEIGHT);
				} else {
					state.set(!state.get());
					timer.stop();
				}		
			}			
		});
	}
	
	/**
	 * Animate the toggle switch
	 */
	private void toggle() {
		timer.start();
	}
	
	/**
	 * Get the boolean state of the switch
	 * 
	 * @return Boolean
	 */
	public boolean getState() {
		return state.get();
	}
	
	/**
	 * Add a click event (accept event) to the switch.
	 * 
	 * @param e AcceptEvent
	 */
	public void addClickEvent(AcceptEvent e) {
		event = e;
	}
}
