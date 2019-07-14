package sounds;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Demonstrates how to put sound files into a project so that they will be included when the project is exported, and
 * demonstrates how to play sounds.
 * 
 * @author Joe Zachary
 */
@SuppressWarnings("serial")
public class SoundDemo extends JFrame implements ActionListener
{
    /**
     * Launches a simple sound demo application
     */
    public static void main (String[] args)
    {
        SoundDemo demo = new SoundDemo();
        demo.setVisible(true);
    }

    /** A Clip that, when played, sounds like a weapon being fired */
    private Clip fireClip;

    /** A Clip that, when played repeatedly, sounds like a small saucer flying */
    private Clip smallSaucerClip;
    private Clip bangShipClip;
    private Clip bangLargeClip;
    private Clip bangMediumClip;
    private Clip bangSmallClip;
    private Clip bangAlienShipClip;
    private Clip thrustClip;
    private Clip saucerBig;
    private Clip saucerSmall;

    /**
     * Creates the demo.
     */
    public SoundDemo ()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        JPanel fire = new JPanel();
        JButton fireButton = new JButton("Fire");
        fireButton.addActionListener(this);
        fireButton.setActionCommand("fire");
        fire.add(fireButton);
        buttons.add(fire);

        JPanel saucer1 = new JPanel();
        JButton saucerButton1 = new JButton("Small Saucer");
        saucerButton1.setActionCommand("small saucer");
        saucerButton1.addActionListener(this);
        saucer1.add(saucerButton1);
        buttons.add(saucer1);

        JPanel saucer2 = new JPanel();
        JButton saucerButton2 = new JButton("Big Saucer");
        saucerButton2.setActionCommand("big saucer");
        saucerButton2.addActionListener(this);
        saucer2.add(saucerButton2);
        buttons.add(saucer2);

        JPanel bangship = new JPanel();
        JButton bangshipButton = new JButton("Bang Ship");
        bangshipButton.setActionCommand("bang ship");
        bangshipButton.addActionListener(this);
        bangship.add(bangshipButton);
        buttons.add(bangship);

        JPanel thrust = new JPanel();
        JButton thrustButton = new JButton("Thrust");
        thrustButton.setActionCommand("thrust");
        thrustButton.addActionListener(this);
        thrust.add(thrustButton);
        buttons.add(thrust);

        JPanel banglarge = new JPanel();
        JButton banglargeButton = new JButton("Bang Large");
        banglargeButton.setActionCommand("bang large");
        banglargeButton.addActionListener(this);
        banglarge.add(banglargeButton);
        buttons.add(banglarge);

        JPanel bangmedium = new JPanel();
        JButton bangmediumButton = new JButton("Bang Medium");
        bangmediumButton.setActionCommand("bang medium");
        bangmediumButton.addActionListener(this);
        bangmedium.add(bangmediumButton);
        buttons.add(bangmedium);

        JPanel bangsmall = new JPanel();
        JButton bangsmallButton = new JButton("Bang Small");
        bangsmallButton.setActionCommand("bang medium");
        bangsmallButton.addActionListener(this);
        bangsmall.add(bangsmallButton);
        buttons.add(bangsmall);

        JPanel bangalienship = new JPanel();
        JButton bangalienshipButton = new JButton("Bang Alien Ship");
        bangalienshipButton.setActionCommand("bang alien ship");
        bangalienshipButton.addActionListener(this);
        bangalienship.add(bangalienshipButton);
        buttons.add(bangalienship);

        setContentPane(buttons);
        pack();

