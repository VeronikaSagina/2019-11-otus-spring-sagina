alter table app_user add column consent_to_communication boolean default true;
create table sending_email_history(
    id uuid primary key,
    user_id uuid references app_user(user_id),
    sending_date timestamp
)