package com.aa.JVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ThreadDumpFileHandler {
    private static Logger logger = Logger.getLogger("com.JVM.ThreadDumpFileHandler");
    private String inputFolder;
    private String outputFolder;
    List<ThreadDump> threadDumpList;

    public ThreadDumpFileHandler(String input, String output){
        threadDumpList = new LinkedList<>();
        if(input != null){
            this.inputFolder = input;
        }

        this.outputFolder = output;
        checkAndCreateFolder();
    }

    public String writeToFile(String str){
        String fileName = outputFolder + "/td_" + System.currentTimeMillis() + ".txt";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
            logger.info("writing thread dump to " + fileName);
        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(-1);
        }

        return fileName;
    }

    public void checkAndCreateFolder(){
        String tempfolder = "/tmp/jta/" + System.currentTimeMillis();
        if(this.outputFolder == null){
            outputFolder = tempfolder;
        }else{
            Path path = Paths.get(outputFolder);
            if(Files.isDirectory(path)){
                logger.info("folder exists " + outputFolder);
            }else{
                logger.info("folder does not exists " + outputFolder + ". Using folder " + tempfolder);
                outputFolder = tempfolder;
            }
        }

        try {
            Files.createDirectories(Paths.get(outputFolder));
        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(-1);
        }

    }

    public void processInputFiles() {
        try {
            final File folder = new File(inputFolder);
            if (folder.listFiles() == null) {
                throw new FileNotFoundException("unable to open " + inputFolder);
            }
            listFilesForFolder(folder);
        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void listFilesForFolder(final File folder) throws FileNotFoundException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
//                System.out.println(fileEntry.getName());
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


