package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    public boolean Good=true;

    public Apple(Handler handler,int x, int y,boolean Good){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
        this.Good=Good;
    }
    public boolean isGood() {
    return Good;}


}
