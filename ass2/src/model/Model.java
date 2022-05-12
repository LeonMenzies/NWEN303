package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Model{
	public static final double size = 900;// just for the canvas
	public static final double gravitationalConstant = 0.001;
	public static final double lightSpeed = 10;// the smaller, the larger is the chunk of universe we simulate
	public static final double timeFrame = 0.1;// the bigger, the shorter is the time of a step, the slower but more
												// precise the simulation
	public List<Particle> p = new ArrayList<>();
	public volatile List<DrawableParticle> pDraw = new ArrayList<>();

	public void step() {
		for (var p : this.p) {
			p.interact(this);
		}
		for (var p : this.p) {
			p.move();
		}
		mergeParticles();
		updateGraphicalRepresentation();
	}

	protected void updateGraphicalRepresentation() {
		var d = new ArrayList<DrawableParticle>();
		for (var p : this.p) {
			d.add(DrawableParticle.of(p));
		}
		this.pDraw = d;// atomic update
	}

	public void mergeParticles() {
		var deadPs = new Stack<Particle>();
		for (var p : this.p) {
			if (!p.impacting.isEmpty()) {
				deadPs.add(p);
			}
		}
		this.p.removeAll(deadPs);
		while (!deadPs.isEmpty()) {
			Particle current = deadPs.pop();
			Set<Particle> ps = getSingleChunck(current);
			deadPs.removeAll(ps);
			this.p.add(mergeParticles(ps));
		}
	}

	protected Set<Particle> getSingleChunck(Particle current) {
		Set<Particle> impacting = new HashSet<>();
		impacting.add(current);
		while (true) {
			Set<Particle> tmp = new HashSet<>();
			for (Particle pi : impacting) {
				tmp.addAll(pi.impacting);
			}
			boolean changed = impacting.addAll(tmp);
			if (!changed) {
				break;
			}
		}
		// now impacting have all the chunk of collapsing particles
		return impacting;
	}

	private Particle mergeParticles(Set<Particle> ps) {
		double speedX = 0;
		double speedY = 0;
		double x = 0;
		double y = 0;
		double mass = 0;
		for (Particle p : ps) {
			mass += p.mass;
			x += p.x * p.mass;
			y += p.y * p.mass;
			speedX += p.speedX * p.mass;
			speedY += p.speedY * p.mass;
		}
		x /= mass;
		y /= mass;
		speedX /= mass;
		speedY /= mass;
		return new Particle(mass, speedX, speedY, x, y);
	}
}