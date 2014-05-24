drop table IF EXISTS TASK_INFO;
-- テーブル定義
create table TASK_INFO(
    user_id                varchar(30)  NOT NULL,
    task_id                char(5)      NOT NULL,
    task_name              varchar(200) NOT NULL,
    task_contents          varchar(4000),
    remarks                varchar(4000),
    parent_task_id         char(5),
    root_task_id           char(5)      NOT NULL,
    task_sts               char(2),
    priority               char(1),
    yotei_start_date       date,
    yotei_end_date         date,
    start_date             date,
    end_date               date,
    child_task_num         integer,
    fin_child_task_num     integer,
    all_child_task_num     integer,
    all_fin_child_task_num integer,
	primary key (user_id,task_id)

);
