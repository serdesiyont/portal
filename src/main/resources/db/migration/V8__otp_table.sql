create table if not exists OTP
(
    id         bigserial primary key,
    email      varchar(50) not null,
    purpose    varchar not null,
    code       integer     not null,
    expires_at timestamptz not null
    );