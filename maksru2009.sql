--
-- PostgreSQL database dump
--

-- Dumped from database version 11.21
-- Dumped by pg_dump version 15.3

-- Started on 2023-09-03 18:02:02

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2923 (class 1262 OID 17411)
-- Name: maksru2009; Type: DATABASE; Schema: -; Owner: admin
--

CREATE DATABASE maksru2009 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE maksru2009 OWNER TO admin;

\connect maksru2009

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 17412)
-- Name: banktest; Type: SCHEMA; Schema: -; Owner: admin
--

CREATE SCHEMA banktest;


ALTER SCHEMA banktest OWNER TO admin;

--
-- TOC entry 2924 (class 0 OID 0)
-- Dependencies: 7
-- Name: SCHEMA banktest; Type: COMMENT; Schema: -; Owner: admin
--

COMMENT ON SCHEMA banktest IS 'Created schema bankTest!';


--
-- TOC entry 8 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 598 (class 1247 OID 17414)
-- Name: mod; Type: TYPE; Schema: public; Owner: admin
--

CREATE TYPE public.mod AS ENUM (
    'OUTGOING',
    'INCOMING',
    'CASH',
    'ADDING'
);


ALTER TYPE public.mod OWNER TO admin;

SET default_tablespace = '';

--
-- TOC entry 197 (class 1259 OID 17423)
-- Name: account; Type: TABLE; Schema: banktest; Owner: admin
--

CREATE TABLE banktest.account (
    id bigint NOT NULL,
    accountnumber character varying(30) NOT NULL,
    amount double precision NOT NULL,
    bank_id integer,
    user_id integer,
    dateopen timestamp without time zone NOT NULL,
    CONSTRAINT account_accountnumber_check CHECK (((accountnumber)::text <> ''::text))
);


ALTER TABLE banktest.account OWNER TO admin;

--
-- TOC entry 198 (class 1259 OID 17427)
-- Name: account_id_seq; Type: SEQUENCE; Schema: banktest; Owner: admin
--

CREATE SEQUENCE banktest.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banktest.account_id_seq OWNER TO admin;

--
-- TOC entry 2926 (class 0 OID 0)
-- Dependencies: 198
-- Name: account_id_seq; Type: SEQUENCE OWNED BY; Schema: banktest; Owner: admin
--

ALTER SEQUENCE banktest.account_id_seq OWNED BY banktest.account.id;


--
-- TOC entry 199 (class 1259 OID 17429)
-- Name: bank; Type: TABLE; Schema: banktest; Owner: admin
--

CREATE TABLE banktest.bank (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    CONSTRAINT bank_name_check CHECK (((name)::text <> ''::text))
);


ALTER TABLE banktest.bank OWNER TO admin;

--
-- TOC entry 200 (class 1259 OID 17433)
-- Name: bank_id_seq; Type: SEQUENCE; Schema: banktest; Owner: admin
--

CREATE SEQUENCE banktest.bank_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banktest.bank_id_seq OWNER TO admin;

--
-- TOC entry 2927 (class 0 OID 0)
-- Dependencies: 200
-- Name: bank_id_seq; Type: SEQUENCE OWNED BY; Schema: banktest; Owner: admin
--

ALTER SEQUENCE banktest.bank_id_seq OWNED BY banktest.bank.id;


--
-- TOC entry 201 (class 1259 OID 17435)
-- Name: bank_user; Type: TABLE; Schema: banktest; Owner: admin
--

CREATE TABLE banktest.bank_user (
    bank_id integer,
    user_id integer
);


ALTER TABLE banktest.bank_user OWNER TO admin;

--
-- TOC entry 202 (class 1259 OID 17438)
-- Name: transaction; Type: TABLE; Schema: banktest; Owner: admin
--

CREATE TABLE banktest.transaction (
    id bigint NOT NULL,
    type public.mod NOT NULL,
    sendingbank_id integer,
    beneficiarybank_id integer,
    sendinguser_id integer,
    beneficiaryuser_id integer,
    sendingaccount_id integer,
    beneficiaryaccount_id integer,
    amount double precision NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    account_id integer
);


ALTER TABLE banktest.transaction OWNER TO admin;

--
-- TOC entry 203 (class 1259 OID 17441)
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: banktest; Owner: admin
--

CREATE SEQUENCE banktest.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banktest.transaction_id_seq OWNER TO admin;

