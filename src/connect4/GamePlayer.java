package connect4;

import java.util.ArrayList;

public class GamePlayer {
    //Variable that holds the maximum depth the MiniMax algorithm will reach for this player

    private int maxDepth;
    //Variable that holds which letter this player controls
    
    public GamePlayer() {
        maxDepth = 5;
            }

    public GamePlayer(int maxDepth) {
        this.maxDepth = maxDepth;
        
    }

    //Initiates the MiniMax algorithm
    public Move MiniMax(State state) 
    {
        //If the 1 plays then it wants to MAXimize the heuristics value           
        if (state.getTurn()) 
        {           
            return max(new State(state), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } //If the -1 plays then it wants to MINimize the heuristics value
        else 
        {
            return min(new State(state), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    // The max and min functions are called interchangingly, one after another until a max depth is reached
    public Move max(State state, int depth, int alpha, int beta) 
    {
        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
        if (state.isFinal()) 
        {
            Move lastmove = new Move(state.getLastMove().getRow(), state.getLastMove().getCol(), state.getValue());
            return lastmove;
        } 
        else if (depth == maxDepth) 
        {
            Move lastmove = new Move(state.getLastMove().getRow(), state.getLastMove().getCol(), state.heuristic());
            return lastmove;
        }
        //The children-moves of the state are calculated
        ArrayList<State> children = new ArrayList<State>(state.createChildren());
        Move maxMove = new Move(Integer.MIN_VALUE);
        for (State child : children) {
            //And for each child min is called, on a lower depth
            if (child != null) {
                Move move = min(child, depth + 1, alpha, beta);
                if(maxMove.getValue() >= beta)
                        return maxMove; 
                alpha = Math.max(alpha, maxMove.getValue());  
                //The child-move with the greatest value is selected and returned by max
                if (move.getValue() >= maxMove.getValue()) {         
                    maxMove.setRow(child.getLastMove().getRow());
                    maxMove.setCol(child.getLastMove().getCol());
                    maxMove.setValue(move.getValue());
                }                                  
            }
        }
        return maxMove;
    }

    //Min works similarly to max
    public Move min(State state, int depth, int alpha, int beta) {        

        if (state.isFinal()) {
            Move lastMove = new Move(state.getLastMove().getRow(), state.getLastMove().getCol(), state.getValue());
            return lastMove;
        }
        else if(depth == maxDepth) {
            Move lastMove = new Move(state.getLastMove().getRow(), state.getLastMove().getCol(), state.heuristic());
            return lastMove;
        }
        ArrayList<State> children = new ArrayList<State>(state.createChildren());
        Move minMove = new Move(Integer.MAX_VALUE);
        for (State child : children) {
            if (child != null) {
                Move move = max(child, depth + 1, alpha, beta);
                if(minMove.getValue() <= alpha)
                        return minMove;
                beta = Math.min(beta, minMove.getValue());
                
                if (move.getValue() <= minMove.getValue()) {                                        
                    minMove.setRow(child.getLastMove().getRow());
                    minMove.setCol(child.getLastMove().getCol());
                    minMove.setValue(move.getValue());                   
                }                
            }
        }        
        return minMove;
    }
}
