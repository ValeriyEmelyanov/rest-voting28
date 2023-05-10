insert into USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
values ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@gmail.com', 'Admin_First', 'Admin_Last', '{noop}admin');

insert into USER_ROLE (ROLE, USER_ID)
values ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

insert into RESTAURANT (NAME)
values ('Crazy Cook'),
       ('Weeping Willow'),
       ('Number One');

insert into DISH (NAME, RESTAURANT_ID)
values ('Dish 1-1', 1),
       ('Dish 1-2', 1),
       ('Dish 1-3', 1),
       ('Dish 2-1', 2),
       ('Dish 2-2', 2),
       ('Dish 2-3', 2),
       ('Dish 3-1', 3),
       ('Dish 3-2', 3),
       ('Dish 3-3', 3);

insert into MENU (RESTAURANT_ID, DATE)
values (1, '2023-02-03'),
       (2, '2023-02-03'),
       (3, '2023-02-03'),
       (1, '2023-02-04');

insert into MENU_ITEMS(MENU_ID, DISH_ID, PRICE)
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
       (4, 3, 62);
