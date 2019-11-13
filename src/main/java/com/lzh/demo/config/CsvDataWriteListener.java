package com.lzh.demo.config;

import com.lzh.demo.entity.CsvData;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CsvDataWriteListener implements ItemWriteListener<CsvData> {

    @Override
    public void beforeWrite(List<? extends CsvData> list) {

    }

    @Override
    public void afterWrite(List<? extends CsvData> list) {
        CsvJobListener.total += list.size();
    }

    @Override
    public void onWriteError(Exception e, List<? extends CsvData> list) {

    }
}
