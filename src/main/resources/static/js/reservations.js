document.addEventListener('DOMContentLoaded', () => {
    const reservationTable = document.getElementById('reservationTable').querySelector('tbody');
    const apiUrl = '/reservations'; // URL endpointu

    // Načtení rezervací z backendu
    function loadReservations() {
        fetch(apiUrl)
            .then(response => response.json())
            .then(reservations => {
                const rows = reservations.map(reservation => `
                    <tr>
                        <td>${reservation.id}</td>
                        <td>${reservation.user.name}</td>
                        <td>${reservation.room.name}</td>
                        <td>${reservation.room.category.name}</td>
                        <td>${reservation.reservationDate}</td>
                        <td>${reservation.startTime || '—'}</td>
                        <td>${reservation.endTime || '—'}</td>
                        <td>${reservation.note || '—'}</td>
                    </tr>
                `).join('');
                reservationTable.innerHTML = rows;
            })
            .catch(error => console.error('Chyba při načítání rezervací:', error));
    }

    // Načti rezervace při načtení stránky
    loadReservations();
});
