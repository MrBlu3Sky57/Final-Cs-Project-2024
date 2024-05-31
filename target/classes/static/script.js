document.getElementById('searchForm').addEventListener('submit', function(event) {
    event.preventDefault();
    console.log("Hello");

    const formData = new FormData(this);
    const name = formData.get('name');

    const queryString = `name=${encodeURIComponent(name)}`;

    fetch(`/search?${queryString}`)
    .then(response => response.json())
    .then (data => {
        console.log(data);
        updateSearch(data);
    })
    .catch(error => console.error('Error:', error));

})

function updateSearch(data) {
    console.log('lol', data)
    document.getElementById('body').innerHTML = `<h4> ${data} </h4>`;
}