package com.baqi.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
public class ReadExcel {

    static SimpleDateFormat sdf;
    static Date september;

    static {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            september = sdf.parse("2019-09-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ParseException {

        String path = ReadExcel.class.getResource("").getPath();
        Set<String> hospitalMobileSet = getHospitalMobiles(path);
        Set<String> akyMobileSet = getAkyMobiles(path);

        System.out.println("医院有手机号用户：" + hospitalMobileSet.size());
        System.out.println("爱康云有手机号用户：" + akyMobileSet.size());
        Set<String> diffSet = new HashSet<>();
        for (String mobile : akyMobileSet) {
            if (hospitalMobileSet.contains(mobile) == false) {
                diffSet.add(mobile);
            }
        }
        System.out.println("在爱康云但不在医院的用户：" + diffSet.size());

        System.out.println(hospitalMobileSet);
        System.out.println(akyMobileSet);
        System.out.println(diffSet);
    }

    public static Set<String> getAkyMobiles(String path) throws IOException {
        Set<String> mobileSet = new HashSet();
        FileInputStream fis = new FileInputStream(path + "patient_info.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s;
        while ((s = br.readLine()) != null) {
            String[] arr = s.split(",");
            String mobile = arr[6].replaceAll("\"", "");
            String createDateStr = arr[9].replaceAll("\"", "");
            Date createDate = null;
            try {
                createDate = sdf.parse(createDateStr);
            } catch (ParseException e) {
                continue;
            }
            if(createDate.getTime() > september.getTime()) {
                mobileSet.add(mobile);
            }
        }
        return mobileSet;
    }

    public static Set<String> getHospitalMobiles(String path) throws IOException {
        Set<String> mobileSet = new HashSet();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getAbsolutePath().endsWith(".xls")) {
                Set<String> set = getHospitalMobile(file);
                mobileSet.addAll(set);
            }
        }
        return mobileSet;
    }

    public static Set<String> getHospitalMobile(File file) throws IOException {
        System.out.println("读取文件" + file.getAbsolutePath());
        Set<String> subSet = new HashSet();

        FileInputStream is = new FileInputStream(file);
        HSSFWorkbook excel = new HSSFWorkbook(is);
        HSSFSheet sheet0 = excel.getSheetAt(0);
        //取第一行，计算手机号所在位置
        Row row0 = sheet0.getRow(0);
        Integer mobileIndex = -1;
        for (Cell cell : row0) {
            mobileIndex++;
            if (cell.getStringCellValue().equals("联系电话")) {
                break;
            }
        }
        //取所有手机号
        for (Row row : sheet0) {
            String mobile = null;
            Cell mobileCell = row.getCell(mobileIndex);
            if (mobileCell == null) {
                continue;
            }
            if (mobileCell.getCellType().equals(CellType.NUMERIC)) {
                Double cellValue = mobileCell.getNumericCellValue();
                mobile = String.valueOf(cellValue.longValue());
            }
            if (mobileCell.getCellType().equals(CellType.STRING)) {
                mobile = mobileCell.getStringCellValue();
            }
            if (mobile != null && mobile.length() == 11) {
                subSet.add(mobile);
            }
        }
        return subSet;
    }
}
