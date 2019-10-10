import java.util.ArrayList;

import static spark.Spark.port;

public class App {
    public boolean divisible(ArrayList<Integer> list, Integer div) {
        for (Integer num : list) {
            if (num % 2 > 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}