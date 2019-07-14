package asteroids.game;

import javax.swing.*;
import static asteroids.game.Constants.*;
import java.awt.*;

/**
 * Defines the top-level appearance of an Asteroids game.
 */
@SuppressWarnings("serial")
public class Display extends JFrame
{
    /** The area where the action takes place */
    private Screen screen;

    /**
     * Lays out the game and creates the controller
     */
    public Display (Controller controller)
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.screen = new Screen(controller);
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(this.screen);
        JPanel controls = new JPanel();
        controls.setLayout(new GridBagLayout());
        JPanel left = new JPanel();
        controls.add(left);
        JButton startGame = new JButton(START_LABEL);
        controls.add(startGame);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add((Component) screenPanel, "Center");
        mainPanel.add((Component) controls, "North");
        setContentPane(mainPanel);
        pack();
        startGame.addActionListener(controller);
    }

    /**
     * Called when it is time to update the screen display. This is what drives the animation.
     */
    public void refresh ()
    {
        screen.repaint();
    }

    public void setLevel ()
    {
        this.screen.setLevel();
    }

    public void setLives ()
    {
        this.screen.setLives();

    }

    public void setScore ()
    {
        this.screen.setScore();
    }

    /**
     * Sets the large legend
     */
    public void setLegend (String s)
    {
        screen.setLegend(s);
    }
}
