package pl.bkkuc.purutils;

import java.util.Arrays;

public class Utility {

    public static String ATS(String[] array, int index){
        return String.join(" ", Arrays.copyOfRange(array, index, array.length));
    }
}
