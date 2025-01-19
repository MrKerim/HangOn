import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.*;
import java.util.List;

public class DrawingUtils {
    DrawingUtils(){}

    public static void drawCharacter(Graphics g, Component observer) {

        ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource(Game.getCharcterImagePath()));
        Image image = icon.getImage();

        g.drawImage(image, GameSettings.character_drawing_x,
                           GameSettings.character_drawing_y ,observer);

        // indicating the position of the character
//        g.setColor(Color.MAGENTA);
//        g.fillRect(GameSettings.character_drawing_x + icon.getIconWidth()/2,GameSettings.character_drawing_y+ icon.getIconHeight()*8/10,10,10);
    }

    public static void drawSky(Graphics g,Component observer) {
        //g.setColor(Color.blue);
        g.setColor(new Color(130, 202, 215));
        g.fillRect(0, 0, GameSettings.with, GameSettings.height/2 + 1);



        ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource("assets/back_test.png"));
        Image image = icon.getImage();
        g.drawImage(image,0,GameSettings.height/2-icon.getIconHeight() +40,observer);


    }

    private static void drawFourSidedPolygon(Graphics g, Color c, int x1, int y1, int w1, int x2, int y2, int w2) {
        int[] xPoints = { x1 - w1, x2 - w2, x2 + w2, x1 + w1 };
        int[] yPoints = { y1, y2, y2, y1 };
        g.setColor(c);
        g.fillPolygon(xPoints, yPoints, 4);
    }

    public static void drawRoadAndGrass(Graphics g,int pos,int playerX) {
        double x = 0, dx = 0;
        int startPos = pos / GameSettings.segL;
        double maxY = GameSettings.height;
        List<Line> lines = Game.getLines();
        int camH = GameSettings.camHeightBase + (int) lines.get(startPos).y;
        Color grassColor, roadBorderColor, middleLineColor;
        Color roadColor = Color.BLACK;

        for (int i = startPos; i < startPos + GameSettings.horizon_length; i++) {

            Line l = lines.get(i);
            l.project(playerX - (int) x, camH, pos);
            x += dx;
            dx += l.curve;
            if (l.Y > 0 && l.Y < maxY) {
                maxY = l.Y;

                if(((i / 2) % 2) == 0){
                    grassColor = GameSettings.ligthGrass;
                    roadBorderColor = GameSettings.lightRoadBorder;
                    middleLineColor = GameSettings.lightMiddleLine;
                }
                else {
                    grassColor = GameSettings.darkGrass;
                    roadBorderColor = GameSettings.darkRoadBorder;
                    middleLineColor = GameSettings.darkMiddleLine;
                }

                Line p = null;
                if (i == 0) {
                    p = l;
                } else {
                    p = lines.get(i - 1);
                }

                drawFourSidedPolygon(g, grassColor, 0, (int) p.Y, GameSettings.with, 0, (int) l.Y, GameSettings.with);
                drawFourSidedPolygon(g, roadBorderColor, (int) p.X, (int) p.Y, (int) (p.W * 1.5), (int) l.X, (int) l.Y,
                        (int) (l.W * 1.5));

                drawFourSidedPolygon(g, roadColor, (int) p.X, (int) p.Y, (int) (p.W * 1.4), (int) l.X, (int) l.Y,
                        (int) (l.W * 1.4));

                drawFourSidedPolygon(g, middleLineColor, (int) p.X, (int) p.Y, (int) (p.W * 0.8), (int) l.X, (int) l.Y,
                        (int) (l.W * 0.8));
                drawFourSidedPolygon(g, roadColor, (int) p.X, (int) p.Y, (int) (p.W * 0.7), (int) l.X, (int) l.Y, (int) (l.W* 0.7));

                if(l.isCheckPoint) drawCheckPoint(g,p,l,i);
            }
        }

    }

    private static void drawCheckPoint(Graphics g,Line previous,  Line current,int i) {
        double pDW = (previous.W*1.4)/10;
        double cDW = (current.W*1.4)/10;
        boolean even = true;

        Color c1;
        Color c2;
        if(i%2 == 0){
            c1 = Color.BLACK;
            c2 = Color.WHITE;
        }
        else {
            c1 = Color.WHITE;
            c2 = Color.BLACK;
        }

        for(double temp = 0;temp<10;temp+=1.5){
            if(even) drawFourSidedPolygon(g, c1, (int) previous.X, (int) previous.Y, (int) (previous.W * 1.4 - temp*pDW), (int) current.X, (int) current.Y,
                    (int) (current.W * 1.4 - temp*cDW));
            else drawFourSidedPolygon(g, c2, (int) previous.X, (int) previous.Y, (int) (previous.W * 1.4 - temp*pDW), (int) current.X, (int) current.Y,
                    (int) (current.W * 1.4 - temp*cDW));

            even = !even;
        }
    }

    public static void drawRacer(Graphics g,Component observer, Racer racer, int pos, int playerX) {
        racer.project(playerX, GameSettings.camHeightBase,pos);
            if(racer.get_z() - pos > 25*GameSettings.segL ) return;
            if(racer.get_z() - pos <  -20) return;

            int temp_x = 0;
            int start_pos = pos / GameSettings.segL + 1;
            int end_pos = (int) racer.get_z()/GameSettings.segL + 1;

            for(int i = start_pos; i < end_pos-1; i++){
                temp_x += (int) Game.getLines().get(i).curve;
            }

            int draw_x = (int) racer.get_X() + temp_x;
            int draw_y = (int) racer.get_Y();

            // testing
//            g.setColor(Color.cyan);
//            g.fillRect(draw_x,(int) draw_y,10,10);

            ImageIcon icon;
            Image image;

            if(racer.getEndingGameCharacter()) {
                icon = new ImageIcon(DrawingUtils.class.getResource(getEndingCharacterImageBasedOnDistanceDiff(racer.get_z() - pos)));
                image = icon.getImage();
            }else{
                icon = new ImageIcon(DrawingUtils.class.getResource(getRacerImageBasedOnDistanceDiff(racer.get_z() - pos)));
                image = icon.getImage();
            }

            g.drawImage(image, draw_x - icon.getIconWidth()/2 ,
        draw_y - icon.getIconHeight() ,observer);


    }

    private static String getRacerImageBasedOnDistanceDiff(double distance) {
        double segDistance =  distance / GameSettings.segL;

        if(segDistance < 1.2 ) return "assets/opponent_back_1.png";
        if(segDistance < 1.8 ) return "assets/opponent_back_2.png";
        if(segDistance < 2.5 ) return "assets/opponent_back_3.png";
        if(segDistance < 3.6 ) return "assets/opponent_back_4.png";
        if(segDistance < 5 ) return "assets/opponent_back_5.png";
        if(segDistance < 7.2 ) return "assets/opponent_back_6.png";
        if(segDistance < 10 ) return "assets/opponent_back_7.png";
        if(segDistance < 20 ) return "assets/opponent_back_8.png";
        return "assets/opponent_back_9.png";

    }

    private static String getEndingCharacterImageBasedOnDistanceDiff(double distance) {
        double segDistance =  distance / GameSettings.segL;

        if(segDistance < 1.2 ) return "assets/character_winning_1.png";
        if(segDistance < 1.8 ) return "assets/character_winning_2.png";
        if(segDistance < 2.5 ) return "assets/character_winning_3.png";
        if(segDistance < 3.6 ) return "assets/character_winning_4.png";
        if(segDistance < 5.2 ) return "assets/character_winning_5.png";
        if(segDistance < 7 ) return "assets/character_winning_6.png";
        return "assets/character_winning_7.png";

    }

    public static void drawAllRacers(Graphics g, Component observer,int pos, int playerX) {
        List<Racer> racers = Game.getRacers();
        Collections.sort(racers, new RacerComprator());

        for(Racer racer : racers) {
            drawRacer(g,observer,racer,pos,playerX);
        }
    }

    private static String  getTreeImageBasedOnDistanceDiff(double distance,int i) {
        double segDistance =  distance / GameSettings.segL;

        if(i% (2*GameSettings.treeSpawnFrequency) < GameSettings.treeSpawnFrequency ) {
            if (segDistance < 4) return "assets/tree_b_1.png";
            if (segDistance < 7) return "assets/tree_b_2.png";
            if (segDistance < 14) return "assets/tree_b_3.png";
            if (segDistance < 21) return "assets/tree_b_4.png";
            if (segDistance < 27) return "assets/tree_b_5.png";
            return "assets/tree_b_6.png";
        }
        else{
            if (segDistance < 4) return "assets/tree_a_1.png";
            if (segDistance < 7) return "assets/tree_a_2.png";
            if (segDistance < 10) return "assets/tree_a_3.png";
            if (segDistance < 14) return "assets/tree_a_4.png";
            if (segDistance < 20) return "assets/tree_a_5.png";
            return "assets/tree_a_6.png";
        }
    }

    public static void drawTree(Graphics g, Component observer,int pos, int playerX) {

        int startPos = pos  / GameSettings.segL;

        for(int i = startPos + GameSettings.horizon_length -1 ; i > startPos ; i--) {
            Line l = Game.getLines().get(i);
            double distance = l.z - pos;

            if(distance > 30*GameSettings.segL ) continue;
            if(distance <  1200) break;


            String iconName = getTreeImageBasedOnDistanceDiff(distance,i);

            if(!(l.treeOnRight || l.treeOnLeft)) continue;

            if(l.treeOnRight) {

                ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource(iconName));
                Image image = icon.getImage();

                int tree_x = (int) (l.X + l.W * 1.8);
                int tree_y = (int) l.Y - icon.getIconHeight();


                g.drawImage(image, tree_x, tree_y, observer);
//                g.setColor(Color.MAGENTA);
//                g.fillRect(tree_x, tree_y, 10, 10);
            }
            if(l.treeOnLeft) {
                ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource(iconName));
                Image image = icon.getImage();

                int tree_x = (int) (l.X - l.W * 1.8 - icon.getIconWidth());
                int tree_y = (int) l.Y - icon.getIconHeight();


                g.drawImage(image, tree_x, tree_y, observer);

//                g.setColor(Color.MAGENTA);
//                g.fillRect(tree_x, tree_y, 10, 10);
            }
        }
    }

    public static void drawFallingCharacter(Graphics g, Component observer,int scene) {

        ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource(getFallingCharacterImagePath(scene)));
        Image image = icon.getImage();

        g.drawImage(image, GameSettings.character_drawing_x,
                GameSettings.character_drawing_y ,observer);
    }

    public static void drawExploadingCharacter(Graphics g, Component observer,int scene) {

        ImageIcon icon = new ImageIcon(DrawingUtils.class.getResource(getExplodingImagePath(scene)));
        Image image = icon.getImage();

        g.drawImage(image, GameSettings.character_drawing_x,
                GameSettings.character_drawing_y-icon.getIconHeight()/2 ,observer);
    }

    private static String getFallingCharacterImagePath(int scene) {
        if(scene == 1) return "assets/fall_ending_1.png";
        if(scene == 2) return "assets/fall_ending_2.png";
        if(scene == 3) return "assets/fall_ending_3.png";
        if(scene == 4) return "assets/fall_ending_4.png";
        if(scene == 5) return "assets/fall_ending_5.png";
        if(scene == 6) return "assets/fall_ending_6.png";
        if(scene == 7) return "assets/fall_ending_7.png";
        if(scene == 8) return "assets/fall_ending_8.png";
        return "assets/fall_ending_9.png";

    }

    private static String getExplodingImagePath(int scene) {
        if(scene == 1) return "assets/explosion_1.png";
        if(scene == 2) return "assets/explosion_2.png";
        if(scene == 3) return "assets/explosion_3.png";
        if(scene == 4) return "assets/explosion_4.png";
        if(scene == 5) return "assets/explosion_5.png";
        if(scene == 6) return "assets/explosion_6.png";
        return "assets/explosion_7.png";


    }

    public static void drawBeforeStart(Graphics g, int time) {


        g.setFont(GameSettings.arcadeFontBackground.deriveFont(80f));
        g.setColor(Color.white);
        g.drawString("" +time , 760 , 400);

        g.setFont(GameSettings.arcadeFont.deriveFont(80f));
        g.setColor(Color.black);
        g.drawString("" +time, 760 , 400);

    }

    public static void drawGameOverText(Graphics g, Component observer) {
        //g.setFont(new Font("Arial", Font.BOLD, 60));
        g.setFont(GameSettings.arcadeFontBackground.deriveFont(62f));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER",550, 350);

        g.setFont(GameSettings.arcadeFont.deriveFont(62f));
        g.setColor(Color.BLACK);
        g.drawString("GAME OVER",550, 350);
    }

    public static void drawGameWinText(Graphics g, Component observer) {
        g.setFont(GameSettings.arcadeFontBackground.deriveFont(62f));
        g.setColor(Color.WHITE);
        g.drawString("GAME WON",600, 350);

        g.setFont(GameSettings.arcadeFont.deriveFont(62f));
        g.setColor(Color.BLACK);
        g.drawString("GAME WON",600, 350);
    }

    public static void drawSpeedPanel(Graphics g, int speed) {



        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.red);
        g.drawString("SPEED  ", 1020 , 107);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString("SPEED  ", 1020 , 107);

        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.white);
        g.drawString("" + (speed/3) , 1165 , 107);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString("" + (speed/3), 1165 , 107);
    }

    public static void drawScorePanel(Graphics g, int pos) {


        // High Score
        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(new Color(31, 73, 39));
        g.drawString("HIGH SCORE ", 120 , 107);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString("HIGH SCORE  ", 120 , 107);

        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.white);
        g.drawString(LoginScreen.getPreviousHighScore(), 360 , 107);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString(LoginScreen.getPreviousHighScore(), 360 , 107);

        //Current Score
        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.GREEN);
        g.drawString("SCORE  ", 120 , 167);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString("SCORE  ", 120 , 167);

        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.white);
        g.drawString(""+pos, 265 , 167);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString(""+pos, 265 , 167);

    }

    public static void drawRemainingTime(Graphics g, int totalTimeElapsed,int previousTime){

        int interval = GameSettings.timeRemainingPerCheckPoint - (totalTimeElapsed - previousTime)/1000;

        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));
        g.setColor(Color.YELLOW);
        g.drawString("TIME"  , 745, 107);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString("TIME" , 745, 107);

        g.setFont(GameSettings.arcadeFontBackground.deriveFont(30f));

        if(interval == GameSettings.timeRemainingPerCheckPoint){
            if(totalTimeElapsed % 500 < 250) g.setColor(Color.WHITE);
            else g.setColor(Color.RED);
        } else g.setColor(Color.white);

        g.drawString(""+interval  , 765, 140);

        g.setFont(GameSettings.arcadeFont.deriveFont(30f));
        g.setColor(Color.black);
        g.drawString(""+interval , 765, 140);
    }

    public static void drawPauseScreen(Graphics g) {
        g.setColor(new Color(0,0,0, 145));
        g.fillRect(0,0,GameSettings.with,GameSettings.height);
    }

}
