package symbols;

import common.exceptions.UnknownOperationException;

import static symbols.Constant.EPSILON;

public enum RegulationExpressionSymbols {
    REPETITION(0,'*'),
    POSITIVE_REPETITION(1,'+'),
    TRANSFER(0,'/'),
    MERGE(1,'-'),
    CONCATENATE(1,'.'),
    ALTERNATION(2,'|'),
    LEFT_BRACKET(3,'('),
    RIGHT_BRACKET(4,')'),
    NOT_A_OPERATION(10,EPSILON);

    final int priority;
    final char operation;

    RegulationExpressionSymbols(int priority, char operation) {
        this.priority = priority;
        this.operation = operation;
    }

    public int getPriority() {
        return priority;
    }

    public char getOperation() {
        return operation;
    }

    public static RegulationExpressionSymbols getOperation(char c)
    {
        return switch (c)
        {
            case '*' ->REPETITION;
            case '|' ->ALTERNATION;
            case '(' ->LEFT_BRACKET;
            case ')' ->RIGHT_BRACKET;
            case '.' ->CONCATENATE;
            case '-' ->MERGE;
            case '+' -> POSITIVE_REPETITION;
            case '/' -> TRANSFER;
            default -> NOT_A_OPERATION;
        };
    }

    public static boolean isOperation(char c)
    {
        return switch (c)
                {
                    case '*','|' ,'(',')','.','+','-','/'->true;
                    default -> false;
                };
    }
}
