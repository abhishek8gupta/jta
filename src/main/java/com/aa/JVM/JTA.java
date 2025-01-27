package com.aa.JVM;

import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.attach.HotSpotVirtualMachine;

public class JTA {
    private static Logger logger = Logger.getLogger("com.JVM");

    public static void main(String[] args){
        JTA jta = new JTA();
        ArgumentParser argumentParser = new ArgumentParser(args);

        logger.log(Level.INFO, "command line arguments {0}", Arrays.toString(args));

        String pid = argumentParser.getPid();
        ThreadDumpFileHandler threadDumpFileHandler = new ThreadDumpFileHandler(argumentParser.getInputFolderPath(), argumentParser.getOutputFolderPath());
        if(pid != null){
            ThreadDump td = jta.getThreadDumpFromPid(pid, threadDumpFileHandler);
            jta.output(argumentParser, td);
        }else if(null != argumentParser.getInputFolderPath()){
            threadDumpFileHandler.processInputFiles();
            List<ThreadDump> threadDumpList = threadDumpFileHandler.getThreadDumpList();
            for(ThreadDump td: threadDumpList){
                jta.output(argumentParser, td);
            }
        }else{
            logger.log(Level.INFO, "Nothing to do!  with what you specified {0}", Arrays.toString(args));
        }
    }

    private void output(ArgumentParser argumentParser, ThreadDump td){
        if(argumentParser.isSummary()){
            td.printSummary();
        }else if(argumentParser.isDetails()){
            td.print();
        }else if(argumentParser.isStacktrace()){
            td.printStackTrace();
        }else{
            td.printSummary();
        }
    }


    public ThreadDump getThreadDumpFromPid(String pid, ThreadDumpFileHandler threadDumpFileHandler){
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(pid);
        } catch (Exception x) {
            String msg = x.getMessage();
            if (msg != null) {
                System.err.println(pid + ": " + msg);
            } else {
                x.printStackTrace();
            }
            System.exit(1);
        }

        String params[] = new String[0];

        try {
            HotSpotVirtualMachine hvm = (HotSpotVirtualMachine) vm;
            InputStream in = hvm.remoteDataDump((Object[]) params);
            // read to EOF and just print output
            byte b[] = new byte[256];
            int n;
            StringBuilder sb = new StringBuilder();

            int line = 0;
            do {
                n = in.read(b);
                if (n > 0) {
                    String s = new String(b, 0, n, "UTF-8");
                    sb.append(s);
                }
            } while (n > 0);
            in.close();
            vm.detach();
            String filename = threadDumpFileHandler.writeToFile(sb.toString());
            ThreadDump td = new ThreadDump(sb.toString(), filename);
            return td;
        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        return null;

    }

}
