package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;

import asteroids.destroyers.AlienShipBulletDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;


public class AlienShipBullet extends Participant implements AsteroidDestroyer,ShipDestroyer
{

    private Shape outline;
    private Controller controller;
    public AlienShipBullet (double x, double y, double direction, Controller controller)
    {
        Path2D.Double poly = new Path2D.Double();
        this.controller = controller;
        setPosition(x, y);
        setVelocity(BULLET_SPEED, direction);
        poly.moveTo(1, 1);
        poly.lineTo(-1, 1);
        poly.lineTo(-1, -1);
        poly.lineTo(1, -1);
        poly.closePath();
        outline = poly;
        new ParticipantCountdownTimer(this, "",BULLET_DURATION);	
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
        if (p instanceof AlienShipBulletDestroyer)
        {
            Participant.expire(this);
            controller.alienbulletDestroyed();            
        }
    }
}
