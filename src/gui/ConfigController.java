package gui;


public class ConfigController {
    private Panel controlPanel;
    public ConfigController(Panel panel){
        controlPanel = panel;
    }

    public void setRewardLocX(int val){
        controlPanel.getjTextFieldRewardLocX().setText(Integer.toString(val));
    }
    public void setRewardLocY(int val){
        controlPanel.getjTextFieldRewardLocY().setText(Integer.toString(val));
    }

    public void setNumberOfWalls(int val){
        controlPanel.getjTextFieldNumWalls().setText(Integer.toString(val));
    }

    public void setMusicOn(boolean val){
        controlPanel.getjCheckBoxMusicToggle().setSelected(val);
    }

    public int getNumberOfWalls(int defaultNumberOfWalls){
        return controlPanel.getjTextFieldNumWalls().getText()== null?defaultNumberOfWalls:Integer.parseInt(controlPanel.getjTextFieldNumWalls().getText());
    }
    public int getRewardLocX(int defaultRewardLocX){
        return (controlPanel.getjTextFieldRewardLocX().getText() == null||Integer.parseInt(controlPanel.getjTextFieldRewardLocX().getText())>19)?defaultRewardLocX:Integer.parseInt(controlPanel.getjTextFieldRewardLocX().getText());
    }
    public int getRewardLocY(int defaultRewardLocY){
        return (controlPanel.getjTextFieldRewardLocY().getText() == null||Integer.parseInt(controlPanel.getjTextFieldRewardLocY().getText())>19)?defaultRewardLocY:Integer.parseInt(controlPanel.getjTextFieldRewardLocY().getText());
    }
    public boolean isMusicOn(){
        return controlPanel.getjCheckBoxMusicToggle().isSelected();
    }
}
