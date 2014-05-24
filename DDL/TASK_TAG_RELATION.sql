drop table IF EXISTS TASK_TAG_RELATION;
-- テーブル定義
create table TASK_TAG_RELATION(
    user_id varchar(30) NOT NULL,
    task_id char(5)     NOT NULL,
    tag_id  char(32)    NOT NULL
);
