/**
DROP TABLE IF EXISTS public.flyway_schema_history;
DROP TABLE IF EXISTS public.agreement;
DROP TABLE IF EXISTS public.tpp_product_register;
DROP TABLE IF EXISTS public.tpp_product;
DROP TABLE IF EXISTS public.account;
DROP TABLE IF EXISTS public.account_pool;
DROP TABLE IF EXISTS public.tpp_ref_product_register_type;
DROP TABLE IF EXISTS public.tpp_ref_product_class;
DROP TABLE IF EXISTS public.tpp_ref_account_type;
**/

CREATE TABLE IF NOT EXISTS tpp_ref_account_type
(
    internal_id serial PRIMARY KEY ,
    value VARCHAR(100) UNIQUE NOT NULL
    );
create unique index idx_account_type_value on tpp_ref_account_type(value);


CREATE TABLE IF NOT EXISTS tpp_ref_product_class
(
    internal_id serial PRIMARY KEY ,
    value VARCHAR(100) UNIQUE NOT NULL,
    gbl_code VARCHAR(50),
    gbl_name VARCHAR(100),
    product_row_code VARCHAR(50),
    product_row_name VARCHAR(100),
    subclass_code VARCHAR(50),
    subclass_name VARCHAR(100)
    );
create unique index idx_product_class_value on tpp_ref_product_class(value);


CREATE TABLE IF NOT EXISTS tpp_ref_product_register_type
(
    internal_id serial PRIMARY KEY ,
    value VARCHAR(100) UNIQUE NOT NULL,
    register_type_name VARCHAR(100) NOT NULL,
    product_class_code VARCHAR(100) NOT NULL,
    register_type_start_date TIMESTAMP,
    register_type_end_date TIMESTAMP,
    account_type VARCHAR(100)
    );

create unique index idx_register_type_value on tpp_ref_product_register_type(value);

ALTER TABLE tpp_ref_product_register_type
    ADD FOREIGN KEY (product_class_code) REFERENCES tpp_ref_product_class (value);

ALTER TABLE tpp_ref_product_register_type
    ADD FOREIGN KEY (account_type) REFERENCES tpp_ref_account_type (value);

CREATE TABLE IF NOT EXISTS account_pool(
                                           id bigserial PRIMARY KEY,
                                           branch_code VARCHAR(50),
    currency_code VARCHAR(30),
    mdm_code VARCHAR(50),
    priority_code VARCHAR(30),
    register_type_code VARCHAR(100)
    );

ALTER TABLE account_pool
    ADD FOREIGN KEY (register_type_code) REFERENCES tpp_ref_product_register_type (value);

CREATE TABLE IF NOT EXISTS account(
                                      id bigserial PRIMARY KEY,
                                      account_pool_id BIGINT,
                                      account_number VARCHAR(25),
    busy BOOLEAN
    );

ALTER TABLE account
    ADD FOREIGN KEY (account_pool_id) REFERENCES account_pool (id);

CREATE TABLE IF NOT EXISTS tpp_product
(
    id bigserial PRIMARY KEY,
    product_code_id INT,
    client_id BIGINT,
    type VARCHAR(50),
    number VARCHAR(50),
    priority BIGINT,
    date_of_conclusion TIMESTAMP,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    days INT,
    penalty_rate DECIMAL,
    nso DECIMAL,
    threshold_amount DECIMAL,
    requisite_type VARCHAR(50),
    interest_rate_type VARCHAR(50),
    tax_rate DECIMAL,
    reason_close VARCHAR(100),
    state VARCHAR(50)
    );

ALTER TABLE tpp_product
    ADD FOREIGN KEY (product_code_id) REFERENCES tpp_ref_product_class (internal_id);

CREATE TABLE IF NOT EXISTS tpp_product_register
(
    id bigserial PRIMARY KEY ,
    product_id BIGINT,
    register_type VARCHAR(100) NOT NULL,
    account BIGINT,
    currency_code VARCHAR(30),
    state VARCHAR(50),
    account_number VARCHAR(25)
    );

ALTER TABLE tpp_product_register
    ADD FOREIGN KEY (register_type) REFERENCES tpp_ref_product_register_type (value);

ALTER TABLE tpp_product_register
    ADD FOREIGN KEY (product_id) REFERENCES tpp_product (id);

CREATE TABLE IF NOT EXISTS agreement
(
    id bigserial PRIMARY KEY,
    product_id BIGINT,
    general_agreement_id VARCHAR(50),
    supplementary_agreement_id VARCHAR(50),
    arrangement_type VARCHAR(50),
    scheduler_job_id BIGINT,
    number VARCHAR(50),
    opening_date TIMESTAMP,
    closing_date TIMESTAMP,
    cancel_date TIMESTAMP,
    validity_duration BIGINT,
    cancellation_reason VARCHAR(100),
    status VARCHAR(50),
    interest_calculation_date TIMESTAMP,
    interest_rate DECIMAL,
    coefficient DECIMAL,
    coefficient_action VARCHAR(50),
    minimum_interest_rate DECIMAL,
    minimum_interest_rate_coefficient DECIMAL,
    minimum_interest_rate_coefficient_action VARCHAR(50),
    maximal_interest_rate DECIMAL,
    maximal_interest_rate_coefficient DECIMAL,
    maximal_interest_rate_coefficient_action VARCHAR(50)
    );

ALTER TABLE agreement
    ADD FOREIGN KEY (product_id) REFERENCES tpp_product (id);
