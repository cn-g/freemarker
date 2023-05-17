package com.gcp.freemarker.db;

import lombok.Data;

@Data
public class ColumnClass {

    /**
     * 对应java属性的名字
     */
    private String propertyName;

    /**
     * 数据库中的名字
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 字段是不是一个主键
     */
    private Boolean isPrimary;

}
