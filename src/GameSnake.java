
/**
Classic Snake Game
22.03.2020
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GameSnake {
    final String TITLE_OF_PROGRAM = "Classic Snake Game";
    final String GAME_OVER_MSG = "GAME OVER";
    final int POINT_RADIUS = 20; // in pixels
    final int FIELD_HEIGHT = 20; // cubics
    final int FIELD_WIDTH = 30;
    final int FIELD_DX = 6;  //correction variables
    final int FIELD_DY = 28;
    final int START_LOCATION = 200; // respown
    final int START_SNAKE_SIZE = 6;
    final int START_SNAKE_X = 10;
    final int START_SNAKE_Y = 6;
    final int SHOW_DELAY = 150;
    final int LEFT = 37; // code of buttons on keyboard
    final int UP = 38;
    final int RIGHT = 39;
    final int DOWN = 40;
    final int START_DIRECTION = RIGHT;

    final Color DEFAULT_COLOR = Color.black;
    final Color FOOD_COLOR = Color.green;
    final Color POISON_COLOR = Color.red;
     Snake snake;
     Food food;
//    Poison poison;
    JFrame frame;
    Canvas canvasPanel;
    Random random = new Random();
    boolean gameOver = false;






    public static void main(String[] args) {
      new GameSnake().go();
    }

    void go() {
        frame = new JFrame(TITLE_OF_PROGRAM + " : "+ START_SNAKE_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FIELD_WIDTH * POINT_RADIUS + FIELD_DX, FIELD_HEIGHT*POINT_RADIUS+FIELD_HEIGHT);
        frame.setLocation(START_LOCATION, START_LOCATION);
        frame.setResizable(false);

        canvasPanel = new Canvas();
        canvasPanel.setBackground(Color.white);

        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);// when i write is, snake is showing
        frame.addKeyListener(new KeyAdapter() {// tracking key pressed
            public void keyPressed(KeyEvent e) {
            snake.setDirection(e.getKeyCode());
            System.out.println(e.getKeyCode());// code of pressed key
            }
        });
        frame.setVisible(true);
        snake = new Snake(START_SNAKE_X, START_SNAKE_Y, START_SNAKE_SIZE, START_DIRECTION);
        food = new Food();
        while (!gameOver){
            snake.move();
            if (food.isEaten()){
                food.next();
            }
            canvasPanel.repaint();
            try{
                Thread.sleep(SHOW_DELAY);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    class Point {
        int x, y;
        Color color = DEFAULT_COLOR;
        //construcror
        public Point(int x, int y){
            this.setXY(x,y);
        }

        void paint(Graphics g){ // painting for coord
            g.setColor(color);
            g.fillOval(x*POINT_RADIUS,y*POINT_RADIUS,POINT_RADIUS,POINT_RADIUS );

        }

        void setXY(int x, int y){ // install coord
            this.x = x;
            this.y = y;

        }
        int getX() {
            return  x;
        }
        int getY() {
            return y;
        };

    }
    class Food extends Point{

        public Food() {
            super(-1, -1);
            this.color = FOOD_COLOR;
        }

        void eat(){
            this.setXY(-1,-1);
        }
        boolean isEaten(){
            return this.getX() == -1;
        }
        void next(){
            int x, y;
            do {
                x = random.nextInt(FIELD_WIDTH);
                y = random.nextInt(FIELD_HEIGHT);
            }while (snake.isInsideSnake(x,y));
            this.setXY( x, y);
        }
    }

    class Snake {
        ArrayList<Point> snake = new ArrayList<Point>();
        int direction;

        public Snake(int x,int y, int length, int direction) {
            for(int i = 0; i < length; i++){
                Point point = new Point(x-i, y);
                snake.add(point);
            }
            this.direction = direction;
        }

        boolean isInsideSnake(int x, int y){
            for (Point point : snake){
                if ((point.getX()==x)&&(point.getY() == y)){
                    return true;
                }
            }
            return false;
        }
        boolean ifFood(Point food){
            return ((snake.get(0).getX()==food.getX())&& (snake.get(0).getY()==food.getY()));
        }

        void move(){
            int x = snake.get(0).getX();
            int y = snake.get(0).getY();
            if (direction ==LEFT) {x--; };
            if (direction == RIGHT) {x++;};
            if(direction == UP) {y--; };
            if (direction == DOWN) {y++;};

            //here add no walls

            snake.add(0, new Point(x,y));
            if (ifFood(food)){
               food.eat();
               frame.setTitle(TITLE_OF_PROGRAM+ " : "+ snake.size());
            }else{
                snake.remove(snake.size()-1);
            }
            snake.remove(snake.size()- 1);
        }

        void setDirection(int direction){
            if ((direction >= LEFT) && (direction <= DOWN)){
                this.direction=direction;
            }
        }

        void paint(Graphics g){
            for (Point point: snake){
                point.paint(g);
            }
        }
    }

    public class Canvas extends JPanel{
        @Override
        public void paint(Graphics g){
            super.paint(g);
            snake.paint(g);
            food.paint(g);
        }

    }

}
