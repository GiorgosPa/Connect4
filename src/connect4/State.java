/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.util.*;

/**
 *
 * @author Giorgos
 */
public class State {
    
    private Move lastmove;
    private int state[][];
    private boolean myturn;
    private int value ;
    private boolean tie;
    private ArrayList<State> children; // child[1] antisixi me kinisi sti 2i stili ...

    public State(){
        tie = false;
        myturn = true;
        value = 0;
        lastmove = new Move();
        children = new ArrayList<State>();
        state = new int[6][7];
        for (int row=0;row<6;row++){
            for (int col=0;col<7;col++)
                state[row][col]=0;
        }
    }
    
   public State(State newstate){
        //arraycopy
        this.tie=newstate.tie;
        this.children = new ArrayList<State>(newstate.children);
        this.state = new int[6][7];
        for (int row=0;row<6;row++)
                for (int col=0;col<7;col++)
                    this.state[row][col]=newstate.state[row][col]; 
        this.myturn=newstate.myturn;
        this.value=newstate.value;
        this.lastmove=newstate.lastmove;        
    }
   
   public State clone(){
        State newstate = new State(); 
        newstate.tie=this.tie;
        newstate.children = new ArrayList<State>();
        newstate.state = new int[6][7];
        //arraycopy
        for (int row=0;row<6;row++)
                for (int col=0;col<7;col++)
                    newstate.state[row][col]=this.state[row][col]; 
        newstate.myturn=this.myturn;
        newstate.value=this.value;
        newstate.lastmove=this.lastmove;
        return newstate;
    }
    
    public boolean getTurn(){
        return myturn;
    }

    public void setTurn(boolean myturn){
        myturn = this.myturn;
    }

    public ArrayList<State> getChildren(){
        return children;
    }
    
    public ArrayList<State> createChildren(){
       for(int col=0;col<7;col++){// gia ta 7 paidia
            
            if(state[0][col]!=0)//An den einai 0 exei simplirothei i col+1 stili kai den epitrepete i kinisi
            {
                children.add(null);
                continue;
            }
            
            State child = this.clone();//ta paidia ginonte antigrafa tou gonea
            if(myturn)
                child.myturn=false;// allazei to myturn
            else
                child.myturn=true;// allazei to myturn
            
            //vriskei tin teleftea seira tis stilis col+1 pou einai keni
            for(int row=5;row>=0;row--){                
                if(child.state[row][col]==0)
                    if(myturn){
                        child.state[row][col]=1;//vazei to pouli stin k+1 stili
                        child.lastmove = new Move(row,col) ;                       
                        break;
                    }
                    else{
                        child.state[row][col]=-1;//vazei to pouli stin k+1 stili
                        child.lastmove = new Move(row,col) ;
                        break;
                    }
            }
            this.children.add(child);
        }
        return children;
    }
    
     public boolean isFinal() 
     {
        //Checking if there is a horizontal score4
        for (int row = 0; row < 6; row++) 
            for (int col = 0; col < 4; col++)  //4 giati an doume tis epomenes 3 theseis ftanei apo 4-6
                if (state[row][col] != 0
                        && state[row][col] == state[row][col + 1]
                        && state[row][col + 1] == state[row][col + 2]
                        && state[row][col + 2] == state[row][col + 3]) 
                {
                    if(state[row][col]==1)
                        this.value=Integer.MAX_VALUE;
                    else
                        this.value=Integer.MIN_VALUE;
                    return true;
                }
                  
        //Checking if there is a vertical score4
        for (int col = 0; col < 7; col++) 
            for (int row = 0; row < 3; row++)   //3 giati an doume tis epomenes 3 theseis ftanei apo 3-5
                if (state[row][col] != 0
                        && state[row][col] == state[row + 1][col]
                        && state[row + 1][col] == state[row + 2][col]
                        && state[row + 2][col] == state[row + 3][col]) 
                {
                    if(state[row][col]==1)
                        this.value=Integer.MAX_VALUE;
                    else
                        this.value=Integer.MIN_VALUE;
                    return true;
                }      
        
        //Checking if there is a diagonal score4
        for (int row = 0; row < 3; row++) 
            for (int col = 0; col < 4; col++) 
                if (state[row][col] != 0
                        && state[row][col] == state[row + 1][col + 1]
                        && state[row + 1][col + 1] == state[row + 2][col + 2]
                        && state[row + 2][col + 2] == state[row + 3][col + 3]) 
                {
                    if(state[row][col]==1)
                        this.value=Integer.MAX_VALUE;
                    else
                        this.value=Integer.MIN_VALUE;
                    return true;
                }
                    
        for (int row = 0; row < 3; row++) 
            for (int col = 3; col < 7; col++) 
                if (state[row][col] != 0
                        && state[row][col] == state[row + 1][col - 1]
                        && state[row + 1][col - 1] == state[row + 2][col - 2]
                        && state[row + 2][col - 2] == state[row + 3][col - 3]) 
                {
                    if(state[row][col]==1)
                        this.value=Integer.MAX_VALUE;
                    else
                        this.value=Integer.MIN_VALUE;
                    return true;
                }            
        
        //Checking if there is no empty tile -- tie
       int counter = 0;
       for (int col = 0; col < 7; col++) {
                if (state[0][col] != 0) {
                    counter++; //gemates stiles
                }
       }
       if(counter==7)
       {
           tie=true;
           value = 0;
           return true;
       }
       return false;
    }
    
