package com.baqi.download;

import java.io.IOException;

public class MengNaiAiHua {

    public static void main(String[] args) throws IOException {
        String path = "E:\\360Browser\\360se6\\download\\梦乃爱华\\";
        String domain = "https://www.meitulu.com";
        String[] urls = new String[]{
                "https://www.meitulu.com/item/19569.html",
                "https://www.meitulu.com/item/12161.html",
                "https://www.meitulu.com/item/11874.html",
                "https://www.meitulu.com/item/4509.html",
        };

        DownloadPic downloadPic = new DownloadPic();
        downloadPic.meitulu(path, urls);
    }

}
