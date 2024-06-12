package org.tove.group3itinformationsecurity.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskingUtils {

    public static String anonymize(String email) {

        int atIndex = email.indexOf('@');
        if (atIndex == -1) return email;

        String repeatedString = IntStream
                .range(0, atIndex - 2)
                .mapToObj(i -> "*")
                .collect(Collectors.joining());

        return email.charAt(0) + repeatedString + email.substring(atIndex - 1);
    }
}
