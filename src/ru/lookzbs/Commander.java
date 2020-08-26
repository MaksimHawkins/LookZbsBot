package ru.lookzbs;

import com.blade.mvc.RouteContext;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.lookzbs.model.FullProfile;
import ru.lookzbs.model.Profile;
import ru.lookzbs.model.TopProfile;
import ru.lookzbs.sql.SqlHelper;
import ru.lookzbs.util.Keyboards;
import ru.lookzbs.util.VKUtil;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Commander {

    private static final String CONFIRMATION_TYPE = "confirmation";
    private static final String MESSAGE_TYPE = "message_new";
    private static final String OK_RESPONSE = "ok";

    private static final int STATUS_MAIN_MENU = 10;
    private static final int STATUS_RATE = 11;
    private static final int STATUS_MY_PROFILE = 12;

    private static final int STATUS_FILL_GENDER = 21;
    private static final int STATUS_FILL_AGE = 22;
    private static final int STATUS_FILL_CITY = 23;
    private static final int STATUS_FILL_PHOTO = 24;

    private static final int STATUS_FILTER_GENDER = 31;
    private static final int STATUS_FILTER_CITY = 32;

    private static final int STATUS_FAKE_PROFILE = 41;

    private final static String str1 = "Оцени внешность этого пользователя от 1 до 4\n\n";

    private Random random;

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SqlHelper sqlHelper;

    public Commander() {
        sqlHelper = new SqlHelper();
        random = new Random();
    }

    public void doCommand(RouteContext ctx) {
        String body = ctx.bodyToString();
        Gson gson = new Gson();
        JsonObject request = gson.fromJson(body, JsonObject.class);

        String type = request.get("type").getAsString();
        String secret = request.get("secret").getAsString();
        if (!secret.equals(VKUtil.secretKey)) {
            System.out.println("Invalid secret");
            return;
        }

        //Code for bot validation
        if (type.equals(CONFIRMATION_TYPE)) {
            //TODO Move to config
            ctx.text("3693a0be");
        }

        if (type.equals(MESSAGE_TYPE)) {
            JsonObject message = request.get("object").getAsJsonObject();
            try {
                handleMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(body);
            ctx.text(OK_RESPONSE);
        }
    }

    private void handleMessage(JsonObject message) {
        System.out.println(message.toString());
        System.out.println("\n");
        int user_id = 0;
        if (message.has("user_id")) user_id = message.get("user_id").getAsInt();
        else if (message.has("from_id")) user_id = message.get("from_id").getAsInt();

        if (!sqlHelper.isUserRegistered(user_id)) {
            simpleMessage(user_id, "Давай зарегистрируемся &#128588;");
            startRegister(user_id);
        } else {
            int currentStatus = sqlHelper.getStatus(user_id);

            String body = message
                    .get("body")
                    .getAsString();

            System.out.println("User status: " + currentStatus);

            //Пользователь заполняет профиль
            if (currentStatus >= STATUS_FILL_GENDER && currentStatus <= STATUS_FILL_PHOTO) {
                fillProfileStage(user_id, currentStatus, message);
                return;
            }

            //Пользователь изменяет фильтры
            if (currentStatus >= STATUS_FILTER_GENDER && currentStatus <= 34) {
                filterStage(user_id, currentStatus, message);
                return;
            }

            //Пользователь в главном меню
            if (currentStatus == STATUS_MAIN_MENU) {
                if (message.has("payload")) {
                    switch (message.get("payload").getAsString()) {
                        case "1": // Оценить других
                            showNext(user_id);
                            break;
                        case "2": // Изменить фильтры
                            startFilter(user_id);
                            break;
                        case "3": // Мой профиль
                            toMyProfile(user_id);
                            break;
                        case "4": // Общий топ
                            topMessage(user_id, false);
                            break;
                        case "5": // Городской топ
                            topMessage(user_id, true);
                            break;
                    }
                } else {
                    switch (body) {
                        case "1": //Оценить других
                            showNext(user_id);
                            break;
                        case "2": //Изменить фильтры
                            startFilter(user_id);
                            break;
                        case "3": //Мой профиль
                            toMyProfile(user_id);
                            break;
                        case "4": //Общий топ
                            topMessage(user_id, false);
                            break;
                        case "5": //Городской топ
                            topMessage(user_id, true);
                            break;
                        case "69":
                            if (sqlHelper.isAdmin(user_id)) {
                                adminFillFake(user_id);
                            }
                            break;
                    }
                }
                return;
            }

            //Пользователь кого-то оценил
            if (currentStatus == STATUS_RATE) {
                int rated_id = -1;
                if (message.has("payload")) {
                    //TODO Code is repeated. Fix it
                    switch (message.get("payload").getAsString()) {
                        case "1": // 10%
                            rated_id = sqlHelper.rateUser(user_id, 10);
                            showNext(user_id);
                            break;
                        case "2": // 40%
                            rated_id = sqlHelper.rateUser(user_id, 40);
                            showNext(user_id);
                            break;
                        case "3": // 70%
                            rated_id = sqlHelper.rateUser(user_id, 70);
                            showNext(user_id);
                            break;
                        case "4": // 100%
                            rated_id = sqlHelper.rateUser(user_id, 100);
                            showNext(user_id);
                            break;
                        case "5":
                            showNext(user_id);
                            break;
                        case "6":
                            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
                            mainMenuMessage(user_id, 0);
                            break;
                    }
                } else {
                    switch (body) {
                        case "1": // 10%
                            rated_id = sqlHelper.rateUser(user_id, 10);
                            showNext(user_id);
                            break;
                        case "2": // 40%
                            rated_id = sqlHelper.rateUser(user_id, 40);
                            showNext(user_id);
                            break;
                        case "3": // 70%
                            rated_id = sqlHelper.rateUser(user_id, 70);
                            showNext(user_id);
                            break;
                        case "4": // 100%
                            rated_id = sqlHelper.rateUser(user_id, 100);
                            showNext(user_id);
                            break;
                        case "0": //Пропустить
                            showNext(user_id);
                            break;
                        case "9":
                            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
                            mainMenuMessage(user_id, 0);
                            break;
                        default:
                            simpleMessage(user_id, "Оцени внешность пользователя от 1 до 4.\n\n 0. Пропустить\n9. Вернуться в меню");
                            break;
                    }
                }
                if (rated_id != 0) simpleMessage(rated_id, "Тебя оценили несколько человек! Самое время проверить свой рейтинг &#128064;");
                return;
            }

            //Пользователь рассматривает свой профиль
            if (currentStatus == STATUS_MY_PROFILE) {
                if (message.has("payload")) {
                    switch (message.get("payload").getAsString()) {
                        case "1": // Изменить фото
                            toChangePhoto(user_id);
                            break;
                        case "2": // Заполнить профиль заново
                            startEditProfile(user_id);
                            break;
                        case "3": // Активировать/деактивировать
                            FullProfile profile = sqlHelper.getProfile(user_id);
                            sqlHelper.setShownProfile(user_id, profile.isShown() ? 0 : 1);
                            myProfileMessage(user_id);
                            break;
                        case "4": // В главное меню
                            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
                            mainMenuMessage(user_id, 0);
                            break;
                    }
                } else {
                    switch (body) {
                        case "1": // Изменить фото
                            toChangePhoto(user_id);
                            break;
                        case "2": // Заполнить профиль заново
                            startEditProfile(user_id);
                            break;
                        case "3": // Активировать/деактивировать
                            FullProfile profile = sqlHelper.getProfile(user_id);
                            sqlHelper.setShownProfile(user_id, profile.isShown() ? 0 : 1);
                            myProfileMessage(user_id);
                            break;
                        case "4": // В главное меню
                            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
                            mainMenuMessage(user_id, 0);
                            break;
                        default:
                            simpleMessage(user_id, "Введи цифру");
                            break;
                    }
                }
                return;
            }

            if (currentStatus == STATUS_FAKE_PROFILE) {
                handleFake(user_id, message);
            }
        }
    }

    private void startRegister(int user_id) {
        sqlHelper.registerUser(user_id);
        genderMessage(user_id);
    }
    
    private void startEditProfile(int user_id) {
        sqlHelper.setStatus(user_id, STATUS_FILL_GENDER);
        genderMessage(user_id);
    }

    private void startFilter(int user_id) {
        sqlHelper.setStatus(user_id, STATUS_FILTER_GENDER);
        genderFilterMessage(user_id);
    }

    private void showNext(int user_id) {
        FullProfile profile = sqlHelper.getProfile(user_id);

        int ageLeft = 12, ageRight = 12;
        if (profile.getGender() && profile.getGenderFilter() == 0) {
            ageLeft = 13;
            ageRight = 11;
        } else if (!profile.getGender() && profile.getGenderFilter() == 1) {
            ageLeft = 11;
            ageRight = 13;
        }

        Profile newShown = sqlHelper.nextUser(user_id, profile.getGenderFilter(),profile.getAge() - ageLeft, profile.getAge() + ageRight, profile.isMycity(), profile.getCity());
        if (newShown != null) {
            rateMessage(user_id, newShown.getGender(), newShown.getAge(), newShown.getCity(), newShown.getPhoto());
            if (profile.getStatus() != STATUS_RATE) sqlHelper.setStatus(user_id, STATUS_RATE);
            sqlHelper.incrementMetric(getCurrentDate(), SqlHelper.METRIC_NEXT);
        } else {
            mainMenuMessage(user_id, 2);
            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
        }
    }

    private void toMyProfile(int user_id) {
        sqlHelper.setStatus(user_id, STATUS_MY_PROFILE);
        sqlHelper.updateLastActivity(user_id);
        myProfileMessage(user_id);
    }

    private void toChangePhoto(int user_id) {
        simpleMessage(user_id, "Отправь мне одну свою фотографию");
        sqlHelper.setStatus(user_id, STATUS_FILL_PHOTO);
    }

    private void fillProfileStage(int user_id, int status, JsonObject body) {
        String message;
        if (body.has("body")) message = body.get("body").getAsString();
        else if (body.has("text")) message = body.get("text").getAsString();
        else return;

        switch (status){
            case STATUS_FILL_GENDER:
                if (message.equalsIgnoreCase("Мужской") || message.equalsIgnoreCase("Женский")) {
                    int gender = message.equals("Мужской") ? 1 : 0;
                    sqlHelper.setGender(user_id, gender);
                    sqlHelper.setStatus(user_id, STATUS_FILL_AGE);

                    simpleMessage(user_id, "Сколько тебе лет?");
                } else {
                    simpleMessage(user_id, "Введи \"Мужской\" или \"Женский\"");
                }
                break;
            case STATUS_FILL_AGE:
                int age = 0;
                try {
                    age = Integer.parseInt(message);
                } catch (Exception e) {
                    simpleMessage(user_id, "Введи число от 10 до 100");
                    return;
                }
                if (age >= 10 && age <= 99) {
                    sqlHelper.setAge(user_id, age);
                    sqlHelper.setStatus(user_id, STATUS_FILL_CITY);

                    simpleMessage(user_id, "Укажи свой город");
                } else {
                    simpleMessage(user_id, "Введи число от 10 до 100");
                }
                break;
            case STATUS_FILL_CITY:
                if (message.length() > 1 && message.length() <= 24) {
                    sqlHelper.setCity(user_id, message);
                    sqlHelper.setStatus(user_id, STATUS_FILL_PHOTO);

                    simpleMessage(user_id, "Отправь мне одну свою фотографию");
                } else {
                    simpleMessage(user_id, "Неправильно введен город");
                }
                break;
            case STATUS_FILL_PHOTO:
                //Получаем фотку
                String finalPhoto = null;
                try {
                    String responseStr = VKUtil.vk.messages().getById(VKUtil.groupActor, body.get("id").getAsInt()).executeAsString();
                    JsonObject response = new Gson().fromJson(responseStr, JsonObject.class).get("response").getAsJsonObject();
                    if (response.has("items")) {
                        JsonObject item = response.get("items").getAsJsonArray().get(0).getAsJsonObject();

                        if (item.has("attachments")) {
                            JsonArray attachments = item.get("attachments").getAsJsonArray();
                            for (JsonElement element: attachments) {
                                JsonObject attachment = element.getAsJsonObject();
                                String type = attachment.get("type").getAsString();
                                if (type.equals("photo")) {
                                    JsonObject photo = attachment.get("photo").getAsJsonObject();
                                    int photo_id = photo.get("id").getAsInt();
                                    int photo_owner = photo.get("owner_id").getAsInt();

                                    finalPhoto = "photo" + photo_owner + "_" + photo_id;

                                    if (photo.has("access_key")) {
                                        finalPhoto += "_" + photo.get("access_key").getAsString();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (ClientException e) {
                    System.err.println("Не удалось получить сообщение с фотографией");
                    e.printStackTrace();
                    return;
                }

                if (finalPhoto == null) {
                    simpleMessage(user_id, "Отправь одну свою фотографию");
                } else {
                    sqlHelper.setPhoto(user_id, finalPhoto);
                    sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
                    sqlHelper.updateLastActivity(user_id);
                    mainMenuMessage(user_id, 0);
                }
                break;

        }
    }

    private void filterStage(int user_id, int status, JsonObject body) {
        String message;
        if (body.has("body")) message = body.get("body").getAsString();
        else if (body.has("text")) message = body.get("text").getAsString();
        else return;

        switch (status) {
            case STATUS_FILTER_GENDER:
                switch (message) {
                    case "Парней":
                        sqlHelper.setGenderFilter(user_id, 1);
                        cityFilterMessage(user_id);
                        break;
                    case "Девушек":
                        sqlHelper.setGenderFilter(user_id, 0);
                        cityFilterMessage(user_id);
                        break;
                    case "Всех":
                        sqlHelper.setGenderFilter(user_id, 2);
                        cityFilterMessage(user_id);
                        break;
                    case "1":
                        sqlHelper.setGenderFilter(user_id, 1);
                        cityFilterMessage(user_id);
                        break;
                    case "2":
                        sqlHelper.setGenderFilter(user_id, 0);
                        cityFilterMessage(user_id);
                        break;
                    case "3":
                        sqlHelper.setGenderFilter(user_id, 2);
                        cityFilterMessage(user_id);
                        break;
                    default:
                        simpleMessage(user_id, "Чето неправильно вводишь");
                        break;
                }
                break;
            case STATUS_FILTER_CITY:
                switch (message) {
                    case "Из моего":
                    case "1":
                        sqlHelper.setCityFilter(user_id, 1);
                        mainMenuMessage(user_id, 1);
                        break;
                    case "Из любого":
                    case "2":
                        sqlHelper.setCityFilter(user_id, 0);
                        mainMenuMessage(user_id, 1);
                        break;
                    default:
                        simpleMessage(user_id, "Чето неправильно вводишь");
                        break;
                }
                break;
        }
    }

    private void simpleMessage(int user_id, String message) {
        try {
            VKUtil.vk.messages().send(VKUtil.groupActor).message(message).userId(user_id).randomId(random.nextInt()).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    private void rateMessage(int user_id, boolean gender, int age, String city, String photo) {
        try {
            String text = str1;
            //if (gender) text += "М, "; else text += "Ж, ";
            if (gender) text += "♂ "; else text += "♀ ";
            text += age + " - ";
            text += city;

            text += "\n\n0. Пропустить\n9. Вернуться в меню";
            VKUtil.vk.messages().send(VKUtil.groupActor).message(text).userId(user_id).randomId(random.nextInt()).attachment(photo).unsafeParam("keyboard", Keyboards.KEYBOARD_RATE).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void mainMenuMessage(int user_id, int code) {
        try {
            String text = "Меню: \n\n1. Оценить других\n2. Изменить фильтры\n3. Мой профиль\n4. Общий топ\n5. Городской топ";

            if (code != 0) {
                text += "\n\n";
                switch (code) {

                    //После смены фильтров
                    case 1:
                        FullProfile profile = sqlHelper.getProfile(user_id);
                        text += "Теперь тебе будут показываться";
                        switch (profile.getGenderFilter()) {
                            case 0:
                                text += " девушки";
                                break;
                            case 1:
                                text += " парни";
                                break;
                            case 2:
                                text += " люди";
                                break;
                        }

                        text += " приблизительно твоего возраста";
                        text += profile.isMycity() ? " только из твоего" : " из любого";
                        text += " города";
                        break;

                        //Когда нет подходящих анкет
                    case 2:
                        text += "Подходящие анкеты кончились. Попробуй позже... &#128055;";
                        break;
                }
            }

            VKUtil.vk.messages().send(VKUtil.groupActor).message(text).userId(user_id).randomId(random.nextInt()).unsafeParam("keyboard", Keyboards.KEYBOARD_MENU).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void myProfileMessage(int user_id) {
        try {
            FullProfile profile = sqlHelper.getProfile(user_id);
            String text = "";
            String keyboard = Keyboards.KEYBOARD_MY_PROFILE;

            text += "Твой профиль выглядит так:\n\n";
            if (profile.getGender()) text += "♂ "; else text += "♀ ";
            text += profile.getAge() + " - ";
            text += profile.getCity();

            text += "\nРейтинг твоей внешности " + profile.getRating() + "%";

            text += "\n\n1. Изменить фото\n2. Заполнить профиль заново\n3. ";
            text += profile.isShown() ? "Деактивировать (Профиль не будет выдаваться другим и не будет отображаться в топе)" : "Активировать\n\nТвой профиль не выдается другим и не отображается в топе";
            keyboard = keyboard.replaceAll("#tivate#", profile.isShown() ? "Деактивировать" : "Активировать");
            keyboard = keyboard.replaceAll("#color#", profile.isShown() ? "negative" : "positive");
            text += "\n4. Вернуться в меню";

            text += "\n\nВнимание! При изменении фотографии твой рейтинг сбросится";

            VKUtil.vk.messages().send(VKUtil.groupActor).message(text).userId(user_id).randomId(random.nextInt()).attachment(profile.getPhoto()).unsafeParam("keyboard", keyboard).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void topMessage(int user_id, boolean inCity) {
        try {
            String city = null;
            if (inCity) {
                city = sqlHelper.getProfile(user_id).getCity();
            }
            List<TopProfile> profiles = sqlHelper.getTop(city, 0);

            String text;
            text = inCity ? "Городской топ:" : "Общий топ:";
            text += "\n\n";
            String photos = "";

            if (profiles.size() == 0) {
                text += inCity ? "Топ твоего города пуст :(\n" : "Общий топ почему-то пуст О_О";
            }

            for (int i = 0; i < profiles.size(); i++) {
                TopProfile profile = profiles.get(i);

                text += (i + 1) + ". ";
                text += profile.getGender() ? "♂" : "♀";
                text += " " + profile.getAge() + ", " + profile.getCity()  + " - " + profile.getRating() + "%" + "\n";
                photos += profile.getPhoto();
                if (i != profiles.size() - 1) photos += ",";
            }

            VKUtil.vk.messages().send(VKUtil.groupActor).message(text).userId(user_id).randomId(random.nextInt()).attachment(photos).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void genderMessage(int user_id) {
        try {
            VKUtil.vk.messages().send(VKUtil.groupActor).message("Укажи свой пол").userId(user_id).randomId(random.nextInt()).unsafeParam("keyboard", Keyboards.KEYBOARD_GENDER).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void genderFilterMessage(int user_id) {
        try {
            VKUtil.vk.messages().send(VKUtil.groupActor).message("Людей какого пола тебе выдавать?\n\n1. Парней\n2. Девушек\n3. Всех").userId(user_id).randomId(random.nextInt()).unsafeParam("keyboard", Keyboards.KEYBOARD_GENDER_FILTER).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void cityFilterMessage(int user_id) {
        try {
            VKUtil.vk.messages().send(VKUtil.groupActor).message("Из какого города показывать профили?\n\n1. Из моего\n2. Из любого").userId(user_id).randomId(random.nextInt()).unsafeParam("keyboard", Keyboards.KEYBOARD_CITY_FILTER).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void adminFillFake(int user_id) {
        String text = "Для создания фейковой анкеты введи в одном сообщении через пробел следующие данные:\n" +
                "1) Пол: 1 - мужской, 0 - женский\n" +
                "2) Возраст: от 10 до 100\n" +
                "3) Город\n" +
                "4) Рейтинг: от 0 до 100\n" +
                "\n" +
                "Так же к сообщению прикрепи фото.\n" +
                "\n" +
                "Пример:\n" +
                "\n" +
                "0 20 Краснодар 70 - создаст женскую анкету с прикрепленной фото, возраст: 20, город Краснодар, рейтинг - 70.\n\nДля выхода из админки напиши 11";

        sqlHelper.setStatus(user_id, STATUS_FAKE_PROFILE);
        simpleMessage(user_id, text);
    }

    private void handleFake(int user_id, JsonObject body) {
        String message;
        if (body.has("body")) message = body.get("body").getAsString();
        else if (body.has("text")) message = body.get("text").getAsString();
        else return;

        //Обработка выхода
        if (message.equals("11")) {
            sqlHelper.setStatus(user_id, STATUS_MAIN_MENU);
            mainMenuMessage(user_id, 0);
            return;
        }

        String words[] = message.split(" ");
        //Обрабатываем пол
        int gender = -1;
        try {
            gender = Integer.parseInt(words[0]);
            if (gender != 0 && gender != 1) {
                throw new Exception("0 - женский, 1 - мужской");
            }
        } catch (Exception e) {
            simpleMessage(user_id, "Неправильно введён пол: " + e.getMessage());
            return;
        }

        //Обрабатываем возраст
        int age = -1;
        try {
            age = Integer.parseInt(words[1]);
            if (!(age >= 10 && age <= 100)) {
                throw new Exception("от 10 до 100");
            }
        } catch (Exception e) {
            simpleMessage(user_id, "Неправильно введён возраст: " + e.getMessage());
            return;
        }

        //Обрабатываем город
        String city = words[2];

        //Обрабатываем рейтинг
        int rating = -1;
        try {
            rating = Integer.parseInt(words[3]);
            if (!(rating > 0 && rating <= 100)) {
                throw new Exception("от 1 до 100");
            }
        } catch (Exception e) {
            simpleMessage(user_id, "Неправильно введён рейтинг: " + e.getMessage());
            return;
        }

        //Обрабатываем фотку
        String finalPhoto = null;
        try {
            String responseStr = VKUtil.vk.messages().getById(VKUtil.groupActor, body.get("id").getAsInt()).executeAsString();
            JsonObject response = new Gson().fromJson(responseStr, JsonObject.class).get("response").getAsJsonObject();
            if (response.has("items")) {
                JsonObject item = response.get("items").getAsJsonArray().get(0).getAsJsonObject();

                if (item.has("attachments")) {
                    JsonArray attachments = item.get("attachments").getAsJsonArray();
                    for (JsonElement element: attachments) {
                        JsonObject attachment = element.getAsJsonObject();
                        String type = attachment.get("type").getAsString();
                        if (type.equals("photo")) {
                            JsonObject photo = attachment.get("photo").getAsJsonObject();
                            int photo_id = photo.get("id").getAsInt();
                            int photo_owner = photo.get("owner_id").getAsInt();

                            finalPhoto = "photo" + photo_owner + "_" + photo_id;

                            if (photo.has("access_key")) {
                                finalPhoto += "_" + photo.get("access_key").getAsString();
                            }
                            break;
                        }
                    }
                }
            }
        } catch (ClientException e) {
            System.err.println("Не удалось получить сообщение с фотографией");
            simpleMessage(user_id, "Ошибка " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (finalPhoto == null) {
            simpleMessage(user_id, "Нужно ещё фотку прикрепить");
        } else {
            int randomUserId = random.nextInt();
            if (randomUserId > 0) randomUserId *= -1;
            sqlHelper.addFakeUser(randomUserId, gender, age, city, finalPhoto, rating, random.nextInt(20) + 10);
            simpleMessage(user_id, "Челик создан");
        }
    }

    private String getCurrentDate() {
        return dateFormat.format(new Date());
    }
}
