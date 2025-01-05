
function isTokenExpired(token) {
    const arrayToken = token.split('.')
    const tokenPayload = JSON.parse(atob(arrayToken[1]))
    return new Date().getTime() >= tokenPayload?.exp * 1000
}

document.onreadystatechange =() =>{
    let token;
    if (!sessionStorage.getItem("token") && !localStorage.getItem("token")) {
        window.location = "../html/login.html"
        return;
    } else if (sessionStorage.getItem("token")){
        token = sessionStorage.getItem("token")
    } else {
        token = localStorage.getItem("token")
    }

    try {
        if (isTokenExpired(token)) {
            localStorage.clear()
            sessionStorage.clear()
            window.location = "../html/login.html"
        }else {
            let hidden = document.getElementsByClassName("needAuthentication")
            for (let element of hidden){
                element.classList.remove("needAuthentication")
                element.classList.add("authenticated")
            }
        }
    } catch (err) {
        console.log(err)
    }
}

let token, tokenType, username, id

function refreshData(){
    if(localStorage.getItem("tokenType") && localStorage.getItem("token"), localStorage.getItem("id"), localStorage.getItem("username")){
        token = localStorage.getItem("token")
        tokenType = localStorage.getItem("tokenType")
        username = localStorage.getItem("username")
        id = localStorage.getItem("id")
    } else if (sessionStorage.getItem("tokenType") && sessionStorage.getItem("token") && sessionStorage.getItem("username") && sessionStorage.getItem("id")){
        token = sessionStorage.getItem("token")
        tokenType = sessionStorage.getItem("tokenType")
        username = sessionStorage.getItem("username")
        id = sessionStorage.getItem("id")
    } else {
        window.location = "../html/login.html"
    }
}

export function logOut(){
    localStorage.clear()
    sessionStorage.clear()
    window.location = "../html/login.html"
}

export function getUserId(){
    refreshData()
    return id;
}

export function getUsername(){
    refreshData()
    return username;
}

export function getTokenType(){
    refreshData()
    return tokenType;
}

export function getToken(){
    refreshData()
    return token;
}

export function generateAuthorization(){
    return getTokenType() + " " + getToken()
}