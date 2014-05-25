package yukimura1227.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static yukimura1227.util.StringUtil._SplitStr2Camel;

import org.junit.Test;

public class StringUtilTest {

    public static class _SplitStr2CamelTest {
        @Test
        public void _SplitStr2Camelに空文字を渡して空文字となること() {
            String op1 = "";
            String expected = op1;

            String result = _SplitStr2Camel(op1,true);

            assertThat( result, is(expected) );
        }

        @Test
        public void _SplitStr2Camelにnullを渡してnullとなること() {
            String op1 = null;

            String result = _SplitStr2Camel(op1,true);

            assertThat(  result, is( nullValue() )  );
        }

        @Test
        public void _SplitStr2Camelにaとtrueを渡してAとなること() {
            String op1 = "a";
            String expected = "A";

            String result = _SplitStr2Camel(op1,true);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelにaとfalseを渡してaとなること() {
            String op1 = "a";
            String expected = "a";

            String result = _SplitStr2Camel(op1,false);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelに_とtrueを渡して_となること() {
            String op1 = "_";
            String expected = "_";

            String result = _SplitStr2Camel(op1,true);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelに_とfalseを渡して_となること() {
            String op1 = "_";
            String expected = "_";

            String result = _SplitStr2Camel(op1,false);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelにabc_defg_hijklとtrueを渡してAbcDefgHijklとなること() {
            String op1 = "abc_defg_hijkl";
            String expected = "AbcDefgHijkl";

            String result = _SplitStr2Camel(op1,true);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelにabc_defg_hijklとfalseを渡してabcDefgHijklとなること() {
            String op1 = "abc_defg_hijkl";
            String expected = "abcDefgHijkl";

            String result = _SplitStr2Camel(op1,false);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelに1abc_defg_hijklとtrueを渡して1abcDefgHijklとなること() {
            String op1 = "1abc_defg_hijkl";
            String expected = "1abcDefgHijkl";

            String result = _SplitStr2Camel(op1,true);

            assertThat( result, is( expected ) );
        }

        @Test
        public void _SplitStr2Camelに1abc_defg_hijklとfalseを渡して1abcDefgHijklとなること() {
            String op1 = "1abc_defg_hijkl";
            String expected = "1abcDefgHijkl";

            String result = _SplitStr2Camel(op1,false);

            assertThat( result, is( expected ) );
        }
    }
}
