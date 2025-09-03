create table if not exists conversations (
    id bigserial primary key,
    user_id   bigint    not null
    constraint posts_users_id_fk
    references users
    on update restrict on delete restrict,
    user_message text not null,
    ai_response text not null,
    created_at timestamptz
)