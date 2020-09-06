package com.aa.JVM;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentParser {
    private String pid;
    private String inputFolderPath;
    private String outputFolderPath;

    public ArgumentParser(String[] args){
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path (mandatory if PID is not specified)");
        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);

        Option processid = new Option("p", "pid", true, "jvm process id (mandatory if input file path is not specified)");
        output.setRequired(true);
        options.addOption(processid);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("jta", options);
            System.exit(1);
        }

        if(!isValidOption(cmd)){
            formatter.printHelp("jta", options);
            System.exit(1);
        }

        if(cmd.hasOption("p")) {
            pid = cmd.getOptionValue("pid");
        }

        if(cmd.hasOption("i")) {
            inputFolderPath = cmd.getOptionValue("input");
        }

        if(cmd.hasOption("o")) {
            outputFolderPath = cmd.getOptionValue("output");
        }
    }

    public String getPid() {
        return pid;
    }

    public String getInputFolderPath() {
        return inputFolderPath;
    }

    public String getOutputFolderPath() {
        return outputFolderPath;
    }


    boolean isValidOption(CommandLine cmd){
        if(cmd.getOptions().length == 0){
            return false;
        }

        if(cmd.hasOption("p") && cmd.hasOption("i")){
            return false;
        }

        if(!cmd.hasOption("p") && !cmd.hasOption("i")){
            return false;
        }

        return true;
    }
}
