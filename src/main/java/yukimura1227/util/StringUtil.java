package yukimura1227.util;

public class StringUtil {
    /**
     * "_"で繋げられた文字列をCamelCase形式に変更する。
     * @param targetString
     * @param initialUpperFlg 最初の文字を大文字化するかどうか？
     * @return
     */
    public static String _SplitStr2Camel( String targetString, boolean initialUpperFlg){
        // 変換できない文字列は、そのまま返却する。
        if( targetString == null || "".equals(targetString) || "_".equals(targetString) ) {
            return targetString;
        }
        // "_"で分割
        String[] wordArray = targetString.split("_");

        StringBuilder sb = new StringBuilder("");

        // 先頭文字の大文字化を行う場合
        if( initialUpperFlg ) {
            // 一つ目の単語の１文字目を大文字化
            sb.append( toUpperCaseAtFirstChar(wordArray[0]) );

        // 上記以外の場合
        } else {
            // 一つ目の単語はそのまま設定
            sb.append(wordArray[0]);

        }

        // ２つ目以降の単語は、常に、１文字目を大文字化
        for( int i = 1; i < wordArray.length; i++ ) {
            sb.append( toUpperCaseAtFirstChar(wordArray[i]) );

        }

        return sb.toString();
    }

    private static String toUpperCaseAtFirstChar(String targetWord) {
        return targetWord.substring(0,1).toUpperCase() + targetWord.substring(1);
    }

    /**
     * targetStringをdressStrで囲んだ文字列を返却する
     * @param targetString
     * @param dressStr
     * @return
     */
    public static String embrace(String targetString, String dressStr) {
        return dressStr + targetString + dressStr;
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
    public static String calcDirName(final String baseDir, String packageName) {
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
    public static String buildJavaFileFullPath(final String baseDir, final String packageName, final String className) {
        String javaFileName = calcDirName(baseDir,packageName) + className + ".java";

        return javaFileName;
    }

    public static String calcSqlMapFileName(final String baseDir, final String packageName, final String fileNameBase) {
        String xmlFileName = calcDirName(baseDir,packageName) + fileNameBase + ".xml";

        return xmlFileName;
    }

}
