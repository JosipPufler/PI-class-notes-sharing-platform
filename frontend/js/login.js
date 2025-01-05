const loginForm = document.getElementById("logInForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const rememberMe = document.getElementById("rememberMe")

if (loginForm) {
    loginForm.addEventListener("submit", (e) => {
        e.preventDefault()
        login()
    })
}

function showError(errorMessage) {
    if (!document.getElementById("errorMessage")) {
        password.insertAdjacentHTML("afterend",
            `<p id="errorMessage" class="errorMessage">
                ${errorMessage}
            </p>`
        )
        username.classList.add("error")
        password.classList.add("error")
    }else{
        document.getElementById("errorMessage").innerHTML = errorMessage
    }

}


export function login() {
    const loginData = {
        username: username.value,
        password: password.value
    }


    function resolveErrors() {
        if (document.getElementById("errorMessage")) {
            username.classList.remove("error")
            password.classList.remove("error")
            document.getElementById("errorMessage").remove();    
        }
    }

    sessionStorage.clear()
    localStorage.clear()

    fetch(
        'http://localhost:8080/api/user/logIn',
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginData)
        }
    ).then(res => res.json())
        .then(json => {
            resolveErrors()
            if(json.status == "401"){
                showError(json.message)
                return
            }
            if (!rememberMe.checked) {
                localStorage.setItem("token", json.token)
                localStorage.setItem("username", json.username)
                localStorage.setItem("tokenType", json.tokenType)
                localStorage.setItem("id", json.userId)
            } else {
                sessionStorage.setItem("token", json.token)
                sessionStorage.setItem("username", json.username)
                sessionStorage.setItem("tokenType", json.tokenType)
                sessionStorage.setItem("id", json.userId)
            }
            window.location="../html/home.html"
        }
    )
}
