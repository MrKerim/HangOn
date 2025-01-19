import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameUpdate implements ActionListener {

    private int speed;
    private double angle;
    private Game game;
    private HoldDownKeyListener holdDownKeyListener;
    private int prevChekPoint = -1;


    GameUpdate(Game game, HoldDownKeyListener holdDownKeyListener) {
        this.game = game;
        this.holdDownKeyListener = holdDownKeyListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(GameLoop.isGamePaused()){
            return;
        }

        if(game.getGameStatus() == GameSettings.GameStatus.BEFORE_GAME_START){
            game.repaint();
            return;
        }

        handleTotaltimeElapsed();

        updateOtherRacers();

        angle = updateAngle(game.getAngle(),speed);
        speed = updateSpeed(game.getSpeed());

        collosionDetectionWithRacer();
        collosionDetectionWithTree();



        shiftPlayerInCorner();
        updateCharacterImageBasedOnAngle(angle);

        keepCharacterInRaceTrackBounds();


        // Ending
        if(isGameFinished()) gameFinishProc();
        else{
            game.setSpeed(speed);
            game.setAngle(angle);
            game.setPos(game.getPos() + speed);
        }
        //render
        game.repaint();
    }

    private void updateOtherRacers(){
        List<Racer> racers = game.getRacers();
        //default update is jsut accelerate forward
        for(Racer racer : racers) racer.updateStep();
    }

    private void shiftPlayerInCorner(){
        int startPos = game.getPos() / GameSettings.segL;
        Line l = game.getLines().get(startPos);
        game.setPlayerX( game.getPlayerX() + ((int) ( speed/ ((double) GameSettings.characterMaxSpeed) *-1*(GameSettings.shiftPlayerInCornerOffset )* l.curve) ));
        //
    }

    private int updateSpeed(int speed){
        if(holdDownKeyListener.isUpDown()) speed+= 1;
        else speed-=1;

        if(holdDownKeyListener.isDownDown()) speed-= 5;
        if(speed < 0) speed = 0;
        if(speed > GameSettings.characterMaxSpeed) speed = GameSettings.characterMaxSpeed;

        return speed;

    }

    private double updateAngle(double angle, int speed){
        if(holdDownKeyListener.isLeftDown() && speed>0){
            game.setPlayerX(game.getPlayerX() - 10);
            angle -= 0.6;

        }else if(holdDownKeyListener.isRightDown() && speed>0) {
            game.setPlayerX(game.getPlayerX() + 10);
            angle += 0.6;

        }
        else{
            angle = angle - (angle/10);
        }

        if(angle< -60) angle = -60;
        if(angle > 60) angle = 60;
        return angle;
    }

    private void updateCharacterImageBasedOnAngle(double angle){
        if(-10< angle && angle< 10){
            game.setCharacterImagePath("assets/character_back.png");
        }
        else if(angle >= 10 && angle < 20){
            game.setCharacterImagePath("assets/character_right_1.png");
        }
        else if(angle >= 20 && angle < 30){
            game.setCharacterImagePath("assets/character_right_2.png");
        }
        else if(angle >= 30 && angle < 40){
            game.setCharacterImagePath("assets/character_right_3.png");
        }
        else if(angle >= 40 && angle < 50){
            game.setCharacterImagePath("assets/character_right_4.png");
        }
        else if(angle >= 50 && angle < 60){
            game.setCharacterImagePath("assets/character_right_5.png");
        }
        else if(angle > -20 && angle <= -10 ){
            game.setCharacterImagePath("assets/character_left_1.png");
        }
        else if(angle > -30 && angle <= -20){
            game.setCharacterImagePath("assets/character_left_2.png");
        }
        else if(angle > -40 && angle <= -30){
            game.setCharacterImagePath("assets/character_left_3.png");
        }
        else if(angle > -50 && angle <= -40){
            game.setCharacterImagePath("assets/character_left_4.png");
        }
        else if(angle > -60 && angle <= -50){
            game.setCharacterImagePath("assets/character_left_5.png");
        }
    }

    private void keepCharacterInRaceTrackBounds(){
        if(game.getPlayerX() < -1*GameSettings.raceTrackBound ) game.setPlayerX(-1-GameSettings.raceTrackBound);
        if(game.getPlayerX() > GameSettings.raceTrackBound ) game.setPlayerX(GameSettings.raceTrackBound);
    }

    private void collosionDetectionWithRacer(){
        List<Racer> racers = game.getRacers();

        // temp image
        ImageIcon icon = new ImageIcon(GameUpdate.class.getResource(Game.getCharcterImagePath()));
        Image image = icon.getImage();

        int character_x = (int) GameSettings.character_drawing_x + icon.getIconWidth()/2;
        int character_y = (int) GameSettings.character_drawing_y+ icon.getIconHeight()*8/10;



        for(Racer racer : racers) {
            racer.project(game.getPlayerX(), GameSettings.camHeightBase,game.getPos());
            int racerX = (int) racer.get_X();
            int racerY = (int) racer.get_Y();

            if( Math.abs(racerX - character_x)<100 && Math.abs(racerY - character_y)<40){
                System.out.println("Collision Detected");
                this.speed = speed/2;
                int random_k;
                if(game.getPos()%2 == 1) random_k = 1;
                else random_k = -1;
                this.angle = 70 * random_k;
            }

        }

    }

    private void collosionDetectionWithTree(){

        // temp image
        ImageIcon icon = new ImageIcon(GameUpdate.class.getResource(Game.getCharcterImagePath()));
        Image image = icon.getImage();

        int character_x = (int) GameSettings.character_drawing_x + icon.getIconWidth()/2;
        int character_y = (int) GameSettings.character_drawing_y+ icon.getIconHeight()*8/10;


        int i = game.getPos()/GameSettings.segL ;


        int tree_x;
        if(Game.getLines().get(i).treeOnLeft){
            tree_x = (int) (Game.getLines().get(i).X - Game.getLines().get(i).W * 1.8);
        }
        else if(Game.getLines().get(i).treeOnRight){
            tree_x = (int) (Game.getLines().get(i).X + Game.getLines().get(i).W * 1.8);
        }
        else return;

        if( Math.abs(tree_x - character_x)<200){
                System.out.println("Collision Detected with tree");
                System.out.println("Speed: " + speed);

                if(speed>GameSettings.deadSpeedLimitAfterHittingTree)explosionEndingScene();
                else if(speed>GameSettings.fallingSpeedLimitAfterHittingTree) fallingEndingScene();

                this.speed = speed/2;
                game.setPlayerX(game.getPlayerX()*9/10);
                int random_k;
                if(game.getPos()%2 == 1) random_k = 1;
                else random_k = -1;
                this.angle = 70 * random_k;
            }

    }

    private void explosionEndingScene(){
        if(game.getGameStatus()!= GameSettings.GameStatus.RUNNING) return;
        game.setSpeed(0);
        game.setAngle(0);
        System.out.println("explosionEndingScene");
        game.setGameStatus(GameSettings.GameStatus.GAME_OVER_EXPLOSION);
        unMountKeyListener();
    }

    private void fallingEndingScene(){
        if(game.getGameStatus()!= GameSettings.GameStatus.RUNNING) return;
        game.setSpeed(0);
        game.setAngle(0);
        System.out.println("fallingEndingScene");
        game.setGameStatus(GameSettings.GameStatus.GAME_OVER_FALL);
        unMountKeyListener();

    }

    public void unMountKeyListener(){
        holdDownKeyListener.resetState();
        game.removeKeyListener(holdDownKeyListener);

    }

    private boolean isGameFinished() {
        return (game.getPos()/GameSettings.segL >= GameSettings.checkpoints[GameSettings.checkpoints.length - 1]);
    }

    private void gameFinishProc(){
        if(game.getGameStatus()!= GameSettings.GameStatus.RUNNING) return;
        game.setSpeed(0);
        game.setAngle(0);
        holdDownKeyListener.resetState();
        unMountKeyListener();
        System.out.println("Game Ended");
        game.setGameStatus(GameSettings.GameStatus.GAME_FINISHED);
    }

    private void handleTotaltimeElapsed(){

        if(game.getGameStatus()!= GameSettings.GameStatus.RUNNING) return;

        int elapsedTime = game.getTotalTimeElapsed() + 10;
        int previousTime = game.getPreviousTime();

        //are at a checkpoint
        List<Line> lines = game.getLines();
        int currentPos = game.getPos()/GameSettings.segL;
        if(lines.get(currentPos).isCheckPoint && prevChekPoint  < currentPos){

            int interval = (elapsedTime - previousTime)/1000;
            game.setExtraPoints(game.getExtraPoints() + interval);

            game.setTotalTimeElapsed(elapsedTime);
            game.setPreviousTime(elapsedTime);
            prevChekPoint = currentPos + 6; // to make sure to pass the current checkpoint
            return;
        }

        if((elapsedTime - previousTime)/1000 >= GameSettings.timeRemainingPerCheckPoint + 1){
            game.setGameStatus(GameSettings.GameStatus.GAME_OVER_TIME_OUT);
            game.setSpeed(0);
            game.setAngle(0);
            System.out.println("timeOutEndingScene");
            unMountKeyListener();
            return;
        }

        game.setTotalTimeElapsed(elapsedTime);
    }
}
