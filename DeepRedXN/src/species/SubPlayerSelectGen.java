package species;

import deprecated.LogicTree;
import deprecated.LogicTree1;
import players.EndGameScoreMaxPlayer1;
import players.Greedy;
import players.NeuralNetPlayer;
import players.Player;
import players.Stupid;
import players.SubPlayerSelect;

public class SubPlayerSelectGen extends PlayerGen {
	
	//Possible subplayers: 
		//endGameScoreMax(1,2,3)
		//NeuralNet (Spec1)
		//Greedy
		//Stupid
		//???
	public Player[] makePlayers(int numToMake) {
		Player[] p= new Player[numToMake];
		for(int i = 0; i < numToMake; i++){
			int pow = 3;
			int numSub = (int)Math.pow(2,pow);
			LogicTree l = new LogicTree1(pow, 10, 2, 4);
			Player[] subPlayers = new Player[numSub];
			for(int j = 0; j < numSub; j++)	subPlayers[j]=randomPlayer();
			p[i]=new SubPlayerSelect(l,subPlayers);
		}
		return p;
	}
	private Player randomPlayer() {
		int i = (int)(100*Math.random());
		if(i < 10)return new Stupid(0);
		if(i < 25)return new Greedy();
		if(i < 40)return new EndGameScoreMaxPlayer1();
		if(i < 55)return new EndGameScoreMaxPlayer1();
		return new NeuralNetPlayer(new Species1());
		
		
	}
	@Override
	public void evolve(int stage) {
	}

}

