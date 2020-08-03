package com.baqi.pic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Move {
    public static void main(String[] args) {
        String descPath = "D:\\360安全浏览器下载\\all\\";
        File mainDir = new File("D:\\360安全浏览器下载\\种马的气息");
        File[] dirs = mainDir.listFiles();
        for (int i = 0; i < dirs.length; i++) {
            File dir = dirs[i];
            String dirName = dir.getName();
            File[] pics = dir.listFiles();
            for (int j = 0; j < pics.length; j++) {
                File pic = pics[j];
                String newPicName = dirName + "_" + pic.getName();
                File descPic = new File(descPath + newPicName);
                copyPic(pic, descPic);
                System.out.println(descPic.getAbsolutePath());
            }
        }
    }

    public static void copyPic(File srcPic, File desPic){
        byte[] b=new byte[(int)srcPic.length()];
        FileInputStream in=null;
        FileOutputStream out=null;
        try {
            in=new FileInputStream(srcPic);
            //没有指定文件则会创建
            out=new FileOutputStream(desPic);
            //read()--int，-1表示读取完毕
            while(in.read(b)!=-1){
                out.write(b);
            }
            out.flush();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
