package com.aa.JVM;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class ThreadDump {
    private static Logger logger = Logger.getLogger("com.JVM.ThreadDump");
    private LocalDateTime localDateTime;
    private long timestampMillis;
    private Map<String, ThreadStack> threadStackMap;
    private Map<String, ThreadStack> blockedThreadStackMap;
    private String infoStr;
    private String filename;

    ThreadDump(String dumpStr, String filename){
        threadStackMap = new HashMap<>();
        blockedThreadStackMap = new HashMap<>();
        this.filename = filename;
        String [] strSplit = dumpStr.split("\n\n");
        int index = 0;
        String threadMetaInfo = "";
        for(String str : strSplit) {
            if(str.isEmpty() || str.contains("JNI global references")) continue;
            if(index++ == 0){
                threadMetaInfo = str;
                continue;
            }
            pasrseMetaInfo(threadMetaInfo);
            ThreadStack threadStack = new ThreadStack(str);
            threadStackMap.put(threadStack.getTid(), threadStack);
            if(threadStack.getState() != null && threadStack.getState().equals("BLOCKED")) {
                blockedThreadStackMap.put(threadStack.getTid(), threadStack);
            }
        }
    }

    void pasrseMetaInfo(String metaInfo){
        int index = 0;
        for(String str : metaInfo.split("\n")){
            str = StringUtils.strip(str);
            if(str.isEmpty()) continue;
            if(index++ == 0){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                localDateTime = LocalDateTime.parse(str, formatter);
                timestampMillis = localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            }else{
                infoStr = str;
            }
        }
    }

    public void print(){
        JTAUtil.printThreadDumpSummary(this);
    }

    public void printStackTrace(){
        JTAUtil.printStackTrace(this);
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public Map<String, ThreadStack> getThreadStackMap() {
        return threadStackMap;
    }

    public Map<String, ThreadStack> getBlockedThreadStackMap() {
        return blockedThreadStackMap;
    }

    public String getInfoStr() {
        return infoStr;
    }

    public String getFilename() {
        return filename;
    }

    public void printSummary(){

    }

    @Override
    public String toString() {
        return "ThreadDump{" +
            "localDateTime=" + localDateTime +
            ", timestampMillis=" + timestampMillis +
            ", threadStackList=" + threadStackMap +
            ", infoStr='" + infoStr + '\'' +
            '}';
    }
}
