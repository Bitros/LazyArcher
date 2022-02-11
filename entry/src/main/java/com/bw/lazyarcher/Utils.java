package com.bw.lazyarcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static List<Integer> getResourceByFilePrefix(String filePrefix) {
        return Arrays.stream(ResourceTable.class.getDeclaredFields()).filter(field -> field.getName().split("_", 2)[1].startsWith(filePrefix)).map(field -> {
            try {
                field.setAccessible(true);
                return field.getInt(ResourceTable.class);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @param filePrefix
     * @param type       Animation, Media, Graphic
     * @return
     */
    public static List<Integer> getResourceByFilePrefixAndType(String filePrefix, String type) {
        return Arrays.stream(ResourceTable.class.getDeclaredFields()).filter(field -> field.getName().startsWith(type) && field.getName().split("_", 2)[1].startsWith(filePrefix)).map(field -> {
            try {
                field.setAccessible(true);
                return field.getInt(ResourceTable.class);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @param fileName
     * @return
     */
    public static Integer getResourceByFileName(String fileName) {
        return Arrays.stream(ResourceTable.class.getDeclaredFields()).filter(field -> field.getName().split("_", 2)[1].equals(fileName)).map(field -> {
            try {
                field.setAccessible(true);
                return field.getInt(ResourceTable.class);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().orElseThrow(() -> new RuntimeException("can not find " + fileName));
    }
}
