package finite_automachine.dfa;

import common.exceptions.UnknownOperationException;
import finite_automachine.nfa.NFA;
import finite_automachine.nfa.states.NFAState;

import java.util.*;

public class DFA {
    private DFAState entry;


    public DFA(String regExp) throws UnknownOperationException,CloneNotSupportedException
    {
        this(new NFA(regExp));
    }

    public DFA(NFA nfa) {
        HashSet<Character> characterSet=nfa.getCharacterSet();
        NFAState nfaState=nfa.getEntry();
        entry=new DFAState(nfaState);
        Queue<DFAState> queue=new LinkedList<>();
        queue.add(entry);
        HashSet<DFAState> set=new HashSet<>();
        set.add(entry);
        while(!queue.isEmpty())
        {
            DFAState state=queue.remove();
            for (char c : characterSet)
            {
                DFAState tmp=new DFAState(state,c); //生成一个状态move(state,c)
                for(DFAState dfaState:set) //检查状态是否已经存在
                    if(dfaState.equals(tmp)) //
                    {
                        state.addNextStates(c,dfaState);
                        tmp=null;
                        break;
                    }
                if(tmp!=null)
                {
                    state.addNextStates(c,tmp);
                    queue.add(tmp);
                    set.add(tmp);
                }
            }
        }
    }

    public boolean match(String s)
    {
        DFAState state=entry;
        int index=0,length=s.length();
        while(index<length)
        {
            char c=s.charAt(index);
            if(state.hasPathTo(c))
            {
                state=state.getNext(c);
                index++;
            }
            else return false;
        }
       return state.end();
    }


}
