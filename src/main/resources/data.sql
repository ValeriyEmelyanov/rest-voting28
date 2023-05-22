insert into USERS (EMAIL, FIRST_NAME, LAST_NAME, CONTACT, PASSWORD)
values ('user@gmail.com', 'User_First', 'User_Last', '+7(007) 000 0009', '{noop}password'),
       ('admin@gmail.com', 'Admin_First', 'Admin_Last', 't.me/AdminOne', '{noop}admin');

insert into USER_ROLE (ROLE, USER_ID)
values ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

insert into RESTAURANT (NAME)
values ('Crazy Cook'),
       ('Weeping Willow'),
       ('Number One'),
       ('Big plate');

insert into DISH (NAME, RESTAURANT_ID)
values ('Dish 1-1', 1),
       ('Dish 1-2', 1),
       ('Dish 1-3', 1),
       ('Dish 2-1', 2),
       ('Dish 2-2', 2),
       ('Dish 2-3', 2),
       ('Dish 3-1', 3),
       ('Dish 3-2', 3),
       ('Dish 3-3', 3),
       ('Dish 4-1', 3),
       ('Dish 4-2', 3),
       ('Dish 4-3', 3);

insert into MENU (RESTAURANT_ID, DATED)
values (1, '2023-05-21'),
       (2, '2023-05-21'),
       (3, '2023-05-21'),
       (4, '2023-05-21'),
       (1, '2023-05-22'),
       (2, '2023-05-22'),
       (3, '2023-05-22');

insert into MENU_ITEM (MENU_ID, DISH_ID, PRICE)
values (1, 1, 120),
       (1, 2, 142.2),
       (1, 3, 60),
       (2, 4, 111),
       (2, 5, 133),
       (2, 6, 41),
       (3, 7, 125),
       (3, 8, 135),
       (3, 8, 55.5),
       (4, 2, 142),
       (4, 3, 62),
       (5, 4, 100),
       (5, 5, 100),
       (5, 6, 100),
       (6, 7, 110),
       (6, 8, 110),
       (6, 9, 110);

insert into VOTE (USER_ID, RESTAURANT_ID, DATED)
values (1, 1, '2023-05-22'),
       (2, 2, '2023-05-22'),
       (1, 2, '2023-05-23');
