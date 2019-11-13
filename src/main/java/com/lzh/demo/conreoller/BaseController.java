package com.lzh.demo.conreoller;

import com.lzh.demo.entity.CsvData;
import com.lzh.demo.service.CsvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class BaseController {

    @Autowired
    private CsvDataService service;

    @RequestMapping("/data/{data}")
    public boolean save(@PathVariable double data) {
        service.saveToDb(data);
        return true;
    }

    @RequestMapping("/all")
    public List<CsvData> quaryAll() {
        return service.quaryAll();
    }

    @RequestMapping("/path/{file}")
    public boolean toDb(@PathVariable(name="file") String file) {
        boolean b = service.startJob(file);
        return b;
    }
}
