


import java.util.ArrayList;
import java.util.List;

import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;


//static Node initialState = null; 

public class minimaxBot implements Bot {

    StateSpace root;

    public void doTurn(PlanetWars pw) {
        SimulatedPlanetWars spw = new SimulatedPlanetWars(pw);

        //check if we already created the StateSpace
        if(root == null){
            //1 for max player
            root = new StateSpace(spw, 1, 0, null);
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

    	StateSpace(SimulatedPlanetWars spw, int player, int nodeDepth, StateSpace parent){

    		this.spw = spw;
    		this.playerID = player;
    		this.depth = nodeDepth; 
    		buildOperators();

    		if(depth == 0){
    			parent = null; 
    		} else this.parent = parent;

    		if(spw.getWinner() != 0){
    			ArrayList<SimulatedPlanetWars> simulations = new ArrayList<>();

    			for(int[] operator : operatorsList) {
    				SimulatedPlanetWars x = spw.clone();
    				x.simulateAttack(player, spw.getPlanet(operator[0]), spw.getPlanet(operator[1]));
    				x.simulateGrowth();
    				simulations.add(x);
    			}
    			
    			if(this.playerID == 1) this.playerID = 2;
    			if(this.playerID == 2) this.playerID = 1;
    			
    			children = new ArrayList<StateSpace>();
    			for(SimulatedPlanetWars simulation: simulations) {
    				children.add(new StateSpace(simulation, playerID, this.depth + 1, this));
    				//difference of ships /ratio
    				
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
}


