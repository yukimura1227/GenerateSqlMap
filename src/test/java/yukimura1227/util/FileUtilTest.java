package yukimura1227.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static yukimura1227.util.FileUtil.calcDirName;

import org.junit.Test;

public class FileUtilTest {


    public static class calcDirNameTest {
        @Test
        public void calcDirName第一引数がスラッシュで終わらない場合() {
            String baseDir = "/var/tmp";
            String packageName = "jp.co.xxx.yyy";
            String expected = "/var/tmp/jp/co/xxx/yyy/";

            String result = calcDirName(baseDir,packageName);

            assertThat( result, is(expected) );

        }
        @Test
        public void calcDirName第一引数がスラッシュで終わる場合() {
            String baseDir = "/var/tmp/";
            String packageName = "jp.co.xxx.yyy";
            String expected = "/var/tmp/jp/co/xxx/yyy/";

            String result = calcDirName(baseDir,packageName);

            assertThat( result, is(expected) );

        }
        @Test
        public void calcDirName第一引数が円マークで終わらない場合() {
            String baseDir = "c:\\var\\tmp";
            String packageName = "jp.co.xxx.yyy";
            String expected = "c:/var/tmp/jp/co/xxx/yyy/";

            String result = calcDirName(baseDir,packageName);

            assertThat( result, is(expected) );

        }
        @Test
        public void calcDirName第一引数が円マークで終わる場合() {
            String baseDir = "c:\\var\\tmp\\";
            String packageName = "jp.co.xxx.yyy";
            String expected = "c:/var/tmp/jp/co/xxx/yyy/";

            String result = calcDirName(baseDir,packageName);

            assertThat( result, is(expected) );

        }
    }
}
