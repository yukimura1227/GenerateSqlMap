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
            // 一つ目の単語は小文字にして設定
            sb.append( wordArray[0].toLowerCase() );

        }

        // ２つ目以降の単語は、常に、１文字目を大文字化
        for( int i = 1; i < wordArray.length; i++ ) {
            sb.append( toUpperCaseAtFirstChar(wordArray[i]) );

        }

        return sb.toString();
    }

    /**
     * 1文字目を大文字に変換し、
     * 2文字目以降を小文字に変換する。
     * @param targetWord
     * @return
     */
    private static String toUpperCaseAtFirstChar(String targetWord) {
        return targetWord.substring(0,1).toUpperCase() + targetWord.substring(1).toLowerCase();
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



}
