document.addEventListener("DOMContentLoaded", () => {
    const groupNameElement = document.getElementById("groupName");
    const searchBar = document.getElementById("searchBar");
    const addMaterialButton = document.getElementById("addMaterialButton");
    const materialsList = document.getElementById("materialsList");
    const fileInput = document.getElementById("fileInput");

    const groupId = new URLSearchParams(window.location.search).get("id");

    if (groupId) {
        fetch(`http://localhost:8080/api/group/${groupId}`)
            .then(response => response.json())
            .then(group => {
                if (group && group.name) {
                    groupNameElement.textContent = group.name;
                    groupNameElement.addEventListener("click", () => {
                        window.location.href = `groupdetails.html?id=${groupId}`;
                    });
                } else {
                    groupNameElement.textContent = "Group not found";
                }
            })
            .catch(error => {
                console.error("Error fetching group data:", error);
                groupNameElement.textContent = "Error loading group";
            });
    } else {
        groupNameElement.textContent = "Invalid group ID";
    }

    addMaterialButton.addEventListener("click", () => {
        fileInput.click();
    });

    fileInput.addEventListener("change", (event) => {
        const file = event.target.files[0];
        if (file) {
            const listItem = document.createElement("li");
            listItem.textContent = file.name;
            materialsList.appendChild(listItem);
        }
    });

    const materials = materialsList.getElementsByTagName("li");

    searchBar.addEventListener("input", () => {
        const searchTerm = searchBar.value.toLowerCase();
        Array.from(materials).forEach(material => {
            const materialName = material.textContent.toLowerCase();
            if (materialName.includes(searchTerm)) {
                material.style.display = "";
            } else {
                material.style.display = "none";
            }
        });
    });
});