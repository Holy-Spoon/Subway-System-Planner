
import ecs100.*;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.nio.file.*;

/**
 * MilanoSubway
 * A program to answer queries about Milan Metro subway lines and timetables for
 *  the subway services on those subway lines.
*/

public class MilanoSubway{
    //Fields to store the collections of Stations and Lines
    private Map<String, Station> allStations = new HashMap<String, Station>(); // all stations, indexed by station name
    private Map<String, SubwayLine> allSubwayLines = new HashMap<String, SubwayLine>(); // all subway lines, indexed by name of the line

    // Fields for the GUI  (with default values)
    private String currentStationName = "Zara";     // station to get info about, or to start journey from
    private String currentLineName = "M1-north";    // subway line to get info about.
    private String destinationName = "Brenta";      // station to end journey at
    private int startTime = 1200;                   // time for enquiring about

    /**
     * main method:  set up the user interface and load the data
     */
    public static void main(String[] args){
        MilanoSubway milan = new MilanoSubway();
        milan.setupGUI();   // set up the interface
        milan.loadData();   // load all the data
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and subway line
     */
    public void setupGUI(){
        UI.addButton("List all Stations",    this::listAllStations);
        UI.addButton("List Stations by name",this::listStationsByName);
        UI.addButton("List all Lines",       this::listAllSubwayLines);
        UI.addButton("Set Station",          this::setCurrentStation); 
        UI.addButton("Set Line",             this::setCurrentLine);
        UI.addButton("Set Destination",      this::setDestinationStation);
        UI.addTextField("Set Time (24hr)",   this::setTime);
        UI.addButton("Lines of Station",     this::listLinesOfStation);
        UI.addButton("Stations on Line",     this::listStationsOnLine);
        UI.addButton("On same line?",        this::onSameLine);
        UI.addButton("Next Services",        this::findNextServices);
        UI.addButton("Find Trip",            this::findTrip);

        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1500, 750);
        UI.setDivider(0.2);

        UI.drawImage("data/system-map.jpg", 0, 0, 1000, 704);

    }

    /**
     * Load data files
     */
    public void loadData(){
        loadStationData();
        UI.println("Loaded Stations");
        loadSubwayLineData();
        UI.println("Loaded Subway Lines");
        loadLineServicesData();
        UI.println("Loaded Line Services");
    }

    // Methods for loading data
    
