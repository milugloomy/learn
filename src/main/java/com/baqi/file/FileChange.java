package com.baqi.file;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

/***
 *      ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │  ─┬┘       └┬─  │
 *   │                 │
 *   │       ─┴─       │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
public class FileChange {

    public static void main(String[] args) throws Exception{
        new FileChange().test();
    }


    public void test() throws Exception {
        String filePath = "D:\\WebWorkspace\\ak-mini-common\\";
        FileFilter filter = FileFilterUtils.and(new MyFileFilter());
        FileAlterationObserver filealtertionObserver = new FileAlterationObserver(filePath, filter);
        filealtertionObserver.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onDirectoryCreate(File directory) {
                System.out.println("onDirectoryCreate" + directory.getAbsoluteFile());
                super.onDirectoryCreate(directory);
            }

            @Override
            public void onDirectoryDelete(File directory) {
                System.out.println("onDirectoryDelete" + directory.getAbsoluteFile());
                super.onDirectoryDelete(directory);
            }

            @Override
            public void onFileChange(File file) {
                System.out.println("onFileChange" + file.getAbsoluteFile());
                super.onFileChange(file);
            }

            @Override
            public void onFileCreate(File file) {
                System.out.println("onFileCreate" + file.getAbsoluteFile());
                super.onFileCreate(file);
            }

            @Override
            public void onFileDelete(File file) {
                System.out.println("onFileDelete" + file.getAbsoluteFile());
                super.onFileDelete(file);
            }

            @Override
            public void onStart(FileAlterationObserver observer) {
                System.out.println("onStart");
                super.onStart(observer);
            }

        });

        FileAlterationMonitor filealterationMonitor = new FileAlterationMonitor(1000);
        filealterationMonitor.addObserver(filealtertionObserver);
        filealterationMonitor.start();
    }

}

class MyFileFilter implements IOFileFilter {

    @Override
    public boolean accept(File file) {
        /*String extension=FilenameUtils.getExtension(file.getAbsolutePath());
        if(extension!=null&&extension.equals("txt")){
            return true;
        }
        return false;*/
        return true;
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }


}

