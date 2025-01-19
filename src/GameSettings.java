import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GameSettings {
    GameSettings() {}

    public enum GameStatus{
        BEFORE_GAME_START,GAME_OVER_TIME_OUT,RUNNING,GAME_OVER_EXPLOSION,GAME_OVER_FALL, GAME_FINISHED
    }

    public static final int D_W = 1600;
    public static final int D_H = 1200;
    public static final int with = 1600;
    public static final int height = 768;
    //public static final int roadW = 600;
    public static final int roadW = 1000;
    public static final int segL = 768;
    public static final double camD = 0.84;
    public static final int camHeightBase = 1000;

    // Character to be drawned bttom center (1600/2 and 700+)
    public static final int character_drawing_x = 692;
    public static final int character_drawing_y = 400;

    public static final int raceTrackBound = 1950;

    public static final  double shiftPlayerInCornerOffset = 3.1;
    //public static final  double shiftPlayerInCornerOffset = 0;

    public static final int characterMaxSpeed = 1000;

    public static final int roadLength = 5000;

    public static final int curve_length = 400;
    public static final int curve_angle = 10;
    public static final int[] rightCurveStart = new int[]{ 1000,2400 };
    public static final int[] leftCurveStart = new int[]{ 300,1700 };
    public static final int[] checkpoints = new int[] { 0,800,1500,2200,2900,3700 };

    // number of lines we see until the horizon (useless to increase after 300)
    public static final int horizon_length = 300;

    //public static final Color ligthGrass = new Color(16, 200, 16);
    public static final Color ligthGrass = new Color(61,127,90);
    //public static final Color darkGrass = new Color(0, 154, 0);
    public static final Color darkGrass = new Color(43, 75, 67);
    public static final Color lightRoadBorder = new Color(255, 255, 255);
    public static final Color darkRoadBorder = new Color(255, 0, 0);
    public static final Color lightMiddleLine = new Color(255, 255, 255);
    public static final Color darkMiddleLine = new Color(0, 0, 0);

    public static final int racersXOffset = 400;
    public static final int numberOfRacers = 5;
    public static final double posDifferenceInStartForRacersCoeff = 5;
    //public static final int maxRacerSpeed = 1000;
    public static final int maxRacerSpeed = 1400;
    public static final int minRacerSpeed = 50;
    public static final double racerAcceleration = 1.9;

    public static final int treeSpawnFrequency = 300;
    public static final int deadSpeedLimitAfterHittingTree = 700;
    public static final int fallingSpeedLimitAfterHittingTree = 400;


    public static Font arcadeFont;
    public static Font arcadeFontBackground;
    public static void initFont() {
        try {
            InputStream fontStream = GameSettings.class.getResourceAsStream("font/arcadeFont.ttf");
            if (fontStream == null) throw new IOException("Font not found");
            arcadeFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(15f);

            InputStream fontBackgroundStream = GameSettings.class.getResourceAsStream("font/arcadeFontBackGround.otf");
            if (fontBackgroundStream == null) throw new IOException("Font not found");
            arcadeFontBackground = Font.createFont(Font.TRUETYPE_FONT, fontBackgroundStream).deriveFont(15f);

        } catch (FontFormatException e) {
            e.printStackTrace();
            arcadeFont = new Font("Arial", Font.PLAIN, 30);
        } catch (IOException e) {
            e.printStackTrace();
            arcadeFont = new Font("Arial", Font.PLAIN, 30);
        }
    }


    public static final int gameStartInterval = 4000;

    public static final int timeRemainingPerCheckPoint = 30;

    public static final String bcryptSalt = "$2a$12$1234567890123456789012";

}
