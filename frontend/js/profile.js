import { getUserId, getToken, getTokenType, getUsername, generateAuthorization } from "./tokenValidator.js"

const updateForm = document.getElementById("updateForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const email = document.getElementById("email")
const phoneNumber = document.getElementById("phoneNumber")
const lastName = document.getElementById("lastName")
const firstName = document.getElementById("firstName")
const btnDelete = document.getElementById("btnDelete")
const spanX = document.getElementById("spanX")
let interests
let responseStatus
let user
const components = [username, password, email, phoneNumber]    

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

btnDelete.onclick = function(){toggleConfirmPopup()}
spanX.onclick = function(){toggleConfirmPopup()}

document.onkeydown = function (evt) {
    let isEscape = false;
    if ("key" in evt) {
        isEscape = (evt.key === "Escape" || evt.key === "Esc");
    }
    if (isEscape && popUpActive) {
        toggleConfirmPopup()
    }
};

if (popUpForm) {
    popUpForm.addEventListener("submit", (e) => {
        deleteUser()        
        toggleConfirmPopup()
    })
}

function loadUser(user){
    username.value = user.username
    email.value = user.email
    phoneNumber.value = user.phoneNumber
    lastName.value = user.lastName
    firstName.value = user.firstName
}

function deleteUser(){
    return fetch(
        'http://localhost:8080/api/user/' + getUserId(),
        {
            method: "Delete",
            headers: {
                "Content-Type": "application/json",
                "Authorization": generateAuthorization()
            }   
        }
    ).then(res => {
        if(res.ok){
            alert("Obrisan")
        }else{
            alert("Nije obrisan :(")
        }
        return res.json()
    })
}

function showError(errorMessage, errorType) {
    resolveErrors()
    if (!document.getElementById("errorMessage")) {
        if(errorType == "email"){
            addError(errorMessage, email)
        } else if(errorType == "phone"){
            addError(errorMessage, phoneNumber)
        } else if(errorType == "password"){
            addError(errorMessage, password)
        } else if(errorType == "username"){
            addError(errorMessage, username)
        } else if (errorType == "general"){
            components.forEach(x => x.classList.add("error"))
            addError(errorMessage, phoneNumber)
        }
    }else{
        document.getElementById("errorMessage").innerHTML = errorMessage
    }
}

function addError(errorMessage, component){
    component.insertAdjacentHTML("afterend",
        `<p id="errorMessage" class="errorMessage">
            ${errorMessage}
        </p>`
    )
    component.classList.add("error")
}

function resolveErrors() {
    if (document.getElementById("errorMessage")) {
        username.classList.remove("error")
        password.classList.remove("error")
        email.classList.remove("error")
        phoneNumber.classList.remove("error")
        document.getElementById("errorMessage").remove();
    }

}

(() => {
    updateForm.addEventListener("submit", (e) => {
        e.preventDefault()
        updateUser()
    })


    function updateUser() {
        resolveErrors()

        console.log($('#interests').val())
        const profileData = {
            username: username.value,
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value,
            email: email.value,
            phoneNumber: phoneNumber.value,
            interests: $('#interests').val()
        }
        
        console.log(JSON.stringify(profileData))

        return fetch(
            'http://localhost:8080/api/user/' + getUserId(),
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": generateAuthorization()
                },
                body: JSON.stringify(profileData)
            }
        ).then(res => {
            if(res.ok){
                responseStatus = res.status
            }
            return res.json()
        })
            .then(json => {
                
                console.log(json)
                if(responseStatus == 200){
                    alert("Updated")
                }
                else if(json == "username"){
                    showError("To korisnicko ime je zauzeto", json)
                }
                else if(json == "email"){
                    showError("Taj mail je zauzet", json)
                }
                else if(json == "phone"){
                    showError("Taj broj je zauzet", json)
                }
                else if(json == "general"){
                    showError("Dogodila se greška probajte ponovo", json)
                }
                else if (json.errorMessages[0] === "User exists!") {
                    showError("Korisnik već postoji")
                }
                return true
            }
            ).catch(error => {
                if(error != undefined){
                    console.log(error)
                }
                return false
            })
    }

}
)()

$(document).ready(function() {
    fetch(
        'http://localhost:8080/api/interest',
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": generateAuthorization()
            }
        }
    ).then(res => res.json())
        .then(json => {
            console.log(json)
            interests = json
        }
        ).catch(error => {
            console.log(error)
        }).then(() =>{
            fetch(
                'http://localhost:8080/api/user/' + getUserId(),
                {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": generateAuthorization()
                    }
                }
            ).then(res => res.json())
                .then(json => {
                    console.log(json)
                    user = json
                    loadUser(user)
                }
                ).then(() => {            
                    let userInterests = user.interests
                    let currentInterest = false
                    for(var i in interests){
                        if(userInterests.filter(e => e.name === interests[i].name).length > 0){
                            currentInterest = true
                        } else {
                            currentInterest = false
                        }
                        $('#interests').append(new Option(interests[i].name, interests[i].id, currentInterest, currentInterest)).trigger('change')
                    }
                }).catch(error => {
                    console.log(error)
                })
        })
    
    
        $('#interests').select2({
            matcher: function(params, data) {
                if ($.trim(params.term) === '') { return data; }
        
                if (typeof data.text === 'undefined') { return null; }
            
                var q = params.term.toLowerCase();
                if (data.text.toLowerCase().indexOf(q) > -1 || data.id.toLowerCase().indexOf(q) > -1) {
                    return $.extend({}, data, true);
                }
        
                return null;
            }
        });
});