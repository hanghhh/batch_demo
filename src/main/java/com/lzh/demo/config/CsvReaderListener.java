package com.lzh.demo.config;

import com.lzh.demo.entity.CsvData;
import org.springframework.batch.core.ItemReadListener;

public class CsvReaderListener implements ItemReadListener<CsvData> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(CsvData csvData) {

    }

    @Override
    public void onReadError(Exception e) {

    }
}
