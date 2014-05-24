package yukimura1227.generate.sql.map.util;

import static yukimura1227.generate.sql.map.util.CommonConstants.COMMON_MAPPER_NAME;
import static yukimura1227.generate.sql.map.util.CommonConstants.COMMON_SELECT_MAPPER_NAME;
import static yukimura1227.generate.sql.map.util.CommonConstants.DOUBLE_QUATE;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
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
 * SQLMapを生成するクラス
 * @author yukimura1227
 *
 */
public class SQLMapGenerater {
    private static boolean isVelocitySetupDone = false;
    private static Template        velocityMapperXmlTemplate;
    private static VelocityContext velocityContext4MapperXmlTemplate;
    private static Template        velocityCommonMapperTemplate;
    private static VelocityContext velocityContext4CommonMapperTemplate;
    private static Template        velocityCommonSelectMapperTemplate;
    private static VelocityContext velocityContext4CommonSelectMapperTemplate;
    private static Template        velocityEntityMapperTemplate;
    private static VelocityContext velocityContext4EntityMapperTemplate;

    private SQLMapGenerater() {}

    private static void velocitySetup() {
        if( isVelocitySetupDone ) {
            return;
        }
        // 基本的にエンドユーザが変更する必要が無いためpropertiesのパスは固定でOK
        InputStream is = EntityGenerater.class.getResourceAsStream("/sample/config/velocity.properties");
        Properties prop = new Properties();
        try {
            prop.load(is);

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();

        }

        VelocityEngine engine = new VelocityEngine();
        engine.init(prop);

        velocityContext4MapperXmlTemplate          = new VelocityContext();
        velocityContext4CommonMapperTemplate       = new VelocityContext();
        velocityContext4CommonSelectMapperTemplate = new VelocityContext();
        velocityContext4EntityMapperTemplate       = new VelocityContext();

        PropertyHolder propertyHolder = SingletonManager.getPropertyHolder();
        String mapplerXmlTemplatePath            = propertyHolder.getMapplerXmlTemplatePath();
        String commonMapperXmlTemplatePath       = propertyHolder.getCommonXmlMaplerTemplate();
        String selectCommonMapperXmlTemplatePath = propertyHolder.getCommonSelectMapperXmlTemplate();
        String entityInterfaceTemplatePath       = propertyHolder.getEntityInterfaceTemplatePath();

        velocityMapperXmlTemplate          = engine.getTemplate(mapplerXmlTemplatePath);
        velocityCommonMapperTemplate       = engine.getTemplate(commonMapperXmlTemplatePath);
        velocityCommonSelectMapperTemplate = engine.getTemplate(selectCommonMapperXmlTemplatePath);
        velocityEntityMapperTemplate       = engine.getTemplate(entityInterfaceTemplatePath);

        isVelocitySetupDone = true;

    }

