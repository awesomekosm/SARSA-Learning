package gridworld;


public class Square {
    private double value = 0.0;
    private double up = 0.0;
    private double down = 0.0;
    private double left = 0.0;
    private double right = 0.0;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getDown() {
        return down;
    }

    public void setDown(double down) {
        this.down = down;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getRight() {
        return right;
    }

    public Double getHighestWeightByDirection() {
        String direction = getLargest();
        switch (direction) {
            case "UP":
                return up;
            case "DOWN":
                return down;
            case "LEFT":
                return left;
            case "RIGHT":
                return right;
        }
        return null;
    }
    public Double getWeightByDirection(String direction){
        switch (direction) {
            case "UP":
                return up;
            case "DOWN":
                return down;
            case "LEFT":
                return left;
            case "RIGHT":
                return right;
        }
        return null;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public String getLargest() {
        double direction[] = {up, down, left, right};
        int indexOfLargest = -1;
        double largest = 0.0;
        for (int i = 0; i < 4; i++) {
            double temp = direction[i];
            if (temp > largest) {
                largest = temp;
                indexOfLargest = i;
            }
        }

        switch (indexOfLargest) {
            case 0:
                return "UP";
            case 1:
                return "DOWN";
            case 2:
                return "LEFT";
            case 3:
               return "RIGHT";
            default:
                return null;
        }
    }

    public void setWeightForDirection(String direction, double weight) {
        switch (direction) {
            case "UP":
                up = weight;
                break;
            case "DOWN":
                down = weight;
                break;
            case "LEFT":
                left = weight;
                break;
            case "RIGHT":
                right = weight;
                break;
        }
    }

    public void setSquare(Square square) {
        up = square.getUp();
        down = square.getDown();
        left = square.getLeft();
        right = square.getRight();
        value = square.getValue();
    }


}
