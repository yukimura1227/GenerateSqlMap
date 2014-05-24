package yukimura1227.generate.sql.map.main;

import static yukimura1227.generate.sql.map.util.CommonConstants.CR;
import static yukimura1227.generate.sql.map.util.CommonConstants.CRLF;
import static yukimura1227.generate.sql.map.util.CommonConstants.LF;
import static yukimura1227.generate.sql.map.util.CommonConstants.SPACE;
import static yukimura1227.generate.sql.map.util.CommonConstants.TAB;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import yukimura1227.generate.sql.map.bean.ColumnInfo;
import yukimura1227.generate.sql.map.bean.TableInfoBean;
import yukimura1227.generate.sql.map.holder.PropertyHolder;
import yukimura1227.generate.sql.map.holder.SingletonManager;
import yukimura1227.generate.sql.map.util.EntityGenerater;
import yukimura1227.generate.sql.map.util.SQLMapGenerater;

/**
 *
 * @author yukimura1227
 *
 */
public class AutoGenerateMyBatisSource {

    private static PropertyHolder prop;
    /**
     * エントリーポイント
     * @param args
     * @throws IOException
     */
    @Deprecated
    public static void main(String[] args) throws IOException {
        // 初期処理実施
        init();

        File inputFolder  = new File( prop.getInputPath() );

        File[] targetFiles = inputFolder.listFiles();
        for( File targetFile : targetFiles ) {
            // ディレクトリは処理しない
            if( targetFile.isDirectory() ) {
                continue;
            }

            generateSqlMap(targetFile);

        }
    }

    /**
     * 実行を行う上で必要な設定を行う。
     */
    public static void init() {
        // Springの設定ファイルの読み込み
        ApplicationContext context = new ClassPathXmlApplicationContext("sample/config/applicationContext.xml");
        context.getBean(SingletonManager.class);
        prop = SingletonManager.getPropertyHolder();
    }

    /**
     * ファイルからcreate文の部分を抽出して、返却する。
     * 内部でファイルのOpen,Closeを行う。
     * 前提条件として、create分のdelimiterは「;」を想定。
     * @param targetDdlFile
     * @return
     * @throws IOException
     */
    private static StringBuilder extractCreateSentence(File targetDdlFile) throws IOException {
        StringBuilder  sb = new StringBuilder();
        try (
            FileReader     fr = new FileReader(targetDdlFile);
            BufferedReader br = new BufferedReader(fr);
        ) {

            // create文の部分を操作中であることを判定するフラグ
            boolean createStartFlg = false;

            String line = "";
            while(  ( line = br.readLine() ) != null  ) {

                // createという文を見つけたら,sbに詰めるように設定
                if( line.toLowerCase().contains("create") ) {
                    createStartFlg = true;
                }

                // sbに各行を詰める
                if( createStartFlg ) {
                    sb.append( collectUpSpace(line) );
                }

                // ;で、sbに詰める設定を解除
                if( line.contains(";") ) {
                    createStartFlg = false;
                }

            }
        }

        return sb;

    }

    /**
     * create文を渡すと、「tableName columnName1 columnType1 colName2 colTYpe2...」の文字列を返却する。
     * @param createSentence
     * @return
     */
    private static String extractTableNameAndColumnInfo(String createSentence) {
        String temp = null;

        // create文の改行文字を除去し、小文字で統一
        temp = createSentence.replaceAll(CR, "");
        temp = temp.replaceAll(LF, "");
        temp = temp.toLowerCase();

        // (とそれに続く「 」（半角スペース）の除去
        temp = temp.replaceAll("\\([ ]+", " ( ");
        // )とそれの前に入っている「 」（半角スペース）の除去
        temp = temp.replaceAll("[ ]*\\);", " );");
        // 「(数値)」の部分を除去
        temp = temp.replaceAll("\\([0-9 ]+\\)", "");
        // プライマリーキーの部分を除去 // TODO これあってるか確認
        temp = temp.replaceAll("primary key \\([ ]*[a-z,_0-9]+[ ]*\\)", "");
        // 「not null」または[null」を削除
        temp = temp.replaceAll("[not ]*null", "");
        // デフォルト値の部分を削除
        temp = temp.replaceAll("default ['a-z0-9_]+", "");
        // create構文を除去
        temp = temp.replaceAll("create[\\s]+[temporary]*[\\s]*table[\\s]*","");
        // if not exsistsの除去 // TODO スペースの個数違いを拾えるようにしておく。
        temp = temp.replace("if not exsists ","");
        temp = temp.replace("(", "");
        temp = temp.replace(")", "");
        temp = temp.replace(";", "");
        temp = temp.replace(",", "");
        temp = temp.replaceAll("[\\s]+", " ");
        temp = temp.replaceAll("^ ", "");
        // 改行コードの統一
        temp = temp.replace(CRLF, LF);
        temp = temp.replace(CR, LF);
        System.out.println( temp );

        return temp;
    }

    // 一つのddlファイルに対する処理。
    private static void generateSqlMap(File targetDdlFile) throws IOException {
        // ファイルのcreate文の部分を抽出
        StringBuilder sb = extractCreateSentence(targetDdlFile);

        // create文を必要な情報のみ残して加工
        String tableNameAndColumnInfoStr = extractTableNameAndColumnInfo( sb.toString() );

        // テーブル情報保持用のリスト
        List<TableInfoBean> tableInfoList = new ArrayList<TableInfoBean>();

        // TODO 一つのファイルに複数のcreate文があった場合は、extractTableNameAndColumnInfoは改行区切りになる？？？
        String[] lineArray = tableNameAndColumnInfoStr.split(LF);
        for( String targetLine : lineArray ) {

            // 空白で分割
            String[] tokenArray = targetLine.split(SPACE);

            // テーブル名を取得（加工した文字列の" "区切りの１番目がテーブル名）
            String tableName = tokenArray[0];
            TableInfoBean tableInfoBean= new TableInfoBean(tableName);

            ColumnInfo columnInfo = null;
            for(int i = 1; i < tokenArray.length; i++) {
                String token = tokenArray[i];
                // TODO カラム名等の詰め物
                // カラム名 カラム型・・・という入り方のため、[i%2 == 1]するとカラム名
                if( i % 2 == 1 ) {
                    columnInfo = new ColumnInfo("", "");
                    columnInfo.setColumnName(token);
                    columnInfo.setJavaType("String");
                } else {
                    columnInfo.setColumnType(token);
                    tableInfoBean.addColumn(columnInfo);
                }
            }

            tableInfoList.add(tableInfoBean);

            EntityGenerater.generateEntityClassStr(tableInfoBean);

            SQLMapGenerater.generateSQLMapStr(tableInfoBean);

            SQLMapGenerater.generateMapperSource(tableInfoBean);

        }

        SQLMapGenerater.generateCommonSelectMapperSource();

        SQLMapGenerater.generateCommonMapperSource();

    }


    /**
     * \tや\sの塊を一つの\sにまとめあげる。（Stringでがちゃがちゃやっているので効率が悪い）
     * @param target
     * @return
     */
    private static String collectUpSpace(String target) {
        String collectUpSpaceStr = target;
        collectUpSpaceStr = collectUpSpaceStr.replaceAll(TAB   , SPACE);
        collectUpSpaceStr = collectUpSpaceStr.replaceAll("    ", SPACE);
        collectUpSpaceStr = collectUpSpaceStr.replaceAll("  "  , SPACE);
        return collectUpSpaceStr;
    }
}

