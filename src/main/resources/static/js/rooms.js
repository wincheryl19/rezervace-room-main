document.addEventListener('DOMContentLoaded', () => {
    const roomForm = document.getElementById('roomForm');
    const roomTable = document.getElementById('roomTable');
    const apiUrl = '/api/rooms';

    // Načtení všech místností
    function loadRooms() {
        fetch(apiUrl)
            .then(response => response.json())
            .then(rooms => {
                const rows = rooms.map(room => `
                    <tr>
                        <td>${room.id}</td>
                        <td>${room.name}</td>
                        <td>${room.capacity}</td>
                        <td>${room.equipment || '—'}</td>
                        <td>${room.category.name}</td>
                        <td>
                            <button class="btn btn-warning btn-sm edit-btn" data-id="${room.id}">Editovat</button>
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${room.id}">Smazat</button>
                        </td>
                    </tr>
                `).join('');
                roomTable.querySelector('tbody').innerHTML = rows;
            });
    }

    // Přidání nebo úprava místnosti
    roomForm.addEventListener('submit', event => {
        event.preventDefault();
        const formData = new FormData(roomForm);
        const room = Object.fromEntries(formData.entries());
        const method = room.id ? 'PUT' : 'POST';
        const url = room.id ? `${apiUrl}/${room.id}` : apiUrl;

        fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        })
            .then(() => {
                loadRooms();
                roomForm.reset();
            });
    });

    // Akce na editaci a smazání místnosti
    roomTable.addEventListener('click', event => {
        if (event.target.classList.contains('edit-btn')) {
            const id = event.target.dataset.id;
            fetch(`${apiUrl}/${id}`)
                .then(response => response.json())
                .then(room => {
                    Object.entries(room).forEach(([key, value]) => {
                        if (roomForm.elements[key]) {
                            roomForm.elements[key].value = value;
                        }
                    });
                });
        } else if (event.target.classList.contains('delete-btn')) {
            const id = event.target.dataset.id;
            fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
                .then(() => loadRooms());
        }
    });

    loadRooms();
});
