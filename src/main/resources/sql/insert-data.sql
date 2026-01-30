
INSERT INTO uzytkownik (login, email) VALUES
('adam', 'adam@example.com'),
('bartek', 'bartek@example.com'),
('celina', 'celina@example.com'),
('daria', 'daria@example.com');

INSERT INTO follower (user_id, follows_user_id) VALUES
(0, 1),
(0, 2),
(1, 2),
(2, 3);


INSERT INTO wpis (user_id, tresc) VALUES
(0, 'Pierwszy wpis Adama'),
(1, 'Bartek testuje system'),
(2, 'Celina wrzuca aktualizację'),
(0, 'Adam dodaje kolejny wpis'),
(3, 'Daria dołącza do platformy');
