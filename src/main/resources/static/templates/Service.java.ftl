package ${packageName}.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.dao.${mapperName};
import ${packageName}.entity.${entityName};

@Service
public class ${serviceName} extends ServiceImpl<${mapperName},${entityName}> {}