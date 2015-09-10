DROP TABLE tag_record;
DROP TABLE record_list;
DROP TABLE user_card;
DROP TABLE address;
DROP TABLE role;
DROP TABLE tag;

CREATE TABLE role (
    role_id int PRIMARY KEY,
    name character(20) NOT NULL
);

CREATE TABLE  address (
    id int PRIMARY KEY,
    country character(20) NOT NULL,
    city character(20) NOT NULL,
    street character(20) NOT NULL,
    build_number int NOT NULL
);
insert into address values(1, 'fff', 'dffgf', 'hjhj', 3) ;

CREATE TABLE user_card (
    u_id int PRIMARY KEY,
    nick_name character(20) NOT NULL,
    first_name character(20) NOT NULL,
    second_name character(20) NOT NULL,
	
    address_id int references address(id),
    e_mail character(30) NOT NULL,
    password character varying(20) NOT NULL,
    sex bit(1) NOT NULL,
    date_of_birth date NOT NULL,
    avatar character varying(50) NOT NULL,
    role int references role(role_id)
);


CREATE TABLE record_list (
    u_u_id int  PRIMARY KEY,
    user_u_u_id int references user_card(u_id),
    "timestamp" timestamp without time zone NOT NULL,
    text text NOT NULL,
    supplement character varying(50) NOT NULL,
    visibility bit(1) NOT NULL,
    rec_id character varying(10) NOT NULL
);


CREATE TABLE tag (
    u_u_id int PRIMARY KEY,
    tag character varying(20) NOT NULL
);

CREATE TABLE tag_record (
    u_u_id int PRIMARY KEY,
    record_uuid int references record_list(u_u_id),
    tag_uuid int references tag(u_u_id)
);





