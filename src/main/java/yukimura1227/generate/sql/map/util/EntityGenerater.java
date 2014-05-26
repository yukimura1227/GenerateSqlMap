package yukimura1227.generate.sql.map.util;


import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import yukimura1227.generate.sql.map.bean.ColumnInfo;
import yukimura1227.generate.sql.map.bean.TableInfoBean;
import yukimura1227.generate.sql.map.holder.PropertyHolder;
import yukimura1227.generate.sql.map.holder.SingletonManager;
import yukimura1227.util.FileUtil;
import yukimura1227.util.StringUtil;

/**
 * Entityクラスを生成するクラス
 * @author yukimura1227
 *
 */
public class EntityGenerater {


    // インスタンス生成不可
    private EntityGenerater() {}


    /**
     * テーブル情報を基に、テーブル情報を保持するためのEntityクラスのjavaソース文字列を生成
     * @param tableInfo
     */
    public static String generateEntityClassStr(TableInfoBean tableInfo) {
        PropertyHolder prop  = SingletonManager.getPropertyHolder();
        String packageEntity = prop.getPackageEntity();

        String entityClassName = StringUtil._SplitStr2Camel( tableInfo.getTableName(), true );

        List<ColumnInfo> columnList = tableInfo.getColumnList();

        List<String> columnName4JavaList = tableInfo.generateColumnNames4JavaList();

        // where句の条件指定用のカラムをcolumnName4JavaListに追加
        for( String columnNameString : tableInfo.generateColumnNames4JavaList() ) {
            columnName4JavaList.add( columnNameString + "4Where");
        }
        String columnName4JavaListCsv = StringUtils.join(columnName4JavaList, ",");

        // velocityのSetup
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put( "entityPackage"         , packageEntity );
        velocityContext.put( "entityClassName"       , entityClassName);
        velocityContext.put( "columnList"            , columnList);
        velocityContext.put( "columnName4JavaListCsv", columnName4JavaListCsv);

        // templateとマージ
        String entityTemplatePath = SingletonManager.getPropertyHolder().getEntityTemplatePath();

        String entityClassSource = VelocityUtil.templateMerge(entityTemplatePath, velocityContext);
        System.out.println(entityClassSource);

        try {
            FileUtil.writeJavaFile( prop.getOutputPath(), packageEntity, entityClassName, entityClassSource );

        } catch (IOException ioe){
            ioe.printStackTrace();

        }

        return entityClassSource;

    }

}
