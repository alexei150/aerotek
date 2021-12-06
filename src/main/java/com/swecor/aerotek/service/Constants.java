package com.swecor.aerotek.service;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    private Constants() {
    }

    //ID Типов секций
    public static final int FAN_FREE_WHEEL_ID = 1;
    public static final int MUFFLER_ID = 2;
    public static final int AIR_VALVE_ID = 3;
    public static final int EMPTY_SECTION_ID = 4;
    public static final int POCKET_FILTER_ID = 5;
    public static final int CASSETTE_FILTER_ID = 6;
    public static final int HEATER_ID = 7;
    public static final int COOLER_ID = 8;
    public static final int FREON_COOLER_ID = 9;
    public static final int ROTOR_RECUPERATOR_ID = 10;
    public static final int ELECTRIC_HEATER_ID = 11;

    public static final String AIR_SPEED = "Скорость воздуха";
    public static final String PRESSURE_LOSS = "Потери давления воздуха";

    //Вентилятор свободное колесо
    public static final String FAN_INDEX = "Индекс вентилятора";
    public static final String FAN_AIR_OUTLET_DIRECTION = "Направление выхода воздуха вентилятора";

    //Шумоглушитель
    public static final String SECTION_LENGTH = "Длина секции";
    public static final String NOISE_REDUCTION_LEVEL = "Уровень снижения шума";

    //Воздушный клапан
    public static final String VALVE_SIZE = "Размер клапана";
    public static final String VALVE_HEAVING = "Подогрев клапана";
    public static final String WORKING_RANGE = "Диапазон работы";

    //Кассетный и Карманный фильтр
    public static final String FILTER_CLASS_FORM = "Класс фильтрующего элемента";
    public static final String FILTER_CELL_SIZE = "Размер ячеек фильтров";

    //Электрический нагреватель
    public static final String HEAT_POWER = "Тепловая мощность";
    public static final String NUMBER_OF_CONTROL_STEPS = "Количество ступеней регулирования";
    public static final String INLET_TEMP = "Температура воздуха на входе";
    public static final String INLET_TEMP_TRANSIENT = "Температура воздуха на входе ";
    public static final String REQUIRED_TEMP_OUT = "Желаемая температура воздуха на выходе";
    public static final String REQUIRED_TEMP_OUT_TRANSIENT = "Желаемая температура воздуха на выходе ";
    public static final String OUTLET_TEMP = "Температура воздуха на выходе";
    public static final String OUTLET_TEMP_TRANSIENT = "Температура воздуха на выходе ";
    public static final String RELATIVE_HUMIDITY = "Относительная влажность воздуха на выходе";
    public static final String RELATIVE_HUMIDITY_TRANSIENT = "Относительная влажность воздуха на выходе ";
    public static final String CALCULATED_HEAT_POWER = "Расчетная тепловая мощность";
    public static final String CALCULATED_HEAT_POWER_TRANSIENT = "Расчетная тепловая мощность ";

    public static final Map<Character, String> ROTOR_TYPE_MAP = new HashMap<>();
    static {
        ROTOR_TYPE_MAP.put('P', "Конденсационный ротор Р/Р");
        ROTOR_TYPE_MAP.put('K', "Покрытый эпокс. смолой К/К");
        ROTOR_TYPE_MAP.put('E', "Энтальпийный ротор Е/ЕТ");
        ROTOR_TYPE_MAP.put('N', "Сорбционный ротор HUgo N");
    }

    public static final int[] DENSITY_ARRAY = {-50, -45, -40, -35, -30, -25, -20, -15, -10, -5, 0, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140};

    public static final Map<Integer, Double> DENSITY_MAP = new HashMap<>();
    static {
        DENSITY_MAP.put(-50, 1.584);
        DENSITY_MAP.put(-45, 1.549);
        DENSITY_MAP.put(-40, 1.515);
        DENSITY_MAP.put(-35, 1.484);
        DENSITY_MAP.put(-30, 1.453);
        DENSITY_MAP.put(-25, 1.424);
        DENSITY_MAP.put(-20, 1.395);
        DENSITY_MAP.put(-15, 1.369);
        DENSITY_MAP.put(-10, 1.342);
        DENSITY_MAP.put(-5, 1.318);
        DENSITY_MAP.put(-0, 1.293);
        DENSITY_MAP.put(10, 1.247);
        DENSITY_MAP.put(15, 1.226);
        DENSITY_MAP.put(20, 1.205);
        DENSITY_MAP.put(30, 1.165);
        DENSITY_MAP.put(40, 1.128);
        DENSITY_MAP.put(50, 1.093);
        DENSITY_MAP.put(60, 1.06);
        DENSITY_MAP.put(70, 1.029);
        DENSITY_MAP.put(80, 1.0);
        DENSITY_MAP.put(90, 0.972);
        DENSITY_MAP.put(100, 0.946);
        DENSITY_MAP.put(110, 0.922);
        DENSITY_MAP.put(120, 0.898);
        DENSITY_MAP.put(130, 0.876);
        DENSITY_MAP.put(140, 0.854);
    }

}
