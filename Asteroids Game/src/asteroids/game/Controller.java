package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.AlienShipBullet;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;
import asteroids.participants.Life;
import sounds.SoundDemo;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{
    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;
    private AlienShip alienship;
    SoundDemo demo;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;
    private Bullet bullet;
    private AlienShipBullet alienbullet;
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    /** The game display */
    private Display display;
    private boolean shipDestroyedBoolean;
    private boolean nextScreenBoolean;
    private boolean turnRight;
    private boolean turnLeft;
    private boolean acc;
    private int levels;
    private int score;
    private Life ll;
    private Life l2;
    private Life l3;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        demo = new SoundDemo();
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    public AlienShip getalienShip ()
    {
        return alienship;
    }

    public Bullet getBullet ()
    {
        return bullet;
    }

    public AlienShipBullet getAlienShipBullet ()
    {
        return alienbullet;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();

    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
    }

    private void placeAlienShip ()
    {
        Participant.expire(alienship);
        alienship = new AlienShip(Math.PI / 8, this);
        new ParticipantCountdownTimer(alienship, "move", ALIEN_DELAY);
        addParticipant(alienship);
        scheduleTransition(1000);
        if (alienship.getSize() == 1)
        {
            demo.saucerBig();
        }

        if (alienship.getSize() == 0)
        {
            demo.saucerSmall();
        }
    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids ()
    {
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, (int) Math.random() + 2, this));
        addParticipant(new Asteroid(0, 2, -EDGE_OFFSET, EDGE_OFFSET + 2, (int) Math.random() + 2, this));
        addParticipant(new Asteroid(0, 2, 2 - EDGE_OFFSET, -EDGE_OFFSET - 3, (int) Math.random() + 2, this));
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, -EDGE_OFFSET, (int) Math.random() + 2, this));
        int j = 4 - levels;
        Random rd = new Random();
        while (j < 3)
        {
            int i = rd.nextInt(298);
            addParticipant(new Asteroid(3, 2, i - 150, i, 3, this));
            j++;
        }

    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
        lives = 3;
        score = 0;
        levels = 1;
        display.setScore();
        display.setLevel();
        display.setLives();
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Plac asteroids
        placeAsteroids();

        // Place the ship
        placeShip();
        placeLife();
        lives = 3;
        levels = 1;
        score = 0;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    private void placeLife ()
    {
        Participant.expire(ll);
        Participant.expire(l2);
        Participant.expire(l3);
        if (lives == 1)
        {
            ll = (new Life(60, 90, -Math.PI / 2, this));

        }
        if (lives == 2)
        {
            ll = (new Life(45, 90, -Math.PI / 2, this));
            l2 = (new Life(75, 90, -Math.PI / 2, this));

        }
        if (lives == 3)
        {
            ll = (new Life(30, 90, -Math.PI / 2, this));
            l2 = (new Life(60, 90, -Math.PI / 2, this));
            l3 = (new Life(90, 90, -Math.PI / 2, this));
        }

        addParticipant(ll);
        addParticipant(l2);
        addParticipant(l3);
    }

    private void nextScreen ()
    {
        // Clear the screen
        levels++;

        if (levels >= 2)
        {
            placeAlienShip();
        }
        display.setLevel();

        placeAsteroids();
        placeShip();

        display.removeKeyListener(this);
        display.addKeyListener(this);
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        lives--;
        display.setLives();
        placeLife();
        shipDestroyedBoolean = true;
        if (lives <= 0)
        {
            // shipLeftBoolean = false;
            ship = null;
            display.setLegend(GAME_OVER);
        }
        else
        {
            ship = null;
            scheduleTransition(END_DELAY);
        }
    }

    public void alienShipDestroyed (int size)
    {

        this.score = score + ALIENSHIP_SCORE[size];
        display.setScore();
        alienship = null;
        scheduleTransition(ALIEN_DELAY);
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (int size)
    {
        this.score = this.score + ASTEROID_SCORE[size];

        display.setScore();
        if (pstate.countAsteroids() <= 0 && size == 0)
        {
            scheduleTransition(END_DELAY);
            nextScreenBoolean = true;
        }
    }

    public void BulletDestroyed ()
    {
        bullet = null;
    }

    public void alienbulletDestroyed ()
    {
        alienbullet = null;
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            if (turnRight && ship != null)
            {
                ship.turnRight();
            }

            if (turnLeft && ship != null)
            {
                ship.turnLeft();
            }

            if (acc && ship != null)
            {
                ship.accelerate();
            }

            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
        }
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {

        if (transitionTime <= System.currentTimeMillis())
        {
            transitionTime = Long.MAX_VALUE;
            if (lives <= 0)
            {
                display.setScore();
                finalScreen();
            }
            if (lives > 0 && ship == null)
            {
                placeShip();
            }

            if (alienship != null)
            {
                alienship.getchangeDirectionBoolean(true);
                scheduleTransition(1000);
            }
            
            if (nextScreenBoolean == true)
            {
                nextScreenBoolean = false;
                nextScreen();
            }
            
            if (alienship == null && ship != null && levels >= 2)
            {
                placeAlienShip();
                scheduleTransition(ALIEN_DELAY);
            }
            
            if (alienship == null && ship != null && shipDestroyedBoolean == true && levels >= 2)
            {
                placeShip();
                scheduleTransition(ALIEN_DELAY);
            }
        }
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            turnRight = true;
            // ship.turnRight();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            turnLeft = true;
            // ship.turnLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            demo.playthrust();
            acc = true;
            // ship.accelerate();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null && pstate.countBullet() < BULLET_LIMIT)
        {
            ship.setShootBullet(true);
            ship.fire();
        }
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            turnRight = false;
            // ship.turnRight();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            turnLeft = false;
            // ship.turnLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            acc = false;
            // ship.accelerate();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            ship.setShootBullet(false);
        }
    }

    public boolean getAcc ()
    {
        return this.acc;
    }

    public int getlevel ()
    {
        return levels;
    }

    public int getScore ()
    {
        return this.score;
    }

    public int getLives ()
    {
        return this.lives;
    }
}
