package com.lzh.demo.mapper;

import com.lzh.demo.entity.CsvData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CsvDataMapper {
    void save(double data);

    @Select("select data_line from lzh_test")
    @Results({
            @Result(property = "dataLine", column = "data_line", jdbcType = JdbcType.DOUBLE)
    })
    List<CsvData> quaryALL();

    void batchInsert1(List<String> list);
}
