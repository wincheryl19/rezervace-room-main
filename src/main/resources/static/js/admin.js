document.addEventListener('DOMContentLoaded', function () {
    const titleSelect = document.getElementById('title');
    const dateInput = document.getElementById('date');
    const startTimeSelect = document.getElementById('startTime');
    const endTimeSelect = document.getElementById('endTime');

    const editReservationStartTime = startTimeSelect ? startTimeSelect.dataset.startTime : null;
    const editReservationEndTime = endTimeSelect ? endTimeSelect.dataset.endTime : null;

    let existingReservations = [];

    // Předvyplnění časů při načtení stránky
    function prefillReservationTimes() {
        if (editReservationStartTime && editReservationEndTime) {
            startTimeSelect.add(new Option(editReservationStartTime, editReservationStartTime, true, true));
            endTimeSelect.add(new Option(editReservationEndTime, editReservationEndTime, true, true));
        }
    }

    // Načtení dostupných časových slotů
    function loadAvailableSlots() {
        const title = titleSelect.value;
        const date = dateInput.value;

        if (title && date) {
            fetch(`/admin/reservations/availableSlots?title=${title}&date=${date}`, {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            })
            .then(response => response.ok ? response.json() : Promise.reject('Chyba při načítání dostupných slotů'))
            .then(data => {
                existingReservations = data.reservations || [];
                startTimeSelect.innerHTML = '';
                endTimeSelect.innerHTML = '';

                data.availableSlots.forEach(slot => {
                    startTimeSelect.add(new Option(slot, slot));
                    endTimeSelect.add(new Option(slot, slot));
                });

                prefillReservationTimes();
                if (startTimeSelect.value) {
                    updateEndTimeOptions(startTimeSelect.value);
                }
            })
            .catch(error => console.error(error));
        }
    }

    // Aktualizace koncových časů na základě startovacího času
    function updateEndTimeOptions(selectedStart) {
        const startTime = parseTime(selectedStart);
        let firstCollisionTime = null;

        existingReservations.forEach(reservation => {
            const resStart = parseTime(reservation.startTime.split("T")[1].substring(0, 5));
            if (resStart > startTime && (!firstCollisionTime || resStart < firstCollisionTime)) {
                firstCollisionTime = resStart;
            }
        });

        Array.from(endTimeSelect.options).forEach(option => {
            const optionTime = parseTime(option.value);
            option.hidden = optionTime <= startTime || (firstCollisionTime && optionTime > firstCollisionTime);
        });

        const firstAvailableEnd = Array.from(endTimeSelect.options).find(option => !option.hidden);
        if (firstAvailableEnd) {
            endTimeSelect.value = firstAvailableEnd.value;
        }
    }

    // Připojení listenerů k formulářovým prvkům
    titleSelect.addEventListener('change', loadAvailableSlots);
    dateInput.addEventListener('change', loadAvailableSlots);
    startTimeSelect.addEventListener('change', function () {
        updateEndTimeOptions(this.value);
    });

    if (titleSelect.value && dateInput.value) {
        loadAvailableSlots();
    } else {
        prefillReservationTimes();
    }

    function parseTime(timeString) {
        const [hours, minutes] = timeString.split(':').map(Number);
        return hours + minutes / 60;
    }

    // Validace formuláře pro rezervace
    const timeForm = document.getElementById('timeForm');
    if (timeForm) {
        timeForm.addEventListener('submit', function (event) {
            const startTime = startTimeSelect.value;
            const endTime = endTimeSelect.value;
            const title = titleSelect.value;
            const date = dateInput.value;

            if (!title || !date || !startTime || !endTime) {
                alert("Vyplňte všechna pole formuláře.");
                event.preventDefault();
            } else {
                const editReservationId = this.getAttribute('data-reservation-id');
                this.action = editReservationId
                    ? `/admin/reservations/update/${editReservationId}`
                    : `/reservations/save?title=${encodeURIComponent(title)}&date=${encodeURIComponent(date)}`;
            }
        });
    }

    // Validace formuláře pro úpravu uživatele
    const userForm = document.getElementById('userForm');
    if (userForm) {
        userForm.addEventListener('submit', function (event) {
            const userId = this.getAttribute('data-user-id');
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const role = document.getElementById('role').value;

            // Kontrola, zda jsou všechna pole vyplněna
            if (!name || !email || !role) {
                alert("Vyplňte všechna pole formuláře.");
                event.preventDefault();
                return;
            }

            // Nastavení akce pro aktualizaci nebo vytvoření uživatele
            if (userId) {
                this.action = `/admin/user/update/${userId}`;
            } else {
                // Pro nového uživatele nastavíme akci pro vytvoření s parametry v URL
                this.action = `/admin/user/create?name=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&role=${encodeURIComponent(role)}`;
            }
        });
}


document.addEventListener('click', function (event) {
    if (event.target.classList.contains('btn-close-reservation')) {
        closeForm();
    }
    if (event.target.classList.contains('btn-close-user')) {
        closeUserForm();
    }
});

function closeForm() {
    window.location.href = '/admin';
}

function closeUserForm() {
    window.location.href = '/admin';
}
});
