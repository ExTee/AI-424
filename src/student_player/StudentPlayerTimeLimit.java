package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;

import student_player.mytools.MyTools;
import java.util.Stack;

/** A Hus player submitted by a student. */
public class StudentPlayerTimeLimit extends HusPlayer {
	
	//HusMove best_move;

    private static final HusMove Null = null;
    boolean terminate = false;

	/** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayerTimeLimit() { super("MK7NoRandom - TimeLimited iterative"); }

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

        Stack<MoveInfo> move_stack = new Stack<MoveInfo>();
       
        long start = System.currentTimeMillis();
        long end = 0;
        
        if (board_state.getTurnNumber() == 0 && board_state.firstPlayer() == opponent_id)
        {
        	if (board_state.getPits() [opponent_id] [6] == 0)
        	{
        		return new HusMove(21);
        	}
        }
        
        
        if (board_state.getTurnNumber() == 0)
        {
        	end = start + 28000;
        }
        else
        {
        	end = start + 1800;
        }
        //long end = start + 1800;
        
        int depth = 0;
       
        int i = 5;
        
        
       do
        {
    	   if (System.currentTimeMillis() > end)
    	   {
    		   terminate = true;
    	   }
        	
        	MoveInfo chosen = AB_minimax(Null, board_state, player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, i, end);
    		if (chosen != null)
    		{
    			move_stack.push(chosen);
    			//System.out.println("pushed move");
    		}
    		
    		depth = i;
    		i ++; 		
        	
        } while (terminate == false);
       
        System.out.println("Max depth is: " + depth);
  /*      
        for (int i = 5; i < 20; i ++)
        {
        	if (terminate == false)
        	{
        		MoveInfo chosen = AB_minimax(Null, board_state, player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, i, end);
        		if (chosen != null)
        		{
        			move_stack.push(chosen);
        		}
        		depth = i;
        	}
        	else
        	{
        		System.out.println("Max depth is: " + depth);
        		return move_stack.pop().move;
        	}
        }
*/
         

        //we return the latest stored move

        terminate = false;
        return move_stack.pop().move;
    }
    
    
	public double eval1(HusBoardState board_state)
	{
		int sum = 0;
		for(int i = 0; i < 32; i++)
		{
			
			int a = board_state.getPits() [player_id] [i];
			sum = sum + a;
		}

		return (double) sum;
	}
    
	public double eval(HusBoardState board_state)
	{
		
		
		//Contribution of the number of seeds for me
		int my_seeds_value = 0;
		int my_danger_seeds_value = 0;
		
		for(int i = 0; i < 32; i++)
		{
			int a = board_state.getPits() [player_id] [i];
			my_seeds_value = my_seeds_value + a;
			
			
			
			
			if ((board_state.getPits() [opponent_id] [i]) != 0 )
			{
				int inner_seeds = (board_state.getPits() [player_id] [i]);
				
				if (inner_seeds != 0)
				{
					my_danger_seeds_value = my_danger_seeds_value + inner_seeds + (board_state.getPits() [player_id] [31-i]);
				}
			}
			
			
			
			
			
		}
		//End my seeds
		/*
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
		*/
		
		double result_eval = (double) my_seeds_value - 0.5*((double) my_danger_seeds_value) ;
		
		return result_eval;
	}
	
	
	public MoveInfo AB_minimax(HusMove previous_move, HusBoardState current_state, int playerid, double alpha, double beta, int depth, long end)
	{
		double value;
	
		if (System.currentTimeMillis() >= end)
		{
			terminate = true;
			return null;
		}
		
		if (current_state.gameOver() || depth == 0)
		{

			double eval_result = eval1(current_state);
			
			
			return new MoveInfo(previous_move, eval_result);
		}
		
		ArrayList<HusMove> moves = current_state.getLegalMoves();  //children are the valid moves for this player
		
		//Max
		if (current_state.getTurnPlayer() == player_id)
		{
			for (HusMove test_move : moves)
			{
				//we simulate a move
				if (System.currentTimeMillis() >= end)
				{
					terminate = true;
					return null;
				}
				
				HusBoardState cloned_board_state = (HusBoardState) current_state.clone();
		        cloned_board_state.move(test_move);
			
		        //System.out.println("Calling minimax on: " + opponent_id);
		        
		        MoveInfo simulate = AB_minimax( test_move, cloned_board_state, opponent_id, alpha, beta, depth -1 , end);
		        
				if (System.currentTimeMillis() >= end)
				{
					terminate = true;
					return null;
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
				if (System.currentTimeMillis() >= end)
				{
					terminate = true;
					return null;
				}
				HusBoardState cloned_board_state = (HusBoardState) current_state.clone();
		        cloned_board_state.move(test_move);
		       // System.out.println("Calling minimax on: " + player_id);
		        
				MoveInfo simulate = AB_minimax( test_move, cloned_board_state, player_id, alpha, beta, depth -1, end);
				
				if (System.currentTimeMillis() >= end)
				{
					terminate = true;
					return null;
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
	
}
/*
class MoveInfo
{
	
	HusMove move;
	double value;	
	
	public MoveInfo(HusMove move, double value)
	{
		this.move = move;
		this.value = value;
	}
}


//this is our minimax algorithm.
class Minimax
{
	//this is our evaluation function
	public static int eval(HusMove move)
	{
		return 5;
	}
	
	
	
	
	
	/*
	public static HusMove AB_minimax(HusBoardState current_state, int current_depth, int max_depth)
	{
		if (current_depth == max_depth)
		{
			return current_state.getLegalMoves().get(0); //placeholder
		}
		
		return current_state.getLegalMoves().get(0); //placeholder
	}
	
	
}*/













