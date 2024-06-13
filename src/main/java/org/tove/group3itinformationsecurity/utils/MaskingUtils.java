package org.tove.group3itinformationsecurity.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskingUtils {

    /**
     * Anonymiserar en email adress genom att maskera karaktärerna som kommer innan '@' symbolen.
     * Den första och sista karaktären innan '@' förblir synlig, medan resterande av karraktärer blir ersatta av asterixer.
     *
     * @param email emailadressen som ska anonymiseras
     * @return den anonymiserade emailadressen, eller den orginella emailen om denne var felaktig
     */
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
