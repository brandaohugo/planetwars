


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;


//static Node initialState = null; 

public class minimaxBot implements Bot {

    StateSpace state;

    public void doTurn(PlanetWars pw) {
        SimulatedPlanetWars spw = new SimulatedPlanetWars(pw);

        //check if we already created the StateSpace
        if(state == null){
            //1 for max player
            state = new StateSpace(spw, 1, 0, null);
        } 
        
        
        
        
        
        
        
        
        
        //minimaxAlgorithm();
    }

    //STILL HAVE TO IMPLEMENT THE MAX AND THE MIN METHODS 
    int minimaxAlgorithm(Node StateSpace, int depth){
        //if there are no other children
        if(StateSpace.children.size() == 0){
            return StateSpace.score;

            //if the bot is playing then return the MIN of minimaxAlgorithm
        } else if(StateSpace.playerID == 1){
            return minimaxAlgorithm(StateSpace, depth-1);
        }
        //if the bot is playing then return the MAX of minimaxAlgorithm 
        return minimaxAlgorithm(StateSpace, depth-1);
    }


    public static void main(String[] args) {
        GameClient.run(new minimaxBot());
    }
    
    private class StateSpace {

    	SimulatedPlanetWars spw;
    	StateSpace parent;
    	ArrayList <StateSpace> children;
    	private ArrayList<int[]> operatorsList;
    	int playerID;
    	int depth;
    	int score = 20;
    	int [] operator;

    	StateSpace(SimulatedPlanetWars spw, int player, int nodeDepth, StateSpace parent, int[] operator){
    		this.operator = operator;
    		this.spw = spw;
    		this.playerID = player;
    		this.depth = nodeDepth; 
    		buildOperators();

    		if(depth == 0){
    			parent = null; 
    		} else this.parent = parent;

    		if(spw.getWinner() != 0){
    			ArrayList<SimulatedPlanetWars> simulations = new ArrayList<>();
    			children = new ArrayList<StateSpace>();
    			
    			if(this.playerID == 1) this.playerID = 2;
    			if(this.playerID == 2) this.playerID = 1;
    			    			
    			for(int[] op : operatorsList) {
    				SimulatedPlanetWars simulation = spw.clone();
    				simulation.simulateAttack(player, spw.getPlanet(op[0]), spw.getPlanet(op[1]));
    				simulation.simulateGrowth();
    				children.add(new StateSpace(simulation, playerID, this.depth + 1, this, op));
    			}    			    		    			
    		}
    	}
    

    	void buildOperators(){
    		List<Planet> planetList = spw.getAllPlanets();

    		ArrayList<int[]> operators = new ArrayList<>();

    		for(int i = 0; i < planetList.size(); i++){
    			for (int j = 0; j < planetList.size(); j++){
    				//check that we don't shoot our planets 
    				if(planetList.get(i).getOwner() == playerID){
    					int[] operator = new int[2];
    					//operator[0] = source operator[1] = destination
    					operator[0] = planetList.get(i).getID();
    					if(planetList.get(j).getOwner() != playerID) {
    						operator[1] = planetList.get(j).getID();
    						operators.add(operator);
    					}
    				}
    			}
    			this.operatorsList = operators; 
    		}
    	}
    
        }
    
    private class SimulatedPlanetWars {

        List<Planet> planets = new ArrayList<Planet>();

        public SimulatedPlanetWars(PlanetWars pw) {
            for (Planet planet : pw.getAllPlanets()) {
                planets.add(planet);
            }
        }
        
        public SimulatedPlanetWars(List<Planet> allPlanets) {
            this.planets = allPlanets;
            }
              
        public SimulatedPlanetWars clone(){
    		List<Planet> allPlanets = new ArrayList<Planet>();

    		for(Planet planet : this.getAllPlanets()) {
    			Planet newPlanet = new Planet(planet.getID(), planet.getOwner(), planet.getNumShips(), planet.getGrowthRate(), planet.getX(), planet.getY());
    			allPlanets.add(newPlanet);
    		}
    		return new SimulatedPlanetWars(allPlanets);
    	}

