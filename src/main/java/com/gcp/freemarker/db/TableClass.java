package com.gcp.freemarker.db;

import lombok.Data;

import java.util.List;

/**
 * @author Admin
 */
@Data
public class TableClass {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 服务层名称
     */
    private String serviceName;

    /**
     * dao层名称
     */
    private String mapperName;

    /**
     * 控制层名称
     */
    private String controllerName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 表字段
     */
    private List<ColumnClass> columns;


}
