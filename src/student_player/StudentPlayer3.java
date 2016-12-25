package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Collections;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer3 extends HusPlayer {
	
	//HusMove best_move;

    private static final HusMove Null = null;


	/** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer3() { super("MKX_No_Randomized_+Danger+21+Timer"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState board_state)
    {
        // Get the contents of the pits so we can use it to make decisions.
        int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
        int[] my_pits = pits[player_id];
        int[] op_pits = pits[opponent_id];

        // Use code stored in ``mytools`` package.
        MyTools.getSomething();

       
        
        MoveInfo chosen;
        if (board_state.getTurnNumber() == 0 ) //&& board_state.getPits() [opponent_id] [6] == 0 && player_id == 1)
        {
        	//HusMove first_move = new HusMove(21);
        	//return first_move;
        	long start = System.currentTimeMillis();
        	long end = start + 29000;
        	       	
        	//System.out.println(start + "to :" + end);
        	chosen = AB_minimax(Null, board_state, player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 80, end);
        }
        else 
        {
        	long start = System.currentTimeMillis();
        	long end = start + 1800;
        	       	
        	//System.out.println(start + "to :" + end);
        	chosen = AB_minimax(Null, board_state, player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 80, end);
        }
        
        //System.out.println("Final value: " + result_alpha);
        //usMove chosen_move = best_move;
       // best_move = null;
        
        
/*       
        // Get the legal moves for the current board state.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        
        HusMove chosenMove = moves.get(0);
        int currentMax = board_state.getPits() [player_id] [moves.get(0).getPit()];
        
        for (int i = 0; i < moves.size(); i ++)
        {
        	int currentCount = board_state.getPits() [player_id] [moves.get(i).getPit()];
        	//System.out.println(currentCount);
        	if (currentCount > currentMax)
        	{
        		chosenMove = moves.get(i);
        		currentMax = currentCount;
        	}
      		     
        }

        HusMove move = chosenMove;

        // We can see the effects of a move like this...
        //HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
        //cloned_board_state.move(move);

        // But since this is a placeholder algorithm, we won't act on that information.
         
*/
        return chosen.move;
    }
    
    
	public double eval(HusBoardState board_state)
	{
		
		
		//Contribution of the number of seeds for me
		int my_seeds_value = 0;
		for(int i = 0; i < 32; i++)
		{
			int a = board_state.getPits() [player_id] [i];
			my_seeds_value = my_seeds_value + a;
		}
		//End my seeds
		
		//Seeds in danger of being captured by opponent
		int my_danger_seeds_value = 0;
		for (int i = 16; i<32; i++)
		{
			if ((board_state.getPits() [opponent_id] [i]) != 0 )
			{
				int inner_seeds = (board_state.getPits() [player_id] [i]);
				
				if (inner_seeds != 0)
				{
					my_danger_seeds_value = my_danger_seeds_value + inner_seeds + (board_state.getPits() [player_id] [31-i]);
				}
			}
		}
		//end my seeds in danger
		
		
		double result_eval = (double) my_seeds_value - 0.0*((double) my_danger_seeds_value) ;
		
		return result_eval;
	}
    
    
	public MoveInfo AB_minimax(HusMove previous_move, HusBoardState current_state, int playerid, double alpha, double beta, int depth, long end_time)
	{
		double value;
	
			
		if (current_state.gameOver() || depth == 0)
		{
			double eval_result = eval(current_state);
			
			
			return new MoveInfo(previous_move, eval_result);
		}
		
		ArrayList<HusMove> moves = current_state.getLegalMoves();  //children are the valid moves for this player
		//Collections.shuffle(moves);
		//Max
		if (current_state.getTurnPlayer() == player_id)
		{
			for (HusMove test_move : moves)
			{
				//we simulate a move
				HusBoardState cloned_board_state = (HusBoardState) current_state.clone();
		        cloned_board_state.move(test_move);
			
		       // System.out.println("Calling minimax on: " + opponent_id);
		        
		        MoveInfo simulate;
		        if (System.currentTimeMillis() <= end_time)
		        {
		        	//System.out.println("Still going: " + depth + System.currentTimeMillis());
		        	simulate = AB_minimax( test_move, cloned_board_state, opponent_id, alpha, beta, depth -1 ,end_time);
		        }
		        else
		        {
		        	//System.out.println("About to time out, exiting: " + depth );
		        	simulate = AB_minimax( test_move, cloned_board_state, opponent_id, alpha, beta, 0 ,end_time);
		        }

				value = simulate.value;
				
				if (value > alpha)
				{
					alpha = value;
					previous_move = test_move;
					
				}
				
				if (alpha >= beta)
				{
					break;//prune beta
				}
					
			}
			//System.out.println("Alpha returned: " + alpha);
			
			return new MoveInfo(previous_move, alpha);
		}
		//Min
		else	
		{
			for (HusMove test_move : moves)
			{
				HusBoardState cloned_board_state = (HusBoardState) current_state.clone();
		        cloned_board_state.move(test_move);
		       // System.out.println("Calling minimax on: " + player_id);
		        
				
				
		        MoveInfo simulate;
		        if (System.currentTimeMillis() <= end_time)
		        {
		        	simulate = AB_minimax( test_move, cloned_board_state, player_id, alpha, beta, depth-1 , end_time);
		        }
		        else
		        {
		        	//System.out.println("About to time out, exiting: " + depth );
		        	simulate = AB_minimax( test_move, cloned_board_state, player_id, alpha, beta, 0 , end_time);
		        }
				
				
				
				value = simulate.value;
				
				if (value < beta)
				{					
					beta = value;
					previous_move = test_move;
				}
		
				if (alpha >= beta)
				{
					break; //prune off alpha
				}
				
			}
			//System.out.println("Beta returned: " + beta);
			
			return new MoveInfo(previous_move, beta);
		}
	
	}	
	
	public HusMove Monte_Carlo(HusBoardState current_state)
	{
		
		
		return null;
		
	}
	
}












