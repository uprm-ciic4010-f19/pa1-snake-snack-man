package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

import Game.GameStates.State;

import javax.imageio.ImageIO;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public static int score = 0;
    public int length;
    public boolean justAte;
    private Handler handler;
    public static boolean Stall=true;


    public int xCoord;
    public int yCoord;


    public int SlowingSpeed=10;
    public int moveCounter;

    public String direction;//is your first name one?

    public static int badAppleCounter=0;
    public int badAppleSteps = 0;
    public int badAppleSteps_threshold = 5 * 60; // n * seconds
    public static int TimeTillRotten=5;

    // Load images
    public int pacman_steps = 0;
    public int pacman_direction;// Determines sprite direction
    public boolean pacman_opening = true;
    public static BufferedImage[] items;
    public static BufferedImage[] pacman;
    public static BufferedImage[][] ghosts;

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        length = 1;

        //Sprite loading
        items = new BufferedImage[2];
        pacman = new BufferedImage[9];
        ghosts = new BufferedImage[4][4];

        int pacmanIndex;
        int ghostsIndex;
        String ghostPath;

        try { // cleaner way to load player and ghost sprites
            String[] directions = {"left", "right", "up", "down"};
            String[] ghostNames = {"blinky", "pinky", "clyde", "inky"};
            String[] states = {"semi", "full"};

            items[0] = ImageIO.read(getClass().getResourceAsStream("/items/apple.png"));
            items[1] = ImageIO.read(getClass().getResourceAsStream("/items/grenade.png"));
            pacman[0] = ImageIO.read(getClass().getResourceAsStream("/player/pacman_closed.png"));
            pacmanIndex = 1;

            for (int direction = 0; direction < directions.length; direction++){
                //Load player sprites
                for (int state = 0; state < states.length; state++){
                    pacman[pacmanIndex] = ImageIO.read(getClass().getResourceAsStream("/player/pacman_open_" + states[state] + "_" + directions[direction] + ".png"));
                    pacmanIndex++;
                }

                //load ghost sprites
                for (int ghost = 0; ghost < ghosts.length; ghost++){
                    ghostPath = "/ghosts/" + ghostNames[ghost] + "/" + ghostNames[ghost] + "_" + directions[direction] + "_1.png";
                    ghosts[ghost][direction] = ImageIO.read(getClass().getResourceAsStream(ghostPath));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void tick(){
        moveCounter++;
        badAppleSteps++; //Count the snake steps
        if(moveCounter>=SlowingSpeed) {
          checkCollisionAndMove();
          moveCounter=0;
        }
        if(SlowingSpeed>6) {
        	Stall=true;
        }
        else {
        	Stall=false;
        }
        if((badAppleSteps%60)==0) {
        	TimeTillRotten--;
        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)&&!direction.equals("Down")){
            direction="Up";
        }
        else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)&&!direction.equals("Up")){
            direction="Down";
        }
        else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)&&!direction.equals("Right")){
            direction="Left";
        }
        else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)&&!direction.equals("Left")){
            direction="Right";
        }

        //To remove speed press "-" on the number pad
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT)){
        	//SlowingSpeed++;
            SlowingSpeed=SlowingSpeed+6;

        }
        //To add speed press "+" on the number pad
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD)){
        	//SlowingSpeed--;
            SlowingSpeed=SlowingSpeed-6;

        }
        //To add a piece of the tail press "n"
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
            this.add_tail();

        }
        //To select the pause screen press "Esc"
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){

            State.setState(handler.getGame().pauseState);
            Game.GameStates.MenuState.Music.MusicManagerChangeGameToPause();

        }

    }

    public void checkCollisionAndMove(){

        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        for(int i=0;i<handler.getWorld().body.size();i++) {
        	if(x==handler.getWorld().body.get(i).x&&y==handler.getWorld().body.get(i).y)
        		kill();
        }
        switch (direction){
            case "Left":

                pacman_direction = 0;

                if(xCoord==0){
                    xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    xCoord--;
                }
                break;
            case "Right":

                pacman_direction = 2;

                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    xCoord = 0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":

                pacman_direction = 4;

                if(yCoord==0){
                    yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":

                pacman_direction = 6;

                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    yCoord = 0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){ // Player is in apple's location
            badAppleSteps = 0; //Reset time till apple goes bad
            TimeTillRotten=6;
            Eat();
        }

        else if(handler.getWorld().badAppleLocations[xCoord][yCoord]){
        	badAppleCounter--;
            Eat_bad();
            // if there are too many bad apples start killing them
        }

        if(!handler.getWorld().body.isEmpty()) { //handler.getWorld().body linkedlist that keep the values of each tail (not the head)
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler)); //Removes the last present tail and adds a new one in front
        }

        if(badAppleSteps >= badAppleSteps_threshold&&badAppleCounter<30){ //Checks if apple goes bad
            badAppleSteps = 0;
            TimeTillRotten=6;
            badAppleCounter++;
            // Make a new apple spawn
            handler.getWorld().appleLocation[handler.getWorld().apple.xCoord][handler.getWorld().apple.yCoord]=false;
            handler.getWorld().appleOnBoard=false;

            // Generate new bad apple
            handler.getWorld().badAppleLocations[handler.getWorld().apple.xCoord][handler.getWorld().apple.yCoord] = true;
        }

        // Add "movement" to the pacman sprite
        if (pacman_opening){
            if (pacman_steps == 2){
                pacman_opening = false;
                pacman_steps--;
            }
            else{
                pacman_steps++;
            }
        }
        else {
            if (pacman_steps == 0){
                pacman_opening = true;
                pacman_steps++;
            }
            else{
                pacman_steps--;
            }
        }

    }

    public void render(Graphics g,Boolean[][] playerLocation){
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                if(playerLocation[i][j]){
                    if(pacman_steps == 0){
                        pacman_direction = 0;
                    }
                    g.drawImage(pacman[pacman_steps + pacman_direction],(i*handler.getWorld().GridPixelsize),
                                    (j*handler.getWorld().GridPixelsize),
                                    handler.getWorld().GridPixelsize,
                                    handler.getWorld().GridPixelsize, null);
                }

                else if(handler.getWorld().appleLocation[i][j]){
                    g.drawImage(items[0],(i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize, null);
                }

                else if(handler.getWorld().badAppleLocations[i][j]){
                    g.drawImage(items[1],(i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize, null);
                }

                else if(!handler.getWorld().body.isEmpty()) {
                    //Call each tail as a ghost
                    for (int index = 0; index < handler.getWorld().body.size(); index++){
                        int xLoc = handler.getWorld().body.get(index).x;
                        int yLoc = handler.getWorld().body.get(index).y;

                        g.drawImage(ghosts[handler.getWorld().bodyColor.get(index)][pacman_direction%2],
                                (xLoc*handler.getWorld().GridPixelsize),
                                (yLoc*handler.getWorld().GridPixelsize),
                                handler.getWorld().GridPixelsize,
                                handler.getWorld().GridPixelsize, null);

                    }
                }

            }
        }

    }

    public void Eat(){
        score = (int) Math.sqrt((2 * score + 1))+score;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        this.add_tail(); // Moved to function for better readability


    }

    public void Eat_bad() {
    	if((score - (int) Math.sqrt( (2 * score + 1)))>=0){
    		score = score - (int) Math.sqrt( (2 * score + 1) );
    	}
        handler.getWorld().badAppleLocations[xCoord][yCoord]=false;
        SlowingSpeed=SlowingSpeed+6;
        try{
            // Remove last tail
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            //remove one from handler.getWorld().body
            handler.getWorld().bodyColor.remove(handler.getWorld().bodyColor.size() - 1);
        }
        catch (NoSuchElementException e){
            kill();
        }
    }

    public void add_tail(){
        length++;
        Tail tail= null;
        switch (direction){
            case "Left":
                if(handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }
                else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if(handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if(handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1&&this.yCoord<59){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if(handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0&&this.yCoord>0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
        SlowingSpeed=SlowingSpeed-6;
    }

    public void kill(){
        length = 0;
        State.setState(handler.getGame().GameOver);
        Game.GameStates.MenuState.Music.MusicManagerGameToGameOver();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
