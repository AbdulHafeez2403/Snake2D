import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener , KeyListener {

    private int[] snakexLength = new int[750];
    private int[] snakeyLength = new int[750];
    private int lengthOfSnake = 3;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private int score = 0;    //score

    //position of apple
    private int[] xpos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    private int[] ypos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};

    private Random random=new Random();
    private int applex , appley;

    private int moves =0;
    //making object of image
    private ImageIcon snaketitle=new ImageIcon(getClass().getResource("snaketitle.jpg"));
    private ImageIcon leftMouth=new ImageIcon(getClass().getResource("leftmouth.png"));
    private ImageIcon rightMouth=new ImageIcon(getClass().getResource("rightmouth.png"));
    private ImageIcon upMouth=new ImageIcon(getClass().getResource("upmouth.png"));
    private ImageIcon downMouth=new ImageIcon(getClass().getResource("downmouth.png"));
    private ImageIcon snakeImage=new ImageIcon(getClass().getResource("snakeimage.png"));
    private ImageIcon apple = new ImageIcon(getClass().getResource("enemy.png"));

    private boolean gameOver = false;

    private Timer timer;
    private int delay = 100;

    GamePanel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        //after every 100ms it will call this actionListener class i.e it will call overridden method
        timer = new Timer(delay,this);
        timer.start();

        //setting apple
        newApple();
    }

    private void newApple() {
        //position of apple
        applex = xpos[random.nextInt(34)];
        appley = ypos[random.nextInt(23)];

        //apple should not be placed on snake's body
        for(int i = lengthOfSnake-1 ; i>=0 ; i--) {
            if(snakexLength[0] == applex && snakeyLength[0] == appley) {
                newApple();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //draw panel and ser border to it
        g.setColor(Color.WHITE);
        g.drawRect(24,10,851,55);
        g.drawRect(24,74,851,576);
        snaketitle.paintIcon(this,g,25,11);
        g.setColor(Color.BLACK);
        g.fillRect(25,75,850,575);

        //Snake
        if(moves ==0){
            snakexLength[0]=100;    //head position
            snakexLength[1]=75;     //second part
            snakexLength[2]=50;     //third part

            snakeyLength[0]=100;
            snakeyLength[1]=100;
            snakeyLength[2]=100;
            //moves++;        //snake will run if we execute program
        }
        //Drawing snake head
        if(left) {
            leftMouth.paintIcon(this,g,snakexLength[0],snakeyLength[0]);
        }
        if(right){
            rightMouth.paintIcon(this,g,snakexLength[0],snakeyLength[0]);
        }
        if(up) {
            upMouth.paintIcon(this,g,snakexLength[0],snakeyLength[0]);
        }
        if(down){
            downMouth.paintIcon(this,g,snakexLength[0],snakeyLength[0]);
        }

        //body of snake
        for(int i = 0 ; i<lengthOfSnake ; i++) {
            snakeImage.paintIcon(this,g,snakexLength[i],snakeyLength[i]);
        }

        apple.paintIcon(this , g , applex , appley);

        //score and length
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD,14));
        g.drawString("LENGTH : "+lengthOfSnake , 750 ,35);
        g.drawString("SCORE : "+score , 750 ,55);

        //GameOver
        if(gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD , 50));
            g.drawString("Game Over", 300 , 300);
            g.setFont(new Font("Arial", Font.PLAIN , 20));
            g.drawString("Press Space To 'Restart'", 300 , 350);
            g.drawString("Press 'Esc' To Quite", 300 , 375);
        }

        g.dispose();    //after this snake will dispose

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //move body with snake Head
        for(int i=lengthOfSnake-1 ; i>0 ; i--) {
            snakexLength[i]=snakexLength[i-1];
            snakeyLength[i] = snakeyLength[i-1];
        }

        //Changing snakeHead position
        if(left) {
            snakexLength[0]=snakexLength[0]-25;
        }
        if(right) {
            snakexLength[0]=snakexLength[0]+25;
        }
        if(up) {
            snakeyLength[0]=snakeyLength[0]-25;
        }
        if(down) {
            snakeyLength[0]=snakeyLength[0]+25;
        }

        //if it go right then appears from left and vice versa
        if(snakexLength[0]>850)snakexLength[0]=25; //850 range of x-axis
        if(snakexLength[0]<25)snakexLength[0]=850;

        if(snakeyLength[0]>625)snakeyLength[0]=75;
        if(snakeyLength[0]<25)snakeyLength[0]=550;

        collideWithApple();     //function to eat apple

        //on touching body it will show gameOver
        collideWithBody();

        repaint(); //it will call paint method again

    }

    private void collideWithBody() {
        for(int i= lengthOfSnake-1 ; i>0 ; i--) {
            if(snakexLength[i] == snakexLength[0] && snakeyLength[i] == snakeyLength[0]) {
                timer.stop();
                gameOver = true;
            }
        }
    }

    //snake collision with apple
    private void collideWithApple() {
        if(snakexLength[0] == applex && snakeyLength[0] == appley) {
            newApple();
            lengthOfSnake++;
            score++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT && (!right)) {
            left = true;
            right = false;
            up = false;
            down = false;
            moves++;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && (!left)) {
            left = false;
            right = true;
            up = false;
            down = false;
            moves++;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP && (!down)) {
            left = false;
            right = false;
            up = true;
            down = false;
            moves++;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && (!up)) {
            left = false;
            right = false;
            up = false;
            down = true;
            moves++;
        }

        //after pressing space it will restart
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            restart();
        }
//        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//
//        }
    }

    private void restart() {
        gameOver = false;
        moves = 0;
        score = 0;
        lengthOfSnake = 3;
        left = false;
        right = true;
        up = false;
        down = false;
        timer.start();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
