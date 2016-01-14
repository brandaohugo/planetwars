package planetWars;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

public class MinimaxBot implements Bot {

	public void doTurn(PlanetWars pw) {
		SimulatedPlanetWars spw = new SimulatedPlanetWars(pw);
		//we got a bug here cause the buildTree returns a node 
		Node root = buildTree(spw); 
	}

	//and we got a bug here because the buildtree returns a node
	public Node buildTree(SimulatedPlanetWars spw){
		ArrayList<int[]> operatorList = getOperators(spw);

		ArrayList<SimulatedPlanetWars> children = new ArrayList<>();

		for(int[] operator : operatorList) { 
			SimulatedPlanetWars x = spw.clone();
			x.simulateAttack(0, spw.getPlanet(operator[0]), spw.getPlanet(operator[1]));
			children.add(x);
		}
	}

	ArrayList<int[]> getOperators(SimulatedPlanetWars spw){
		//this is for our planets
		//we used to have pw as an argument 
		List<Planet> myPlanets = spw.getMyPlanets();

		ArrayList<Integer> myPlanetIDs = new ArrayList<>();

		for(Planet planet : myPlanets){
			myPlanetIDs.add(planet.getID());
		}

		//for all the planets in the game
		//we used to have pw as an argument
		List<Planet> allPlanets = spw.getAllPlanets();

		ArrayList<Integer> allPlanetIDs = new ArrayList<>();

		for(Planet allPlanet : allPlanets){
			allPlanetIDs.add(allPlanet.getID());	
		}

		ArrayList<int[]> allPossibleOperators = new ArrayList<>();

		for(int i = 0; i < myPlanets.size(); i++){
			for(int j = 0; j < allPlanets.size(); j++){
				//obey the same planets
				if(myPlanetIDs.get(i) == allPlanetIDs.get(j)){
					continue; 		
				}
				int [] operator = new int[2];
				operator[0] = myPlanetIDs.get(i);
				operator[1] = myPlanetIDs.get(j);
				allPossibleOperators.add(operator);
			}
		}
		return allPossibleOperators;
	}

	public static void main(String[] args) {
		GameClient.run(new MinimaxBot());
	}

	public class SimulatedPlanetWars {

		List<Planet> planets = new ArrayList<Planet>();

		public SimulatedPlanetWars(PlanetWars pw) {
			for (Planet planet : pw.getAllPlanets()) {
				planets.add(planet);
			}
		}

		public SimulatedPlanetWars(List<Planet> planets){
			this.planets = planets; 
		}

		public SimulatedPlanetWars clone(){
			List<Planet> totalPlanets = new ArrayList<Planet>();

			for(Planet planet : this.getAllPlanets()) {
				Planet newPlanet = new Planet(planet.getID(), planet.getOwner(), planet.getNumShips(), planet.getGrowthRate(), planet.getX(), planet.getY());
				totalPlanets.add(newPlanet);
			}
			return new SimulatedPlanetWars(totalPlanets);
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


