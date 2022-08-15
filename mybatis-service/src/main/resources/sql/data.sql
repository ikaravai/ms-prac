INSERT INTO "users"
VALUES (1, 'my@mail.com', 'Joe Biden'),
       (2, 'next@mail.com', 'Bezos'),
       (3, 'cool@mail.com', 'short name')
ON CONFLICT DO NOTHING;


INSERT INTO "addresses"
VALUES (1, 'Minsk', 'Melnikayte', 15, 220000),
       (2, 'Brest', 'Centralnaya', 25, 220033),
       (3, 'Lodz', 'Some polish name', 3, 993377)
ON CONFLICT DO NOTHING;