        public void simulateGrowth() {
            for (Planet p : planets) {

                if (p.getOwner() == 0)
                    continue;

                Planet newPlanet = new Planet(p.getID(), p.getOwner(), p.getNumShips() + p.getGrowthRate(),
                    p.getGrowthRate(), p.getX(), p.getY());

                planets.set(p.getID(), newPlanet);
            }
        }

        public void simulateAttack(int player, Planet source, Planet dest) {
            if (source != null && dest != null) {
                if (source.getOwner() != player) {
                    return;
                }

                // Simulate attack
                int remnantFleet = dest.getNumShips() - source.getNumShips() / 2;
                int owner = dest.getOwner();

                if (remnantFleet < 0)
                    owner = player;

                Planet newSource = new Planet(source.getID(), source.getOwner(), source.getNumShips() / 2,
                    source.getGrowthRate(), source.getX(), source.getY());
                Planet newDest = new Planet(dest.getID(), owner, Math.abs(remnantFleet),
                    dest.getGrowthRate(), dest.getX(), dest.getY());

                planets.set(source.getID(), newSource);
                planets.set(dest.getID(), newDest);
            }
        }

        public void simulateAttack(Planet source, Planet dest) {
            simulateAttack(1, source, dest);
        }

        public void simulateBullyBotAttack() {
            Planet source = null;
            Planet dest = null;

            double sourceScore = Double.MIN_VALUE;
            double destScore = Double.MAX_VALUE;

            for (Planet planet : planets) {

                if (planet.getOwner() == PlanetWars.ENEMY) {
                    if (planet.getNumShips() <= 1) continue;
                    double scoreMax = (double) planet.getNumShips();
                    if (scoreMax > sourceScore) {
                        sourceScore = scoreMax;
                        source = planet;
                    }
                }

                // (2) Find the weakest enemy or neutral planet.
                if (planet.getOwner() != PlanetWars.ENEMY) {
                    double scoreMin = (double) (planet.getNumShips());
                    if (scoreMin < destScore) {
                        destScore = scoreMin;
                        dest = planet;
                    }
                }

            }

            // (3) Simulate attack
            if (source != null && dest != null) {
                simulateAttack(2, source, dest);
            }

        }

        public int numPlanets() {
            return planets.size();
        }

        public Planet getPlanet(int planetID) {
            return planets.get(planetID);
        }

        public List<Planet> getAllPlanets() {
            return planets;
        }

        public List<Planet> getMyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() == PlanetWars.PLAYER)
                    r.add(p);

            return r;
        }

        public List<Planet> getNeutralPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() == PlanetWars.NEUTRAL)
                    r.add(p);

            return r;
        }

        public List<Planet> getEnemyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() >= PlanetWars.ENEMY)
                    r.add(p);

            return r;
        }

        public List<Planet> getNotMyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() != PlanetWars.PLAYER)
                    r.add(p);

            return r;
        }

        public boolean isPlayerAlive(int player) {
            for (Planet p : planets)
                if (p.getOwner() == player)
                    return true;

            return false;
        }

        public int getWinner() {
            Set<Integer> remainingPlayers = new TreeSet<Integer>();
            for (Planet p : planets)
                remainingPlayers.add(p.getOwner());

            switch (remainingPlayers.size()) {
                case 0:
                    return 0;
                case 1:
                    return (Integer) remainingPlayers.toArray()[0];
                default:
                    return -1;
            }
        }

        public int getNumShips(int player) {
            int numShips = 0;

            for (Planet p : planets)
                if (p.getOwner() == player)
                    numShips += p.getNumShips();

            return numShips;
        }

        public void issueOrder(Planet source, Planet dest) {
            simulateAttack(source, dest);
        }
    }

}


