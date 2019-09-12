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

        int ghostColor = new Random().nextInt(3);
        handler.getWorld().bodyColor.add(ghostColor);

        this.x=x;
        this.y=y;
        handler.getWorld().playerLocation[x][y]=true;

    }

}
