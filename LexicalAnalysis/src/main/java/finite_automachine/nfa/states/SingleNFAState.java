package finite_automachine.nfa.states;

import java.util.*;

public class SingleNFAState extends NFAState {
    private boolean end;
    private char accept;
    protected List<NFAState> nextNFAStates =new ArrayList<>();


    @Override
    public void addNextState(NFAState singleNFAState)
    {
        nextNFAStates.add(singleNFAState);
    }

    public SingleNFAState(char accept)
    {
        this.end=false;
        this.accept=accept;
    }

    public SingleNFAState(char accept, boolean end)
    {
        this.end=end;
        this.accept=accept;
    }

    @Override
    public NFAState getHead() {
        return this;
    }

    @Override
    public NFAState getTail() {
        return this;
    }

    @Override
    public boolean end()
    {
        return end;
    }

    public void setEnd()
    {
        end=true;
    }

    public List<NFAState> getNextStates(char c)
    {
        List<NFAState> result=new ArrayList<>();
        for(NFAState state : nextNFAStates)
            if(state.getAccept()==c)
                result.add(state);

        return result;
    }

    @Override
    public char getAccept() {
        return accept;
    }



}
