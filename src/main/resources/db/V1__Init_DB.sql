-- Hibernate
-- :
--
-- CREATE TABLE my_schema.users (
--                                  id BIGINT GENERATED BY DEFAULT AS IDENTITY,
--                                  confirm_password VARCHAR(255),
--                                  create_date DATE,
--                                  email VARCHAR(255),
--                                  name VARCHAR(255),
--                                  password VARCHAR(255),
--                                  user_id BIGINT, -- Изменено с `user` на `user_id`
--                                  role VARCHAR(255),
--                                  status VARCHAR(255),
--                                  PRIMARY KEY (id)
-- );
-- create table my_schema.comments
-- (
--     create_date      date,
--     id               bigint generated by default as identity,
--     user_id          bigint,
--     content          varchar(255),
--     moderator_status varchar(255) check (moderator_status in ('ОЖИДАЕТ', 'ОДОБРЕН', 'ОТКЛОНЕН')),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.complaints
-- (
--     create_date       date,
--     id                bigint generated by default as identity,
--     user_id           bigint,
--     complaint_content varchar(255),
--     complaint_status  varchar(255) check (complaint_status in ('ОЖИДАЕТ', 'РЕШЕНО', 'ОТКЛОНЕН')),
--     complaint_type    varchar(255) check (complaint_type in
--                                           ('СПАМ', 'ЖАЛОБЫ_НА_ПУБЛИКАЦИИ', 'МОШЕННИЧЕСТВО', 'ДРУГОЕ')),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.mailing
-- (
--     create_date          date,
--     promotion_end_date   date,
--     promotion_start_date date,
--     id                   bigint generated by default as identity,
--     image                varchar(255),
--     mailing_status       varchar(255) check (mailing_status in ('ОШИБКА', 'ОТПРАВЛЕНО')),
--     mailing_type         varchar(255) check (mailing_type in ('НОВОСТИ', 'АКЦИИ', 'ПОЗДРАВЛЕНИЯ')),
--     message              varchar(255),
--     title                varchar(255),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.messages
-- (
--     create_date      date,
--     id               bigint generated by default as identity,
--     user_account_id  bigint,
--     user_id          bigint,
--     content          varchar(255),
--     moderator_status varchar(255) check (moderator_status in ('ОЖИДАЕТ', 'ОДОБРЕН', 'ОТКЛОНЕН')),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.my_publishes
-- (
--     id              bigint generated by default as identity,
--     user_account_id bigint unique,
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.payments
-- (
--     month_date  integer not null,
--     year_date   integer not null,
--     id          bigint generated by default as identity,
--     user_id     bigint,
--     card_number varchar(255),
--     cvc         varchar(255),
--     user_name   varchar(255),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.publishes
-- (
--     create_date    date,
--     id             bigint generated by default as identity,
--     payment_id     bigint,
--     user_id        bigint,
--     address        varchar(255),
--     bank           varchar(255) check (bank in ('SBERBANK', 'TINKPOFF', 'ALFABANK', 'VTB', 'MTS')),
--     category       varchar(255) check (category in
--                                        ('WORK', 'RENT', 'SELL', 'HOTEL', 'SERVICES', 'AUTO', 'REAL_ESTATE')),
--     description    varchar(255),
--     image          varchar(255),
--     metro          varchar(255) check (metro in
--                                        ('БульварРокоссовкого', 'Черкизовская', 'ПреображенскаяПлощадь', 'Сокольники',
--                                         'Красносельская', 'Комсомольская', 'КрасныеВорота', 'ЧистыеПруды', 'Лублянка',
--                                         'ОхотныйРяд', 'БиблиотекаИмениЛенина', 'Кропотинская', 'ПаркКультуры',
--                                         'Фрузенская', 'Спортивная', 'ВоробьевыГоры', 'Университет',
--                                         'ПроспектВернадского', 'ЮгоЗападная', 'Тропарево', 'Румянцево', 'Саларьева',
--                                         'ФилатовЛуг', 'Прокшино', 'Ольховая', 'Коммунарко', 'Ховрино', 'Беломорская',
--                                         'РечнойВокзал', 'ВодныйСтадион', 'Войковская', 'Сокол', 'Аэропорт', 'Динамо',
--                                         'Белорусская', 'Маяковская', 'Тверская', 'Театральная', 'Новокузнецкая',
--                                         'Павелецкая', 'Автозаводская', 'Технопарк', 'Коломенская', 'Каширская',
--                                         'Кантемировская', 'Царицыно', 'Орехово', 'Домодедовская', 'Красногвардейская',
--                                         'АлмаАтинская', 'ПятницкоеШоссе', 'Митино', 'Волоколамская', 'Мякинино',
--                                         'Строгино', 'Крылатское', 'Молодежная', 'Кунцевская', 'СлавянскийБульвар',
--                                         'ПаркПобеды', 'Киевкая', 'Смоленская', 'Арбатская', 'ПлощадьРеволюции',
--                                         'Курская', 'Бауманская', 'Электрозаводская', 'Семеновская', 'Партизанская',
--                                         'Изайловская', 'Первомайская', 'Щёлковская', 'КунцевскаяЧерноСиний',
--                                         'Пионерская', 'ФилёвскийПарк', 'Багратионовская', 'Фили', 'Кутузовская',
--                                         'Студенческая', 'МоскваСити', 'ДеловойЦентр', 'Киевская', 'СмоленскаяСиний',
--                                         'АрбатскаяСиний', 'АлександровскийСад', 'ПаркКультурыКоричневый', 'Октябрская',
--                                         'Добрынинская', 'ПавелецкаяКоричневый', 'Таганская', 'КурскаяКоричневый',
--                                         'КомсомольскаяКоричневый', 'ПроспектМира', 'Новослободская',
--                                         'БелорусскаяКоричневый', 'Краспросненская', 'КиевскаяКоричневый', 'Медведкова',
--                                         'Бабушкинская', 'Свиблово', 'БотаническийСад', 'ВДНХ', 'Алексеевкая', 'Рижская',
--                                         'ПроспектМираОранжевый', 'Сухаревкая', 'Тургеневкая', 'КитайГород',
--                                         'Третьяковская', 'Октябрьская', 'Шаболовская', 'ЛенинскийПроспект',
--                                         'Академическая', 'Просоюзная', 'НовыеЧеремушки', 'Калужская', 'Беляево',
--                                         'Коньково', 'ТеплыйСтан', 'Ясеново', 'Новоясеневская', 'Планерная',
--                                         'Сходненская', 'Тушинская', 'Спартак', 'Щукинская', 'ОктябрьскоеПоле',
--                                         'Полежаевкая', 'Беговая', 'УлицаТысячидведсотпять', 'Баррикадная', 'Пушинская',
--                                         'КузнецкийМост', 'КитайГородФиолетовая', 'ТаганскаяФиолетовая',
--                                         'ПролетарскаяФиолетовая', 'ВолгоградскийПроспек', 'Текстильщики', 'Кузьминки',
--                                         'РязанскийПроспект', 'Выхино', 'ЛермонтовскийПроспект', 'Жулебино', 'Котельник',
--                                         'ТретьяковскаяЖелтая', 'Марксистская', 'ПлощадьИльича', 'Авиамоторная',
--                                         'ШоссеЭнтузиастов', 'Перова', 'Новогиреева', 'Новокосино', 'ДеловойЦентрЖелтая',
--                                         'ПаркПобедыЖелтая', 'Минская', 'ЛомоносовкийПроспект', 'Раменки',
--                                         'МичуринскийПроспект', 'Озерная', 'Говорово', 'Солнцево', 'БоровскоеШоссе',
--                                         'Новопеределкино', 'Рассказовка', 'Пыхтино', 'АэропортВнукова', 'Алтуфьева',
--                                         'Бибирево', 'Отрадное', 'Владыкино', 'ПетровскоРазумовская', 'Тимирязевская',
--                                         'Дмитровская', 'Савёловская', 'Менделеевская', 'ЦветнойБульвар', 'Чеховская',
--                                         'Боровицкая', 'Полянка', 'Серпуховская', 'Тульская', 'Нагатинская', 'Нагорная',
--                                         'НахимовскийПроспект', 'Севастопольская', 'Чертановская', 'Южная', 'Пражская',
--                                         'УлицаАкадемикаЯнгеля', 'Аннино', 'БульварДмитрияДонского', 'Физтех',
--                                         'Лианозово', 'Яхромская', 'Селигерская', 'ВерхниеЛихоборы', 'Окружная',
--                                         'ПетровскоРазумовскаяЛюблинскоДмитровской', 'Фонвизинская', 'Бутырская',
--                                         'МарьинаРоща', 'Достоевская', 'Трубная', 'СретенскийБульвар', 'Чкаловская',
--                                         'Римская', 'КрестьянскаяЗастава', 'Дубровка', 'Кожуховская', 'Печатники',
--                                         'Волжская', 'Люблино', 'Братиславская', 'Марьино', 'Борисово', 'Шипиловская',
--                                         'Зябликово', 'Шелепиха', 'ДеловойЦентрБольшаяКольцевая',
--                                         'СавёловскаяБольшаяКольцевая', 'ПетровскийПарк', 'ЦСКА', 'Хорошёвская',
--                                         'БольшаяКольцеваяЛинияНародноеОполчение', 'Мнёвники', 'Терехово',
--                                         'КунцевскаяБольшаяКольцевая', 'Давыдково', 'Аминьевская',
--                                         'МичуринскийПроспектБольшаяКольцевая', 'ПроспектВернадскогоБольшаяКольцевая',
--                                         'Новаторская', 'Воронцовская', 'Зюзино', 'Каховская', 'Варшавская',
--                                         'КаширскаяБольшаяКольцевая', 'КленовыйБульвар', 'НагатинскийЗатон',
--                                         'ПечатникиБольшаяКольцевая', 'ТекстильщикиБольшаяКольцевая', 'Нижегородская',
--                                         'АвиамоторнаяБольшаяКольцевая', 'Лефортово', 'ЭлектрозаводскаяБольшаяКольцевая',
--                                         'СокольникиБольшаяКольцевая', 'РижскаяБольшаяКольцевая',
--                                         'МарьинаРощаБольшаяКольцевая', 'БитцевскийПарк', 'Лесопарковая',
--                                         'УлицаСтарокачаловская', 'УлицаСкобелевская', 'БульварАдмиралаУшакова',
--                                         'УлицаГорчакова', 'БунинскаяАллея', 'НижегородскаяБутовской', 'Стахановская',
--                                         'Окская', 'ЮгоВосточная', 'Косино', 'УлицаДмитриевского', 'Лухмановская',
--                                         'Некрасовка', 'Другое')),
--     phone          varchar(255),
--     publish_status varchar(255) check (publish_status in ('ОДОБРЕН', 'ОТКЛОНЕН', 'ОЖИДАЕТ')),
--     sub_category   varchar(255) check (sub_category in
--                                        ('SaleOfCar', 'RentOfCar', 'DailyRent', 'LongTermRent', 'PartTime', 'FullTime',
--                                         'House', 'Apartment', 'PartOfLand', 'Space', 'I_RentRoom', 'I_RentBed',
--                                         'I_RenApartment', 'RentRoom', 'RentBed', 'RentApartment', 'RentOffice',
--                                         'Clothes', 'HouseAppliances', 'Electronics', 'MEDICAL', 'LEGAL', 'BEAUTY',
--                                         'AIRTICKET', 'TAXIANDTRACK', 'REPAIR', 'DIFFERENT')),
--     title          varchar(255),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.user_accounts
-- (
--     id      bigint generated by default as identity,
--     user_id bigint unique,
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.users
-- (
--     create_date      date,
--     id               bigint generated by default as identity,
--     confirm_password varchar(255),
--     email            varchar(255) unique,
--     name             varchar(255),
--     password         varchar(255),
--     role             varchar(255) check (role in ('ADMIN', 'USER')),
--     status           varchar(255) check (status in ('АКТИВНЫЙ', 'ЗАБЛОКИРОВАН')),
--     primary key (id)
-- ) Hibernate:
-- create table my_schema.users_publishes
-- (
--     publishes_id bigint not null unique,
--     user_id      bigint not null
-- ) Hibernate:
-- alter table if exists my_schema.comments add constraint FK8omq0tc18jd43bu5tjh6jvraq foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.complaints add constraint FK83j5gqkd7ku4vc908g4rtmglr foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.messages add constraint FKpsmh6clh3csorw43eaodlqvkn foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.messages add constraint FKbvy906q6u6urgr1ba8k9vmdtg foreign key (user_account_id) references my_schema.user_accounts
--     Hibernate:
-- alter table if exists my_schema.my_publishes add constraint FKf38b0f63dr0ee2pyc4iqfiewh foreign key (user_account_id) references my_schema.user_accounts
--     Hibernate:
-- alter table if exists my_schema.payments add constraint FKj94hgy9v5fw1munb90tar2eje foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.publishes add constraint FKqrblppor8it6q59lcbt7jtgvn foreign key (payment_id) references my_schema.payments
--     Hibernate:
-- alter table if exists my_schema.publishes add constraint FKhubg3219bdu3sy2iykm2p147c foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.user_accounts add constraint FKeu175seh3s7swirv0s1ugieyu foreign key (user_id) references my_schema.users
--     Hibernate:
-- alter table if exists my_schema.users_publishes add constraint FK75q52m5a8i4dvgtw8mpf6xy9d foreign key (publishes_id) references my_schema.publishes
--     Hibernate:
-- alter table if exists my_schema.users_publishes add constraint FKmgvierus0pvkdc5xsfg22dfg7 foreign key (user_id) references my_schema.users
--
-- create table my_schema.mailing_user (
--                               user_id BIGINT NOT NULL,
--                               mailing_id BIGINT NOT NULL,
--                               PRIMARY KEY (user_id, mailing_id),
--                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--                               FOREIGN KEY (mailing_id) REFERENCES mailings(id) ON DELETE CASCADE
-- );

