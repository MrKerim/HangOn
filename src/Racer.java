import java.util.ArrayList;
import java.util.List;

public class Racer {

    private double x, y, z;
    private double X, Y;
    private double scale;
    private double speed;

    private boolean isEndingGameCharacter = false;

    public Racer(int z, int x) {
        speed = 0;
        this.x = x;
        y = 0;
        this.z = z;
    }

    public double get_z(){
        return z;
    }

    public double get_x(){
        return x;
    }

    public double get_X(){
        return X;
    }

    public double get_Y(){
        return Y;
    }

    public void project(int camX, int camY, int camZ) {
        scale = GameSettings.camD / (z - camZ);
        X = (1 + scale * (x - camX)) * GameSettings.with / 2;
        Y = (1 - scale * (y - camY)) * GameSettings.height / 2;
    }

    public void updateStep(){

        speed += GameSettings.racerAcceleration;
        if(speed > GameSettings.maxRacerSpeed) speed = GameSettings.maxRacerSpeed;
        if(speed < GameSettings.minRacerSpeed) speed = GameSettings.minRacerSpeed;
        z += speed/3;
        //z += 0;

    }

    public void setEndingGameCharacter() {
        isEndingGameCharacter = true;
    }

    public boolean getEndingGameCharacter() {
        return isEndingGameCharacter;
    }

    public static List<Racer> initRacers(){
        List<Racer> racers = new ArrayList<Racer>();
        for(int i = 0; i < GameSettings.numberOfRacers; i++){
            double x_mltp;
            if(i%2 == 0) x_mltp = 1;
            else x_mltp = -1;
            //racers.add(new Racer((i*5+5) * GameSettings.segL, (int) (200 * x_mltp)));
            racers.add(new Racer((int) ((i*1.5*GameSettings.posDifferenceInStartForRacersCoeff + 1.5) * GameSettings.segL), (int) (GameSettings.racersXOffset * x_mltp)));
        }
        return racers;
    }


}
