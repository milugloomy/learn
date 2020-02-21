package com.baqi.download;

import java.io.IOException;

public class XiTiaoLiuli {

    public static void main(String[] args) throws IOException {
        String path = "E:\\360Browser\\360se6\\download\\西条琉璃\\";
        String[] urls = new String[]{
                "https://www.meitulu.com/item/3011.html",
                "https://www.meitulu.com/item/2940.html"
        };

        DownloadPic downloadPic = new DownloadPic();
        downloadPic.meitulu(path, urls);
    }

}
