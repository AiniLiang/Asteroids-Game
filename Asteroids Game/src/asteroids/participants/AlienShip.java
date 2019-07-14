package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.Random;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import sounds.SoundDemo;

public class AlienShip extends Participant implements AsteroidDestroyer, BulletDestroyer, ShipDestroyer
{
    private Shape outline;
    private Controller controller;
    private Path2D.Double poly1;
    SoundDemo demo;
    private int counter;
    private int size = 0;
    private boolean changeDirection = true;

    public AlienShip (double deriction, Controller controller)
    {
        demo = new SoundDemo();
        this.controller = controller;

        if (controller.getlevel() == 2)
        {
            size = 1;
        }
        else
        {
            size = 0;
        }

        setPosition(0, RANDOM.nextDouble() * SIZE);

        this.setVelocity(ALIENSHIP_SPEED[size], RANDOM.nextInt(2) == 0 ? 0 : Math.PI);

        this.poly1 = new Path2D.Double();
        poly1.moveTo(20, 0);
        poly1.lineTo(10, 8);
        poly1.lineTo(-10, 8);
        poly1.lineTo(-20, 0);
        poly1.lineTo(20, 0);
        poly1.lineTo(-20, 0);
        poly1.lineTo(-10, -8);
        poly1.lineTo(10, -8);
        poly1.lineTo(-8, -8);
        poly1.lineTo(-6, -15);
        poly1.lineTo(6, -15);
        poly1.lineTo(8, -8);
        poly1.lineTo(10, -8);
        poly1.closePath();
        double scale = ASTEROID_SCALE[size];
        poly1.transform(AffineTransform.getScaleInstance(scale, scale));
        outline = poly1;

    }

    public int getSize ()
    {
        return size;
    }

    @Override
    public void move ()
    {
        super.move();
        counter++;
        shoot();
        if (this.changeDirection)
        {
            this.changeDirection = false;
            if (Math.cos(this.getDirection()) > 0)
            {
                this.setDirection(RANDOM.nextInt(3) - 1);
            }
            else
            {
                this.setDirection(Math.PI + (double) RANDOM.nextInt(3) - 1);
            }
            new ParticipantCountdownTimer(this, "change", 1000);
        }
    }

    public void getchangeDirectionBoolean (boolean b)
    {
        changeDirection = b;
    }

    public void shoot ()
    {
        if (counter == 30)
        {
            launch();
            counter = 0;
        }

    }

    public void launch ()
    {
        Random r = new Random();
        if (controller.getlevel() == 2)
        {
            controller.addParticipant(new AlienShipBullet(getXNose(), getYNose(), r.nextInt(10), controller));
            demo.playfirebullet();
        }

        if (controller.getShip() != null && size == 0)
        {
            Ship ship = controller.getShip();
            double min = Math.PI / 2 + Math.atan2(-(ship.getX() - this.getX()), (ship.getY() - this.getY()))
                    - Math.PI / 18;
            double max = Math.PI / 2 + Math.atan2(-(ship.getX() - this.getX()), (ship.getY() - this.getY()))
                    + Math.PI / 18;
            double direction = min + (max - min) * RANDOM.nextDouble();
            AlienShipBullet b = new AlienShipBullet(getXNose(), getYNose(), direction, controller);
            controller.addParticipant(b);
            new ParticipantCountdownTimer(b, "disappear", BULLET_DURATION);
            demo.playfirebullet();
        }

    }

    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof AlienShipDestroyer)
        {
            demo.playbangalienship();
            Participant.expire(this);
            this.controller.addParticipant(new Debris(this.getX(), this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(new Debris(this.getX(), this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris2(this.getX() - Math.random() * 15, this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris3(this.getX() + Math.random() * 15, this.getY(), Math.random() * 15, controller));
            this.controller.addParticipant(
                    new Debris4(this.getX(), this.getY() + Math.random() * 15, Math.random() * 15, controller));
            controller.alienShipDestroyed(size);
        }
    }

    @Override
    public void countdownComplete (Object payload)
    {
        if (this.isExpired())
        {
            return;
        }
        new ParticipantCountdownTimer(this, "", BULLET_DURATION);
    }
}
