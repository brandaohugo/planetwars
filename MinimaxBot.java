package planetWars;

import java.util.List;

import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

public class MinimaxBot implements Bot {
    
    public MinimaxBot(PlanetWars pw){
        simulatePlanetWars(pw);
        
    }

    public void simulatePlanetWars(PlanetWars pw){
        List<Planet> planets = pw.getAllPlanets();
        
        
    }
    public void doTurn(PlanetWars pw) {
    

        //notice that a PlanetWars object called pw is passed as a parameter which you could use
        //if you want to know what this object does, then read PlanetWars.java

        //create a source planet, if you want to know what this object does, then read Planet.java
        Planet source = null;

        //create a destination planet
        Planet dest = null;

        //(1) implement an algorithm to determine the source planet to send your ships from
        //... code here

        //(2) implement an algorithm to determine the destination planet to send your ships to
        //... code here

        //(3) Attack
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

    public static void main(String[] args) {
        GameClient.run(new MinimaxBot(null));
    }
}
