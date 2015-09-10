DROP TABLE IF EXISTS tag_record;
DROP TABLE IF EXISTS record_list;
DROP TABLE IF EXISTS user_card;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS tag;

CREATE TABLE role (
    role_id int PRIMARY KEY,
    name character(20) NOT NULL
);
insert into role values(1, 'Admin') ;
insert into role values(2, 'User') ;

CREATE TABLE  address (
    id int PRIMARY KEY,
    country character(20) NOT NULL,
    city character(20) NOT NULL,
    street character(20) NOT NULL,
    build_number int NOT NULL
);
insert into address values(1, 'Ukraine', 'Lviv', 'centre', 3) ;
insert into address values(2, 'USA', 'NC', 'timesquare', 5) ;
insert into address values(3, 'Poland', 'Warshav', 'Bog', 55) ;

CREATE TABLE user_card (
    u_id int PRIMARY KEY,
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

insert into user_card values(1, 'BigBunny', 'Oleg', 'Pavliv', 2, 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, 2);
insert into user_card values(2, 'Sonic', 'Ira', 'Dub', 1, 'dfhfght@gmail.com', 'vfjukiuu', 'F', '1990-03-08', null, 1);
insert into user_card values(3, 'TreeTree', 'Sergey', 'Gontar', 3, 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'M', '1989-02-20', null, 2);

CREATE TABLE record_list (
    u_u_id int  PRIMARY KEY,
    user_u_u_id int references user_card(u_id),
    "timestamp" timestamp without time zone NOT NULL,
    text text NOT NULL,
    supplement character varying(50),
    visibility character(10) NOT NULL
 
);
insert into record_list values(1,1,'2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'public');
insert into record_list values(2,3,'2015-05-20 12:00:56','skjdhugfkdxufge',null,'private');
insert into record_list values(3,1,'2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'public');


CREATE TABLE tag (
    u_u_id int PRIMARY KEY,
    tag character varying(20) NOT NULL
);

insert into tag values(1, 'cars');
insert into tag values(2, 'family');
insert into tag values(3, 'Love');
insert into tag values(4, 'murderrrrrr');

CREATE TABLE tag_record (
    u_u_id int PRIMARY KEY,
    record_uuid int references record_list(u_u_id) NOT NULL,
    tag_uuid int references tag(u_u_id) NOT NULL
);

insert into tag_record values(1, 1, 2);
insert into tag_record values(2, 3, 1);
insert into tag_record values(3, 2, 3);





