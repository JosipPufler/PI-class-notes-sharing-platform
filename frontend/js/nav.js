import { getUsername, getUserId, logOut, generateAuthorization } from "./tokenValidator.js";

window.onload = function(){
    $("#includeNavbar").load("../html/nav.html", () => {
        const dropdown = document.getElementById("userDropdown")
        const profileIcon = document.getElementById("profileIcon")
        const username = document.getElementById("dropdownUsername")
        const dropdownLogOut = document.getElementById("dropdownLogOut")
        const notificationIcon = document.getElementById("notificationSymbol")
        const notificationList = document.getElementById("notificationList")
        const groupsDropdown = document.querySelector("li.dropdown:nth-of-type(3) .dropdown-content")
        var numberOfNotifications = 0

        const parser = new DOMParser();
        var popUpActive = false;
        const confirmPopUp = document.getElementById("confirmPopUp");
        const popUpForm = document.getElementById("popUpForm")

        confirmPopUp.style.display = "none";
        document.body.style.overflow = "auto"

        function toggleConfirmPopup() {
            if (popUpActive) {
                confirmPopUp.style.display = "none";
                document.body.style.overflow = "auto"
            } else {
                confirmPopUp.style.display = "block";
                document.body.style.overflow = "hidden"
            }
            popUpActive = !popUpActive;
        }

        document.onkeydown = function (evt) {
            let isEscape = false;
            if ("key" in evt) {
                isEscape = (evt.key === "Escape" || evt.key === "Esc");
            }
            if (isEscape && popUpActive) {
                toggleConfirmPopup()
            }
        };

        username.innerHTML = getUsername()

        dropdownLogOut.onclick = function(){
            logOut()
        }

        profileIcon.onclick = function(){
            toggleUserDropdown()
        }

        notificationIcon.onclick = function(){
            notificationIcon.classList.remove("badge")
            toggleConfirmPopup()
        }

        dropdown?.addEventListener('click', function (event) {
            event.stopPropagation();
        });

        window.onclick = function (event) {
            if (!event.target.matches('#userSymbol')) {
                dropdown.style.display = "none"
            }
            if (event.target.matches('.popUpBackground')) {
                toggleConfirmPopup()
            }
        }

        function toggleUserDropdown() {
            if (dropdown.style.display === "block") {
                dropdown.style.display = "none"
            } else {
                dropdown.style.display = "block"
            }
        }

        function fetchNotifications(){
            fetch(
                'http://localhost:8080/api/notification/' + getUserId(),
                {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": generateAuthorization()
                    }
                }
            ).then(res => res.json())
                .then(json => {
                    if(numberOfNotifications != json.length){
                        notificationIcon.classList.add("badge")
                        numberOfNotifications = json.length
                        for(let notification of json){
                            insertNotification(notification)
                        }
                    }
                    console.log(json)
                }).catch(error => {
                console.log(error)
            })
        }

        function insertNotification(notification){
            const doc = parser.parseFromString(`<div style="padding: 10px;" id=` + notification.id +`>
                  <div style="display: flex; flex-direction: row; justify-content: space-between;">
                    <h3>` + notification.title + `</h3>
                    <h4>` + parseISOString(notification.time) + `</h4>
                  </div>
                  <div>
                    <p style="text-align: justify;">
                        `+ notification.content +`
                    </p>
                  </div>
                </div>`, 'text/html');
            if(!notification.isRead){
                doc.body.firstChild.style.fontWeight = "800"
            }else{
                doc.body.firstChild.style.fontWeight = "400"
            }
            doc.body.firstChild.addEventListener("click", markAsRead(doc.body.firstChild.id))
            notificationList.insertBefore(parser.parseFromString('<hr>', 'text/html').body.firstChild, notificationList.firstChild)
            notificationList.insertBefore(doc.body.firstChild, notificationList.firstChild)
        }

        function parseISOString(s) {
            let b = s.split(/\D+/);
            let date = new Date(Date.UTC(b[0], --b[1], b[2], b[3], b[4], b[5], b[6]))
            return date.toLocaleDateString('hr-HR') + ' ' + date.getUTCHours() + ':' + date.getUTCMinutes();
        }

        async function startNotificationListener(){
            fetchNotifications()
            while(true){
                await new Promise(resolve => setTimeout(resolve, 5000))
                fetchNotifications()
            }
        }

        startNotificationListener()

        fetch(`http://localhost:8080/api/group/user/${getUserId()}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": generateAuthorization()
            }
        })
            .then(response => response.json())
            .then(groups => {
                groups.forEach(group => {
                    const groupLink = document.createElement("a");
                    groupLink.href = `groupview.html?id=${group.id}`;
                    groupLink.textContent = group.name;
                    groupsDropdown.appendChild(groupLink);
                });
            })
            .catch(error => {
                console.error("Error fetching groups:", error);
            });
    })
}

$();