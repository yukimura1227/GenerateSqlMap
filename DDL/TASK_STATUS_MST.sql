-- テーブル定義

create table TASK_STATUS_MST(
	status_id char(2),
	status_name varchar(50)
);



select * from TASK_STATUS_MST;


insert into TASK_STATUS_MST (status_id,status_name) values ('00','未着手');
insert into TASK_STATUS_MST (status_id,status_name) values ('99','保留中');
insert into TASK_STATUS_MST (status_id,status_name) values ('01','着手中');
insert into TASK_STATUS_MST (status_id,status_name) values ('02','完了');

