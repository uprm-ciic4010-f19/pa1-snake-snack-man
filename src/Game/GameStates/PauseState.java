package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;
import Game.GameStates.*;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class PauseState extends State {

    public static int count = 0;
    private UIManager uiManager;

    public PauseState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(70, 310, 128, 64, Images.Resume, () -> {
        	count=0;
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().gameState);
            Game.GameStates.MenuState.Music.MusicManagerPauseToGame();
        }));

        uiManager.addObjects(new UIImageButton(70, (223+(64+16))+(64+16), 256, 74, Images.BTitle, () -> {
            handler.getMouseManager().setUimanager(null);
            Game.GameStates.MenuState.Music.MusicManagerPauseToMenu();
            State.setState(handler.getGame().menuState);
            
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
            Game.GameStates.MenuState.Music.MusicManagerPauseToGame();
        }


    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.Pause,0,0,840,840,null);
        uiManager.Render(g);

    }
}
