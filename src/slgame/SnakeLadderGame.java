package slgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

public class SnakeLadderGame extends Application {

    private Pane root = new Pane();
    private ImageView bgImage;
    private SLPlayer p1;
    private SLPlayer p2;
    private Button p1Btn;
    private Button p2Btn;
    private Button startBtn;
    private Button hiddenBoii = new Button("Heh");
    private boolean p1Turn = true;
    private boolean p2Turn = true;
    private boolean gameStart = false;
    private Image[] faces;
    private Label dieFace;
    private AudioClip winSound;

    private Parent createContent() {
        root.setPrefSize(600, 635);
        root.setPadding(new Insets(10));
        p1 = new SLPlayer("Player 1", 30, 570, Color.BLACK);
        p2 = new SLPlayer("Player 2", 90, 570, Color.BROWN);
        URL boardImage = ClassLoader.getSystemResource("snakebg.jpg");
        bgImage = new ImageView(new Image(boardImage.toString()));
        bgImage.setFitHeight(596.0);
        bgImage.setFitWidth(596.0);

        URL winFile = ClassLoader.getSystemResource("Supermariowin.wav");
        try {
            winSound = new AudioClip(winFile.toURI().toString());
        } catch(Exception e) {
            e.printStackTrace();
        }

        faces = new Image[6];
        for(int i = 0; i < 6; i++) {
            String file = "d%d.jpg".formatted(i + 1);
            faces[i] = new Image(ClassLoader.getSystemResource(file).toString());
        }

        p1Btn = new Button("Player 1");
        p1Btn.setOnAction(e -> {
            if(gameStart) {
                if(p1Turn) {
                    p1.processRoll(getDieRoll());
                    p1.check4Desitination();
                    p1.move();
                    p1Turn = false;
                    p2Turn = true;
                    p2Btn.requestFocus();
                    if(p1.isWinner()) winnerIs(p1);
                }
            }
        });
        p2Btn = new Button("Player 2");
        p2Btn.setOnAction(e -> {
            if(gameStart) {
                if(p2Turn) {
                    p2.processRoll(getDieRoll());
                    p2.check4Desitination();
                    p2.move();
                    p2Turn = false;
                    p1Turn = true;
                    p1Btn.requestFocus();
                    if(p2.isWinner()) winnerIs(p2);
                }
            }
        });
        startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
            if(gameStart == false) {
                gameStart = true;
                startBtn.setText("Game started");
                startBtn.setDisable(true);
                dieFace.setText("");
                p1Btn.requestFocus();
                p1.setXY(30, 570);
                p2.setXY(30, 570);
                p1Turn = true;
                p2Turn = true;
            }
        }); // end startBtn
        p1Btn.setTranslateX(30);
        p1Btn.setTranslateY(605);

        dieFace = new Label();
        dieFace.setTranslateX(150);
        dieFace.setTranslateY(605);

        startBtn.setTranslateX(260);
        startBtn.setTranslateY(605);

        p2Btn.setTranslateX(510);
        p2Btn.setTranslateY(605);

        // ArrowKeys();

        hiddenBoii.setTranslateX(-30);
        hiddenBoii.setTranslateY(-30);

        hiddenBoii.setOnAction(e ->{
            System.out.println("You found me\nNOW PAY FOR THE CONCEQUENCES!"); // add some fuuny things
        });

        root.getChildren().addAll(bgImage, p1, p2, p1Btn, dieFace, startBtn, p2Btn, hiddenBoii);
        return root;
    }

    private int getDieRoll() {
        int roll = (int) (Math.random()*6+1);
        ImageView image = new ImageView(faces[roll - 1]);
        image.setFitWidth(24);
        image.setFitHeight(24);
        dieFace.setGraphic(image);
        return roll;
    }

    private void winnerIs(SLPlayer player) {
        winSound.play();
        gameStart = false;
        dieFace.setText(String.format("%s wins", player));
        startBtn.setDisable(false);
        startBtn.setText("Start Again");
        // startBtn.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Snake And Ladder Game");
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }
    public void hiddenBoiiMethod() {

    }


    public void ArrowKeys() {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setFocusable(true);

        JPanel panel = new JPanel();
        JLabel up = new JLabel();
        JLabel down = new JLabel();
        JLabel left = new JLabel();
        JLabel right = new JLabel();

        panel.add(up);
        panel.add(down);
        panel.add(right);
        panel.add(left);

        up.setText("Click here to activate arrow keys");
        // down.setText("down: 0");
        // left.setText("left: 0");
        // right.setText("right: 0");

        frame.addKeyListener(new KeyListener() {
            int upCount = 0;
            int downCOunt = 0;
            int rightCount = 0;
            int leftCount = 0;

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        if(gameStart) {
                            if(p1Turn) {
                                p1.processRoll(getDieRoll());
                                p1.move();
                                p1Turn = false;
                                p2Turn = true;
                            }
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(gameStart) {
                            if(p2Turn) {
                                p2.processRoll(getDieRoll());
                                p2.move();
                                p2Turn = false;
                                p1Turn = true;
                            }
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.add(panel);
    }

} // end of everything


