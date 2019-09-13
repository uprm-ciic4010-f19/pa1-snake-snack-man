package Game.Entities.Dynamic;

import Main.Handler;
import java.awt.Color;
import java.util.Random;
/**
 * Created by AlexVR on 7/2/2018.
 */
public class Tail {
    public int x,y;
    public Tail(int x, int y,Handler handler){

    	if(Game.Entities.Dynamic.Player.experiment==true) {
        int ghostColor = new Random().nextInt(3);
        handler.getWorld().bodyColor2.add(ghostColor);}
    	else {
            boolean randomColors = true; //Keep false to have only the ghost colors

            if (randomColors == true){
                int red = new Random().nextInt(255);
                int green = new Random().nextInt(255);
                int blue = new Random().nextInt(255);

                handler.getWorld().bodyColor.add(new Color(red, green, blue));
            }
            else{
                Color colors[] = new Color[4];
                colors[0] = new Color(255, 0, 0);
                colors[1] = new Color(255, 184, 255);
                colors[2] = new Color(0, 255, 255);
                colors[3] = new Color(255, 184, 82);

                int rnd = new Random().nextInt(colors.length);
                handler.getWorld().bodyColor.add(colors[rnd]);
            }
    	}

        this.x=x;
        this.y=y;
        handler.getWorld().playerLocation[x][y]=true;

    }

}


