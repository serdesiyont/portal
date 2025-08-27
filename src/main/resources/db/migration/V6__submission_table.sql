create table if not exists submissions
(
    id bigserial primary key,
    student_id     bigint    not null
    constraint submissions_users_id_fk
    references users
    on update restrict on delete restrict,
    exercise_id    bigint           not null
    constraint submissions_exercises_id_fk
    references exercises
    on update restrict on delete restrict,
    submitted_at   timestamptz default now() not null,
    status         varchar(15)                  not null,
    submitted_code jsonb                     not null
    );

