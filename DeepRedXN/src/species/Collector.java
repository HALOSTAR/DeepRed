package species;

import game.Board;
import game.Result;

public class Collector {
	private PlayerGen playerGen;
	public Collector(PlayerGen p){
		this.playerGen = p;
	}
	/*
	 * result r = result from competing against some opponent
	 * b = final state of board in said game
	 * playing as = playerId in said game
	 * playerIndex = playerIndex of this generation (eg myCompetingPlayer = players[playerIndex])
	 */
	public void takeResult(Result r, int playingAs, Board b, int playerIndex) {
		//playing as = this player's int on board
		//TODO: This is essentially collector, here is one possible implementation: 
		if(r.isUnfinished()){
			//GAME UNFINISHED
			double myDistFromGoal = Species.getEndGameScore(b, playingAs);
			double otherDistFromGoal = Species.getEndGameScore(b, 3-playingAs);
			double gameScore = (otherDistFromGoal - myDistFromGoal)/(myDistFromGoal + otherDistFromGoal);
			//gameScore is in range -1 -> 1, -1=> other won, 1 --> win
			(playerGen.getCurrentGeneration()[playerIndex]).addScore(gameScore);
		}else{
			
			boolean win = r.winner==playingAs;
			if(win)
				(playerGen.getCurrentGeneration()[playerIndex]).addScore(1);
			else 
				(playerGen.getCurrentGeneration()[playerIndex]).addScore(-1);
		}	
	}
}
