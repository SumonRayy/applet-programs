package squidgame;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings({ "deprecation" })
public class SquidGame extends Applet implements ActionListener, Runnable {

    Label player, lineLabel, lightLabel;
    Image fgBgPic, winBoxPic, loseBoxPic, bgPicture;
    AudioClip dollSound, gunSound, bgSound;
    Button forwardButton, startButton, exitButton, restartButton;

    int stepCounter = 600, lightCounter = 0;
    Boolean gameOver = false, gameStarted = false, lightColorRedFlag = true;
    String playerName = "456", red = "RedLight", green = "GreenLight";
    Random rand = new Random();
    String now = "";

    Color playerColor = new Color(32, 178, 169);
    Color redLight = new Color(255, 0, 0);
    Color greenLight = new Color(0, 255, 0);

    Font btnFont = new Font("Courier", Font.PLAIN, 30);
    Font playerFont = new Font("Courier", Font.PLAIN, 20);
    Font lightFont = new Font("Courier", Font.PLAIN, 30);

    public void init() {

        fgBgPic = getImage(getDocumentBase(), "assets/images/fg_bg.jpg");
        winBoxPic = getImage(getDocumentBase(), "assets/images/winBox.jpg");
        loseBoxPic = getImage(getDocumentBase(), "assets/images/loseBox.jpg");
        bgPicture = getImage(getDocumentBase(), "assets/images/sg.jpg");
        dollSound = getAudioClip(getDocumentBase(), "assets/sounds/doll.wav");
        gunSound = getAudioClip(getDocumentBase(), "assets/sounds/gun.wav");
        bgSound = getAudioClip(getDocumentBase(), "assets/sounds/sg_theme.wav");

        player = new Label(playerName, Label.CENTER);
        lightLabel = new Label(green, Label.CENTER);
        lineLabel = new Label("");

        forwardButton = new Button("Forward");
        startButton = new Button("Click to Play!");
        restartButton = new Button("Play Again!");
        exitButton = new Button("Exit");

        forwardButton.setFont(btnFont);
        forwardButton.setBounds(500, 550, 150, 70);
        startButton.setFont(btnFont);
        startButton.setBounds(250, 550, 280, 60);
        restartButton.setFont(btnFont);
        restartButton.setBounds(250, 520, 280, 60);
        exitButton.setFont(btnFont);
        exitButton.setBounds(250, 620, 280, 60);

        player.setFont(playerFont);
        player.setBackground(playerColor);
        player.setForeground(Color.white);
        player.setBounds(100, stepCounter, 45, 45);

        lineLabel.setBackground(Color.WHITE);
        lineLabel.setBounds(35, 250, 650, 2);

        lightLabel.setBackground(greenLight);
        lightLabel.setFont(lightFont);
        lightLabel.setForeground(Color.WHITE);
        lightLabel.setBounds(450, 170, 200, 60);

        add(startButton);
        add(restartButton);
        add(exitButton);

        forwardButton.addActionListener(this);
        startButton.addActionListener(this);
        restartButton.addActionListener(this);
        exitButton.addActionListener(this);

        setLayout(null);
    }

    public void paint(Graphics g) {
        if (gameOver) {
            if (stepCounter > 200) {
                remove(player);
                gunSound.play();
                g.drawImage(fgBgPic, 10, 10, this);
                g.drawImage(loseBoxPic, 60, 300, this);
                restartButton.setVisible(true);
                exitButton.setVisible(false);
            } else {
                dollSound.stop();
                bgSound.play();
                g.drawImage(fgBgPic, 10, 10, this);
                g.drawImage(winBoxPic, 60, 300, this);
                restartButton.setVisible(true);
                exitButton.setVisible(true);
            }
        } else {
            if (gameStarted == false) {
                bgSound.loop();
                g.drawImage(bgPicture, 10, 10, this);
                restartButton.setVisible(false);
                exitButton.setVisible(false);
            } else {
                bgSound.stop();
                g.drawImage(fgBgPic, 10, 10, this);
                restartButton.setVisible(false);
                exitButton.setVisible(false);
            }
        }
    }

    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(4670);
                if (!gameStarted || gameOver) {
                    continue;
                }
                if (lightColorRedFlag) {
                    dollSound.stop();
                    lightLabel.setText(red);
                    lightLabel.setBackground(redLight);
                    lightColorRedFlag = false;
                } else {
                    dollSound.play();
                    lightLabel.setText(green);
                    lightLabel.setBackground(greenLight);
                    lightColorRedFlag = true;
                }
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            gameStarted = true;
            startButton.setVisible(false);
            add(player);
            add(lineLabel);
            add(forwardButton);
            add(lightLabel);
            repaint();
        } else if (e.getSource() == forwardButton) {
            if (stepCounter <= 201) {
                gameOver = true;
                remove(forwardButton);
                repaint();
            } else {
                if (!lightColorRedFlag) {
                    stepCounter -= 10;
                    player.setBounds(100, stepCounter, 45, 45);
                    gameOver = true;
                    remove(forwardButton);
                    repaint();
                } else {
                    stepCounter -= 10;
                    player.setBounds(100, stepCounter, 45, 45);
                }

            }
        } else if (e.getSource() == restartButton) {
            gameOver = false;
            gameStarted = false;
            stepCounter = 600;
            player.setBounds(100, stepCounter, 45, 45);
            remove(player);
            remove(lineLabel);
            remove(forwardButton);
            remove(lightLabel);
            restartButton.setVisible(false);
            exitButton.setVisible(false);
            startButton.setVisible(true);
            repaint();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

}