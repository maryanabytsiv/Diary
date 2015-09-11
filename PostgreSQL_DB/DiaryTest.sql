DROP TABLE IF EXISTS tag_record;
DROP TABLE IF EXISTS record_list;
DROP TABLE IF EXISTS user_card;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS tag;

CREATE TABLE role (
    role_id varchar(40) PRIMARY KEY,
    name varchar(20) NOT NULL
);

CREATE TABLE  address (
    id varchar(40) PRIMARY KEY,
    country varchar(10),
    city varchar(20),
    street varchar(20),
    build_number int
);

CREATE TABLE user_card (
    uid varchar(40) PRIMARY KEY,
    nick_name varchar(20) NOT NULL,
    first_name varchar(20),
    second_name varchar(20),
	
    address_id int references address(id),
    e_mail varchar(30) NOT NULL,
    password varchar(20) NOT NULL,
    sex varchar(1),
    date_of_birth date,
    avatar varchar(50),
    role int references role(role_id) NOT NULL
);

CREATE TABLE record_list (
    uuid varchar(40) PRIMARY KEY,
    user_uuid int references user_card(u_id),
    created_time timestamp without time zone,
    text text,
    supplement varchar(50),
    visibility varchar(10) NOT NULL
 
);

CREATE TABLE tag (
    uuid varchar(40) PRIMARY KEY,
    tag varchar(20)
);

CREATE TABLE tag_record (
    uuid varchar(40) PRIMARY KEY,
    record_uuid int references record_list(uuid),
    tag_uuid int references tag(uuid)
);




