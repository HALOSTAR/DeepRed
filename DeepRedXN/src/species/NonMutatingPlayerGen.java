package species;

/*
 * @Author Lisa
 * Returns same player every time, for use w/ tester -- greedybot / other static bots
 */
import players.Player;

public class NonMutatingPlayerGen extends PlayerGen{
	Player player;
	Player[] last;
	public NonMutatingPlayerGen(Player p){
		this.player = p;
		this.setCurrentGeneration(new Player[]{p});
	}
	public Player[] makePlayers(int numToMake) {
		last = new Player[numToMake];
		for(int i = 0;  i < numToMake; i++)last[i]=player;
		return last;
	}
	@Override
	public void evolve(int stage) {
		
	}

}
