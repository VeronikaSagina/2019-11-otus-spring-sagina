alter table book_comment
    add column user_id uuid references app_user (user_id);
update book_comment
set user_id = '5c33792b-f687-4f0f-aa3b-04faae53c663'
where comment_id = 'af036220-bcb6-4682-86ac-f9b2cf5bf63e';
update book_comment
set user_id = '5bf25594-a3ee-4444-861b-4839f3172d97'
where comment_id = '5e444e0c-0295-43d2-a6f9-4a18adb983af';
update book_comment
set user_id = '003f44ab-24f0-4f46-9420-a97ba70ee513'
where comment_id = 'ce860dc6-4425-4d47-bdee-e8eb3c050990';
update book_comment
set user_id = '7b74ae7b-8691-48ed-9502-255ebafe1b7f'
where comment_id = 'a56704ad-2437-4be7-8d6e-55ea2c85dbb4';
update book_comment
set user_id = '7b74ae7b-8691-48ed-9502-255ebafe1b7f'
where comment_id = '42e70e95-cc21-449b-95e9-a6eb548e8c13';
alter table book_comment alter column user_id set not null;