    public State getChild(int k){
        return children.get(k);
    }
    
    public String toString(){
        String output = "";
        for (int i=0;i<6;i++){
            output += "\n";
            for (int j=0;j<7;j++)
                output = output + state[i][j] + "\t";
        }
        return output;
    }
    
    public int heuristic(){
        value=0;        
        int[] weightlist = {1,1,2,3,2,1,1};
        int cellweight=0;
        int weight3=100; // varos 3adas
        int weight2=50; //  varos 2adas
        
        //ipologizei ta varh ton kelion os eksis
        // 1    1   2   3   2   1   1
        // 2    2   4   6   4   2   2
        // 3    3   6   9   6   3   3
        // 3    3   6   9   6   3   3
        // 2    2   4   6   4   2   2
        // 1    1   2   3   2   1   1
        int count = 0;
        for (int row=0;row<6;row++)
            for (int col=0;col<7;col++)
            {
                if(row<3)
                    cellweight = state[row][col]* weightlist[col]*(++count);
                else 
                    cellweight = state[row][col]* weightlist[col]*(--count); 
                value+=cellweight;
            }
                
        //katheta
        for(int col=0;col<7;col++)
        {
            for(int row=5;row>2;row--)
            {
                //3ades
                //1110
                if((state[row][col]!=0)&&(state[row][col]==state[row-1][col])&&(state[row][col]==state[row-2][col])&&(state[row-3][col]==0))
                    value+=state[row][col]*weight3;
                //2ades
                //1100
                else if((state[row][col]!=0)&&(state[row][col]==state[row-1][col])&&(state[row-2][col]==0)&&(state[row-3][col]==0))
                    value+=state[row][col]*weight2;
            }
        }
        //orizontia
        for(int row=0;row<6;row++)
        {
            for(int col=0;col<4;col++)
            {
                //3ades
                //1110
                if((state[row][col]!=0)&&(state[row][col]==state[row][col+1])&&(state[row][col]==state[row][col+2])&&(state[row][col+3]==0))
                    value+=state[row][col]*weight3; 
                //0111
                if((state[row][col]==0)&&(state[row][col+1]!=0)&&(state[row][col+1]==state[row][col+2])&&(state[row][col+2]==state[row][col+3]))
                    value+=state[row][col+1]*weight3;                 
                //1011
                if((state[row][col+1]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row][col+2])&&(state[row][col]==state[row][col+3]))
                    value+=state[row][col]*weight3; 
                //1101
                if((state[row][col+2]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row][col+1])&&(state[row][col]==state[row][col+3]))
                    value+=state[row][col]*weight3;                
                //2ades
                //1100
                if((state[row][col]!=0)&&(state[row][col]==state[row][col+1])&&(state[row][col+2]==0)&&(state[row][col+3]==0))
                    value= state[row][col]*weight2 + value;
                //0110
                if((state[row][col]==0)&&(state[row][col+1]!=0)&&(state[row][col+1]==state[row][col+2])&&(state[row][col+3]==0))
                    value=state[row][col+1]*weight2 + value;
                
                //0011
                if((state[row][col+2]!=0)&&(state[row][col+2]==state[row][col+3])&&(state[row][col+1]==0)&&(state[row][col]==0))
                    value+=state[row][col+2]*weight2;                
                //0101
                if((state[row][col+1]!=0)&&(state[row][col+1]==state[row][col+3])&&(state[row][col+2]==0)&&(state[row][col]==0))
                    value+=state[row][col+1]*weight2;
                //1001
                if((state[row][col]!=0)&&(state[row][col]==state[row][col+3])&&(state[row][col+1]==0)&&(state[row][col+2]==0))
                    value+=state[row][col]*weight2;
                //1010
                if((state[row][col]!=0)&&(state[row][col]==state[row][col+2])&&(state[row][col+1]==0)&&(state[row][col+3]==0))
                    value+=state[row][col]*weight2;                
            }
        }
        //diagonia pros ta kato
        for(int row=0;row<3;row++)
        {
            for(int col=0;col<4;col++)
            {
                //3ades
                //1110
                if((state[row][col]!=0)&&(state[row][col]==state[row+1][col+1])&&(state[row][col]==state[row+2][col+2])&&(state[row+3][col+3]==0))
                    value+=state[row][col]*weight3;
                //0111
                else if((state[row][col]==0)&&(state[row+1][col+1]!=0)&&(state[row+1][col+1]==state[row+2][col+2])&&(state[row+2][col+2]==state[row+3][col+3]))
                    value+=state[row+1][col+1]*weight3;                
                //1011
                else if((state[row+1][col+1]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row+2][col+2])&&(state[row][col]==state[row+3][col+3]))
                    value+=state[row][col]*weight3;
                //1101
                else if((state[row+2][col+2]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row+1][col+1])&&(state[row][col]==state[row+3][col+3]))
                    value+=state[row][col]*weight3;
                //2ades
                //1100
                else if((state[row][col]!=0)&&(state[row][col]==state[row+1][col+1])&&(state[row+2][col+2]==0)&&(state[row+3][col+3]==0))
                    value+=state[row][col]*weight2;
                //0110
                else if((state[row][col]==0)&&(state[row+1][col+1]!=0)&&(state[row+1][col+1]==state[row+2][col+2])&&(state[row+3][col+3]==0))
                    value+=state[row+1][col+1]*weight2;
                //0011
                else if((state[row+2][col+2]!=0)&&(state[row+2][col+2]==state[row+3][col+3])&&(state[row+1][col+1]==0)&&(state[row][col]==0))
                    value+=state[row+2][col+2]*weight2;                
                //0101
                else if((state[row+1][col+1]!=0)&&(state[row+1][col+1]==state[row+3][col+3])&&(state[row+2][col+2]==0)&&(state[row][col]==0))
                    value+=state[row+1][col+1]*weight2;
                //1001
                else if((state[row][col]!=0)&&(state[row][col]==state[row+3][col+3])&&(state[row+1][col+1]==0)&&(state[row+2][col+2]==0))
                    value+=state[row][col]*weight2;
                //1010
                else if((state[row][col]!=0)&&(state[row+2][col+2]==state[row][col])&&(state[row+1][col+1]==0)&&(state[row+3][col+3]==0))
                    value+=state[row][col]*weight2;
                
            }
        }        
        //diagonia pros ta pano        
        for(int row=5;row>2;row--)
        {
            for(int col=0;col<4;col++)
            {
                //3ades
                //1110
                if((state[row][col]!=0)&&(state[row][col]==state[row-1][col+1])&&(state[row][col]==state[row-2][col+2])&&(state[row-3][col+3]==0))
                    value+=state[row][col]*weight3;
                //0111
                else if((state[row][col]==0)&&(state[row-1][col+1]!=0)&&(state[row-1][col+1]==state[row-2][col+2])&&(state[row-2][col+2]==state[row-3][col+3]))
                    value+=state[row-1][col+1]*weight3;
                //1011
                else if((state[row-1][col+1]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row-2][col+2])&&(state[row][col]==state[row-3][col+3]))
                    value+=state[row][col]*weight3;
                //1101
                else if((state[row-2][col+2]==0)&&(state[row][col]!=0)&&(state[row][col]==state[row-1][col+1])&&(state[row][col]==state[row-3][col+3]))
                    value+=state[row][col]*weight3;                
                //2ades
                //1100
                else if((state[row][col]!=0)&&(state[row][col]==state[row-1][col+1])&&(state[row-2][col+2]==0)&&(state[row-3][col+3]==0))
                    value+=state[row][col]*weight2;
                //0110
                else if((state[row][col]==0)&&(state[row-1][col+1]!=0)&&(state[row-1][col+1]==state[row-2][col+2])&&(state[row-3][col+3]==0))
                    value+=state[row-1][col+1]*weight2;
                //0011
                else if((state[row-2][col+2]!=0)&&(state[row-2][col+2]==state[row-3][col+3])&&(state[row-1][col+1]==0)&&(state[row][col]==0))
                    value+=state[row-2][col+2]*weight2; 
                 //0101
                else if((state[row-1][col+1]!=0)&&(state[row-1][col+1]==state[row-3][col+3])&&(state[row-2][col+2]==0)&&(state[row][col]==0))
                    value+=state[row-1][col+1]*weight2;
                //1001
                else if((state[row][col]!=0)&&(state[row][col]==state[row-3][col+3])&&(state[row-1][col+1]==0)&&(state[row-2][col+2]==0))
                    value+=state[row][col]*weight2;
                //1010
                else if((state[row][col]!=0)&&(state[row-2][col+2]==state[row][col])&&(state[row-1][col+1]==0)&&(state[row-3][col+3]==0))
                    value+=state[row][col]*weight2;
            }
        }            
        return value;
    }
    
    public int getValue(){
        return value;
    }
    
    public void setValue(int v){
        value = v;
    }
    public void setLastMove(Move lastMove)
    {
		this.lastmove.setRow(lastMove.getRow());
		this.lastmove.setCol(lastMove.getCol());
		this.lastmove.setValue(lastMove.getValue());
    }
    public Move getLastMove()
    {
		return lastmove;
    }
    public State makeMove(Move move)
    {
        return getChild(move.getCol());
    }
    public boolean getTie()
    {
        return tie;
    }
    
}
    
    

