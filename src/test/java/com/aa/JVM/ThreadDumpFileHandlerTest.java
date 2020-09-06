package com.aa.JVM;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ThreadDumpFileHandlerTest {
    @Test
    public void threadDumpFileHandlerTest(){
        ThreadDumpFileHandler threadDumpFileHandler = new ThreadDumpFileHandler("/Users/abhigup4/Downloads/threaddump", null);
        List<ThreadDump> threadDumpList = threadDumpFileHandler.getThreadDumpList();
        for(ThreadDump td: threadDumpList){
            td.print();
        }
        Assert.assertNotNull(threadDumpFileHandler);
    }

}
