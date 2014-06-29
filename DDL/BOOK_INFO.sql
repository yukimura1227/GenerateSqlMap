-- テーブル定義
create table BOOK_INFO(
    isbn varchar(30),
    title varchar(256),
    author varchar(256),
    price INTEGER,
    publish varchar(256),
    published_date DATETIME
);

--
select * from BOOK_INFO;

-- 以下は、mySQL用（sysdate()を利用しているので）
-- INSERT INTO BOOK_INFO (isbn,title,author,price,publish,published_date) VALUES ('99999999','タイトル','著者',100,'なんとか出版',sysdate());

/* ログインユーザの取得 */
select * from BOOK_INFO;