    /**
     * テーブル情報を基に、テーブル情報を保持するためのSQLMapper用の文字列を生成
     * @param tableInfo
     */
    public static String generateSQLMapStr(TableInfoBean tableInfo) {
        PropertyHolder prop         = SingletonManager.getPropertyHolder();
        String tableName            = tableInfo.getTableName();
        String entityClassName      = StringUtil._SplitStr2Camel(tableName, /* 1文字目を大文字にする */ true);
        String mapperClassName      = entityClassName + "Mapper";
        List<ColumnInfo> columnList = tableInfo.getColumnList();
        String mapperPackage        = prop.getPackageMapper();
        String outputPath           = prop.getOutputPath();
        String packageBase          = prop.getPackageBase();

        // 作業場所
        velocitySetup();
        velocityContext4MapperXmlTemplate.put( "packageMapper"   , mapperPackage );
        velocityContext4MapperXmlTemplate.put( "entityClassName" , entityClassName);
        velocityContext4MapperXmlTemplate.put( "mapperClassName" , mapperClassName);
        velocityContext4MapperXmlTemplate.put( "tableInfo"       , tableInfo);

        int colNameMaxLength = calcMaxColNameLength(columnList);
        List<String[]> resultPartList = new ArrayList<>();
        List<String[]> wherePartList  = new ArrayList<>();
        List<String[]> setPartList    = new ArrayList<>();
        for( ColumnInfo targetColumn : columnList ) {
            String columnName = targetColumn.getColumnName();

            // resultタグのcolumn属性およびproperty属性の値を生成
            String columnPart = "";
            columnPart = StringUtil.embrace(columnName ,DOUBLE_QUATE);
            columnPart = StringUtils.rightPad(columnPart, colNameMaxLength+2);// +2は"でくくっている分

            String propertyPart = "";
            propertyPart = StringUtil._SplitStr2Camel(columnName,false);
            propertyPart = StringUtil.embrace(propertyPart,DOUBLE_QUATE);
            propertyPart = StringUtils.rightPad(propertyPart,colNameMaxLength+2);// +2は"でくくっている分
            resultPartList.add(new String[]{columnPart, propertyPart});

            // where句用の文字列生成
            String propertyName4Where    = StringUtil._SplitStr2Camel(columnName + "4Where",false);
            String colName4WherePaded    = StringUtils.rightPad(propertyName4Where         , colNameMaxLength+6);// 6は4Whereの長さ
            String colNamePaded          = StringUtils.rightPad(columnName                 , colNameMaxLength);
            String colName4WhereRefPaded = StringUtils.rightPad("#{"+propertyName4Where+"}", colNameMaxLength+9);// 9は#{}と4Whereの長さ
            wherePartList.add(new String[]{colName4WherePaded, colNamePaded, colName4WhereRefPaded});

            // updateのset句用の文字列生成
            String propertyName         = StringUtil._SplitStr2Camel(columnName, false);
            String propertyNamePaded    = StringUtils.rightPad(propertyName         , colNameMaxLength);
            String propertyNameRefPaded = StringUtils.rightPad("#{"+propertyName+"}", colNameMaxLength+3);// 3は#{}の長さ
            setPartList.add(new String[]{propertyNamePaded, colNamePaded, propertyNameRefPaded});

        }
        velocityContext4MapperXmlTemplate.put("resultPartList", resultPartList);
        velocityContext4MapperXmlTemplate.put("wherePartList" , wherePartList);
        velocityContext4MapperXmlTemplate.put("setPartList"   , setPartList);

        // templateとマージ
        StringWriter writer = new StringWriter();
        velocityMapperXmlTemplate.merge(velocityContext4MapperXmlTemplate, writer);

        String sqlMapXml = writer.toString();

        try {
            FileUtil.writeXmlFile(outputPath , packageBase, mapperClassName, sqlMapXml);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        System.out.println(sqlMapXml);

        return sqlMapXml;
    }

    /**
     * CommonSelectMapperクラスの生成
     * @return
     */
    public static String generateCommonSelectMapperSource() {
        PropertyHolder prop         = SingletonManager.getPropertyHolder();

        String packageMapper = prop.getPackageMapper();

        velocitySetup();
        velocityContext4CommonSelectMapperTemplate.put( "packageMapper"          , packageMapper );
        velocityContext4CommonSelectMapperTemplate.put( "commonSelectMapperName" , COMMON_SELECT_MAPPER_NAME);

        // templateとマージ
        StringWriter writer = new StringWriter();
        velocityCommonSelectMapperTemplate.merge(velocityContext4CommonSelectMapperTemplate, writer);


        String mapperSource = writer.toString();
        try {
            FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, COMMON_SELECT_MAPPER_NAME, mapperSource);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        System.out.print(mapperSource);

        return mapperSource;

   }

    /**
     * CommonMapperクラスの生成
     * @return
     */
    public static String generateCommonMapperSource() {
        PropertyHolder prop = SingletonManager.getPropertyHolder();

        String packageMapper = prop.getPackageMapper();

        velocitySetup();
        velocityContext4CommonMapperTemplate.put( "packageMapper"         , packageMapper );
        velocityContext4CommonMapperTemplate.put( "commonMapperName"      , COMMON_MAPPER_NAME);
        velocityContext4CommonMapperTemplate.put( "commonSelectMapperName", COMMON_SELECT_MAPPER_NAME);

        // templateとマージ
        StringWriter writer = new StringWriter();
        velocityCommonMapperTemplate.merge(velocityContext4CommonMapperTemplate, writer);

        String mapperSource = writer.toString();
        try {
            FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, COMMON_MAPPER_NAME, mapperSource);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        System.out.print(mapperSource);

        return mapperSource;

    }


    /**
     * 各テーブル用のMapperクラスを作成
     * @param tableInfo
     * @return
     */
    public static String generateMapperSource(TableInfoBean tableInfo) {
        PropertyHolder prop    = SingletonManager.getPropertyHolder();
        StringBuilder sb       = new StringBuilder();
        String tableName       = tableInfo.getTableName();
        String entityClassName = StringUtil._SplitStr2Camel(tableName,true);
        String MapperClassName = entityClassName + "Mapper";

        String packageMapper = prop.getPackageMapper();

        velocitySetup();
        velocityContext4EntityMapperTemplate.put( "packageMapper"    , packageMapper );
        velocityContext4EntityMapperTemplate.put( "entityClassFqdn"  , prop.getPackageEntity() + "." + entityClassName );
        velocityContext4EntityMapperTemplate.put( "mapperClassName"  , MapperClassName);
        velocityContext4EntityMapperTemplate.put( "commonMapperName" , CommonConstants.COMMON_MAPPER_NAME );
        velocityContext4EntityMapperTemplate.put( "entityClassName"  , entityClassName);

        StringWriter writer = new StringWriter();
        velocityEntityMapperTemplate.merge(velocityContext4EntityMapperTemplate, writer);
        String mapperSource = writer.toString();

        try {
            FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, MapperClassName, mapperSource);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        System.out.print( sb.toString() );

        return mapperSource;
    }

    /**
     * リストに設定されたカラム名で最大の長さを探しだす。
     */
    private static int calcMaxColNameLength(List<ColumnInfo> columnList){
        int maxLength = 0;
        for( ColumnInfo targetCol : columnList) {
            String targetColName = targetCol.getColumnName();
            if( maxLength < targetColName.length() ) {
                maxLength = targetColName.length();

            }

        }

        return maxLength;

    }


}
