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

    private static VelocityEngine engine = new VelocityEngine();

    private SQLMapGenerater() {}

    static {
        // 基本的にエンドユーザが変更する必要が無いためpropertiesのパスは固定でOK
        Properties velocityProperties = new Properties();
        try
        (
            InputStream is = EntityGenerater.class.getResourceAsStream("/sample/config/velocity.properties");
        )
        {
            velocityProperties.load(is);

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();

        }

        engine.init(velocityProperties);

    }

    /**
     * テーブル情報を基に、テーブル情報を保持するためのSQLMapper用の文字列を生成
     * @param tableInfo
     * @throws IOException
     */
    public static String generateSQLMapStr(TableInfoBean tableInfo) throws IOException {
        PropertyHolder prop         = SingletonManager.getPropertyHolder();
        String tableName            = tableInfo.getTableName();
        String entityClassName      = StringUtil._SplitStr2Camel(tableName, /* 1文字目を大文字にする */ true);
        String mapperClassName      = entityClassName + "Mapper";
        List<ColumnInfo> columnList = tableInfo.getColumnList();
        String mapperPackage        = prop.getPackageMapper();
        String outputPath           = prop.getOutputPath();
        String packageBase          = prop.getPackageBase();

        String mapplerXmlTemplatePath            = prop.getMapplerXmlTemplatePath();
        VelocityContext velocityContext4MapperXmlTemplate = new VelocityContext();

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

        velocityContext4MapperXmlTemplate.put( "packageMapper"   , mapperPackage );
        velocityContext4MapperXmlTemplate.put( "entityClassName" , entityClassName);
        velocityContext4MapperXmlTemplate.put( "mapperClassName" , mapperClassName);
        velocityContext4MapperXmlTemplate.put( "tableInfo"       , tableInfo);
        velocityContext4MapperXmlTemplate.put( "resultPartList"  , resultPartList);
        velocityContext4MapperXmlTemplate.put( "wherePartList"   , wherePartList);
        velocityContext4MapperXmlTemplate.put( "setPartList"     , setPartList);

        // templateとマージ
        String sqlMapXml = templateMerge(mapplerXmlTemplatePath, velocityContext4MapperXmlTemplate);

        FileUtil.writeXmlFile(outputPath , packageBase, mapperClassName, sqlMapXml);

        System.out.println(sqlMapXml);

        return sqlMapXml;
    }

    /**
     * CommonSelectMapperクラスの生成
     * @return
     * @throws IOException
     */
    public static String generateCommonSelectMapperSource() throws IOException {
        PropertyHolder prop  = SingletonManager.getPropertyHolder();

        String packageMapper = prop.getPackageMapper();

        String selectCommonMapperXmlTemplatePath = prop.getCommonSelectMapperXmlTemplate();
        VelocityContext velocityContext4CommonSelectMapperTemplate = new VelocityContext();
        velocityContext4CommonSelectMapperTemplate.put( "packageMapper"          , packageMapper );
        velocityContext4CommonSelectMapperTemplate.put( "commonSelectMapperName" , COMMON_SELECT_MAPPER_NAME);

        // templateとマージ
        String mapperSource = templateMerge(selectCommonMapperXmlTemplatePath, velocityContext4CommonSelectMapperTemplate);
        FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, COMMON_SELECT_MAPPER_NAME, mapperSource);

        System.out.print(mapperSource);

        return mapperSource;

   }

    /**
     * CommonMapperクラスの生成
     * @return
     * @throws IOException
     */
    public static String generateCommonMapperSource() throws IOException {
        PropertyHolder prop = SingletonManager.getPropertyHolder();

        String packageMapper = prop.getPackageMapper();

        String commonMapperXmlTemplatePath       = prop.getCommonXmlMaplerTemplate();
        VelocityContext velocityContext4CommonMapperTemplate = new VelocityContext();
        velocityContext4CommonMapperTemplate.put( "packageMapper"         , packageMapper );
        velocityContext4CommonMapperTemplate.put( "commonMapperName"      , COMMON_MAPPER_NAME);
        velocityContext4CommonMapperTemplate.put( "commonSelectMapperName", COMMON_SELECT_MAPPER_NAME);

        // templateとマージ
        String mapperSource = templateMerge(commonMapperXmlTemplatePath, velocityContext4CommonMapperTemplate);
        FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, COMMON_MAPPER_NAME, mapperSource);

        System.out.print(mapperSource);

        return mapperSource;

    }


    /**
     * 各テーブル用のMapperクラスを作成
     * @param tableInfo
     * @return
     * @throws IOException
     */
    public static String generateMapperSource(TableInfoBean tableInfo) throws IOException {
        PropertyHolder prop    = SingletonManager.getPropertyHolder();
        StringBuilder sb       = new StringBuilder();
        String tableName       = tableInfo.getTableName();
        String entityClassName = StringUtil._SplitStr2Camel(tableName,true);
        String MapperClassName = entityClassName + "Mapper";

        String packageMapper = prop.getPackageMapper();

        String entityInterfaceTemplatePath = prop.getEntityInterfaceTemplatePath();
        VelocityContext velocityContext4EntityMapperTemplate = new VelocityContext();
        velocityContext4EntityMapperTemplate.put( "packageMapper"    , packageMapper );
        velocityContext4EntityMapperTemplate.put( "entityClassFqdn"  , prop.getPackageEntity() + "." + entityClassName );
        velocityContext4EntityMapperTemplate.put( "mapperClassName"  , MapperClassName);
        velocityContext4EntityMapperTemplate.put( "commonMapperName" , CommonConstants.COMMON_MAPPER_NAME );
        velocityContext4EntityMapperTemplate.put( "entityClassName"  , entityClassName);

        String mapperSource = templateMerge(entityInterfaceTemplatePath, velocityContext4EntityMapperTemplate);
        FileUtil.writeJavaFile(prop.getOutputPath(), packageMapper, MapperClassName, mapperSource);

        System.out.print( sb.toString() );

        return mapperSource;
    }

    /**
     * velocityTemplateとveclotiyContextのマージを行い、結果の文字列を返却する。
     * @param template
     * @param velocityContext
     * @return
     */
    private static String templateMerge(String templatePath, VelocityContext velocityContext) {
        // TODO engineからtemplateを取得する処理もここにまとめるようにする。
        Template template = engine.getTemplate(templatePath);
        String mergedString = "";
        try
        (
            StringWriter writer = new StringWriter();
        )
        {
            template.merge(velocityContext, writer);
            mergedString = writer.toString();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        return mergedString;

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
