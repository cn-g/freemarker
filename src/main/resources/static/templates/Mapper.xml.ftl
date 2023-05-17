<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.dao.${mapperName}">

    <resultMap id="BaseResultMap" type="${packageName}.entity.${entityName}">
        <#list columns as column>
            <<#if column.primary?? && column.primary>id<#else>result</#if> column="${column.columnName}" property="${column.propertyName?uncap_first}"/>
        </#list>
    </resultMap>

</mapper>