package controller.sound;

import java.util.List;
import java.util.Random;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import org.apache.log4j.Logger;

import controller.exceptions.MissingSettingException;
import controller.settings.ControlSetting;
import controller.settings.Setting;

public class MidiPlayer implements SoundPlayer {
	/** Constants. */
	private static final String GENERAL_SETTINGS = "general";
	private static final String LOWEST_NOTE_SETTING = "lowest_note";
	private static final String HIGHEST_NOTE_SETTING = "highest_note";
	private static final String INTERVAL_SETTINGS = "interval";
	private static final String INTERVAL_DURATION_SETTING = "duration";
	private static final String INTERVAL_PAUSE_SETTING = "pause";
	private static final String CHORD_SETTINGS = "chord";
	private static final String CHORD_DURATION_SETTING = "duration";
	private static final String CHORD_NOTE_PAUSE_SETTING = "note_pause";
	private static final String CADENCE_CHORD_DURATION_SETTING = "duration";
	private static final String CADENCE_PAUSE_SETTING = "pause";
	private static final String CADENCE_SETTINGS = "cadence";
	private static final int MIDI_CHANNEL = 5;
	/** Synthesizer used to play the sound. */
	private Synthesizer synthesizer = null;
	private Setting setting = null;
	/** The logger. */
	private Logger logger = Logger.getLogger(MidiPlayer.class);
	/** The lowest note that can be played. */
	private int lowestNote = -1;
	/** The highest note that can be played. */
	private int highestNote = -1;
	/** The duration of the notes played for intervals. */
	private int intervalNoteDuration = -1;
	/** The pause between playing intervals. */
	private int intervalNotePause = -1;
	/** The duration of the chord. */
	private int chordDuration = -1;
	/** The chord duration of cadences. */
	private int cadenceChordDuration = -1;
	/** The pause duration between cadence chords. */
	private int cadencePause = -1;
	/** The current root. */
	private int currentRoot = 0;
	/** The pause between chord notes. */
	private int chordNotePause = -1;

