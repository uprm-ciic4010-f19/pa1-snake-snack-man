package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public double score = 0;
    public int length;
    public boolean justAte;
    private Handler handler;

    public int xCoord;
    public int yCoord;

    public int SlowingSpeed=10;
    public int moveCounter;

    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        length = 1;

    }

    public void tick(){
        moveCounter++;
        if(moveCounter>=SlowingSpeed) {
          checkCollisionAndMove();
          moveCounter=0;
        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)&&direction!="Down"){
            direction="Up";
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)&&direction!="Up"){
            direction="Down";
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)&&direction!="Right"){
            direction="Left";
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)&&direction!="Left"){
            direction="Right";
        }

      //To remove speed press "-" on the number pad
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT)){
        	SlowingSpeed++;

        }
        //To add speed press "+" on the number pad
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD)){
        	SlowingSpeed--;

        }
        //To add a piece of the tail press "n"
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
            this.add_tail();

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
                if(xCoord==0){
                    xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    xCoord = 0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    yCoord = 0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) { //handler.getWorld().body linkedlist that keep the values of each tail (not the head)
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler)); //Removes the last present tail and adds a new one in front
        }

    }

    public void render(Graphics g,Boolean[][] playerLocation){
        int frameLength = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(Color.YELLOW); //Color the pac-man character

                if(playerLocation[i][j]){ // Counts length of player per frame (self collision detection)
                    frameLength++;
                }

                if(playerLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

                if (!handler.getWorld().body.isEmpty()) {
                    //Call each tail as a ghost
                    //TODO: since its a linked list its adding a new "tail" in the front and removing the bottom one, inconsistent design.
                    for (int index = 0; index < handler.getWorld().body.size(); index++){
                        int xLoc = handler.getWorld().body.get(index).x;
                        int yLoc = handler.getWorld().body.get(index).y;
                        g.setColor(handler.getWorld().body.get(index).ghostColor);
                        g.fillRect((xLoc*handler.getWorld().GridPixelsize),
                                (yLoc*handler.getWorld().GridPixelsize),
                                handler.getWorld().GridPixelsize,
                                handler.getWorld().GridPixelsize);
                    }
                }

            }
        }
        if(frameLength < length){ // Detects collisions indirectly
            //this.kill(); //Framing is messing with any type of precision
        }

    }

    public void Eat(){
        score = Math.sqrt((2 * score + 1));
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        this.add_tail(); // Moved to function for better readability


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
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
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
                    if(handler.getWorld().body.getLast().y!=0){
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
        Game.GameStates.MenuState.Music.MusicManagerChange2();
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
