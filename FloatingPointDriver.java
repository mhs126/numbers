package numbers;

import javax.swing.text.html.parser.Parser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

/* Driver to build and run FloatingPointParser on input readers */
public final class FloatingPointDriver {

    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        FloatingPointDriver driver = new FloatingPointDriver();
        Optional<Double> result = driver.runFloatingPointParser(input);
        printOutput(result);
    }

    // Prints out the given Double (or "Invalid Input" if given empty result)
    public final static void printOutput(Optional<Double> result) {
        System.out.println(result.isPresent() ? result.get() : "Invalid Input");
    }

    // Retrieves input from the given BufferedReader and parses it to a Double
    public final Optional<Double> runFloatingPointParser(BufferedReader input) {
        FloatingPointParser parser = getFloatingPointParser(input);
        return parser.isValidInput() ? Optional.of(parser.parseDouble()) : Optional.empty();
    }

    // Reads from the input reader and builds a parser
    private final FloatingPointParser getFloatingPointParser(BufferedReader input) {
        FloatingPointParser parser = null;
        if (input != null) {
            String line = null;
            try {
                line = input.readLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (line == null)
                throw new NullPointerException("No input recieved.");
            else {
                StringBuilder builder = removeWhiteSpace(line);
                parser = makeUpperCase(builder);
            }
        }
        if (parser == null) {
            return FloatingPointParser.build("input that is really bad");
        } else {
            return parser;
        }
    }

    public static StringBuilder removeWhiteSpace(String line){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length(); i++)
            if (!Character.isWhitespace(line.charAt(i)))
                builder.append(line.charAt(i));
        return builder;
    }

    public static FloatingPointParser makeUpperCase(StringBuilder builder){
        String line = builder.toString();
        if (line.length() != 0) {
            builder = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                if (!Character.isUpperCase(line.charAt(i)))
                    builder.append(Character.toUpperCase(line.charAt(i)));
                else
                    builder.append(line.charAt(i));
            }
            return FloatingPointParser.build(line);
        } else {
            return FloatingPointParser.build("bad input");
        }
    }

}