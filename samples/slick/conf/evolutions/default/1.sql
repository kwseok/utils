# -- Default schema

# --- !Ups

create table "cats" (
  "name" VARCHAR NOT NULL PRIMARY KEY,
  "color" VARCHAR NOT NULL,
  "flag" BOOLEAN DEFAULT true NOT NULL,
  "state" VARCHAR DEFAULT 'Normal' NOT NULL
);

insert into "cats" ("name", "color", "flag", "state") values ('a', 'red', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('b', 'orange', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('c', 'yellow', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('d', 'green', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('e', 'blue', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('f', 'dark blue', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('g', 'purple', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('h', 'red', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('i', 'orange', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('j', 'yellow', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('k', 'green', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('l', 'blue', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('m', 'dark blue', true, 'Normal');
insert into "cats" ("name", "color", "flag", "state") values ('n', 'purple', true, 'Normal');

create table "tasks" (
  "id" integer not null default nextval('tasks_id_seq'),
  "label" varchar
);

create sequence "tasks_id_seq";

insert into "tasks" ("label") values ('a');
insert into "tasks" ("label") values ('b');
insert into "tasks" ("label") values ('c');
insert into "tasks" ("label") values ('d');
insert into "tasks" ("label") values ('e');
insert into "tasks" ("label") values ('f');
insert into "tasks" ("label") values ('g');
insert into "tasks" ("label") values ('h');
insert into "tasks" ("label") values ('i');
insert into "tasks" ("label") values ('j');
insert into "tasks" ("label") values ('k');
insert into "tasks" ("label") values ('l');
insert into "tasks" ("label") values ('m');
insert into "tasks" ("label") values ('n');

create table "tests" (
  "a" varchar not null,
  "b" varchar not null,
  "c" varchar not null,
  "d" varchar not null,
  constraint tests_pkey primary key ("a", "b")
);

insert into "tests" ("a", "b", "c", "d") values ('a', 'a', 'a', 'a');
insert into "tests" ("a", "b", "c", "d") values ('b', 'b', 'b', 'b');
insert into "tests" ("a", "b", "c", "d") values ('c', 'c', 'c', 'c');
insert into "tests" ("a", "b", "c", "d") values ('d', 'd', 'd', 'd');
insert into "tests" ("a", "b", "c", "d") values ('e', 'e', 'e', 'e');
insert into "tests" ("a", "b", "c", "d") values ('f', 'f', 'f', 'f');
insert into "tests" ("a", "b", "c", "d") values ('g', 'g', 'g', 'g');
insert into "tests" ("a", "b", "c", "d") values ('h', 'h', 'h', 'h');
insert into "tests" ("a", "b", "c", "d") values ('i', 'i', 'i', 'i');
insert into "tests" ("a", "b", "c", "d") values ('j', 'j', 'j', 'j');
insert into "tests" ("a", "b", "c", "d") values ('k', 'k', 'k', 'k');

# --- !Downs

drop table "cats";
drop table "tasks";
drop sequence "tasks_id_seq";
drop table "tests";
