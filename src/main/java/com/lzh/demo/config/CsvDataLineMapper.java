package com.lzh.demo.config;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.lzh.demo.entity.CsvData;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.util.StringUtils;

public class CsvDataLineMapper implements LineMapper<CsvData> {
    private MappingJsonFactory factory = new MappingJsonFactory();

    @Override
    public CsvData mapLine(String line, int lineNumber) throws Exception {
        CsvData csvData = new CsvData();
        if(StringUtils.hasText(line)) {
            csvData.setDataLine(Double.parseDouble(line));
        }
        return csvData;
    }
}
