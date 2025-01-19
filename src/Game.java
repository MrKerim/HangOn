import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Game extends JPanel {

    private int pos;
    private int playerX;

    private int speed;
    private double angle;

    private int totalTimeElapsed;
    private int previousTime;

    private static List<Racer> racers;
    private static List<Line> lines;

    private static String charcter_image_path;

    private static GameSettings.GameStatus gameStatus;

    private int endingGameCount = 0;

    private int extraPoints = 0;

    Game(){
        pos = 0;
        playerX = 0;

        speed = 0;
        angle = 0;

        previousTime = 0;
        totalTimeElapsed = 0;
        extraPoints = 0;

        gameStatus = GameSettings.GameStatus.BEFORE_GAME_START;


        charcter_image_path = "assets/character_back.png";
        // init road of #number lines (16000 - 25000 is enough)
        lines = Line.initLines(GameSettings.roadLength);
        racers = Racer.initRacers();
        endingGameCount = 0;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        DrawingUtils.drawSky(g,this);
        DrawingUtils.drawRoadAndGrass(g,pos,playerX);
        DrawingUtils.drawTree(g,this,pos,playerX);
        DrawingUtils.drawAllRacers(g,this,pos,playerX);

        //Testing
        DrawingUtils.drawSpeedPanel(g,speed);
        DrawingUtils.drawScorePanel(g,pos);

        if(gameStatus == GameSettings.GameStatus.GAME_OVER_FALL) drawGameFallEndCharacter(g);
        else if(gameStatus == GameSettings.GameStatus.GAME_OVER_EXPLOSION) drawGameExplodeCharacter(g);
        else if(gameStatus == GameSettings.GameStatus.GAME_FINISHED) drawGameEndingCharacter(g);
        else DrawingUtils.drawCharacter(g, this);

        if(gameStatus == GameSettings.GameStatus.BEFORE_GAME_START) drawTimeBeforeGameStart(g);

        DrawingUtils.drawRemainingTime(g,totalTimeElapsed,previousTime);

        if(gameStatus == GameSettings.GameStatus.GAME_OVER_TIME_OUT) timeOutEnding(g);



        if(GameLoop.isGamePaused()){
            DrawingUtils.drawPauseScreen(g);
        }

    }

    // setter getter for updates*

    public static List<Racer> getRacers(){
        return racers;
    }

    public static List<Line> getLines(){
        return lines;
    }

    public static void setCharacterImagePath(String path) {
        charcter_image_path = path;
    }

    public static String getCharcterImagePath() {
        return charcter_image_path;
    }

    public int getPos(){
        return pos;
    }

    public void setPos(int pos){
        this.pos = pos;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setGameStatus(GameSettings.GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameSettings.GameStatus getGameStatus() {
        return gameStatus;
    }


    private void drawTimeBeforeGameStart(Graphics g) {
        endingGameCount+= 10;


        for(int i = 0; i<=GameSettings.gameStartInterval;i+=1000){
            if(endingGameCount <= i  ){
            DrawingUtils.drawBeforeStart(g,(GameSettings.gameStartInterval - endingGameCount)/1000);
            break;
            }
        }


        if(endingGameCount >= GameSettings.gameStartInterval){
            endingGameCount = 0;
            totalTimeElapsed = 0;
            gameStatus = GameSettings.GameStatus.RUNNING;
            return;
        }


    }

    private void timeOutEnding(Graphics g) {
        endingGameCount+=10;
        if(endingGameCount < 3000){
            DrawingUtils.drawGameOverText(g,this);
        }
        else GameLoop.endGame();
    }

    private void drawGameFallEndCharacter(Graphics g){
        endingGameCount+=10;
        if(endingGameCount < 1000){
            DrawingUtils.drawFallingCharacter(g,this,endingGameCount/100);
        }
        else if(endingGameCount < 3000){
            DrawingUtils.drawGameOverText(g,this);
        }
        else GameLoop.endGame();
    }

    private void drawGameExplodeCharacter(Graphics g){
        endingGameCount+=10;
        if(endingGameCount < 1500){
            DrawingUtils.drawExploadingCharacter(g,this,endingGameCount/150);
        }
        else if(endingGameCount < 3500){
            DrawingUtils.drawGameOverText(g,this);
        }
        else GameLoop.endGame();
    }

    private void drawGameEndingCharacter(Graphics g){
        endingGameCount+=10;
        if(endingGameCount == 10){
            Racer endingGameRacer = new Racer(pos + GameSettings.segL,playerX);
            endingGameRacer.setEndingGameCharacter();
            racers.add(endingGameRacer);

        }
        else if(endingGameCount <3500){
            DrawingUtils.drawGameWinText(g,this);
        }
        else
            GameLoop.endGame();

    }

    public int getTotalTimeElapsed() {
        return totalTimeElapsed;
    }
    public void setTotalTimeElapsed(int totalTimeElapsed) {
        this.totalTimeElapsed = totalTimeElapsed;
    }
    public int getPreviousTime() {
        return previousTime;
    }
    public void setPreviousTime(int previousTime) {
        this.previousTime = previousTime;
    }

    public int getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

}