    public void loadStationData(){
        try{
        Scanner sc = new Scanner(Path.of("data/stations.data"));
        while (sc.hasNextLine()){
            String  line = sc.nextLine();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split("\\s+");
            
            double stationX = Double.parseDouble(parts[parts.length - 2]);
            double stationY = Double.parseDouble(parts[parts.length - 1]);
            
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < parts.length - 2; i++){
                sb.append(parts[i]);
            }
            String stationName = sb.toString();
            
            Station station = new Station(stationName, stationX, stationY);
            allStations.put(stationName, station);        
            
        } 
        sc.close();
    } catch (IOException e) {
        UI.println("Failed to load station data " + e);
        }
    }
    
    public void loadSubwayLineData() {
        try {
            Scanner sc = new Scanner(Path.of("data/subway-lines.data"));
            while (sc.hasNextLine()) {
                String lineName = sc.nextLine().trim();
                if (lineName.isEmpty()) continue;
    
                
                SubwayLine subwayLine = new SubwayLine(lineName);
                allSubwayLines.put(lineName, subwayLine);
    
                
                String stationFileName = "data/" + lineName + "-stations.data";
                Scanner stationScanner = new Scanner(Path.of(stationFileName));
                while (stationScanner.hasNextLine()) {
                    String stationInfo = stationScanner.nextLine().trim();
                    if (stationInfo.isEmpty()) continue;
                    
                    //get stations and dis
                    Scanner stationDis = new Scanner (stationInfo);
                    String name = stationDis.next();
                    Double dis = stationDis.nextDouble();
                    
                    //adds in class subwayLine 
                    Station statName = allStations.get(name);
                    if (statName == null) {UI.println("Station dosen't exist"); continue;}
                    subwayLine.addStation(statName, dis);
                    
                    //adds subwayline to station
                    statName.addSubwayLine(subwayLine);
                    
                }
    
                stationScanner.close();
            }
    
            sc.close();
        } catch (IOException e) {
            UI.println("Failed to load subwayLineData: " + e);
        }
    }




    public void loadLineServicesData() {
        for (String lineName : allSubwayLines.keySet()) {
            SubwayLine subwayLine = allSubwayLines.get(lineName);
            
            String serviceName = "data/" + lineName + "-services.data";
            try {
                Scanner sc = new Scanner(Path.of(serviceName));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;
                    
                    LineService lineService = new LineService(subwayLine);
                    
                    Scanner lineScanner = new Scanner(line);
                    while (lineScanner.hasNextInt()) {
                        int time = lineScanner.nextInt();
                        lineService.addTime(time);
                    }
                    subwayLine.addLineService(lineService);
                    lineScanner.close();
                }
                sc.close();
            } catch (IOException e) {
                UI.println("Failed to load services for line " + lineName + ": " + e);
            }
        }
    }

    // Methods for answering the queries
    
    public void listAllStations(){
        UI.clearText();
        for (Station station : allStations.values()) {
            UI.println(station);
        }
    }   

    public void listStationsByName(){
        UI.clearText();
        List <String> stationNames = new ArrayList<>(allStations.keySet());
        Collections.sort(stationNames);
        for (String name : stationNames){                                   
            Station station = allStations.get(name);                        
            UI.println(station); 
        }
    }
    
    public void listAllSubwayLines(){
        UI.clearText();
        for (SubwayLine subwayLine : allSubwayLines.values()){
            UI.println(subwayLine);
        }
    }

    public void listLinesOfStation(){
        UI.clearText();
        Station listStation = allStations.get(currentStationName);
        Set<SubwayLine> subway = listStation.getSubwayLines();
        
        UI.println("Subway lines for " + listStation + ":");
        for (SubwayLine sl : subway) {
            UI.println(sl);
        }
    }
    
    public void listStationsOnLine() {
        UI.clearText(); 
        SubwayLine line = allSubwayLines.get(currentLineName);
        List<Station> stations = line.getStations();
        
        UI.println("Stations for " + currentLineName + ":");
        for (Station station : stations) {
            UI.println(station);
        }
    }
    
 public void onSameLine() {
    Station start = allStations.get(currentStationName);
    Station destination = allStations.get(destinationName);

    if (start == null || destination == null) {
        UI.println("Invalid station name(s).");
        return;
    }

    for (SubwayLine line : allSubwayLines.values()) {
        List<Station> stations = line.getStations();
        int startIdx = stations.indexOf(start);
        int destIdx = stations.indexOf(destination);

        if (startIdx != -1 && destIdx != -1 && startIdx < destIdx) {
            double startDist = line.getDistanceFromStart(start);
            double destDist = line.getDistanceFromStart(destination);
            double distance = destDist - startDist;

            UI.println("Stations are on line: " + line.getName());
            UI.println("Distance from " + currentStationName + " to " + destinationName + ": " + String.format("%.2f", distance) + " km");
            return;
        }
    }

    UI.println("No subway line goes from " + currentStationName + " to " + destinationName + " in that direction.");
}

    
    public void findNextServices() {
        UI.clearText(); 
        Station currentStation = allStations.get(currentStationName);
        
        UI.println("Next services from " + currentStationName + " after " + startTime + ":");
    
        Set<SubwayLine> lines = currentStation.getSubwayLines();
        for (SubwayLine line : lines) {
            List<Station> stationsOnLine = line.getStations();
            int stationNum = stationsOnLine.indexOf(currentStation);
            if (stationNum == -1) continue;
    
            LineService nextService = null;
            for (LineService service : line.getLineServices()) {
                List<Integer> times = service.getTimes();
                if (stationNum < times.size()) {
                    int timeAtStation = times.get(stationNum);
                    if (timeAtStation >= startTime) {
                        nextService = service;
                        break;
                    }
                }
            }
    
            if (nextService != null) {
                int time = nextService.getTimes().get(stationNum);
                UI.println("At " + time + " on the " + line.getName() + " line");
            } else {
                UI.println("  " + line.getName() + ": No service after " + startTime);
            }
        }
    }
    
    public void findTrip() {
        Station startStation = allStations.get(currentStationName);
        Station endStation = allStations.get(destinationName);
    
        if (startStation == null || endStation == null) {
            UI.println("Invalid station name(s).");
            return;
        }
    
        for (SubwayLine line : allSubwayLines.values()) {
            List<Station> stations = line.getStations();
            int startIndex = stations.indexOf(startStation);
            int endIndex = stations.indexOf(endStation);
    
            // both stations are on this line and in correct direction
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                for (LineService service : line.getLineServices()) {
                    List<Integer> times = service.getTimes();
    
                    if (endIndex >= times.size()) continue;
    
                    int departTime = times.get(startIndex);
                    int arriveTime = times.get(endIndex);
    
                    if (departTime >= startTime) {
                        double distance = line.getDistanceFromStart(endStation) - line.getDistanceFromStart(startStation);
    
                        UI.println("Trip Found:");
                        UI.println("  Line: " + line.getName());
                        UI.println("  Train: " + service.getTrainID());
                        UI.println("  Departure from " + currentStationName + ": " + departTime);
                        UI.println("  Arrival at " + destinationName + ": " + arriveTime);
                        UI.println("  Distance: " + String.format("%.2f", distance) + " km");
                        return;
                    }
                }
    
                UI.println("No trip available on line " + line.getName() + " after time " + startTime);
                return;
            }
        }
    
        UI.println("No line connects " + currentStationName + " to " + destinationName + " directly in that order.");
    }


    // Methods for asking the user for station names, line names, and time.
    
    /**
     * Set the startTime.
     * If user enters an invalid time, it reports an error
     */
    public void setTime (String time){
        int newTime = startTime;
        try{
            newTime=Integer.parseInt(time);
            if (newTime >=0 && newTime<2400){
                startTime = newTime;
            }
            else {
                UI.println("Time must be between 0000 and 2359");
            }
        }catch(Exception e){UI.println("Enter time as a four digit integer");}
    }

    /**
     * Ask the user for a station name and assign it to the currentStationName field
     * Must pass a collection of the names of the stations to getOptionFromList
     */
    public void setCurrentStation(){
        String name = getOptionFromList("Choose current station", allStations.keySet());
        if (name==null ) {return;}
        UI.println("Setting current station to "+name);
        currentStationName = name;
    }

    /**
     * Ask the user for a destination station name and assign it to the destinationName field
     * Must pass a collection of the names of the stations to getOptionFromList
     */
    public void setDestinationStation(){
        String name = getOptionFromList("Choose destination station", allStations.keySet());
        if (name==null ) {return;}
        UI.println("Setting destination station to "+name);
        destinationName = name;
    }

    /**
     * Ask the user for a subway line and assign it to the currentLineName field
     * Must pass a collection of the names of the lines to getOptionFromList
     */
    public void setCurrentLine(){
        String name =  getOptionFromList("Choose current subway line", allSubwayLines.keySet());
        if (name==null ) {return;}
        UI.println("Setting current subway line to "+name);
        currentLineName = name;
    }


    // 
    /**
    * Method to get a string from a dialog box with a list of options
    */
    public String getOptionFromList(String question, Collection<String> options){
        Object[] possibilities = options.toArray();
        Arrays.sort(possibilities);
        return (String)javax.swing.JOptionPane.showInputDialog
            (UI.getFrame(),
             question, "",
             javax.swing.JOptionPane.PLAIN_MESSAGE,
             null,
             possibilities,
             possibilities[0].toString());
    }
}
