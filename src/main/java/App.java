import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

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

        post("/", (req, res) -> {
            Map context = new HashMap<String, Object>();


            return new ModelAndView(context, "app.mustache");
        }, new MustacheTemplateEngine());

        get("/",
                (rq, rs) -> {
                    Map map = new HashMap();
                    return new ModelAndView(map, "app.mustache");
                },
                new MustacheTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}