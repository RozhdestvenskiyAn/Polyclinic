CREATE TABLE IF NOT EXISTS Doctor
(
    id         BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    last_name  VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    patronymic varchar(50)
);
INSERT INTO Doctor(last_name, first_name, patronymic)
VALUES ('Ivanov', 'Ivan', 'Ivanovich'),
       ('Sidorov', 'Sidr', 'Sidorovich'),
       ('Petrov', 'Sergey', 'Andreevich'),
       ('Sokolov', 'Andrey', 'Petrovich');

CREATE TABLE IF NOT EXISTS Patient
(
    id            BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    last_name     VARCHAR(50) NOT NULL,
    first_name    VARCHAR(50) NOT NULL,
    patronymic    varchar(50),
    date_birthday date        NOT NULL
);
INSERT INTO Patient(last_name, first_name, patronymic, date_birthday)
VALUES ('Morozov', 'Mark', 'Igorevich', '1999-01-08'),
       ('Ivanov', 'Nikolai', 'Nikolaevich', '2015-05-11'),
       ('Kozlov', 'Aleksey', 'Ivanovich', '1980-11-25'),
       ('Nosov', 'Aleksandr', 'Andreevich', '1982-02-11'),
       ('Nosova', 'Svetlana', 'Igorevna', '1982-08-13'),
       ('Pavlova', 'Olga', 'Ivanovna', '1999-01-01'),
       ('Ivanova', 'Irina', 'Petrovna', '2001-5-19');

CREATE TABLE IF NOT EXISTS Ticket
(
    id               BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_doctor        BIGINT REFERENCES Doctor (id) NOT NULL,
    id_patient       BIGINT REFERENCES Patient (id),
    time_appointment TIMESTAMP                     NOT NULL
);
INSERT INTO Ticket(id_doctor, id_patient, time_appointment)
VALUES (1, 1, '2023-01-21 08:00:00'),
       (1, null, '2023-01-21 08:30:00'),
       (1, 2, '2023-01-21 09:00:00'),
       (1, 3, '2023-01-21 09:30:00'),
       (1, 7, '2023-01-21 10:00:00'),
       (2, 1, '2023-01-21 16:00:00'),
       (2, 7, '2023-01-21 17:00:00'),
       (2, 2, '2023-01-21 18:00:00'),
       (2, null, '2023-01-21 19:00:00');