--
-- TOC entry 2928 (class 0 OID 0)
-- Dependencies: 203
-- Name: transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: banktest; Owner: admin
--

ALTER SEQUENCE banktest.transaction_id_seq OWNED BY banktest.transaction.id;


--
-- TOC entry 204 (class 1259 OID 17443)
-- Name: user; Type: TABLE; Schema: banktest; Owner: admin
--

CREATE TABLE banktest."user" (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    lastname character varying(30) NOT NULL,
    secondname character varying(30) NOT NULL,
    phonenumber character varying(30) NOT NULL,
    CONSTRAINT user_lastname_check CHECK (((lastname)::text <> ''::text)),
    CONSTRAINT user_name_check CHECK (((name)::text <> ''::text)),
    CONSTRAINT user_phonenumber_check CHECK (((phonenumber)::text <> ''::text)),
    CONSTRAINT user_secondname_check CHECK (((secondname)::text <> ''::text))
);


ALTER TABLE banktest."user" OWNER TO admin;

--
-- TOC entry 205 (class 1259 OID 17450)
-- Name: user_id_seq; Type: SEQUENCE; Schema: banktest; Owner: admin
--

CREATE SEQUENCE banktest.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banktest.user_id_seq OWNER TO admin;

--
-- TOC entry 2929 (class 0 OID 0)
-- Dependencies: 205
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: banktest; Owner: admin
--

ALTER SEQUENCE banktest.user_id_seq OWNED BY banktest."user".id;


--
-- TOC entry 2759 (class 2604 OID 17452)
-- Name: account id; Type: DEFAULT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.account ALTER COLUMN id SET DEFAULT nextval('banktest.account_id_seq'::regclass);


--
-- TOC entry 2760 (class 2604 OID 17453)
-- Name: bank id; Type: DEFAULT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.bank ALTER COLUMN id SET DEFAULT nextval('banktest.bank_id_seq'::regclass);


--
-- TOC entry 2761 (class 2604 OID 17454)
-- Name: transaction id; Type: DEFAULT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction ALTER COLUMN id SET DEFAULT nextval('banktest.transaction_id_seq'::regclass);


--
-- TOC entry 2762 (class 2604 OID 17455)
-- Name: user id; Type: DEFAULT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest."user" ALTER COLUMN id SET DEFAULT nextval('banktest.user_id_seq'::regclass);


--
-- TOC entry 2909 (class 0 OID 17423)
-- Dependencies: 197
-- Data for Name: account; Type: TABLE DATA; Schema: banktest; Owner: admin
--

INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (13, 'BY06BELV13579864201357927420', 85953.0200000000041, 2, 5, '2017-05-02 16:30:30');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (8, 'BY55CLEV99999999996699988999', 10493.8999999999996, 1, 8, '2017-10-02 16:30:34');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (14, 'BY06BELV24681357902468755790', 9948.5, 2, 6, '2000-09-02 16:30:10');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (18, 'BY11BPSB88887777666655557744', 33990.5400000000009, 3, 10, '2014-07-02 16:30:43');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (20, 'BY11BPSB44443333222211110000', 6606.40999999999985, 3, 12, '1992-05-02 16:30:59');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (10, 'BY06BELV44443333222331121000', 11683.6800000000003, 2, 2, '2008-09-02 16:30:46');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (4, 'BY55CLEV13579246801357664680', 168522.540000000008, 1, 4, '2019-10-02 16:30:15');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (6, 'BY55CLEV11112222331236345555', 252.5, 1, 6, '1995-09-02 16:30:03');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (9, 'BY06BELV77778888999900141111', 6610.44999999999982, 2, 1, '2005-07-02 16:31:13');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (2, 'BY55CLEV98765432109456343210', 25250, 1, 2, '1993-09-02 16:31:29');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (22, 'BY11BPSB13579246801357924680', 1010, 3, 14, '2006-09-02 16:31:25');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (11, 'BY06BELV23456789012345654901', 79709.1999999999971, 2, 3, '2023-09-02 16:31:19');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (5, 'BY55CLEV55555555565432135555', 104439, 1, 5, '1850-06-02 16:31:05');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (15, 'BY06BELV55555555445555525555', 5500.57999999999993, 2, 7, '2012-09-02 16:31:21');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (21, 'BY11BPSB13579864201357986420', 99000.6000000000058, 3, 13, '2012-05-02 16:30:38');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (12, 'BY06BELV43210987654321081765', 782.649999999999977, 2, 4, '2019-09-02 16:30:22');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (3, 'BY55CLEV24681357902422135790', 1000, 1, 3, '2013-09-02 16:30:55');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (24, 'BY11BPSB77778888999900001112', 10648, 3, 16, '2012-09-02 16:30:24');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (30, 'BY45BNBB13579782101357924680', 5706.5, 4, 14, '2009-05-02 16:31:46');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (19, 'BY11BPSB77778888999900001111', 11494.5, 3, 11, '2010-09-02 16:32:30');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (34, 'BY75BSBB44455713222211110000', 656.5, 5, 18, '2008-09-02 16:32:20');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (1, 'BY55CLEV12345568906712567890', 959.5, 1, 1, '2005-09-02 16:32:08');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (17, 'BY11BPSB11112222333344473555', 3353.19999999999982, 3, 9, '2013-09-02 16:31:50');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (7, 'BY55CLEV88887777666655824444', 359.560000000000002, 1, 7, '2012-09-02 16:31:39');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (28, 'BY45BNBB44443333222212560000', 7575, 4, 12, '2004-09-02 16:32:02');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (38, 'BY75BSBB44443333810211110000', 4428.60000000000036, 5, 20, '2001-09-02 16:31:33');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (32, 'BY45BNBB13579246536757924680', 353500, 4, 16, '2007-09-02 16:29:42');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (31, 'BY45BNBB77326888999900001111', 151.5, 4, 15, '2011-09-02 16:31:56');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (39, 'BY75BSBB13579864512357986420', 757.5, 5, 2, '1998-09-02 16:31:36');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (25, 'BY45BNBB11112275633344445555', 2250, 4, 9, '2004-09-02 16:32:17');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (40, 'BY75BSBB77715239999900001111', 3232, 5, 5, '2022-09-02 16:31:54');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (36, 'BY75BSBB55555512345675555555', 7000, 5, 20, '2020-09-02 16:31:52');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (37, 'BY75BSBB98765456109876543210', 5000, 5, 20, '2007-09-02 16:32:14');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (33, 'BY75BSBB11112265413344445555', 61199.5, 5, 17, '2008-09-02 16:32:22');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (29, 'BY45BNBB13579671201357986420', 8100, 4, 13, '2001-09-02 16:31:59');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (27, 'BY45BNBB77778886549900001111', 9531.45999999999913, 4, 11, '2005-09-02 16:32:11');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (26, 'BY45BNBB98765432643876543210', 8925.39999999999964, 4, 10, '2023-06-02 16:30:27');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (35, 'BY75BSBB77778888991534801111', 19373, 5, 19, '2007-09-02 16:32:25');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (23, 'BY11BPSB55555555555555555554', 892333, 3, 15, '2014-09-02 16:32:28');
INSERT INTO banktest.account (id, accountnumber, amount, bank_id, user_id, dateopen) VALUES (16, 'BY06BELV98765432109876561210', 145.219999999999999, 2, 8, '2000-06-02 16:30:50');


--
-- TOC entry 2911 (class 0 OID 17429)
-- Dependencies: 199
-- Data for Name: bank; Type: TABLE DATA; Schema: banktest; Owner: admin
--

INSERT INTO banktest.bank (id, name) VALUES (1, 'Clever-Bank');
INSERT INTO banktest.bank (id, name) VALUES (2, 'Belveb-Bank');
INSERT INTO banktest.bank (id, name) VALUES (3, 'BPS-Bank');
INSERT INTO banktest.bank (id, name) VALUES (4, 'BNB-Bank');
INSERT INTO banktest.bank (id, name) VALUES (5, 'BSB-Bank');


--
-- TOC entry 2913 (class 0 OID 17435)
-- Dependencies: 201
-- Data for Name: bank_user; Type: TABLE DATA; Schema: banktest; Owner: admin
--

INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 1);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 2);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 3);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 4);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 5);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 6);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 7);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (1, 8);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 1);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 2);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 3);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 4);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 5);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 6);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 7);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (2, 8);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 9);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 10);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 11);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 12);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 13);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 14);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 15);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (3, 16);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 9);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 10);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 11);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 12);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 13);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 14);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 15);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (4, 16);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 17);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 18);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 19);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 20);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 2);
INSERT INTO banktest.bank_user (bank_id, user_id) VALUES (5, 5);


