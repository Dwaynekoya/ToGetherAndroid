package com.example.together.view;

import android.text.InputFilter;
import android.text.Spanned;

public class DateFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char currentChar = source.charAt(i);
            if (Character.isDigit(currentChar) || currentChar == '-') {
                builder.append(currentChar);
            }
        }
        return builder.toString();
    }
}

