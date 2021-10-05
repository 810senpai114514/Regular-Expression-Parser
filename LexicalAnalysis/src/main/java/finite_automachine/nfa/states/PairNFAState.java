package finite_automachine.nfa.states;


import java.util.List;


public class PairNFAState extends NFAState{
    private NFAState head;
    private NFAState tail;


    public PairNFAState(NFAState head, NFAState tail) {
        this.head = head;
        this.tail = tail;
    }



    @Override
    public void setEnd() {
        tail.setEnd();
    }

    @Override
    public void addNextState(NFAState singleNFAState) {
        tail.addNextState(singleNFAState);
    }


    @Override
    public char getAccept() {
        return head.getAccept();
    }

    @Override
    public List<NFAState> getNextStates() {
        return head.getNextStates();
    }


    @Override
    public List<NFAState> getNextStates(char c) {
        return head.getNextStates(c);
    }


    @Override
    public NFAState getHead() {
        return head;
    }

    @Override
    public NFAState getTail() {
        return tail;
    }

    @Override
    public boolean end() {
        return tail.end();
    }

}
