import javax.swing.*;
import java.awt.*;

public class RenderingClass extends JPanel
{
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
    }
    
}
