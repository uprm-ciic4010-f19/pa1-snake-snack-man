package Game.GameStates;


import Main.Handler;
import Resources.Images;
import UI.ClickListlener;
import UI.UIImageButton;
import UI.UIManager;
import java.awt.*;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


import Input.MusicManager;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class MenuState extends State {
    public InputStream audioFile;
    public AudioInputStream audioStream;
    public AudioFormat format;
    public DataLine.Info info;
    public Clip audioClip;
    public static MusicManager Music = new MusicManager();
    
    private UIManager uiManager;

    public MenuState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);
        Music.MusicManager();//Sets title screen music


        uiManager.addObjects(new UIImageButton(handler.getWidth()/2-64, handler.getHeight()/2+230, 128, 64, Images.butstart, new ClickListlener() {
            @Override
            public void onClick() {
                handler.getMouseManager().setUimanager(null);
                handler.getGame().reStart();
                State.setState(handler.getGame().gameState);
                Music.MusicManagerChangeMenuToGame();//Changes music to in-game music
                
            }
        }));
    }
  
    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        g.drawImage(Images.title,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);

    }


}
