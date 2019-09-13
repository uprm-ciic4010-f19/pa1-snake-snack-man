package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }
    public boolean isGood() {
    	if(Game.Entities.Dynamic.Player.badAppleSteps >= Game.Entities.Dynamic.Player.badAppleSteps_threshold) {
    		return false;
    	}
    return true ;}


}
