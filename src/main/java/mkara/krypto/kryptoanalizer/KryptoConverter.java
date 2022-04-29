package mkara.krypto.kryptoanalizer;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class KryptoConverter {
    public static String currentSrcText = "";
    public static String currentModText = "";
    public static String resPas = "";
    private static final String charArrRusSymbols = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
                                                    "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
                                                    ".,!?\":- ";
    private static final int charArrSize = charArrRusSymbols.length();



    public static void CodeDecode (String sourcePath, int key){
        currentModText = "";
        currentSrcText = "";
        if (key>=charArrSize){
            key = key % charArrSize;
        }
        String resultPath = Path.of(sourcePath).getParent() + "\\modified_result_" + Path.of(sourcePath).getFileName();
        resPas = resultPath;
        char[] bufferIn = new char[1024];
        char[] bufferOut;
        try(BufferedReader input = new BufferedReader(new FileReader(sourcePath));
            BufferedWriter output = new BufferedWriter(new FileWriter(resultPath)))
        {
            while (input.ready()) {
                int real = input.read(bufferIn);
                currentSrcText = currentSrcText.equals("")? String.copyValueOf(bufferIn) : currentSrcText;
                bufferOut = new char[real];
                for (int i = 0; i < real; i++) {
                    bufferOut[i] = shiftByteOfChar(bufferIn[i], key);
                }
                currentModText = currentModText.equals("") ? String.copyValueOf(bufferOut) : currentModText;
                output.write(bufferOut);
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static char shiftByteOfChar (char ch, int key){
        if (charArrRusSymbols.contains(Character.valueOf(ch).toString())) {
            int indexOfCh = charArrRusSymbols.indexOf(ch);
            int newIndexOfCh = charArrSize-indexOfCh > key ? indexOfCh + key : key - (charArrSize - indexOfCh);
            newIndexOfCh = newIndexOfCh < 0 ? charArrSize+newIndexOfCh : newIndexOfCh;
            return charArrRusSymbols.charAt(newIndexOfCh);
        }
        else {
            return ch;
        }
    }

    public static void bruteForceCrack (String sourcePath){
        try (BufferedReader input = new BufferedReader(new FileReader(sourcePath))){
            char[] bufferIn  = new char[6400];
            char[] bufferOut;
            boolean stop = false;
            int key = 1;
            int real = input.read(bufferIn);
            bufferOut = new char[real];
            while (!stop) {
                for (int i =0 ; i < bufferIn.length; i++){
                    bufferOut[i] = shiftByteOfChar(bufferIn[i], -key);
                }

                if (checkCrack(bufferOut)){
                    stop = true;
                }
                else
                {
                    key++;
                }
            }
            System.out.println("Key: " + key);
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private static boolean checkCrack (char[] buffer){
        int i=0;
        for (char ch : buffer) {
            if (ch == ' ') {
              i++;
            }
        }
        if (i > 20) {
            return true;
        }
        else {
            System.out.println("No " + i);
            return false;
        }
    }



}
