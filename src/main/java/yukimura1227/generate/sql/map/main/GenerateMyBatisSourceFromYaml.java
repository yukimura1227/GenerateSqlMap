package yukimura1227.generate.sql.map.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.yaml.snakeyaml.Yaml;

import yukimura1227.generate.sql.map.bean.TableInfoBean;
import yukimura1227.generate.sql.map.holder.SingletonManager;
import yukimura1227.generate.sql.map.util.EntityGenerater;
import yukimura1227.generate.sql.map.util.SQLMapGenerater;

/**
 *
 * @author yukimura1227
 *
 */
public class GenerateMyBatisSourceFromYaml {

    /**
     * エントリーポイント
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 初期処理実施
        init();

        String inputYamlPath = SingletonManager.getPropertyHolder().getInputYamlPath();

        File inputFolder  = new File(inputYamlPath);

        File[] targetFiles = inputFolder.listFiles();
        for( File targetFile : targetFiles ) {
            // ディレクトリは処理しない
            if( targetFile.isDirectory() ) {
                continue;
            }

            generateSqlMap(targetFile);

        }

      SQLMapGenerater.generateCommonSelectMapperSource();

      SQLMapGenerater.generateCommonMapperSource();


    }

    /**
     * 実行を行う上で必要な設定を行う。
     */
    public static void init() {
        // Springの設定ファイルの読み込み
        ApplicationContext context = new ClassPathXmlApplicationContext("sample/config/applicationContext.xml");
        context.getBean(SingletonManager.class);
    }

    /*
     *  一つのddlファイルに対する処理。
     */
    private static void generateSqlMap(File taragetTableDefYaml) throws IOException {

        TableInfoBean tableInfoBean =null;
        Yaml yaml = new Yaml();
        tableInfoBean = (TableInfoBean) yaml.load(new FileInputStream(taragetTableDefYaml) );

        EntityGenerater.generateEntityClassStr(tableInfoBean);

        SQLMapGenerater.generateSQLMapStr(tableInfoBean);

        SQLMapGenerater.generateMapperSource(tableInfoBean);

        SQLMapGenerater.generateMyBatisConfig();

    }

}

