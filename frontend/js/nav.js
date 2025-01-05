import { getUsername, logOut } from "./tokenValidator.js";

window.onload = function(){
    $("#includeNavbar").load("../html/nav.html", () => {
        const dropdown = document.getElementById("userDropdown")
        const profileIcon = document.getElementById("profileIcon")
        const username = document.getElementById("dropdownUsername")
        const dropdownLogOut = document.getElementById("dropdownLogOut");

        username.innerHTML = getUsername()

        dropdownLogOut.onclick = function(){
            logOut()
        }

        profileIcon.onclick = function(){
            toggleUserDropdown()
        }
        
        dropdown?.addEventListener('click', function (event) {
            event.stopPropagation();
        });

        window.onclick = function (event) {
            if (!event.target.matches('#userSymbol')) {
                dropdown.style.display = "none"
            }
            if (event.target.matches('.popUpBackground')) {
                toggleContactPopup()
            }
        }

        function toggleUserDropdown() {
            if (dropdown.style.display === "block") {
                dropdown.style.display = "none"
            } else {
                dropdown.style.display = "block"
            }
        }

    })
}

$();
