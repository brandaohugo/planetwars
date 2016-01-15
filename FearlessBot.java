import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

/* FearlessBot
 * An attempt of smarter bot that attacks 
 * to the strongest enemy planet with its weakest planet
 * The power is computed based on the number of ships.
 * @author Evangelos Daniil - edl320
 */

public class FearlessBot implements Bot {
    /** Function that gets called every turn.
     *  @param pw The game state
     */
    public void doTurn(PlanetWars pw) {
	//find planets according to the fearless heuristics
        Planet source = findMyWeakestPlanet(pw);
        Planet dest = findEnemyStrongestPlanet(pw);

        //attack
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

    //finds our weakest planet 
    private Planet findMyWeakestPlanet(PlanetWars pw) {
        Planet weakestPlanet = null;
        double weakestScore = Double.MAX_VALUE;

        for (Planet planet : pw.getMyPlanets()) {
            // skip planets with only one ship
            if (planet.getNumShips() <= 1){
                continue;
            }
            double score = (double) planet.getNumShips();

            if (score < weakestScore) {
                weakestScore = score;
                weakestPlanet = planet;
            }
        }
        return weakestPlanet;
    }

    //finds enemy's strongest planet
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
        GameClient.run(new FearlessBot());
    }
}
