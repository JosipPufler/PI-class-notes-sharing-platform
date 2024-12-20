const createGroupForm = document.getElementById("createGroupForm");
const nameGroup = document.getElementById("name");
const description = document.getElementById("description");

const components = [nameGroup, description];

function showError(errorMessage, errorType) {
    if (!document.getElementById("errorMessage")) {
        if (errorType == "name") {
            addError(errorMessage, nameGroup);
        }
    } else {
        document.getElementById("errorMessage").innerHTML = errorMessage;
    }
}

function addError(errorMessage, component) {
    component.insertAdjacentHTML("afterend", `<p id="errorMessage" class="errorMessage">${errorMessage}</p>`);
}

function resolveErrors() {
    if (document.getElementById("errorMessage")) {
        nameGroup.classList.remove("error");
        document.getElementById("errorMessage").remove();
    }
}

(() => {
    createGroupForm.addEventListener("submit", (e) => {
        e.preventDefault();
        createGroup();
    });

    function createGroup() {
        const groupData = {
            name: nameGroup.value,
            description: description.value,
        };

        return fetch("http://localhost:8080/api/group", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(groupData),
        })
            .then((res) => res.json())
            .then((json) => {
                console.log(json);
                resolveErrors();
                if (json.id != null) {
                    alert("Group created successfully");
                    window.location.href = `groupview.html?id=${json.id}`;
                } else if (json == "name") {
                    showError("To ime grupe je zauzeto", json);
                }
                return true;
            }).catch((error) => {
                console.log(error)
                return false;
            });
    }
})()
