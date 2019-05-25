package com.kyoshii.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.kyoshii.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-25
 * @Description: database操作的工具类
 */
public class DatabaseHelper {
    private static final QueryRunner QUERY_RUNNER;
    private static final DruidDataSource DATA_SOURCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    static {
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new DruidDataSource();
        DATA_SOURCE.configFromPropety(PropsUtil.loadProps("druid-pool.properties"));
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return connection;
    }

    /**
     * 自动关闭PreparedStatement和ResultSet但是不会关闭connection
     *
     * @param entityClass 映射的实体类
     * @param sql         sql的执行语句
     * @param params      sql中的?对应参数
     * @param <T>类的类型
     * @return entityList
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        try {
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
        }
        return entityList;
    }

    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T query = null;
        try {
            Connection connection = getConnection();
            query = QUERY_RUNNER.query(connection, sql, new BeanHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
        }
        return query;
    }


    /**
     * 查询最通用的方法
     *
     * @param sql    sql的执行语句
     * @param params sql中的?对应参数
     * @return List中对应的列和值
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = null;
        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
        }
        return result;
    }

    /**
     * 更新语句
     *
     * @param sql    sql的执行语句
     * @param params sql中的?对应参数
     * @return 更新影响的行数
     */
    public static int executeUpdate(String sql, Object... params) {
        int row = 0;
        try {
            Connection connection = getConnection();
            row = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
        }
        return row;
    }


    /**
     * 更强大的插入执行方法，并不需要sql语句
     *
     * @param entityClass 实体类的Class
     * @param fieldMap    插入的实体类
     * @param <T>         实体类的类型
     * @return 成功或者失败
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (isMapEmpty(fieldMap)) {
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }
        String sql = "insert into " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(",");
            values.append("?").append(",");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " values" + values;
        int row = 0;
        try {
            Connection connection = getConnection();
            row = QUERY_RUNNER.update(connection, sql, fieldMap.values().toArray());
        } catch (SQLException e) {
            LOGGER.error("execute insert entity failure", e);
        }
        return row != 0;
    }

    /**
     * 更强大的更新执行方法，并不需要sql语句
     *
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (isMapEmpty(fieldMap)) {
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql = "update " + getTableName(entityClass) + " set ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " where id=?";
        List<Object> params = new ArrayList<>(fieldMap.values());
        params.add(id);
        int row = 0;
        try {
            Connection connection = getConnection();
            row = QUERY_RUNNER.update(connection, sql, params.toArray());
        } catch (SQLException e) {
            LOGGER.error("execute update entity failure", e);
        }
        return row != 0;
    }

    /**
     * 删除操作
     *
     * @param entityClass
     * @param params
     * @param <T>
     * @return
     */
    public static <T> boolean executeDelete(Class<T> entityClass, Object... params) {
        String sql = "delete from " + getTableName(entityClass) + " where id=?";
        int row = 0;
        try {
            Connection connection = getConnection();
            row = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute delete failure", e);
        }
        return row > 0;
    }

    public static void executeSqlFile(String filePath) {
        InputStream inputStream = DatabaseHelper.class.getClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        String sql;
        Connection connection = getConnection();
        try {
            while ((sql = reader.readLine()) != null) {
                QUERY_RUNNER.execute(connection, sql);
            }
        } catch (IOException | SQLException e) {
            LOGGER.error("execute sql file failure", e);
        }

    }

    private static String getTableName(Class<?> entityClass) {
        if (entityClass == null) {
            LOGGER.error("can not get class simple name");
            throw new RuntimeException("can not get class simple name");
        }
        return entityClass.getSimpleName().toLowerCase();
    }

    private static boolean isMapEmpty(Map map) {
        return map == null || map.isEmpty();
    }


}
