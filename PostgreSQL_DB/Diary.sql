
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    name character(20) NOT NULL
);


CREATE TABLE  address (
    id SERIAL PRIMARY KEY,
    country character(20) NOT NULL,
    city character(20) NOT NULL,
    street character(20) NOT NULL,
    build_number int NOT NULL
);


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


CREATE TABLE record_list (
    u_u_id SERIAL  PRIMARY KEY,
    user_u_u_id int references user_card(u_id),
    created_time timestamp without time zone NOT NULL,
    text text NOT NULL,
    supplement character varying(50),
    visibility character(10) NOT NULL
 
);


CREATE TABLE tag (
    u_u_id SERIAL PRIMARY KEY,
    tag character varying(20) NOT NULL
);


CREATE TABLE tag_record (
    u_u_id SERIAL PRIMARY KEY,
    record_uuid int references record_list(u_u_id) NOT NULL,
    tag_uuid int references tag(u_u_id) NOT NULL
);