--
-- TOC entry 2914 (class 0 OID 17438)
-- Dependencies: 202
-- Data for Name: transaction; Type: TABLE DATA; Schema: banktest; Owner: admin
--

INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (1, 'OUTGOING', 5, 5, 17, 19, 33, 35, 5690, '2023-09-03 15:32:06.376', 33);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (2, 'INCOMING', 5, 5, 17, 19, 33, 35, 5690, '2023-09-03 15:32:06.376', 35);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (3, 'ADDING', 5, 5, 17, 17, 33, 33, 15, '2023-09-03 15:32:13.435', 33);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (4, 'OUTGOING', 1, 2, 3, 4, 3, 12, 109.989999999999995, '2023-09-03 15:37:50.924', 3);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (5, 'INCOMING', 1, 2, 3, 4, 3, 12, 109.989999999999995, '2023-09-03 15:37:50.924', 12);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (6, 'OUTGOING', 4, 3, 11, 16, 27, 24, 1058, '2023-09-03 15:42:30.973', 27);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (7, 'INCOMING', 4, 3, 11, 16, 27, 24, 1058, '2023-09-03 15:42:30.973', 24);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (8, 'CASH', 3, 3, 15, 15, 23, 23, 55350, '2023-09-03 17:27:52.02', 23);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (9, 'OUTGOING', 2, 4, 8, 10, 16, 26, 150, '2023-09-03 17:33:11.992', 16);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (10, 'INCOMING', 2, 4, 8, 10, 16, 26, 150, '2023-09-03 17:33:11.992', 26);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (11, 'ADDING', 2, 2, 8, 8, 16, 16, 100, '2023-09-03 17:35:27.1', 16);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (12, 'CASH', 2, 2, 8, 8, 16, 16, 130, '2023-09-03 17:37:54.295', 16);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (13, 'ADDING', 3, 3, 15, 15, 23, 23, 1, '2023-09-03 17:53:12.939', 23);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (14, 'OUTGOING', 3, 5, 15, 19, 23, 35, 6668, '2023-09-03 17:53:35.126', 23);
INSERT INTO banktest.transaction (id, type, sendingbank_id, beneficiarybank_id, sendinguser_id, beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, "timestamp", account_id) VALUES (15, 'INCOMING', 3, 5, 15, 19, 23, 35, 6668, '2023-09-03 17:53:35.126', 35);


--
-- TOC entry 2916 (class 0 OID 17443)
-- Dependencies: 204
-- Data for Name: user; Type: TABLE DATA; Schema: banktest; Owner: admin
--

INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (1, 'Алексей', 'Иванов', 'Сергеевич', '+375291234567');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (2, 'Екатерина', 'Петрова', 'Андреевна', '+375292345678');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (3, 'Денис', 'Смирнов', 'Викторович', '+375293456789');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (4, 'Ольга', 'Козлова', 'Ивановна', '+375294567890');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (5, 'Игорь', 'Морозов', 'Николаевич', '+375295678901');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (6, 'Мария', 'Васильевна', 'Павловна', '+375296789012');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (7, 'Артем', 'Новиков', 'Владимирович', '+375297890123');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (8, 'Анастасия', 'Федоровна', 'Дмитриевна', '+372989012134');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (9, 'Павел', 'Соколов', 'Александрович', '+375299012345');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (10, 'Елена', 'Кузнецова', 'Михайловна', '+375291112233');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (11, 'Дмитрий', 'Андреев', 'Олегович', '+375292223344');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (12, 'Алена', 'Жукова', 'Алексеевна', '+375293334455');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (13, 'Иван', 'Гусев', 'Валерьевич', '+375294445566');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (14, 'Никита', 'Козлов', 'Сергеевич', '+375295556677');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (15, 'Ольга', 'Семенова', 'Вячеславовна', '+375296667788');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (16, 'Даниил', 'Иванов', 'Анатольевич', '+375297778899');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (17, 'Алина', 'Медведева', 'Геннадьевна', '+375298889900');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (18, 'Аресений', 'Попов', 'Юрьевич', '+375299990011');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (19, 'Виктория', 'Соловьева', 'Петровна', '+375291001122');
INSERT INTO banktest."user" (id, name, lastname, secondname, phonenumber) VALUES (20, 'Александр', 'Лебедев', 'Васильевич', '+375292112233');


