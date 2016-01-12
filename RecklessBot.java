import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;
/*
 * RecklessBot
 * An attempt of smarter bot that attacks 
 * to the the strongest enemy planet with its strongest planet
 * The power is computed based on the number of ships.
 * @author Hugo Brandao
 */
public class RecklessBot implements Bot {

    /* Function that gets called every turn.
     * @param pw The game state
     */
    
    public void doTurn(PlanetWars pw) {

        //find planets according to the reckless heuristics
        Planet source = findMyStrongestPlanet(pw);
        Planet dest = findEnemyStrongestPlanet(pw);

        //attack
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

    //Find our strongest planet 
    private Planet findMyStrongestPlanet(PlanetWars pw) {
        Planet strongestPlanet = null;
        double strongestScore = Double.MIN_VALUE;

        for (Planet planet : pw.getMyPlanets()) {
            // skip planets with only one ship
            if (planet.getNumShips() <= 1)
                continue;

            double score = (double) planet.getNumShips();

            if (score > strongestScore) {
                strongestScore = score;
                strongestPlanet = planet;
            }
        }
        return strongestPlanet;
    }

    //find enemy's Strongest planet 
    private Planet findEnemyStrongestPlanet(PlanetWars pw) {
        Planet strongestEnemyPlanet = null;
        double strongestEnemyScore = Double.MIN_VALUE;

        for (Planet planet : pw.getEnemyPlanets()) {            
            double score = (double) planet.getNumShips();

            if (score > strongestEnemyScore) {
                strongestEnemyScore = score;
                strongestEnemyPlanet = planet;
            }
        }
        return strongestEnemyPlanet;
    }

    public static void main(String[] args) {
        GameClient.run(new RecklessBot());
    }
}