const interestForm = document.getElementById("interestForm")
const interestName = document.getElementById("name")
let interests

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
        interestName.classList.remove("error")
        document.getElementById("errorMessage").remove();
    }

}

(() => {
    interestForm.addEventListener("submit", (e) => {
        e.preventDefault()
        create()
    })
    

    function create() {
        console.log($('#interest').val())
        let parentId = null
        if($('#interest').select2('data').length == 1){
            if($('#interest').val() != "null")
                parentId = $('#interest').val()
        }
        console.log(parentId)
        const interestData = {
            name: interestName.value,
            parentInterestId: Number(parentId),
        }
        
        let status

        fetch(
            'http://localhost:8080/api/interest',
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(interestData)
            }
        ).then(res => {
            resolveErrors()
            if(res.ok)
                return res.json()
            throw new Error(res.status)
        }).then(json => {
                console.log(json)
                if (json.id != null) {
                    alert("Interest created")
                    window.location.href="../html/profile.html"
                }else{
                    alert("Unexpected error")
                }
                return true
            }
            ).catch(error => {
                if(Number(error.errorMessage) == 409)
                    console.log("Error")
                    addError("Taj interest vec postoji", interestName)
                return false
            })
    }

}
)()

$(document).ready(function() {
    $('.js-example-basic-single').select2({
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
        }
    ).then( () => {
        for(var i in interests){
            let interestData = interests[i]
            $('#interest').append(new Option(interestData.name, interestData.id, false, false)).trigger('change')
        }
    })
});