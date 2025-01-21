const registerForm = document.getElementById("signInForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const email = document.getElementById("email")
const phoneNumber = document.getElementById("phoneNumber")
const lastName = document.getElementById("lastName")
const firstName = document.getElementById("firstName")
const confirmPassword = document.getElementById("confirmPassword")

const components = [username, password, email, phoneNumber, confirmPassword]

function showError(errorMessage, errorType) {
    resolveErrors()
    if (!document.getElementById("errorMessage")) {
        if(errorType == "email"){
            addError(errorMessage, email)
        } else if(errorType == "phone"){
            addError(errorMessage, phoneNumber)
        } else if(errorType == "password"){
            addError(errorMessage, confirmPassword)
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
        confirmPassword.classList.remove("error")
        email.classList.remove("error")
        phoneNumber.classList.remove("error")
        document.getElementById("errorMessage").remove();
    }

}

(() => {
    registerForm.addEventListener("submit", (e) => {
        e.preventDefault()
        if (confirmPassword.value === password.value) {
            resolveErrors()
            register()
        } else {
            showError("Lozinke moraju biti iste", "password")
        }
    })


    function register() {
        const registerData = {
            username: username.value,
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value,
            email: email.value,
            phoneNumber: phoneNumber.value
        }

        return fetch(
            'http://localhost:8080/api/user/signIn',
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(registerData)
            }
        ).then(res => res.json())
            .then(json => {
                console.log(json)
                if (json.id != null) {
                    alert("User created")
                    localStorage.setItem("UserId", json.id)
                    window.location.href="../html/login.html"
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