	/**
	 * Constructor for MidiPlayer.
	 * 
	 * @param synthesizer Synthesizer.
	 * @param CompositeSetting List of Settings for this component.
	 */
	public MidiPlayer(Synthesizer synthesizer, Setting setting) {
		this.synthesizer = synthesizer;
		this.setting = setting;
		try {
			setGeneralSettings(setting.findSetting(GENERAL_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find General Settings", e);
		}
		try {
			setIntervalSettings(setting.findSetting(INTERVAL_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error here
			logger.error("Could not find Interval Settings", e);
		}
		try {
			setChordSettings(setting.findSetting(CHORD_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find Chord Settings", e);
		}
		try {
			setCadenceSettings(setting.findSetting(CADENCE_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find cadence settings", e);
		}
	}
	
	/**
	 * Set the general sound settings.
	 * 
	 * @param settings List of general sound Settings.
	 * @throws MissingSettingException
	 */
	private void setGeneralSettings(Setting settings) throws MissingSettingException {
		if (settings == null) {
			throw new MissingSettingException("General settings could not be found");
		} else {
			ControlSetting<?> currentSetting = getSetting(settings, LOWEST_NOTE_SETTING);
			lowestNote = (Integer) currentSetting.getValue();
			currentSetting = getSetting(settings, HIGHEST_NOTE_SETTING);
			highestNote = (Integer) currentSetting.getValue();
		}
	}
	
	/**
	 * Set the interval sound settings.
	 * 
	 * @param settings List of Settings for playing intervals.
	 * @throws MissingSettingException
	 */
	public void setIntervalSettings(Setting settings) throws MissingSettingException {
		if (settings == null) {
			throw new MissingSettingException("Interval settings could not be found");
		} else {
			try {
				ControlSetting<?> currentSetting = getSetting(settings, INTERVAL_DURATION_SETTING);
				intervalNoteDuration = (Integer) currentSetting.getValue();
				currentSetting = getSetting(settings, INTERVAL_PAUSE_SETTING);
				intervalNotePause = (Integer) currentSetting.getValue();
			} catch (MissingSettingException e) {
				throw e;
			}
		}
	}
	
	/**
	 * Set the chord sound settings.
	 * 
	 * @param settings List of Settings for playing chords.
	 * @throws MissingSettingException
	 */
	private void setChordSettings(Setting settings) throws MissingSettingException {
		if (settings == null) {
			throw new MissingSettingException("Chord settings could not be found");
		} else {
			ControlSetting<?> currentSetting = getSetting(settings, CHORD_DURATION_SETTING);
			chordDuration = (Integer) currentSetting.getValue();
			currentSetting = getSetting(settings, CHORD_NOTE_PAUSE_SETTING);
			chordNotePause = (Integer) currentSetting.getValue();
		}
	}
	
	/**
	 * Set the cadence settings.
	 * 
	 * @param settings List of Settings for playing cadences.
	 * @throws MissingSettingException
	 */
	private void setCadenceSettings(Setting settings) throws MissingSettingException {
		if (settings == null) {
			throw new MissingSettingException("Cadence settings could not be found");
		} else {
			try {
				ControlSetting<?> currentSetting = getSetting(settings, CADENCE_CHORD_DURATION_SETTING);
				cadenceChordDuration = (Integer) currentSetting.getValue();
				currentSetting = getSetting(settings, CADENCE_PAUSE_SETTING);
				cadencePause = (Integer) currentSetting.getValue();
			} catch (MissingSettingException e) {
				throw e;
			}
		}
	}
	
	/**
	 * Lookup a specific setting. Does a bit of MissingSetting checking.
	 * 
	 * @param settings The list of Settings.
	 * @param name String- The name of the setting to find.
	 * @return Setting - The result.
	 */
	private ControlSetting<?> getSetting(Setting settings, String name) {
		Setting currentSetting = settings.findSetting(name);
		if (currentSetting != null && currentSetting instanceof ControlSetting) {
			ControlSetting<?> controlSetting = (ControlSetting<?>)currentSetting;
			return controlSetting;
		} else {
			throw new MissingSettingException((name + " Setting could not be found"));
		}
	}

	/**
	 * Play the current interval.
	 * 
	 * @param interval Interval to play.
	 */
	@Override
	public void playInterval(Interval interval) {
		MidiChannel channel = synthesizer.getChannels()[MIDI_CHANNEL];
		channel.noteOn(currentRoot, intervalNoteDuration);
		try {
			Thread.sleep(intervalNotePause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channel.noteOn(currentRoot + interval.getValue(), intervalNoteDuration);
	}
	
	/**
	 * Play an interval with a random root note.
	 * 
	 * @param interval Interval to play.
	 */
	@Override
	public void playNewInterval(Interval interval) {
		currentRoot = getRandomRoot(interval.getValue());
		playInterval(interval);
	}

	/**
	 * Play the current cadence.
	 * The first the chords will be the root.
	 * 
	 * @param cadence Cadence to play.
	 */
	@Override
	public void playCadence(Cadence cadence) {
		List<Interval> keyChordIntervals = cadence.getChordIntervals();		
		
		MidiChannel channel = synthesizer.getChannels()[MIDI_CHANNEL];
		// Play the root chord twice
		Chord rootChord = Chord.Major;
		for (int i = 0; i < 2; ++i) {
			//System.out.println("Playing Unison " + rootChord);
			channel.noteOn(currentRoot, cadenceChordDuration);
			playChordNotes(channel, rootChord, currentRoot, cadenceChordDuration);
			
			try {
				Thread.sleep(cadencePause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Now play the other key chords in the cadence
		for (Interval interval: keyChordIntervals) {
			// Get the current chord
			Chord chord = cadence.getKeyChords().get(keyChordIntervals.indexOf(interval));
			// Play the root
			channel.noteOn(currentRoot + interval.getValue(), cadenceChordDuration);
			// ..and the rest of the notes
			playChordNotes(channel, chord, currentRoot + interval.getValue(), cadenceChordDuration);
			
			try {
				Thread.sleep(cadencePause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Play a cadence with a random root note.
	 * 
	 * @param cadence Cadence
	 */
	@Override
	public void playNewCadence(Cadence cadence) {
		List<Interval> keyChordIntervals = cadence.getChordIntervals();
		// What is the highest interval?
		Interval maxInterval = 
				(keyChordIntervals.get(0).getValue() > keyChordIntervals.get(1).getValue()) ? keyChordIntervals.get(0) : keyChordIntervals.get(1);
		Chord maxChord = cadence.getKeyChords().get(keyChordIntervals.indexOf(maxInterval));
		List<Interval> chordIntervals = maxChord.getIntervals();
		currentRoot = getRandomRoot(chordIntervals.get(chordIntervals.size()-1).getValue() + maxInterval.getValue());
		
		playCadence(cadence);
	}

	/**
	 * Play the current chord.
	 * 
	 * @param chord Chord to play.
	 */
	@Override
	public void playChord(Chord chord) {
		MidiChannel channel = synthesizer.getChannels()[MIDI_CHANNEL];
		channel.noteOn(currentRoot, chordDuration);
		// Play other notes from chord
		playChordNotes(channel, chord, currentRoot, chordDuration);
	}
	
	/**
	 * Play a chord with a new root note
	 */
	@Override
	public void playNewChord(Chord chord) {
		List<Interval> intervals = chord.getIntervals();
		currentRoot = getRandomRoot(intervals.get(intervals.size()-1).getValue());
		
		playChord(chord);
	}
	
	/**
	 * Run through chord Interval list and play all in order.
	 * 
	 * @param channel The MidiChannel to play the notes on.
	 * @param chord The Chord to play.
	 * @param root Integer value of chord root.
	 * @param duration The Integer duration of the notes. 
	 */
	private void playChordNotes(MidiChannel channel, Chord chord, int root, int duration) {
		for (Interval interval: chord.getIntervals()) {
			try {
				Thread.sleep(chordNotePause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			channel.noteOn(root + interval.getValue(), duration);
		}	
	}
	
	/**
	 * Calculate a random interval based on note range constraints.
	 * 
	 * @param maxRange The maximum value from range of intervals.
	 * @return Random Integer.
	 */
	private int getRandomRoot(int maxRange) {
		Random rand = new Random();
		int maxInterval = highestNote + 1 - maxRange;
		int currentRoot = lowestNote + rand.nextInt(maxInterval-lowestNote);
		
		return currentRoot;
	}

	@Override
	public void updateIntervalSettings() {
		try {
			setIntervalSettings(setting.findSetting(INTERVAL_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error here
			logger.error("Could not find Interval Settings", e);
		}
	}

	@Override
	public void updateChordSettings() {
		try {
			setChordSettings(setting.findSetting(CHORD_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find Chord Settings", e);
		}
	}

	@Override
	public void updateCadenceSettings() {
		try {
			setCadenceSettings(setting.findSetting(CADENCE_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find cadence settings", e);
		}	
	}

	@Override
	public void updateGeneralSettings() {
		try {
			setGeneralSettings(setting.findSetting(GENERAL_SETTINGS));
		} catch (MissingSettingException e) {
			// Log error
			logger.error("Could not find general settings", e);
		}
	}
}
