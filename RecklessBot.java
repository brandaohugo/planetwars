import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;
<<<<<<< HEAD

/**
 * RecklessBot
 * An attempt of smarter bot
 * It attacks the the strongest enemy planet with its strongest planet
 * The power is computed based on the number of ships.
 * <p>
 *
=======
/*
 * RecklessBot
 * An attempt of smarter bot that attacks 
 * to the the strongest enemy planet with its strongest planet
 * The power is computed based on the number of ships.
>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
 * @author Hugo Brandao
 */
public class RecklessBot implements Bot {

<<<<<<< HEAD
    /**
     * Function that gets called every turn.
     *
     * @param pw The game state
     */
    public void doTurn(PlanetWars pw) {

		//find planets according to the reckless heuristics
=======
    /* Function that gets called every turn.
     * @param pw The game state
     */
    
    public void doTurn(PlanetWars pw) {

        //find planets according to the reckless heuristics
>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
        Planet source = findMyStrongestPlanet(pw);
        Planet dest = findEnemyStrongestPlanet(pw);

        //attack
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

<<<<<<< HEAD
=======
    //Find our strongest planet 
>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
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
<<<<<<< HEAD

        return strongestPlanet;
    }

=======
        return strongestPlanet;
    }

    //find enemy's Strongest planet 
>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
    private Planet findEnemyStrongestPlanet(PlanetWars pw) {
        Planet strongestEnemyPlanet = null;
        double strongestEnemyScore = Double.MIN_VALUE;

        for (Planet planet : pw.getEnemyPlanets()) {            
            double score = (double) planet.getNumShips();
<<<<<<< HEAD
   
=======

>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 5fc528fa0ad8fc63de38590929a5fffcee43c2dd
