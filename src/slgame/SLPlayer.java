package slgame;

import javafx.animation.TranslateTransition;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.*;

import java.net.URL;
import java.util.*;


public class SLPlayer extends Circle {

    // instance
    private int x;
    private int y;
    private String id;
    private int rowNum = 1;
    private int locationNbr = 1;
    private AudioClip snakeSound;
    private AudioClip jumpSound;
    URL snakeFile = ClassLoader.getSystemResource("snakeSound.wav");
    URL jumpFile = ClassLoader.getSystemResource("jumpSound.wav");

    private static record BoardPoint(int x, int y, int row) {}
    private static Map<Integer, BoardPoint> endPoints = new HashMap<>();

    public SLPlayer(String id, int x, int y, Color color) {
        super(24);
        this.id = id;
        setFill(color);
        setXY(x, y);
        try {
            snakeSound = new AudioClip(String.valueOf(snakeFile.toURI()));
            jumpSound = new AudioClip(String.valueOf(jumpFile.toURI()));
        } catch(Exception e) {
            e.printStackTrace();
        }

        // ladders
        endPoints.put(12, new BoardPoint(510, 510, 2));
        endPoints.put(39, new BoardPoint(90, 390, 4));
        endPoints.put(53, new BoardPoint(450, 270, 6));
        endPoints.put(84, new BoardPoint(210, 90, 9));
        endPoints.put(90, new BoardPoint(570, 90, 9));
        endPoints.put(99, new BoardPoint(90, 30, 10));
        // snakes
        endPoints.put(4, new BoardPoint(210, 570, 1));
        endPoints.put(13, new BoardPoint(450, 510, 2));
        endPoints.put(25, new BoardPoint(270, 450, 3));
        endPoints.put(52, new BoardPoint(510, 270, 6));
        endPoints.put(60, new BoardPoint(30, 270, 6));
        endPoints.put(75, new BoardPoint(330, 150, 8));
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        setTranslateX(x);
        setTranslateY(y);
        if(x == 30 && y == 570) {
            rowNum = 1;
            locationNbr = 1;
        }
    }

    public void processRoll(int dieRoll) {
        if(y == 30 && x < 570) {
            if(x == 330 && dieRoll > 5) return; // tile 95
            if(x == 270 && dieRoll > 4) return; // tile 96
            if(x == 210 && dieRoll > 3) return; // tile 97
            if(x == 150 && dieRoll > 2) return; // tile 98
            if(x == 90 && dieRoll > 1) return; // tile 99
        }
        for(int i = 0; i < dieRoll; i++) {
            if(rowNum % 2 == 1) {
                x += 60;
            }
            if(rowNum % 2 == 0) {
                x-=60;
            }
            if(x > 570) { // at right side
                y -= 60;
                x -= 60;
                rowNum++;
            }
            else if(x < 30) { //left side
                y -= 60;
                x += 60;
                rowNum++;
            }
        } // end loop
        locationNbr += dieRoll;
    }

    public void move() {
        TranslateTransition animate  =
                new TranslateTransition(Duration.millis(1000), this);
        animate.setToX(x);
        animate.setToY(y);
        animate.play();

    }
    public boolean isWinner() {
        return(x == 30 && y == 30);
    }

    public void check4Desitination() {
        if(locationNbr == 3) moveTo(39);
        else if(locationNbr == 10) moveTo(12);
        else if(locationNbr == 16) moveTo(13);
        else if(locationNbr == 27) moveTo(53);
        else if(locationNbr == 31) moveTo(4);
        else if(locationNbr == 47) moveTo(25);
        else if(locationNbr == 56) moveTo(84);
        else if(locationNbr == 60) moveTo(63);
        else if(locationNbr == 66) moveTo(52);
        else if(locationNbr == 61) moveTo(99);
        else if(locationNbr == 72) moveTo(90);
        else if(locationNbr == 97) moveTo(75);

    }

    private void moveTo(int tileNumber) {
        BoardPoint point = endPoints.get(tileNumber);
        setXY(point.x, point.y);
        if(tileNumber > locationNbr)
            jumpSound.play();
        else
            snakeSound.play();
        rowNum = point.row;
        locationNbr = tileNumber;
    }

   /* public void check4Ladder() {
        if(x == 150 && y == 570) { // 3 to 39
            setXY(90, 390);
            rowNum = 4;
            jumpSound.play();
        }
        else if(x == 570 && y == 570) { // 10 to 12
            setXY(510, 510);
            rowNum = 2;
            jumpSound.play();
        }
        else if(x == 390 && y == 450) { // 27 to 53
            setXY(450, 270);
            rowNum = 6;
            jumpSound.play();
        }
        else if(x == 270 && y == 270) { // 56 to 84
            setXY(210, 90);
            rowNum = 7;
            jumpSound.play();
        }
        else if(x == 30 && y == 210) { // 61 to 99
            setXY(90, 30);
            rowNum = 10;
            jumpSound.play();
        }
        else if(x == 510 && y == 150) { // 72 to 90
            setXY(570, 90);
            rowNum = 9;
            jumpSound.play();
        }
    }


    public void check4Snake() {
        if(x == 210 && y == 570) { // 4 to 31
                setXY(570, 390);
                rowNum = 4;
                snakeSound.play();
            }
        else if(x == 570 && y == 390) { // 31 to 4
                setXY(210, 570);
                rowNum = 1;
                snakeSound.play();
            }
        else if(x == 450 && y == 510) { // 13 to 16
                setXY(270, 510);
                rowNum = 2;
                snakeSound.play();
            }
        else if(x == 270 && y == 510) { // 16 to 13
            setXY(450, 510);
            rowNum = 2;
            snakeSound.play();
            }
        else if(x == 270 && y == 450) { // 25 to 47
            setXY(390, 330);
            rowNum = 5;
            snakeSound.play();
            }
        else if(x == 390 && y == 330) { // 47 to 25
            setXY(270, 450);
            rowNum = 3;
            snakeSound.play();
           }
        else if(x == 510 && y == 270) { // 52 to 66
            setXY(330, 210);
            rowNum = 7;
            snakeSound.play();
          }
        else if(x == 330 && y == 210) { // 66 to 52
            setXY(510, 270);
            rowNum = 6                                                                  ;
            snakeSound.play();
          }
        else if(x == 30 && y == 270) { // 60 to 63
            setXY(150, 210);
            rowNum = 7;
            snakeSound.play();
        }
        else if(x == 150 && y == 210) { // 63 to 60
            setXY(30, 270);
            rowNum = 6;
            snakeSound.play();
        }
        else if(x == 330 && y == 150) { // 75 to 97
            setXY(210, 30);
            rowNum = 10;
            snakeSound.play();
        }
        else if(x == 210 && y == 30) { // 97 to 75
            setXY(330, 150);
            rowNum = 7;
            snakeSound.play();
        }

    } */

    public String toString() {
        return id;
    }
}
