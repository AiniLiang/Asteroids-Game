package asteroids.participants;
import java.awt.Shape;
import java.awt.geom.*;



import asteroids.game.Controller;
import asteroids.game.Participant;
public class Life extends Participant{
	private Controller controller;
	private Shape outline;
	private int bullet;
	private Path2D.Double poly;
	public int getBulletNum() {
		return bullet;
	}
	public Life(int x, int y, double direction, Controller controller) {
		this.controller = controller;
		setPosition(x, y);
		setRotation(direction);
		this.poly = new Path2D.Double();
		poly.moveTo(21, 0);
		poly.lineTo(-21, 12);
		poly.lineTo(-14, 10);
		poly.lineTo(-14, -10);
		poly.lineTo(-21, -12);
		poly.closePath();
		outline = poly;
	}

	@Override
	protected Shape getOutline() {
		// TODO Auto-generated method stub
		return outline;
	}

	@Override
	public void collidedWith(Participant p) {
		// TODO Auto-generated method stub
		
	}

	
}

		

