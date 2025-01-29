document.addEventListener('DOMContentLoaded', () => {
    const roomForm = document.getElementById('roomForm');
    const roomTable = document.querySelector('#roomTable tbody');
    const apiUrl = '/rooms';

    if (!roomForm || !roomTable) {
        console.error("Chyba: Formulář nebo tabulka nebyly nalezeny.");
        return;
    }

    function loadRooms() {
        fetch('/rooms/api') //   Požadujeme JSON z nového endpointu
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Chyba serveru: ${response.status}`);
                }
                return response.json();
            })
            .then(rooms => {
                roomTable.innerHTML = rooms.map(room => `
                    <tr>
                        <td>${room.id}</td>
                        <td>${room.name}</td>
                        <td>${room.capacity}</td>
                        <td>${room.equipment || '—'}</td>
                        <td>${room.category?.name || '—'}</td>
                        <td>
                            <button class="btn btn-warning btn-sm edit-btn" data-id="${room.id}">Upravit</button>
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${room.id}">Smazat</button>
                        </td>
                    </tr>
                `).join('');
            })
            .catch(error => console.error('❌ Chyba při načítání místností:', error));
    }

    function addRoom(event) {
        event.preventDefault(); //   Zabraňuje HTML formulářovému odeslání

        const formData = new FormData(document.querySelector('#roomForm'));
        const room = Object.fromEntries(formData.entries());

        // 🔹 Ujistíme se, že kategorie je objekt, ne jen ID
        room.category = { id: parseInt(room.categoryId, 10) };
        delete room.categoryId; //   Odstraníme duplicitní hodnotu

        fetch('/rooms', { //   Použijeme správný endpoint
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || "Chyba serveru"); });
            }
            return response.json();
        })
        .then(() => {
            document.querySelector('#roomForm').reset();
            loadRooms(); //   Po úspěchu načti znovu místnosti
        })
        .catch(error => console.error('❌ Chyba při přidávání místnosti:', error));
    }

    //   Akce na editaci a smazání místnosti
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
