create table if not exists users
(
    id          bigserial primary key,
    name        varchar(126) not null,
    email       varchar unique not null,
    password    varchar not null,
    division    varchar not null check (upper(division) in ('ALL', 'DJANGO', 'REACT', 'LARAVEL', 'DSA', 'BEGINNER', 'FLUTTER')),
    role        varchar not null check (upper(role) in ('SOCIAL', 'ADMIN', 'MENTOR', 'STUDENT')),
    gender      varchar not null check (upper(gender) in ('MALE', 'FEMALE')),
    phoneNum    integer not null,
    additional text,
    created_at  timestamptz default now(),
    passchanged bool default false
    );
);

create table if not exists posts
(
    id          bigserial primary key,
    title       varchar(150) not null,
    pdf_title   varchar,
    pdf_link    varchar,
    video_title varchar,
    video_link  varchar,
    schedule    timestamptz,
    division    varchar not null check (upper(division) in ('ALL', 'DJANGO', 'REACT', 'LARAVEL', 'DSA', 'BEGINNER', 'FLUTTER')),
    posted_by   bigint    not null
    constraint posts_users_id_fk
    references users
    on update restrict on delete restrict
    );

