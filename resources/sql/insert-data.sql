
INSERT INTO uzytkownik (login, email) VALUES
('adam', 'adam@example.com'),
('bartek', 'bartek@example.com'),
('celina', 'celina@example.com'),
('daria', 'daria@example.com');

INSERT INTO follower (user_id, follows_user_id) VALUES
(1, 2),
(1, 3),
(2, 3),
(3, 4);

INSERT INTO wpis (user_id, tresc) VALUES
(1, 'Pierwszy wpis Adama'),
(2, 'Bartek testuje system'),
(3, 'Celina wrzuca aktualizację'),
(1, 'Adam dodaje kolejny wpis'),
(4, 'Daria dołącza do platformy');