package Calculator;

public class TestCalculator {
    public void testCalc(){
        Calculator calculator = new Calculator();
        float result = calculator.Result("1+1+2+3+4");
        if (result != 11) {
            System.out.print("testCulc 1 work wrong");
        }

        result = calculator.Result("1*2*3+1");
        if (result != 7) {
            System.out.print("testCulc 2 work wrong");
        }

        result = calculator.Result("2+2*(2+2)");
        if (result != 10) {
            System.out.print("testCulc 3 work wrong");
        }
        result = calculator.Result("((3+2)*3+5)/5");
        if (result != 4) {
            System.out.print("testCulc 4 work wrong");
        }
    }
}
