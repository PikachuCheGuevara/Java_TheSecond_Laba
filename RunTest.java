import Calculator.TestCalculator;

public class RunTest {
    public static void main(String[] args) {
        try {
            TestCalculator testCalculator = new TestCalculator();
            testCalculator.testCalc();
        } catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}
