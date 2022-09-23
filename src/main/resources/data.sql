INSERT INTO roles (id, name)
VALUES (1, 'ROLE_HELP-SEEKER'),
       (2, 'ROLE_VOLUNTEER'),
       (3, 'ROLE_ADMIN');

INSERT INTO users (username, email, password)
VALUES ('Fatma', 'Fatma@gmail.com', '$2a$10$i0aOW9jHAtY7gAd/IF09b.ynx09W0fehRFY7RHA3kFRz9SAl8R8/W'),
       ('Annie', 'ASprock@gmail.com', '$2a$10$yKyFDHNu/eNrOSvo0/1vSuowoHflBnA4Y9PKEV9bzT/SoX57P3bEa'),
       ('Bas', 'Bassie@gmail.com', '$2a$10$PKRmQ4lD3pLbIk8NimZkt.fkoC04cEeEneNHiyVxktIiprVEqLlJK'),
       ('Richard', 'RVRiper@gmail.com', '$2a$10$4DPG/0KqRCoO9TIVDuCAJ.gze6MGAQ09N1kZbh7gGoD5TRsePfc56'),
       ('Karen', 'Karen1966@gmail.com', '$2a$10$BAcbok/mztd1MyaTS.3b2OkmIDNGmMt4WY2b90kxtLHgoBSlZPhLG'),
       ('YEmir', 'Yusuf1@gmail.com', '$2a$10$MPrU3At9LFQqJdb70OKVpOc3rcVDMIbuK8e7iclAwb32K2RuIJ3hi'),
       ('Bella', 'IsaBello@gmail.com', '$2a$10$438sunxGwaMwKhRU6wj02u.EAxMPy17OMzPZqkpv0g7EhQDNputSq'),
       ('Chris', 'Ossyman@gmail.com', '$2a$10$izlBLt10vip6ZFrZPeVLLejDR9bIcndw9MpIAEe6mPUDuVgwDIR.a'),
       ('ADMIN', 'HulppostAdmin@gmail.com', '$2a$10$a1c5ghLm4z960q8AnHyXGeWsYqej1e.LB28qqf1ec1OUALYbPhuHG');

INSERT INTO user_roles (role_id, user_id)
VALUES (3, 1),
       (2, 2),
       (1, 3),
       (2, 4),
       (1, 5),
       (2, 6),
       (1, 7),
       (2, 8),
       (3, 9);

INSERT INTO user_accounts (id, first_name, surname, gender, birthday, zip_code)
VALUES (1, 'Fatma', 'Gul', 'V', '01/10/1991', '1068AA'),
       (2, 'Annie', 'Sprock', 'V', '10/02/1998', '1056BZ'),
       (3, 'Bas', 'Bodden', 'M', '13/05/1965', '1100DV'),
       (4, 'Richard', 'Van Riper', 'M', '09/03/1983', '1045PO'),
       (5, 'Karen', 'Lubben', 'V', '18/01/1966', '1077LA'),
       (6, 'Yusuf', 'Emir', 'M', '17/09/2000', '1059KE'),
       (7, 'Isabella', 'Schumacker', 'V', '01/07/1970', '1051PP'),
       (8, 'Christiaan', 'Ossman', 'M', '16/03/1998', '1067DR'),
       (9, 'ADMIN', 'PASS', 'M', '24/02/1985', '1069KU');

INSERT INTO requests (id, title, type_request, content, user_id)
VALUES (1, 'Maaltijden rondbrengen bij ouderen in Osdorp', 'Praktisch',
        'Zorgcentrum HulpOrganisatie in Osdorp is op zoek naar enkele enthousiaste vrijwilligers, die maaltijden bij ouderen in de wijk willen bezorgen. Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.',
        3),
       (2, 'Hulp bij ophalen pakket voedselbank', 'Praktisch',
        'Hallo, Voor een cliënte van de Thuisbegeleiding van de Amstelring ben ik op zoek naar een persoon die haar kan helpen met het ophalen van een pakket bij de voedselbank in Hoofddorp. Met vriendelijke groet, Karen',
        5),
       (3, 'Docent computer (M/V)', 'Sociaal',
        'Werkzaamheden: Senioren leren omgaan met Windows 10, Android of iOS. Bedoeling is om senioren wegwijs te maken met het internet, e-mail, apps, computer-veiligheid en de sociale media. Klassikaal lesgeven met behulp van lesmateriaal.',
        7);

INSERT INTO replies (id, text, user_id, request_id)
VALUES (1,
        'Hallo, ik ben bereid om de maaltijden te bezorgen aan de ouderen in de wijk u kunt me bereiken via mijn mail RVRiper@gmail.com',
        4, 1),
       (2, 'Hoi, Ik ben Annie en ik kan u wel helpen met uw verzoek om de kratten naar het aangegeven adres te leveren',
        2, 2),
       (3,
        'Mijn naam is Yusuf en ik kan wel een uurtje in de week langskomen om een uitleg te geven. Met vriendelijke groet, Yusuf',
        6, 3),
       (4,
        'Hallo, als u het goed vind kan ik per week een keer langskomen om de maaltijden rond te brengen, Groetjes, Christiaan',
        8, 1);

