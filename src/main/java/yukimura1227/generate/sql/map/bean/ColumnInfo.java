package yukimura1227.generate.sql.map.bean;

import yukimura1227.util.StringUtil;

/**
 *
 * @author yukimura1227
 *
 */
public class ColumnInfo {
    private String columnName;
    private String columnType;
    private String columnName4Java;
    private String javaType;
    private String getterMethodName;
    private String setterMethodName;

    public ColumnInfo(){};
    public ColumnInfo(String columnName, String columnType ) {
        this.setColumnName(columnName);
        this.columnType = columnType;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
        // カラム名→Java変数名に変換（「_」を取っ払って、キャメルケースに変換）
        columnName4Java = StringUtil._SplitStr2Camel(columnName, false);
        String columnNameCamel = StringUtil._SplitStr2Camel(columnName, true);
        getterMethodName = "get" + columnNameCamel;
        setterMethodName = "set" + columnNameCamel;
    }
    public String getColumnType() {
        return columnType;
    }
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
    public String getJavaType() {
        return javaType;
    }
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
    public String getColumnName4Java() {
        return columnName4Java;
    }
    public String getGetterMethodName() {
        return getterMethodName;
    }
    public String getSetterMethodName() {
        return setterMethodName;
    }

}
