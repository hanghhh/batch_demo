package com.lzh.demo.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CsvJobListener implements JobExecutionListener {
    public static long total = 0;
    public static String filePath = "";
    private long starTime = 0;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        starTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long endTime = System.currentTimeMillis();
        System.out.println("==========================================================================================");
        System.out.println("文件路径：" + filePath +" 写入条数："+total+"-- 运行时间 ： "+ (endTime - starTime) +"毫秒");
        System.out.println("=============================================================================================");
    }
}
