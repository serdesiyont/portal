create table if not exists lessons(
    id          bigserial primary key,
    title       varchar(100) not null,
    address     varchar not null,
    schedule    timestamptz not null,
    division    varchar not null check (upper(division) in ('ALL', 'DJANGO', 'REACT', 'LARAVEL', 'DSA', 'BEGINNER', 'FLUTTER')),
    posted_by   bigint    not null
    constraint lessons_users_id_fk
    references users
    on update restrict on delete restrict
)