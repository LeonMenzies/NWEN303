package model;

import java.awt.Color;
import java.awt.Graphics2D;

public record DrawableParticle (int x, int y,int r, Color c) {
  public void draw(Graphics2D g){
    g.setColor(c);
    g.drawOval(x-r, y-r, r*2, r*2);
  }
  public static DrawableParticle of(Particle p){
    int mass = (int)Math.sqrt(p.mass);
    return new DrawableParticle((int)p.x, (int)p.y, mass ,Color.ORANGE);
  }
}
