package com.ulutman.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum Metro {
        БульварРокоссовкого,
        Черкизовская,
        ПреображенскаяПлощадь,
        Сокольники,
        Красносельская,
        Комсомольская,
        КрасныеВорота,
        ЧистыеПруды,
        Лублянка,
        ОхотныйРяд,
        БиблиотекаИмениЛенина,
        Кропотинская,
        ПаркКультуры,
        Фрузенская,
        Спортивная,
        ВоробьевыГоры,
        Университет,
        ПроспектВернадского,
        ЮгоЗападная,
        Тропарево,
        Румянцево,
        Саларьева,
        ФилатовЛуг,
        Прокшино,
        Ольховая,
        Коммунарко,
        Ховрино,
        Беломорская,
        РечнойВокзал,
        ВодныйСтадион,
        Войковская,
        Сокол,
        Аэропорт,
        Динамо,
        Белорусская,
        Маяковская,
        Тверская,
        Театральная,
        Новокузнецкая,
        Павелецкая,
        Автозаводская,
        Технопарк,
        Коломенская,
        Каширская,
        Кантемировская,
        Царицыно,
        Орехово,
        Домодедовская,
        Красногвардейская,
        АлмаАтинская,
        ПятницкоеШоссе,
        Митино,
        Волоколамская,
        Мякинино,
        Строгино,
        Крылатское,
        Молодежная,
        Кунцевская,
        СлавянскийБульвар,
        ПаркПобеды,
        Киевкая,
        Смоленская,
        Арбатская,
        ПлощадьРеволюции,
        Курская,
        Бауманская,
        Электрозаводская,
        Семеновская,
        Партизанская,
        Изайловская,
        Первомайская,
        Щёлковская,
        КунцевскаяЧерноСиний,
        Пионерская,
        ФилёвскийПарк,
        Багратионовская,
        Фили,
        Кутузовская,
        Студенческая,
        МоскваСити,
        ДеловойЦентр,
        Киевская,
        СмоленскаяСиний,
        АрбатскаяСиний,
        АлександровскийСад,
        ПаркКультурыКоричневый,
        Октябрская,
        Добрынинская,
        ПавелецкаяКоричневый,
        Таганская,
        КурскаяКоричневый,
        КомсомольскаяКоричневый,
        ПроспектМира,
        Новослободская,
        БелорусскаяКоричневый,
        Краспросненская,
        КиевскаяКоричневый,
        Медведкова,
        Бабушкинская,
        Свиблово,
        БотаническийСад,
        ВДНХ,
        Алексеевкая,
        Рижская,
        ПроспектМираОранжевый,
        Сухаревкая,
        Тургеневкая,
        КитайГород,
        Третьяковская,
        Октябрьская,
        Шаболовская,
        ЛенинскийПроспект,
        Академическая,
        Просоюзная,
        НовыеЧеремушки,
        Калужская,
        Беляево,
        Коньково,
        ТеплыйСтан,
        Ясеново,
        Новоясеневская,
        Планерная,
        Сходненская,
        Тушинская,
        Спартак,
        Щукинская,
        ОктябрьскоеПоле,
        Полежаевкая,
        Беговая,
        УлицаТысячидведсотпять,
        Баррикадная,
        Пушинская,
        КузнецкийМост,
        КитайГородФиолетовая,
        ТаганскаяФиолетовая,
        ПролетарскаяФиолетовая,
        ВолгоградскийПроспек,
        Текстильщики,
        Кузьминки,
        РязанскийПроспект,
        Выхино,
        ЛермонтовскийПроспект,
        Жулебино,
        Котельник,
        ТретьяковскаяЖелтая,
        Марксистская,
        ПлощадьИльича,
        Авиамоторная,
        ШоссеЭнтузиастов,
        Перова,
        Новогиреева,
        Новокосино,
        ДеловойЦентрЖелтая,
        ПаркПобедыЖелтая,
        Минская,
        ЛомоносовкийПроспект,
        Раменки,
        МичуринскийПроспект,
        Озерная,
        Говорово,
        Солнцево,
        БоровскоеШоссе,
        Новопеределкино,
        Рассказовка,
        Пыхтино,
        АэропортВнукова,
        Алтуфьева,
        Бибирево,
        Отрадное,
        Владыкино,
        ПетровскоРазумовская,
        Тимирязевская,
        Дмитровская,
        Савёловская,
        Менделеевская,
        ЦветнойБульвар,
        Чеховская,
        Боровицкая,
        Полянка,
        Серпуховская,
        Тульская,
        Нагатинская,
        Нагорная,
        НахимовскийПроспект,
        Севастопольская,
        Чертановская,
        Южная,
        Пражская,
        УлицаАкадемикаЯнгеля,
        Аннино,
        БульварДмитрияДонского,
        Физтех,
        Лианозово,
        Яхромская,
        Селигерская,
        ВерхниеЛихоборы,
        Окружная,
        ПетровскоРазумовскаяЛюблинскоДмитровской,
        Фонвизинская,
        Бутырская,
        МарьинаРоща,
        Достоевская,
        Трубная,
        СретенскийБульвар,
        Чкаловская,
        Римская,
        КрестьянскаяЗастава,
        Дубровка,
        Кожуховская,
        Печатники,
        Волжская,
        Люблино,
        Братиславская,
        Марьино,
        Борисово,
        Шипиловская,
        Зябликово,
        Шелепиха,
        ДеловойЦентрБольшаяКольцевая,
        СавёловскаяБольшаяКольцевая,
        ПетровскийПарк,
        ЦСКА,
        Хорошёвская,
        БольшаяКольцеваяЛинияНародноеОполчение,
        Мнёвники,
        Терехово,
        КунцевскаяБольшаяКольцевая,
        Давыдково,
        Аминьевская,
        МичуринскийПроспектБольшаяКольцевая,
        ПроспектВернадскогоБольшаяКольцевая,
        Новаторская,
        Воронцовская,
        Зюзино,
        Каховская,
        Варшавская,
        КаширскаяБольшаяКольцевая,
        КленовыйБульвар,
        НагатинскийЗатон,
        ПечатникиБольшаяКольцевая,
        ТекстильщикиБольшаяКольцевая,
        Нижегородская,
        АвиамоторнаяБольшаяКольцевая,
        Лефортово,
        ЭлектрозаводскаяБольшаяКольцевая,
        СокольникиБольшаяКольцевая,
        РижскаяБольшаяКольцевая,
        МарьинаРощаБольшаяКольцевая,
        БитцевскийПарк,
        Лесопарковая,
        УлицаСтарокачаловская,
        УлицаСкобелевская,
        БульварАдмиралаУшакова,
        УлицаГорчакова,
        БунинскаяАллея,
        НижегородскаяБутовской,
        Стахановская,
        Окская,
        ЮгоВосточная,
        Косино,
        УлицаДмитриевского,
        Лухмановская,
        Некрасовка,
        Другое;

        public static Optional<Metro> getById(int id) {
                return Stream.of(values()).filter(metro -> metro.ordinal() == id).findFirst();
        }

        public String getValue() {
                return this.name();
        }

        public String getLabel() {

                return this.name().replaceAll("([А-Я])", " $1").trim();
        }

        public static String formatMetroName(Metro metro) {
                String name = metro.name();
                StringBuilder formattedName = new StringBuilder();
                for (int i = 0; i < name.length(); i++) {
                        char c = name.charAt(i);
                        if (i > 0 && Character.isUpperCase(c)) {
                                formattedName.append(" ");
                        }
                        formattedName.append(c);
                }
                return formattedName.toString();
        }
    }




