document.getElementById('searchForm').addEventListener('submit', function(event) {
    event.preventDefault();
    console.log('New Hello')

    const formData = new FormData(this);
    const name = formData.get('name');

    const queryString = `name=${encodeURIComponent(name)}`;

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