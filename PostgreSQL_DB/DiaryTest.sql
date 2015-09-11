DROP TABLE IF EXISTS tag_record;
DROP TABLE IF EXISTS record_list;
DROP TABLE IF EXISTS user_card;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS tag;

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    name character(20) NOT NULL
);
insert into role (name) values('Admin') ;
insert into role (name) values('User') ;

CREATE TABLE  address (
    id SERIAL PRIMARY KEY,
    country character(20) NOT NULL,
    city character(20) NOT NULL,
    street character(20) NOT NULL,
    build_number int NOT NULL
);
insert into address (country, city, street, build_number) values('Ukraine', 'Lviv', 'centre', 3) ;
insert into address (country, city, street, build_number) values('USA', 'NC', 'timesquare', 5) ;
insert into address (country, city, street, build_number) values('Poland', 'Warshav', 'Bog', 55) ;

CREATE TABLE user_card (
    u_id SERIAL PRIMARY KEY,
    nick_name character(20) NOT NULL,
    first_name character(20) NOT NULL,
    second_name character(20) NOT NULL,
	
    address_id int references address(id),
    e_mail character(30) NOT NULL,
    password character varying(20) NOT NULL,
    sex character(1) NOT NULL,
    date_of_birth date ,
    avatar character varying(50) ,
    role int references role(role_id) NOT NULL
);

insert into user_card (nick_name, first_name, second_name, address_id, e_mail, password, sex, date_of_birth, avatar, role) values('BigBunny', 'Oleg', 'Pavliv', 2, 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, 2);
insert into user_card (nick_name, first_name, second_name, address_id, e_mail, password, sex, date_of_birth, avatar, role) values('Sonic', 'Ira', 'Dub', 1, 'dfhfght@gmail.com', 'vfjukiuu', 'F', '1990-03-08', null, 1);
insert into user_card (nick_name, first_name, second_name, address_id, e_mail, password, sex, date_of_birth, avatar, role) values('TreeTree', 'Sergey', 'Gontar', 3, 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'M', '1989-02-20', null, 2);

CREATE TABLE record_list (
    u_u_id SERIAL  PRIMARY KEY,
    user_u_u_id int references user_card(u_id),
    created_time timestamp without time zone NOT NULL,
    text text NOT NULL,
    supplement character varying(50),
    visibility character(10) NOT NULL
 
);
insert into record_list (user_u_u_id, created_time, text, supplement, visibility) values(1,'2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'public');
insert into record_list (user_u_u_id, created_time, text, supplement, visibility) values(3,'2015-05-20 12:00:56','skjdhugfkdxufge',null,'private');
insert into record_list (user_u_u_id, created_time, text, supplement, visibility) values(1,'2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'public');


CREATE TABLE tag (
    u_u_id SERIAL PRIMARY KEY,
    tag character varying(20) NOT NULL
);

insert into tag (tag) values('#cars');
insert into tag (tag) values('#family');
insert into tag (tag) values('#Love');
insert into tag (tag) values('#murderrrrrr');

CREATE TABLE tag_record (
    u_u_id SERIAL PRIMARY KEY,
    record_uuid int references record_list(u_u_id) NOT NULL,
    tag_uuid int references tag(u_u_id) NOT NULL
);

insert into tag_record (record_uuid, tag_uuid) values(1, 2);
insert into tag_record (record_uuid, tag_uuid) values(3, 1);
insert into tag_record (record_uuid, tag_uuid) values(2, 3);





