package com.lzh.demo.service;

import com.lzh.demo.entity.CsvData;
import com.lzh.demo.mapper.CsvDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CsvDataServiceImpl implements CsvDataService{
    final static Logger logger = LoggerFactory.getLogger(CsvDataServiceImpl.class);

    //上次文件大小
    private long pointer = 0;
    //行数记录
    private long lastLine = 0;
    //每次要读取得行数
    private int perSize = 6000000;

    ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);


    @Autowired
    private CsvDataMapper csvDataMapper;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("csvDataJob")
    private Job csvDataJob;

    @Value("${fullPathFile}")
    private String fullPathFile;



    @Override
    public void saveToDb(double data) {
        csvDataMapper.save(data);
    }

    @Override
    public List<CsvData> quaryAll() {
        return csvDataMapper.quaryALL();
    }

    @Override
    public boolean startJob(String file) {
        boolean res = true;
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("path", fullPathFile).toJobParameters();
        try {
            jobLauncher.run(csvDataJob, jobParameters);
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String run(String path) {
        System.out.println(fullPathFile);
        final File tmpLogFile = new File(fullPathFile);
        try {
            realtimeShowLog(tmpLogFile);
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
        return "Start task to read file";
    }

    @Override
    public String stop() {
        if (exec != null) {
            exec.shutdown();
            logger.info("file read stop ！");
        }
        return "Stoped task";
    }

    public void realtimeShowLog(File logFile) throws Exception {

        if (logFile == null) {
            throw new IllegalStateException("logFile can not be null");
        }

        //启动一个线程每10秒读取新增的日志信息
        exec.scheduleWithFixedDelay(() -> {
            //获得变化部分
            try {
                long len = logFile.length();
                if (len < pointer) {
                    logger.info("Log file was reset. Restarting read file.");
                    pointer = 0;
                } else {
                    //指定文件可读可写
                    RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");


                    //获取RandomAccessFile对象文件指针的位置，初始位置是0
                    logger.info("RandomAccessFile文件指针的初始位置:" + pointer);
                    randomFile.seek(pointer);//移动文件指针位置

                    if(pointer == 0) {
                        randomFile.readLine();
                    }
                    String line;
                    while ((line = randomFile.readLine()) != null) {
                        System.out.println(line);
                        pointer = randomFile.getFilePointer();
                    }

                    randomFile.close();
                }

            } catch (Exception e) {
                //实时读取日志异常，需要记录时间和lastTimeFileSize 以便后期手动补充
                e.printStackTrace();
            } finally {
                //将pointer 落地以便下次启动的时候，直接从指定位置获取
            }
        }, 0, 10, TimeUnit.SECONDS);

    }
}
