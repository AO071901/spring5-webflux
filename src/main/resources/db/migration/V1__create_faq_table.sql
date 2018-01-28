CREATE TABLE IF NOT EXISTS spring.faq (
  faq_id serial primary key,
  title varchar(255),
  question text,
  answer text,
  version integer not null default 0,
  updated_time timestamp not null default current_timestamp,
  created_time timestamp not null default current_timestamp
);