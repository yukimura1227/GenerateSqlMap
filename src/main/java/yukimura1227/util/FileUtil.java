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
        String writeTargetDir = StringUtil.calcDirName(writeBaseDir, packageName);

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

    public static void writeXmlFile(final String writeBaseDir, String packageName, String mapperClassName, String writeContents) throws IOException {
        mkdir(writeBaseDir, packageName);
        String writeTargetJavaFilePath = calcSqlMapFileName(writeBaseDir, packageName, mapperClassName);

        // ファイル書き込み
        writeFile(writeTargetJavaFilePath,writeContents);

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
        String javaFileName = StringUtil.calcDirName(baseDir,packageName) + className + ".java";

        return javaFileName;
    }

    private static String calcSqlMapFileName(final String baseDir, final String packageName, final String fileNameBase) {
        String xmlFileName = StringUtil.calcDirName(baseDir,packageName) + fileNameBase + ".xml";

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
