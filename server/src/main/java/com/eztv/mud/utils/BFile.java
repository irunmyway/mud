package com.eztv.mud.utils;

import java.io.*;

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

    public static boolean writeFile(String src,String content){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(src));
            bw.write(content);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
