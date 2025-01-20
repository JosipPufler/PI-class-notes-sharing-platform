import { getUserId, getToken, getTokenType, getUsername, generateAuthorization } from "./tokenValidator.js"
const twoFA = document.getElementById("twoFA")
const submit = document.getElementById("submit")

submit.onclick = function(){updateSettings()}

function loadUser(user){
    console.log(user.settings)
    twoFA.checked = user.settings.twoFactorAuthenticationEnabled
}

function updateSettings(){
    const settingsData = {
        twoFactorAuthenticationEnabled: twoFA.checked
    }

    return fetch(
                'http://localhost:8080/api/user/updateSettings/' + getUserId(),
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": generateAuthorization()
                    },
                    body: JSON.stringify(settingsData)
                }
            ).then(res => {
                if(res.ok){
                    responseStatus = res.status
                    alert("Updated")
                    return
                }
                return res.json()
            })
}

$(document).ready(function() {
    $('.js-example-basic-single').select2();

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
            loadUser(json)
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
});

