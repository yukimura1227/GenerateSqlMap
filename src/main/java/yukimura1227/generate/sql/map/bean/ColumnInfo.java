package yukimura1227.generate.sql.map.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yukimura1227.util.StringUtil;

/**
 *
 * @author yukimura1227
 *
 */
@NoArgsConstructor
public class ColumnInfo {
    @Getter
    private String columnName;
    @Getter @Setter
    private String columnType;
    @Getter
    private String columnName4Java;
    @Getter @Setter
    private String javaType;
    @Getter
    private String getterMethodName;
    @Getter
    private String setterMethodName;

    public ColumnInfo(String columnName, String columnType ) {
        this.setColumnName(columnName);
        this.columnType = columnType;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
        // カラム名→Java変数名に変換（「_」を取っ払って、キャメルケースに変換）
        columnName4Java = StringUtil._SplitStr2Camel(columnName, false);
        String columnNameCamel = StringUtil._SplitStr2Camel(columnName, true);
        getterMethodName = "get" + columnNameCamel;
        setterMethodName = "set" + columnNameCamel;
    }

}
