package com.lzh.demo.conreoller;

import com.lzh.demo.entity.CsvData;
import com.lzh.demo.service.CsvDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    //springbatch 的方式开始任务
    @RequestMapping("/path/{file}")
    public boolean toDb(@PathVariable(name="file") String file) {
        boolean b = service.startJob(file);
        return b;
    }

    //开始读取文件
    @RequestMapping("/run")
    public String toDb2(@RequestParam(required = false) String path) {
        String run = service.run(path);
        return run;
    }

    //结束读取文件
    @RequestMapping("/stop")
    public String stop() {
        String run = service.stop();
        return run;
    }
}
