// File header comment

public class User {
    private String username;
    private String password;
    private Restaurant[] favourites;

    /**
     * Get the username of the user
     * @return The user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the user's hashed password
     * @return The user's hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the user's favourite restaurants
     * @return The user's favourite restaurants
     */
    public Restaurant[] getFavourites() {
        Restaurant[] favourites = new Restaurant[this.favourites.length];

        int i = 0;
        for (Restaurant restaurant : this.favourites) {
            favourites[i] = new Restaurant(restaurant);
            i++;
        }
        return favourites;
    }

    /**
     * Set the username of the user
     * @param username The user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the user's password
     * @param password The password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the user's favourites
     * @param favourites The user's favourites
     */
    public void setFavourites(Restaurant[] favourites) {
        this.favourites = new Restaurant[favourites.length];

        int i = 0;
        for (Restaurant restaurant : favourites) {
            this.favourites[i] = new Restaurant(restaurant);
            i++;
        }
    }
}
