package com.yinhai.tty.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DataBaseType {
    Oracle("0","oracle", "oracle.jdbc.driver.OracleDriver"),
    MySql("1","mysql", "com.mysql.jdbc.Driver"),
    SQLServer("2","sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    private String typeId;
    private String typeName;
    private String driverClassName;

    DataBaseType(String typeId,String typeName, String driverClassName) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.driverClassName = driverClassName;
    }

    public static Map<String, DataBaseType> typeName2Enum = new HashMap<String, DataBaseType>();
    static {
        for (DataBaseType v : values()) {
            typeName2Enum.put(v.typeName, v);
        }
    }

    public static Map<String, DataBaseType> typeId2Enum = new HashMap<String, DataBaseType>();
    static {
        for (DataBaseType v : values()) {
            typeId2Enum.put(v.typeId, v);
        }
    }

    public static DataBaseType fromTypeName(String typeName) {
        return typeName2Enum.get(typeName);
    }

    public static DataBaseType fromTypeId(String typeId) {
        return typeId2Enum.get(typeId);
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    private static Pattern mysqlPattern = Pattern.compile("jdbc:mysql://(.+):\\d+/.+");
    private static Pattern oraclePattern = Pattern.compile("jdbc:oracle:thin:@(.+):\\d+:.+");

    /**
     * 注意：目前只实现了从 mysql/oracle 中识别出ip 信息.未识别到则返回 null.
     */
    public static String parseIpFromJdbcUrl(String jdbcUrl) {
        Matcher mysql = mysqlPattern.matcher(jdbcUrl);
        if (mysql.matches()) {
            return mysql.group(1);
        }
        Matcher oracle = oraclePattern.matcher(jdbcUrl);
        if (oracle.matches()) {
            return oracle.group(1);
        }
        return null;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
