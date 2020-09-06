package com.aa.JVM;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class ThreadStack implements Comparable<ThreadStack> {
    private static Logger logger = Logger.getLogger("com.JVM.ThreadStack");
    private String tname;
    private String tid;
    private String nid;
    private Integer sequence;
    private String priority;
    private String OSPriority;
    private String state;
    private String threadType;
    private List<StackTraceElement> traceElement = new LinkedList<>();

    ThreadStack(){

    }

    ThreadStack(List<String> dump){
        pasrseStack(dump);
    }

    ThreadStack(String dumpStr){
        String[] str = dumpStr.split("\n");
        List stack = new LinkedList<>();
        for(String s : str){
            if(s.isEmpty()) continue;
            stack.add(StringUtils.strip(s));
        }
        pasrseStack(stack);
    }

    public void pasrseStack(List<String> dump){
        for(int lineNo = 0; lineNo < dump.size() ; lineNo++){
            if(lineNo == 0){
                readMetaInformation(dump.get(0));
            }else if(lineNo == 1){
                String state = StringUtils.strip(dump.get(1));
                String[] str = state.split(":");
                try {
                    this.state = StringUtils.strip(str[1]).split(" ")[0];
                }catch(Exception ex){
                    logger.log(Level.SEVERE, "failed to parse {0}", state);
                    continue;
                }
            }else {
                traceElement.add(getStackTraceElement(dump.get(lineNo)));
            }
        }
    }

    void readMetaInformation(String metaString){
        String[] medataInfo = metaString.split("\"");
        int nameindex = 0;
        int otherIndex = 1;
        if(medataInfo.length > 2){
            nameindex = 1;
            otherIndex = 2;
        }
        this.tname = medataInfo[nameindex];
        String[] otherInfo = {};
        try {
            otherInfo = medataInfo[otherIndex].split(" ");
        }catch(Exception ex){
            logger.log(Level.SEVERE, "unable to parse {0}", medataInfo);
            return;
        }

//        logger.info(Arrays.toString(medataInfo));
        int nidIndex = 0;
        StringJoiner sj = new StringJoiner(" ");
        for(int index=0; index < otherInfo.length ; index++){
            String other = otherInfo[index];

            try {
                if (other.contains("#")) {
                    other = StringUtils.strip(other, "#");
                    other = StringUtils.trim(other);
                    this.sequence = Integer.parseInt(other);
                    continue;
                }

                if (other.contains("prio")) {
                    this.priority = getValue(other);
                }

                if (other.contains("os_prio")) {
                    this.OSPriority = getValue(other);
                }

                if (other.contains("tid")) {
                    this.tid = getValue(other);
                }

                if (other.contains("nid")) {
                    this.nid = getValue(other);
                    nidIndex = index;
                }

                if (nidIndex != 0 && index > nidIndex && index < otherInfo.length - 1) {
                    sj.add(other);
                }
            }catch(Exception ex){
                logger.log(Level.SEVERE, "failed to pasrse {0}", other);
                continue;
            }
        }

        this.threadType = sj.toString();
    }

    String getValue(String str){
        str = StringUtils.strip(str);
        return str.split("=")[1];
    }

    boolean checkType(String str){
        str = StringUtils.strip(str);
        return str.contains("runnable") || str.contains("daemon");
    }


    StackTraceElement getStackTraceElement(String str){
        int start = str.indexOf(" ", 0);
        int end = str.indexOf("(", start);
        String methodClass = str.substring(start, end);
        String[] mclSplit = methodClass.split("\\.");
        String methodName = mclSplit[mclSplit.length - 1];
        StringJoiner sj = new StringJoiner(".");
        for(int i = 0 ; i < mclSplit.length - 1 ; i++){
            sj.add(mclSplit[i]);
        }
        String declaringClass = sj.toString();

        start = end + 1;
        end = str.indexOf(")", start);
        String fileName = str.substring(start, end);
        String [] classLine = fileName.split(":");
        Integer lineNumber = -1;
        if(classLine.length > 1){
            lineNumber = Integer.parseInt(classLine[1]);
            fileName = classLine[0];
        }

        return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }

    public String getTname() {
        return tname;
    }

    public String getTid() {
        return tid;
    }

    public String getNid() {
        return nid;
    }

    public Integer getSequence() {
        return sequence;
    }

    public String getPriority() {
        return priority;
    }

    public String getOSPriority() {
        return OSPriority;
    }

    public String getState() {
        return state;
    }

    public String getThreadType() {
        return threadType;
    }

    public List<StackTraceElement> getTraceElement() {
        return traceElement;
    }

    @Override
    public String toString() {
        return "ThreadStack{" +
            "tname='" + tname + '\'' +
            ", tid='" + tid + '\'' +
            ", nid='" + nid + '\'' +
            ", sequence=" + sequence +
            ", priority='" + priority + '\'' +
            ", OSPriority='" + OSPriority + '\'' +
            ", state=" + state +
            ", threadType='" + threadType + '\'' +
            ", traceElement=" + traceElement.toString() +
            '}';
    }

    @Override
    public int compareTo(ThreadStack o) {
        if(o.getSequence() == null && this.getSequence() == null){
            return 0;
        }

        if(o.getSequence() == null && this.getSequence() != null){
            return -1;
        }

        if(o.getSequence() != null && this.getSequence() == null){
            return 1;
        }

        return o.getSequence() - this.sequence;
    }
}
