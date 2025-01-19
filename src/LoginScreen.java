import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class LoginScreen {

    private static String playerName = "";
    private static String playerToken = "";
    private static String previousHighScore = "...";

    private static JFrame frame;

    private static JButton loginButton;
    private static JButton registerButton;

    private static JLabel userNameLabel;
    private static JTextField userNameComp;

    private static JLabel passwordLabel;
    private static JPasswordField passwordComp;

    private static JButton registerNewUserButton;
    private static JButton loginUserButton;

    private static JButton startGameButton;
    private static JButton logOutButton;

    private static JButton showScoreTable;

    private static JButton helpButton;

    public static void showLoginScreen() {

        AudioManager.playMusic();

        GameSettings.initFont();

        frame = new JFrame("HangOn - Login");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
//        frame.setFocusable(true);
//        frame.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {}
//
//            @Override
//            public void keyPressed(KeyEvent e) {}
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_M) {
//                    AudioManager.togglePlayStopMusic();
//                }
//            }
//        });

        ImageIcon backgroundIcon = new ImageIcon(LoginScreen.class.getResource("assets/login_background.png"));
        System.out.println(LoginScreen.class.getResource("assets/login_background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);


        loginButton = new JButton("Login");
        loginButton.setFont(GameSettings.arcadeFont);
        loginButton.setBounds(200, 400, 200, 50);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.setVisible(false);
                loginButton.setVisible(false);
                showScoreTable.setVisible(false);
                helpButton.setVisible(false);

                userNameLabel.setVisible(true);
                userNameComp.setVisible(true);
                passwordLabel.setVisible(true);
                passwordComp.setVisible(true);

                loginUserButton.setVisible(true);
            }
        });

        registerButton = new JButton("Register");
        registerButton.setFont(GameSettings.arcadeFont);
        registerButton.setBounds(200, 450, 200, 50);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.setVisible(false);
                loginButton.setVisible(false);
                showScoreTable.setVisible(false);
                helpButton.setVisible(false);

                userNameLabel.setVisible(true);
                userNameComp.setVisible(true);
                passwordLabel.setVisible(true);
                passwordComp.setVisible(true);

                registerNewUserButton.setVisible(true);
            }
        });

        userNameLabel = new JLabel("Username");
        userNameLabel.setFont(GameSettings.arcadeFont);
        userNameLabel.setForeground(Color.white);
        userNameLabel.setBounds(200, 270, 200, 50);
        userNameLabel.setVisible(false);

        //User name texfield
        userNameComp = new JTextField();
        userNameComp.setFont(GameSettings.arcadeFont);
        userNameComp.setBounds(200, 310, 200, 50);
        userNameComp.setEditable(true);
        userNameComp.setVisible(false);


        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(GameSettings.arcadeFont);
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBounds(200, 350, 200, 50);
        passwordLabel.setVisible(false);

        //password field
        passwordComp = new JPasswordField();
        passwordComp.setBounds(200, 390, 200, 50);
        passwordComp.setEditable(true);
        passwordComp.setVisible(false);


        registerNewUserButton = new JButton("Register");
        registerNewUserButton.setFont(GameSettings.arcadeFont);
        registerNewUserButton.setBounds(200, 460, 200, 50);
        registerNewUserButton.setVisible(false);
        registerNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userNameComp.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Username must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userNameComp.getText().trim().length() > 5) {
                    JOptionPane.showMessageDialog(null, "Username can't be more than 5 letters", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (passwordComp.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Password must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                }


                LoadingSpinner spinner = new LoadingSpinner();
                spinner.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                frame.add(spinner);
                frame.repaint();
                spinner.start();

                userNameLabel.setVisible(false);
                userNameComp.setVisible(false);
                passwordLabel.setVisible(false);
                passwordComp.setVisible(false);
                registerNewUserButton.setVisible(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean temp = false;
                        try {
                            temp = Request.createAnewPlayer(userNameComp.getText(), passwordComp.getText());
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }

                        spinner.stop();
                        frame.remove(spinner);

                        if (temp) SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                JOptionPane.showMessageDialog(null, "User registired", "Success", JOptionPane.INFORMATION_MESSAGE);

                                registerButton.setVisible(true);
                                loginButton.setVisible(true);
                                showScoreTable.setVisible(true);
                                helpButton.setVisible(true);

                                userNameLabel.setVisible(false);
                                userNameComp.setVisible(false);
                                passwordLabel.setVisible(false);
                                passwordComp.setVisible(false);

                                registerNewUserButton.setVisible(false);


                            }
                        });
                        else SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                userNameLabel.setVisible(true);
                                userNameComp.setVisible(true);
                                passwordLabel.setVisible(true);
                                passwordComp.setVisible(true);
                                registerNewUserButton.setVisible(true);
                            }
                        });

                        frame.repaint();
                    }
                }).start();


            }
        });

        loginUserButton = new JButton("Login");
        loginUserButton.setFont(GameSettings.arcadeFont);
        loginUserButton.setBounds(200, 460, 200, 50);
        loginUserButton.setVisible(false);
        loginUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userNameLabel.setVisible(false);
                userNameComp.setVisible(false);
                passwordLabel.setVisible(false);
                passwordComp.setVisible(false);
                loginUserButton.setVisible(false);

                LoadingSpinner spinner = new LoadingSpinner();
                spinner.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                frame.add(spinner);
                frame.repaint();
                spinner.start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayList<Request.PlayerTable> users = Request.getPlayersData();

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                spinner.stop();
                                frame.remove(spinner);
                                frame.repaint();


                                boolean found = false;
                                boolean passwordSame = false;
                                for (Request.PlayerTable user : users) {
                                    if (user.playerName.equals(userNameComp.getText())) {
                                        found = true;
                                        if (PasswordUtils.verifyPassword(passwordComp.getText(), user.password))
                                            passwordSame = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    JOptionPane.showMessageDialog(null, "Username not found", "Error", JOptionPane.ERROR_MESSAGE);

                                    userNameLabel.setVisible(true);
                                    userNameComp.setVisible(true);
                                    passwordLabel.setVisible(true);
                                    passwordComp.setVisible(true);
                                    loginUserButton.setVisible(true);

                                    return;
                                } else if (!passwordSame) {
                                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);

                                    userNameLabel.setVisible(true);
                                    userNameComp.setVisible(true);
                                    passwordLabel.setVisible(true);
                                    passwordComp.setVisible(true);
                                    loginUserButton.setVisible(true);

                                    return;
                                }

                                JOptionPane.showMessageDialog(null, "Logged in succesfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                                playerName = userNameComp.getText();
                                playerToken = PasswordUtils.generateToken(userNameComp.getText(), passwordComp.getText());

                                // user ecist and password is correct
                                registerButton.setVisible(false);
                                loginButton.setVisible(false);
                                loginUserButton.setVisible(false);

                                userNameLabel.setVisible(false);
                                userNameComp.setVisible(false);
                                passwordLabel.setVisible(false);
                                passwordComp.setVisible(false);
                                registerNewUserButton.setVisible(false);

                                startGameButton.setVisible(true);
                                logOutButton.setVisible(true);
                                showScoreTable.setVisible(true);
                                helpButton.setVisible(true);

                                frame.repaint();
                            }
                        });


                    }
                }).start();


            }
        });


        logOutButton = new JButton("Logout");
        logOutButton.setFont(GameSettings.arcadeFont);
        logOutButton.setBounds(200, 450, 200, 50);
        logOutButton.setVisible(false);
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                playerName = "";
                playerToken = "";
                previousHighScore = "...";

                userNameComp.setText("");
                passwordComp.setText("");

                logOutButton.setVisible(false);
                startGameButton.setVisible(false);

                loginButton.setVisible(true);
                registerButton.setVisible(true);
                showScoreTable.setVisible(true);
            }
        });

        startGameButton = new JButton("Start");
        startGameButton.setFont(GameSettings.arcadeFont);
        startGameButton.setBounds(200, 400, 200, 50);
        startGameButton.setVisible(false);
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousHighScore = "...";
                reAcquirePrevHighScore();

                showScoreTable.setVisible(false);
                startGameButton.setVisible(false);
                logOutButton.setVisible(false);
                helpButton.setVisible(false);

                frame.setVisible(false);
                GameLoop.startGame();
            }
        });

        showScoreTable = new JButton("Score Table");
        showScoreTable.setFont(GameSettings.arcadeFont);
        showScoreTable.setBounds(200, 350, 200, 50);
        showScoreTable.setVisible(true);
        showScoreTable.addActionListener(new ActionListener() {
                                             @Override
                                             public void actionPerformed(ActionEvent e) {
                                                 JFrame frame = new JFrame("HangOn - Score Table");
                                                 frame.setLayout(null);
                                                 frame.setSize(420, 500);
                                                 frame.setLocationRelativeTo(null);
                                                 frame.setResizable(false);
                                                 frame.setVisible(true);


                                                 LoadingSpinner spinner = new LoadingSpinner();
                                                 spinner.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                                                 frame.add(spinner);
                                                 frame.revalidate();
                                                 frame.repaint();
                                                 spinner.start();


                                                 new Thread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         List<Request.PlayerTable> users = Request.getPlayersData();

                                                         SwingUtilities.invokeLater(() -> {
                                                             spinner.stop();
                                                             frame.remove(spinner);

                                                             Collections.sort(users, new Comparator<Request.PlayerTable>() {
                                                                 @Override
                                                                 public int compare(Request.PlayerTable o1, Request.PlayerTable o2) {
                                                                     return o2.playerScore - o1.playerScore;
                                                                 }
                                                             });

                                                             JPanel contentPanel = new JPanel();
                                                             contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                                                             JScrollPane scrollPane = new JScrollPane(contentPanel);
                                                             scrollPane.setBounds(0, 0, 420, 500);


                                                             for (int i = 0; i < users.size(); i++) {
                                                                 JLabel rankLabel = new JLabel((i + 1) + "");
                                                                 JLabel nameLabel = new JLabel(users.get(i).playerName);
                                                                 JLabel scoreLabel = new JLabel("" + users.get(i).playerScore);

                                                                 rankLabel.setFont(GameSettings.arcadeFont.deriveFont(18f));  // Set size to 18pt
                                                                 nameLabel.setFont(GameSettings.arcadeFont.deriveFont(18f));
                                                                 scoreLabel.setFont(GameSettings.arcadeFont.deriveFont(18f));

                                                                 rankLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                                                                 nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                                                                 scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                                                                 JPanel rowPanel = new JPanel(new GridLayout(1, 3));
                                                                 rowPanel.add(rankLabel);
                                                                 rowPanel.add(nameLabel);
                                                                 rowPanel.add(scoreLabel);

                                                                 contentPanel.add(rowPanel);

                                                                 contentPanel.add(Box.createVerticalStrut(10));
                                                             }

                                                             frame.add(scrollPane);
                                                             frame.revalidate();
                                                             frame.repaint();
                                                         });
                                                     }
                                                 }).start();


                                             }
                                         }
        );


        helpButton = new JButton("Help");
        helpButton.setFont(GameSettings.arcadeFont);
        helpButton.setBounds(200, 500, 200, 50);
        helpButton.setVisible(true);
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Game is played with arrow keys, press P if you want to pause the game.\nYou need to login first to play, good luck!");
            }
        });


        backgroundLabel.add(loginButton);
        backgroundLabel.add(registerButton);

        backgroundLabel.add(userNameLabel);
        backgroundLabel.add(userNameComp);

        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(passwordComp);

        backgroundLabel.add(registerNewUserButton);
        backgroundLabel.add(loginUserButton);

        backgroundLabel.add(startGameButton);
        backgroundLabel.add(logOutButton);

        backgroundLabel.add(showScoreTable);

        backgroundLabel.add(helpButton);


        frame.setContentPane(backgroundLabel);
        frame.setVisible(true);

    }

    public static void setLoginVisible() {
        frame.setVisible(true);
    }

    public static void setScore(int score) {

        LoadingSpinner spinner = new LoadingSpinner();
        spinner.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(spinner);
        frame.revalidate();
        frame.repaint();
        spinner.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean temp = false;

                try {
                    temp = Request.updatePlayerScore(playerName, playerToken, score);

                } catch (UnsupportedEncodingException e) {
                    JOptionPane.showMessageDialog(frame, "Something went wrong and we couldn't update your score.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                spinner.stop();
                frame.repaint();
                frame.remove(spinner);
                showScoreTable.setVisible(true);
                startGameButton.setVisible(true);
                logOutButton.setVisible(true);
                helpButton.setVisible(true);


                if (temp)
                    JOptionPane.showMessageDialog(frame, "Your score has been updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

    public static void reAcquirePrevHighScore() {
        LoadingSpinner spinner = new LoadingSpinner();
        spinner.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(spinner);
        frame.revalidate();
        frame.repaint();
        spinner.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                spinner.stop();
                frame.remove(spinner);
                frame.repaint();

                ArrayList<Request.PlayerTable> users = Request.getPlayersData();
                for (Request.PlayerTable player : users) {
                    if (player.playerName.equals(playerName)) {
                        previousHighScore = "" + player.playerScore;
                    }
                }
            }
        }).start();

    }

    public static String getPreviousHighScore() {
        return previousHighScore;
    }


}
