package finite_automachine.nfa.states;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class setTest {
    @Test
    public void test()
    {
        HashSet<Character> set1=new HashSet<>();
        HashSet<Character> set2=new HashSet<>();

        set1.add('a');
        set2.add('b');
        System.out.println(set1.equals(set2));

    }
}
