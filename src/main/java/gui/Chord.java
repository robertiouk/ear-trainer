package gui;

import gui.components.GameFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import client.menu.ChordClient;

/**
 * GUI for Chord form.
 * 
 * @author Robertio
 *
 */
public class Chord extends GameFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7463374350303423853L;
	private ChordClient client;

	public Chord(ChordClient client, String title) {
		super(title);
		this.client = client;
		
		addSettingsPanel();
		JRadioButton easyButton = new JRadioButton("Easy");
		easyButton.setOpaque(false);;
		easyButton.setForeground(Color.WHITE);
		easyButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		easyButton.setBounds(32, 134, 109, 23);
		easyButton.setFocusPainted(false);
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset("easy");
			}
		});
		settingsPanel.add(easyButton);
		
		JRadioButton mediumButton = new JRadioButton("Medium");
		mediumButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mediumButton.setForeground(Color.WHITE);
		mediumButton.setOpaque(false);
		mediumButton.setBounds(32, 179, 206, 23);
		mediumButton.setFocusPainted(false);
		mediumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset("medium");
			}
		});
		settingsPanel.add(mediumButton);
		
		JRadioButton hardButton = new JRadioButton("Hard");
		hardButton.setForeground(Color.WHITE);
		hardButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		hardButton.setFocusPainted(false);
		hardButton.setOpaque(false);
		hardButton.setBounds(32, 225, 206, 23);
		hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset("hard");
			}
		});

		settingsPanel.add(hardButton);
		
		JRadioButton customButton = new JRadioButton("Custom");
		customButton.setForeground(Color.WHITE);
		customButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		customButton.setFocusPainted(false);
		customButton.setOpaque(false);
		customButton.setBounds(32, 271, 206, 23);
		customButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset("custom");
			}
		});

		settingsPanel.add(customButton);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(easyButton);
		buttonGroup.add(mediumButton);
		buttonGroup.add(hardButton);
		buttonGroup.add(customButton);
		
		String settingName = client.getSetting();
		Map<String, JRadioButton> settingLookup = new HashMap<String, JRadioButton>();
		settingLookup.put("easy", easyButton);
		settingLookup.put("medium", mediumButton);
		settingLookup.put("hard", hardButton);
		settingLookup.put("custom", customButton);
		settingLookup.get(settingName).setSelected(true);
		
		JLabel lblNewLabel_1 = new JLabel("Difficulty");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		lblNewLabel_1.setBounds(20, 92, 155, 23);
		settingsPanel.add(lblNewLabel_1);
		
		buildButtons();
	}

	@Override
	protected void play() {
		client.play();
	}

	@Override
	protected void getNext() {
		client.getNextChord();
	}

	@Override
	protected void exit() {
		client.exit();
	}

	@Override
	protected void buildButtons() {
		buttonList.clear();
		buttonLabelList.clear();
		List<controller.sound.Chord> chords = client.getChords();
		
		int cellx = 0;
		int celly = 1;
		int columnLimit = 5;
		int width = 152;
		int height = 92;
		for (final controller.sound.Chord chord : chords) {
			JLabel labelText = new JLabel(chord.toString().replaceAll("Sharp", "#"));
			labelText.setFont(new Font("Segoe UI", Font.BOLD, 12));
			labelText.setSize(width, height);
			labelText.setHorizontalAlignment(SwingConstants.CENTER);
			labelText.setVerticalAlignment(SwingConstants.CENTER);
			String cell = "cell " + cellx + " " + celly + ", pos (" + chord.toString() + ".x) (" + chord.toString() + ".y), width " + width + "::, height " + height + "::";
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
					if (!controlLock) {
						processResult(client.submit(chord));
					}
					newLabel.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Button.png")));
				}
			});
			cell = "cell " + cellx + " " + celly + ", grow, gapx 25px, gapy 25px, id " + chord.toString();
			displayPanel.add(newLabel, cell);
			buttonList.add(newLabel);
			
			if (cellx < columnLimit) {
				++cellx;
			} else {
				cellx = 0;
				++celly;
			}
		}
		displayPanel.revalidate();
		scrollPane.revalidate();
	}
	
	/**
	 * Process the submitted interval and display info pane.
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
	
	/**
	 * Reset with a new setting and chord.
	 * 
	 * @param setting
	 */
	private void reset(String setting) {
		client.setSetting(setting);
		clearButtons();
		buildButtons();
		repaint();
		revalidate();
		client.getNextChord();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (client != null) {
			client.updateSettings();
			if (client.getSetting().equals("custom")) {
				clearButtons();
				buildButtons();
			}
		}
		super.setVisible(visible);
	}
}
