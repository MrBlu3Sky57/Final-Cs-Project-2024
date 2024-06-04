if (localStorage.getItem("id") === null) {
    document.getElementById('login').innerHTML = `
        <div class="mb-3">
            <h3>Login to Restaurant Tracker</h3>
            <br><br>
            <form action="/login" method="post">
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
        </div>
        `;

} else {
    document.getElementById('body').innerHTML = `
        <div id="welcomeMessage">
            <p>Welcome to Restaurant Tracker! You can search, rate and get recommendations for restaurants in the GTA!</p>
        </div>        
        <div id="message">
        </div>
        <div>
        <button id="1">Search Restaurants</button>
        <div id="hiddenContent1" style="display:none">
        <h3>Search Restaurant by Name</h3>
        <form id="searchForm">
            <label for="name">Restaurant Name</label>
            <input id="name" name="name" type="text">
            <button type="submit" class="btn btn-default">Search</button>
        </form>
        </div>
        </div>
        <br>
        <br>
        <div>
        <button id="2">Rate Restaurants</button>
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
        <button id="3">Get Recommendations</button>
        <div id="hiddenContent3" style="display:none">
            <button id="smartRecommend">Personalized Recommendations</button>
            <button id="avgRecommend">General Recommendations</button>
        </div>
        </div>
        <div id="data">
        </div>
    `
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
}