# Milano Subway System Planner

A Java application that simulates and queries the Milan Metro subway system.  
Built as part of a course project using **BlueJ** and the `ecs100` graphics library.

The program loads station, line, and timetable data, then provides a GUI to answer queries such as:
- List all stations or subway lines
- Show which stations are on a particular line   
- Find which lines serve a given station
- Check if two stations are on the same line and calculate the distance
- Display the next available train services from a given station
- Plan a trip from one station to another based on time and line schedules

---

## Features
- **Interactive GUI** with buttons for queries and data entry  
- **Data-driven**: Reads station, line, and service information from files in the `/data` folder  
- **Map display**: Draws the subway system map in the application window  
- **Journey planning**: Finds routes and calculates travel distances/times  
- **Timetable integration**: Displays the next available trains after a chosen time  

---

## Screenshots
<img width="1804" height="902" alt="image" src="https://github.com/user-attachments/assets/85948e04-cf1f-40e0-8c26-f6c83f249809" />

---

## Project Structure
### Src
- MilanoSubway.java # Main program & GUI
- Station.java # Represents a subway station
- SubwayLine.java # Represents a subway line
- LineService.java # Represents services on a line
### Data
- stations.data # All station info
- subway-lines.data # List of subway lines
- *lineName* -stations.data # Stations for each line
- *lineName* -services.data # Timetable for each line
- system-map.jpg # Subway system map
  ---
## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/milano-subway.git
   cd milano-subway
2. Open the project in BlueJ (or any Java IDE).
3. Make sure the ecs100.jar library is included in the classpath.
4. Run MilanoSubway.java → the GUI will launch with the system map.
