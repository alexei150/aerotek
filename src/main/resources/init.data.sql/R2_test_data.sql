-- Юзеры
insert into users (email, password, first_name, last_name, patronymic, company, phone_number, role, status, created_date, last_modified_date, employee) values
('user@gmail.com', '$2y$12$RYgP8rYxaS4vJm3IcgEifeglwMT6Pw52QwzeZkGML0KKb3xxgvLcF', 'usver', 'usverov', 'usverovich', 'swecor', '89169876543', 'USER', 'ACTIVE', now(), now(), 1),
('test2@gmail.com', '$2a$12$TdsEt7.flfo4Gz75h68toeXt7der0qcG/w/dHS72FR4A/wS1AejlS', 'test', 'testov', 'testovich', 'testovskaya', '89169876541', 'USER', 'ACTIVE', now(), now(), 1),
('editor@gmail.com', '$2a$12$OEE0j.LtVc034ujDtR2r.OdmhGfZKHxHAREGY1vF66YVR27reOsnC', 'Chepuh', 'Shepuhovich', 'Shepchukov', 'swecor', '89169876543', 'USER', 'ACTIVE', now(), now(), 2);

-- Сотрудники
insert into employees (email, password, first_name, last_name, patronymic, company, phone_number, role, status, created_date, last_modified_date, users) values
('admin@gmail.com', '$2y$12$RYgP8rYxaS4vJm3IcgEifeglwMT6Pw52QwzeZkGML0KKb3xxgvLcC', 'admin', 'adminov', 'adminovich', 'swecor', '89169876543', 'ADMIN', 'ACTIVE', now(), now(), 1),
('Tech@gmail.com', '$2a$12$OEE0o.LtVc034ujDtR2r.OdmhGfZKHxHAREGY1vF66YVR27re9snC', 'Tech', 'Tech', 'Tech', 'swecor', '89169876553', 'COMMERCIAL_DIR', 'ACTIVE', now(), now(), 3);

