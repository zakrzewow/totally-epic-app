package pl.zakrzewow.totallyepicapp.io;

public class Noun {

    public static String getForm(int count, String one, String from2to4, String more) {
        String form;
        if (count == 1) form = one;
        else if (isFrom2To4Form(count)) form = from2to4;
        else form = more;
        return count + " " + form;
    }

    private static boolean isFrom2To4Form(int count) {
        return !isAboutDozen(count) && isLastDigitBetween2And4(count);
    }

    private static boolean isLastDigitBetween2And4(int count) {
        String s = Integer.toString(count);
        int c = Integer.parseInt(s.substring(s.length() - 1));
        return c >= 2 && c <= 4;
    }

    private static boolean isAboutDozen(int count) {
        return count > 10 && count < 20;
    }
}
