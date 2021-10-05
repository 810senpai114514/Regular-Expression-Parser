package finite_automachine.nfa.states;

import finite_automachine.nfa.NFA;

import java.util.List;

import static symbols.Constant.EPSILON;

public abstract class NFAState{
    public static NFAState merge(NFAState left, NFAState right) {
        left.getTail().addNextState(right.getHead());
        NFAState result=new PairNFAState(left,right);
        return result;
    }

    public static NFAState concatenate(NFAState left, NFAState right) {
        left.getTail().addNextState(right.getHead());
        return right;
    }


    public static NFAState repeat(NFAState state) {
        state.getTail().addNextState(state.getHead());
        NFAState head=new SingleNFAState(EPSILON);
        head.addNextState(state.getHead());
        NFAState tail=new SingleNFAState(EPSILON);
        state.getTail().addNextState(tail);
        head.addNextState(tail);
        return new PairNFAState(head,tail);
    }

    public static NFAState alternate(NFAState up, NFAState down) {
        NFAState head=new SingleNFAState(EPSILON);
        NFAState tail=new SingleNFAState(EPSILON);

        NFAState upHead=new SingleNFAState(EPSILON);
        NFAState downHead=new SingleNFAState(EPSILON);
        upHead.addNextState(up);
        downHead.addNextState(down);

        head.addNextState(upHead.getHead()); //头连接上部
        head.addNextState(downHead.getHead());//头连接下部

        up.getTail().addNextState(tail);//上部连接尾
        down.getTail().addNextState(tail);//下部连接尾

        return new PairNFAState(head,tail);
    }

    public abstract List<NFAState> getNextStates();
    public abstract List<NFAState> getNextStates(char c);

    public abstract NFAState getHead();
    public abstract NFAState getTail();
    public abstract boolean end();


    public abstract void addNextState(NFAState singleNFAState);


    public abstract void setEnd();

    public abstract char getAccept();

}
