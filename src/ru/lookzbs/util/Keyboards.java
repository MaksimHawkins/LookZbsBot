package ru.lookzbs.util;

public class Keyboards {

    //TODO Move to .json files

    public static final String KEYBOARD_GENDER = "{\n" +
            "\"one_time\": true,\n" +
            "\"buttons\":\n" +
            "[\n" +
            "[\n" +
            "{\n" +
            "\"action\": \n" +
            "{\n" +
            "\"type\": \"text\",\n" +
            "\"label\":\"Мужской\"\n" +
            "},\n" +
            "\"color\": \"secondary\"\n" +
            "},\n" +
            "\n" +
            "{\n" +
            "\"action\": \n" +
            "{\n" +
            "\"type\": \"text\",\n" +
            "\"label\":\"Женский\"\n" +
            "},\n" +
            "\"color\": \"secondary\"\n" +
            "}\n" +
            "]\n" +
            "]\n" +
            "}";

    public static final String KEYBOARD_RATE = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"&#129314;\",\n" +
            "\t\t  \"payload\": \"1\"\n" +
            "        },\n" +
            "        \"color\": \"negative\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"&#128534;\",\n" +
            "\t\t  \"payload\": \"2\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"&#128559;\",\n" +
            "\t\t  \"payload\": \"3\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"&#128525;\",\n" +
            "\t\t  \"payload\": \"4\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      }\n" +
            "    ],\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Пропустить\",\n" +
            "\t\t  \"payload\": \"5\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Вернуться в меню\",\n" +
            "\t\t  \"payload\": \"6\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

    public static final String KEYBOARD_MENU = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Оценить других\",\n" +
            "\t\t  \"payload\": \"1\"\n" +
            "        },\n" +
            "        \"color\": \"positive\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Изменить фильтры\",\n" +
            "\t\t  \"payload\": \"2\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      }\n" +
            "    ],\n" +
            "\t[\n" +
            "\t\t{\n" +
            "\t\t\t\"action\": {\n" +
            "\t\t\t  \"type\": \"text\",\n" +
            "\t\t\t  \"label\": \"Общий топ\",\n" +
            "\t\t\t  \"payload\": \"4\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"color\": \"secondary\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"action\": {\n" +
            "\t\t\t  \"type\": \"text\",\n" +
            "\t\t\t  \"label\": \"Городской топ\",\n" +
            "\t\t\t  \"payload\": \"5\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"color\": \"secondary\"\n" +
            "\t\t}\n" +
            "\t],\n" +
            "\t[\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Мой профиль\",\n" +
            "\t\t  \"payload\": \"3\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

    public static final String KEYBOARD_GENDER_FILTER = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Парней\",\n" +
            "\t\t  \"payload\": \"1\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Девушек\",\n" +
            "\t\t  \"payload\": \"2\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Всех\",\n" +
            "\t\t  \"payload\": \"3\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

    public static final String KEYBOARD_CITY_FILTER = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Из моего\",\n" +
            "\t\t  \"payload\": \"1\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Из любого\",\n" +
            "\t\t  \"payload\": \"2\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      }\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

    public static final String KEYBOARD_MY_PROFILE = "{\n" +
            "  \"one_time\": false,\n" +
            "  \"buttons\": [\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Изменить фото\",\n" +
            "\t\t  \"payload\": \"1\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"Заполнить заново\",\n" +
            "\t\t  \"payload\": \"2\"\n" +
            "        },\n" +
            "        \"color\": \"secondary\"\n" +
            "      }\n" +
            "    ],\n" +
            "    [\n" +
            "      {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"#tivate#\",\n" +
            "\t\t  \"payload\": \"3\"\n" +
            "        },\n" +
            "        \"color\": \"#color#\"\n" +
            "      },\n" +
            "\t  {\n" +
            "        \"action\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"label\": \"В главное меню\",\n" +
            "\t\t  \"payload\": \"4\"\n" +
            "        },\n" +
            "        \"color\": \"primary\"\n" +
            "\t\t}\t\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";
}
