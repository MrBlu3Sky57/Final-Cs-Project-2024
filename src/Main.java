// Testing stuff


import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, User> users = new HashMap<String, User>();
        Helpers.addUser("jim", "password4", users);
        users = Helpers.getUsers();


        // Map<String, Restaurant> restaurants = new HashMap<String, Restaurant>();
        // String name = "WVRST";
        // String location = "609 King St W.";
        // Map<String, Double> menu = new HashMap<String, Double>();
        // menu.put("Belgian Fries", 10.00);
        // menu.put("Berkshire Sausage", 15.00);
        // menu.put("Tamworth Sausage", 13.00);
        
        // Map<String, String> tags = new HashMap<String, String>();
        // tags.put("Cuisine", "German");
        // tags.put("Ambiance", "Lively");
        // tags.put("Price", "Moderately-Expensive");
        // tags.put("Style", "Casual");
        
        Helpers.addRating(Helpers.getUserId("jim"), Helpers.getRestrId("Zakushi"), "Taste", 1.5);
        Map<String, Restaurant> restr = Helpers.getRestr();
        String[] recs = Recommend.avgRecommend(restr, restr.size());

        for (String id : recs) {
            System.out.println(id + "|| " + restr.get(id).getName() + "|| " + restr.get(id).getLocation());
        }
    }     
}
