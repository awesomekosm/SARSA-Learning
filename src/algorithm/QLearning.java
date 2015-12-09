package algorithm;

import gridworld.BlindMouse;
import gridworld.Board;
import gui.ConfigController;
import gui.Frame;
import gui.Panel;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;



public class QLearning {
    private Point rewardLocation = new Point(9,13);
    private Point oldRewardLocation = new Point(-1,-1);
    private double wallsReward = -1.0;
    private double prizeReward = 1.0;
    private int numberOfWalls = 8;
    private int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    private int delay;
    private Random random = new Random();
    private Board board = new Board();
    private Frame frame = new Frame();
    private ConfigController configController;
    private Panel panel;
    private Clip clip;
    private BlindMouse blindMouse = new BlindMouse();
    private ImageIcon directionIcon[] = new ImageIcon[4];
    private String possibleDirections[] = {"UP", "DOWN", "LEFT", "RIGHT"};
    private boolean mainMusicToggle = true;

    //q-learning
    private double learningRate = 0.5;
    private double epsilon = 0.9;
    private double sarsa = 0.9;
    private double gamma = 0.8;

    private int episodeCounter = 0;
    private int episodeStepCounter = 0;
    private int hitAWallCounter = 0;

    public QLearning() {
        assignIcons();
        //enable sound
        loadSound();
        generateWalls(numberOfWalls);
        drawOrientationBasedOnWeight();
        panel = frame.getPanel();
        initConfig();
        musicToggle();
        //enable listeners
        randomizeWalls();
        speedControl();
        //run Q(s,a)
        loop();
    }

    private void initConfig(){
        configController = new ConfigController(panel);
        configController.setRewardLocX(rewardLocation.x);
        configController.setRewardLocY(rewardLocation.y);
        configController.setNumberOfWalls(numberOfWalls);
        configController.setMusicOn(false);
    }

    public boolean uiControl(){
        boolean running;
        //stopped can randomize mouse and wall location
        if (delay == 0) {
            panel.getRandomizeWorld().setEnabled(true);
            running = false;
        }
        else {
            panel.getRandomizeWorld().setEnabled(false);
            running = true;
        }

        return running;
    }

