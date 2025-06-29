import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EnhancedConsoleCalculator {
    private static final Logger LOGGER = Logger.getLogger(EnhancedConsoleCalculator.class.getName());
    private final Scanner scanner;

    public EnhancedConsoleCalculator() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        EnhancedConsoleCalculator calculator = new EnhancedConsoleCalculator();
        calculator.start();
    }

    public void start() {
        while (true) {
            displayMenu();
            String choice = getUserInput("Enter your choice (1-8): ");
            if (choice.equals("8")) {
                LOGGER.info("Calculator shutting down.");
                System.out.println("Goodbye!");
                break;
            }
            processOperation(choice);
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n=== Enhanced Console Calculator ===");
        System.out.println("1. Addition");
        System.out.println("2. Subtraction");
        System.out.println("3. Multiplication");
        System.out.println("4. Division");
        System.out.println("5. Power");
        System.out.println("6. Square Root");
        System.out.println("7. Logarithm (base 10)");
        System.out.println("8. Exit");
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void processOperation(String choice) {
        try {
            Operation operation = OperationFactory.getOperation(choice);
            if (operation == null) {
                LOGGER.warning("Invalid operation selected: " + choice);
                System.out.println("Invalid choice. Please select a number between 1 and 8.");
                return;
            }

            double[] inputs = operation.requiresTwoOperands()
                    ? getTwoNumbers()
                    : new double[]{getSingleNumber()};
            double result = operation.execute(inputs);
            System.out.printf("Result: %.2f%n", result);
            LOGGER.info("Operation " + choice + " executed successfully with result: " + result);
        } catch (CalculatorException e) {
            LOGGER.severe("Error during operation: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: ", e);
            System.out.println("An unexpected error occurred. Please try again.");
        }
    }

    private double[] getTwoNumbers() throws CalculatorException {
        double num1 = getSingleNumber();
        double num2 = getSingleNumber();
        return new double[]{num1, num2};
    }

    private double getSingleNumber() throws CalculatorException {
        String input = getUserInput("Enter a number: ");
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new CalculatorException("Invalid number format: " + input);
        }
    }
}

interface Operation {
    double execute(double[] inputs) throws CalculatorException;
    boolean requiresTwoOperands();
}

class CalculatorException extends Exception {
    public CalculatorException(String message) {
        super(message);
    }
}

class OperationFactory {
    public static Operation getOperation(String choice) {
        switch (choice) {
            case "1": return new Addition();
            case "2": return new Subtraction();
            case "3": return new Multiplication();
            case "4": return new Division();
            case "5": return new Power();
            case "6": return new SquareRoot();
            case "7": return new Logarithm();
            default: return null;
        }
    }
}

class Addition implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 2) throw new CalculatorException("Addition requires two numbers.");
        return inputs[0] + inputs[1];
    }

    @Override
    public boolean requiresTwoOperands() {
        return true;
    }
}

class Subtraction implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 2) throw new CalculatorException("Subtraction requires two numbers.");
        return inputs[0] - inputs[1];
    }

    @Override
    public boolean requiresTwoOperands() {
        return true;
    }
}

class Multiplication implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 2) throw new CalculatorException("Multiplication requires two numbers.");
        return inputs[0] * inputs[1];
    }

    @Override
    public boolean requiresTwoOperands() {
        return true;
    }
}

class Division implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 2) throw new CalculatorException("Division requires two numbers.");
        if (inputs[1] == 0) throw new CalculatorException("Division by zero is not allowed.");
        return inputs[0] / inputs[1];
    }

    @Override
    public boolean requiresTwoOperands() {
        return true;
    }
}

class Power implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 2) throw new CalculatorException("Power requires two numbers.");
        return Math.pow(inputs[0], inputs[1]);
    }

    @Override
    public boolean requiresTwoOperands() {
        return true;
    }
}

class SquareRoot implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 1) throw new CalculatorException("Square root requires one number.");
        if (inputs[0] < 0) throw new CalculatorException("Square root of a negative number is not allowed.");
        return Math.sqrt(inputs[0]);
    }

    @Override
    public boolean requiresTwoOperands() {
        return false;
    }
}

class Logarithm implements Operation {
    @Override
    public double execute(double[] inputs) throws CalculatorException {
        if (inputs.length != 1) throw new CalculatorException("Logarithm requires one number.");
        if (inputs[0] <= 0) throw new CalculatorException("Logarithm of a non-positive number is not allowed.");
        return Math.log10(inputs[0]);
    }

    @Override
    public boolean requiresTwoOperands() {
        return false;
    }
}
