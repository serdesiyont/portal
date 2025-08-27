create table if not exists exercises
(
    id          bigserial primary key,
    title       varchar(50)   not null,
    description text      not null,
    language    varchar(10)   not null,
    boilerplate jsonb     not null,
    test_cases  jsonb     not null,
    schedule    timestamptz not null,
    division    varchar not null check (upper(division) in ('DJANGO', 'REACT', 'LARAVEL', 'DSA', 'BEGINNER', 'FLUTTER')),
    posted_by   bigint    not null
    constraint resources_users_id_fk
    references users
    on update restrict on delete restrict
);

