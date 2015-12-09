package gridworld;

import java.awt.*;
import java.util.Random;


public class BlindMouse {
    private Random random = new Random();
    private Point blindMouseLocation = new Point(-1,-1);
    public BlindMouse(){}

    public void setBlindMouseLocation(){
        blindMouseLocation.setLocation(random.nextInt(20),random.nextInt(20));
    }

    public Point getBlindMouseLocation(){
        return blindMouseLocation;
    }

    public void move(String direction){
        switch (direction){
            case "UP":
                moveUp();
                break;
            case "DOWN":
                moveDown();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "RIGHT":
                moveRight();
                break;
        }
    }

    private void moveUp(){
        if(blindMouseLocation.y > 0){
            blindMouseLocation.y -=1;
        }
    }
    private void moveDown(){
        if(blindMouseLocation.y < 19) {
            blindMouseLocation.y += 1;
        }
    }
    private void moveLeft(){
        if(blindMouseLocation.x > 0) {
            blindMouseLocation.x -= 1;
        }
    }
    private void moveRight(){
        if(blindMouseLocation.x < 19) {
            blindMouseLocation.x += 1;
        }
    }

}
