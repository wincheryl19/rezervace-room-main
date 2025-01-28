document.addEventListener('DOMContentLoaded', () => {
    const categoryForm = document.getElementById('categoryForm');
    const categoryTable = document.getElementById('categoryTable');
    const apiUrl = '/api/categories';

    // Načtení všech kategorií
    function loadCategories() {
        fetch(apiUrl)
            .then(response => response.json())
            .then(categories => {
                const rows = categories.map(category => `
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.name}</td>
                        <td>
                            <button class="btn btn-warning btn-sm edit-btn" data-id="${category.id}">Editovat</button>
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${category.id}">Smazat</button>
                        </td>
                    </tr>
                `).join('');
                categoryTable.querySelector('tbody').innerHTML = rows;
            });
    }

    // Přidání nebo úprava kategorie
    categoryForm.addEventListener('submit', event => {
        event.preventDefault();
        const formData = new FormData(categoryForm);
        const category = Object.fromEntries(formData.entries());
        const method = category.id ? 'PUT' : 'POST';
        const url = category.id ? `${apiUrl}/${category.id}` : apiUrl;

        fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(category)
        })
            .then(() => {
                loadCategories();
                categoryForm.reset();
            });
    });

    // Akce na editaci a smazání kategorie
    categoryTable.addEventListener('click', event => {
        if (event.target.classList.contains('edit-btn')) {
            const id = event.target.dataset.id;
            fetch(`${apiUrl}/${id}`)
                .then(response => response.json())
                .then(category => {
                    Object.entries(category).forEach(([key, value]) => {
                        if (categoryForm.elements[key]) {
                            categoryForm.elements[key].value = value;
                        }
                    });
                });
        } else if (event.target.classList.contains('delete-btn')) {
            const id = event.target.dataset.id;
            fetch(`${apiUrl}/${id}`, { method: 'DELETE' })
                .then(() => loadCategories());
        }
    });

    loadCategories();
});
