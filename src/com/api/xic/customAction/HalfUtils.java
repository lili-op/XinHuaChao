package com.api.xic.customAction;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class HalfUtils {
    public HalfUtils() {
    }

    public static String toHalfWidth(String input) {
        String normalized = Normalizer.normalize(input, Form.NFKC);
        StringBuilder sb = new StringBuilder();
        char[] var3 = normalized.toCharArray();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            if (c >= '！' && c <= '～') {
                sb.append((char)(c - 'ﻠ'));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
