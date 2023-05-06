package com.seproject.seboard.application.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUtils {
    private static String rootPath;

    @Value("${storage.rootPath}")
    public void setRootPath(String rootPath) {
        FileUtils.rootPath = rootPath;
    }

    public static String getStoredFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "")+extension;
    }

    public static String getFilePath(){
        String currentDate = getCurrentDate();

        File directory = new File(rootPath+"/"+currentDate);

        if(!directory.exists()){
            directory.mkdirs();
        }

        return directory.getPath();
    }

    public static String getUrlPath(String urlRoot){
        String currentDate = getCurrentDate();

        return urlRoot+"/"+currentDate;
    }

    private static String getCurrentDate(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

}
