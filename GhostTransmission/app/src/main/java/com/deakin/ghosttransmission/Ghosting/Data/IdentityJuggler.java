package com.deakin.ghosttransmission.Ghosting.Data;

import java.util.Random;

public class IdentityJuggler {

    /**
     * Juggles and Drops the given string such that remaining string is a combination substring of the initial one
     *
     * @param address input string to Juggle and Drop
     * @return the combination substring
     */
    public static String juggleDropIdentity(String address) {
        // check if the string contains only numerical data
        String regexNumberCheckPattern = "^\\w*$";
        String regexNumberReplacePattern = "[^0-9a-zA-Z]+";

        if (!address.matches(regexNumberCheckPattern)) {
            address = address.replaceAll(regexNumberReplacePattern, "");
        }

        // create random object for generating random numbers
        Random random = new Random();
        char[] juggleDrop = address.toCharArray();

        // replace a number with one at a random place in the array
        for (byte i = 0, r = (byte) random.nextInt(juggleDrop.length); i < juggleDrop.length / 2; i++, r = (byte) random.nextInt(juggleDrop.length)) {
            // every second number, the index (i) character is taken from the end of the array
            if (((i + 1) % 2) == 0) {
                char rchar = juggleDrop[r];
                char ichar = juggleDrop[juggleDrop.length - i];
                juggleDrop[juggleDrop.length - i] = rchar;
                juggleDrop[r] = ichar;
            } else { // every other number, the index (i) number is taken from the beginning
                char rchar = juggleDrop[r];
                char ichar = juggleDrop[i];
                juggleDrop[i] = rchar;
                juggleDrop[r] = ichar;
            }
        }

        byte double_length = (byte) (juggleDrop.length * 2); // minimize space usage by down casting int to byte (realistically, an int won't be required)
        // randomly drop characters
        for (byte i = 0; i < juggleDrop.length / 2; i++) {
            byte r = (byte) random.nextInt(double_length);
            if (r < juggleDrop.length)
                juggleDrop[r] = '-';
        }

        // create the final String of characters
        StringBuilder sb = new StringBuilder();
        for (char b : juggleDrop)
            if (b != '-')
                sb.append(b);

        return sb.toString();
    }
}
