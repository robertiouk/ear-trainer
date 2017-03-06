package gui;

import gui.components.GameFrame;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import client.menu.CadenceClient;

/**
 * The GUI for cadences.
 * 
 * @author Robertio
 *
 */
public class Cadence extends GameFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3087162753787503057L;

	private CadenceClient client;
	
	public Cadence(CadenceClient client, String title) {
		super(title);
		
		this.client = client;
		buildButtons();	
	}

	@Override
	protected void play() {
		client.play();
	}

	@Override
	protected void getNext() {
		client.getNextCadence();
	}

	@Override
	protected void exit() {
		client.exit();
	}

	@Override
	protected void buildButtons() {
		buttonList.clear();
		buttonLabelList.clear();
		List<controller.sound.Cadence> cadences = client.getCadences();
		
		int cellx = 0;
		int celly = 1;
		int columnLimit = 5;
		int width = 152;
		int height = 92;
		for (final controller.sound.Cadence cadence : cadences) {
			JLabel labelText = new JLabel(cadence.toString());
			labelText.setFont(new Font("Segoe UI", Font.BOLD, 12));
			labelText.setSize(width, height);
			labelText.setHorizontalAlignment(SwingConstants.CENTER);
			labelText.setVerticalAlignment(SwingConstants.CENTER);
			String cell = "cell " + cellx + " " + celly + ", pos (" + cadence.toString() + ".x) (" + cadence.toString() + ".y), width " + width + "::, height " + height + "::";
			displayPanel.add(labelText, cell);
			buttonLabelList.add(labelText);
			final JLabel newLabel = new JLabel();
			newLabel.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Button.png")));
			newLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

				}
				@Override
				public void mousePressed(MouseEvent e) {
					newLabel.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/ButtonPressed.png")));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					newLabel.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Button.png")));
					if (!controlLock) {
						processResult(client.submit(cadence));
					}
				}
			});
			cell = "cell " + cellx + " " + celly + ", grow, gapx 25px, gapy 25px, id " + cadence.toString();
			displayPanel.add(newLabel, cell);
			buttonList.add(newLabel);
			
			if (cellx < columnLimit) {
				++cellx;
			} else {
				cellx = 0;
				++celly;
			}
		}
	}

	/**
	 * Process the submitted cadence and display info pane.
	 * 
	 * @param correct Boolean.
	 */
	private void processResult(boolean correct) {
		if (correct) {
			infoPane = correctPane;
		} else {
			infoPane = incorrectPane;
		}
		infoPane.setVisible(true);
		
		setControlLock(true);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (client != null) {
			client.updateSettings();
		}
		super.setVisible(visible);
	}
}
