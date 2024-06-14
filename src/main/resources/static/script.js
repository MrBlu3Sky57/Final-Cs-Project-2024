/*
 * Script File that controls website HTTP requests
 *
 * Note: Unless otherwise specified in comments, code is written by Aaron
 * Authors: Aaron Avram, Ishai Tepper
 * Date Programmed: June 14, 2024
 */

// Event Listener for the DOM Content
document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('id');

    if (userId !== null) { // If there is a current user, go to main menu
        setupLoggedInView();
    } else {
        setupLoginView(); // If there is no current user, go to login page
    }

    /**
     * Gives user the login page so they can enter their username and password
     * or sign up with a new account
     */
    function setupLoginView() {
        document.getElementById('main').innerHTML = `
            <div class="mb-3">
                <h5>Login to TAFTR</h5>
                <form id="loginForm">
                    <div class="mb-3">
                        <input autocomplete="off" autofocus class="form-control mx-auto w-auto" id="username" name="username" placeholder="Username" type="text" required>
                    </div>
                    <div class="mb-3">
                        <input class="form-control mx-auto w-auto" id="password" name="password" placeholder="Password" type="password" required>
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
    
        document.getElementById('loginForm').addEventListener('submit', handleLogin);
        document.getElementById('signUp').addEventListener('click', displaySignUpForm);

        function displaySignUpForm() {
            document.getElementById('main').innerHTML = `
                <div class="mb-3">
                    <h5>Create your TAFTR Account</h5>
                    <form id="signUpForm">
                        <div class="mb-3">
                            <input autocomplete="off" autofocus class="form-control mx-auto w-auto" id="username" name="username" placeholder="Username" type="text" required>
                        </div>
                        <div class="mb-3">
                            <input class="form-control mx-auto w-auto" id="password" name="password" placeholder="Password" type="password" required>
                        </div>
                        <button class="btn btn-primary" type="submit">Create Account</button>
                    </form>
                    <p id="errorMessage"></p>
                    <div><button id="backToLogin" class="btn btn-primary">Back</button></div>
                    <br>
                </div>
            `;

            document.getElementById('backToLogin').addEventListener('click', backToLogin);
            document.getElementById('signUpForm').addEventListener('submit', handleSignUp);
        }

        function handleLogin(event) {
            event.preventDefault();

            const formData = new FormData(event.target);
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

                // Parse response and update page
                .then(response => response.json())
                .then(data => checkLogin(data))
                .catch(error => console.error('Error:', error));
        }

        function handleSignUp(event) {
            event.preventDefault();
            const formData = new FormData(event.target);
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

                // Parse response and update page
                .then(response => response.json())
                .then(data => checkSignUp(data))
                .catch(error => console.error('Error:', error));
        }
    }

    // Display the web content for a user who is logged in
    function setupLoggedInView() {
        document.getElementById('main').innerHTML = `
            <div id="welcomeMessage" class="mb-3"></div>
            <div id="message"></div>
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
            <br><br>
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
                        <br><br>
                        <button type="submit" class="btn btn-primary">Rate</button>
                    </form>
                </div>
            </div>
            <br><br>
            <div>
                <button id="recommendButton" class="btn btn-primary">Get Recommendations</button>
                <div id="hiddenRecommend" style="display:none">
                    <br>
                    <button id="smartRecommend" class="btn btn-primary">Personalized Recommendations</button>
                    <button id="avgRecommend" class="btn btn-primary">General Recommendations</button>
                </div>
            </div>
            <br><br>
            <div class="mb-3">
                <button id="addRestrButton" class="btn btn-primary">Add a Restaurant</button>
            </div>
            <br><br>
            <div>
                <button id="logout" class="btn btn-primary">Logout</button>
            </div>
        `;

        // Changes style once logged in.     Author: Ishai Tepper
        subtitle.classList.add('hidden');
        title.classList.add('shrink');
        navbar.classList.remove('hidden');
        //logo.classList.add('hidden');

        document.getElementById('logout').addEventListener('click', logout);

        document.getElementById('searchButton').addEventListener('click', toggleSearch);
        document.getElementById('rateButton').addEventListener('click', toggleRate);
        document.getElementById('recommendButton').addEventListener('click', toggleRecommend);
        document.getElementById('addRestrButton').addEventListener('click', displayAddRestrForm);

        // Displaying form for adding restaurant    Author: Ishai Tepper
        function displayAddRestrForm() {
            document.getElementById('main').innerHTML = `
                <div class="mb-3">
                    <h5>Add a New Restaurant</h5>
                    <form id="addRestr">
                        <div class="mb-3">
                            <input autocomplete="off" autofocus class="form-control mx-auto w-auto" id="name" name="name" placeholder="Restaurant Name" type="text" required>
                        </div>
                        <div class="mb-3">
                            <input class="form-control mx-auto w-auto" id="location" name="location" placeholder="Location" type="text" required>
                        </div>
                        <div class="mb-3">
                            <textarea class="form-control mx-auto w-auto" id="menu" name="menu" placeholder="Menu (item1,price1,...)" rows="5" required></textarea>
                        </div>
                        <div class="mb-3">
                            <textarea class="form-control mx-auto w-auto" id="tags" name="tags" placeholder="Tags (tag1,desc1,...)" rows="3" required></textarea>
                        </div>
                        <button class="btn btn-primary" type="submit">Add Restaurant</button>
                    </form>
                    <p id="addRestaurantMessage"></p>
                </div>
            `;

            // Back to main menu button
            document.getElementById('main').innerHTML += `<div><br><button id="backToMainMenu" class="btn btn-primary">Main Menu</button></div>`;
            document.getElementById('backToMainMenu').addEventListener('click', () => window.location.replace('/'));

            document.getElementById('addRestr').addEventListener('submit', handleAddRestr);

            // Handles the adding of a restaurant from form    Author: Ishai Tepper
            function handleAddRestr(event) {
                event.preventDefault();
                const formData = new FormData(event.target);
                const name = formData.get('name');
                const location = formData.get('location');
                const menu = formData.get('menu');
                const tags = formData.get('tags');
            
                // Create HTTP request body
                const requestBody = `name=${encodeURIComponent(name)}&location=${encodeURIComponent(location)}&menu=${encodeURIComponent(menu)}&tags=${encodeURIComponent(tags)}`;
            
                // Send request and wait for response
                fetch(`/addRestr`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestBody
                })
                .then(response => response.json())
                .then(data => {
                    if (data.verification) {
                        document.getElementById('addRestaurantMessage').innerText = `Restaurant added successfully. ID: ${data.id}`;
                    } else {
                        document.getElementById('addRestaurantMessage').innerText = 'Failed to add restaurant.';
                    }
                })
                .catch(error => console.error('Error:', error));
            }
            
        }
    }

    function toggleSearch() {
        const hiddenSearch = document.getElementById('hiddenSearch');
        hiddenSearch.style.display = hiddenSearch.style.display === 'none' ? 'block' : 'none';
        document.getElementById('searchForm').addEventListener('submit', handleSearch);
    }

    function handleSearch(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const name = formData.get('name');

        // Create HTTP request body
        const queryString = `id=${encodeURIComponent(localStorage.getItem("id"))}&name=${encodeURIComponent(name)}`;

        // Send request and wait for response
        fetch(`/search?${queryString}`)

            // Parse response and update page
            .then(response => response.json())
            .then(data => updateSearch(data))
            .catch(error => console.error('Error:', error));
    }

    function toggleRate() {
        const hiddenRate = document.getElementById('hiddenRate');
        hiddenRate.style.display = hiddenRate.style.display === 'none' ? 'block' : 'none';
        document.getElementById('rateForm').addEventListener('submit', handleRate);
    }

    function handleRate(event) {
        event.preventDefault();
        const formData = new FormData(event.target);
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

            // Parse response and update page
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.replace('/');
                } else {
                    document.getElementById('title').innerHTML += '<br><h3>Sorry this restaurant does not exist in our records</h3>';
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function toggleRecommend() {
        const hiddenRecommend = document.getElementById('hiddenRecommend');
        hiddenRecommend.style.display = hiddenRecommend.style.display === 'none' ? 'block' : 'none';
        document.getElementById('avgRecommend').addEventListener('click', handleAvgRecommend);
        document.getElementById('smartRecommend').addEventListener('click', handleSmartRecommend);
    }

    function handleAvgRecommend(event) {
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
    }

    function handleSmartRecommend(event) {
        event.preventDefault();

        // Create HTTP request body
        const queryString = `user_id=${encodeURIComponent(localStorage.getItem('id'))}`;

        // Send request and wait for response
        fetch(`/smartRecommend?${queryString}`)

            // Parse response and update page
            .then(response => response.json())
            .then(data => {
                document.getElementById('main').innerHTML = `<h4>Here are some popular restaurants you might like based on your preferences:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
                backToMainMenu();
            })
            .catch(error => console.error('Error:', error));
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
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username is already taken`;
        }
    }

    /**
     * Checks if a restaurant was successfully added
     * @param {*} data The container for the verification and id values
     * @author Ishai Tepper
     */
    function checkAddRestr(data) {
        if (data.verification) {
            localStorage.setItem('id', data.id);
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this restaurant is already registered`;
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
        return `
            <h4><b>Restaurant Name:</b> ${restaurant.name}</h4>
            <p><b>Restaurant Location:</b> ${restaurant.location}</p>
            <h5><b>Menu:</b></h5>
            <ul>
                ${Object.entries(restaurant.menu).map(([key, value]) => `<li><b>Item:</b> ${key} &emsp; <b>Price:</b> ${value}</li>`).join('')}
            </ul>
            <br>
            <h5><b>Tags:</b></h5>
            <ul>
                ${Object.entries(restaurant.tags).map(([key, value]) => `<li><b>${key}:</b> ${value} &emsp; <b>Rating:</b> ${ratings[value]}</li>`).join('')}
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
        return `<ol>${Object.entries(restaurants).map(([key, restaurant]) => `<li>${restaurantFormat(restaurant, ratings[key])}</li><br><br>`).join('')}</ol>`;
    }

    /**
     * Logs the user out of the website
     */
    function logout() {
        localStorage.removeItem('id');
        window.location.replace('/');
    }

    /**
     * Routes the user back to the login menu
     * Author: Ishai Tepper
     */
    function backToLogin() {
        localStorage.removeItem('id');
        window.location.replace('/');
    }

    /**
     * Routes the user back to the main menu
     */
    function backToMainMenu() {
        document.getElementById('main').innerHTML += `<div><br><br><button id="backToMainMenu" class="btn btn-primary">Main Menu</button></div>`;
        document.getElementById('backToMainMenu').addEventListener('click', () => window.location.replace('/'));
    }

    /** 
     * Navbar scroll functionality
     * Author: Ishai Tepper
     */
    let prevScrollpos = window.scrollY;
    window.onscroll = () => {
        const currentScrollPos = window.scrollY;
        document.getElementById("navbar").style.top = prevScrollpos > currentScrollPos ? "0" : "-50px";
        prevScrollpos = currentScrollPos;
    };
});
