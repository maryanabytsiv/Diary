
CREATE TABLE  address (
    id varchar(40) PRIMARY KEY,
    country varchar(20),
    city varchar(20),
    street varchar(20),
    build_number varchar(20)
);


CREATE TABLE user_card (
    uid varchar(40) PRIMARY KEY,
    nick_name varchar(20) NOT NULL,
    first_name varchar(20),
    second_name varchar(20),
    address_id varchar(40) references address(id),
    e_mail varchar(30) NOT NULL,
    password varchar(50) NOT NULL,
    sex varchar(6),
    date_of_birth date,
    avatar varchar(150),
    role varchar(6) NOT NULL,
    session varchar(40) 
);


CREATE TABLE record_list (
    id_rec varchar(40) PRIMARY KEY,
    user_id_rec varchar(40) references user_card(uid),
    created_time timestamp without time zone default current_timestamp,
    title varchar(60) default 'default title',
    text text,
    supplement varchar(50),
    visibility varchar(10) NOT NULL 
);


CREATE TABLE tag (
    uuid varchar(40) PRIMARY KEY,
    tag_message varchar(50)
);

CREATE TABLE tag_record (
    uuid_tr varchar(40) PRIMARY KEY,
    record_uuid varchar(40) references record_list(id_rec),
    tag_uuid varchar(40) references tag(uuid)
);