document.addEventListener('DOMContentLoaded', function () {
    if (localStorage.getItem("id") !== null) {
        setupLoggedInView();
    } else {
        setupLoginView();
    }

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

        document.getElementById('loginForm').addEventListener('submit', function (event) {
            event.preventDefault();

            const formData = new FormData(this);
            const username = formData.get('username');
            const password = formData.get('password');

            const queryString = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;
            fetch(`/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: queryString
            })
            .then(response => response.json())
            .then(data => {
                checkLogin(data);
            })
            .catch(error => console.error('Error:', error));
        });

        document.getElementById('signUp').addEventListener('click', function(event) {
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

            document.getElementById('signUpForm').addEventListener('submit', function (event) {
                event.preventDefault();
    
                const formData = new FormData(this);
                const username = formData.get('username');
                const password = formData.get('password');
    
                const queryString = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;
                fetch(`/signUp`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: queryString
                })
                .then(response => response.json())
                .then(data => {
                    checkSignUp(data);
                })
                .catch(error => console.error('Error:', error));
            });
        });
    }

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

        document.getElementById('logout').addEventListener('click', logout);

        document.getElementById('searchButton').addEventListener('click', function(event) {
            if (document.getElementById('hiddenSearch').style.display === 'none') {
                document.getElementById('hiddenSearch').style.display = 'block';
            } else {
                document.getElementById('hiddenSearch').style.display = 'none';
            }
            
            document.getElementById('searchForm').addEventListener('submit', function (event) {
                event.preventDefault();
    
                const formData = new FormData(this);
                const name = formData.get('name');
    
                const queryString = `id=${encodeURIComponent(localStorage.getItem("id"))}&name=${encodeURIComponent(name)}`;
    
                fetch(`/search?${queryString}`)
                .then(response => response.json())
                .then(data => {
                    updateSearch(data);
                    backToMainMenu()
                })
                .catch(error => console.error('Error:', error));
            });
        })

        document.getElementById('rateButton').addEventListener('click', function(event) {
            if (document.getElementById('hiddenRate').style.display === 'none') {
                document.getElementById('hiddenRate').style.display = 'block';
            } else {
                document.getElementById('hiddenRate').style.display = 'none';
            }
            
            document.getElementById('rateForm').addEventListener('submit', function (event) {
                event.preventDefault();

                const formData = new FormData(this);
                const restr_name = formData.get('name');
                const rating_type = formData.get('rating_type');
                const rating = formData.get('rating');
    
                const queryString = `rating_type=${encodeURIComponent(rating_type)}&rating=${encodeURIComponent(rating)}&user_id=${encodeURIComponent(localStorage.getItem('id'))}&restr_name=${encodeURIComponent(restr_name)}`;
                fetch(`/rate`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: queryString
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        window.location.replace('/');
                    } else {
                        document.getElementById('error').innerHTML = '<h4>Sorry this restaurant does not exist in our records</h4>';
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        })

        document.getElementById('recommendButton').addEventListener('click', function(event) {
            
            if (document.getElementById('hiddenRecommend').style.display === 'none') {
                document.getElementById('hiddenRecommend').style.display = 'block';
            } else {
                document.getElementById('hiddenRecommend').style.display = 'none';
            }
            
            document.getElementById('avgRecommend').addEventListener('click', function (event) {
                event.preventDefault();
    
                fetch('/avgRecommend')
                .then(response => response.json())
                .then(data => {
                    document.getElementById('main').innerHTML = `<h4>Here are some popular restaurants you might like:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
                    backToMainMenu();
                })
                .catch(error => console.error('Error:', error));
            });

            document.getElementById('smartRecommend').addEventListener('click', function (event) {
                event.preventDefault();
                const queryString = `user_id=${encodeURIComponent(localStorage.getItem('id'))}`;

                fetch(`/smartRecommend?${queryString}`)
                .then(response => response.json())
                .then(data => {
                    document.getElementById('main').innerHTML = `<h4>Here are some popular restaurants you might like based on your preferences:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
                    backToMainMenu();
                })
                .catch(error => console.error('Error:', error));
            });
        })
    }

    function checkLogin(data) {
        if (data.verification) {
            localStorage.setItem('id', data.id);
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username and/or password are incorrect.`;
        }
    }

    function checkSignUp(data) {
        if (data.verification) {
            localStorage.setItem('id', data.id);
            console.log('id');
            window.location.replace('/');
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username is already taken`;
        }
    }

    function updateSearch(data) {
        if (data.error) {
            document.getElementById('error').innerHTML = `<h3> ${data.error_message} </h3>`;
            document.getElementById('main').innerHTML = `<h4>Here are some other popular restaurants you might like:</h4><br><h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
        } else {
            document.getElementById('main').innerHTML = `<h4>${restaurantFormat(data.restaurant, data.ratings)}</h4>`;
        }
    }

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

    function multiRestaurantFormat(restaurants, ratings) {
        let output = `<ol>`;

        for (let key in restaurants) {
            output += `<li>${restaurantFormat(restaurants[key], ratings[key])}</li><br><br>`;
        }
        output += `</ol>`;

        return output;
    }

    function logout() {
        console.log('Logout');
        localStorage.removeItem('id');
        window.location.replace('/');
    }

    function backToMainMenu() {
        document.getElementById('main').innerHTML += `<div><br><br><button id="backToMainMenu" class="btn btn-primary">Main Menu</button></div>`;

        document.getElementById('backToMainMenu').addEventListener('click', function (event) {
            window.location.replace('/');
        });
    }
});
