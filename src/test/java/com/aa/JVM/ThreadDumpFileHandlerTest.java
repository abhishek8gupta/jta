package com.aa.JVM;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ThreadDumpFileHandlerTest {
    @Test
    public void threadDumpFileHandlerTest(){
        ThreadDumpFileHandler threadDumpFileHandler = new ThreadDumpFileHandler("/tmp/threaddump", null);
        List<ThreadDump> threadDumpList = threadDumpFileHandler.getThreadDumpList();
        for(ThreadDump td: threadDumpList){
            td.print();
            ThreadDumpSummary summary = new ThreadDumpSummary(td.getThreadStackMap());
            summary.printSummary();
        }
        Assert.assertNotNull(threadDumpFileHandler);
    }

}
