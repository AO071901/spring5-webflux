create table faqs (
  id serial primary key,
  title varchar(255) not null,
  content varchar(255) not null,
  version integer not null default 0,
  updated_time timestamp not null default current_timestamp,
  created_time timestamp not null default current_timestamp
);