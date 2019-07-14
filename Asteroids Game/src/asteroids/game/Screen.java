package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import java.util.Iterator;
import javax.swing.*;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    private int level;
    private int score;
    private int life;
    /** Legend that is displayed across the screen */
    private String legend;

    /** Game controller */
    private Controller controller;

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        level = controller.getlevel();
        score = controller.getScore();
        life = controller.getLives();
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    public void setLevel() {
        this.level = this.controller.getlevel();
    }

    public void setScore() {
        this.score = this.controller.getScore();
    }

    public void setLives() {
        this.life = this.controller.getLives();
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g);
        Iterator<Participant> iter = controller.getParticipants();
        while (iter.hasNext()) {
            iter.next().draw(g);
        }
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString(Integer.toString(this.score), 30, 50);
        g.drawString(Integer.toString(this.level), 700, 50);

    }
}
