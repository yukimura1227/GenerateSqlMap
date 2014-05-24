drop table IF EXISTS TAG_MST;

-- テーブル定義
create table TAG_MST(
    tag_id  char(128)    NOT NULL,
    tag_name varchar(100),
	primary key (tag_id)
);
