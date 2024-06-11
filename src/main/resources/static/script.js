/*
 *  Script File that controls website HTTP requests
 *
 *  Completely Programmed by Aaron Avram
 *  Date Programmed: June 14, 2024
 */

// Event Listener for the DOM Content
document.addEventListener('DOMContentLoaded', function () {

    // If there is no current user go to login page
    if (localStorage.getItem("id") !== null) {
        setupLoggedInView();  
    } 
    
    // If there is a current user go to logged in page
    else {
        setupLoginView();
    }

    /**
     * Gives user the login page so they can enter their username and password
     * or sign up with a new account
     */
    function setupLoginView() {
        document.getElementById('main').innerHTML = `
            <div class="mb-3">
                <h5>Login to Restaurant Tracker</h5>
                <form id="loginForm">
                    <div class="mb-3">
                        <input autocomplete="off" autofocus class="form-control mx-auto w-auto" id="username" name="username" placeholder="Username" type="text">
                    </div>
                    <div class="mb-3">
                        <input class="form-control mx-auto w-auto" id="password" name="password" placeholder="Password" type="password">
                    </div>
                    <button class="btn btn-primary" type="submit">Log In</button>
                </form>
                <div class="mb-3">
                    <br>
                    <button id="signUp" class="btn btn-primary">Don't Have an Account? Sign up!</button>
                </div>
                <div class="mb-3">
                <br>
                <p id="errorMessage"></p>
            </div>
            </div>
        `;

        // Event Listener for login form
        document.getElementById('loginForm').addEventListener('submit', function (event) {
            event.preventDefault();

            const formData = new FormData(this);
            const username = formData.get('username');
            const password = formData.get('password');

            // Create HTTP request body
            const queryString = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;

            // Send request and wait for response
            fetch(`/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: queryString
            })

            // Parse response and update website
            .then(response => response.json())
            .then(data => {
                checkLogin(data);
            })
            .catch(error => console.error('Error:', error));
        });

        // Event listener for signUp
        document.getElementById('signUp').addEventListener('click', function(event) {

            // If user clicks on signUp, display sign up page
            document.getElementById('main').innerHTML = `
            <div class="mb-3">
                <h5>Make a Restaurant Tracker Account</h5>
                <form id="signUpForm">
                    <div class="mb-3">
                        <input autocomplete="off" autofocus class="form-control mx-auto w-auto" id="username" name="username" placeholder="Username" type="text">
                    </div>
                    <div class="mb-3">
                        <input class="form-control mx-auto w-auto" id="password" name="password" placeholder="Password" type="password">
                    </div>
                    <button class="btn btn-primary" type="submit">Create Account</button>
                </form>
                <div class="mb-3">
                <br>
                <p id="errorMessage"></p>
            </div>
            </div>
            `;

            // Event listener for the sign up form
            document.getElementById('signUpForm').addEventListener('submit', function (event) {
                event.preventDefault();
    
                const formData = new FormData(this);
                const username = formData.get('username');
                const password = formData.get('password');
    
                // Create HTTP request body
                const queryString = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;

                // Send request and wait for response
                fetch(`/signUp`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: queryString
                })

                // Parse response and update website
                .then(response => response.json())
                .then(data => {
                    checkSignUp(data);
                })
                .catch(error => console.error('Error:', error));
            });
        });
    }

    // Display the web content for a user who is logged in
    function setupLoggedInView() {
        document.getElementById('main').innerHTML = `
            <div id="welcomeMessage" class="mb-3">
            </div>        
            <div id="message">
            </div>
            <div>
            <button id="searchButton" class="btn btn-primary">Search Restaurants</button>
            <div id="hiddenSearch" style="display:none">
            <br>
            <h3>Search Restaurant by Name</h3>
            <form id="searchForm">
                <input id="name" name="name" type="text" placeholder="Restaurant Name">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
            </div>
            </div>
            <br>
            <br>
            <div>
            <button id="rateButton" class="btn btn-primary">Rate Restaurants</button>
            <div id="hiddenRate" style="display:none">
            <br>
            <h3>Rate a Restaurant</h3>
            <br>
            <form id="rateForm">
                <label for="name">Restaurant Name</label>
                <input id="name" name="name" type="text">
                <label for="rating_type">Type of Rating</label>
                <select id="rating_type" name="rating_type">
                    <option value="Taste">Taste</option>
                    <option value="Ambiance">Ambiance</option>
                    <option value="Worth The Price?">Worth The Price</option>
                    <option value="Enjoyment">Enjoyment</option>
                    <option value="Hygiene">Hygiene</option>
                    <option value="Service">Service</option>
                </select>
                <label for="rating">Rating</label>
                <select id="rating" name="rating">
                    <option value="0">0</option>
                    <option value="0.5">0.5</option>
                    <option value="1">1</option>
                    <option value="1.5">1.5</option>
                    <option value="2">2</option>
                    <option value="2.5">2.5</option>
                    <option value="3">3</option>
                    <option value="3.5">3.5</option>
                    <option value="4">4</option>
                    <option value="4.5">4.5</option>
                    <option value="5">5</option>
                </select>
                <br>
                <br>
                <button type="submit" class="btn btn-primary">Rate</button>
            </form>
            </div>
            </div>
            <br>
            <br>
            <div>
            <button id="recommendButton" class="btn btn-primary">Get Recommendations</button>
            <div id="hiddenRecommend" style="display:none">
                <br>
                <button id="smartRecommend" class="btn btn-primary">Personalized Recommendations</button>
                <button id="avgRecommend" class="btn btn-primary">General Recommendations</button>
            </div>
            </div>
            <br>
            <br>
            <div>
            <button id="logout" class="btn btn-primary">Logout</button>
            </div>
        `;

        // When user presses on the logout button, call the logout function
        document.getElementById('logout').addEventListener('click', logout);

        // Event listener for the search
        document.getElementById('searchButton').addEventListener('click', function(event) {
            // Toggle display based on button pressing
            if (document.getElementById('hiddenSearch').style.display === 'none') {
                document.getElementById('hiddenSearch').style.display = 'block';
            } else {
                document.getElementById('hiddenSearch').style.display = 'none';
            }
            
            // Event listener for the search form
            document.getElementById('searchForm').addEventListener('submit', function (event) {
                event.preventDefault();
    
                const formData = new FormData(this);
                const name = formData.get('name');
    
                // Create HTTP request body
                const queryString = `id=${encodeURIComponent(localStorage.getItem("id"))}&name=${encodeURIComponent(name)}`;
    
                // Send request and wait for response
                fetch(`/search?${queryString}`)

                // parse response and update website
                .then(response => response.json())
                .then(data => {
                    updateSearch(data);
                    backToMainMenu()
                })
                .catch(error => console.error('Error:', error));
            });
        })

        // Event listener for the rate button
        document.getElementById('rateButton').addEventListener('click', function(event) {
            // Toggle display based on button pressing
            if (document.getElementById('hiddenRate').style.display === 'none') {
                document.getElementById('hiddenRate').style.display = 'block';
            } else {
                document.getElementById('hiddenRate').style.display = 'none';
            }
            
            // Event listener for the rate form
            document.getElementById('rateForm').addEventListener('submit', function (event) {
                event.preventDefault();

                const formData = new FormData(this);
                const restr_name = formData.get('name');
                const rating_type = formData.get('rating_type');
                const rating = formData.get('rating');
    
                // Create HTTP request body
                const queryString = `rating_type=${encodeURIComponent(rating_type)}&rating=${encodeURIComponent(rating)}&user_id=${encodeURIComponent(localStorage.getItem('id'))}&restr_name=${encodeURIComponent(restr_name)}`;
                
                // Send request and wait for response
                fetch(`/rate`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: queryString
                })

                // Parse response and update website
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        window.location.replace('/');
                    } else {
                        document.getElementById('title').innerHTML += '<br><h3>Sorry this restaurant does not exist in our records</h3>';
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        })

        // Event listener for the recommend button
        document.getElementById('recommendButton').addEventListener('click', function(event) {
            // Toggle display based on button pressing
            if (document.getElementById('hiddenRecommend').style.display === 'none') {
                document.getElementById('hiddenRecommend').style.display = 'block';
            } else {
                document.getElementById('hiddenRecommend').style.display = 'none';
            }
            
            // Event Listener for average recommend button
            document.getElementById('avgRecommend').addEventListener('click', function (event) {
                event.preventDefault();
    
                // Send HTTP request and wait for response
                fetch('/avgRecommend')

                // Parse response and update page
                .then(response => response.json())
                .then(data => {
                    document.getElementById('main').innerHTML = `<h4>Here are some popular restaurants you might like:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
                    backToMainMenu();
                })
                .catch(error => console.error('Error:', error));
            });

            // Event listener for the smart recommend button
            document.getElementById('smartRecommend').addEventListener('click', function (event) {
                event.preventDefault();

                // Create HTTP request body
                const queryString = `user_id=${encodeURIComponent(localStorage.getItem('id'))}`;

                // Send request and wait for response
                fetch(`/smartRecommend?${queryString}`)

                // Parse response and update website
                .then(response => response.json())
                .then(data => {
                    document.getElementById('main').innerHTML = `<h4>Here are some popular restaurants you might like based on your preferences:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
                    backToMainMenu();
                })
                .catch(error => console.error('Error:', error));
            });
        })
    }

    /**
     * Checks if a user was successfully logged in or not
     * @param {*} data The container for the verification and id values
     */
    function checkLogin(data) {
        if (data.verification) {
            localStorage.setItem('id', data.id);
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username and/or password are incorrect.`;
        }
    }

    /**
     * Checks if a user was successfully signed up
     * @param {*} data The container for the verification and id values
     */
    function checkSignUp(data) {
        if (data.verification) {
            localStorage.setItem('id', data.id);
            console.log('id');
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username is already taken`;
        }
    }

    /**
     * Updates the website to display the search results
     * @param {*} data The container for the error verifier, error message and restaurant data
     */
    function updateSearch(data) {
        if (data.error) {
            document.getElementById('title').innerHTML += `<h3> ${data.error_message} </h3>`;
            document.getElementById('main').innerHTML = `<h4><br>Here are some other popular restaurants you might like:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
        } else {
            document.getElementById('main').innerHTML = `<h4>${restaurantFormat(data.restaurant, data.ratings)}</h4>`;
        }
    }

    /**
     * Formats a restaurant on the website
     * @param {*} restaurant The restaurant data
     * @param {*} ratings The restaurant's rating data
     * @returns The html containing the new data
     */
    function restaurantFormat(restaurant, ratings) {
        console.log(ratings);

        return `
                <h4><b>Restaurant Name:</b> ${restaurant.name}</h4>
                <p><b>Restaurant Location:</b> ${restaurant.location}<p>
                <h5><b>Menu:</b></h5>
                <ul>
                    ${Object.entries(restaurant.menu).map(([key, value]) => `<li> <b>Item:</b> ${key} &emsp; <b>Price:</b> ${value}`)}
                </ul>

                <br>
                <h5><b>Tags:</b></h5>
                <ul>
                    ${Object.entries(restaurant.tags).map(([key, value]) => `<li> <b>${key}:</b> ${value} &emsp; <b>Rating: </b> ${ratings[value]}`)}
                </ul>
        `;
    }

    /**
     * Formats multiple restaurants on the website
     * @param {*} restaurants The restaurant data
     * @param {*} ratings The restaurant rating data
     * @returns The html containing the new data
     */
    function multiRestaurantFormat(restaurants, ratings) {
        let output = `<ol>`;

        for (let key in restaurants) {
            output += `<li>${restaurantFormat(restaurants[key], ratings[key])}</li><br><br>`;
        }
        output += `</ol>`;

        return output;
    }

    /**
     * Logs the user out of the website
     */
    function logout() {
        console.log('Logout');
        localStorage.removeItem('id');
        window.location.replace('/');
    }

    /**
     * Routes the user back to the main menu
     */
    function backToMainMenu() {
        document.getElementById('main').innerHTML += `<div><br><br><button id="backToMainMenu" class="btn btn-primary">Main Menu</button></div>`;

        document.getElementById('backToMainMenu').addEventListener('click', function (event) {
            window.location.replace('/');
        });
    }
});
