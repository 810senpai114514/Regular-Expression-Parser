package finite_automachine.dfa;

import finite_automachine.nfa.states.NFAState;


import java.util.*;

import static symbols.Constant.EPSILON;

public class DFAState {
    protected boolean end;
    HashSet<NFAState> set;
    HashMap<Character,DFAState> nextStates;


    public DFAState(NFAState entry)
    {
        set=new HashSet<>();
        nextStates=new HashMap<>();
        epsilonClosure(entry);
    }

    public DFAState(DFAState dfaState,char input)
    {
        set=new HashSet<>();
        nextStates=new HashMap<>();
        move(dfaState,input);
    }



    private void move(DFAState state,char input)
    {
        for(NFAState nfaState:state.set)  //寻找接收input的NFA状态
            set.addAll(nfaState.getNextStates(input));
        List<NFAState> list=new ArrayList<>();
        for(NFAState nfaState:set){
            if(nfaState.end()) end=true;
            list.add(nfaState);
        }

        for(NFAState nfaState:list)
            epsilonClosure(nfaState);
    }



    private void epsilonClosure(NFAState start)
    {
        set.add(start);
        List<NFAState> next=start.getNextStates(EPSILON);
        for(NFAState state:next)//深度优先搜索Epsilon
        {
            if(!set.contains(state))
            {
                set.add(state);
                if(state.end()) end=true;
                epsilonClosure(state);
            }
        }
    }

    public void addNextStates(char input,DFAState next) //输入为input时指向next
    {
        nextStates.put(input,next);
    }

    public boolean equals(DFAState state)
    {
        if(set.size()!=state.set.size()) return false;
        for(NFAState nfaState:set)
            if(!state.set.contains(nfaState))
                return false;
        return true;
    }

    public boolean hasPathTo(char c)
    {
        return nextStates.containsKey(c);
    }

    public DFAState getNext(char c)
    {
        return nextStates.get(c);
    }

    public boolean end()
    {
        return end;
    }

}
