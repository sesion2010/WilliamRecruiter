package com.myriad.app;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class DU {

    public static void main(String[] args) {
        for (String dirName : args) {

            File file = new File(dirName);

            Map<String, Long> myFiles = collectDataFromDirectory(file);
            myFiles.forEach((k, v) -> System.out.println((k + " " + v + "KB")));
        }
    }


    private static Map<String,Long> collectDataFromDirectory(File dirName) {
        File[] fileList = dirName.listFiles();
        Map<String,Long> responseMap = new HashMap<>();
        if(fileList != null) {
            for (File file : fileList) {

                if (file.isFile()) {
                    long fileLength = file.length() / 1024;
                    responseMap.put("FILE " + file, fileLength);
                } else if (file.isDirectory()) {
                    long length = getLength(file);
                    responseMap.put("DIR " + file, length);
                }
            }

            return sortByValue(responseMap);
        }else{
            throw new RuntimeException("Empty or wrong directory");
        }
    }

    private static Long getLength(File file) {
        File[] fileList = file.listFiles();
        long totalLength = 0L;
        if(fileList != null)
            for (File fileInside : fileList)
                if (fileInside.isFile())
                    totalLength += fileInside.length()/1024;
                else if (fileInside.isDirectory())
                    getLength(fileInside);

        return totalLength;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x,y)-> {throw new AssertionError();}, LinkedHashMap::new));
    }


}