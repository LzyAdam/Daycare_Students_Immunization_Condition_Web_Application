package edu.neu.csye6200.Helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{
    public static List<String> readAllLines(String filepath)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
        {
            String str;
            List<String> ret = new ArrayList<>();
            while ((str = br.readLine()) != null)
            {
                ret.add(str);
            }
            return ret;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readLines(String filepath, int numberOfLines)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
        {
            String str;
            List<String> ret = new ArrayList<>();
            while ((str = br.readLine()) != null && (numberOfLines-- != 0))
            {
                ret.add(str);
            }
            return ret;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static int writeToFile(String filepath, List<String> strings)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath)))
        {
            for (String str : strings)
            {
                bw.write(str);
                bw.newLine();
            }
            return 0;
        } catch (Exception e)
        {
            e.printStackTrace();
            // error code
            return -1;
        }
    }
}
