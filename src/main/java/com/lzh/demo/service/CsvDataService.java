package com.lzh.demo.service;

import com.lzh.demo.entity.CsvData;

import java.util.List;

public interface CsvDataService {
    public void saveToDb(double data);
    public List<CsvData> quaryAll();
    public boolean startJob(String path);
    public String run(String path);
    String stop();
}
