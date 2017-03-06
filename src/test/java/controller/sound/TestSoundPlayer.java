package controller.sound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;

public class TestSoundPlayer {
	private static final int NOTE_DURATION = 100;
	private static final int CHORD_DURATION = 200;
	private static final int CADENCE_CHORD_DURATION = 600;
	private static final int LOWEST_NOTE = 39; // Low E
	private static final int HIGHEST_NOTE = 87; // High E - 12th fret
	private static final int CHORD_NOTE_PAUSE = 0;
	private Synthesizer synthesizer = null;
	private MidiChannel[] channels = null;
	CompositeSetting soundSettings = null;
			
	@Before
	public void setup() {
		// Create settings for sound player
		soundSettings = new CompositeSetting("sound");
		// General settings
		CompositeSetting generalSettings = new CompositeSetting("general");
		Setting lowestNote = new ControlSetting<Integer>(LOWEST_NOTE, "lowest_note");
		Setting highestNote = new ControlSetting<Integer>(HIGHEST_NOTE, "highest_note");
		generalSettings.addSetting(lowestNote);
		generalSettings.addSetting(highestNote);
		// Interval settings
		CompositeSetting intervalSettings = new CompositeSetting("interval");
		Setting noteDuration = new ControlSetting<Integer>(NOTE_DURATION, "duration");
		Setting intervalPause = new ControlSetting<Integer>(0, "pause");
		// Add interval settings
		intervalSettings.addSetting(intervalPause);
		intervalSettings.addSetting(noteDuration);
		// Chord settings
		CompositeSetting chordSettings = new CompositeSetting("chord");
		Setting chordDuration = new ControlSetting<Integer>(CHORD_DURATION, "duration");
		Setting chordNotePause = new ControlSetting<Integer>(CHORD_NOTE_PAUSE, "note_pause");
		// Add chord settings
		chordSettings.addSetting(chordDuration);
		chordSettings.addSetting(chordNotePause);
		// Cadence settings
		CompositeSetting cadenceSettings = new CompositeSetting("cadence");
		Setting cadenceChordDuration = new ControlSetting<Integer>(CADENCE_CHORD_DURATION, "duration");
		Setting cadencePause = new ControlSetting<Integer>(0, "pause");
		// Add cadence settings
		cadenceSettings.addSetting(cadenceChordDuration);
		cadenceSettings.addSetting(cadencePause);
		// Add sound settings
		soundSettings.addSetting(generalSettings);
		soundSettings.addSetting(intervalSettings);
		soundSettings.addSetting(chordSettings);
		soundSettings.addSetting(cadenceSettings);
		
		synthesizer = mock(Synthesizer.class);
		// Mock the synthesizer channels
		channels = new MidiChannel[6];
		for (int i = 0; i < 6; ++i) {
			channels[i] = mock(MidiChannel.class);
		}
		when(synthesizer.getChannels()).thenReturn(channels);
	}

