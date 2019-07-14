package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents asteroids
 */
public class Debris4 extends Participant implements AsteroidDestroyer, ShipDestroyer
{

    private Shape outline;

    private Controller controller;

    public Debris4 (double x, double y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setVelocity(0.5, direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(1, 25);
        poly.lineTo(-23, 20);

        new ParticipantCountdownTimer(this, 2000);

        outline = poly;
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }
}
