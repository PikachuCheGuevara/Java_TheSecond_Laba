package Calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

enum LexemeType{
    BRACKET_LEFT, BRACKET_RIGHT,
    PLUS, MINUS, MUL, DIV,
    NUMBER,
    VAR,
    EOF;
}

class  Var{
    String name;
    String number;
    Var(String name, String number)
    {
        this.name = name;
        this.number = number;
    }
}
public class Calculator {

    ArrayList<Var> variables = new ArrayList<Var>();

    String SetValue(String name) {
        System.out.print("Enter " + name + " \n");
        return new Scanner(System.in).next();
    }

    public  float Result(String expressionText){
        List<Lexeme> lexemes = lexAnalyze(expressionText);
        for(int i = 0; i < lexemes.size(); i++)
        {
            if(lexemes.get(i).type == LexemeType.VAR)
            {
                boolean t = true;
                for(int j = 0; j < variables.size(); j++)
                {
                    if(lexemes.get(i).value.equals( variables.get(j).name)){
                        lexemes.add(i,new Lexeme(LexemeType.NUMBER, variables.get(j).number));
                        lexemes.remove(i+1);
                        t  = false;
                    }
                }
                if(t)
                {
                    String value = SetValue(lexemes.get(i).value);
                    variables.add(new Var(lexemes.get(i).value, value));
                    lexemes.add(i,new Lexeme(LexemeType.NUMBER, value));
                    lexemes.remove(i+1);
                }

            }
        }
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        return expr(lexemeBuffer);
    }

    static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }
    }

    private static List<Lexeme> lexAnalyze(String expText){
     ArrayList<Lexeme> lexems = new ArrayList<>();
     int pos = 0;
     while(pos < expText.length()){
         char c = expText.charAt(pos);
         switch (c) {
             case '(':
                 lexems.add(new Lexeme(LexemeType.BRACKET_LEFT, c));
                 pos++;
                 continue;
             case ')':
                 lexems.add(new Lexeme(LexemeType.BRACKET_RIGHT, c));
                 pos++;
                 continue;
             case '*':
                 lexems.add(new Lexeme(LexemeType.MUL, c));
                 pos++;
                 continue;
             case '/':
                 lexems.add(new Lexeme(LexemeType.DIV, c));
                 pos++;
                 continue;
             case '+':
                 lexems.add(new Lexeme(LexemeType.PLUS, c));
                 pos++;
                 continue;
             case '-':
                 lexems.add(new Lexeme(LexemeType.MINUS, c));
                 pos++;
                 continue;
             default:
                 if(c >= '0' &&  c <= '9')
                 {
                     StringBuilder sb = new StringBuilder();
                     do{
                         sb.append(c);
                         pos++;
                         if(pos>=expText.length())
                             break;
                         c = expText.charAt(pos);
                     }while(c > '0' && c < '9');
                     lexems.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                 }
                 else if(c >= 'a' && c <='z') {
                     StringBuilder sb = new StringBuilder();
                     do{
                         sb.append(c);
                         pos++;
                         if(pos>=expText.length())
                             break;
                         c = expText.charAt(pos);
                     }while(c >= 'a' && c <= 'z');
                     lexems.add(new Lexeme(LexemeType.VAR, sb.toString()));
             }else{
                     if(c != ' ')
                         throw  new RuntimeException("Uncexpexted charaster: " + c);
                     pos++;
                 }
         }
     }
     lexems.add(new Lexeme(LexemeType.EOF,""));
     return  lexems;
    }

    private static class LexemeBuffer {
        private int pos;
        private Vector<Var> variables;
        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }
        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    private static float expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    private static float plusminus(LexemeBuffer lexemes) {
        float value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case PLUS:
                    value += multdiv(lexemes);
                    break;
                case MINUS:
                    value -= multdiv(lexemes);
                    break;
                case EOF:
                case BRACKET_RIGHT:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private static float multdiv(LexemeBuffer lexemes) {
        float value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case MUL:
                    value *= factor(lexemes);
                    break;
                case DIV:
                    value /= factor(lexemes);
                    break;
                case EOF:
                case BRACKET_RIGHT:
                case PLUS:
                case MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private static float factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case BRACKET_LEFT:
                float value = plusminus(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.BRACKET_RIGHT) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + lexemes.getPos());
        }
    }
}
