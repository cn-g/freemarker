package com.gcp.freemarker.service;

import com.gcp.freemarker.db.ColumnClass;
import com.gcp.freemarker.db.DBUtils;
import com.gcp.freemarker.db.TableClass;
import com.google.common.base.CaseFormat;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * 订单服务类
 * @author gcp
 */
@Service
@Slf4j
public class TableService{

    @Resource
    private DBUtils dbUtils;

    Configuration cfg = null;

    {
        cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setTemplateLoader(new ClassTemplateLoader(TableService.class, "/static/templates"));
        cfg.setDefaultEncoding("UTF-8");
    }

    /**
     * 将数据库中的表全部拉出来，生成源代码
     * @param schemaName 数据库名
     * @param realPath 源代码文件存放位置
     * @param startWith 实体名截取（比如表名为m_goods，不截取的实体名为MGoods，令startWith=1，此时生成的实体名为Goods）
     * @return 生成信息
     */
    public String getTableInfo(String schemaName,String realPath,int startWith,String packageName) {
        Connection connection = dbUtils.initDb();
        try {
            //查询数据库中的表名和表注释的sql
            String sql = "select table_name,table_comment \n" +
                    "from information_schema.tables where table_schema='"+schemaName+"'";
            //执行sql
            ResultSet tables = connection.createStatement().executeQuery(sql);
            List<TableClass> tableClassList = new ArrayList<>();
            //获取表名
            while (tables.next()) {
                TableClass tableClass = new TableClass();
                tableClass.setPackageName(packageName);
                String tableName = tables.getString("table_name");
                String tableComment = tables.getString("table_comment");
                String entityName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName).substring(startWith);
                tableClass.setTableName(tableName);
                tableClass.setTableComment(tableComment);
                tableClass.setEntityName(entityName);
                tableClass.setControllerName(entityName + "Controller");
                tableClass.setMapperName(entityName + "Mapper");
                tableClass.setServiceName(entityName+"Service");
                tableClassList.add(tableClass);
            }
            //生成源代码
            createCode(tableClassList,connection,realPath);
            return "生成源代码成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "生成源代码失败";
        }
    }

    /**
     * 根据某个表名生成对应的源代码
     * @param tableName 表名
     * @param realPath 保存路径
     * @param entityName 想要设为的实体名
     */
    public String generateCode(String tableName, String realPath,String entityName,String packageName) {
        try {
            Connection connection = dbUtils.initDb();
            List<TableClass> tableClassList = new ArrayList<>();
            TableClass tableClass = new TableClass();
            tableClass.setTableName(tableName);
            tableClass.setPackageName(packageName);
            tableClass.setEntityName(entityName);
            tableClass.setMapperName(entityName+"Mapper");
            tableClass.setServiceName(entityName+"Service");
            tableClass.setControllerName(entityName+"Controller");
            tableClassList.add(tableClass);
            createCode(tableClassList,connection,realPath);
            return "生成"+tableName+"相关源代码成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "生成"+tableName+"相关源代码失败";
        }
    }

    /**
     * 获取表中的字段信息，根据模版生成源代码
     * @param tableClassList 表信息
     * @param connection 连接
     * @param realPath 保存路径
     */
    private void createCode(List<TableClass> tableClassList,Connection connection,String realPath){
        try {
            //获取每个表的字段名
            for (TableClass tableClass : tableClassList) {
                //引入模版
                Template entityTemplate = cfg.getTemplate("Entity.java.ftl");
                Template mapperJavaTemplate = cfg.getTemplate("Mapper.java.ftl");
                Template mapperXmlTemplate = cfg.getTemplate("Mapper.xml.ftl");
                Template serviceTemplate = cfg.getTemplate("Service.java.ftl");
                Template controllerTemplate = cfg.getTemplate("Controller.java.ftl");
                DatabaseMetaData columnMetaData = connection.getMetaData();
                ResultSet columns = columnMetaData.getColumns(connection.getCatalog(), null, tableClass.getTableName(), null);
                ResultSet primaryKeys = columnMetaData.getPrimaryKeys(connection.getCatalog(), null, tableClass.getTableName());
                List<ColumnClass> columnClassList = new ArrayList<>();
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    String remarks = columns.getString("REMARKS");
                    ColumnClass columnClass = new ColumnClass();
                    columnClass.setRemark(remarks);
                    columnClass.setColumnName(columnName);
                    columnClass.setType(typeName);
                    columnClass.setPropertyName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName));
                    while (primaryKeys.next()) {
                        String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (columnName.equals(pkName)) {
                            columnClass.setIsPrimary(true);
                        }
                    }
                    columnClassList.add(columnClass);
                }
                tableClass.setColumns(columnClassList);
                String path = realPath + "/src/main/java/" + tableClass.getPackageName().replace(".", "/");
                generate(entityTemplate, tableClass, path + "/entity/");
                generate(mapperJavaTemplate, tableClass, path + "/dao/");
                generate(mapperXmlTemplate, tableClass, path + "/dao/");
                generate(serviceTemplate, tableClass, path + "/service/");
                generate(controllerTemplate, tableClass, path + "/controller/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成文件
     * @param template 模版
     * @param tableClass 数据
     * @param path 路径
     * @return 文件路径
     * @throws Exception
     */
    private void generate(Template template, TableClass tableClass, String path) throws Exception {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = path + "/" + tableClass.getEntityName() + template.getName().replace(".ftl", "").replace("Entity", "");
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter out = new OutputStreamWriter(fos);
        template.process(tableClass,out);
        fos.close();
        out.close();
        log.info("保存路径为:{}", fileName);
    }


}
