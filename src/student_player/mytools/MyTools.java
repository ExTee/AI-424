package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MyTools {

    public static double getSomething(){
        return Math.random();
        
    }
    
    public static HusMove Evaluate(ArrayList<HusMove> Moves, HusBoardState Board, int Depth, double Safety, int MCAmount, int Length){
    	HeuristicMove HDecision = null;
    	ArrayList<HeuristicMove> HMoves = AddHeuristic(Moves);
    	for(HeuristicMove m:HMoves){
    		ArrayList<HeuristicMove> TempMove = new ArrayList<HeuristicMove>();
    		TempMove.add(m);
    		HusBoardState Temp = (HusBoardState)Board.clone();
    		
    		DFS(m,TempMove,Temp,Depth,0,Safety);
    		MonteCarlo(m,Temp,MCAmount,Length);
    		
    		if(m.Win + m.Loss != 0){
    			if(m.Win == 0){
    				m.Win += 0.9;
    			}
    			m.Rating = (int)((double)m.Rating * (m.Win/(m.Win+m.Loss)));
    		}
    		
    		if(HDecision == null || HDecision.Rating < m.Rating){
    			HDecision = m;
    		}
    	}
    	HusMove Decision = HDecision.Move;
    	return Decision;
    }
    
    public static void DFS(HeuristicMove HM,ArrayList<HeuristicMove> HMs, HusBoardState B, int Depth, int CurrentDepth, double Safety){
    	if(!(CurrentDepth >= Depth)){
    		if(CurrentDepth%2 == 0){
    			for(HeuristicMove m:HMs){
    				HusBoardState Temp = (HusBoardState)B.clone();
    				Temp.move(m.Move);
    				ArrayList<HeuristicMove> MinMoves = HeuristicChoose(HM,Temp,Safety);
    				if(!(MinMoves.isEmpty())){
    					DFS(HM,MinMoves,Temp,Depth,CurrentDepth+1,Safety);
    				}
    				else{
    					HM.Win += 1.0;
    				}
    			}
    		}
    		else{
    			for(HeuristicMove m:HMs){
    				HusBoardState Temp = (HusBoardState)B.clone();
    				Temp.move(m.Move);
    				ArrayList<HeuristicMove> MaxMoves = HeuristicChoose(HM,Temp,1.0);
    				if(!(MaxMoves.isEmpty())){
    					DFS(HM,MaxMoves,Temp,Depth,CurrentDepth+1,Safety);
    				}
    				else{
    					HM.Loss += 1.0;
    				}
    			}
    		}
    	}
    	else{
    		if(!(HMs.isEmpty())){
    			if(HMs.get(0).Rating > HM.Rating){
    				HM.Rating = HMs.get(0).Rating;
    			}
    		}
    	}
    }
    
    public static ArrayList<HeuristicMove> HeuristicChoose(HeuristicMove HM, HusBoardState B, double Safety){
    	ArrayList<HusMove> Moves = B.getLegalMoves();
    	ArrayList<HeuristicMove> HMoves = AddHeuristic(Moves);
    	for(HeuristicMove H:HMoves){
    		HusBoardState Temp = (HusBoardState)B.clone();
    		int MyID = Temp.getTurnPlayer();
    		Temp.move(H.Move);
    		H.Rating = RateState(Temp, MyID);	
    	}
    	Collections.sort(HMoves);
    	int MoveAmount = Moves.size() - (int)Math.floor(Moves.size()*Safety);
    	for(int i = 0; i < MoveAmount;i++){
    		HMoves.remove(0);
    	}
    	return HMoves;
    }
    
    public static void MonteCarlo(HeuristicMove HM, HusBoardState B, int Amount, int Length){
    	double Win = 0;
    	double Total = (double)Amount;
    	
    	for(int i = 0; i < Amount; i++){
    		HusBoardState Temp = (HusBoardState)B.clone();
    		int Count = 0;
    		while(Temp.getWinner() != 1 && Temp.getWinner() != 0 && Count < Length){
    			if(Count == 0){
    				Temp.move(HM.Move);
    			}
    			else{
    				ArrayList<HusMove> Moves = Temp.getLegalMoves();
        			if(!(Moves.isEmpty())){
        				Temp.move(Moves.get(rand.nextInt(Moves.size())));
        			}
    			}
    			Count++;
    		}
    		
    		if(Temp.getWinner() == B.getTurnPlayer()){
    			Win += 1.0;
    		}
    		else if(Temp.getWinner() != (1-B.getTurnPlayer())){
    			if(RateState(Temp,Temp.getTurnPlayer()) > HM.Rating){
    				Win += 1.0;
    			}
    		}
    	}
    	double WinRate = Win/Total;
    	int temp = (int)((double)(HM.Rating) * WinRate);
		HM.Rating = temp;
    }

    public static int RateState(HusBoardState B, int MyID){
    	int OpID = 1-MyID;
    	int[][] pits = B.getPits();
		int[] my_pits = pits[MyID];
		int[] op_pits = pits[OpID];
		int MySeed = 0;
		int MyPlayable = 0;
		int OpPlayable = 0;
		int MyRisked = 0;
		for(int i = 0;i < my_pits.length;i++){
			MySeed += my_pits[i];
		}
		
		int Rating = MySeed;
		
		return Rating;
    }
    
    public static ArrayList<HeuristicMove> AddHeuristic(ArrayList<HusMove> M){
    	ArrayList<HeuristicMove> HM = new ArrayList<HeuristicMove>();
    	for(HusMove m:M){
    		HM.add(new HeuristicMove(m));
    	}
    	return HM;
    }
    
    static Random rand = new Random();
}

class HeuristicMove implements Comparable<HeuristicMove>{
	HusMove Move;
	int Rating;
	double Loss;
	double Win;
	
	HeuristicMove(HusMove M){
		this.Move = M;
		this.Rating = 0;
		this.Loss = 0.0;
		this.Win = 0.0;
	}

	@Override
	public int compareTo(HeuristicMove Other) {
		Integer R1 = (Integer)Rating;
		Integer R2 = (Integer)Other.Rating;
		return R1.compareTo(R2);
	}
}
