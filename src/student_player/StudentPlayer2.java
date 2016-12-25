package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer2 extends HusPlayer {
	
	//HusMove best_move;

    private static final HusMove Null = null;


	/** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer2() { super("MK7No_Randomized_no_danger+monte carlo"); }

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

        MoveInfo chosen = AB_minimax(Null, board_state, player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 7);
        /*
        if (board_state.getTurnNumber() == 0 )
        {
        	HusMove first_move = new HusMove(21);
        	return first_move;
        }
        */
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
		
		
		double result_eval = (double) my_seeds_value - 0.5*((double) my_danger_seeds_value) ;
		
		return result_eval;
	}
    
    
	public MoveInfo AB_minimax(HusMove previous_move, HusBoardState current_state, int playerid, double alpha, double beta, int depth)
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
		        
		        MoveInfo simulate = AB_minimax( test_move, cloned_board_state, opponent_id, alpha, beta, depth -1 );
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
		        
				MoveInfo simulate = AB_minimax( test_move, cloned_board_state, player_id, alpha, beta, depth -1 );
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
	
	public MonteCarloNode Monte_Carlo(MonteCarloNode curr_state)
	{
		ArrayList<HusMove> moves = curr_state.current_state.getLegalMoves();
		
		for (HusMove current_move : moves)
		{
			//initialize a new montecarlo node
			MonteCarloNode test_node = new MonteCarloNode(0.0,0.0, current_move, curr_state.current_state);
			
			//simulate and update
			test_node.UpdateNode(simulate(test_node));
		}
		
		
		
		return null;
		
	}
	
	public int simulate(MonteCarloNode node)
	{
		Random rand = new Random();
		HusBoardState board_state = node.current_state;
		
		while (!board_state.gameOver())
		{
			ArrayList<HusMove> moves = board_state.getLegalMoves();
			HusMove move = moves.get(rand.nextInt(moves.size()));
			board_state.move(move);
			
		}
		if (board_state.getWinner() == player_id)
		{
			return 1;
		}
		else if (board_state.getWinner() == opponent_id)
		{
			return -1;
		}
		else
		{
			return 0;
		}
		
	}
	
	
	class MonteCarloNode
	{
		double numerator;
		double denominator;
		double score = numerator/denominator;
		HusMove move_chosen;
		HusBoardState current_state;
		
		public MonteCarloNode(double numerator, double denominator, HusMove move_chosen, HusBoardState current_state)
		{
			this.numerator = numerator;
			this.denominator = denominator;
			this.move_chosen = move_chosen;
			this.current_state = current_state;
			
		}
		
		public void UpdateNode(double numerator_incr)
		{
			this.numerator = numerator + numerator_incr;
			this.denominator ++;
			this.score = numerator/denominator;
		}
	}
}












