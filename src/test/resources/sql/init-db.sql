create table if not exists payment_providers (
    id   bigint generated by default as identity,
    name varchar(100) not null constraint payment_providers_pkey primary key
);

create table if not exists  transactions (
    id bigint generated by default as identity constraint transactions_pkey primary key,
    external_id text not null,
    core_id text,
    status text not null,
    replenishment_status text not null,
    amount numeric(12, 2) not null,
    currency text not null,
    commission_amount numeric(12, 2) not null,
    commission_currency text not null,
    user_id text not null,
    external_date timestamp,
    created_at timestamp      not null,
    updated_at timestamp      not null,
    replenish_after timestamp,
    additional_data text not null,
    provider varchar(100) constraint fk_transactions_providers references payment_providers
        on update restrict on delete restrict
);

INSERT INTO payment_providers(name)
VALUES ('test');

INSERT INTO transactions(id, external_id, status, replenishment_status, amount, currency, commission_amount,
                         commission_currency, user_id, created_at, updated_at, additional_data, provider)
VALUES (0, 'test', 'INITIAL', 'INITIAL', 123.123, 'USD', 12.12, 'USD', '321', '2022-02-10 19:06:35.940163',
        '2022-02-10 19:06:35.940163', '{}', 'test');