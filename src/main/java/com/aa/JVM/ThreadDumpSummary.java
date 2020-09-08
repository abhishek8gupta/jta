package com.aa.JVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class ThreadDumpSummary {
    private Map<String, Integer> groupCount = new HashMap<>();
    private Map<String, Integer> stateCount = new HashMap<>();
    private int totalCount = 0;

    ThreadDumpSummary(Map<String, ThreadStack> threadStackMap){
        for(Entry entry : threadStackMap.entrySet()){
            ThreadStack threadStack = (ThreadStack)entry.getValue();
            String group = threadStack.getTname();
            if(StringUtils.isBlank(group)){
                System.out.println("group : " + group);
            }
            if(threadStack.getTname().length() > 4) {
                group = threadStack.getTname().substring(0, 4);
            }
            if(groupCount.containsKey(group)){
                int count = groupCount.get(group);
                count = count + 1;
                groupCount.put(group, count);
            }else{
                groupCount.put(group, 1);
            }

            String state = threadStack.getState();
            state = state == null ? "NA" : state;
            if(stateCount.containsKey(state)){
                int count = stateCount.get(state);
                count = count+1;
                stateCount.put(state, count);
            }else{
                stateCount.put(state, 1);
            }
        }

        List<String> removeKeys = new ArrayList<>();

        int otherCount = 0;
        for(Entry entry : groupCount.entrySet()){
            String name = (String)entry.getKey();
            Integer count = (Integer)entry.getValue();
            if(count < 5){
                otherCount += count;
                removeKeys.add(name);
            }
            totalCount += count;
        }

        removeKeys.forEach(key -> groupCount.remove(key));
        groupCount.put("others", otherCount);
    }

    public void printSummary(){
        JTAUtil.printSummary(this);
    }

    public Map<String, Integer> getGroupCount() {
        return groupCount;
    }

    public Map<String, Integer> getStateCount() {
        return stateCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
