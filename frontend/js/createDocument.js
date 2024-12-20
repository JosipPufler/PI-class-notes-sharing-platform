const documentForm = document.getElementById("createDocumentForm");
const nameInput = document.getElementById("name");
const descriptionInput = document.getElementById("description");
const materialTypeSelect = document.getElementById("materialType");
const userIdInput = document.getElementById("userId");
const materialsListDiv = document.getElementById("materialsList");

const components = [nameInput, descriptionInput, materialTypeSelect];

function showError(errorMessage, errorType) {
    resolveErrors();
    if (!document.getElementById("errorMessage")) {
        if (errorType === "name") {
            addError(errorMessage, nameInput);
        } else if (errorType === "description") {
            addError(errorMessage, descriptionInput);
        } else if (errorType === "materialType") {
            addError(errorMessage, materialTypeSelect);
        } else if (errorType === "general") {
            components.forEach(x => x.classList.add("error"));
            addError(errorMessage, descriptionInput);
        }
    } else {
        document.getElementById("errorMessage").innerHTML = errorMessage;
    }
}

function addError(errorMessage, component) {
    component.insertAdjacentHTML("afterend",
        `<p id="errorMessage" class="errorMessage">
            ${errorMessage}
        </p>`
    );
    component.classList.add("error");
}

function resolveErrors() {
    if (document.getElementById("errorMessage")) {
        components.forEach(component => component.classList.remove("error"));
        document.getElementById("errorMessage").remove();
    }
}

function displayMaterials(materials) {
    materialsListDiv.innerHTML = ''; 
    
    if (materials.length === 0) {
        materialsListDiv.innerHTML = '<p>No materials found.</p>';
        return;
    }

    const materialsList = document.createElement('div');
    materialsList.className = 'materials-grid';

    materials.forEach(material => {
        const materialCard = document.createElement('div');
        materialCard.className = 'material-card';
        materialCard.innerHTML = `
            <h3>${material.name}</h3>
            <p><strong>Type:</strong> ${material.materialType.name}</p>
            <p><strong>Description:</strong> ${material.description}</p>
            <p><strong>Created by:</strong> ${material.user.firstName} ${material.user.lastName}</p>
            <p><strong>Created on:</strong> ${new Date(material.creationDate).toLocaleDateString()}</p>
            <button onclick="window.location.href='editDocument.html?id=${material.id}'" class="edit-button">
                <i class="fas fa-edit"></i> Edit
            </button>
        `;
        materialsList.appendChild(materialCard);
    });

    materialsListDiv.appendChild(materialsList);
}


async function fetchMaterials() {
    try {
        const response = await fetch('http://localhost:8080/api/materials');
        const materials = await response.json();
        const materialsListDiv = document.getElementById('materialsList');
        materialsListDiv.innerHTML = materials.map(material => `
            <div class="material-item">
                <h3>${material.name}</h3>
                <p>${material.description}</p>
                <div class="material-actions">
                    <button onclick="window.location.href='editDocument.html?id=${material.id}'" class="edit-button">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button onclick="deleteMaterial(${material.id})" class="delete-btn">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error fetching materials:', error);
    }
}


async function deleteMaterial(id) {
    if (confirm('Are you sure you want to delete this material?')) {
        try {
            const response = await fetch(`http://localhost:8080/api/materials/${id}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                
                fetchMaterials();
            } else {
                throw new Error('Failed to delete material');
            }
        } catch (error) {
            console.error('Error deleting material:', error);
            alert('Failed to delete material');
        }
    }
}


document.getElementById('createDocumentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const name = document.getElementById('name').value;
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', document.getElementById('description').value);
    formData.append('materialTypeId', document.getElementById('materialType').value);
    formData.append('userId', document.getElementById('userId').value);
    
    
    const fileWithSpace = new File([' '], `${name}.txt`, { type: 'text/plain' });
    formData.append('file', fileWithSpace);

    try {
        const response = await fetch('http://localhost:8080/api/materials', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert('Document created successfully!');
            
            document.getElementById('createDocumentForm').reset();
            fetchMaterials();
        } else {
            throw new Error('Failed to create document');
        }
    } catch (error) {
        console.error('Error creating document:', error);
        alert('Failed to create document');
    }
});


async function initializeMaterialTypes() {
    try {
        const response = await fetch('http://localhost:8080/api/material-types');
        const materialTypes = await response.json();
        const select = document.getElementById('materialType');
        materialTypes.forEach(type => {
            const option = new Option(type.name, type.id);
            select.appendChild(option);
        });
        
        
        $('.js-example-basic-single').select2();
    } catch (error) {
        console.error('Error fetching material types:', error);
    }
}


document.addEventListener('DOMContentLoaded', () => {
    fetchMaterials();
    initializeMaterialTypes();
});


window.deleteMaterial = deleteMaterial;