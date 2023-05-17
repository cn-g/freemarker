package com.gcp.freemarker.controller;

import com.gcp.freemarker.service.TableService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/order")
public class TableController {

    @Resource
    private TableService tableService;

    @PostMapping("/initProj")
    public String initProj() {
        //生成mall数据库中所有表相关的源代码，并保存在D:/mall文件夹下
        return tableService.getTableInfo("mall","D:/mall",1,"com.gcp.freemarker");
    }

    @PostMapping("/generateCode")
    public String generateCode() {
        //生成m_order表相关的源代码，并保存在本项目的包内
        return tableService.generateCode("m_order", System.getProperty("user.dir"),"Order","com.gcp.freemarker");
    }

}
