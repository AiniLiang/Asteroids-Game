package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AlienShipDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents asteroids
 */
public class Bullet extends Participant implements AsteroidDestroyer,BulletDestroyer,AlienShipDestroyer
{
    /** The outline of the asteroid */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Throws an IllegalArgumentException if size or variety is out of range.
     * 
     * Creates an asteroid of the specified variety (0 through 3) and size (0 = small, 1 = medium, 2 = large) and
     * positions it at the provided coordinates with a random rotation. Its velocity has the given speed but is in a
     * random direction.
     */
    public Bullet (double x, double y, double direction, Controller controller)
    {
        // Create the bullet
        this.controller = controller;
        setPosition(x, y);
        setVelocity(BULLET_SPEED, direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(1, 1);
        poly.lineTo(-1, 1);
        poly.lineTo(-1, -1);
        poly.lineTo(1, -1);
        poly.closePath();

        new ParticipantCountdownTimer(this, 1000);

        // double scale = ASTEROID_SCALE[size];
        // poly.transform(AffineTransform.getScaleInstance(scale, scale));
        outline = poly;
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * When an Asteroid collides with an AsteroidDestroyer, it expires.
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof BulletDestroyer)
        {
            Participant.expire(this);
            controller.BulletDestroyed();
            
        }
    }

    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }
}
