import Calculator.Calculator;

public class Main {
    public static void main(String[] args) {
        float num = new Calculator().Result("(1+2+3)*2+3/6");
        
        System.out.print("res = " + num);
    }
}
