import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

public class LineService{
    // Fields
    private SubwayLine subwayLine;  
    private String trainID;    // train line name + starting time of the train
    private List<Integer> times = new ArrayList<Integer>();

    //Constructor
    /**
     * Make a new LineService on a particular subway line.
     */
    public LineService(SubwayLine line){
        subwayLine = line;
    }

    //getters
    public SubwayLine getSubwayLine(){
        return subwayLine;
    }

    public String getTrainID(){
        return this.trainID;
    }

    public List<Integer> getTimes(){
        return Collections.unmodifiableList(times);  
    }

    // Other methods.
    /**
     * Add the next time to the LineService
     */
    public void addTime(int time){
        times.add(time);
        if (trainID==null){
            trainID = subwayLine.getName()+"-"+time;
        }
    }

    /**
     * Return the start time of this Train Service
     */
    public int getStart(){
        if (times.size() != 0) return times.get(0);
        else                   return -1;
    }

    /**
     * ID plus number of stops
     */
    public String toString(){
        if (trainID==null){return subwayLine.getName()+"-unknownStart";}
        return trainID+" ("+times.size()+" stops)";
    }

}
