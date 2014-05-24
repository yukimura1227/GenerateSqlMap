-- テーブル定義
/*
create table STATUS_MST(
	status_id char(2),
	status_name varchar(50)
);
*/


select * from STATUS_MST;


insert into STATUS_MST (status_id,status_name) values ('00','未着手');
insert into STATUS_MST (status_id,status_name) values ('99','保留中');
insert into STATUS_MST (status_id,status_name) values ('01','読書中');
insert into STATUS_MST (status_id,status_name) values ('02','読了');
insert into STATUS_MST (status_id,status_name) values ('03','読書中２週目');
insert into STATUS_MST (status_id,status_name) values ('04','読了２週目');
insert into STATUS_MST (status_id,status_name) values ('98','気になる');

