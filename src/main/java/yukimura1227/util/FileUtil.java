package yukimura1227.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private FileUtil() {}

    public static void main(String[] args) {
        // 動作確認。
        System.out.println( mkdir("C:/temp/", "generate.sql.map.util") );
        System.out.println( mkdir("C:/temp/", "generate.sql.map.util") );
    }

    /**
     * 第一引数で与えられたディレクトリ配下に、第二引数で渡されたパッケージ名のフォルダを作成する。
     * 例：op1=c:/ op2=foo.bar.buz→c:/foo/bar/buzが作成される。
     * @param wirteBaseDir ベースディレクトリ
     * @param packageName パッケージ名（foo.bar形式）。
     * @return フォルダを作成したらtrue。既にフォルダが存在するなどで、作成しなかったらfalse
     */
    public static boolean mkdir(final String writeBaseDir, String packageName) {
        String writeTargetDir = calcDirName(writeBaseDir, packageName);

        File dirDigger = new File(writeTargetDir);

        return dirDigger.mkdirs();
    }

    /**
     *
     * @param writeBaseDir
     * @param packageName
     * @param className
     * @param writeContents
     * @throws IOException
     */
    public static void writeJavaFile(final String writeBaseDir, String packageName, String className, String writeContents) throws IOException {
        mkdir(writeBaseDir, packageName);
        String writeTargetJavaFilePath = buildJavaFileFullPath(writeBaseDir, packageName, className);

        // ファイル書き込み
        writeFile(writeTargetJavaFilePath,writeContents);

    }

    public static void writeXmlFile(final String writeBaseDir, String packageName, String fileNameBase, String writeContents) throws IOException {
        mkdir(writeBaseDir, packageName);
        String writeTargetJavaFilePath = calcSqlMapFileName(writeBaseDir, packageName, fileNameBase);

        // ファイル書き込み
        writeFile(writeTargetJavaFilePath,writeContents);

    }

    /**
     * パッケージ名から想定されるディレクトリ階層を判断し
     * 「baseDir/ディレクトリ階層」という値を組み立てる。
     * ex)
     *   baseDir="/var/tmp" packageName="jp.co.xxx.yyy"
     *   -> return "/var/tmp/jp/co/xxx/yyy/"
     * @param baseDir
     * @param packageName
     * @return
     */
    private static String calcDirName(final String baseDir, String packageName) {
        String[] packageToken = packageName.split("\\.");

        // ファイルセパレータを"/"に統一
        String targetDir = baseDir.replaceAll("\\\\", "/");

        // baseDirがファイルセパレータで終わっていない場合
        if( !targetDir.endsWith("/") ) {
            targetDir += "/";
        }

        for( String targetPackage : packageToken ) {
            targetDir += targetPackage + "/";
        }

        return targetDir;
    }

    /**
     * javaのパッケージ名とクラス名から、.javaファイルのフルパスを組み立てて
     * 返却する。
     * @param baseDir
     * @param packageName
     * @param className
     * @return
     */
    private static String buildJavaFileFullPath(final String baseDir, final String packageName, final String className) {
        String javaFileName = calcDirName(baseDir,packageName) + className + ".java";

        return javaFileName;
    }

    private static String calcSqlMapFileName(final String baseDir, final String packageName, final String fileNameBase) {
        String xmlFileName = calcDirName(baseDir,packageName) + fileNameBase + ".xml";

        return xmlFileName;
    }

    private static void writeFile(final String targetFileFullPath, String writeContents) throws IOException {
        File writeTargetFile = new File(targetFileFullPath);
        try (
              FileWriter writer = new FileWriter(writeTargetFile);
         ) {
            writer.write(writeContents);
        }
    }

}
