package main.Waypoints;

import main.DCSconnection.PortListenerThread;
import main.DCSconnection.PortSender;
import main.UI.GUI;
import main.Waypoints.PlanesCommands.F16;
import main.Waypoints.PlanesCommands.F18;
import main.Waypoints.PlanesCommands.A10CII;
import main.Waypoints.PlanesCommands.M2000;
import main.models.Hemisphere;
import main.models.Point;

import java.util.ArrayList;
import java.util.List;

public class WaypointManager {
    static private ArrayList<Point> waypoints = new ArrayList<>();

    public static void transfer(){
        String model = PortListenerThread.getPlaneModel();

        if(model != null && !waypoints.isEmpty()){
            if(model.equals("F-16C_50")) {
                List<Point> f16Coords = F16.getCoords(waypoints);
                String dataToSend = F16.getCommands(f16Coords).toString();
                PortSender.send(dataToSend);
            } else if(model.equals("FA-18C_hornet")){
                GUI.warning("Please make sure that: \n" +
                        "1. PRECISE option is boxed in HSI > DATA\n" +
                        "2. You are not in the TAC menu\n"+
                        "3. You are in the 00°00.0000' coordinate format");
                ArrayList<Point> f18Coords = F18.getCoords(waypoints);
                String dataToSend = F18.getCommands(f18Coords).toString();
                PortSender.send(dataToSend);
            } else if(model.equals("A-10C_2") || model.equals("A-10C")) {
                ArrayList<Point> a10Coords = A10CII.getCoords(waypoints);
                String dataToSend = A10CII.getCommands(a10Coords).toString();
                PortSender.send(dataToSend);
            } else if(model.equals("M-2000C")) {
                List<Point> m2000Coords = M2000.getCoords(waypoints);
                String dataToSend = M2000.getCommands(m2000Coords).toString();
                PortSender.send(dataToSend);
            }
        }

    }

    public static boolean saveWaypointSuccessful(){
        String latitude = PortListenerThread.getLatitude();
        String longitude = PortListenerThread.getLongitude();
        String elevation = PortListenerThread.getElevation();
        if(latitude != null && longitude != null && elevation != null){
            Hemisphere latHem;
            Hemisphere longHem;
            if(latitude.contains("-")){
                latHem = Hemisphere.SOUTH;
            } else {
                latHem = Hemisphere.NORTH;
            }
            if(longitude.contains("-")){
                longHem = Hemisphere.WEST;
            } else {
                longHem = Hemisphere.EAST;
            }
            var point = new Point(latitude.replace("-", ""), longitude.replace("-", ""), elevation, latHem, longHem);
            waypoints.add(point);
            return true;
        }
        return false;
    }

    public static void clearWaypoints(){
        waypoints.clear();
    }

    public static int getSelectedWaypointsCount(){
        return waypoints.size();
    }
}
