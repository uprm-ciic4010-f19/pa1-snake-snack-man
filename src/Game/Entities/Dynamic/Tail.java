package Game.Entities.Dynamic;

import Main.Handler;
import java.awt.Color;
import java.util.Random;
/**
 * Created by AlexVR on 7/2/2018.
 */
public class Tail {
    public int x,y;
    public Color ghostColor;
    public Tail(int x, int y,Handler handler){
        Color colors[] = new Color[4];
        colors[0] = new Color(255, 0, 0);
        colors[1] = new Color(255, 184, 255);
        colors[2] = new Color(0, 255, 255);
        colors[3] = new Color(255, 184, 82);

        int rnd = new Random().nextInt(colors.length);
        this.ghostColor = colors[rnd];
        this.x=x;
        this.y=y;
        handler.getWorld().playerLocation[x][y]=true;

    }

}
