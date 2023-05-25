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
       ('Dish 4-1', 4),
       ('Dish 4-2', 4),
       ('Dish 4-3', 4);

insert into MENU_ITEM (RESTAURANT_ID, DATED, DISH_ID, PRICE)
values (1, CURRENT_DATE() - 1, 1, 120),
       (1, CURRENT_DATE() - 1, 2, 142.2),
       (1, CURRENT_DATE() - 1, 3, 60),
       (2, CURRENT_DATE() - 1, 4, 111),
       (2, CURRENT_DATE() - 1, 5, 133),
       (2, CURRENT_DATE() - 1, 6, 41),
       (3, CURRENT_DATE() - 1, 7, 125),
       (3, CURRENT_DATE() - 1, 8, 135),
       (3, CURRENT_DATE() - 1, 9, 55.5),
       (4, CURRENT_DATE() - 1, 10, 142),
       (4, CURRENT_DATE() - 1, 11, 62),
       (1, CURRENT_DATE(), 1, 100),
       (1, CURRENT_DATE(), 2, 100),
       (1, CURRENT_DATE(), 3, 100),
       (2, CURRENT_DATE(), 4, 110),
       (2, CURRENT_DATE(), 5, 110),
       (2, CURRENT_DATE(), 6, 110);

insert into VOTE (USER_ID, RESTAURANT_ID, DATED)
values (1, 1, CURRENT_DATE() - 1),
       (2, 2, CURRENT_DATE() - 1),
       (1, 2, CURRENT_DATE());
