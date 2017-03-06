package controller.cadences;

import controller.sound.Cadence;

/**
 * Interface to define the behaviour of Cadence generator.
 * 
 * @author Robertio
 *
 */
public interface CadenceGenerator {
	public Cadence getNextCadence();
}
