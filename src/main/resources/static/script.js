if (localStorage.getItem("id") !== null) {
    document.getElementById('login').innerHTML = `
        <div class="mb-3">
            <h3>Login to Restaurant Tracker</h3>
            <br><br>
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

    document.getElementById('loginForm').addEventListener('submit', function(event) {
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
        .then (data => {
            console.log(data);
            checkLogin(data);
        })
        .catch(error => console.error('Error:', error));
    })

    function checkLogin(data) {
        if (data.verification) {
            localStorage.setItem("id", data.id);
            window.location.replace("/");
        } else {
            document.getElementById('errorMessage').innerText = `Sorry this username and/or password are incorrect.`
        }
    }

}

document.getElementById('title').innerHTML += `<p>Welcome to Restaurant Tracker! You can search, rate and get recommendations for restaurants in the GTA!</p>`;
document.getElementById('main').innerHTML = `
    <div id="welcomeMessage" class="mb-3">
    </div>        
    <div id="message">
    </div>
    <div>
    <button id="1" class="btn btn-primary">Search Restaurants</button>
    <div id="hiddenContent1" style="display:none">
    <h3>Search Restaurant by Name</h3>
    <form id="searchForm">
        <label for="name">Restaurant Name</label>
        <input id="name" name="name" type="text">
        <button type="submit" class="btn btn-primary">Search</button>
    </form>
    </div>
    </div>
    <br>
    <br>
    <div>
    <button id="2" class="btn btn-primary">Rate Restaurants</button>
    <div id="hiddenContent2" style="display:none">
    <h3>Rate a Restaurant</h3>
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
        <button type="submit" class="btn btn-default">Search</button>
    </form>
    </div>
    </div>
    <br>
    <br>
    <div>
    <button id="3" class="btn btn-primary">Get Recommendations</button>
    <div id="hiddenContent3" style="display:none">
        <button id="smartRecommend">Personalized Recommendations</button>
        <button id="avgRecommend">General Recommendations</button>
    </div>
    </div>
    <br>
    <br>
    <div>
    <button id="logout" class="btn btn-primary">Logout</button>
    </div>
    <div id="data">
    </div>
`;


document.getElementById('searchForm').addEventListener('submit', function(event) {
    event.preventDefault();
    console.log('New Hello')

    const formData = new FormData(this);
    const name = formData.get('name');

    const queryString = `id=${encodeURIComponent(localStorage.getItem("id"))}&name=${encodeURIComponent(name)}`;

    fetch(`/search?${queryString}`)
    .then(response => response.json())
    .then (data => {
        updateSearch(data);
    })
    .catch(error => console.error('Error:', error));

})

function updateSearch(data) {
    document.getElementById('form').innerHTML = ``;
    if (data.error) {
        console.log(data) 
        document.getElementById('error').innerHTML = `<h3> ${data.error_message} </h3>`;
        document.getElementById('data').innerHTML = `<h4>Here are some other popular restaurants you might like:</h4><ul></ul> <h4>${multiRestaurantFormat(data.restaurant, data.ratings)}</h4>`;
    } else {
        document.getElementById('data').innerHTML = `<h4>${restaurantFormat(data.restaurant, data.ratings)}</h4>`;
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

            <h5><b>Tags:</b></h5>
            <ul>
                ${Object.entries(restaurant.tags).map(([key, value]) => `<li> <b>${key}:</b>${value} &emsp; <b>Rating: </b> ${ratings[value]}`)}
            </ul>
    `;
}

function multiRestaurantFormat(restaurants, ratings) {
    output = `<ol>`

    for (let key in restaurants) {
        output += `<li>${restaurantFormat(restaurants[key], ratings[key])}</li><br>`
    }
    output += `</ol>`

    return output
}

console.log("testing 1");
document.addEventListener("DOMContentLoaded", (event) => {
    console.log("testing 2");
    document.getElementById('logout').addEventListener('click', function(event) {
        console.log("testing 3");
        event.preventDefault();
        console.log("testing 4");
        localStorage.removeItem('id');
        console.log("testing 5");
        window.location.replace('/');
    });
});
