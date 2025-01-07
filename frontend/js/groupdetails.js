import { getUserId, getToken, getTokenType, getUsername, generateAuthorization } from "./tokenValidator.js"

document.addEventListener("DOMContentLoaded", () => {
    const groupNameElement = document.getElementById("groupName");
    const groupDescriptionElement = document.getElementById("groupDescription");
    const groupMembersList = document.getElementById("groupMembers");
    const addUserButton = document.getElementById("addUserButton");
    const addUserForm = document.getElementById("addUserForm");
    const searchBar = document.getElementById("userSearchBar");
    const usersList = document.getElementById("usersList");

    const groupId = new URLSearchParams(window.location.search).get("id");
    let group; // Define the group variable here

    fetch(`http://localhost:8080/api/group/${groupId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": generateAuthorization()
        }
    })
        .then(response => response.json())
        .then(data => {
            group = data;
            groupNameElement.textContent = group.name;
            groupDescriptionElement.textContent = group.description;
            groupMembersList.innerHTML = "";
            if (group.users) {
                group.users.forEach(member => {
                    const listItem = document.createElement("li");
                    listItem.textContent = member.username;
                    groupMembersList.appendChild(listItem);
                });
            }
        });

    addUserButton.addEventListener("click", () => {
        if (addUserForm.style.display === "none" || addUserForm.style.display === "") {
            addUserForm.style.display = "block";
        } else {
            addUserForm.style.display = "none";
        }    });

    searchBar.addEventListener("input", () => {
        const searchTerm = searchBar.value.toLowerCase();
        Array.from(usersList.getElementsByTagName("li")).forEach(user => {
            const userName = user.textContent.toLowerCase();
            if (userName.includes(searchTerm)) {
                user.style.display = "";
            } else {
                user.style.display = "none";
            }
        });
    });

    fetch("http://localhost:8080/api/user", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": generateAuthorization()
        }
    })
        .then(response => response.json())
        .then(users => {
            usersList.innerHTML = "";
            users.forEach(user => {
                const listItem = document.createElement("li");
                listItem.textContent = user.username;
                const addButton = document.createElement("button");
                addButton.textContent = "Add";
                addButton.addEventListener("click", () => {
                    addUserToGroup(user.id, groupId);
                });
                listItem.appendChild(addButton);
                usersList.appendChild(listItem);
            });
        });

    function addUserToGroup(userId, groupId) {
        fetch(`http://localhost:8080/api/group/${groupId}/addUser`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": generateAuthorization()
            },
            body: JSON.stringify({ userId: userId })
        })
            .then(response => {
                if (response.status === 409) {
                    alert("User is already in the group");
                    return;
                }
                if (!response.ok) {
                    throw new Error("Failed to add user to group");
                }
                return response.text();
            })
            .then(result => {
                console.log(result);
                return fetch(`http://localhost:8080/api/group/${groupId}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": generateAuthorization()
                    }
                });
            })
            .then(response => response.json())
            .then(updatedGroup => {
                group = updatedGroup;
                groupMembersList.innerHTML = "";
                if (group.users) {
                    group.users.forEach(member => {
                        const listItem = document.createElement("li");
                        listItem.textContent = member.username;
                        groupMembersList.appendChild(listItem);
                    });
                }
            })
            .catch(error => {
                console.error("Error adding user to group:", error);
            });
    }
});