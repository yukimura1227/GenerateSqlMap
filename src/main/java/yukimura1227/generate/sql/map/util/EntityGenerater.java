package yukimura1227.generate.sql.map.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

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

    private static Template velocityTemplate;
    private static VelocityContext velocityContext;

    // インスタンス生成不可
    private EntityGenerater() {}

    private static void velocitySetup() {
        // 基本的にエンドユーザが変更する必要が無いためpropertiesのパスは固定でOK
        final String VELOCITY_PROPERTY_PATH = "/sample/config/velocity.properties";
        InputStream is = EntityGenerater.class.getResourceAsStream(VELOCITY_PROPERTY_PATH);
        Properties prop = new Properties();
        try {
            prop.load(is);

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();

        }

        VelocityEngine engine = new VelocityEngine();
        engine.init(prop);

        velocityContext = new VelocityContext();

        String entityTemplatePath = SingletonManager.getPropertyHolder().getEntityTemplatePath();
        velocityTemplate = engine.getTemplate(entityTemplatePath);

    }


    /**
     * テーブル情報を基に、テーブル情報を保持するためのEntityクラスのjavaソース文字列を生成
     * @param tableInfo
     */
    public static String generateEntityClassStr(TableInfoBean tableInfo) {
        PropertyHolder prop  = SingletonManager.getPropertyHolder();
        String packageEntity = prop.getPackageEntity();

        String entityClassName = StringUtil._SplitStr2Camel(tableInfo.getTableName(),true);

        List<ColumnInfo> columnList = tableInfo.getColumnList();

        List<String> columnName4JavaList = tableInfo.generateColumnNames4JavaList();
        for( String columnNameString : tableInfo.generateColumnNames4JavaList() ) {
            columnName4JavaList.add( columnNameString + "4Where");
        }
        String columnName4JavaListCsv = StringUtils.join(columnName4JavaList, ",");

        // velocityのSetup
        velocitySetup();
        velocityContext.put( "entityPackage"         , packageEntity );
        velocityContext.put( "entityClassName"       , entityClassName);
        velocityContext.put( "columnList"            , columnList);
        velocityContext.put( "columnName4JavaListCsv", columnName4JavaListCsv);

        // templateとマージ
        StringWriter writer = new StringWriter();
        velocityTemplate.merge(velocityContext, writer);

        String entityClassSource = writer.toString();
        System.out.println(entityClassSource);

        try {
            FileUtil.writeJavaFile( prop.getOutputPath(), packageEntity, entityClassName, entityClassSource );

        } catch (IOException ioe){
            ioe.printStackTrace();

        }

        return entityClassSource;

    }

}
