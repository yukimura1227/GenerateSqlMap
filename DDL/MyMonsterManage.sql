drop table IF EXISTS my_monster_manage;

-- テーブル定義
create table my_monster_manage(
  seq integer,
  id  integer,
  want_evolution_flag char(1) DEFAULT '1'
);
