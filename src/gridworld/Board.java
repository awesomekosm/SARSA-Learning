package gridworld;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Square[][] boardArrayQ = new Square[20][20];
    private Square[][] boardArrayE = new Square[20][20];
    private Point rewardLocation = new Point();
    private ArrayList<Point> wallLocation  = new ArrayList<>();
    private Random random = new Random();

    public Point getRewardLocation() {
        return rewardLocation;
    }

    public ArrayList<Point> getWallLocation(){
        return wallLocation;
    }

    public Square[][] getBoardArrayQ() {
        return boardArrayQ;
    }

    public Square[][] getBoardArrayE() {
        return boardArrayE;
    }

    public Board() {
        initBoards();
    }

    private void initBoards() {
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                boardArrayQ[x][y] = new Square();
                boardArrayQ[x][y].setDown(random.nextDouble());
                boardArrayQ[x][y].setUp(random.nextDouble());
                boardArrayQ[x][y].setLeft(random.nextDouble());
                boardArrayQ[x][y].setRight(random.nextDouble());
                boardArrayQ[x][y].setValue(0.0);

                boardArrayE[x][y] = new Square();
            }
        }
    }

    public void resetBoardE(){
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                boardArrayQ[x][y].setDown(random.nextDouble());
                boardArrayQ[x][y].setUp(random.nextDouble());
                boardArrayQ[x][y].setLeft(random.nextDouble());
                boardArrayQ[x][y].setRight(random.nextDouble());
            }
        }
    }

    public void setRewardLocation(int x, int y, double value) {
        if (x < 20 && y < 20) {
            boardArrayQ[x][y].setValue(value);
            rewardLocation.setLocation(x, y);
        } else {
            System.out.print("x and y out of bound");
        }
    }

    public void setBlankLocationValue(int x, int y, double value) {
        if (x < 20 && y < 20) {
            boardArrayQ[x][y].setValue(value);
        } else {
            System.out.print("x and y out of bound");
        }
    }

    public void setWallLocation(int x, int y, double value) {
        if (x < 20 && y < 20) {
            boardArrayQ[x][y].setValue(value);
            wallLocation.add(new Point(x, y));
        } else {
            System.out.print("x and y out of bound");
        }
    }
    public void setClearLocation(int x,int y, double value){
        if (x < 20 && y < 20) {
            boardArrayQ[x][y].setValue(value);
        } else {
            System.out.print("x and y out of bound");
        }
    }

    public void resetBoardArrayE(){
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                boardArrayE[x][y].setDown(0.0);
                boardArrayE[x][y].setUp(0.0);
                boardArrayE[x][y].setLeft(0.0);
                boardArrayE[x][y].setRight(0.0);
                boardArrayE[x][y].setValue(0.0);
            }
        }
    }
    public void resetBoardArrayQ(){
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                boardArrayQ[x][y].setDown(random.nextDouble());
                boardArrayQ[x][y].setUp(random.nextDouble());
                boardArrayQ[x][y].setLeft(random.nextDouble());
                boardArrayQ[x][y].setRight(random.nextDouble());
                boardArrayQ[x][y].setValue(0.0);
            }
        }
    }

}
