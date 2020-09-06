package com.aa.JVM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

public class JTAUtil {
    private static PrintWriter writer = new PrintWriter(System.out, true);
    public static void printHelp(){
        writer.printf("--help\n");
        writer.printf("[pid] process id of process to attach to\n");
    }

    public static void printThreadDumpSummary(ThreadDump threadDump){
        Collection<ThreadStack> threadStackCollection = threadDump.getThreadStackMap().values();
        ArrayList<ThreadStack> newList = new ArrayList<>(threadStackCollection);
        Collections.sort(newList);
        PrintWriter writer = new PrintWriter(System.out, true);
        String s5 = getString("-", 5);
        String s20 = getString("-", 20);
        String s10 = getString("-", 10);
        String s50 = getString("-", 50);
        writer.printf("+%1$50s+\n", s50);
        writer.printf("|%1$50s|\n", threadDump.getLocalDateTime());
        writer.printf("+%3$50s+%1$20s+%1$20s+%2$10s+%2$10s+%2$10s+%4$5s+%3$50s+\n", s20,  s10, s50, s5);
        writer.printf("|%1$50s|%2$20s|%3$20s|%4$10s|%5$10s|%6$10s|%7$5s|%8$50s|\n", "Name", "STATE", "TID", "NID", "PRIO", "OS-PRIOR", "SEQ", "DETAILS");
        writer.printf("+%3$50s+%1$20s+%1$20s+%2$10s+%2$10s+%2$10s+%4$5s+%3$50s+\n", s20,  s10, s50, s5);
        for(ThreadStack ts : newList) {
            writer.printf("|%1$50s|%2$20s|%3$20s|%4$10s|%5$10s|%6$10s||%7$5s|%8$50s|\n", ts.getTname(), ts.getState(), ts.getTid(), ts.getNid(), ts.getPriority(), ts.getOSPriority(), ts.getSequence(), ts.getThreadType());
        }
        writer.printf("+%3$50s+%1$20s+%1$20s+%2$10s+%2$10s+%2$10s+%4$5s+%3$50s+\n", s20,  s10, s50,s5);
        writer.flush();
    }

    public static void printStackTrace(ThreadDump td){
        PrintWriter writer = new PrintWriter(System.out, true);
        String s20 = getString("-", 20);
        String s100 = getString("-", 100);
        String se20 = getString(" ", 20);
        writer.printf("+%1$20s+%2$100s+\n", s20, s100);
        writer.printf("|%1$20s|%2$100s|\n", "TID", "STACK TRACE");
        writer.printf("+%1$20s+%2$100s+\n", s20, s100);
        Map<String, ThreadStack> tmap = td.getThreadStackMap();
        for(Entry<String, ThreadStack> entrySet : tmap.entrySet()){
            List<StackTraceElement> ste = entrySet.getValue().getTraceElement();
            StringJoiner sj = new StringJoiner("\n");
            int startIndex = 0;
            for(StackTraceElement se : ste){
                sj.add(se.toString());
                if(startIndex++ == 0) {
                    writer.printf("|%1$20s|%2$100s|\n", entrySet.getKey(), se.toString());
                }else{
                    writer.printf("|%1$20s|%2$100s|\n", se20, se.toString());
                }
            }
            if(startIndex != 0) {
                writer.printf("+%1$20s+%2$60s+\n", s20, s100);
            }
        }
        writer.flush();
    }

    private static String getString(String s, int len){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i < len ; i++){
            sb.append(s);
        }
        return sb.toString();
    }

    void close(){
        writer.flush();
        writer.close();
    }
}
