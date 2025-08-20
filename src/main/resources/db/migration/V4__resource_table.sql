create table if not exists resources(
    id          bigserial primary key,
    title       varchar(100) not null,
    address     varchar not null,
    division    varchar not null check (upper(division) in ('DJANGO', 'REACT', 'LARAVEL', 'DSA', 'BEGINNER', 'FLUTTER')),
    posted_by   bigint    not null
    constraint resources_users_id_fk
    references users
    on update restrict on delete restrict
    )