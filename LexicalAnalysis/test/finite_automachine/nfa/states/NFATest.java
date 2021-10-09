package finite_automachine.nfa.states;


import finite_automachine.dfa.DFA;
import finite_automachine.nfa.NFA;
import org.junit.jupiter.api.Test;

import static symbols.Constant.DIGITS;


class NFATest {

    @Test
    void parseTest() throws Exception{
        NFA nfa=new NFA(DIGITS+"/."+DIGITS);
        DFA dfa=new DFA(nfa);
        System.out.println(dfa.match("3.14159"));
        System.out.println(dfa.match("."));
        System.out.println(dfa.match("KBTIT"));
        System.out.println(dfa.match(""));
        System.out.println("检查结束");

    }

}