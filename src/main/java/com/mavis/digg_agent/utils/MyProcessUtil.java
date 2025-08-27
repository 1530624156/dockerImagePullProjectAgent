package com.mavis.digg_agent.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyProcessUtil {
    /**
     * Java调用shell命令，返回执行结果
     * @param cmdarray 包含所调用命令及其参数的数组
     * @param dir 子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为 null
     * @return 执行结果List
     */
    public static List<String> callShellCommand(String[] cmdarray, String dir) throws IOException {
        List<String> processList = new ArrayList<String>();

        ProcessBuilder pb = new ProcessBuilder(cmdarray);
        // Sets this process builder's working directory
        if (dir == null) {
            pb.directory(null);
        } else {
            pb.directory(new File(dir));
        }

        Process process = pb.start();
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"));
        try {
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
        } finally {
            input.close();
            process.destroy();
        }

        for (String line : processList) {
            System.out.println(line);
        }
        return processList;
    }

    public static void main(String[] args) throws IOException {
        String[] cmds = {"ping","114.114.114.114"};
        List<String> res = MyProcessUtil.callShellCommand(cmds, null);
        for (String s : res) {
            System.out.println(s);
        }
    }

}
