package algorithm;
import javax.media.j3d.*;

/*
 * Simulation is an interface to make the construction
 * of a simulation abstract for future use with other
 * graphics algorithms implemented using Java3D.
 */

interface Simulation {
	public void constructSim(Canvas3D canvas);
}