    public void decayEpsilon(double counter){
        if(counter%200==0){
            epsilon = epsilon*0.7;
        }
    }
    public void loop() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    //initializeStateAction
                    setMouseLocationRandom();
                    clearVisitedLocations();
                    setRewardLoc(rewardLocation);
                    episodeStepCounter = 0;
                    //Q-Learning Here
                    try {
                        loopUntilReward();
                    }catch(Exception e){
                        setTextArea("Resetting mouse: "+e.getMessage());
                    }
                    decayEpsilon(episodeCounter);
                    episodeCounter++;
                }
            }
        };
        thread.start();
    }
    public double nextBestActionByGreedy(){
        return board.getBoardArrayQ()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getHighestWeightByDirection();
    }
    //epsilon decays over time, 1/10 picks move based on weight 9/10 times at random. as the algorithm progresses epsilon 1/10 increases
    public String randGreedyMove(double epsilon){
        if(random.nextDouble()<epsilon){
            return possibleDirections[random.nextInt(4)];
        }else{
            return board.getBoardArrayQ()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getLargest()==null?possibleDirections[random.nextInt(4)]:board.getBoardArrayQ()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getLargest();
        }
    }
    public double currentStateMouseReward(){
        return board.getBoardArrayQ()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getValue();
    }

    public void loopUntilReward(){
        while(board.getBoardArrayQ()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getValue() !=1){
            if(uiControl()) {
                //mouse Q(s,a) location
                Point oldMouseLocation = new Point(blindMouse.getBlindMouseLocation());
                //used to resetMouse incase it hits a wall
                double rewardBeforeMove = board.getBoardArrayQ()[oldMouseLocation.x][oldMouseLocation.y].getValue();
                if(rewardBeforeMove < 0){
                    hitAWallCounter++;
                    break;
                }
                String actionDirection = randGreedyMove(epsilon);
                blindMouse.move(actionDirection);
                //value of action taken by mouse at a
                double bestActionTakenVal = board.getBoardArrayQ()[oldMouseLocation.x][oldMouseLocation.y].getWeightByDirection(actionDirection);
                //best possible value a mouse should take next at a'
                double nextBestActionNotTakenVal = nextBestActionByGreedy();
                double delta = currentStateMouseReward() + 0.9 * nextBestActionNotTakenVal - bestActionTakenVal;
                //set visited for e(s,a)
                board.getBoardArrayE()[oldMouseLocation.x][oldMouseLocation.y].setWeightForDirection(actionDirection, 1.0);

                //loop all values in boardQ and change value of the actions based on the action taken above
                for (int x = 0; x < 20; x++) {
                    for (int y = 0; y < 20; y++) {
                        double adjustmentForDirection = board.getBoardArrayQ()[x][y].getWeightByDirection(actionDirection)+learningRate*delta*board.getBoardArrayE()[x][y].getWeightByDirection(actionDirection);
                        board.getBoardArrayQ()[x][y].setWeightForDirection(actionDirection,adjustmentForDirection);
                        //decay
                        double adjustmentForDecay = board.getBoardArrayE()[x][y].getWeightByDirection(actionDirection)*sarsa*gamma;
                        board.getBoardArrayE()[x][y].setWeightForDirection(actionDirection,adjustmentForDecay);
                    }
                }
                setTextArea("Episode Number: "+Integer.toString(episodeCounter)+ "\nEpisode Step: "+Integer.toString(episodeStepCounter)+"\nHit a Wall: "+Integer.toString(hitAWallCounter));
                episodeStepCounter++;
                drawOrientationBasedOnWeight();
                drawMouseMovement(blindMouse.getBlindMouseLocation().x, blindMouse.getBlindMouseLocation().y, oldMouseLocation.x, oldMouseLocation.y);
                reDrawWalls();

                try {Thread.sleep(300 - delay);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }
        board.resetBoardArrayE();
    }

    public void drawMouseMovement(int newMoveX,int newMoveY,int oldMoveX, int oldMoveY){
        //remove mouse draw from it's original location
        frame.paintButtonLoc(oldMoveX, oldMoveY, null);
        frame.getPanel().getMarked().push(new Point(oldMoveX, oldMoveY));
        frame.getPanel().getMarked().push(new Point(newMoveX, newMoveY));
        //paint new mouse location
        frame.paintButtonLoc(newMoveX, newMoveY, new Color(34, 139, 34));
        //paint move trail
        frame.paintButtonLoc(oldMoveX, oldMoveY, new Color(169, 181, 125));
    }
    public void reDrawWalls(){
        for (int i = 0; i < board.getWallLocation().size(); i++) {
            drawWalls(board.getWallLocation().get(i).x, board.getWallLocation().get(i).y);
        }
    }

    public void clearVisitedLocations(){
        for (Point key : frame.getPanel().getMarked()) {
            frame.paintButtonLoc(key.x,key.y,null);
        }
        frame.getPanel().getMarked().clear();
    }

    private void assignIcons() {
        directionIcon[UP] = loadIcons("./Resources/up.png");
        directionIcon[DOWN] = loadIcons("./Resources/down.png");
        directionIcon[LEFT] = loadIcons("./Resources/left.png");
        directionIcon[RIGHT] = loadIcons("./Resources/right.png");
    }
    public void loadSound() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("./Resources/yakety.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private ImageIcon loadIcons(String fileLoc) {
        try {
            ImageIcon iIcon = new ImageIcon(ImageIO.read(new File(fileLoc)));
            Image image = iIcon.getImage();
            Image scaledImage = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            return new ImageIcon();
        } catch (NullPointerException e){
            return new ImageIcon();
        }
    }

    public void  drawOrientationBasedOnWeight() {
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if(board.getBoardArrayQ()[x][y].getValue() != wallsReward && board.getBoardArrayQ()[x][y].getValue() != prizeReward) {
                    String tempOrientation = board.getBoardArrayQ()[x][y].getLargest()==null?" ":board.getBoardArrayQ()[x][y].getLargest();
                    switch (tempOrientation) {
                        case "UP":
                            frame.getPanel().getButtonArray()[x][y].setIcon(directionIcon[UP]);
                            break;
                        case "DOWN":
                            frame.getPanel().getButtonArray()[x][y].setIcon(directionIcon[DOWN]);
                            break;
                        case "LEFT":
                            frame.getPanel().getButtonArray()[x][y].setIcon(directionIcon[LEFT]);
                            break;
                        case "RIGHT":
                            frame.getPanel().getButtonArray()[x][y].setIcon(directionIcon[RIGHT]);
                            break;
                        default:
                            frame.getPanel().getButtonArray()[x][y].setIcon(null);
                            break;
                    }
                }else{
                    frame.getPanel().getButtonArray()[x][y].setIcon(null);
                }
            }
        }
    }

    public void setRewardLoc(Point point) {
        if(oldRewardLocation.x!=-1 && oldRewardLocation.y !=-1){
            frame.paintButtonLoc(oldRewardLocation.x, oldRewardLocation.y, null);
            board.setBlankLocationValue(oldRewardLocation.x, oldRewardLocation.y, 0);
            if(point.x != oldRewardLocation.x || point.y != oldRewardLocation.y){
                board.resetBoardArrayE();
                episodeCounter = 0;
                episodeStepCounter = 0;
                hitAWallCounter = 0;
            }
        }
        oldRewardLocation.setLocation(point);
        frame.paintButtonLoc(point.x, point.y, Color.RED);
        board.setRewardLocation(point.x, point.y, prizeReward);
    }

    public void generateWalls(int numberOfWalls) {
        Random rand = new Random();
        int initWallLocationX[] = new int[numberOfWalls];
        int initWallLocationY[] = new int[numberOfWalls];
        for (int i = 0; i < initWallLocationX.length; i++) {
            initWallLocationX[i] = rand.nextInt(17 - 3) + 3;
            initWallLocationY[i] = rand.nextInt(17 - 3) + 3;
            if (Math.random() < 0.5) {
                setWalls(initWallLocationX[i], initWallLocationY[i] + 1);
                setWalls(initWallLocationX[i], initWallLocationY[i] + 2);
                setWalls(initWallLocationX[i], initWallLocationY[i] + 3);
                setWalls(initWallLocationX[i] + 1, initWallLocationY[i] + 1);
                setWalls(initWallLocationX[i] + 2, initWallLocationY[i] + 1);
            } else {
                setWalls(initWallLocationX[i], initWallLocationY[i] + 1);
                setWalls(initWallLocationX[i], initWallLocationY[i]);
                setWalls(initWallLocationX[i], initWallLocationY[i] - 1);
                setWalls(initWallLocationX[i] - 1, initWallLocationY[i] + 1);
                setWalls(initWallLocationX[i] - 2, initWallLocationY[i] + 1);
            }
        }
    }

    public void setMouseLocationRandom() {
        //remove previous mouse spawn location
        if (blindMouse.getBlindMouseLocation().x != -1 && blindMouse.getBlindMouseLocation().y != -1) {
            frame.paintButtonLoc(blindMouse.getBlindMouseLocation().x, blindMouse.getBlindMouseLocation().y, null);
        }
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if(frame.getPanel().getButtonArray()[x][y].getBackground()==new Color(34, 139, 34)){
                    frame.paintButtonLoc(blindMouse.getBlindMouseLocation().x, blindMouse.getBlindMouseLocation().y, null);
                }
            }
        }
        //check if mouse spawned in good spot
        boolean gotRightSpawn = false;
        while (!gotRightSpawn) {
            blindMouse.setBlindMouseLocation();
            if (frame.getPanel().getButtonArray()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getBackground() != Color.BLACK &&
                    frame.getPanel().getButtonArray()[blindMouse.getBlindMouseLocation().x][blindMouse.getBlindMouseLocation().y].getBackground() != Color.RED) {
                gotRightSpawn = true;
            }
        }
        frame.paintButtonLoc(blindMouse.getBlindMouseLocation().x, blindMouse.getBlindMouseLocation().y, new Color(34, 139, 34));
    }

    public void setWalls(int x, int y) {
        if (x != board.getRewardLocation().x && y != board.getRewardLocation().y) {
            board.setWallLocation(x, y, wallsReward);
            drawWalls(x,y);
        }
    }
    public void drawWalls(int x, int y){
        frame.paintButtonLoc(x, y, Color.BLACK);
    }

    public void clearWalls() {
        for (int i = 0; i < board.getWallLocation().size(); i++) {
            frame.paintButtonLoc(board.getWallLocation().get(i).x, board.getWallLocation().get(i).y, null);
            board.setClearLocation(board.getWallLocation().get(i).x, board.getWallLocation().get(i).y, 0.0);
        }
        board.getWallLocation().clear();
    }

    private void randomizeWalls() {
        panel.getRandomizeWorld().addActionListener(actionEvent -> {
            //reset so the mouse doesn't get stuck on a rare occasion when the walls move
            rewardLocation.setLocation(configController.getRewardLocX(rewardLocation.x), configController.getRewardLocY(rewardLocation.y));
            numberOfWalls = configController.getNumberOfWalls(numberOfWalls);
            setRewardLoc(rewardLocation);
            epsilon = 0.5;
            clearWalls();
            clearVisitedLocations();
            generateWalls(numberOfWalls);
            drawOrientationBasedOnWeight();
            setMouseLocationRandom();
        });
    }

    private void musicToggle(){
        panel.getjCheckBoxMusicToggle().addActionListener(actionEvent ->{
            JCheckBox source = (JCheckBox)actionEvent.getSource();
            if(!source.isSelected()){
                clip.stop();
                mainMusicToggle = false;
            }else{
                mainMusicToggle = true;
                clip.start();
            }
        });
    }

    private void speedControl() {
        panel.getjSlider().addChangeListener(changeEvent -> {
            JSlider source = (JSlider) changeEvent.getSource();
            if (!source.getValueIsAdjusting()) {
                delay = source.getValue();
                if(delay != 0 & mainMusicToggle){
                    configController.setMusicOn(true);
                    clip.start();
                }else{
                    configController.setMusicOn(false);
                    clip.stop();
                    clip.setFramePosition(0);
                }
            }
        });
    }
    private void setTextArea(String text) {
            panel.getjTextField().setText(text);
    }
}