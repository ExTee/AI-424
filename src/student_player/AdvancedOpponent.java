package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Random;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class AdvancedOpponent extends HusPlayer {

	   /* Random rand = new Random();

	    public StudentPlayer() { super("260638099"); }

	    
	    public HusMove chooseMove(HusBoardState board_state)
	    {
	        // Pick a random move from the set of legal moves.
	        ArrayList<HusMove> moves = board_state.getLegalMoves();
	        HusMove move = moves.get(rand.nextInt(moves.size()));
	        return move;
	    }
	}*/
    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
   public AdvancedOpponent() { super("260638099"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
   public static int numSeeds(int[] pits){
   	int n = 0;
   	for (int i=0; i< pits.length; i++){
   		n+= pits[i];
   	}
   	return n;
   }
   public static double evaluate(HusBoardState board_state, int[] pits){
   	double value=0;
   	for(int i=0; i<pits.length;i++){
   		
   		/*if(i >= HusBoardState.BOARD_WIDTH && i < 2 * HusBoardState.BOARD_WIDTH){
           // Pit is not in the inner row, so no capture.
   			if(pits[i]!=0){
       			int n = pits[31-i];
       			value-=10.0*(double)n;
       		}
   			else
   			value+=10*(double)pits[31-i];
   		}
   		else*/
   			value+= (double)pits[i];
   			
   	}
   	
   	
   return value;
   }
   public static int[] max(HusBoardState board_state, int depth, int playerID, int opponentID, double alpha, double beta, int turncounter){
   	ArrayList<HusMove> moves = board_state.getLegalMoves();
   	if (depth==0){
   		/*double [] my_seeds_new= new double[moves.size()];

           int maxIndex=0;
           double maxFunction=0;
           
           for(int i =0; i<moves.size(); i++){
           	HusMove move = moves.get(i);
           	HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
           
           	cloned_board_state.move(move);
           	
           	int[][] cloned_pits=cloned_board_state.getPits();
           	int[] my_pits_new = cloned_pits[playerID];
           	
           
       	
           	my_seeds_new[i]= MyTools.evaluate(cloned_board_state,my_pits_new);
           	
           
           	if (my_seeds_new[i]>maxFunction){
           		maxIndex=i;
           		maxFunction=my_seeds_new[i];
           	}
           }*/
   		
   		int[][] cloned_pits=board_state.getPits();
       	int[] my_pits_new = cloned_pits[playerID];
   		
           int[] max = new int[2];
           max[0]=0;        
           max[1]=(int)evaluate(board_state,my_pits_new);
           return max;
   		
   	}
   	else{
   		
   		double score;

           int maxIndex=0;
           //double maxFunction=0;
           
           if (turncounter%2==0){
           
           	for(int i =0; i<moves.size(); i++){
           		HusMove move = moves.get(i);
           		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
           
           		cloned_board_state.move(move);
           	
           		//int[][] cloned_pits=cloned_board_state.getPits();
           		//int[] my_pits_new = cloned_pits[playerID];
           	
           	
       	
           		score= 96-max(cloned_board_state, (depth-1) , opponentID , playerID, alpha,beta,(turncounter+1))[1];
           	
           
           		if (score>alpha){
           			maxIndex=i;
           			alpha = score;
           			if (alpha>=beta){
           				break;
           			}
           			//maxFunction=my_seeds_new[i];
           		}
           	}
           	int[] max = new int[2];
           	max[0]=maxIndex;
           	max[1]=(int)alpha;
           	return max;
           
           }
           else{
               
           	for(int i =0; i<moves.size(); i++){
           		HusMove move = moves.get(i);
           		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
           
           		cloned_board_state.move(move);
           	
           		//int[][] cloned_pits=cloned_board_state.getPits();
           		//int[] my_pits_new = cloned_pits[playerID];
           	
           	
       	
           		score= 96-max(cloned_board_state, (depth-1) ,opponentID , playerID, alpha,beta,(turncounter+1))[1];
           	
           
           		if (score<beta){
           			maxIndex=i;
           			beta = score;
           			if (alpha>=beta){
           				break;
           			}
           			//maxFunction=my_seeds_new[i];
           		}
           	}
           	int[] max = new int[2];
           	max[0]=maxIndex;
           	max[1]=(int)beta;
           	return max;
           
           }
   		
   	}
   	
      /* else{
   		int max =0;
   		for(int j =0; j<moves.size();j++){
   			max =0;
   			HusBoardState temp_board_state = (HusBoardState) board_state.clone();
   			temp_board_state.move(moves.get(j));
   			if(max(temp_board_state, depth-1 , opponentID , playerID)[1]>max){
   				max=max(temp_board_state, depth-1 , opponentID , playerID)[1];
   				bestmove=max(temp_board_state, depth-1 , opponentID , playerID)[0];
   			}
   			
   			
   		}
   		return max()
   	}*/
       
   
   	
   }
   
   public static int MonteCarloMove(HusBoardState board_state, int playerID){
   	ArrayList<HusMove> moves = board_state.getLegalMoves();
   	Random rn = new Random();
   	int gamecounter=0;
   	
   	int[]MonteCarloTreeNumerator= new int[moves.size()];
   	int[]MonteCarloTreeDenominator= new int[moves.size()];
   	while (gamecounter<1000){
   	int turncounter=0;
   	int i = rn.nextInt(moves.size());
   	HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
       
		cloned_board_state.move(moves.get(i));
		ArrayList<HusMove> clonedmoves= cloned_board_state.getLegalMoves();
   		while(clonedmoves.size()!=0){
   			int j = rn.nextInt(clonedmoves.size());
   			cloned_board_state.move(moves.get(j));
   			clonedmoves= cloned_board_state.getLegalMoves();
   			turncounter++;
   		}
   		if (turncounter%2==0){
   			MonteCarloTreeNumerator[i]++;
   		}
   		MonteCarloTreeDenominator[i]++;
   		
   	}
   	int indexMax=0;
   	double valueMax=0;
   	for(int j =0; j<moves.size();j++){
   		double currentvalue=(double)MonteCarloTreeNumerator[j]/(double)MonteCarloTreeDenominator[j];
   		if (currentvalue>valueMax){
   			valueMax=currentvalue;
   			indexMax=j;
   		}
   	}
   	
   	return indexMax;
   	
   }
   public HusMove chooseMove(HusBoardState board_state)
    {
    	//System.out.println("Hello");
    	//long start = System.currentTimeMillis();
    	//long end = start + 20; // 1500 ms
    	
        // Get the contents of the pits so we can use it to make decisions.
        int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
        int[] my_pits = pits[player_id];
        int my_seeds= numSeeds(my_pits);
        int[] op_pits = pits[opponent_id];

        // Use code stored in ``mytools`` package.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        if (moves.size()>20){
        	int monteCarloIndex= MonteCarloMove(board_state, 0);
        	HusMove move = moves.get(monteCarloIndex);
        	return move;
        }
        int maxIndex=max(board_state, 7, player_id, opponent_id,0,96,0)[0];
        
       
        HusMove move = moves.get(maxIndex);

        return move;
    }
}
