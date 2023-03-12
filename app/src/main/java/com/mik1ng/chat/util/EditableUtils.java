package com.mik1ng.chat.util;

import android.text.Editable;

public class EditableUtils {

    public static void limitEdittext(Editable editable, String s) {
        if (editable.length() > 0) {
            char c = editable.charAt(editable.length() - 1);
            if (!s.contains(String.valueOf(c))) {
                editable.delete(editable.length() - 1, editable.length());
            }
        }
    }
}
