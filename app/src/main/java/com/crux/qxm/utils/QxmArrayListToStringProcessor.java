package com.crux.qxm.utils;

import java.util.ArrayList;
import java.util.List;

public class QxmArrayListToStringProcessor {
    public static String getStringFromArrayList(ArrayList<String> stringArrayList) {
        //Output: "String1, String2, String3..."

        StringBuilder allCategory = new StringBuilder();
        for (int i = 0; i < stringArrayList.size(); i++) {

            if (i == stringArrayList.size() - 1) {
                allCategory.append(stringArrayList.get(i));
            } else {
                allCategory.append(stringArrayList.get(i)).append(", ");
            }
        }

        return allCategory.toString();
    }


    public static String getStringFromArrayListWithNewLine(List<String> stringArrayList) {
        //Output: "String1\nString2\nString3..."

        StringBuilder allCategory = new StringBuilder();
        if (stringArrayList != null && !stringArrayList.isEmpty()) {

            for (int i = 0; i < stringArrayList.size(); i++) {

                if (i == stringArrayList.size() - 1) {
                    allCategory.append(stringArrayList.get(i));
                } else {
                    allCategory.append(stringArrayList.get(i)).append("\n");
                }
            }
            return allCategory.toString();
        }
        return "";
    }

    public static String getStringFromArrayListAlphabetically(List<String> stringArrayList) {
        //Output: "String1\nString2\nString3..."

        String[] alphabets = {"(a) ", "(b) ", "(c) ", "(d) ", "(e) ", "(f) ", "(g) ", "(h) ", "(i) ", "(j) ", "(k) "};

        StringBuilder allCategory = new StringBuilder();

        for (int i = 0; i < stringArrayList.size(); i++) {
            allCategory.append(alphabets[i]);
            if (i == stringArrayList.size() - 1) {
                allCategory.append(stringArrayList.get(i));
            } else {
                allCategory.append(stringArrayList.get(i)).append("\n");
            }
        }

        return allCategory.toString();
    }
}
