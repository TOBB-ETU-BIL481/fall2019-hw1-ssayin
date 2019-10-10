import java.util.*;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

class App {
    public static List<Row> matcher(ArrayList<String> firmName, String[] firmSymbols,
                                      ArrayList<String> symbolIds, boolean matchCase) {

        Map<String, String> map = new HashMap<>();
        /*
         * We must make sure length of firmSymbols
         * is not bigger than the length of symbolIds - IndexOutOfBoundsException
         */
        for (int i = 0; i < firmSymbols.length; ++i)
            map.put(symbolIds.get(i), firmSymbols[i]);

        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getValue().length() > o2.getValue().length()) return -1;
            else if (o1.getValue().length() < o2.getValue().length()) return 1;
            else return 0;
        });

        ArrayList<Row> ret = new ArrayList<>();
        for (int i = 0; i < firmName.size(); ++i)
            for (Map.Entry<String, String> obj : list) {
                if (matchCase ? firmName.get(i).contains(obj.getValue()) :
                        firmName.get(i).toLowerCase().contains(obj.getValue().toLowerCase())) {
                    ret.add(new Row(obj.getKey(),
                            firmName.get(i).replaceAll((matchCase ? "" : "(?i)") + obj.getValue(),
                                    "[$0]")));
                    break;
                }
            }
        return ret;
    }


    public static ArrayList<String> processInput(String input)
    {
        Scanner sc = new Scanner(input);
        sc.useDelimiter("[;\r\n]+");

        ArrayList<String> out = new ArrayList<>();
        while (sc.hasNext()) out.add(sc.next());
        return out;
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        post("/", (req, res) -> {
            String input1 = req.queryParams("input1");
            String input2 = req.queryParams("input2");
            String input3 = req.queryParams("input3");
            boolean input4 = (req.queryParams("input4") != null);

            ArrayList<String> firmNames = processInput(input1);
            ArrayList<String> firmIds = processInput(input2);
            ArrayList<String> firmSymbols = processInput(input3);


            List<Row> out = App.matcher(firmNames, firmSymbols.toArray(new String[0]),
                    firmIds, input4);


            Map context = new HashMap<String, Object>();
            context.put("matches", out);

            return new ModelAndView(context, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/",
                (rq, rs) -> {
                    Map map = new HashMap();
                    return new ModelAndView(map, "compute.mustache");
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