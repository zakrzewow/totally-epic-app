package pl.zakrzewow.totallyepicapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws IOException {
        int count = 12;
        String s = Integer.toString(count);
        int c = Integer.parseInt(s.substring(s.length() - 1));
        System.out.println(c);
    }
}
