import java.util.ArrayList;
import java.util.List;

public class Line {
    double x, y, z;
    double X, Y, W;
    double scale;
    double curve;

    boolean treeOnLeft;
    boolean treeOnRight;

    boolean isCheckPoint;

    public Line() {
        curve = 0;
        x = 0;
        y = 0;
        z = 0;
        treeOnLeft = false;
        treeOnRight = false;
    }

    void project(int camX, int camY, int camZ) {
        scale = GameSettings.camD / (z - camZ);
        X = (1 + scale * (x - camX)) * GameSettings.with / 2;
        Y = (1 - scale * (y - camY)) * GameSettings.height / 2;
        W = scale * GameSettings.roadW *GameSettings.with / 2;
    }


    public static List<Line> initLines(int n){
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < n; i++) {
            Line line = new Line();
            line.z = i * GameSettings.segL;


            for(int j = 0; j < GameSettings.leftCurveStart.length; j++)
                if(i>=GameSettings.leftCurveStart[j] &&
                i<GameSettings.leftCurveStart[j] + GameSettings.curve_length )
                    line.curve += -1 * GameSettings.curve_angle;



            for(int j = 0; j < GameSettings.rightCurveStart.length; j++)
                if(i>=GameSettings.rightCurveStart[j] &&
                        i<GameSettings.rightCurveStart[j] + GameSettings.curve_length )
                    line.curve +=  GameSettings.curve_angle;



            for(int j = 0; j< GameSettings.checkpoints.length;j++)
                if(i >= GameSettings.checkpoints[j]  &&
                i < GameSettings.checkpoints[j] + 4) line.isCheckPoint = true;

            if(i%(GameSettings.treeSpawnFrequency)<80  && i % 4 == 0){
                line.treeOnRight = true;
            }

            if(i%(GameSettings.treeSpawnFrequency)>120 && i%(GameSettings.treeSpawnFrequency)<= 200  && i % 4 == 0){
                line.treeOnLeft = true;
            }


            lines.add(line);
        }
        return lines;
    }

}