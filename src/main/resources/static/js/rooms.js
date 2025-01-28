document.addEventListener('DOMContentLoaded', () => {
    const roomForm = document.getElementById('roomForm');
    const roomTable = document.getElementById('roomTable tbody');
    const apiUrl = '/api/rooms';

    function loadRooms() {
        fetch(apiUrl)
            .then(response => response.json())
            .then(rooms => {
                roomTable.innerHTML = '';
                rooms.forEach(room => {
                    const row = `
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
                    `;
                    roomTable.insertAdjacentHTML('beforeend', row);
                });
            })
            .catch(error => console.error("Chyba při načítání místností:", error));
    }

    //   Opravené přidání místnosti přes AJAX
    roomForm.addEventListener('submit', event => {
        event.preventDefault();

        const formData = new FormData(roomForm);
        const room = {
            name: formData.get("name"),
            capacity: parseInt(formData.get("capacity")),
            equipment: formData.get("equipment"),
            category: { id: parseInt(formData.get("categoryId")) } //   Oprava formátu JSON
        };

        fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        })
        .then(response => {
            if (!response.ok) throw new Error("Chyba při přidávání místnosti");
            return response.json();
        })
        .then(() => {
            loadRooms(); // Aktualizuje tabulku
            roomForm.reset(); // Vyčistí formulář
        })
        .catch(error => console.error("Chyba:", error));
    });

    loadRooms();
});
