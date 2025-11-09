package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.io.File;

public class StudyDeckFile {
    private String displayName, fullPath, fileName;
    private BufferedReader reader;

    public StudyDeckFile(String filePath){
        this.fullPath = filePath;
        this.displayName = makePathRelative(removeExtension(filePath));
        this.fileName = makePathRelative(filePath);
    }

    public void open(){
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine(){
        try {
            String line;
            if ((line = reader.readLine()) != null) {
                return line;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(ArrayList<String[]> data) {
        boolean saveSuccessful = true;
        try {
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath + ".tmp"));
            System.out.println("Saving " + fileName + "...");
            
            writer.write("Definition,Term,Image_Path");
            writer.newLine();
            for (String[] flashcard : data) {
                writer.write(String.join(",", flashcard[0] , flashcard[1] , flashcard[2]));
                writer.newLine();
            }
            writer.close();
            try {
                Files.move((Paths.get(fullPath + ".tmp")), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {   
                saveSuccessful = false;
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            saveSuccessful = false;
        }
        if (!saveSuccessful) {
            File tmpFile = new File(fullPath + ".tmp");
            tmpFile.delete();
        }
        return saveSuccessful;
    }

    public String getFullPath(){
        return this.fullPath;
    }

    public String getName(){
        return this.fileName;
    }

    @Override
    public String toString(){
        return this.displayName;
    }

    private String removeExtension(String filePath){
        int dotindx = filePath.lastIndexOf(".");
        if (dotindx != -1) {
            filePath = filePath.substring(0, dotindx);
        }
        return filePath;
    }

    private String makePathRelative(String filePath){
        int slashindx = filePath.lastIndexOf("\\") + 1;
        if (slashindx != -1){
            filePath = filePath.substring(slashindx, filePath.length());
        }
        return filePath;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof StudyDeckFile) {
            StudyDeckFile otherFile = (StudyDeckFile) obj;
            return otherFile.getFullPath().equals(this.getFullPath());
        }
        return false;
    }
}
