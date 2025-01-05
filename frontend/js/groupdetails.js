import { getUserId, getToken, getTokenType, getUsername, generateAuthorization } from "./tokenValidator.js"

document.addEventListener("DOMContentLoaded", () => {
    const groupNameElement = document.getElementById("groupName");
    const groupDescriptionElement = document.getElementById("groupDescription");
    const groupMembersList = document.getElementById("groupMembers");
    const addUserButton = document.getElementById("addUserButton");

    const groupId = new URLSearchParams(window.location.search).get("id");

    fetch(`http://localhost:8080/api/group/${groupId}`, 
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": generateAuthorization()
            }
            
        }
    )
        .then(response => response.json())
        .then(group => {
            groupNameElement.textContent = group.name;
            groupDescriptionElement.textContent = group.description;
            //show users
        });

    addUserButton.addEventListener("click", () => {
        //to do add user
    });
});