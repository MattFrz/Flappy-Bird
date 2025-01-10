import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class FlappyBird implements ActionListener,MouseListener
{
    public static FlappyBird flappyBird;
    public int WIDTH=1200;
    public int HEIGHT=800;
    public int ticks, ymotion, score;
    public boolean gameOver,started,win;
    public RenderingClass render;
    public Rectangle bird;
    public ArrayList<Rectangle>pipes;
    public Random rand= new Random();;
    
    public FlappyBird()
    {
        JFrame jframe=new JFrame();
        Timer timer = new Timer(20,this);
        
        render=new RenderingClass();
        
        jframe.add(render);
        jframe.setTitle("Flappy Bird");
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setVisible(true);
        int BirdWidth=WIDTH/2-10;
        int BirdHeight=HEIGHT/2-10;
        bird=new Rectangle(BirdWidth,BirdHeight,40,20);
        pipes=new ArrayList<Rectangle>();
        for(int i=0;i<100;i++)
        {
            addPipe(true);
        }
        gameOver=false;
        
        timer.start();
    }
    public void actionPerformed(ActionEvent e)
    {
        int speed=5;
        ticks++;
        if(started)
        {
            for(int i=0;i<pipes.size();i++)
            {
                Rectangle pipe=pipes.get(i);
                pipe.x=pipe.x-speed;
            }
            
            if(ticks%2==0 && ymotion<15)
            {
                ymotion=ymotion+2;
            }
            //sets speed
            for(int i=0;i<pipes.size();i++)
            {
                Rectangle pipe=pipes.get(i);
                pipe.x=pipe.x-speed;
            }
            bird.y=bird.y+ymotion;
            //if bird collides pipes
            for(Rectangle pipe:pipes)
            {
                if(pipe.y==0 && bird.x+20 >= pipe.x+90 && bird.x+20 <= pipe.x+90)
                {
                    score++;
                }
                if(pipe.intersects(bird))
                {
                    gameOver=true;
                    bird.x=pipe.x-bird.width;
                }
            }
            //If bird collides with the ground
            if(bird.y>630||bird.y<0)
            {
                gameOver=true;
            }
            //drops bird
            if(bird.y+ymotion>=HEIGHT-150)
            {
                bird.y=HEIGHT-150-bird.height;
            }
            if(score==100)
            {
                win=true;
            }
        }
        render.repaint();
    }
    public void addPipe(boolean start)
    {
        int space=300;
        int width=100;
        int height=50+rand.nextInt(300-0)+0;
        if(start)
        {
            pipes.add(new Rectangle(WIDTH+width+pipes.size()*300, HEIGHT-height-150, width,height));
            pipes.add(new Rectangle(WIDTH+width+(pipes.size()-1)*300, 0, width, HEIGHT-height-space));
        }
        else
        {
            pipes.add(new Rectangle(pipes.get(pipes.size()-1).x+600, HEIGHT-height-150, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size()-1).x+600, 0, width, HEIGHT-height-space));
        }
    }
    public void paintpipe(Graphics g, Rectangle pipe)
    {
        g.setColor(Color.green.darker());
        g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        g.setColor(Color.black);
        g.drawRect(pipe.x, pipe.y, pipe.width, pipe.height);
    }
    public void jump()
    {
        if(gameOver)
        {
            int BirdWidth=WIDTH/2-10;
            int BirdHeight=HEIGHT/2-10;
            bird=new Rectangle(BirdWidth,BirdHeight,40,20);
            pipes.clear();
            ymotion=0;
            score=0;
            gameOver=false;
            for(int i=0;i<100;i++)
            {
                addPipe(true);
            }
        }
        if(win)
        {
            int BirdWidth=WIDTH/2-10;
            int BirdHeight=HEIGHT/2-10;
            bird=new Rectangle(BirdWidth,BirdHeight,40,20);
            pipes.clear();
            ymotion=0;
            score=0;
            gameOver=false;
            win=false;
            for(int i=0;i<100;i++)
            {
                addPipe(true);
            }
        }
        if(!started)
        {
            started=true;
        }
        else if(!gameOver && !win)
        {
            if(ymotion>0)
            {
                ymotion=0;
            }
            ymotion-=10;
        }
    }
    public void repaint(Graphics g)
    {
        //sky
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH,HEIGHT);
        //ground
        g.setColor(Color.orange);
        int GroundHeight=HEIGHT-150;
        g.fillRect(0,GroundHeight,WIDTH,150);
        //grass
        g.setColor(Color.green);
        int GrassHeight=HEIGHT-150;
        g.fillRect(0,GrassHeight,WIDTH,20);
        //grass and ground outline
        g.setColor(Color.black);
        g.drawRect(0,GroundHeight,WIDTH,150);
        g.drawRect(0,GrassHeight,WIDTH,20);
        
        //bird design
        g.setColor(Color.yellow);
        g.fillOval(bird.x, bird.y, bird.width, bird.height);
        //bird eyes
        g.setColor(Color.white);
        g.fillOval(bird.x+20, bird.y+3, 10, 10);
        g.setColor(Color.black);
        g.fillOval(bird.x+25, bird.y+6, 5, 5);
        //outline of bird
        g.setColor(Color.black);
        g.drawOval(bird.x, bird.y, bird.width, bird.height);
        //bird beak
        g.setColor(Color.orange);
        g.fillPolygon(new int[] {bird.x+42, bird.x+32 ,bird.x+32}, new int[] {bird.y+10, bird.y+1, bird.y+19}, 3);
        g.setColor(Color.black);
        g.drawPolygon(new int[] {bird.x+42, bird.x+32 ,bird.x+32}, new int[] {bird.y+10, bird.y+1, bird.y+19}, 3);
        //bird eye outline
        g.drawOval(bird.x+20, bird.y+3, 10, 10);
        g.drawOval(bird.x+25, bird.y+6, 5, 5);
        
        for(Rectangle pipe:pipes)
        {
            paintpipe(g,pipe);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Serif",1,100));
        if(!started)
        {
            g.drawString("Click to start",275,HEIGHT/2-50);
        }
        g.setColor(Color.red);
        if(gameOver && !win)
        {
            g.setFont(new Font("Serif",1,50));
            g.drawString("Game Over You're score is: "+score,300,300);
            g.drawString("Click Mouse To Play Again",330,350);
        }
        g.setColor(Color.white);
        if(!gameOver && !win && started)
        {
            g.drawString(String.valueOf(score), WIDTH/2-25, 100);
        }
        g.setColor(Color.yellow);
        g.setFont(new Font("Serif",1,50));
        if(win)
        {
            g.drawString("You Passed All Of The Pipes!!!",250,300);
            g.drawString("Click Mouse To Play Again",280,350);
        }
    }
    public static void main(String[]args)
    {
        flappyBird=new FlappyBird();
    }
    public void mouseClicked(MouseEvent e)
    {
        jump();
    }
    public void mousePressed(MouseEvent e)
    {
        
    }
    public void mouseEntered(MouseEvent e)
    {
        
    }
    public void mouseExited(MouseEvent e)
    {
        
    }
    public void mouseReleased(MouseEvent e)
    {
        
    }
}
