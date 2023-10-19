INSERT INTO stock (uuid, name, ticker, price, amount)
VALUES ('1a02b5ea-72c2-4b6d-bca3-03b242c6e7a5', 'Sberbank', 'SBER', 250.25, 12150),
       ('2fbc9cc2-331e-4ae0-85d1-40c7e1666b81', 'Yandex', 'YNDX', 2550.50, 5230),
       ('3d44f2f7-7f70-4cc3-8c94-9f9e1dcd415e', 'Gazprom', 'GAZP', 175.75, 15780);

INSERT INTO account (uuid, name, funds)
VALUES ('f2e06a2d-5a36-4e67-9b5d-7f7aee87c83a', 'one', 10000.00),
       ('950adf02-8a5a-42f5-aa47-46ec18d145dd', 'two', 7500.00);

INSERT INTO account_stock (uuid, account_uuid, stock_uuid, amount)
VALUES ('6e5b6674-55b7-4f2a-aac6-28e4c96c8b09', 'f2e06a2d-5a36-4e67-9b5d-7f7aee87c83a', '1a02b5ea-72c2-4b6d-bca3-03b242c6e7a5', 250),
       ('d940f79d-9e83-4c64-8de4-4f24e5830f6a', 'f2e06a2d-5a36-4e67-9b5d-7f7aee87c83a', '2fbc9cc2-331e-4ae0-85d1-40c7e1666b81', 50),
       ('a74644f6-50c0-45f2-9130-5a6b140abecd', '950adf02-8a5a-42f5-aa47-46ec18d145dd', '3d44f2f7-7f70-4cc3-8c94-9f9e1dcd415e', 1000);
