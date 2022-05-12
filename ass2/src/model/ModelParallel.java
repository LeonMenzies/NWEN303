package model;

/**
 * A parallel version of the Model class
 * @author leonmenzies
 *
 */
public class ModelParallel extends Model{
	

	@Override
	public void step() {
			
		//User parallel streams to iterate each particle update step
		p.parallelStream().forEach(p -> p.interact(this));

		//Keeping the original for each loops as this step is only O(n)
		for (var p : this.p) {
			p.move();
		}
		
		mergeParticles();
		updateGraphicalRepresentation();
	}
}
