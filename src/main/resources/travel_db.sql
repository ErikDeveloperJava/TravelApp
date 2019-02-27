create database travel_db character set utf8 collate utf8_general_ci;

use travel_db;

create table region(
  id int not null auto_increment primary key ,
  name_en varchar(100) not null ,
  name_ru varchar(100) not null ,
  name_arm varchar(100) not null
);

create table city(
  id int not null auto_increment primary key ,
  name_en varchar(100) not null ,
  name_ru varchar(100) not null ,
  name_arm varchar(100) not null ,
  region_id int,
  foreign key (region_id) references region(id) on delete cascade
);

create table contact(
  id int not null auto_increment primary key ,
  phone varchar(40) not null ,
  address varchar(255) not null ,
  region_id int ,
  city_id int,
  foreign key (region_id) references region(id) on delete cascade ,
  foreign key (city_id) references city(id) on delete cascade
);

create table user(
  id int not null auto_increment primary key ,
  name varchar(40) not null ,
  surname varchar(50) not null ,
  email varchar(100) not null ,
  password varchar(255) not null ,
  user_type enum('USER','ADMIN') not null ,
  img_url varchar(255),
  register_date datetime not null ,
  user_status enum('ACTIVE','INACTIVE') not null ,
  token varchar(255),
  contact_id int not null ,
  foreign key (contact_id) references contact(id) on delete cascade
);

create table place(
  id int not null auto_increment primary key ,
  name varchar(50) not null ,
  information text not null ,
  price double not null ,
  img_url varchar(255) not null ,
  contact_id int not null ,
  foreign key (contact_id) references contact(id) on delete cascade
);

create table place_image(
  id int not null auto_increment primary key ,
  img_url varchar(255) not null ,
  place_id int not null ,
  foreign key (place_id) references place(id) on delete cascade
);

create table hotel(
  id int not null auto_increment primary key ,
  name varchar(50) not null ,
  information text not null ,
  img_url varchar(255) not null ,
  place_id int ,
  contact_id int not null ,
  foreign key (place_id) references place(id) on delete cascade ,
  foreign key (contact_id) references contact(id) on delete cascade
);

create table hotel_room_attribute(
  id int not null auto_increment primary key ,
  room_count int not null ,
  wifi boolean not null ,
  air_conditioner boolean not null,
  tv boolean not null ,
  warm_water boolean not null
);

create table hotel_room(
  id int not null auto_increment primary key ,
  name varchar(50) not null ,
  information text not null ,
  price double not null ,
  img_url varchar(255),
  count int not null ,
  busied_count int,
  hotel_id int not null ,
  hotel_room_attribute_id int not null ,
  foreign key (hotel_id) references  hotel(id) on delete cascade ,
  foreign key (hotel_room_attribute_id) references  hotel_room_attribute(id) on delete cascade
);

create table hotel_image(
  id int not null auto_increment primary key ,
  img_url varchar(255) not null ,
  hotel_id int not null ,
  foreign key (hotel_id) references hotel(id) on delete cascade
);

create table hotel_room_image(
  id int not null auto_increment primary key ,
  img_url varchar(255) not null ,
  hotel_room_id int not null ,
  foreign key (hotel_room_id) references hotel_room(id) on delete cascade
);

create table wish_list(
  id int not null auto_increment primary key ,
  user_id int not null ,
  place_id int ,
  hotel_id int ,
  foreign key (user_id) references user(id) on delete cascade ,
  foreign key (place_id) references place(id) on delete cascade ,
  foreign key (hotel_id) references hotel(id) on delete cascade
);

create table user_order(
  id int not null auto_increment primary key ,
  when_date date not null ,
  how_many_days int not null ,
  price double not null ,
  adult_count int not null ,
  children_count int not null ,
  hotel_id int not null ,
  hotel_room_id int not null ,
  user_id int not null ,
  foreign key (hotel_id) references hotel(id) on delete cascade ,
  foreign key (hotel_room_id) references hotel_room(id) on delete cascade ,
  foreign key (user_id) references user(id) on delete cascade
);

create table review(
  id int not null auto_increment primary key ,
  message text not null ,
  send_date datetime not null ,
  rating int not null ,
  user_id int not null ,
  place_id int ,
  hotel_id int ,
  foreign key (user_id) references user(id) on delete cascade ,
  foreign key (place_id) references place(id) on delete cascade ,
  foreign key (hotel_id) references hotel(id) on delete cascade
);