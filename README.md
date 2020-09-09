# jta
Java Thread Analyzer(JTA) tool can be useful in analyzing thread dumps of a java process. The analysis can be done live using process id or offline after collecting thread dumps. 

### Usage

```
╰─➤  ./jta                                                                                                                                                                                                                                                             1 ↵
usage: jta
 -d,--details        details of thread dump
 -i,--input <arg>    input file path (mandatory if PID is not specified)
 -o,--output <arg>   output file
 -p,--pid <arg>      jvm process id (mandatory if input file path is not
                     specified)
 -s,--summary        summary of thread dump
 -st,--stacktrace    stacktrace of thread dump
 ```

### Build

```bash
gradle clean build fatJar
```

### Run

```
java -jar build/libs/jta-1.0-SNAPSHOT.jar
```

