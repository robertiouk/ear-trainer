package controller;

/**
 * Interface to define generic controller behaviour.
 * 
 * @author Robertio
 *
 */
public interface Controller {
	public boolean submit(Enum<?> sound);
	public void play();
	public void updateSoundSettings();
}
