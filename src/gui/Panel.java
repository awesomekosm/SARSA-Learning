package gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Panel {
    private JPanel mainPanel;
    private JPanel internalGridPanel;
    private JPanel internalCtrlPanel;

    private JPanel infoConfContainer;
    private JPanel infoPanel;
    private JPanel configPanel;


    private JButton buttonArray[][] = new JButton[20][20];
    private JButton randomizeWorld = new JButton("Randomize World");
    private JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL,0,300,0);
    private JTextField jTextField = new JTextField();
    private JTextField jTextFieldNumWalls = new JTextField();
    private JTextField jTextFieldRewardLocX = new JTextField();
    private JTextField jTextFieldRewardLocY = new JTextField();
    private JLabel jLabelNumWalls = new JLabel("Walls",SwingConstants.CENTER);
    private JLabel jLabelRewardLocX = new JLabel("Reward X",SwingConstants.CENTER);
    private JLabel jLabelRewardLocY = new JLabel("Reward Y",SwingConstants.CENTER);
    private JLabel jLabelMusic = new JLabel("Music");
    private JCheckBox jCheckBoxMusicToggle = new JCheckBox();
    private LinkedList<Point> marked = new LinkedList<>();

    public JCheckBox getjCheckBoxMusicToggle() {
        return jCheckBoxMusicToggle;
    }

    public JTextField getjTextFieldRewardLocX() {
        return jTextFieldRewardLocX;
    }

    public JTextField getjTextFieldRewardLocY() {
        return jTextFieldRewardLocY;
    }

    public JTextField getjTextFieldNumWalls() {
        return jTextFieldNumWalls;
    }

    public LinkedList<Point> getMarked() {
        return marked;
    }

    public JButton[][] getButtonArray() {
        return buttonArray;
    }

    public JSlider getjSlider() {
        return jSlider;
    }

    public JButton getRandomizeWorld() {
        return randomizeWorld;
    }

    public JTextField getjTextField(){
        return jTextField;
    }

    public Panel(){
        setUpMainPanel();
    }

    private void setUpMainPanel(){
        internalGridPanel = new JPanel();
        internalGridPanel.setLayout(new GridLayout(20, 20));
        internalGridPanel.setPreferredSize(new Dimension(750, 750));
        internalGridPanel.setMaximumSize(internalGridPanel.getPreferredSize());

        internalCtrlPanel = new JPanel();
        internalCtrlPanel.setLayout(new GridLayout(2, 1));
        internalCtrlPanel.setPreferredSize(new Dimension(750, 60));
        internalCtrlPanel.setMaximumSize(internalCtrlPanel.getPreferredSize());

        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 1));
        infoPanel.setPreferredSize(new Dimension(500, 60));
        infoPanel.setMaximumSize(infoPanel.getPreferredSize());

        configPanel = new JPanel();
        configPanel.setLayout(new GridLayout(2, 4));
        configPanel.setPreferredSize(new Dimension(250, 60));
        configPanel.setMaximumSize(configPanel.getPreferredSize());

        infoConfContainer = new JPanel();
        infoConfContainer.setLayout(new GridLayout(1, 2));
        infoConfContainer.setPreferredSize(new Dimension(750, 60));
        infoConfContainer.setMaximumSize(internalCtrlPanel.getPreferredSize());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        randomizeWorld.setMaximumSize(new Dimension(750, 20));
        randomizeWorld.setFocusable(false);
        jSlider.setMaximumSize(new Dimension(750, 20));
        jSlider.setMajorTickSpacing(10);
        jSlider.setMinorTickSpacing(1);
        jSlider.setPaintTicks(true);
        jTextField.setMaximumSize(new Dimension(375, 20));
        jTextField.setHighlighter(null);
        jTextField.setEditable(false);
        jTextFieldNumWalls.setMaximumSize(new Dimension(10, 10));

        for(int x = 0;x<20;x++){
            for(int y = 0; y<20;y++){
                buttonArray[y][x] = new JButton();
                buttonArray[y][x].setEnabled(false);
                buttonArray[y][x].setPreferredSize(new Dimension(20, 20));
                internalGridPanel.add(buttonArray[y][x]);
            }
        }
        internalCtrlPanel.add(jSlider);
        internalCtrlPanel.add(randomizeWorld);

        infoPanel.add(jTextField);
        jCheckBoxMusicToggle.setAlignmentX(SwingConstants.CENTER);
        configPanel.add(jLabelNumWalls);
        configPanel.add(jLabelRewardLocX);
        configPanel.add(jLabelRewardLocY);
        configPanel.add(jLabelMusic);
        configPanel.add(jTextFieldNumWalls);
        configPanel.add(jTextFieldRewardLocX);
        configPanel.add(jTextFieldRewardLocY);
        configPanel.add(jCheckBoxMusicToggle);


        infoConfContainer.add(infoPanel);
        infoConfContainer.add(configPanel);

        mainPanel.add(internalGridPanel);
        mainPanel.add(internalCtrlPanel);
        mainPanel.add(infoConfContainer);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