--
-- TOC entry 2930 (class 0 OID 0)
-- Dependencies: 198
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: banktest; Owner: admin
--

SELECT pg_catalog.setval('banktest.account_id_seq', 40, true);


--
-- TOC entry 2931 (class 0 OID 0)
-- Dependencies: 200
-- Name: bank_id_seq; Type: SEQUENCE SET; Schema: banktest; Owner: admin
--

SELECT pg_catalog.setval('banktest.bank_id_seq', 7, true);


--
-- TOC entry 2932 (class 0 OID 0)
-- Dependencies: 203
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: banktest; Owner: admin
--

SELECT pg_catalog.setval('banktest.transaction_id_seq', 15, true);


--
-- TOC entry 2933 (class 0 OID 0)
-- Dependencies: 205
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: banktest; Owner: admin
--

SELECT pg_catalog.setval('banktest.user_id_seq', 26, true);


--
-- TOC entry 2770 (class 2606 OID 17457)
-- Name: account account_pkey; Type: CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- TOC entry 2772 (class 2606 OID 17459)
-- Name: bank bank_pkey; Type: CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);


--
-- TOC entry 2774 (class 2606 OID 17461)
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 2776 (class 2606 OID 17463)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 2777 (class 2606 OID 17464)
-- Name: account account_bank_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.account
    ADD CONSTRAINT account_bank_id_fkey FOREIGN KEY (bank_id) REFERENCES banktest.bank(id);


--
-- TOC entry 2778 (class 2606 OID 17469)
-- Name: account account_user_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.account
    ADD CONSTRAINT account_user_id_fkey FOREIGN KEY (user_id) REFERENCES banktest."user"(id);


--
-- TOC entry 2779 (class 2606 OID 17474)
-- Name: bank_user bank_user_bank_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.bank_user
    ADD CONSTRAINT bank_user_bank_id_fkey FOREIGN KEY (bank_id) REFERENCES banktest.bank(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2780 (class 2606 OID 17479)
-- Name: bank_user bank_user_user_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.bank_user
    ADD CONSTRAINT bank_user_user_id_fkey FOREIGN KEY (user_id) REFERENCES banktest."user"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2781 (class 2606 OID 17484)
-- Name: transaction transaction_account_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_account_id_fkey FOREIGN KEY (account_id) REFERENCES banktest.account(id);


--
-- TOC entry 2782 (class 2606 OID 17489)
-- Name: transaction transaction_beneficiaryaccount_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_beneficiaryaccount_id_fkey FOREIGN KEY (beneficiaryaccount_id) REFERENCES banktest.account(id);


--
-- TOC entry 2783 (class 2606 OID 17494)
-- Name: transaction transaction_beneficiarybank_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_beneficiarybank_id_fkey FOREIGN KEY (beneficiarybank_id) REFERENCES banktest.bank(id);


--
-- TOC entry 2784 (class 2606 OID 17499)
-- Name: transaction transaction_beneficiaryuser_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_beneficiaryuser_id_fkey FOREIGN KEY (beneficiaryuser_id) REFERENCES banktest."user"(id);


--
-- TOC entry 2785 (class 2606 OID 17504)
-- Name: transaction transaction_sendingaccount_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_sendingaccount_id_fkey FOREIGN KEY (sendingaccount_id) REFERENCES banktest.account(id);


--
-- TOC entry 2786 (class 2606 OID 17509)
-- Name: transaction transaction_sendingbank_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_sendingbank_id_fkey FOREIGN KEY (sendingbank_id) REFERENCES banktest.bank(id);


--
-- TOC entry 2787 (class 2606 OID 17514)
-- Name: transaction transaction_sendinguser_id_fkey; Type: FK CONSTRAINT; Schema: banktest; Owner: admin
--

ALTER TABLE ONLY banktest.transaction
    ADD CONSTRAINT transaction_sendinguser_id_fkey FOREIGN KEY (sendinguser_id) REFERENCES banktest."user"(id);


--
-- TOC entry 2925 (class 0 OID 0)
-- Dependencies: 8
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2023-09-03 18:02:02

--
-- PostgreSQL database dump complete
--

