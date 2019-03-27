package numbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/* Validation class for simple decimal numbers (an integer with optional fractional chunk) */
/* Global Precondition: number is not null nor does it have leading or trailing spaces
 */
class DecimalInput {

    private static final char DECIMAL = '.';
    private static final char PADDING = '_';
    private static final char PADDING2 = ',';
    private static final String BADPADDING = "_,";
    private static final String  BADPADDING2 = ",,";
    private static final String BADPADDING3 = ",_";
    private static final Set<Character> SIGN_SET;
    private static final Set<Integer> VALID_CHAR_SET; // int set to match chars IntStreams
    private static final List<Character> paddingList = new ArrayList<>();
    private static final List<String> badpaddingList = new ArrayList<>();
    static { // Setup set of valid signs
        Set<Character> signs = new HashSet<>();
        signs.add('-');
        signs.add('+');
        SIGN_SET = Collections.unmodifiableSet(signs);
    }
    static { // Setup set of valid characters in a decimal
        Set<Character> validChars = new HashSet<>();
        validChars.add('0');
        validChars.add('1');
        validChars.add('2');
        validChars.add('3');
        validChars.add('4');
        validChars.add('5');
        validChars.add('6');
        validChars.add('7');
        validChars.add('8');
        validChars.add('9');
        validChars.add(DECIMAL);
        validChars.add(PADDING);
        validChars.add(PADDING2);
        VALID_CHAR_SET = Collections.unmodifiableSet(validChars
                .stream()
                .mapToInt(Character::charValue)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toSet()));
        paddingList.add(PADDING2);
        paddingList.add(PADDING);
        badpaddingList.add(BADPADDING);
        badpaddingList.add(BADPADDING2);
        badpaddingList.add(BADPADDING3);
    }

    private final boolean isPositive;
    private final String number;

    DecimalInput (String number) {
        assert number != null : "Number given should not be null.";
        assert number.trim().equals(number)
                : "Number given should not have leading or trailing whitespace: \""+number+"\"";
        this.isPositive = isNumberPositive(number);
        this.number = removeSign(number);
    }

    public String toString() { return (isPositive ? "+" : "-")+removePadding(number); }

    boolean isInteger() { return isNotWithinString(DECIMAL,number); }

    //make test
    boolean isValid() { return hasValidChars() && isFloatingPointOrInteger() && hasValidPadding(); }

    private boolean isFloatingPointOrInteger(){
        return isInteger() || hasValidDecimalPoint();
    }

    private boolean hasValidChars() { return number.chars().allMatch(VALID_CHAR_SET::contains); }

    /* A number is considered to have a valid decimal point if none exist, or only one
     * exists that splits the string into two further numbers.
     */
    private boolean hasValidDecimalPoint() {
        String[] numbers = getAllChunks();
        return numbers.length == 2
                && !numbers[0].isEmpty()
                && !numbers[1].isEmpty();
    }

    /* A number is considered to have valid padding if they only appear
     * in the place of a comma in the leading number.
     */
    private boolean hasValidPadding() {
        String[] numbers = getAllChunks();
        return (numbers.length == 2 ? isNotWithinString(PADDING,numbers[1]) &&
                isNotWithinString(PADDING2, numbers[1]) : true)
                && hasValidLeadingPadding(numbers[0]);
    }

    private String[] getAllChunks() { return number.split(getRegexOf(DECIMAL)); }

    private static boolean hasValidLeadingPadding(String leading) {
        return hasNoEdgePadding(leading) && hasValidMiddlePadding(leading);
    }

    private static boolean hasNoEdgePadding(String leading) {
        return !paddingList.contains(leading.charAt(0)) &&
                !paddingList.contains(leading.charAt(leading.length()-1));
    }

    private static boolean hasValidMiddlePadding(String leading) {
        /* The padding (underscores) in the middle of a decimal are valid when
         * followed by a multiple of three digits
         *
         * ex: 1_234, 1__234  => true
         *     12_34, _1_234 => false
         */
        if(!doubleCommaCheck(leading)){
            return false;
        }
        String[] s = paddingSplit(leading);
        for(int x = 1; x < s.length; x++){
            if(s[x].length() % 3 != 0){
                return false;
            }
        }
        return true;
/*
        StringBuilder builder = new StringBuilder(leading);
        builder.reverse();
        int count = 0;
        for(int x = 0; x < builder.length(); x++){
            if(!paddingList.contains(builder.charAt(x))){
                count++;
            }
            else {
                if(count%3 == 0){
                    count = 0;
                }
                else{
                    return false;
                }
            }
        }
        return true;
*/
    }

    private static String[] paddingSplit(String leading){ return leading.split("[_,]"); }

    private static boolean doubleCommaCheck(String leading) {
        for(int x = 0; x < leading.length()-1; x++){
            if(badpaddingList.contains(leading.substring(x, x+1))){
                return false;
            }
        }
        return true;
    }

    private static String removePadding(String number) {
        String num = number.replaceAll(getRegexOf(PADDING2), "");
        num = num.replaceAll(getRegexOf(PADDING), "");
        return num;
    }

    private static String removeSign(String number) {
        return number.isEmpty() || !SIGN_SET.contains(number.charAt(0)) ? number
                : number.substring(1, number.length());
    }

    private static boolean isNumberPositive(String number) {
        return !number.isEmpty() && number.charAt(0) != '-';
    }

    private static boolean isNotWithinString (char c, String str) { return str.indexOf(c) < 0; }

    //Not for negative and positive numbers
    private static String getRegexOf(char ch) { return "\\"+ch; }

    class TestHook {
        boolean hasValidMiddlePadding(String leading) {
            return DecimalInput.hasValidMiddlePadding(leading);
        }
        boolean isNumberPositive(String number){
            return DecimalInput.isNumberPositive(number);
        }
        String getRegexOf(char ch){
            return DecimalInput.getRegexOf(ch);
        }

        String removePadding(String s){
            return DecimalInput.removePadding(s);
        }

        boolean hasValidLeadingPadding(String s){
            return DecimalInput.hasValidLeadingPadding(s);
        }

        String removeSign(String s){
            return DecimalInput.removeSign(s);
        }

        boolean doubleCommaCheck(String s){
            return DecimalInput.doubleCommaCheck(s);
        }
    }
}
