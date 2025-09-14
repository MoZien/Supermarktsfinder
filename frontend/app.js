const map = L.map('map').setView([51.1657, 10.4515], 6); // Germany center
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; OpenStreetMap-Mitwirkende'
}).addTo(map);

let markers = [];

const form = document.getElementById('searchForm');
const resultsEl = document.getElementById('results');

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  const address = document.getElementById('address').value;
  const city = document.getElementById('city').value;
  const radius = document.getElementById('radius').value;

  const params = new URLSearchParams({ address, city, radius });
  const res = await fetch(`/api/supermarkets?${params.toString()}`);
  if (!res.ok) {
    resultsEl.innerHTML = '<p>Fehler bei der Suche.</p>';
    return;
  }
  const data = await res.json();
  displayResults(data);
});

function displayResults(data) {
  // Clear previous markers
  markers.forEach(m => map.removeLayer(m));
  markers = [];

  resultsEl.innerHTML = '';
  data.forEach(item => {
    const div = document.createElement('div');
    div.className = 'result-item';
    div.innerHTML = `<strong>${item.name || 'Unbenannter Supermarkt'}</strong><br/>${item.address || ''}`;
    resultsEl.appendChild(div);

    if (item.lat && item.lon) {
      const marker = L.marker([item.lat, item.lon]).addTo(map);
      marker.bindPopup(`<b>${item.name || 'Supermarkt'}</b><br/>${item.address || ''}`);
      markers.push(marker);
    }
  });

  if (data.length > 0 && data[0].lat && data[0].lon) {
    map.setView([data[0].lat, data[0].lon], 13);
  }
}
