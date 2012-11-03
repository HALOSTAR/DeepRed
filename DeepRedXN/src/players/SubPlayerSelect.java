package players;

import deprecated.LogicTree;
import game.Move;
public class SubPlayerSelect extends Player{
	private Player[] subPlayers;
	private LogicTree logic; 
	public SubPlayerSelect(LogicTree l, Player[] subPlayers){ //a random logicTreeTestPlayer
		//requires: size of subPlayers is 2^n where n is the number of outputs in l
		this.subPlayers=subPlayers;
		this.logic=l;
	}
	public Move request_move(int[] args) {
		boolean[] out = logic.eval(board);
		//convert out from binary to decimal to determine subplayer
		int decimal = 0;
		for(boolean b : out){
			int t = 0;if(b)t=1;
			decimal=decimal*2+t;
		}
		Player p = subPlayers[decimal];
		p.board=board;
		return 	p.request_move(args);
	}
	
}
