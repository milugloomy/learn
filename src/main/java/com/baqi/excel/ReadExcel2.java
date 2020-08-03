package com.baqi.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class ReadExcel2 {

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

    public static void main(String[] args) throws IOException {

        String path = ReadExcel2.class.getResource("").getPath();
        Set<String> hospitalNameSet = getHospitalNames(path);
        Set<String> akyNameSet = getAkyNames(path);

        System.out.println("医院用户：" + hospitalNameSet.size());
        System.out.println("爱康云用户：" + akyNameSet.size());
        Set<String> diffSet = new HashSet<>();
        for (String name : akyNameSet) {
            if (hospitalNameSet.contains(name) == false) {
                diffSet.add(name);
            }
        }
        System.out.println("在爱康云但不在医院的用户：" + diffSet.size());

        System.out.println(hospitalNameSet);
        System.out.println(akyNameSet);
        System.out.println(diffSet);
    }

    public static Set<String> getAkyNames(String path) throws IOException {
        Set<String> nameSet = new HashSet();
        FileInputStream fis = new FileInputStream(path + "patient_info.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s;
        while ((s = br.readLine()) != null) {
            String[] arr = s.split(",");
            String name = arr[1].replaceAll("\"", "");
            String createDateStr = arr[9].replaceAll("\"", "");
            Date createDate = null;
            try {
                createDate = sdf.parse(createDateStr);
            } catch (ParseException e) {
                continue;
            }
            if(createDate.getTime() > september.getTime()) {
                nameSet.add(name);
            }
        }
        return nameSet;
    }

    public static Set<String> getHospitalNames(String path) throws IOException {
        Set<String> nameSet = new HashSet();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getAbsolutePath().endsWith(".xls")) {
                Set<String> set = getHospitalName(file);
                nameSet.addAll(set);
            }
        }
        return nameSet;
    }

    public static Set<String> getHospitalName(File file) throws IOException {
        System.out.println("读取文件" + file.getAbsolutePath());
        Set<String> subSet = new HashSet();

        FileInputStream is = new FileInputStream(file);
        HSSFWorkbook excel = new HSSFWorkbook(is);
        HSSFSheet sheet0 = excel.getSheetAt(0);
        //取第一行，计算手机号所在位置
        Row row0 = sheet0.getRow(0);
        Integer nameIndex = -1;
        for (Cell cell : row0) {
            nameIndex++;
            if (cell.getStringCellValue().equals("患者姓名")) {
                break;
            }
        }
        //取所有手机号
        for (Row row : sheet0) {
            Cell nameCell = row.getCell(nameIndex);
            if (nameCell == null) {
                continue;
            }
            String name = nameCell.getStringCellValue();
            if (name != null) {
                subSet.add(name);
            }
        }
        return subSet;
    }
}
