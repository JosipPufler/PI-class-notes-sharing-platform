import { getUserId, getToken, getTokenType, getUsername, generateAuthorization } from "./tokenValidator.js"

const editForm = document.getElementById("editDocumentForm");
const nameInput = document.getElementById("name");
const contentInput = document.getElementById("content");

let currentMaterial = null;

function showError(errorMessage) {
    const existingError = document.getElementById("errorMessage");
    if (existingError) {
        existingError.remove();
    }

    contentInput.insertAdjacentHTML("afterend",
        `<p id="errorMessage" class="errorMessage">
            ${errorMessage}
        </p>`
    );
    contentInput.classList.add("error");
}

function resolveErrors() {
    const errorMessage = document.getElementById("errorMessage");
    if (errorMessage) {
        contentInput.classList.remove("error");
        errorMessage.remove();
    }
}


const urlParams = new URLSearchParams(window.location.search);
const materialId = urlParams.get('id');

if (!materialId) {
    window.location.href = 'createDocument.html';
}


fetch(`http://localhost:8080/api/materials/${materialId}`, {
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        "Authorization": generateAuthorization()
    }
})
.then(res => res.json())
.then(material => {
    currentMaterial = material;
    nameInput.value = material.name;
    
    
    return fetch(`http://localhost:8080/api/materials/download/${material.name}.txt`,
        {
            method: "GET",
            headers: {
                "Authorization": generateAuthorization()
            }
        }
    );
})
.then(res => {
    if (!res.ok) {
        throw new Error(`Failed to fetch file: ${res.status}`);
    }
    return res.text();
})
.then(content => {
    contentInput.value = content;
})
.catch(error => {
    console.error("Error:", error);
    showError("Failed to load document content");
});


editForm.addEventListener("submit", (e) => {
    e.preventDefault();
    saveDocument();
});

function saveDocument() {
    resolveErrors();

    
    const updatedFile = new File([contentInput.value], `${nameInput.value}.txt`, {
        type: "text/plain",
    });

    
    const formData = new FormData();
    formData.append('file', updatedFile);
    formData.append('userId', currentMaterial.user.id);
    formData.append('materialTypeId', currentMaterial.materialType.id);
    formData.append('name', nameInput.value);
    formData.append('description', currentMaterial.description);

    
    fetch(`http://localhost:8080/api/materials/${materialId}`, {
        method: "PUT",
        headers: {
                "Authorization": generateAuthorization()
            },
        body: formData,
    })
    .then(res => {
        if (!res.ok) {
            throw new Error(res.status);
        }
        return res.json();
    })
    .then(json => {
        console.log("Document updated:", json);
        if (json.id != null) {
            alert("Document saved successfully");
            window.location.href = 'createDocument.html';
        } else {
            showError("Unexpected error occurred");
        }
    })
    .catch(error => {
        console.error("Error saving document:", error);
        if (error.message === "409") {
            showError("A document with this name already exists");
        } else {
            showError("Failed to save document. Please try again.");
        }
    });
} 