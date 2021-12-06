--Полагаю, что второй JOIN во всех запросах излишен, но проверять уже не буду)

--Поиск клапана
SELECT *
FROM section
WHERE id = (
    SELECT DISTINCT section_id
    FROM parameter_value AS pv
             INNER JOIN parameter AS p ON p.id = pv.parameter_id
    WHERE section_id IN
          (SELECT DISTINCT section_id
           FROM parameter_value AS pv
                    INNER JOIN parameter AS p ON p.id = pv.parameter_id
           WHERE section_id IN
                 (SELECT s.id
                  FROM section AS s
                  WHERE s.standard_size = 4
                    AND s.section_type_id = 3)
             AND (p.name = 'Размер клапана' AND pv.value = '600мм'))
      AND (p.name = 'Подогрев клапана' AND pv.value = 'Да')
    LIMIT 1);

SELECT value
FROM section AS s
         INNER JOIN parameter_value AS pv ON s.id = pv.section_id
         INNER JOIN parameter AS p ON pv.parameter_id = p.id
WHERE s.id = 5
  AND p.name = 'Диапазон работы';

--Шумоглушитель
SELECT *
FROM section
WHERE id = (
    SELECT DISTINCT section_id
    FROM parameter_value AS pv
             INNER JOIN parameter AS p ON p.id = pv.parameter_id
    WHERE section_id IN
          (SELECT DISTINCT section_id
           FROM parameter_value AS pv
                    INNER JOIN parameter AS p ON p.id = pv.parameter_id
           WHERE section_id IN
                 (SELECT s.id
                  FROM section AS s
                  WHERE s.standard_size = 4
                    AND s.section_type_id = 2)
             AND (p.name = 'Длина секции' AND pv.value = '1100мм'))
    LIMIT 1);

SELECT value
FROM section AS s
         INNER JOIN parameter_value AS pv ON s.id = pv.section_id
         INNER JOIN parameter AS p ON pv.parameter_id = p.id
WHERE s.id = 4
  AND p.name = 'Уровень снижения шума';

--Вентилятор свободное колесо
select *
from section
where id in
      (select section_id
       from section as s
                inner join variable_element as ve on ve.section_id = s.id
       where s.standard_size = 4
       and s.section_type_id = 1
       limit 1);

--Пустая секция
SELECT * FROM section WHERE id =(
    SELECT DISTINCT section_id FROM parameter_value AS pv
                                        INNER JOIN parameter AS p ON p.id = pv.parameter_id
    WHERE section_id IN
          (SELECT DISTINCT section_id FROM parameter_value AS pv
                                               INNER JOIN parameter AS p ON p.id = pv.parameter_id
           WHERE section_id IN
                 (SELECT s.id FROM section AS s
                  WHERE s.standard_size = 4
                    AND s.section_type_id = 4)
             AND (p.name = 'Длина секции' AND pv.value = '300мм'))
    LIMIT 1);

--Кассетный/Карманный фильтр
SELECT * FROM section WHERE id =(
    SELECT DISTINCT section_id FROM parameter_value AS pv
                                        INNER JOIN parameter AS p ON p.id = pv.parameter_id
    WHERE section_id IN
          (SELECT DISTINCT section_id FROM parameter_value AS pv
                                               INNER JOIN parameter AS p ON p.id = pv.parameter_id
           WHERE section_id IN
                 (SELECT s.id FROM section AS s
                  WHERE s.standard_size = 4
                    AND s.section_type_id = 6)
             AND (p.name = 'Класс фильтрующего элемента' AND pv.value = 'EU-4'))
    LIMIT 1);



--Параметры
select pv.value  from parameter_value as pv
                          inner join parameter as p on pv.parameter_id = p.id
where section_id = 1 and p.name = 'Длина';

--Установки
update installation_to_selection set project_id = null
where project_id = 8 and created_by = 'testt@gmail.com';

--функция для преобразования типов к int
create function try_cast_int(p_in text, p_default int default null)
    returns int
as
$$
begin
    begin
        return $1::int;
    exception
        when others then
            return p_default;
    end;
end;
$$
    language plpgsql;

--Поиск электрических нагревателей по Тепловой мощности
SELECT DISTINCT section_id as id, pv.value as value
FROM parameter_value AS pv
         INNER JOIN parameter AS p ON p.id = pv.parameter_id
WHERE section_id IN
      (SELECT section_id
       FROM parameter_value AS pv
                INNER JOIN parameter AS p ON p.id = pv.parameter_id
       WHERE section_id IN
             (SELECT s.id
              FROM section AS s
              WHERE s.standard_size = 4
                AND s.section_type_id = 11)
         AND p.name = 'Количество ступеней регулирования'
         AND pv.value = '1')
  AND p.name = 'Тепловая мощность'
  AND try_cast_int(pv.value) >= 40
ORDER BY value ASC
LIMIT 1;




