--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: address; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE address (
    id character varying(10) NOT NULL,
    country character(20) NOT NULL,
    city character(20) NOT NULL,
    street character(20) NOT NULL,
    build_number integer NOT NULL
);


ALTER TABLE address OWNER TO postgres;

--
-- Name: records; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE record (
    u_u_id character varying(10) NOT NULL,
    user_u_u_id character varying(10) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    text text NOT NULL,
    supplement character varying(50) NOT NULL,
    visibility bit(1) NOT NULL,
    rec_id character varying(10) NOT NULL
);


ALTER TABLE record OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE role (
    role_id character varying(10) NOT NULL,
    name character(20) NOT NULL
);


ALTER TABLE role OWNER TO postgres;

--
-- Name: tag_record; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tag_record (
    u_u_id character varying(10) NOT NULL,
    record_uuid character varying(30) NOT NULL,
    tag_uuid character varying(10) NOT NULL
);


ALTER TABLE tag_record OWNER TO postgres;

--
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tag (
    u_u_id character varying(10) NOT NULL,
    tag character varying(20) NOT NULL
);


ALTER TABLE tag OWNER TO postgres;

--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "user" (
    u_id character varying(10) NOT NULL,
    nick_name character(20) NOT NULL,
    first_name character(20) NOT NULL,
    second_name character(20) NOT NULL,
    address_id character varying(10) NOT NULL,
    e_mail character(30) NOT NULL,
    password character varying(20) NOT NULL,
    sex bit(1) NOT NULL,
    date_of_birth date NOT NULL,
    avatar character varying(50) NOT NULL,
    role character varying(10) NOT NULL
);


ALTER TABLE "user" OWNER TO postgres;

--
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY address (id, country, city, street, build_number) FROM stdin;
rt47328574	USA                 	Chicago             	Gjorgia.st          	77
qw13234551	Germany             	Munchen             	SheftlarnShtrasse   	28
wr22342342	Ukraine             	Lviv                	Bandera.st          	29
nb38492840	Canada              	Ottava              	Klen.st             	84
tyor452589	Poland              	Krakiw              	PshePshePshe.st     	45
\.


--
-- Data for Name: records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY record (u_u_id, user_u_u_id, "timestamp", text, supplement, visibility, rec_id) FROM stdin;
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY role (role_id, name) FROM stdin;
\.


--
-- Data for Name: tag_record; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tag_record (u_u_id, record_uuid, tag_uuid) FROM stdin;
\.


--
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tag (u_u_id, tag) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY "user" (u_id, nick_name, first_name, second_name, address_id, e_mail, password, sex, date_of_birth, avatar, role) FROM stdin;
\.


--
-- Name: pk1_uuid; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tag_record
    ADD CONSTRAINT pk1_uuid PRIMARY KEY (u_u_id);


--
-- Name: pk_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY address
    ADD CONSTRAINT pk_id PRIMARY KEY (id);


--
-- Name: pk_rec_uuid; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY record
    ADD CONSTRAINT pk_rec_uuid PRIMARY KEY (u_u_id);


--
-- Name: pk_role_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT pk_role_id PRIMARY KEY (role_id);


--
-- Name: pk_u_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT pk_u_id PRIMARY KEY (u_id);


--
-- Name: pk_u_u_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tag
    ADD CONSTRAINT pk_u_u_id PRIMARY KEY (u_u_id);


--
-- Name: fk_address_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_address_id FOREIGN KEY (address_id) REFERENCES address(id);


--
-- Name: fk_rec_uuid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tag_record
    ADD CONSTRAINT fk_rec_uuid FOREIGN KEY (record_uuid) REFERENCES records(u_u_id);


--
-- Name: fk_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_role FOREIGN KEY (role) REFERENCES role(role_id);


--
-- Name: fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tag_record
    ADD CONSTRAINT fk_tag_uuid FOREIGN KEY (tag_uuid) REFERENCES tags(u_u_id);


--
-- Name: fk_user_u_u_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY record
    ADD CONSTRAINT fk_user_u_u_id FOREIGN KEY (user_u_u_id) REFERENCES "user"(u_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

