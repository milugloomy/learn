package com.baqi.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
public class DataSourceTest {
    volatile static Boolean stop = false;

    public static void main(String[] args) throws SQLException, InterruptedException {

        /*
        ds.prefix=jdbc:mysql://47.105.145.227:3306/
        ds.suffix=?allowMultiQueries=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false
        ds.driverClassName=com.mysql.jdbc.Driver
        ds.username=aikang_rw
        ds.password=TOsX@r44f4
        ds.initialSize:1
        ds.maxActive:20
        ds.maxWait:60000
        ds.minIdle:10
        ds.timeBetweenEvictionRunsMillis:60000
        ds.minEvictableIdleTimeMillis:300000
        ds.validationQuery:SELECT 'x'
        ds.testWhileIdle:true
        ds.testOnBorrow:false
        ds.testOnReturn:false
        ds.maxOpenPreparedStatements:100
        ds.filters:wall,stat*/

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://47.105.145.227:3306/base_db_2?allowMultiQueries=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        dataSource.setUsername("aikang_rw");
        dataSource.setPassword("TOsX@r44f4");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(6);
        dataSource.setMaxWait(2000);
        dataSource.setMinIdle(1);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setMaxOpenPreparedStatements(2);

        Integer[] params = new Integer[]{4152, 4157, 4237, 4293, 4322};
        CountDownLatch latch = new CountDownLatch(params.length);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(params.length);
        for (int i = 0; i < params.length; i++) {
            final Integer index = i;
            fixedThreadPool.submit(() -> {
                DruidPooledConnection conn = null;
                try {
                    conn = dataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        PreparedStatement query = conn.prepareStatement("select * from patient_info where patient_id = ?");
                        query.setInt(1, params[index]);
                        ResultSet rs = query.executeQuery();
                        if (rs.next()) {
                            String patientName = rs.getString("patient_name");
                            System.out.println("查询" + patientName + "的信息");
                        }
                        Thread.sleep(100);
                        if (stop) {
                            latch.countDown();
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        DruidPooledConnection conn = dataSource.getConnection();
        conn.setAutoCommit(false);

        PreparedStatement query = conn.prepareStatement("select * from patient_info");
        ResultSet rs = query.executeQuery();
        List<Map<String, Object>> patientList = new ArrayList<>();
        while (rs.next()) {
            int patientId = rs.getInt("patient_id");
            String patientName = rs.getString("patient_name");
            Map<String, Object> map = new HashMap();
            map.put("patientId", patientId);
            map.put("patientName", patientName);
            patientList.add(map);
        }
        for (int i = 0; i < patientList.size(); i++) {
            Thread.sleep(100);
//            PreparedStatement update = conn.prepareStatement("update patient_info set remark = ? where patient_id = ?");
//            update.setString(1, "哈哈");
//            update.setInt(2, (Integer) patientList.get(i).get("patientId"));
//            update.executeUpdate();
            System.out.println("-----更新第" + i + "条记录-----");
        }
        conn.commit();
        System.out.println("--------------------已提交--------------------");
        //主线程执行完了，告诉子线程停止，stop为volatile，子线程可见
        stop = true;
        //等待子线程执行完毕
        latch.await();
        //等待子线程执行完毕后关闭线程池
        fixedThreadPool.shutdown();
    }
}