-- Параметры
insert into parameter (name, min_value, max_value, unit, element_type_id, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Площадь живого сечения', 0, 999,'См²', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Длина секции', 0, 10000,'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Длина', 0, 10000,'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Ширина', 0, 10000,'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Высота', 0, 10000,'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Давление', 0, 10000,'М', null, null, 'SYSTEM', now(), 'SYSTEM', now()),
('Плотность', 0, 10000,'ед/изм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),
('Уровень снижения шума', 0, 10000,'dB', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Диапазон работы', 0, 10000,'°С', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Размер клапана', 0, 10000, null, null, null, 'SYSTEM', now(), 'SYSTEM', now()),--10
('Подогрев клапана', 0, 10000, 'boolean', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Вес', 0, 10000, 'кг', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Опциональное оснащение', 25, 45, 'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Направление выхода воздуха вентилятора', null, null, 'направление', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Класс фильтрующего элемента', null, null, 'класс', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--15
('Размер ячеек фильтров', null, null, 'мм', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Тепловая мощность', null, null, 'кВт', null, null, 'SYSTEM', now(), 'SYSTEM', now()),--
('Количество ступеней регулирования', null, null, 'кВт', null, null, 'SYSTEM', now(), 'SYSTEM', now());--

-- ТИПЫ СЕКЦИЙ

-- Вентилятор свободное колесо
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date)
values ('Вентилятор свободное колесо', 'AVG', 'Какое то ненужное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(1, 1),
(1, 2),
(1, 3);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '19.1.Вентилятор свободное колесо(с4.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '19.2.Вентилятор свободное колесо(с4.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '19.3.Вентилятор свободное колесо(с4.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '19.4.Вентилятор свободное колесо(с4.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '19.5.Вентилятор свободное колесо(с4.2).svg', 'SYSTEM', now(), 'SYSTEM', now());
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(1, null, 1, 2, 3, 4, 5, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(1, 'Направление выхода воздуха вентилятора', null, null, null, false),

(1, 'Потери давления установки', null, null, 'Па', true),
(1, 'Располагаемый напор', null, null, 'Па', true),
(1, 'Давление в рабочей точке', null, null, 'Па', true),
(1, 'Эффективность вентилятора', null, null, '%', true),
(1, 'Скорость работы вентилятора', null, null, 'об/мин', true),
(1, 'Частота работы двигателя', null, null, 'об/мин', true),
(1, 'Индекс вентилятора', null, null, 'Код', true),
(1, 'Потребляемая мощность', null, null, 'кВт', true),
(1, 'Номинальная мощность', null, null, 'кВт', true),--10
(1, 'Рабочий ток / Напряжение', null, null, 'А/В', true),
(1, 'Управляющее напряжение', null, null, 'В', true),
(1, 'Шумовые характеристики всасывания', null, null, 'dB(A)', true),
(1, 'Шумовые характеристики нагнетания', null, null, 'dB(A)', true),
(1, 'Марка двигателя', null, null, '-', true),--15
(1, 'Индекс Двигателя', null, null, 'Код', true),
(1, 'SFP для чистых фильтров', null, null, 'кВт/', true);
insert into default_parameter_dictionary (default_parameter_id, value) values
(1, 'прямо'),
(1, 'вверх'),
(1, 'влево'),
(1, 'вправо'),
(1, 'вниз');

-- Шумоглушитель
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Шумоглушитель', 'AVС', 'Какое то невероятно информативное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 8);

insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '5.1.Шумоглушитель(с1.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '5.2.Шумоглушитель(с1.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '5.2.Шумоглушитель(с1.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '5.4.Шумоглушитель(с1.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '5.4.Шумоглушитель(с1.5).svg', 'SYSTEM', now(), 'SYSTEM', now());
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(2, null, 6, 7, 8, 9, 10, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(2, 'Длина секции', null, null, null, false),--18

(2, 'Скорость воздуха', null, null, 'м/с', true),
(2, 'Потери давления воздуха', null, null, 'Па', true),
(2, 'Уровень снижения шума', null, null, 'dB', true);--21
insert into default_parameter_dictionary (default_parameter_id, value) values
(18, '600мм'),
(18, '1100мм');

-- Воздушный клапан
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Воздушный клапан', 'AВС', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 9),
(3, 10),
(3, 11);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '1.1-Воздушнй клапан(с1.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '1.2-Воздушнй клапан(с1.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '1.3-Воздушнй клапан(с1.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '1.4-Воздушнй клапан(с1.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '1.5-Воздушнй клапан(с1.1).svg', 'SYSTEM', now(), 'SYSTEM', now());
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(3, null, 11, 12, 13, 14, 15, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(3, 'Размер клапана', null, null, null, false),--22
(3, 'Подогрев клапана', null, null, null, false),

(3, 'Потери давления воздуха', null, null, 'Па', true),
(3, 'Диапазон работы', null, null, '°С', true);--25
insert into default_parameter_dictionary (default_parameter_id, value) values
(22, '600мм'),
(22, '1100мм'),
(23, 'Нет'),
(23, 'Да');

-- Пустая секция
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Пустая секция', 'AВV', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(4, 1),
(4, 2),
(4, 3),
(4, 4),
(4, 5),
(4, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '4.1-Пустая секция(с1.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '4.2-Пустая секция(с1.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '4.2-Пустая секция(с1.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '4.4-Пустая секция(с1.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '4.4-Пустая секция(с1.4).svg', 'SYSTEM', now(), 'SYSTEM', now());
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(4, null, 16, 17, 18, 19, 20, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(4, 'Длина секции', null, null, 'м/с', false),--26

(4, 'Скорость воздуха', null, null, 'м/с', true),
(4, 'Потери давления воздуха', null, null, 'Па', true);--28
insert into default_parameter_dictionary (default_parameter_id, value) values
(26, '300мм'),
(26, '500мм'),
(26, '800мм'),
(26, '1000мм');

-- Карманный фильтр
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Карманный фильтр', 'ВVС', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(5, 1),
(5, 2),
(5, 3),
(5, 4),
(5, 5),
(5, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '8.1-Карманный фильтр(с1.8).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '8.2-Карманный фильтр(с1.8).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '8.3-Карманный фильтр(с1.8).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '8.4-Карманный фильтр(с1.8).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '8.5-Карманный фильтр(с1.8).svg', 'SYSTEM', now(), 'SYSTEM', now());--25
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(5, null, 21, 22, 23, 24, 25, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(5, 'Класс фильтрующего элемента', null, null, 'класс', false),--29

(5, 'Скорость воздуха', null, null, 'м/с', true),
(5, 'Потери давления воздуха', null, null, 'Па', true),
(5, 'Размер ячеек фильтров', null, null, 'Па', true);--32
insert into default_parameter_dictionary (default_parameter_id, value) values
(29, 'EU-5'),
(29, 'EU-7'),
(29, 'EU-9');

-- Кассетный фильтр
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Кассетный фильтр', 'ВVO', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(6, 1),
(6, 2),
(6, 3),
(6, 4),
(6, 5),
(6, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '2.1-Кассетный фильтр(с1.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '2.2-Кассетный фильтр(с1.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '2.3-Кассетный фильтр(с1.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '2.4-Кассетный фильтр(с1.2).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '2.5-Кассетный фильтр(с1.2).svg', 'SYSTEM', now(), 'SYSTEM', now());--30
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(6, null, 26, 27, 28, 29, 30, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(6, 'Класс фильтрующего элемента', null, null, 'класс', false),--33

(6, 'Скорость воздуха', null, null, 'м/с', true),
(6, 'Потери давления воздуха', null, null, 'Па', true),
(6, 'Размер ячеек фильтров', null, null, 'Па', true);--36
insert into default_parameter_dictionary (default_parameter_id, value) values
(33, 'EU-4');

-- Водяной нагреватель
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Водяной нагреватель', 'ВBO', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(7, 1),
(7, 2),
(7, 3),
(7, 4),
(7, 5),
(7, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '10.1-Водяной нагреватель(с2.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '10.2-Водяной нагреватель(с2.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '10.2-Водяной нагреватель(с2.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '10.4-Водяной нагреватель(с2.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '10.4-Водяной нагреватель(с2.1).svg', 'SYSTEM', now(), 'SYSTEM', now());--35
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(7, null, 31, 32, 33, 34, 35, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(7, 'Тип теплоносителя', null, null, null, false),--37
(7, '%-содержание теплоносителя в воде', 1, 100, '%', false),
(7, 'Температура воздуха на входе', 1, 100, '°С', false),
(7, 'Желаемая температура воздуха на выходе', 1, 100, '°С', false),
(7, 'Относительная влажность воздуха на входе', 1, 100, '%', false),
(7, 'Температура теплоносителя на входе', 1, 100, '°С', false),
(7, 'Температура теплоносителя на выходе', 1, 100, '°С', false),
(7, 'Выбор производителя теплообменников', null, null, null, false),--44

(7, 'Температура воздуха на входе ', 1, 100, '°С', false),
(7, 'Относительная влажность воздуха на входе ', 1, 100, '%', false),
(7, 'Температура теплоносителя на входе ', 1, 100, '°С', false),
(7, 'Температура теплоносителя на выходе ', 1, 100, '°С', false),--48


(7, 'Индекс теплообменника', null, null, null, true),
(7, 'Скорость воздуха', null, null, 'м/с', true),
(7, 'Потери давления воздуха', null, null, 'Па', true),
(7, 'Температура воздуха на выходе', 1, 100, '°С', true),
(7, 'Относительная влажность воздуха на выходе', 1, 100, '%', true),
(7, 'Температура теплоносителя на выходе', 1, 100, '°С', true),
(7, 'Тепловая мощность', null, null, 'кВт', true),
(7, 'Расход теплоносителя', null, null, 'л/с', true),
(7, 'Скорость теплоносителя в теплообменнике', null, null, 'м/с', true),
(7, 'Потери давления теплоносителя', null, null, 'кПа', true),
(7, 'Диаметры патрубков теплообменника', null, null, '"', true),--59

(7, 'Температура воздуха на выходе ', 1, 100, '°С', true),
(7, 'Относительная влажность воздуха на выходе ', 1, 100, '%', true),
(7, 'Тепловая мощность ', null, null, 'кВт', true),
(7, 'Расход теплоносителя ', null, null, 'л/с', true),
(7, 'Скорость теплоносителя в теплообменнике ', null, null, 'м/с', true),
(7, 'Потери давления теплоносителя ', null, null, 'кПа', true);--65
insert into default_parameter_dictionary (default_parameter_id, value) values
(37, 'Вода'),
(37, 'Этиленгликоль'),
(37, 'Пропиленгликоль'),

(44, 'RoenEst');

--Водяной охладитель
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Водяной охладитель', 'ВZZ', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(8, 1),
(8, 2),
(8, 3),
(8, 4),
(8, 5),
(8, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '13.1-Водяной охладитель(с2.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '13.2-Водяной охладитель(с2.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '13.2-Водяной охладитель(с2.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '13.4-Водяной охладитель(с2.4).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '13.4-Водяной охладитель(с2.4).svg', 'SYSTEM', now(), 'SYSTEM', now());--40
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(8, null, 36, 37, 38, 39, 40, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(8, 'Тип теплоносителя', null, null, null, false),--66
(8, '%-содержание теплоносителя в воде', 1, 100, '%', false),
(8, 'Температура воздуха на входе', 1, 100, '°С', false),
(8, 'Желаемая температура воздуха на выходе', 1, 100, '°С', false),
(8, 'Относительная влажность воздуха на входе', 1, 100, '%', false),
(8, 'Температура теплоносителя на входе', 1, 100, '°С', false),
(8, 'Температура теплоносителя на выходе', 1, 100, '°С', false),
(8, 'Выбор производителя теплообменников', null, null, null, false),
(8, 'Каплеуловитель', null, null, null, false),--74

(8, 'Индекс теплообменника', null, null, null, true),
(8, 'Скорость воздуха', null, null, 'м/с', true),
(8, 'Потери давления воздуха', null, null, 'Па', true),
(8, 'Температура воздуха на выходе', 1, 100, '°С', true),
(8, 'Относительная влажность воздуха на выходе', 1, 100, '%', true),
(8, 'Температура теплоносителя на выходе', 1, 100, '°С', true),
(8, 'Тепловая мощность', null, null, 'кВт', true),
(8, 'Расход теплоносителя', null, null, 'л/с', true),
(8, 'Скорость теплоносителя в теплообменнике', null, null, 'м/с', true),
(8, 'Потери давления теплоносителя', null, null, 'кПа', true),
(8, 'Диаметры патрубков теплообменника', null, null, '"', true);--85

insert into default_parameter_dictionary (default_parameter_id, value) values
(66, 'Вода'),
(66, 'Этиленгликоль'),
(66, 'Пропиленгликоль'),

(73, 'RoenEst'),

(74, 'Да'),
(74, 'Нет');

--Фреоновый охладитель
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Фреоновый охладитель', 'ВOZ', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(9, 1),
(9, 2),
(9, 3),
(9, 4),
(9, 5),
(9, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '14.1-Фреоновй охладитель(с2.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '14.2-Фреоновй охладитель(с2.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '14.2-Фреоновй охладитель(с2.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '14.4-Фреоновй охладитель(с2.5).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '14.4-Фреоновй охладитель(с2.5).svg', 'SYSTEM', now(), 'SYSTEM', now());--45
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(9, null, 41, 42, 43, 44, 45, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(9, 'Марка фреона', null, null, null, false),--86
(9, 'Температура воздуха на входе', 0, 100, '°С', false),
(9, 'Желаемая температура воздуха на выходе', 0, 100, '°С', false),
(9, 'Относительная влажность воздуха на входе', 0, 100, '%', false),
(9, 'Температура кипения хладогента', 0, 100, '°С', false),
(9, 'Выбор производителя теплообменников', null, null, null, false),
(9, 'Каплеуловитель', null, null, null, false),--92

(9, 'Индекс теплообменника', null, null, null, true),
(9, 'Скорость воздуха', null, null, 'м/с', true),
(9, 'Потери давления воздуха', null, null, 'Па', true),
(9, 'Температура воздуха на выходе', 0, 100, '°С', true),
(9, 'Относительная влажность воздуха на выходе', 0, 100, '%', true),
(9, 'Тепловая мощность', null, null, 'кВт', true),
(9, 'Расход теплоносителя', null, null, 'л/с', true),
(9, 'Потери давления хладогента', null, null, 'кПа', true),
(9, 'Диаметры патрубков теплообменника', null, null, '"', true);--101

insert into default_parameter_dictionary (default_parameter_id, value) values
(91, 'RoenEst'),

(86, 'R22'),
(86, 'R123'),
(86, 'R134a'),
(86, 'R152a'),
(86, 'R404A'),
(86, 'R407C'),
(86, 'R410A'),
(86, 'R507A');

--Рекуператор роторный
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Рекуператор роторный', 'ВLZ', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(10, 1),
(10, 2),
(10, 3),
(10, 4),
(10, 5),
(10, 6);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '20.1-Рекуператор роторнй(с5.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),--46
('Название картинки', 'Описание картинки', '20.2+-Рекуператор роторнй(с5.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '20.2+-Рекуператор роторнй(с5.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '20.2+-Рекуператор роторнй(с5.1).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '20.2+-Рекуператор роторнй(с5.1).svg', 'SYSTEM', now(), 'SYSTEM', now());--50
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(10, null, 46, 47, 48, 49, 50, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(10, 'Марка рекуператора', null, null, '°С', false),--102
(10, 'Тип ротора', null, null, '°С', false),
(10, 'Профиль матрицы', null, null, '°С', false),
(10, 'Температура приточного воздуха на входе', 0, 100, '°С', false),
(10, 'Относительная влажность приточного воздуха на входе', 0, 100, '%', false),
(10, 'Температура вытяжного воздуха на входе', 0, 100, '°С', false),
(10, 'Относительная влажность вытяжного воздуха на входе', 0, 100, '%', false),--108

(10, 'Температура приточного воздуха на входе ', 0, 100, '°С', false),
(10, 'Относительная влажность приточного воздуха на входе ', 0, 100, '%', false),
(10, 'Температура вытяжного воздуха на входе ', 0, 100, '°С', false),
(10, 'Относительная влажность вытяжного воздуха на входе ', 0, 100, '%', false),--112

(10, 'Индекс рекуператора', null, null, null, true),
(10, 'Скорость воздуха', null, null, 'м/с', true),
(10, 'Потери давления воздуха', null, null, 'Па', true),
(10, 'Температура воздуха на выходе', 0, 100, '°С', true),
(10, 'Относительная влажность воздуха на выходе', 0, 100, '%', true),
(10, 'Тепловая мощность', 0, 100, '%', true),
(10, 'Эффективность рекуператора', null, null, '%', true),
(10, 'Тип частотного регулятора', null, null, 'A/B', true),--120

(10, 'Скорость воздуха ', null, null, 'м/с', true),
(10, 'Потери давления воздуха ', null, null, 'Па', true),
(10, 'Температура воздуха на выходе ', 0, 100, '°С', true),
(10, 'Относительная влажность воздуха на выходе ', 0, 100, '%', true);--124

--Электрический нагреватель
insert into section_type (name, code, note,  created_by, created_date, last_modified_by, last_modified_date) values
('Электрический нагреватель', 'ВSZ', 'Какое то невероятно полезное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_section_type(section_type_id, parameter_id) values
(11, 1),
(11, 2),
(11, 3),
(11, 4),
(11, 5),
(11, 17),
(11, 18);
insert into media_content(name, description, path, created_by, created_date, last_modified_by, last_modified_date) values
('Название картинки', 'Описание картинки', '6.1.Электрический нагреватель(1.6).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '6.2.Электрический нагреватель(1.6).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '6.2.Электрический нагреватель(1.6).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '6.3.Электрический нагреватель(1.6).svg', 'SYSTEM', now(), 'SYSTEM', now()),
('Название картинки', 'Описание картинки', '6.3.Электрический нагреватель(1.6).svg', 'SYSTEM', now(), 'SYSTEM', now());--55
insert into section_type_icon (section_type_id, image_id, icon_base_id, icon_inflow_to_right_id, icon_inflow_to_left_id, icon_outflow_to_right_id, icon_outflow_to_left_id, created_by, created_date, last_modified_by, last_modified_date) values
(11, null, 51, 52, 53, 54, 55, 'SYSTEM', now(), 'SYSTEM', now());
insert into default_parameter (section_type_id, name, min_value, max_value, unit, is_calculating) values
(11, 'Количество ступеней регулирования', null, null, null, false),--125
(11, 'Температура воздуха на входе', 0, 100, '°С', false),
(11, 'Желаемая температура воздуха на выходе', 0, 100, '°С', false),
(11, 'Относительная влажность воздуха на входе', 0, 100, '%', false),--128

(11, 'Температура воздуха на входе ', 0, 100, '°С', false),
(11, 'Желаемая температура воздуха на выходе ', 0, 100, '°С', false),
(11, 'Относительная влажность воздуха на входе ', 0, 100, '%', false),--131

(11, 'Скорость воздуха', null, null, 'м/с', true),
(11, 'Потери давления воздуха', null, null, 'Па', true),
(11, 'Температура воздуха на выходе', 0, 100, '°С', true),
(11, 'Относительная влажность воздуха на выходе', 0, 100, '%', true),
(11, 'Расчетная тепловая мощность', 0, 100, '%', true),
(11, 'Тепловая мощность', null, null, 'кВт', true),--137

(11, 'Температура воздуха на выходе ', 0, 100, '°С', true),
(11, 'Относительная влажность воздуха на выходе ', 0, 100, '%', true),
(11, 'Расчетная тепловая мощность ', 0, 100, '%', true);--140

insert into default_parameter_dictionary (default_parameter_id, value) values
(125, '1'),
(125, '2'),
(125, '3'),
(125, '4'),

(86, 'R22'),
(86, 'R123'),
(86, 'R134a'),
(86, 'R152a'),
(86, 'R404A'),
(86, 'R407C'),
(86, 'R410A'),
(86, 'R507A');

insert into default_parameter_dictionary (default_parameter_id, value) values
(102, 'Klingenburg');


-- Типы элементов
insert into element_type(name, note, created_by, created_date, last_modified_by, last_modified_date) values
('Двигатель', 'Какое то ненужное примечание', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_element_type(element_type_id, parameter_id) values
(1, 1),
(1, 2),
(1, 3);

insert into element_type(name, note, created_by, created_date, last_modified_by, last_modified_date) values
('Ригель', 'Примечание без которого просто не возможно выжить', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_element_type(element_type_id, parameter_id) values
(2, 2),
(2, 3),
(2, 4),
(2, 5);

insert into element_type(name, note, created_by, created_date, last_modified_by, last_modified_date) values
('Болт', 'Примечание без которого просто не возможно выжить', 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_element_type(element_type_id, parameter_id) values (3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6);

-- Элементы
insert into element(name, brand, code, index, cost_price, description, note, drawing_code, element_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Двигатель дизельный', 'Siemens', 'Q1114CX', 'ER31C-2DN.D7.1R', 1000000.25, 'Невероятно важное описание', 'Примечание которое чем то отличается от описания', '1112.2234.789560', 1, 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(element_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(1, 1, 1000, 'SYSTEM', now(), 'SYSTEM', now()),
(1, 2, 2, 'SYSTEM', now(), 'SYSTEM', now()),
(1, 3, 24, 'SYSTEM', now(), 'SYSTEM', now());

insert into element(name, brand, code, index, cost_price, description, note, drawing_code, element_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Болт 24mm', 'Рязанский болтоложительный завод', '', '', 0.25, 'Скучное описание болта', 'Зато веселое примечание по заводу', null, 1, 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(element_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(2, 2, 0.000002, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 3, 0.0024, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 4, 0.0006, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 5, 100500, 'SYSTEM', now(), 'SYSTEM', now());

insert into element(name, brand, code, index, cost_price, description, note, drawing_code, element_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Болт 18mm', 'Рязанский болтоложительный завод', '', '', 0.25, 'Скучное описание болта', 'Зато веселое примечание по заводу', null, 1, 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(element_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(3, 1, 0.0003, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 2, 0.0019, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 3, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 4, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 5, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 6, 0.008, 'SYSTEM', now(), 'SYSTEM', now());

insert into element(name, brand, code, index, cost_price, description, note, drawing_code, element_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Двигатель дизельный', 'Siemens', 'Q1114C', 'ER31C-2DN.D7.1R', 1000000.25, 'Невероятно важное описание', 'Примечание которое чем то отличается от описания', '1112.2234.789560', 1, 'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(element_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(4, 1, 1000, 'SYSTEM', now(), 'SYSTEM', now()),
(4, 2, 2, 'SYSTEM', now(), 'SYSTEM', now()),
(4, 3, 24, 'SYSTEM', now(), 'SYSTEM', now());

-- Секции
insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Вентилятор 1', '2Q2WP4R', 'Какое то описание','Интересное примечание',1.3,0.9,0.6,4,1,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(1, 1, 600, 'SYSTEM', now(), 'SYSTEM', now()),
(1, 3, 500, 'SYSTEM', now(), 'SYSTEM', now()),
(1, 4, 600,'SYSTEM', now(), 'SYSTEM', now()),
(1, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(1, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(1, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(1, 12, 35,'SYSTEM', now(), 'SYSTEM', now()),
(1, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(1, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(1, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(1, 1000, 1.1,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 2000, 2.2,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 3000, 3.3,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 4000, 4.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 5000, 5.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 6000, 6.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 7000, 7.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 8000, 8.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 9000, 9.5,  'SYSTEM', now(), 'SYSTEM', now()),
(1, 10000, 10.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(1,1,1,'SYSTEM', now(), 'SYSTEM', now()),
(1,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(1,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(1, 1, 4, 1, 'ER28C-2DN.B7.1R'),
(1, 1, 4, 2, 'ER31C-2DN.C7.1R'),
(1, 1, 4, 3, 'ER31C-2DN.D7.1R'),
(1, 1, 4, 4, 'ER35C-2DN.E7.1R'),
(1, 1, 4, 5, 'ER35C-2DN.F7.1R');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Вентилятор 2', 'PQ2W384R', 'Какое то описание','Интересное примечание',1.3,0.9,0.66,4,1,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(2, 1, 6000, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 3, 550, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 4, 650,'SYSTEM', now(), 'SYSTEM', now()),
(2, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(2, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(2, 12, 45,'SYSTEM', now(), 'SYSTEM', now()),
(2, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(2, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(2, 14, 'прямо','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(2, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(2, 1000, 11.1,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 2000, 12.2,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 3000, 13.3,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 4000, 14.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 5000, 15.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 6000, 16.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 7000, 17.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 8000, 18.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 9000, 19.5,  'SYSTEM', now(), 'SYSTEM', now()),
(2, 10000, 20.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(2,4,1,'SYSTEM', now(), 'SYSTEM', now()),
(2,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(2,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(2, 1, 4, 1, 'ER28C-2DN.B7.1R'),
(2, 1, 4, 2, 'ER31C-2DN.C7.1R'),
(2, 1, 4, 3, 'ER31C-2DN.D7.1R'),
(2, 1, 4, 4, 'ER35C-2DN.E7.1R'),
(2, 1, 4, 5, 'ER35C-2DN.F7.1R');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Шумоглушитель 1', '3Q2W3P4T', 'Какое то описание','Интересное примечание',8.3,0.1,0.1,4,2,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(3, 2, '600мм', 'SYSTEM', now(), 'SYSTEM', now()),
(3, 3, 400, 'SYSTEM', now(), 'SYSTEM', now()),
(3, 4, 300,'SYSTEM', now(), 'SYSTEM', now()),
(3, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(3, 1, 13,'SYSTEM', now(), 'SYSTEM', now()),
(3, 12, 55,'SYSTEM', now(), 'SYSTEM', now()),
(3, 8, -11,'SYSTEM', now(), 'SYSTEM', now()),
(3, 13, 45,'SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(3, -17.9, -15.2, -12.2, -6.2, -5.3, -4.2, -1.1, -0.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(3, 1000, 61.1,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 2000, 62.2,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 3000, 63.3,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 4000, 64.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 5000, 65.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 6000, 66.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 7000, 67.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 8000, 68.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 9000, 69.5,  'SYSTEM', now(), 'SYSTEM', now()),
(3, 10000, 70.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Шумоглушитель 2', '3Q2W3E4Z', 'Какое то описание','Интересное примечание',8.3,0.1,0.7,4,2,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(4, 2, '1100мм', 'SYSTEM', now(), 'SYSTEM', now()),
(4, 3, 450, 'SYSTEM', now(), 'SYSTEM', now()),
(4, 4, 350,'SYSTEM', now(), 'SYSTEM', now()),
(4, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(4, 1, 1563,'SYSTEM', now(), 'SYSTEM', now()),
(4, 12, 55,'SYSTEM', now(), 'SYSTEM', now()),
(4, 8, -22,'SYSTEM', now(), 'SYSTEM', now()),
(4, 13, 25,'SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(4, -7.9, -5.2, -2.2, -1.6, -1.3, -0.8, -0.4, -0.2,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(4, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(4, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Воздушный клапан 1', '4Q2W3E40', 'Какое то описание','Интересное примечание',8.3,0.1,0.14,4,3,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(5, 1, 13,'SYSTEM', now(), 'SYSTEM', now()),
(5, 2, 2000, 'SYSTEM', now(), 'SYSTEM', now()),
(5, 3, 475, 'SYSTEM', now(), 'SYSTEM', now()),
(5, 4, 375,'SYSTEM', now(), 'SYSTEM', now()),
(5, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(5, 9, 15,'SYSTEM', now(), 'SYSTEM', now()),
(5, 10,'600мм','SYSTEM', now(), 'SYSTEM', now()),
(5, 11,'Да','SYSTEM', now(), 'SYSTEM', now()),
(5, 12, 75,'SYSTEM', now(), 'SYSTEM', now()),
(5, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(5, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(5, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(5, 1000, 11.1,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 2000, 22.2,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 3000, 33.3,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 4000, 44.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 5000, 44.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 6000, 44.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 7000, 45.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 8000, 46.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 9000, 47.5,  'SYSTEM', now(), 'SYSTEM', now()),
(5, 10000, 48.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Воздушный клапан 2', '0Q2W3E4T', 'Какое то описание','Интересное примечание',8.3,0.1,0.03,4,3,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(6, 2, 2000, 'SYSTEM', now(), 'SYSTEM', now()),
(6, 3, 575, 'SYSTEM', now(), 'SYSTEM', now()),
(6, 4, 675,'SYSTEM', now(), 'SYSTEM', now()),
(6, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(6, 1, 300,'SYSTEM', now(), 'SYSTEM', now()),
(6, 9, 15,'SYSTEM', now(), 'SYSTEM', now()),
(6, 10,'1100мм','SYSTEM', now(), 'SYSTEM', now()),
(6, 11,'Нет','SYSTEM', now(), 'SYSTEM', now()),
(6, 12, 85,'SYSTEM', now(), 'SYSTEM', now()),
(6, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(6, 14, 'вниз','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(6, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(6, 1000, 31.1,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 2000, 32.2,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 3000, 33.3,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 4000, 34.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 5000, 35.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 6000, 36.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 7000, 37.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 8000, 38.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 9000, 39.5,  'SYSTEM', now(), 'SYSTEM', now()),
(6, 10000, 40.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Пустая секция 1', '102W3E4T', 'Какое то описание','Интересное примечание',8.3,0.1,0.3,4,4,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(7, 1, 10,'SYSTEM', now(), 'SYSTEM', now()),
(7, 2, '300мм', 'SYSTEM', now(), 'SYSTEM', now()),
(7, 3, 525, 'SYSTEM', now(), 'SYSTEM', now()),
(7, 4, 625,'SYSTEM', now(), 'SYSTEM', now()),
(7, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(7, 6, 13,'SYSTEM', now(), 'SYSTEM', now()),
(7, 12, 15,'SYSTEM', now(), 'SYSTEM', now()),
(7, 9, '-20 +50','SYSTEM', now(), 'SYSTEM', now()),
(7, 13, 45,'SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(7, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(7, 1000, 11.1,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 2000, 22.2,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 3000, 33.3,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 4000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 5000, 77.5,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 6000, 88.5,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 7000, 99.15,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 8000, 120.5,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 9000, 140.5,  'SYSTEM', now(), 'SYSTEM', now()),
(7, 10000, 170.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Пустая секция 2', '1Q2W7E4T', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,4,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(8, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(8, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(8, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(8, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(8, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(8, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(8, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(8, 13, 25,'SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(8, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(8, 1000, 11.1,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 2000, 12.2,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 3000, 13.3,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 4000, 14.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 5000, 15.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 6000, 16.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 7000, 17.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 8000, 18.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 9000, 19.5,  'SYSTEM', now(), 'SYSTEM', now()),
(8, 10000, 20.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Карманный фильтр 1', '1Q2W0E4T', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,5,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(9, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(9, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(9, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(9, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(9, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(9, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(9, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(9, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(9, 15, 'EU-5','SYSTEM', now(), 'SYSTEM', now()),
(9, 16, '500х600','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(9, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(9, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(9, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Карманный фильтр 2', '1Q2W0E9T', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,5,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(10, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(10, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(10, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(10, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(10, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(10, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(10, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(10, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(10, 15, 'EU-7','SYSTEM', now(), 'SYSTEM', now()),
(10, 16, '600х800','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(10, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(10, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(10, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Кссетный фильтр 1', '1Q2W0E9X', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,6,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(11, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(11, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(11, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(11, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(11, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(11, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(11, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(11, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(11, 15, 'EU-4','SYSTEM', now(), 'SYSTEM', now()),
(11, 16, '500х300','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(11, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(11, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(11, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Кссетный фильтр 2', '2Q2W0E9X', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,6,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(12, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(12, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(12, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(12, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(12, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(12, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(12, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(12, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(12, 15, 'EU-4','SYSTEM', now(), 'SYSTEM', now()),
(12, 16, '500х300','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(12, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(12, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(12, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Водяной нагреватель 1', '7Q8WP4R', 'Какое то описание','Интересное примечание',1.3,0.9,0.6,4,7,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(13, 1, 600, 'SYSTEM', now(), 'SYSTEM', now()),
(13, 3, 500, 'SYSTEM', now(), 'SYSTEM', now()),
(13, 4, 600,'SYSTEM', now(), 'SYSTEM', now()),
(13, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(13, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(13, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(13, 12, 35,'SYSTEM', now(), 'SYSTEM', now()),
(13, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(13, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(13, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(13, 1000, 1.1,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 2000, 2.2,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 3000, 3.3,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 4000, 4.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 5000, 5.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 6000, 6.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 7000, 7.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 8000, 8.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 9000, 9.5,  'SYSTEM', now(), 'SYSTEM', now()),
(13, 10000, 10.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(13,1,1,'SYSTEM', now(), 'SYSTEM', now()),
(13,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(13,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(13, 7, 4, 1, '6.30.CU.15.AL.18.01.0670.18.W.X.X.009.018.R 1"L'),
(13, 7, 4, 2, '6.30.CU.10.AL.18.02.0670.18.W.X.X.012.036.R 1"L'),
(13, 7, 4, 3, '6.30.CU.10.AL.18.03.0670.25.W.X.X.012.054.R 1"L'),
(13, 7, 4, 4, '6.30.CU.10.AL.18.04.0670.30.W.X.X.018.072.R 1"L');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Водяной охладитель 1', '7Q8WP4Z', 'Какое то описание','Интересное примечание',1.3,0.9,0.6,4,8,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(14, 1, 600, 'SYSTEM', now(), 'SYSTEM', now()),
(14, 3, 500, 'SYSTEM', now(), 'SYSTEM', now()),
(14, 4, 600,'SYSTEM', now(), 'SYSTEM', now()),
(14, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(14, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(14, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(14, 12, 35,'SYSTEM', now(), 'SYSTEM', now()),
(14, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(14, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(14, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(14, 1000, 1.1,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 2000, 2.2,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 3000, 3.3,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 4000, 4.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 5000, 5.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 6000, 6.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 7000, 7.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 8000, 8.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 9000, 9.5,  'SYSTEM', now(), 'SYSTEM', now()),
(14, 10000, 10.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(14,1,1,'SYSTEM', now(), 'SYSTEM', now()),
(14,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(14,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(14, 8, 4, 1, '6.30.CU.10.AL.17.04.0670.30.W.X.X.009.068.R 1"L'),
(14, 8, 4, 2, '6.30.CU.10.AL.17.06.0670.30.W.X.X.012.102.R 1"L'),
(14, 8, 4, 3, '6.30.CU.10.AL.17.08.0670.30.W.X.X.014.136.R 1"L'),
(14, 8, 4, 4, '6.30.CU.10.AL.17.10.0670.30.W.X.X.017.170.R 1"L'),
(14, 8, 4, 4, '6.30.CU.10.AL.17.12.0660.30.W.X.X.018.204.R 1 1/4"L');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Фреоновый охладитель 1', '7Q8MP4Z', 'Какое то описание','Интересное примечание',1.3,0.9,0.6,4,9,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(15, 1, 600, 'SYSTEM', now(), 'SYSTEM', now()),
(15, 3, 500, 'SYSTEM', now(), 'SYSTEM', now()),
(15, 4, 600,'SYSTEM', now(), 'SYSTEM', now()),
(15, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(15, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(15, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(15, 12, 35,'SYSTEM', now(), 'SYSTEM', now()),
(15, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(15, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(15, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(15, 1000, 1.1,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 2000, 2.2,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 3000, 3.3,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 4000, 4.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 5000, 5.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 6000, 6.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 7000, 7.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 8000, 8.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 9000, 9.5,  'SYSTEM', now(), 'SYSTEM', now()),
(15, 10000, 10.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(15,1,1,'SYSTEM', now(), 'SYSTEM', now()),
(15,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(15,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(15, 9, 4, 1, '6.30.CU.10.AL.18.03.0670.30.E.X.X.005.054.R 16/22L'),
(15, 9, 4, 2, '6.30.CU.10.AL.18.04.0670.30.E.X.X.007.072.R 16/22L'),
(15, 9, 4, 3, '6.30.CU.10.AL.18.06.0670.30.E.X.X.009.108.R 22/28L');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date) values
('Рекуператор 1', '7Q8DP4Z', 'Какое то описание','Интересное примечание',1.3,0.9,0.6,4,10,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(16, 1, 600, 'SYSTEM', now(), 'SYSTEM', now()),
(16, 3, 500, 'SYSTEM', now(), 'SYSTEM', now()),
(16, 4, 600,'SYSTEM', now(), 'SYSTEM', now()),
(16, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(16, 2, 0.008, 'SYSTEM', now(), 'SYSTEM', now()),
(16, 6, 15,'SYSTEM', now(), 'SYSTEM', now()),
(16, 12, 35,'SYSTEM', now(), 'SYSTEM', now()),
(16, 13, 45,'SYSTEM', now(), 'SYSTEM', now()),
(16, 14, 'вверх','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(16, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(16, 1000, 1.1,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 2000, 2.2,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 3000, 3.3,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 4000, 4.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 5000, 5.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 6000, 6.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 7000, 7.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 8000, 8.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 9000, 9.5,  'SYSTEM', now(), 'SYSTEM', now()),
(16, 10000, 10.5,  'SYSTEM', now(), 'SYSTEM', now());
insert into section_element (section_id, element_id, element_count, created_by, created_date, last_modified_by, last_modified_date) values
(16,1,1,'SYSTEM', now(), 'SYSTEM', now()),
(16,2,3,'SYSTEM', now(), 'SYSTEM', now()),
(16,3,4,'SYSTEM', now(), 'SYSTEM', now());
insert into variable_element (section_id, section_type_id, standard_size, order_key, index) values
(16, 10, 4, 1, 'RRS-E-C14-1000/1000-915'),
(16, 10, 4, 2, 'RRS-K-C19-1250/1250-1040'),
(16, 10, 4, 3, 'RRS-E-C19-1500/1500-1040');

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Электрический нагреватель 1', '6Q2W0E9X', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,11,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(17, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(17, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(17, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(17, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(17, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(17, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(17, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(17, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(17, 15, 'EU-4','SYSTEM', now(), 'SYSTEM', now()),
(17, 16, '500х300','SYSTEM', now(), 'SYSTEM', now()),
(17, 17, '50','SYSTEM', now(), 'SYSTEM', now()),
(17, 18, '2','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(17, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(17, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(17, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Электрический нагреватель 2', '7Q2W0E9X', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,11,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(18, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(18, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(18, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(18, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(18, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(18, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(18, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(18, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(18, 15, 'EU-4','SYSTEM', now(), 'SYSTEM', now()),
(18, 16, '500х300','SYSTEM', now(), 'SYSTEM', now()),
(18, 17, '100','SYSTEM', now(), 'SYSTEM', now()),
(18, 18, '1','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(18, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(18, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(18, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into section(name, drawing_code, description, note, build_coefficient, hardware_coefficient, section_area, standard_size, section_type_id, created_by, created_date, last_modified_by, last_modified_date)
values ('Электрический нагреватель 3', '8Q2W0E9X', 'Какое то описание','Интересное примечание',8.3,0.1,0.8,4,11,'SYSTEM', now(), 'SYSTEM', now());
insert into parameter_value(section_id, parameter_id, value, created_by, created_date, last_modified_by, last_modified_date) values
(19, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(19, 2, '1000мм', 'SYSTEM', now(), 'SYSTEM', now()),
(19, 3, 125, 'SYSTEM', now(), 'SYSTEM', now()),
(19, 4, 225,'SYSTEM', now(), 'SYSTEM', now()),
(19, 5, 740,'SYSTEM', now(), 'SYSTEM', now()),
(19, 1, 500,'SYSTEM', now(), 'SYSTEM', now()),
(19, 12, 5,'SYSTEM', now(), 'SYSTEM', now()),
(19, 13, 25,'SYSTEM', now(), 'SYSTEM', now()),
(19, 15, 'EU-4','SYSTEM', now(), 'SYSTEM', now()),
(19, 16, '500х300','SYSTEM', now(), 'SYSTEM', now()),
(19, 17, '150','SYSTEM', now(), 'SYSTEM', now()),
(19, 18, '1','SYSTEM', now(), 'SYSTEM', now());
insert into acoustic_performance(section_id, hz_63, hz_125, hz_250, hz_500, hz_1000, hz_2000, hz_4000, hz_8000,  created_by, created_date, last_modified_by, last_modified_date) values
(19, 0, 0, 0, 0, 0, 0, 0, 0,  'SYSTEM', now(), 'SYSTEM', now());
insert into air_consumption(section_id, consumption, pressure,  created_by, created_date, last_modified_by, last_modified_date) values
(19, 1000, 51.1,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 2000, 52.2,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 3000, 53.3,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 4000, 54.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 5000, 55.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 6000, 56.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 7000, 57.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 8000, 58.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 9000, 59.5,  'SYSTEM', now(), 'SYSTEM', now()),
(19, 10000, 60.5,  'SYSTEM', now(), 'SYSTEM', now());

insert into roenest_heat_exchanger(mode, code, geometry, length, height, num_rows, tubes_type, fin_spacing, fin_type, circuits_type, num_circuits, header_configuration, header_value_1, header_value_2, created_by, created_date, last_modified_by, last_modified_date) values
--Нагреватели
(1, '6.30.CU.15.AL.18.01.0670.18.W.X.X.009.018.R 1"L', 2, 670, 450, 1, 2, 2, 4, 3, 9, 1, 1, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(1, '6.30.CU.10.AL.18.02.0670.18.W.X.X.012.036.R 1"L', 2, 670, 450, 2, 2, 2, 1, 3, 12, 1, 1, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(1, '6.30.CU.10.AL.18.03.0670.25.W.X.X.012.054.R 1"L', 2, 670, 450, 3, 2, 6, 1, 3, 12, 1, 1, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(1,  '6.30.CU.10.AL.18.04.0670.30.W.X.X.018.072.R 1"L', 2, 670, 450, 4, 2, 8, 1, 3, 18, 1, 1, 4, 'SYSTEM', now(), 'SYSTEM', now()),

--Охладители
(2, '6.30.CU.10.AL.17.04.0670.30.W.X.X.009.068.R 1"L', 2, 670, 425, 4, 2, 8, 1, 3, 9, 0, 2, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(2, '6.30.CU.10.AL.17.06.0670.30.W.X.X.012.102.R 1"L', 2, 670, 425, 6, 2, 8, 1, 3, 12, 0, 2, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(2, '6.30.CU.10.AL.17.08.0670.30.W.X.X.014.136.R 1"L', 2, 670, 425, 8, 2, 8, 1, 3, 14, 0, 2, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(2, '6.30.CU.10.AL.17.10.0670.30.W.X.X.017.170.R 1"L', 2, 670, 425, 10, 2, 8, 1, 3, 17, 0, 2, 4, 'SYSTEM', now(), 'SYSTEM', now()),
(2, '6.30.CU.10.AL.17.12.0660.30.W.X.X.018.204.R 1 1/4"L', 2, 660, 425, 12, 2, 8, 1, 18, 12, 0, 2, 5, 'SYSTEM', now(), 'SYSTEM', now()),

--Фреоновые охладители
(4, '6.30.CU.10.AL.18.03.0670.30.E.X.X.005.054.R 16/22L', 2, 670, 450, 3, 2, 8, 1, 3, 5, 0, 3, 2, 'SYSTEM', now(), 'SYSTEM', now()),
(4, '6.30.CU.10.AL.18.04.0670.30.E.X.X.007.072.R 16/22L', 2, 670, 450, 4, 2, 8, 1, 3, 7, 0, 3, 2, 'SYSTEM', now(), 'SYSTEM', now()),
(4, '6.30.CU.10.AL.18.06.0670.30.E.X.X.009.108.R 22/28L', 2, 670, 450, 6, 2, 8, 1, 3, 9, 0, 4, 3, 'SYSTEM', now(), 'SYSTEM', now());

-- insert into ziehlabegg_fan(standard_size, order_key, fan_type) values
-- (4, 1, 'ER28C-2DN.B7.1R'),
-- (4, 2, 'ER31C-2DN.C7.1R'),
-- (4, 3, 'ER31C-2DN.D7.1R'),
-- (4, 4, 'ER35C-2DN.E7.1R'),
-- (4, 5, 'ER35C-2DN.F7.1R'),
-- (5, 1, 'ER35C-4DN.D7.1R'),
-- (5, 2, 'ER35C-2DN.D7.1R'),
-- (5, 3, 'ER35C-2DN.E7.1R'),
-- (5, 4, 'ER40C-2DN.F7.1R'),
-- (5, 5, 'ER40C-2DN.G7.1R'),
-- (6, 1, 'ER40C-4DN.E7.1R'),
-- (6, 2, 'ER40C-4DN.E7.1R'),
-- (6, 3, 'ER45C-4DN.F7.1R'),
-- (6, 4, 'ER45C-4DN.G7.1R'),
-- (6, 5, 'ER45C-2DN.G7.1R'),
-- (7, 1, 'ER50C-4DN.E7.1R'),
-- (7, 2, 'ER50C-4DN.E7.1R'),
-- (7, 3, 'ER56C-4DN.F7.1R'),
-- (7, 4, 'ER56C-4DN.G7.1R'),
-- (7, 5, 'ER63C-4DN.H7.1R'),
-- (7, 6, 'ER63C-4DN.I7.1R'),
-- (8, 1, 'ER50C-4DN.E7.1R'),
-- (8, 2, 'ER56C-4DN.F7.1R'),
-- (8, 3, 'ER56C-4DN.G7.1R'),
-- (8, 4, 'ER63C-4DN.H7.1R'),
-- (8, 5, 'ER63C-4DN.I7.1R'),
-- (9, 1, 'ER56C-4DN.E7.1R'),
-- (9, 2, 'ER56C-4DN.F7.1R'),
-- (9, 3, 'ER63C-4DN.G7.1R'),
-- (9, 4, 'ER63C-4DN.H7.1R'),
-- (9, 5, 'ER63C-4DN.I7.1R'),
-- (9, 6, 'ER63C-4DN.K7.1R'),
-- (10, 1, 'ER56C-4DN.E7.1R'),
-- (10, 2, 'ER56C-4DN.F7.1R'),
-- (10, 3, 'ER63C-4DN.G7.1R'),
-- (10, 4, 'ER63C-4DN.H7.1R'),
-- (10, 5, 'ER63C-4DN.I7.1R'),
-- (10, 6, 'ER71C-4DN.K7.1R'),
-- (11, 1, 'ER63C-4DN.F7.1R'),
-- (11, 2, 'ER63C-4DN.G7.1R'),
-- (11, 3, 'ER71C-4DN.H7.1R'),
-- (11, 4, 'ER71C-4DN.I7.1R'),
-- (11, 5, 'ER71C-4DN.K7.1R'),
-- (12, 1, 'ER71C-6DN.H7.1R'),
-- (12, 2, 'ER71C-4DN.H7.1R'),
-- (12, 3, 'ER71C-4DN.I7.1R'),
-- (12, 4, 'ER80C-4DN.K7.1R'),
-- (12, 5, 'ER80C-4DN.L7.1R'),
-- (13, 1, 'ER71C-6DN.H7.1R'),
-- (13, 2, 'ER71C-4DN.H7.1R'),
-- (13, 3, 'ER80C-6DN.K7.1R'),
-- (13, 4, 'ER80C-4DN.K7.1R'),
-- (13, 5, 'ER90C-6DN.N7.1R'),
-- (14, 1, 'ER71C-6DN.H7.1R'),
-- (14, 2, 'ER71C-4DN.H7.1R'),
-- (14, 3, 'ER80C-6DN.K7.1R'),
-- (14, 4, 'ER80C-4DN.K7.1R'),
-- (14, 5, 'ER90C-6DN.N7.1R'),
-- (15, 1, 'ER80C-6DN.I7.1R'),
-- (15, 2, 'ER80C-6DN.K7.1R'),
-- (15, 3, 'ER90C-6DN.M7.1R'),
-- (15, 4, 'ER90C-6DN.N7.1R'),
-- (15, 5, 'ER10C-6DN.N7.1R'),
-- (16, 1, 'ER90C-6DN.I7.1R'),
-- (16, 2, 'ER90C-6DN.K7.1R'),
-- (16, 3, 'ER10C-6DN.M7.1R'),
-- (16, 4, 'ER10C-6DN.N7.1R'),
-- (16, 5, 'ER10C-6DN.N7.1R'),
-- (16, 6, 'ER10C-6DN.R7.1R'),
-- (17, 1, 'ER90C-6DN.I7.1R'),
-- (17, 2, 'ER90C-6DN.K7.1R'),
-- (17, 3, 'ER10C-6DN.M7.1R'),
-- (17, 4, 'ER10C-6DN.N7.1R'),
-- (17, 5, 'ER10C-6DN.N7.1R'),
-- (17, 6, 'ER10C-6DN.R7.1R'),
-- (18, 1, 'ER90C-6DN.I7.1R'),
-- (18, 2, 'ER90C-6DN.K7.1R'),
-- (18, 3, 'ER10C-6DN.M7.1R'),
-- (18, 4, 'ER10C-6DN.N7.1R'),
-- (18, 5, 'ER10C-6DN.N7.1R'),
-- (18, 6, 'ER10C-6DN.R7.1R'),
-- (19, 1, 'ER10C-8DN.M7.1R'),
-- (19, 2, 'ER10C-6DN.M7.1R'),
-- (19, 3, 'ER10C-6DN.N7.1R'),
-- (19, 4, 'ER10C-6DN.N7.1R'),
-- (19, 5, 'ER10C-6DN.R7.1R'),
-- (20, 1, 'ER10C-8DN.M7.1R'),
-- (20, 2, 'ER10C-6DN.M7.1R'),
-- (20, 3, 'ER11C-8DN.P7.4R'),
-- (20, 4, 'ER11C-8DN.R7.4R'),
-- (20, 5, 'ER11C-6DN.R7.1R'),
-- (20, 6, 'ER11C-6DN.S7.1R'),
-- (21, 1, 'ER10C-8DN.M7.1R'),
-- (21, 2, 'ER10C-6DN.M7.1R'),
-- (21, 3, 'ER10C-6DN.N7.1R'),
-- (21, 4, 'ER10C-6DN.N7.1R'),
-- (21, 5, 'ER11C-6DN.R7.4R'),
-- (21, 6, 'ER11C-6DN.S7.1R'),
-- (21, 7, 'ER11C-6DN.T7.1R'),
-- (21, 8, 'ER11C-6DN.U7.1R'),
-- (22, 1, 'ER10C-6DN.N7.1R'),
-- (22, 2, 'ER10C-6DN.N7.1R'),
-- (22, 3, 'ER11C-6DN.R7.4R'),
-- (22, 4, 'ER11C-6DN.S7.1R'),
-- (22, 5, 'ER11C-6DN.T7.1R'),
-- (22, 6, 'ER11C-6DN.U7.1R'),
-- (23, 1, 'ER11C-8DN.P7.1R'),
-- (23, 2, 'ER11C-8DN.R7.1R'),
-- (23, 3, 'ER11C-6DN.R7.1R');