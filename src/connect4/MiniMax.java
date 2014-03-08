/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

/**
 *
 * @author Giorgos
 */
public class MiniMax {
    
    public MiniMax(){}
    
    /*public State minimax(State s ,int deph)
    { 
        this.max(s,deph-1);
        State temp = new State();
        for (int i=0;i<7;i++)
            if(s.getValue()==s.getChild(i).getValue())
                temp=s.getChild(i);
        return temp;
    }
    public State maxmini(State s,int deph)
    {
        this.min(s,deph-1);
        State temp = new State();
        for (int i=0;i<7;i++)
            if(s.getValue()==s.getChild(i).getValue())
                temp=s.getChild(i);        
        return temp;
    }
    
    public void max(State s,int deph)
    {
        System.out.println("max :"+deph);
        System.out.println(s.toString());
        s.setValue(Integer.MIN_VALUE);
        s.createChildren();
        for (int i=0;i<7;i++)
        {            
            System.out.println("max for loop");
            if(s.getChild(i).isFinal())
                continue;
            if(deph==0){
                s.getChild(i).heuristic();
                continue;                              
            }
            this.min( s.getChild(i),deph -1);//ipologismos tis aksias tou kathe paidiou
            System.out.println("minvalue : " +  s.getChild(i).getValue());
            System.out.println( s.getChild(i).toString());
        }        
        s.setValue(s.getChild(0).getValue());
        for (State child : s.getChildren())
             if(s.getValue()<child.getValue())
                s.setValue(child.getValue());
    }
    
    public void min(State s,int deph)
    {
        System.out.println("min :" + deph);
        System.out.println(s.toString());
        s.setValue(Integer.MAX_VALUE);
        s.createChildren();
        for (int i=0;i<7;i++)
        {
            System.out.println("min for loop");
            if(s.getChild(i).isFinal())
                continue;
            if(deph==0){
                s.getChild(i).heuristic();
                System.out.println("value : " + s.getChild(i).getValue());
                System.out.println(s.getChild(i).toString());
                continue;
            }
            this.max(s.getChild(i),deph -1);//ipologismos tis aksias tou kathe paidiou
            System.out.println("maxvalue : " + s.getChild(i).getValue());
        }        
        s.setValue(s.getChild(0).getValue());
        for (State child : s.getChildren())
           if(s.getValue()>child.getValue())
                s.setValue(child.getValue());        
    }*/
    
    public State minimax(State s,int deph)
    {
        if(s.getTurn())
        {
            s.setValue(Integer.MIN_VALUE);
            max(s,deph-1);
            for(State child : s.getChildren())
                if(s.getValue() < child.getValue())
                    s=child;
                else if(s.getValue()==child.getValue()&& Math.random()>0.5)//an exoun tin idia aksia dialekse 1 paidi stin tixi
                    s=child;
        }
        else
        {
            s.setValue(Integer.MAX_VALUE);
            min(s,deph-1); 
            for(State child : s.getChildren())
                if(s.getValue() > child.getValue())
                    s=child;
            else if(s.getValue()==child.getValue()&& Math.random()>0.5)
                    s=child;
        }
        return s;
    }
    
    public int max(State s,int deph)
    {
        s.createChildren();
        for(State child : s.getChildren())
        {
            if(child.isFinal())
                continue;
            else if(deph==0)
                child.heuristic();
            else
                child.setValue(min(child,deph-1));
        }
        int a=Integer.MIN_VALUE;
        for(State child : s.getChildren())
            if(a<child.getValue())
                a=child.getValue(); 
        return a;
    }
    public int min(State s,int deph)
    {
        s.createChildren();
        for(State child : s.getChildren())
        {
            if(child.isFinal())
                continue;
            else if(deph==0)
                child.heuristic();
            else
                child.setValue(max(child,deph-1));
        }
        int a=Integer.MIN_VALUE;
        for(State child : s.getChildren())
            if(a>child.getValue())
                a=child.getValue(); 
        return a;
    }
    
    
}
