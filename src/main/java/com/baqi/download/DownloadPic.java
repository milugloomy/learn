package com.baqi.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadPic {

    public void meitulu(String path, String[] urls) throws IOException{
        String domain = "https://www.meitulu.com";
        for (String url : urls) {
            String folderName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            System.out.println(folderName);
            String folderPath = path + folderName + "\\";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            while (true) {
                System.out.println(url);
                Element body = Jsoup.connect(url).get().body();
                Element next = body.getElementsByClass("a1").get(1);
                String nextSrc = domain + next.attr("href");
                Elements images = body.getElementsByClass("content_img");
                for (int i = 0; i < images.size(); i++) {
                    String src = images.get(i).attr("src");
                    String fileName = src.substring(src.lastIndexOf("/") + 1);
                    String picPath = folderPath + fileName;
                    downloadPic(src, picPath);
                }
                if(nextSrc.equals(url)) {
                    break;
                }
                url = nextSrc;
            }
        }
    }
    private void downloadPic(String src, String picPath) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        InputStream is = connection.getInputStream();
        File file = new File(picPath);
        FileOutputStream out = new FileOutputStream(file);
        int i = 0;
        while((i = is.read()) != -1){
            out.write(i);
        }
        System.out.println(src + "   下载完毕");
        out.close();
        is.close();
    }
}
