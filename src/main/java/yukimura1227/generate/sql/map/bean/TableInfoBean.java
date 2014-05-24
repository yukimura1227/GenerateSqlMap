package yukimura1227.generate.sql.map.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import yukimura1227.util.StringUtil;

/**
 *
 * @author yukimura1227
 *
 */
public class TableInfoBean {
    @Getter @Setter
    private String tableName;
    @Getter @Setter
    private List<ColumnInfo> columnList   = null;
    private String columnStr4Select       = null;
    private String columnStr4InsertValues = null;
    public TableInfoBean() {};
    public TableInfoBean(String tableName){
        this.setTableName(tableName);
        columnList = new ArrayList<ColumnInfo>();
    }
    public void addColumn(ColumnInfo columnInfo){
        this.columnList.add(columnInfo);
    }
    public void addColumn(String columnName, String columnType) {
        ColumnInfo column = new ColumnInfo(columnName, columnType);
        addColumn(column);
    }
    public String generateColumnStr4Select() {
        if( columnStr4Select != null ) return columnStr4Select;

        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < columnList.size(); i++ ) {
            sb.append( columnList.get(i).getColumnName() );
            if( i < columnList.size() - 1 ) sb.append( "," );
        }
        columnStr4Select = sb.toString();
        return columnStr4Select;
    }

    public String generateColumnStr4Insert() {
        return generateColumnStr4Select();
    }
    public String generateColumnStr4InsertValues() {
        if( columnStr4InsertValues != null ) return columnStr4InsertValues;

        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < columnList.size(); i++ ) {
            sb.append("#{" +  StringUtil._SplitStr2Camel(columnList.get(i).getColumnName(),false) + "}");
            if( i < columnList.size() - 1 ) sb.append( "," );
        }
        columnStr4InsertValues = sb.toString();
        return columnStr4InsertValues;
    }

    public List<String> generateColumnNames4JavaList() {
        List<String> columnNamesList = new ArrayList<>();

        for( ColumnInfo targetCol : columnList ) {
            columnNamesList.add( targetCol.getColumnName4Java() );
        }

        return columnNamesList;
    }

}
