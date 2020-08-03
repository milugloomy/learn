package com.baqi.git;

//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.LoggerFactory;

import java.io.IOException;

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
public class Main {

    static String villageDoctor = "D:\\WebWorkspace\\village-doctor";
    static String patientWeb = "D:\\WebWorkspace\\patient_web";
    static String doctorWeb = "D:\\WebWorkspace\\doctor_web";
    static String heartPatient = "D:\\WebWorkspace\\heart-patient";
    static String heartDoctor = "D:\\WebWorkspace\\heart-doctor";
    static String tumour = "D:\\WebWorkspace\\tumour";
    static String tumourDoctor = "D:\\WebWorkspace\\tumour-doctor";
    static String ms = "D:\\WebWorkspace\\ms";
    static String mall = "D:\\WebWorkspace\\mall";
    static String akbase = "D:\\AkWorkspace\\akbase";
    static String chronic = "D:\\AkWorkspace\\chronic";

    public static void main(String[] args) throws IOException {
        // 设置日志级别
//        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
//        Logger logger = (Logger) iLoggerFactory.getLogger("root");
//        logger.setLevel(Level.INFO);

        String repoPath = villageDoctor;
        String username = "milugloomy@163.com";
        String password = "Password1";
        String comment = "12313213213";
        CherryPick cherryPick = new CherryPick(repoPath, username, password, comment);
        cherryPick.execute();
    }
}
