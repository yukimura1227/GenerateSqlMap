-- テーブル定義
drop table IF EXISTS LGIN_USER;
create table LOGIN_USER(
  id varchar(128) DEFAULT '12345',
  password varchar(128),
  primary key (id)
   );

--
select * from LOGIN_USER;

insert into LOGIN_USER (id,password) VALUES ('hiro','hiro');

/* ログインユーザの取得 */
select * from LOGIN_USER;

