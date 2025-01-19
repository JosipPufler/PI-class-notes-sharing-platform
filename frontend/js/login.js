const loginForm = document.getElementById("logInForm")
const username = document.getElementById("username")
const password = document.getElementById("password")
const rememberMe = document.getElementById("rememberMe")

var popUpActive = false;
const twoFactorPopUp = document.getElementById("twoFactor")
const btnCode = document.getElementById("btnCode")
const code = document.getElementById("code")

twoFactorPopUp.style.display = "none";
document.body.style.overflow = "auto"

function togglePopup() {
    if (popUpActive) {
        twoFactorPopUp.style.display = "none";
        document.body.style.overflow = "auto"
    } else {
        twoFactorPopUp.style.display = "block";
        document.body.style.overflow = "hidden"
    }
    popUpActive = !popUpActive;
}

btnCode.onclick = function(){
    loginWith2FA()
}
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
        code.classList.add("error")
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
    ).then(res => {
        console.log(res.status)
        if(res.ok){
            return res.json()
        }
        if(res.status == '403'){
            togglePopup()
        }
    })
        .then(json => {
            resolveErrors()
            if(json.status == "401"){
                showError(json.message)
                return
            } else if(json.status == 403){
                console.log("403")
                togglePopup()
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
    ).catch(error => {
        if(error != undefined){
            console.log(error)
        }
        return false
    })
}

export function loginWith2FA() {
    const loginData = {
        username: username.value,
        password: password.value,
        code: code.value
    }

    function resolveErrors() {
        if (document.getElementById("errorMessage")) {
            username.classList.remove("error")
            password.classList.remove("error")
            code.classList.remove("error")
            document.getElementById("errorMessage").remove();    
        }
    }

    sessionStorage.clear()
    localStorage.clear()

    fetch(
        'http://localhost:8080/api/user/confirmLogIn',
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
