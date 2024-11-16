package com.task.polynomial.utils;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class HashingUtils {

    public static long generateNumericHash(String inputExpression) {
        CRC32 crc32 = new CRC32();
        crc32.update(inputExpression.getBytes(StandardCharsets.UTF_8));
        return crc32.getValue();
    }
}
