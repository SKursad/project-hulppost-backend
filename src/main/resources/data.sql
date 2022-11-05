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
VALUES (1, 'Fatma', 'Gul', 'V', date '1991-1-19', '1068AA'),
       (2, 'Annie', 'Sprock', 'V', date '1998-2-10', '1056BZ'),
       (3, 'Bas', 'Bodden', 'M', date '1965-5-13', '1100DV'),
       (4, 'Richard', 'Van Riper', 'M', date '1983-9-3', '1045PO'),
       (5, 'Karen', 'Lubben', 'V', date '1966-1-18', '1077LA'),
       (6, 'Yusuf', 'Emir', 'M', date'2000-9-17', '1059KE'),
       (7, 'Isabella', 'Schumacker', 'V', date '1989-1-7', '1051PP'),
       (8, 'Christiaan', 'Ossman', 'M', date '1998-3-16', '1067DR'),
       (9, 'ADMIN', 'PASS', 'M', date'1985-2-24', '1069KU');

INSERT INTO requests (id, timestamp, title, type_request, content, user_id)
VALUES (1, timestamp '2022-10-1 10:12', 'Maaltijden rondbrengen bij ouderen in Osdorp', 'Praktisch',
        'Zorgcentrum HulpOrganisatie in Osdorp is op zoek naar enkele enthousiaste vrijwilligers,
                die maaltijden bij ouderen in de wijk willen bezorgen.
                Dit doe je met je eigen auto, de gereden kilometers kunnen worden gedeclareerd.
                De maaltijden worden dagelijks in de keuken vers bereid. We zijn op zoek voor de zaterdag om de week of 1x in de maand. Het is licht fysiek werk.
                De voorkeur gaat uit naar mensen die in Osdorp wonen. De maaltijden moeten dagelijks tussen 11.30 en 13.00 uur in Osdorp worden bezorgd.
                U ontvangt jaarlijks een eindejaarsetentje en wordt uitgenodigd voor het jaarlijkse vrijwilligersdiner.
                Bent u geïnteresseerd, of ken u iemand uit uw eigen omgeving die dit zou willen doen. Neem dan contact met ons op.',
        3),
       (2, timestamp '2022-10-3 9:23', 'Chauffeur voor korte ritten', 'Praktisch',
        'Beste vrijwilliger, wij zoeken voor onze dagbesteding een chauffeur die de mensen wil halen van hun huisadres naar zorgcentrum De Geuzen waar de dagbesteding is en weer terug brengen.
                Het is een bus waar 8 personen in kunnen. Tijden: 9.00 1e rit en 16.00 uur 2e rit. De dagen zijn dinsdag, woensdag, donderdag of vrijdag.
                Op dit moment is de vraag voor donderdag het grootst. Er is altijd een begeleider bij. Heb je zin om ons te helpen dan kan je contact met mij opnemen.
                Over de dagen is te praten, en ook of je de ochtend ritten of middag ritten zou willen gaan doen.
                Alle hulp is welkom. Voor verdere informatie kan je me mailen. vriendelijke groet Karen Coördinator vrijwilligers.',
        5),
       (3, timestamp '2022-8-6 8:38', 'Docent computer (M/V)', 'Sociaal',
        'Werkzaamheden: Senioren leren omgaan met Windows 10, Android of iOS. Bedoeling is om senioren wegwijs te maken met het internet, e-mail, apps, computer-veiligheid en de sociale media. Klassikaal lesgeven met behulp van lesmateriaal.
                met een collega-vrijwilliger aan maximaal 6 deelnemers of bij de inloop (senioren komen binnen lopen met hun specifieke vragen). U wordt ingewerkt door een ervaren vrijwilliger en eventueel lesmateriaal wordt beschikbaar gesteld door de stichting.
                Het lesgeven en uitleggen gaat op een zeer rustig tempo en het sociale aspect is erg belangrijk, koffie en een praatje.
                Wie zoeken wij:
                Een vrijwilliger met redelijke kennis van één of meerdere van de drie besturingssystemen Windows, Android of iOS. Dit hoeft geen vergevorderde kennis te zijn.
                Ruime basiskennis is voldoende. Hij/zij kan zich aan ons verbinden voor één of meerdere lessen of inlopen per week (van ongeveer 1,5 uur),
                met uitzondering van de zomer- en kerstvakantie (juni/juli/aug respectievelijk medio december-medio januari).
                Daarnaast is geduld met ouderen een belangrijke eigenschap zodat er op een rustige wijze, met een aan de behoefte van de deelnemers aangepaste snelheid, les kan worden gegeven. Technische kennis van computers is niet vereist',
        7);

INSERT INTO replies (id, text, timestamp, user_id, request_id)
VALUES (1,
        'Hallo, ik ben bereid om de maaltijden te bezorgen aan de ouderen in de wijk u kunt me bereiken via mijn mail RVRiper@gmail.com',
        timestamp '2022-10-9 15:45', 4, 1),
       (2, 'Hoi, Ik ben Annie en ik kan u wel helpen met het ophalen en brengen van mensen. Ik ben beschikbaar op donderdag wanneer de vraag het grootst is.',
        timestamp '2022-10-7 12:01', 2, 2),
       (3,
        'Mijn naam is Yusuf en ik kan wel een uurtje in de week langskomen om een uitleg te geven. Met vriendelijke groet, Yusuf',
        timestamp '2022-10-5 17:04', 6, 3),
       (4,
        'Hallo, als u het goed vind kan ik per week een keer langskomen om de maaltijden rond te brengen, Groetjes, Christiaan',
        timestamp '2022-11-6 12:30', 8, 1);


