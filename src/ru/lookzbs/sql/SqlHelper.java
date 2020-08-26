package ru.lookzbs.sql;

import org.sql2o.Connection;
import org.sql2o.Query;
import ru.lookzbs.model.FullProfile;
import ru.lookzbs.model.Profile;
import ru.lookzbs.model.TopProfile;

import java.util.List;

public class SqlHelper {

    public static final int METRIC_NEXT = 0;
    public static final int METRIC_STATUS = 1;
    public static final int METRIC_RATE = 2;

    //Register new user
    public void registerUser(int user_id) {
        String sql = "INSERT INTO users (user_id, status, shown) VALUES (:user_id, 21, 0);";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql).addParameter("user_id", user_id).executeUpdate();
        }
    }

    public boolean isUserRegistered(int user_id) {
        Integer result;
        String sql = "SELECT user_id FROM users WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            result = con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(Integer.class);
            return result != null;
        }
    }

    public int getStatus(int user_id) {
        Integer status = 0;
        String sql = "SELECT status FROM users WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            status = con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(Integer.class);
            return status;
        }
    }

    public void setStatus(int user_id, int status) {
        String sql = "UPDATE users SET status = :status WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("status", status)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setGender(int user_id, int gender) {
        String sql = "UPDATE users SET gender = :gender, shown = 0 WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("gender", gender)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setAge(int user_id, int age) {
        String sql = "UPDATE users SET age = :age WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("age", age)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setCity(int user_id, String city) {
        String sql = "UPDATE users SET city = :city WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("city", city)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setPhoto(int user_id, String photo) {
        String sql = "UPDATE users SET photo = :photo, shown = 1, rating = 0, rates_count = 0 WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("photo", photo)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setGenderFilter(int user_id, int genderFilter) {
        String sql = "UPDATE users SET gender_filter = :filter, status = 32 WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("filter", genderFilter)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public void setCityFilter(int user_id, int cityFilter) {
        String sql = "UPDATE users SET mycity = :filter, status = 10 WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("filter", cityFilter)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public Profile getCursorUser(int user_id) {
        String sql = "SELECT gender, age, city, photo FROM users WHERE user_id = (SELECT cursor_id FROM users WHERE user_id = :user_id);";
        try (Connection con = SqlConnector.sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(Profile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getCursorId(int user_id) {
        Integer cursor_id = 0;
        String sql = "SELECT cursor_id FROM users WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            cursor_id = con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(Integer.class);
            return cursor_id;
        }
    }

    public Profile nextUser(int user_id, int gender, int ageFrom, int ageTo, boolean inCity, String city) {
        try (Connection con = SqlConnector.sql2o.beginTransaction()) {
            String sql1 = "SET @id = (SELECT user_id FROM users WHERE user_id = :user_id);";
            con.createQuery(sql1).addParameter("user_id", user_id).executeUpdate();

            StringBuilder sql2 = new StringBuilder("SET @tmp = (SELECT user_id FROM dev.users WHERE user_id NOT IN (SELECT reciever FROM rates WHERE sender = @id) AND age >= :ageFrom AND age <= :ageTo AND shown = 1 AND user_id != @id");
            if (inCity) {
                sql2.append(" AND city = :city");
            }
            if(gender != 2) {
                sql2.append(" AND gender = :gender");
            }

            sql2.append(" ORDER BY RAND() LIMIT 1);");
            Query query = con.createQuery(sql2.toString())
                    .addParameter("ageFrom", ageFrom)
                    .addParameter("ageTo", ageTo);
            if (inCity) {
                query.addParameter("city", city);
            }
            if(gender != 2) {
                query.addParameter("gender", gender);
            }

            query.executeUpdate();

            String sql3 = "UPDATE users SET cursor_id = @tmp WHERE user_id = @id;";
            con.createQuery(sql3).executeUpdate();

            String sql4 = "SELECT gender, age, city, photo FROM users WHERE user_id = @tmp;";


            Profile pf = con.createQuery(sql4).executeAndFetchFirst(Profile.class);
            con.commit();
            return pf;
        }
    }

    public int rateUser(int user_id, int rate) {
        try (Connection con = SqlConnector.sql2o.beginTransaction()) {
            String sql1 = "SET @id = (SELECT user_id FROM users WHERE user_id = :user_id);";
            con.createQuery(sql1).addParameter("user_id", user_id).executeUpdate();

            String sql2 = "SET @tmp = (SELECT cursor_id FROM users WHERE user_id = @id);";
            con.createQuery(sql2).executeUpdate();

            String sql6 = "SELECT CASE WHEN last_activity < NOW() - INTERVAL 1 DAY THEN user_id ELSE 0 END AS result FROM users WHERE user_id = @tmp";
            int result = con.createQuery(sql6).executeAndFetchFirst(Integer.class);

            String sql3 = "UPDATE users SET rating = (rating * rates_count + :rate) / (rates_count + 1), rates_count = rates_count + 1 WHERE user_id = @tmp;";
            con.createQuery(sql3).addParameter("rate", rate).executeUpdate();

            String sql4 = "INSERT INTO rates (sender, reciever) VALUES (@id, @tmp);";
            con.createQuery(sql4).executeUpdate();

            String sql5 = "UPDATE users SET cursor_id = 0, rated_users = rated_users + 1, last_activity = NOW() WHERE user_id = @id;";
            con.createQuery(sql5).executeUpdate();

            if (result != 0) {
                String sql7 = "UPDATE users SET last_activity = NOW() WHERE user_id = @tmp";
                con.createQuery(sql7).executeUpdate();
            }

            con.commit();
            System.out.println(result);
            return result;
        }
    }

    public FullProfile getProfile(int user_id) {
        String sql = "SELECT gender, age, city, photo, rating, rates_count, shown, mycity, gender_filter FROM users WHERE user_id = :user_id;";
        try (Connection con = SqlConnector.sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(FullProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateLastActivity(int user_id) {
        String sql = "UPDATE users SET last_activity = NOW() WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public List<TopProfile> getTop(String city, int rates_count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT gender, age, city, photo, rating FROM users WHERE rates_count > :rates_count AND shown = 1");
        if (city != null) {
            stringBuilder.append(" AND city = :city");
        }
        stringBuilder.append(" ORDER BY rating DESC LIMIT 10;");

        try (Connection con = SqlConnector.sql2o.open()) {
            Query query = con.createQuery(stringBuilder.toString())
                    .addParameter("rates_count", rates_count);
            if (city != null) {
                query.addParameter("city", city);
            }
            return query.executeAndFetch(TopProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setShownProfile(int user_id, int shown) {
        String sql = "UPDATE users SET shown = :shown WHERE user_id = :user_id;";
        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .addParameter("shown", shown)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementMetric(String date, int metric) {
        String sql = null;
        switch (metric) {
            case METRIC_NEXT:
                sql = "INSERT INTO metrics (id, next) VALUES (:date, 1) ON DUPLICATE KEY UPDATE next = next + 1;";
                break;
            case METRIC_STATUS:
                sql = "INSERT INTO metrics (id, status) VALUES (:date, 1) ON DUPLICATE KEY UPDATE status = status + 1;";
                break;
            case METRIC_RATE:
                sql = "INSERT INTO metrics (id, rate) VALUES (:date, 1) ON DUPLICATE KEY UPDATE rate = rate + 1;";
                break;
        }


        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("date", date)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFakeUser(int user_id, int gender, int age, String city, String photo, int rating, int rates_count) {
        String sql = "INSERT INTO users (user_id, gender, age, city, photo, shown, rating, rates_count) VALUES (:user_id, :gender, :age, :city, :photo, 1, :rating, :rates_count);";
        try (Connection con = SqlConnector.sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .addParameter("gender", gender)
                    .addParameter("age", age)
                    .addParameter("city", city)
                    .addParameter("photo", photo)
                    .addParameter("rating", rating)
                    .addParameter("rates_count", rates_count)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAdmin(int user_id) {
        Boolean result = false;
        String sql = "SELECT admin FROM users WHERE user_id = :user_id;";

        try (Connection con = SqlConnector.sql2o.open()) {
            result = con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetchFirst(Boolean.class);
            return result;
        }
    }
}
