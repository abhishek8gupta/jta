package com.aa.JVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class ThreadDumpFileHandler {
    private String inputFolder;
    private String outputFolder;
    List<ThreadDump> threadDumpList;

    public ThreadDumpFileHandler(String input, String output){
        threadDumpList = new LinkedList<>();
        if(input != null){
            this.inputFolder = input;
        }
        try {
            processFiles();
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
            System.exit(1);
        }

    }

    private void processFiles() throws FileNotFoundException{
        final File folder = new File(inputFolder);
        if(folder.listFiles() == null){
            throw new FileNotFoundException("unable to open " + inputFolder);
        }
        listFilesForFolder(folder);
    }

    private void listFilesForFolder(final File folder) throws FileNotFoundException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println(fileEntry.getName());
                try {
                    threadDumpList.add(readFile(fileEntry.getAbsolutePath()));
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public ThreadDump readFile(String file) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file)) {
            int i = 0;
            do {
                byte[] buf = new byte[1024];
                i = fis.read(buf);
                String value = new String(buf, StandardCharsets.UTF_8);
                sb.append(value);

            } while (i != -1);
        }

        return new ThreadDump(sb.toString(), file);
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public List<ThreadDump> getThreadDumpList() {
        return threadDumpList;
    }
}


