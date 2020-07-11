package com.eztv.mud.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BFile {

    public static String readFromFile(String src){
        try {
            BufferedReader in = new BufferedReader(new FileReader(src));
            StringBuilder result = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                result.append(str);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