	/**
	 * 1. Play every Interval.
	 * 2. Assert the correct interval was played.
	 */
	@Test
	public void testPlayInterval() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		// Loop through Intervals
		int callCount = 1;
		for (Interval currentInterval: Interval.values()) {
			// Play the current interval
			soundPlayer.playInterval(currentInterval);
			
			// Verify the root note was played and record which not was played
			verify(channels[5], times(callCount*2)).noteOn(argument.capture(), eq(NOTE_DURATION));

			// Now assert that the correct interval was played
			int argListLength = argument.getAllValues().size();
			assertEquals("Incorrect interval was played.", currentInterval.getValue(), 
					argument.getAllValues().get(argListLength-1) - argument.getAllValues().get(argListLength-2));
			
			callCount++;
		}		
	}
	
	/**
	 * 1. Play a set Interval multiple times.
	 * 2. Confirm that more than two notes have been used for interval.
	 */
	@Test
	public void testPlayNewInterval() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		Interval theInterval = Interval.Augmented9th;
		Set<Integer> intervals = new HashSet<Integer>();		
		for (int i = 0; i < 100; i++) {
			soundPlayer.playNewInterval(theInterval);
			
			verify(channels[5], times((i+1)*2)).noteOn(argument.capture(), eq(NOTE_DURATION));
			
			List<Integer> argList = argument.getAllValues();
			for (Integer arg : argList) {
				intervals.add(arg);
			}
		}
		
		assertTrue(intervals.size() > 2);
	}
	
	/**
	 * 1. Play every cadence.
	 * 2. Make sure the correct cadences were played.
	 */
	@Test
	public void testPlayCadence() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		
		
		// Loop through cadences
		int cadenceCount = 1;
		int rootIndex = 0;
		for (Cadence cadence: Cadence.values()) {
			// Play the current cadence
			soundPlayer.playCadence(cadence);
			
			// Verify four chords were played and record the root chord
			ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
			verify(channels[5], times(12*cadenceCount)).noteOn(argument.capture(), eq(CADENCE_CHORD_DURATION));
			
			// The first two chords should have been the root
			List<Integer> argList = argument.getAllValues();
			assertEquals(argList.get(0), argList.get(3));
			// They should have also been major chords
			Chord major = Chord.Major;
			int index = rootIndex + 1;
			for (Interval interval: major.getIntervals()) {
				assertEquals("Incorrect root chord type played for cadence.", interval.getValue(), argList.get(index)-argList.get(rootIndex));
				assertEquals("Incorrect root chord type played for cadence.", interval.getValue(), argList.get(index+3)-argList.get(rootIndex+3));
				index++;
			}
			int keyIndex = rootIndex;
			
			// Verify that the correct chords were played
			rootIndex += 6;
			List<Interval> intervals = cadence.getChordIntervals();
			for (int i = 0; i < cadence.getKeyChords().size(); ++i) {
				// Check the correct root chord is being played
				assertEquals("Incorrect key chord played in " + cadence + ".", intervals.get(i).getValue(), argList.get(rootIndex)-argList.get(keyIndex));
				
				// Now make sure the correct chord was played
				index = 1;
				for (Interval interval: cadence.getKeyChords().get(i).getIntervals()) {
					assertEquals("Incorrect chord type was played for cadence.", interval.getValue(), argList.get(rootIndex+index)-argList.get(rootIndex));
					index++;
				}
				
				rootIndex+=3;
			}
			
			cadenceCount++;
			//rootIndex=12*cadenceCount;
		}
	}
	
	/**
	 * 1. Play a given cadence multiple times.
	 * 2. Assert that different notes were used for each cadence.
	 */
	@Test
	public void testNewCadence() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		Cadence theCadence = Cadence.Perfect;
		Set<Integer> intervals = new HashSet<Integer>();
		for (int i = 0; i < 100; ++i) {
			soundPlayer.playNewCadence(theCadence);
			verify(channels[5], times((i+1)*12)).noteOn(argument.capture(), eq(CADENCE_CHORD_DURATION));
			
			for (Integer arg : argument.getAllValues()) {
				intervals.add(arg);
			}
		}

		assertTrue(intervals.size() > 12);
	}
	
	/**
	 * 1. Play every chord.
	 * 2. Assert the correct chord intervals were played.
	 */
	@Test
	public void testPlayChord() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		// Loop through chords
		int callCount = 0;
		for (Chord currentChord: Chord.values()) {
			// Play the current chord
			soundPlayer.playChord(currentChord);
			int numberOfIntervals = currentChord.getIntervals().size();
			
			// Verify all chord intervals were played and record what the chord root was
			callCount += (numberOfIntervals+1);
			verify(channels[5], times(callCount)).noteOn(argument.capture(), eq(CHORD_DURATION));
			
			// Now verify the correct intervals were played
			int argListLength = argument.getAllValues().size();
			int index = numberOfIntervals-1;
			List<Integer> argList = argument.getAllValues();
			for (Interval interval: currentChord.getIntervals()) {				
				assertEquals("Incorrect chord interval was played.", interval.getValue(),
						argList.get((argListLength-1)-index) - argList.get((argListLength-1)-numberOfIntervals));
				index--;
			}
		}
	}

	/**
	 * 1. Play new chord.
	 * 2. Assert that random random root notes have been selected.
	 */
	@Test
	public void testNewChord() {
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		Chord theChord = Chord.Major;
		
		Set<Integer> intervals = new HashSet<Integer>();
		int callCount = theChord.getIntervals().size()+1;
		for (int i = 0; i < 100; ++i) {
			soundPlayer.playNewChord(theChord);
			
			verify(channels[5], times(callCount)).noteOn(argument.capture(), eq(CHORD_DURATION));
			
			List<Integer> argList = argument.getAllValues();
			for (Integer arg : argList) {
				intervals.add(arg);
			}
			
			callCount += theChord.getIntervals().size()+1;
		}
		
		assertTrue(intervals.size() > 3);
	}
	
	/**
	 * 1. Play every interval.
	 * 2. No note should be less than the minimum.
	 * 3. No note should be greater than the maximum.
	 */
	@Test
	public void testIntervalNoteRange() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		// Play every interval x 1000
		for (int i = 0; i < 1000; ++i) {
			for (Interval interval: Interval.values()) {
				soundPlayer.playNewInterval(interval);
			}
		}
		
		verify(channels[5], atLeastOnce()).noteOn(argument.capture(), anyInt());
		// Run through argument list
		for (Integer note: argument.getAllValues()) {
			assertTrue("Note " + note + " is less than " + LOWEST_NOTE, note >= LOWEST_NOTE);
			assertTrue("Note " + note + " is greater than " + HIGHEST_NOTE, note <= HIGHEST_NOTE);
		}
	}
	
	/**
	 * 1. Play every chord.
	 * 2. No note should be less than the minimum.
	 * 3. No note should be greater than the maximum.
	 */
	@Test
	public void testChordNoteRange() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		// Play every chord x 1000
		for (int i = 0; i < 1000; ++i) {
			for (Chord chord: Chord.values()) {
				soundPlayer.playNewChord(chord);
			}
		}
		
		verify(channels[5], atLeastOnce()).noteOn(argument.capture(), anyInt());
		// Run through argument list
		for (Integer note: argument.getAllValues()) {
			assertTrue("Note " + note + " is less than " + LOWEST_NOTE, note >= LOWEST_NOTE);
			assertTrue("Note " + note + " is greater than " + HIGHEST_NOTE, note <= HIGHEST_NOTE);
		}
	}
	/**
	 * 1. Play every cadence.
	 * 2. No note should be less than the minimum.
	 * 3. No note should be greater than the maximum.
	 */
	@Test
	public void testCadenceNoteRange() {
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, soundSettings);
		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		
		// Play every cadence x 1000
		for (int i = 0; i < 1000; ++i) {
			for (Cadence cadence: Cadence.values()) {
				soundPlayer.playNewCadence(cadence);
			}
		}
		
		verify(channels[5], atLeastOnce()).noteOn(argument.capture(), anyInt());
		// Run through argument list
		for (Integer note: argument.getAllValues()) {
			assertTrue("Note " + note + " is less than " + LOWEST_NOTE, note >= LOWEST_NOTE);
			assertTrue("Note " + note + " is greater than " + HIGHEST_NOTE, note <= HIGHEST_NOTE);
		}
	}
	
	/**
	 * 1. Call update interval settings.
	 * 2. Verify that interval settings were read.
	 */
	@Test
	public void testUpdateIntervalSettings() {
		Setting setting = mock(Setting.class);
		ControlSetting<?> intervalSetting = mock(ControlSetting.class);
		@SuppressWarnings("unchecked")
		ControlSetting<Integer> controlSetting = mock(ControlSetting.class);
		when(controlSetting.getValue()).thenReturn(1);
		when(intervalSetting.findSetting(anyString())).thenReturn(controlSetting);
		when(setting.findSetting("interval")).thenReturn(intervalSetting);
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, setting);
		
		soundPlayer.updateIntervalSettings();
		
		verify(intervalSetting, atLeastOnce()).findSetting("duration");
		verify(intervalSetting, atLeastOnce()).findSetting("pause");
	}
	
	/**
	 * 1. Call update chord settings.
	 * 2. Verify that chord settings were read.
	 */
	@Test
	public void testUpdateChordSettings() {
		Setting setting = mock(Setting.class);
		ControlSetting<?> chordSetting = mock(ControlSetting.class);
		@SuppressWarnings("unchecked")
		ControlSetting<Integer> controlSetting = mock(ControlSetting.class);
		when(controlSetting.getValue()).thenReturn(1);
		when(chordSetting.findSetting(anyString())).thenReturn(controlSetting);
		when(setting.findSetting("chord")).thenReturn(chordSetting);
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, setting);
		
		soundPlayer.updateIntervalSettings();
		
		verify(chordSetting, atLeastOnce()).findSetting("duration");
		verify(chordSetting, atLeastOnce()).findSetting("note_pause");
	}
	
	/**
	 * 1. Call update cadence settings.
	 * 2. Verify that cadence settings were read.
	 */
	@Test
	public void testUpdateCadenceSettings() {
		Setting setting = mock(Setting.class);
		ControlSetting<?> cadenceSetting = mock(ControlSetting.class);
		@SuppressWarnings("unchecked")
		ControlSetting<Integer> controlSetting = mock(ControlSetting.class);
		when(controlSetting.getValue()).thenReturn(1);
		when(cadenceSetting.findSetting(anyString())).thenReturn(controlSetting);
		when(setting.findSetting("cadence")).thenReturn(cadenceSetting);
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, setting);
		
		soundPlayer.updateIntervalSettings();
		
		verify(cadenceSetting, atLeastOnce()).findSetting("duration");
		verify(cadenceSetting, atLeastOnce()).findSetting("pause");
	}
	
	@Test
	public void testUpdateGeneralSettings() {
		Setting setting = mock(Setting.class);
		ControlSetting<?> cadenceSetting = mock(ControlSetting.class);
		@SuppressWarnings("unchecked")
		ControlSetting<Integer> controlSetting = mock(ControlSetting.class);
		when(controlSetting.getValue()).thenReturn(1);
		when(cadenceSetting.findSetting(anyString())).thenReturn(controlSetting);
		when(setting.findSetting("general")).thenReturn(cadenceSetting);
		SoundPlayer soundPlayer = new MidiPlayer(synthesizer, setting);
		
		soundPlayer.updateGeneralSettings();
		
		//verify(cadenceSetting, atLeastOnce()).findSetting("instrument");
		verify(cadenceSetting, atLeastOnce()).findSetting("lowest_note");
		verify(cadenceSetting, atLeastOnce()).findSetting("highest_note");
	}
}
