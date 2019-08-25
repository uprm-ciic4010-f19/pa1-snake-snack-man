package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOver extends State {

    private int count = 0;
    private UIManager uiManager;

    public GameOver(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(430, 420, 128, 128, Images.Exit, () -> {
            handler.getMouseManager().setUimanager(null);
            System.exit(0);
        }));


        uiManager.addObjects(new UIImageButton(270, 420, 128, 128, Images.TryAgain, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().reStart();
            State.setState(handler.getGame().gameState);
        }));





    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.GameOver,0,0,840,840,null);
        uiManager.Render(g);

    }
}
