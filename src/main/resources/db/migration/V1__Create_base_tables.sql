-- V1__Create_base_tables.sql
-- Initial database schema

CREATE SEQUENCE public.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.account_currency_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.account_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.account_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.employee_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.passport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Main tables
CREATE TABLE public.passport (
    id bigint NOT NULL DEFAULT nextval('public.passport_id_seq'::regclass),
    first_name character varying(15) NOT NULL,
    last_name character varying(30) NOT NULL,
    middle_name character varying(30),
    date_of_birth date NOT NULL,
    personal_number character varying(255) NOT NULL,
    is_lost boolean DEFAULT false NOT NULL,
    passport_number character varying(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE public.users (
    id bigint NOT NULL DEFAULT nextval('public.users_id_seq'::regclass),
    passport_id bigint NOT NULL UNIQUE,
    password_hash character varying(255),
    email character varying(35) NOT NULL,
    user_type character varying(20) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    phone_number character varying(20) NOT NULL,
    deleted_at timestamp with time zone,
    ac_token character varying,
    activation_token_expires_at timestamp with time zone,
    PRIMARY KEY (id),
    CONSTRAINT users_user_type_check CHECK (((user_type)::text = ANY ((ARRAY['CLIENT'::character varying, 'EMPLOYEE'::character varying, 'ADMIN'::character varying])::text[]))),
    CONSTRAINT users_passport_id_fkey FOREIGN KEY (passport_id) REFERENCES public.passport(id)
);

CREATE TABLE public.client (
    id bigint NOT NULL DEFAULT nextval('public.client_id_seq'::regclass),
    user_id bigint NOT NULL UNIQUE,
    created_at timestamp with time zone NOT NULL,
    credit_rating integer DEFAULT 0,
    client_status character varying(15) DEFAULT 'ACTIVE'::character varying NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT client_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.account_type (
    id bigint NOT NULL DEFAULT nextval('public.account_type_id_seq'::regclass),
    name character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.account_currency (
    id bigint NOT NULL DEFAULT nextval('public.account_currency_id_seq'::regclass),
    code character varying NOT NULL UNIQUE,
    name character varying(10) NOT NULL,
    digital_code character varying NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE public.account (
    id bigint NOT NULL DEFAULT nextval('public.account_id_seq'::regclass),
    date_created timestamp with time zone DEFAULT now() NOT NULL,
    account_type_id bigint NOT NULL,
    balance numeric(19,4) DEFAULT 0 NOT NULL,
    account_number character varying(20) NOT NULL UNIQUE,
    currency_id bigint NOT NULL,
    status character varying(255) NOT NULL,
    client_id bigint,
    PRIMARY KEY (id),
    CONSTRAINT account_account_type_id_fkey FOREIGN KEY (account_type_id) REFERENCES public.account_type(id),
    CONSTRAINT account_currency_id_fkey FOREIGN KEY (currency_id) REFERENCES public.account_currency(id),
    CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES public.client(id)
);

CREATE TABLE public.employee (
    id bigint NOT NULL DEFAULT nextval('public.employee_id_seq'::regclass),
    salary numeric(19,4) NOT NULL,
    hired_at timestamp with time zone NOT NULL,
    employee_status character varying(15) DEFAULT 'ACTIVE'::character varying NOT NULL,
    user_id bigint NOT NULL UNIQUE,
    PRIMARY KEY (id),
    CONSTRAINT employee_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.employee_position (
    id bigint NOT NULL DEFAULT nextval('public.employee_position_id_seq'::regclass),
    name character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.employee_positions_map (
    employee_id bigint NOT NULL,
    position_id bigint NOT NULL,
    assigned_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (employee_id, position_id),
    CONSTRAINT fk_map_employee FOREIGN KEY (employee_id) REFERENCES public.employee(id) ON DELETE CASCADE,
    CONSTRAINT fk_map_position FOREIGN KEY (position_id) REFERENCES public.employee_position(id) ON DELETE CASCADE
);

CREATE TABLE public.account_request (
    id bigint NOT NULL DEFAULT nextval('public.account_request_id_seq'::regclass),
    client_id bigint NOT NULL,
    account_type_id bigint NOT NULL,
    currency_id bigint NOT NULL,
    status character varying(50) DEFAULT 'PENDING'::character varying NOT NULL,
    approved_by_employee_id bigint,
    rejection_reason text,
    requested_at timestamp without time zone DEFAULT now() NOT NULL,
    processed_at timestamp without time zone,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_request_account_type FOREIGN KEY (account_type_id) REFERENCES public.account_type(id),
    CONSTRAINT fk_account_request_client FOREIGN KEY (client_id) REFERENCES public.client(id),
    CONSTRAINT fk_account_request_currency FOREIGN KEY (currency_id) REFERENCES public.account_currency(id),
    CONSTRAINT fk_account_request_employee FOREIGN KEY (approved_by_employee_id) REFERENCES public.employee(id)
);

CREATE TABLE public.operation (
    id uuid NOT NULL,
    initiator_id bigint,
    channel character varying(20) DEFAULT 'WEB'::character varying NOT NULL,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    reason character varying(100) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT operation_initiator_id_fkey FOREIGN KEY (initiator_id) REFERENCES public.users(id)
);

CREATE TABLE public.transaction (
    id bigint NOT NULL DEFAULT nextval('public.transaction_id_seq'::regclass),
    account_id bigint NOT NULL,
    transaction_date timestamp with time zone NOT NULL,
    transaction_type character varying(255) NOT NULL,
    operation_id uuid NOT NULL,
    amount numeric(19,4) NOT NULL,
    transaction_after numeric(19,4),
    PRIMARY KEY (id),
    CONSTRAINT transaction_account_id_fkey FOREIGN KEY (account_id) REFERENCES public.account(id),
    CONSTRAINT transaction_operation_id_fkey FOREIGN KEY (operation_id) REFERENCES public.operation(id)
);

-- Indexes
CREATE INDEX idx_accounts_account_number ON public.account USING btree (account_number);
CREATE INDEX idx_users_email ON public.users USING btree (email);
CREATE INDEX idx_users_phone_number ON public.users USING btree (phone_number);
CREATE UNIQUE INDEX uk_active_client_user ON public.client USING btree (user_id) WHERE ((client_status)::text <> 'DELETED'::text);
CREATE UNIQUE INDEX uk_active_employee_user ON public.employee USING btree (user_id) WHERE ((employee_status)::text <> 'FIRED'::text);
CREATE UNIQUE INDEX users_email_active_idx ON public.users USING btree (email) WHERE (deleted_at IS NULL);
CREATE UNIQUE INDEX users_phone_active_idx ON public.users USING btree (phone_number) WHERE (deleted_at IS NULL);
