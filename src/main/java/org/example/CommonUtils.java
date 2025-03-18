package org.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
    private static final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
    public static final String FORMATTED_DATE_TIME = CURRENT_DATE_TIME.format(DATE_FORMATTER);

    public static String generateName(final String objectName) {
        return String.format("ACMS_Test_%s_%s_%s", objectName, FORMATTED_DATE_TIME, RandomStringUtils.randomAlphanumeric(8));
    }

}
