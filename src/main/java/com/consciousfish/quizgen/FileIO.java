package com.consciousfish.quizgen;

import java.io.*;

/**
 * Created by Sachit on 10/14/2014.
 */
public class FileIO {

    private static final File input = new File("res/Input");
    private static final File output = new File("res/Output");
    private static final File log = new File("res/log");

    public static String read() {
        try {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            fileReader.close();

            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void write(String... outputStr) {
        try {
            FileOutputStream outFileStream
                    = new FileOutputStream(output);
            PrintWriter outStream = new PrintWriter(outFileStream);
            for (String s : outputStr) {
                outStream.println(s);
            }
            outStream.close();
            outFileStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToLog(String... outputStr) {
        try {
            FileOutputStream outFileStream
                    = new FileOutputStream(log);
            PrintWriter outStream = new PrintWriter(outFileStream);
            for (String s : outputStr) {
                outStream.println(s);
            }
            outStream.close();
            outFileStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToLog(Exception e) {
        try {
            FileOutputStream outFileStream
                    = new FileOutputStream(log);
            PrintWriter outStream = new PrintWriter(outFileStream);
            e.printStackTrace(outStream);
            outStream.close();
            outFileStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
