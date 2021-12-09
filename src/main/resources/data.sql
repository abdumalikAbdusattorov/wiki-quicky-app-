insert into role(name)
values ('ROLE_ADMIN'),
       ('ROLE_SUPER_ADMIN'),
       ('ROLE_MODERATOR'),
       ('ROLE_CLIENT'),
       ('ROLE_WORKER');

insert into attachment_type(created_at, updated_at, content_types, size, width, height, type)
values (now()::timestamp, now()::timestamp, 'image/jpeg, image/jpg, image/png', 2000000, 5000, 5000, 'VEHICLE_PHOTO'),
       (now()::timestamp, now()::timestamp, 'image/jpeg, image/jpg, image/png', 2000000, 5000, 5000, 'USER_AVATAR'),
       (now()::timestamp, now()::timestamp, 'image/jpeg, image/jpg, image/png', 2000000, 5000, 5000, 'USER_PASSPORTPHOTOS'),
       (now()::timestamp, now()::timestamp, 'image/jpeg, image/jpg, image/png', 2000000, 5000, 5000, 'TEXPASSPORT'),
       (now()::timestamp, now()::timestamp, 'image/jpeg, image/jpg, image/png', 2000000, 5000, 5000, 'LICENSE');

insert into vehicle_type(created_at, updated_at, name)
values (now()::timestamp, now()::timestamp, 'car'),
       (now()::timestamp, now()::timestamp, 'bike'),
       (now()::timestamp, now()::timestamp, 'onFoot'),
       (now()::timestamp, now()::timestamp, 'scooter'),
       (now()::timestamp, now()::timestamp, 'labo');

insert into load_type (name) values
    ('Конверт'),
    ('Маленькая'),
    ('Средний'),
    ('Большой');

insert into company (name,lan,lat,is_paid,per_km_price) values
('Wiki-Quicky',41.29960823084341, 69.27053085652375, true ,1500),
('TBS',41.33662679202948, 69.28220364105928, true ,1500);

insert into super_admin_value(created_at, updated_at, name, value)
values (now()::timestamp, now()::timestamp, 'profitPercent', 5),
       (now()::timestamp, now()::timestamp, 'costForMinDistanceFoot', 1500),
       (now()::timestamp, now()::timestamp, 'costForMinDistanceBike', 2500),
       (now()::timestamp, now()::timestamp, 'costForMinDistanceScooter', 3500),
       (now()::timestamp, now()::timestamp, 'costForMinDistanceCar', 5500),
       (now()::timestamp, now()::timestamp, 'costForMinDistanceLabo', 6500),
       (now()::timestamp, now()::timestamp, 'costForPerKMFoot', 500),
       (now()::timestamp, now()::timestamp, 'costForPerKMBike', 600),
       (now()::timestamp, now()::timestamp, 'costForPerKMScooter', 700),
       (now()::timestamp, now()::timestamp, 'costForPerKMCar', 800),
       (now()::timestamp, now()::timestamp, 'costForPerKMLabo', 900),
       (now()::timestamp, now()::timestamp, 'maxRadius', 6),
       (now()::timestamp, now()::timestamp, 'trafficTime', 800),
       (now()::timestamp, now()::timestamp, 'doorToDoor', 1),
       (now()::timestamp, now()::timestamp, 'night1', 17),
       (now()::timestamp, now()::timestamp, 'night2', 20),
       (now()::timestamp, now()::timestamp, 'morning1', 7),
       (now()::timestamp, now()::timestamp, 'morning2', 9);

--To'lov turlarini qo'shish
insert into pay_type(name, created_at, updated_at)
values ('Cash', now()::timestamp, now()::timestamp),
       ('Card', now()::timestamp, now()::timestamp);
insert into worker_test(id,lan,lat)
values (1,41.29945235417611, 69.27261494911187),(2,41.30160294180423, 69.2668288582504),(3,41.304069006374526, 69.27062751325379),(4,41.30536660200013, 69.2679430307023),
       (5,41.30045112600198, 69.2884426797824),(6,41.29973821186082, 69.28756964232005),(7,41.30427221276297, 69.29155524916997),(8,41.29751386975358, 69.27618219417738),
       (9,41.307437271962556, 69.26915993360318),(10,41.30695254314982, 69.26836281223319),(11,41.30820712796431, 69.27826989211731),(12,41.301905446722905, 69.27231046092265),

       (13,41.30057473602181, 69.25255894093031),(14,41.302192006207875, 69.25415012420126),(15,41.300171830014534, 69.26980949079365),(16,41.29856192360306, 69.27137037508672),
       (17,41.29806503085837, 69.26726974672304),(18,41.29923769156608, 69.26679354472581),(19,41.30005257896327, 69.26597341906393),(20,41.296872472922395, 69.27182012136325),
       (21,41.29907868795998, 69.27433340968197),(22,41.299496071599265, 69.26533848306761),(23,41.301304703170075, 69.27195239969582),(24,41.30114570460334, 69.27298417068981);

--Karta turlari
-- insert into card_type(name, created_at, updated_at) values ('Uzcard', now()::timestamp, now()::timestamp), ('Humo', now()::timestamp, now()::timestamp)


