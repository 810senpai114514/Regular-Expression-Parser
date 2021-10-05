package finite_automachine.nfa;

import common.exceptions.UnknownOperationException;
import finite_automachine.nfa.states.NFAState;
import finite_automachine.nfa.states.SingleNFAState;
import symbols.RegulationExpressionSymbols;

import java.util.*;


import static finite_automachine.nfa.states.NFAState.*;
import static symbols.Constant.EPSILON;
import static symbols.RegulationExpressionSymbols.*;

public class NFA {
    private NFAState entry;
    private HashSet<Character> characterSet;

    public NFA(String regExp) throws UnknownOperationException, CloneNotSupportedException {
        Stack<NFAState> states =new Stack<>();
        entry=new SingleNFAState(EPSILON);
        states.push(entry);
        characterSet=new HashSet<>();
       Queue<Character> operations=parse(regExp);


       //以下是检测性代码

        System.out.println("检查后缀表达式");
        System.out.print(EPSILON);
        for(char c:operations)
            System.out.print(c);
        System.out.println();
        System.out.println("检查完成");
        System.out.println();

        /**
        *
        * 根据调度场生成的队列进行操作
        *
        * */
        while(!operations.isEmpty())
        {
            char c=operations.remove();
            if(isOperation(c))
            {
                RegulationExpressionSymbols operation=getOperation(c);
                switch (operation)
                {
                    case TRANSFER ->{
                        char transfer=operations.remove();
                        characterSet.add(transfer);
                        states.add(new SingleNFAState(transfer));
                    }
                    case REPETITION -> {
                        states.add(repeat(states.pop()));
                    }
                    case CONCATENATE ->{
                        NFAState right= states.pop();
                        NFAState left= states.pop();
                        states.add(concatenate(left,right));//连接后将状态头入队
                    }
                    case MERGE -> {
                        NFAState right= states.pop();
                        NFAState left= states.pop();
                        states.add(merge(left,right));
                    }
                    case ALTERNATION -> {
                        NFAState up= states.pop(); //上
                        NFAState down= states.pop();//下
                        states.add(alternate(up,down));
                    }

                }
            }
            else
                states.add(new SingleNFAState(c));
        }
        states.pop().setEnd();
        characterSet.remove(EPSILON);

    }

    public HashSet<Character> getCharacterSet() {
        return characterSet;
    }


    private Queue<Character> parse(String regExp) throws UnknownOperationException
    {
        /**
         * 以下正则表达式预处理
         *
         *
         * */
        List<Character> regularExpression=new ArrayList<>();

        regularExpression.add(CONCATENATE.getOperation());
        for(int i=0,length=regExp.length();i<length;i++)
        {
            char c=regExp.charAt(i);
            RegulationExpressionSymbols symbol=getOperation(c);

            /**
             *
             *
             *      遇见转义字符/时，再取出一个操作符，并作为接受字符生成状态
             *
             * */
            /*if(symbol==TRANSFER)
            {
                if(i>1) regularExpression.add(CONCATENATE.getOperation());
                regularExpression.add(c);
                i++;
                regularExpression.add(regExp.charAt(i));
                if(i<length-1)
                    regularExpression.add(CONCATENATE.getOperation());
            }*/
            /**
             *
             * 遇见+号，就将a+转化为等价的aa*形式
             *
             *
             * */
            if(symbol==POSITIVE_REPETITION)
            {
                char tmp=regExp.charAt(i-1);
                regularExpression.add(CONCATENATE.getOperation());
                if(!isOperation(tmp))
                    regularExpression.add(regularExpression.get(regularExpression.size()-2));
                else if(getOperation(tmp)==RIGHT_BRACKET)
                {
                    int index=regularExpression.size()-2;
                    Stack<Character> stack=new Stack<>();
                    char current;
                    do{
                        current=regularExpression.get(index--);
                        stack.push(current);
                    }while(current!=LEFT_BRACKET.getOperation());
                    while (!stack.isEmpty())
                        regularExpression.add(stack.pop());
                }
                regularExpression.add(REPETITION.getOperation());
            }


            /**
             *
             *      在适当的情况，添加连接符号.
             * */
            else if(i<length-1)
            {
                regularExpression.add(c);
                RegulationExpressionSymbols next=getOperation(regExp.charAt(i+1));
                if((symbol==NOT_A_OPERATION && ((next==NOT_A_OPERATION)||(next==LEFT_BRACKET)))
                        ||((symbol==REPETITION||symbol==RIGHT_BRACKET||symbol==POSITIVE_REPETITION)&&((next==NOT_A_OPERATION)||(next==LEFT_BRACKET))||(next==TRANSFER)))
                {

                    regularExpression.add(CONCATENATE.getOperation());
                }
            }

            /**
             *
             *      普通状态，直接加入结果容器
             * */
            else
                regularExpression.add(c);
        }

        //以下是检测性代码

        System.out.println("待处理的正则表达式是");
        for(char c:regularExpression)
            System.out.print(c);

        System.out.println();

        /**
         *
         * 通过迪科斯彻双栈法将中缀表达式变为后缀表达式
         * */
        Stack<RegulationExpressionSymbols> operators=new Stack<>(); //操作符栈
        Stack<Character> operations=new Stack<>(); //操作栈

        boolean hasLeftBracket=false;

        for(int i=0,size=regularExpression.size();i<size;i++)
        {
            char c=regularExpression.get(i);
            if(isOperation(c) ) {
                RegulationExpressionSymbols operation=getOperation(c);
                switch (operation) {
                    //左括号入栈
                    case LEFT_BRACKET -> {
                        operators.push(operation);
                        hasLeftBracket=true;
                    }
                    //右括号时，直到遇到左括号为止，一直出栈并压入操作栈
                    case RIGHT_BRACKET -> {
                        while(operators.peek()!=LEFT_BRACKET)
                        {
                            operation=operators.pop();
                            operations.push(operation.getOperation());
                        }
                        operators.pop();//左括号出栈
                        hasLeftBracket=false;
                    }case TRANSFER -> {
                        operations.push(TRANSFER.getOperation());
                        operations.push(regularExpression.get(++i));
                        operations.push(CONCATENATE.getOperation());
                    }
                    //其他操作符时
                    default -> {
                        while (true) {
                            //如果是空操作符栈，或者发现(，或者发现更高优先级的操作符
                            if (operators.isEmpty() || operation.getPriority() < operators.peek().getPriority()) {
                                if(hasLeftBracket&&operation==CONCATENATE)
                                    operation=MERGE;
                                operators.push(operation); //加入操作符栈
                                break;
                            }
                            else //入操作栈，重复扫描
                                operations.push(operators.pop().getOperation());
                        }
                    }
                }
            }

            //普通字符直接压入操作栈
            else {
                characterSet.add(c);
                operations.push(c);
            }
        }

        //如果操作符栈还有剩余，全部压入操作栈
        while(!operators.isEmpty()) {
            operations.push(operators.pop().getOperation());
        }
        //以队列的形式返回结果
        Queue<Character> list=new LinkedList<>();
        for(char c:operations)
            list.add(c);
        return list;
    }

    public NFAState getEntry() {
        return entry;
    }
}
