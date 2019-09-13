package Worlds;

import Game.Entities.Dynamic.Player;
import Game.Entities.Dynamic.Tail;
import Game.Entities.Static.Apple;
import Main.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Color;

/**
 * Created by AlexVR on 7/2/2018.
 */
public abstract class WorldBase {

    //How many pixels are from left to right
    //How many pixels are from top to bottom
    //Must be equal
    public int GridWidthHeightPixelCount;

    //automatically calculated, depends on previous input.
    //The size of each box, the size of each box will be GridPixelsize x GridPixelsize.
    public int GridPixelsize;

    public Player player;

    protected Handler handler;


    public Boolean appleOnBoard;
    public Apple apple;
    public Boolean[][] appleLocation;
    public boolean[][] badAppleLocations; // Using boolean instead of Boolean is better for this case scenario

    public Boolean[][] playerLocation; //It's an array where true = snake node and false = empty

    public LinkedList<Tail> body = new LinkedList<>();
    public ArrayList<Integer> bodyColor = new ArrayList<Integer>(); // An array to keep track of all the colors


    public WorldBase(Handler handler){
        this.handler = handler;

        appleOnBoard = false;


    }
    public void tick(){



    }

    public void render(Graphics g){
    	g.setColor(Color.pink);
    	g.setFont(new Font(Font.SERIF, Font.BOLD, 16));
    	g.drawString("Score:" + Game.Entities.Dynamic.Player.score, 1, 15);
    }

}