        // We create the clips in advance so that there will be no delay
        // when we need to play them back. Note that the actual wav
        // files are stored in the "sounds" project.
        fireClip = createClip("/sounds/fire.wav");
        bangShipClip = createClip("/sounds/bangShip.wav");
        smallSaucerClip = createClip("/sounds/saucerSmall.wav");
        thrustClip = createClip("/sounds/thrust.wav");
        bangLargeClip = createClip("/sounds/bangLarge.wav");
        bangMediumClip = createClip("/sounds/bangMedium.wav");
        bangSmallClip = createClip("/sounds/bangSmall.wav");
        bangAlienShipClip = createClip("/sounds/bangAlienShip.wav");
        saucerBig = createClip("/sounds/saucerBig.wav");
        saucerSmall = createClip("/sounds/saucerSmall.wav");

    }

    // public Clip FireClip() {
    // return fireClip;
    //
    // }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }

    public void playfirebullet ()
    {
        fireClip.setFramePosition(0); // Must always rewind!
        fireClip.loop(0);
        fireClip.start();
    }

    public void playbangship ()
    {
        bangShipClip.setFramePosition(0); // Must always rewind!
        bangShipClip.loop(0);
        bangShipClip.start();
    }

    public void playthrust ()
    {
        if (!thrustClip.isRunning())
        {
            thrustClip.setFramePosition(0);
            thrustClip.start();
        }
    }

    public void playbanglarge ()
    {
        bangLargeClip.setFramePosition(0); // Must always rewind!
        bangLargeClip.loop(0);
        bangLargeClip.start();
    }

    public void playbangmedium ()
    {
        bangMediumClip.setFramePosition(0); // Must always rewind!
        bangMediumClip.loop(0);
        bangMediumClip.start();
    }

    public void playbangsmall ()
    {
        bangSmallClip.setFramePosition(0); // Must always rewind!
        bangSmallClip.loop(0);
        bangSmallClip.start();
    }

    public void playbangalienship ()
    {
        bangAlienShipClip.setFramePosition(0); // Must always rewind!
        bangAlienShipClip.loop(0);
        bangAlienShipClip.start();
    }

    public void saucerBig ()
    {
        saucerBig.setFramePosition(0); // Must always rewind!
        saucerBig.loop(99999999);
        saucerBig.start();
    }

    public void saucerSmall ()
    {
        saucerSmall.setFramePosition(0); // Must always rewind!
        saucerSmall.loop(99999999);
        saucerSmall.start();
    }

    /**
     * Plays sounds depending on which button was clicked.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // We "rewind" the fireClip and play it.
        if (e.getActionCommand().equals("fire") && fireClip != null)
        {
            if (fireClip.isRunning())
            {
                fireClip.stop();
            }
            fireClip.setFramePosition(0);
            fireClip.start();
        }

        // We "rewind" the smallSaucerClip and play it ten times in a row. To loop
        // continuously, pass Clip.LOOP_CONTINUOUSLY as the parameter.
        else if (e.getActionCommand().equals("small saucer") && saucerSmall != null)
        {
            if (saucerSmall.isRunning())
            {
                saucerSmall.stop();
            }
            saucerSmall.setFramePosition(0);
            saucerSmall.loop(10);
        }
        else if (e.getActionCommand().equals("big saucer") && saucerBig != null)
        {
            if (saucerBig.isRunning())
            {
                saucerBig.stop();
            }
            saucerBig.setFramePosition(0);
            saucerBig.loop(10);
        }
        else if (e.getActionCommand().equals("bang ship") && bangShipClip != null)
        {
            if (bangShipClip.isRunning())
            {
                bangShipClip.stop();
            }
            bangShipClip.setFramePosition(0);
            bangShipClip.loop(0);
        }

        else if (e.getActionCommand().equals("thrust") && thrustClip != null)
        {
            if (thrustClip.isRunning())
            {
                thrustClip.stop();
            }
            thrustClip.setFramePosition(0);
            thrustClip.loop(0);
        }

        else if (e.getActionCommand().equals("bang large") && bangLargeClip != null)
        {
            if (bangLargeClip.isRunning())
            {
                bangLargeClip.stop();
            }
            bangLargeClip.setFramePosition(0);
            bangLargeClip.loop(0);
        }

        else if (e.getActionCommand().equals("bang medium") && bangMediumClip != null)
        {
            if (bangMediumClip.isRunning())
            {
                bangMediumClip.stop();
            }
            bangMediumClip.setFramePosition(0);
            bangMediumClip.loop(0);
        }

        else if (e.getActionCommand().equals("bang small") && bangSmallClip != null)
        {
            if (bangSmallClip.isRunning())
            {
                bangSmallClip.stop();
            }
            bangSmallClip.setFramePosition(0);
            bangSmallClip.loop(0);
        }

        else if (e.getActionCommand().equals("bang alien ship") && bangAlienShipClip != null)
        {
            if (bangAlienShipClip.isRunning())
            {
                bangAlienShipClip.stop();
            }
            bangAlienShipClip.setFramePosition(0);
            bangAlienShipClip.loop(0);
        }
    }
}
