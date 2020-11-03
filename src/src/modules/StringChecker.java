package modules;

public interface StringChecker {
    public boolean check(String textToConvert);
}

/*

another utility function using the "lambda" format
to make things a little more simple, taking the compareTo
function and basically applying it to the application,
doing basic form checking to ensure that the user
didn't have any blank areas of the form that they filled out.

public static boolean sEmpty(String myString) {
    StringChecker sc = (text) -> {
        if (myString.compareTo("") == 0) {
            return false;
        } else {
            return true;
        }
    };
    return sc.check(myString);
}

 */