package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import sounds.SoundDemo;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer,AlienShipBulletDestroyer,AlienShipDestroyer
{
    /** The outline of the ship */
    private Shape outline;
    private Shape outline1;
    private Path2D.Double poly;
    private Path2D.Double poly1;
    /** Game controller */
    private Controller controller;
    SoundDemo demo;
    private boolean ShootBulletBoolean;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);
        demo = new SoundDemo();
        this.poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;

        this.poly1 = new Path2D.Double();
        poly1.moveTo(21, 0);
        poly1.lineTo(-21, 12);
        poly1.lineTo(-14, 10);
        poly1.lineTo(-14, -10);
        poly1.lineTo(-21, -12);
        poly1.closePath();
        poly1.moveTo(-14, -6);
        poly1.lineTo(-25, 0);
        poly1.lineTo(-14, 6);
        outline1 = poly1;

        // Schedule an acceleration in two seconds
        // new ParticipantCountdownTimer(this, "move", 2000);
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        if (controller.getAcc() == true)
        {
            if (System.currentTimeMillis() % 2 == 0)
            {
                return outline;
            }
            else
            {
                return outline1;
            }
        }
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
            this.controller.addParticipant(new Debris(this.getX(), this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(new Debris(this.getX(), this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris2(this.getX() - Math.random() * 15, this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris3(this.getX() + Math.random() * 15, this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris4(this.getX(), this.getY() + Math.random() * 15, Math.random() * 15, controller));

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("move"))
        {
            accelerate();
            new ParticipantCountdownTimer(this, "move", 200);
        }
    }

    public void setShootBullet (boolean b)// setter
    {
        this.ShootBulletBoolean = b;
    }

    public void fire ()
    {
        this.controller.addParticipant(new Bullet(this.getXNose(), this.getYNose(), this.getRotation(), controller));
        demo.playfirebullet();

    }

}
