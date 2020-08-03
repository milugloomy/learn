package com.baqi.file;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindApi {

    // 获取2个单引号之间的字符串
    public Pattern pattern = Pattern.compile("(?<=').*?(?=')");
    // 不扫描的文件夹
    public String[] excludeDirs = new String[]{
            "node_modules",
            ".git",
            ".idea"
    };

    public static void main(String[] args) throws IOException {
        FindApi findApi = new FindApi();
        File[] dirs = new File[]{
                new File("D:\\Workspaces\\WebWorkspace\\aikangFamily\\"),   // 家属端
                new File("D:\\Workspaces\\WebWorkspace\\aikangMini\\"),     // 医生端
                new File("D:\\Workspaces\\WebWorkspace\\patient_web\\"),   // 患者端
                new File("D:\\Workspaces\\WebWorkspace\\ms\\"),   // 运管
        };
        for (int i = 0; i < dirs.length; i++) {
            Set<String> apiSet = findApi.find(dirs[i]);
            System.out.println("----------------");
            System.out.println(apiSet);
        }
    }

    public Set<String> find(File dir) throws IOException {
        List<String> apiList = new ArrayList<>();
        Set<String> apiSet = new HashSet<>();
        List<String> pathList = loop(dir);
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String s;
            while ((s = br.readLine()) != null) {
                if(s.length() > 1000) {
                    continue;
                }
                s = s.trim();
                if (s.contains("app.form") || s.contains("app.json") || //医生端 家属端
                        s.contains(".form") || s.contains(".json") ||     //运管工具
                        s.contains("ajaxform") || s.contains("ajaxjson") || s.contains("url = '")) {    //患者端
                    Matcher m = pattern.matcher(s);
                    if (m.find() && m.group(0).startsWith("/")) {
                        apiList.add(m.group(0));
                    }
                }
            }
        }
        for (int i = 0; i < apiList.size(); i++) {
            apiSet.add(apiList.get(i));
        }
        return apiSet;
    }

    public List<String> loop(File dir) {
        List<String> pathList = new ArrayList<>();
        loopSub(dir, pathList);
        return pathList;
    }

    public void loopSub(File dir, List<String> pathList) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            boolean exclude = false;
            for (int j = 0; j < excludeDirs.length; j++) {
                if (file.getAbsolutePath().endsWith(excludeDirs[j])) {
                    exclude = true;
                    break;
                }
            }
            if (exclude) {
                continue;
            }
            if (file.isDirectory()) {
                loopSub(file, pathList);
            } else {
                String absolutePath = file.getAbsolutePath();
                if (absolutePath.endsWith(".js") || absolutePath.endsWith(".vue")) {
                    pathList.add(file.getAbsolutePath());
                }
            }
        }
    }
}
