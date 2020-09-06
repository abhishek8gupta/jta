package com.aa.JVM;

import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.attach.HotSpotVirtualMachine;

public class JTA {
    private static Logger logger = Logger.getLogger("com.JVM");

    public static void main(String[] args){
        int length = args.length;
        if(length < 1){
            logger.log(Level.SEVERE, "invalid command line arguments {0}", Arrays.toString(args));
            logger.log(Level.INFO, "length {0}", length);
            JTAUtil.printHelp();
            return;
        }

        logger.log(Level.INFO, "invalid command line arguments {0}", Arrays.toString(args));
        int pid = Integer.parseInt(args[0]);
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(args[0]);
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
            InputStream in = hvm.remoteDataDump((Object[]) args);
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
            ThreadDump td = new ThreadDump(sb.toString());
            td.print();
            td.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }

    }

}
