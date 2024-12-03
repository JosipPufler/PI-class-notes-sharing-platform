const registerForm = document.getElementById("signInForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const email = document.getElementById("email")
const phoneNumber = document.getElementById("phoneNumber")
const lastName = document.getElementById("lastName")
const firstName = document.getElementById("firstName")
const confirmPassword = document.getElementById("confirmPassword")

function showError(errorMessage) {
    if (!document.getElementById("errorMessage")) {
        confirmPassword.insertAdjacentHTML("afterend",
            `<p id="errorMessage" class="errorMessage">
                ${errorMessage}
            </p>`
        )
        username.classList.add("error")
        password.classList.add("error")
        confirmPassword.classList.add("error")
    }else{
        document.getElementById("errorMessage").innerHTML = errorMessage
    }
}

function resolveErrors() {
    if (document.getElementById("errorMessage")) {
        username.classList.remove("error")
        password.classList.remove("error")
        confirmPassword.classList.remove("error")
        document.getElementById("my-element").remove();
    }

}

(() => {
    registerForm.addEventListener("submit", (e) => {
        e.preventDefault()
        if (confirmPassword.value === password.value) {
            resolveErrors()
            register()
        } else {
            showError("Lozinke moraju biti iste")
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
            'http://localhost:8080/api/user',
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
                if (json.isSuccess) {
                    resolveErrors()
                    console.log("YESSS")
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