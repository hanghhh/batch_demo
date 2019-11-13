package com.lzh.demo.service;

import com.lzh.demo.config.CsvJobListener;
import com.lzh.demo.entity.CsvData;
import com.lzh.demo.mapper.CsvDataMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvDataServiceImpl implements CsvDataService{

    @Autowired
    private CsvDataMapper csvDataMapper;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("csvDataJob")
    private Job csvDataJob;

    private static final String BASE_DIR = "D:/";



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
        CsvJobListener.filePath = BASE_DIR + file;
        boolean res = true;
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("path", BASE_DIR + file).toJobParameters();
        try {
            jobLauncher.run(csvDataJob, jobParameters);
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
        }
        return res;
    }
}
