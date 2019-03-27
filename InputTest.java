package numbers;

import static org.junit.Assert.*;

import org.junit.Test;

import numbers.DecimalInput.TestHook;

import java.io.*;

import java.io.BufferedReader;
import java.util.Optional;

/** Some example tests of parser **/
public class InputTest {

    // For using hook methods that are not object-specific
    private static final TestHook hook = new DecimalInput("").new TestHook();

    /** hasValidMiddlePadding tests **/
    /* Example: 1_234 -> valid */
    @Test
    public void test_padding_nominal() {
        assertTrue(hook.hasValidMiddlePadding("1_234"));
    }

    /* Example: 1__234 -> valid */
    @Test
    public void test_padding_long_underscore() {
        assertTrue(hook.hasValidMiddlePadding("1__234"));
    }

    /* Example: 12_34 -> invalid */
    @Test
    public void test_padding_bad_underscore() {
        assertFalse(hook.hasValidMiddlePadding("12_34"));
    }

    /* Example: _1_234 -> invalid */
    @Test
    public void test_padding_leading_underscore() {
        assertFalse(hook.hasValidMiddlePadding("_1_234"));
    }

    /* Example 1.0 -> true */
    @Test
    public void test_is_positive_number_nominal(){
        assertTrue(hook.isNumberPositive("1.0"));
    }

    /* Example +1.0 -> true */
    @Test
    public void test_is_positive_number_positive_sign(){
        assertTrue(hook.isNumberPositive("+1.0"));
    }

    /* Example -1.0 -> false */
    @Test
    public void test_is_positive_number_negative_sign(){
        assertFalse(hook.isNumberPositive("-1.0"));
    }

    @Test
    public void test_get_regex_of_nominal(){
        assertEquals("\\1", hook.getRegexOf('1'));
    }

    @Test
    public void test_get_regex_of_underscore(){
        assertEquals("\\_", hook.getRegexOf('_'));
    }

    @Test
    public void test_get_regex_of_decimal(){
        assertEquals("\\.", hook.getRegexOf('.'));
    }

    @Test
    public void test_is_valid_input_exponent(){
        FloatingPointParser parser = FloatingPointParser.build("10e1");
        assertTrue(parser.isValidInput());
    }

    @Test
    public void test_is_valid_input_decimal(){
        FloatingPointParser parser = FloatingPointParser.build("10.1");
        assertTrue(parser.isValidInput());
    }

    @Test
    public void test_is_valid_input_bad_input(){
        FloatingPointParser parser = FloatingPointParser.build("101");
        assertFalse(parser.isValidInput());
    }

    @Test
    public void test_is_valid_input_decimal_and_exponent(){
        FloatingPointParser parser = FloatingPointParser.build("10.1e2");
        assertTrue(parser.isValidInput());
    }

    @Test
    public void test_remove_padding_none(){
        assertEquals("10.1", hook.removePadding("10.1"));
    }

    @Test
    public void test_remove_padding_underscore(){
        assertEquals("1010.1", hook.removePadding("1_010.1"));
    }

    @Test
    public void test_remove_padding_comma(){
        assertEquals("1100.1", hook.removePadding("1,100.1"));
    }

    @Test
    public void test_has_valid_integer_exponent_nominal(){
        FloatingPointParser parser = FloatingPointParser.build("10.1");
        assertTrue(parser.isValidInput());
    }

    @Test
    public void test_has_valid_integer_exponent_valid(){
        FloatingPointParser parser = FloatingPointParser.build("10e10");
        assertTrue(parser.isValidInput());
    }

    @Test
    public void test_has_valid_integer_exponent_invalid(){
        FloatingPointParser parser = FloatingPointParser.build("10.1e1.2");
        assertFalse(parser.isValidInput());
    }

    @Test
    public void test_is_valid_input_no_exponent(){
        FloatingPointParser parser = FloatingPointParser.build("10e");
        assertFalse(parser.isValidInput());
    }

    @Test
    public void test_has_valid_leading_padding_nominal(){
        assertTrue(hook.hasValidLeadingPadding("10.1"));
    }

    @Test
    public void test_has_valid_leading_padding_good_underscore(){
        assertTrue(hook.hasValidLeadingPadding("1_110"));
    }

    @Test
    public void test_has_valid_leading_padding_bad_underscore(){
        assertFalse(hook.hasValidLeadingPadding("11_10"));
    }

    @Test
    public void test_has_valid_leading_padding_good_comma(){
        assertTrue(hook.hasValidLeadingPadding("1,110"));
    }

    @Test
    public void test_has_valid_leading_padding_bad_comma(){
        assertFalse(hook.hasValidLeadingPadding("11,10"));
    }

    @Test
    public void test_has_valid_leading_padding_bad_start_comma(){
        assertFalse(hook.hasValidLeadingPadding(",1110"));
    }

    @Test
    public void test_has_valid_leading_padding_bad_end_comma(){
        assertFalse(hook.hasValidLeadingPadding("1110,"));
    }

    @Test
    public void test_remove_sign_with_sign(){
        assertEquals("1.1", hook.removeSign("+1.1"));
    }


    @Test
    public void test_parse_double(){
        FloatingPointParser parser = FloatingPointParser.build("1.0");
        assertEquals(1.0, (double)parser.parseDouble());
    }

    @Test
    public void test_driver(){
        FloatingPointDriver driver = new FloatingPointDriver();
        BufferedReader reader = new BufferedReader(new StringReader("1.0"));
        //BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("1.0".getBytes())));
        driver.runFloatingPointParser(reader);
        assertEquals(Optional.of(1.0), driver.runFloatingPointParser(reader));
    }

    @Test
    public void test_parser_build(){
        FloatingPointParser parser = FloatingPointParser.build(null);
        FloatingPointParser parser2 = FloatingPointParser.build("");
        assertEquals(parser2, parser);
    }

}
