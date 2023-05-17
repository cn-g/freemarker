package ${packageName}.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${packageName}.entity.${entityName};

@Mapper
public interface ${mapperName} extends BaseMapper<${entityName}> {

}