package es.ull.pcg.hpc.rancid.utils;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

public class FileUtils {
    public static Number readNumberFile (String fileName) {
        try {
            File file = new File(fileName);
            Scanner s = (new Scanner(file)).useDelimiter("\\Z");
            Throwable exception = null;

            String str;
            try {
                str = s.next();
            } catch (Throwable e) {
                exception = e;
                throw e;
            } finally {
                if (exception != null) {
                    try {
                        s.close();
                    } catch (Throwable e2) {
                        exception.addSuppressed(e2);
                    }
                } else {
                    s.close();
                }
            }

            return NumberFormat.getInstance().parse(str);
        } catch (NumberFormatException | ParseException | FileNotFoundException e) {
            return Float.NaN;
        }
    }

    public static void writeFile (String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
