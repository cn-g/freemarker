package ${packageName}.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("${tableName}")
@ApiModel(value = "${entityName}对象", description = "${entityName}表")
public class ${entityName}{
<#if columns??>
    <#list columns as column>

        <#if column.type='VARCHAR'||column.type='TEXT'||column.type='CHAR'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private String ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='INT' || column.type='TINYINT'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private Integer ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='DATETIME'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private LocalDateTime ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='BIGINT'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private Long ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='DOUBLE' || column.type='DECIMAL'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private BigDecimal ${column.propertyName?uncap_first};
        </#if>
        <#if column.type='BIT'>
    @ApiModelProperty(value = "${column.remark}")
            <#if column.isPrimary?? && column.isPrimary>
    @TableId("${column.columnName}")
            <#else>
    @TableField("${column.columnName}")
            </#if>
    private Boolean ${column.propertyName?uncap_first};
        </#if>
    </#list>
</#if>

}