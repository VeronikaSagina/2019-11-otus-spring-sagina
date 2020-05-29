alter table app_user add constraint unique_login unique (login);
update app_user set role = 'ROLE_USER' where role = 'ROLE_LITTLE_USER';