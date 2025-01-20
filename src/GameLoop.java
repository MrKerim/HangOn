import javax.swing.*;
import java.awt.event.*;

public class GameLoop {
    private static boolean gameIsRunning = false;
    private static Timer timer;
    private static Game game;
    private static JFrame frame;
    private static GameUpdate gameUpdate;
    private static HoldDownKeyListener holdDownKeyListener;

    private static boolean isGamePaused;

    public static void startGame(){
        gameIsRunning = true;
        isGamePaused = false;
        frame = new JFrame("HangOn");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GameSettings.with, GameSettings.height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        game = new Game();
        game.setBounds(0, 0, GameSettings.with, GameSettings.height);
        frame.getContentPane().add(game);

        // adding keylistener
        holdDownKeyListener = new HoldDownKeyListener();
        game.addKeyListener(holdDownKeyListener);
        game.setFocusable(true);

        // game update
        gameUpdate = new GameUpdate(game,holdDownKeyListener);

        // Game Loop
        timer = new Timer(10, gameUpdate);

        timer.start();
    }

    public static void endGame(){

        int score = game.getPos() + game.getExtraPoints()*GameSettings.segL*40;
        frame.dispose();
        game = null;
        timer.stop();
        timer = null;
        gameIsRunning = false;
        holdDownKeyListener = null;
        gameUpdate = null;

        JOptionPane.showMessageDialog(null,"Your score was " + score);
        LoginScreen.setLoginVisible();
        LoginScreen.setScore(score);

    }

    public static boolean isGameRunning() {
        return gameIsRunning;
    }

    public static boolean isGamePaused() {
        return isGamePaused;
    }

    public static void togglePauseGame(){
        if(game.getGameStatus() != GameSettings.GameStatus.RUNNING) return;
        isGamePaused = !isGamePaused;
        game.repaint();
        if(isGamePaused())addButtonsOnPause();
        else removeButtonsOnPause();
    }

    public static void addButtonsOnPause(){
        JButton resumeButton = new JButton("RESUME");
        resumeButton.setFont(GameSettings.arcadeFont.deriveFont(30f));
        resumeButton.setBounds(700,300,200,70);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePauseGame();
            }
        });

        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(GameSettings.arcadeFont.deriveFont(30f));
        exitButton.setBounds(700,370,200,70);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });


        game.add(exitButton);
        game.add(resumeButton);
    }

    public static void removeButtonsOnPause(){
        game.removeAll();
    }



}
