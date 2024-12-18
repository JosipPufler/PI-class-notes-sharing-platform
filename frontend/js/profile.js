const registerForm = document.getElementById("signInForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const email = document.getElementById("email")
const phoneNumber = document.getElementById("phoneNumber")
const lastName = document.getElementById("lastName")
const firstName = document.getElementById("firstName")
let interests
let user
const components = [username, password, email, phoneNumber]    

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
    fetch(
        'http://localhost:8080/api/interest',
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }
    ).then(res => res.json())
        .then(json => {
            console.log(json)
            interests = json
        }
        ).catch(error => {
            console.log(error)
        })
    
    fetch(
        'http://localhost:8080/api/user',
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({id: localStorage.getItem("UserId")})
        }
    ).then(res => res.json())
        .then(json => {
            console.log(json)
            user = json
        }
        ).catch(error => {
            console.log(error)
        })
    

    function register() {
        const profileData = {
            id: localStorage.getItem("UserId"),
            username: username.value,
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value,
            email: email.value,
            phoneNumber: phoneNumber.value
        }

        return fetch(
            'http://localhost:8080/api/user',
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(profileData)
            }
        ).then(res => res.json())
            .then(json => {
                console.log(json)
                if (json.id != null) {
                    alert("User created")
                    localStorage.setItem("UserId", json.id)
                    window.location.href="../html/profile.html"
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
                console.log(error)
                return false
            })
    }

}
)()

$(document).ready(function() {
    let userInterests = user.interests
    let currentInterest = false
    $('.js-example-basic-multiple').select2();
    for(var interest in interests){
        if(userInterests.filter(e => e.name === interest.name).length > 0){
            currentInterest = true
        } else {
            currentInterest = false
        }
        $('#interests').append(new Option(interest.name, interest.id, currentInterest, false)).trigger('change')
    }
});