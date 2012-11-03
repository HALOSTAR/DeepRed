package game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Moves {
	/*
	 * @Author Lisa
	 * Generates all legal moves given a set of constraints and a board state
	 */
	public static Move[][][][][][] movesWMarble;
	public static Move[][][][] movesWoutMarble;
	static int cx = 0;
	static int cy = 0;
/*
 * Expand move arrays to accomodate a bigger or differently shaped boards	
 */
public static void initMoves(int x, int y){
		if(x > cx || y > cy){
			int nx = Math.max(cx, x);
			int ny = Math.max(cy, y);
			Move[][][][][][] newMovesWMarble = new Move[nx][ny][nx][ny][nx][ny];
			Move[][][][] newMovesWoutMarble=new Move[nx][ny][nx][ny];	
			for(int x1 = 0; x1 < cx; x1++){
				for(int x2=0;x2 < cx; x2++){
					for(int y1 = 0; y1 < cy; y1++){
						for(int y2 = 0;y2 < cy; y2++){
						//	System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
							Move t = movesWoutMarble[x1][y1][x2][y2];
							newMovesWoutMarble[x1][y1][x2][y2] = t;
							for(int x3=0; x3<cx; x3++){
								for(int y3=0;y3<cy; y3++){
									newMovesWMarble[x1][y1][x2][y2][x3][y3] = movesWMarble[x1][y1][x2][y2][x3][y3];
								}
							}
						}
					}
				}
			}
			movesWMarble = newMovesWMarble;
			movesWoutMarble = newMovesWoutMarble;
			cx = nx;cy=ny;
		}
	}
	
	public static HashSet<Integer> moves= new HashSet<Integer>();
	/*
	 * Returns a list of all legal moves w/ the given properties
	 * MaxMovesOnly --only include moves where a marble travels the farthest it can
	 * extendWithMarble -- use special marble to extend moves
	 * forwardMovesOnly -- only include moves that make forward progress toward the goal
	 * blockOpponentWithMarble -- include moves that use the special marble to block opponent progress
	 */
	public static ArrayList<Move> getMoves(Board b, boolean maxMovesOnly, 
			boolean forwardMovesOnly, boolean extendWithMarble, boolean blockOpponentWithMarble){	
		extendWithMarble = extendWithMarble && (b.getMarbles()[b.getTurn()-1] > 0);
		HashSet<Integer> locs = b.getPlayerLocs()[b.getTurn()-1];
		ArrayList<Move> all = new ArrayList<Move>();
		ArrayList<int[]> blockLocs =null;//getBlockLocs(b, blockOpponentWithMarble);
		HashSet<Integer> visited = new HashSet<Integer>();
		HashMap<Integer, ArrayList<int[]>> extensions = new HashMap<Integer, ArrayList<int[]>>();
		for(int h : locs){
			int[] loc = b.hashToLoc(h);
			int initDistFromGoal = b.getDistancesFromGoal().get(h)[b.getTurn()-1];
			visited.clear();
			all.addAll(getAllMoves(loc[0], loc[1],h,  b, maxMovesOnly, forwardMovesOnly, 
					extendWithMarble, blockLocs, initDistFromGoal, visited, extensions));
		}
		return all;
	}
	/*
	 * list of all moves satisfying constraints that involve moving marble starting at (r,c) with hashcode h
	 */
	private static ArrayList<Move> getAllMoves(int r, int c, int h,
			Board b, boolean maxMovesOnly, boolean forwardMovesOnly, boolean extendWithMarble,
			ArrayList<int[]> blockLocs, int initDistFromGoal,
			HashSet<Integer> visited, HashMap<Integer, ArrayList<int[]>> extensions) {
		//TODO: blockLocs
		visited.clear();
		extensions.clear();
		
		int[][] imm = getImmediate(r, c);//Distances will all be 1 -- do jumps first
		for(int[] l : imm){
			int h2 = b.locToHash(l[0], l[1]);
			if(b.getAtLoc(h2) == Board.getEmpty()){
				visited.add(h2);
			}
		}
		jump2(b, r, c, h, visited, extensions, maxMovesOnly, extendWithMarble, null, initDistFromGoal);
		//Now make moves from visited and extensions
		visited.remove(h);
		
		return makeMovesFromEndLocs(r, c, b, visited,extensions, maxMovesOnly,forwardMovesOnly, initDistFromGoal);
	}
	/*
	 * get moves starting at (r,c) ending at a location in visited, 
	 * at location in extensions (using special marble)
	 * first constrains moves by maxMovesOnly and forwardMovesOnly
	 */
	private static ArrayList<Move> makeMovesFromEndLocs(int r, int c, Board b,
			HashSet<Integer> visited,
			HashMap<Integer, ArrayList<int[]>> extensions,
			boolean maxMovesOnly, boolean forwardMovesOnly, int initDistFromGoal) {

		for(int i : visited)extensions.remove(i);
		
		HashSet<Integer> endLocs = new HashSet<Integer>();
		HashMap<Integer, ArrayList<int[]>> endLocs2 = new HashMap<Integer, ArrayList<int[]>>();
		if(maxMovesOnly){
			getMinDist(visited, endLocs,b);
			getMinDist(extensions, endLocs2, b);
		}else if(forwardMovesOnly){
			getForward(visited,endLocs,b, initDistFromGoal);
			getForward(extensions, endLocs2, b, initDistFromGoal);
		}else{
			endLocs = visited;
			endLocs2=extensions;
		}
		return createMoves(r,c,b,visited,extensions);
    }
	/*
	 * see above -- visited and extensions have already been constrained
	 */
	private static ArrayList<Move> createMoves(int r, int c, Board b,
			HashSet<Integer> visited,
			HashMap<Integer, ArrayList<int[]>> extensions) {
		ArrayList<Move> m = new ArrayList<Move>();
		
		int[] start = new int[]{r,c};
		
		for(int h : visited){
			int[] end = b.hashToLoc(h);
			m.add(Move.getMove(start, end, null));
		}
		for(int h : extensions.keySet()){
			int[] end = b.hashToLoc(h);
			for(int[] marble : extensions.get(h)){
				m.add(Move.getMove(start,end, marble));
			}
		}
		return m;
	}
	/*
	 * get move that ends closest to the goal
	 */
	private static void getMinDist(
			HashMap<Integer, ArrayList<int[]>> extensions,
			HashMap<Integer, ArrayList<int[]>> endLocs2, Board b) {
		int minDist = Integer.MAX_VALUE;
		for(int loc : extensions.keySet()){
			int dist = b.getDistancesFromGoal().get(loc)[b.getTurn()-1];
			if(dist < minDist){
				endLocs2.clear();
				endLocs2.put(loc, extensions.get(loc));
			}else if(dist==minDist){
				endLocs2.put(loc, extensions.get(loc));
			}	
		}
	}
	/*
	 * get move that ends closest to the goal 
	 */
	private static void getMinDist(HashSet<Integer> visited,
			HashSet<Integer> endLocs, Board b) {
		int minDist = Integer.MAX_VALUE;
		for(int loc : visited){
			int dist = b.getDistancesFromGoal().get(loc)[b.getTurn()-1];
			if(dist < minDist){
				endLocs.clear();
				endLocs.add(loc);
			}else if(dist==minDist){
				endLocs.add(loc);
			}	
		}
	}
	/*
	 * eliminate all backward moves
	 */
	private static void getForward(
			HashMap<Integer, ArrayList<int[]>> extensions,
			HashMap<Integer, ArrayList<int[]>> endLocs2, Board b, int initDist) {
		endLocs2.clear();
		for(int loc : extensions.keySet()){
			int dist = b.getDistancesFromGoal().get(loc)[b.getTurn()];
			if(dist <= initDist)endLocs2.put(loc, extensions.get(loc));
		}
	}
	/*
	 * eliminate all backward moves
	 */
	private static void getForward(HashSet<Integer> visited,
			HashSet<Integer> endLocs, Board b, int initDist) {
		endLocs.clear();
		for(int loc : visited){
			int dist = b.getDistancesFromGoal().get(loc)[b.getTurn()-1];
			if(dist <= initDist)endLocs.add(loc);
		}
	}
	/*
	 * recursively jump to all possible locations from (r,c)
	 */
	private static void jump2(Board b, int r, int c, int h,
			HashSet<Integer> visited,HashMap<Integer, ArrayList<int[]>> extensions,
			boolean maxMovesOnly, boolean extendWithMarble, int[] mLoc, int initDistFromGoal) {
		//mLoc = marbleLoc, null if marble has not been placed
		if(visited.contains(h))return;
		ArrayList<int[]> t = extensions.get(h);
		if(t != null && mLoc != null && contains(t,mLoc))return;
		if(mLoc!=null){
			if(!extensions.containsKey(h))extensions.put(h, new ArrayList<int[]>());
			extensions.get(h).add(mLoc);
		}else{
			visited.add(h);
		}
		int[][] jLocs = getJumpLocs(r,c);
		for(int[] l : jLocs){
			int r1 = l[0];
			int c1 = l[1];
			int r2 = l[2];
			int c2 = l[3];
			int h2 = b.locToHash(r2, c2);
			if(!visited.contains(h2)){
			//Jump over (r1,c1) to (r2,c2)
				if(b.getAtLoc(new int[]{r2,c2})== Board.getEmpty()){ // can jump into r2
					int[] mLoc2 = new int[]{r1,c1};
					int at1 = b.getAtLoc(mLoc2);
					if(at1 == Board.getEmpty()){ // nothing to jump over
						if(extendWithMarble && mLoc==null){
							//place marble at (r1,c1) then jump over it to (r2,c2)
						//	extensions.put(h, new int[]{r1,c1});//TODO: Check if this has already been done
						
							jump2(b, r2,c2, h2, visited, extensions, maxMovesOnly, true,mLoc2, initDistFromGoal);
						}
					}else if(at1 != Board.getInvalidlocation()){
						//jump to (r2,c2)
						//TODO: add to visited or extended
						
						jump2(b, r2,c2,h2, visited,extensions, maxMovesOnly,extendWithMarble,mLoc, initDistFromGoal);
					}
			}
			}
		}	
	}
	/*
	 * does t contain mLoc?
	 */
	private static boolean contains(ArrayList<int[]> t, int[] mLoc) {
		for(int[] k : t){
			if(k[0]==mLoc[0] && k[1]==mLoc[1])return true;
		}return false;
	}
	/*
	 * get locations that may inhibit opponent if spec marble is placed in them
	 */
	/*private static ArrayList<int[]> getBlockLocs(Board b,
			boolean blockOpponentWithMarble) {
		ArrayList<int[]> blockLocs = new ArrayList<int[]>();
		if(!blockOpponentWithMarble)return blockLocs;
		else{//TODO
			return blockLocs;
			
		}
	}*/
	public static int[][] getJumpLocs(int r, int c){
		return new int[][] {{r,c-2,r,c-4},{r-1,c-1,r-2,c-2},
				{r-1,c+1,r-2,c+2},{r,c+2,r,c+4},{r+1,c+1,r+2,c+2},{r+1,c-1,r+2,c-2}};
	}
	public static int[][] getImmediate(int r, int c){
		return new int[][] {{r,c-2},{r-1,c-1},{r-1,c+1},{r,c+2},{r+1,c+1},{r+1,c-1}};
	}
}
