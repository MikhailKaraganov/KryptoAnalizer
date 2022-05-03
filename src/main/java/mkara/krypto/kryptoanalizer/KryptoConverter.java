package mkara.krypto.kryptoanalizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import java.util.HashMap;
import java.util.Map.Entry;


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
//        Если ключ больше алфавита, то отрезаем длинну(ы) алфавита
        if (Math.abs(key)>=charArrSize){
            key = key % charArrSize;
        }
        String resultPath = Path.of(sourcePath).getParent() + "\\modified_result_" + Path.of(sourcePath).getFileName();
        resPas = resultPath;
        char[] bufferIn = new char[1024];
        char[] bufferOut;
        try(BufferedReader input = new BufferedReader(new FileReader(sourcePath, StandardCharsets.UTF_8));
            BufferedWriter output = new BufferedWriter(new FileWriter(resultPath, StandardCharsets.UTF_8)))
        {
            while (input.ready()) {
                int real = input.read(bufferIn);
                currentSrcText = currentSrcText.equals("")? String.copyValueOf(bufferIn) : currentSrcText;
                bufferOut = new char[real];
//                Двигаем символы в полученом массиве
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
//           Вот тут реализован переход по алфавиту при превышении размера алфавита
            int newIndexOfCh = charArrSize-indexOfCh > key ? indexOfCh + key : key - (charArrSize - indexOfCh);
//            Если вдруг индекс отрицательный, то берем его с конца массива
            newIndexOfCh = newIndexOfCh < 0 ? charArrSize+newIndexOfCh : newIndexOfCh;
            return charArrRusSymbols.charAt(newIndexOfCh);
        }
        else {
            return ch;
        }
    }

    public static int bruteForceCrack (String sourcePath){
        try (BufferedReader input = new BufferedReader(new FileReader(sourcePath, StandardCharsets.UTF_8))){
            char[] bufferIn = new char[6400];
            char[] bufferOut;
            boolean stop = false;
            int key = 1;
            int real = input.read(bufferIn);
            bufferOut = new char[real];
//            Считываем фрагмент файла (bufferIn), двигаем сиволы и проверяем результат,
//            если успех, то возвращаем найденый ключ, если нет - инкреметируем ключ и по новой
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
            return key;
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        return 0;
    }

    private static boolean checkCrack (char[] buffer){
        HashMap<String, Double> wordsAllMap = new HashMap<>();
        String currentString = String.copyValueOf(buffer).toLowerCase();
//      Разбиваем анализируемую строку на массив строк (слов)
        String[] wordsInBuf = currentString.split("[ ]"); //[ .,!\]/)(\[?:»;«]
        double allWordsCount = wordsInBuf.length;
//        Формируес мапу со словами и счетаем сколько раз встретилось каждое уникальное слово
        for (String str : wordsInBuf){
            if(wordsAllMap.containsKey(str.toLowerCase())){
                wordsAllMap.replace(str.toLowerCase(), wordsAllMap.get(str.toLowerCase())+1);
            }
            else {
                wordsAllMap.put(str.toLowerCase(), 1.00);
            }
        }
//      В мапе переходим от количества повторов слова к его проценту в тексте
        wordsAllMap.replaceAll((k, v) -> ((v / allWordsCount) * 100));
//      Если " и " встречается в тексте более 2%, расшифровка удалась ... наверно
        if (wordsAllMap.containsKey("и")) {
            System.out.println(wordsAllMap.get("и"));
            if (wordsAllMap.get("и") > 2) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
