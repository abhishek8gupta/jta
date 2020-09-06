package com.aa.JVM;

import org.junit.Assert;
import org.junit.Test;

public class ThreadStackTest {
    private static String stack = "2020-09-05 18:30:40\n"
        + "\"Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.131-b11 mixed mode):\"\n\n"
        + "\"H2 TCP Server (tcp://192.168.86.21:9092)\" #21 prio=5 os_prio=31 tid=0x00007f820b975000 nid=0xea03 runnable [0x0000700004004000]\n"
        + "   java.lang.Thread.State: RUNNABLE\n"
        + "\tat java.net.PlainSocketImpl.socketAccept(Native Method)\n"
        + "\tat java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)\n"
        + "\tat java.net.ServerSocket.implAccept(ServerSocket.java:545)\n"
        + "\tat java.net.ServerSocket.accept(ServerSocket.java:513)\n"
        + "\tat org.h2.server.TcpServer.listen(TcpServer.java:248)\n"
        + "\tat org.h2.tools.Server.run(Server.java:610)\n"
        + "\tat java.lang.Thread.run(Thread.java:748)";

    @Test
    public void threadDumpTest(){
        ThreadDump td = new ThreadDump(stack, null);
        System.out.println("stack: " + td.toString());
        td.print();
        Assert.assertNotNull(td);
    }

}
