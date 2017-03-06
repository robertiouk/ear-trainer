package controller.cadences;

import java.util.List;

import controller.sound.Cadence;

/**
 * Interface to define the behaviour of Cadence container.
 * 
 * @author Robertio
 *
 */
public interface CadenceContainer {
	public void setCadences(List<Cadence> cadences);
	public List<Cadence> getCadences();
}
