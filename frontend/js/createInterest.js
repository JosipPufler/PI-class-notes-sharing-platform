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
    )
    

    function create() {
        if($('#interest').select2('data').length == 1){
            const interestData = {
                name: interestName.value,
                parentId: $('#interest').select2('data')[0].value,
            }
        }else{
            return false
        }
        

        return fetch(
            'http://localhost:8080/api/interest',
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(interestData)
            }
        ).then(res => res.json())
            .then(json => {
                console.log(json)
                if (json.id != null) {
                    alert("Interest created")
                    window.location.href="../html/profile.html"
                }
                else if(json == "name"){
                    addError("Taj interest vec postoji", interestName)
                }else{
                    alert("Unexpected error")
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

function matchStart(params, data) {
    if ($.trim(params.term) === '') {
      return data;
    }
  
    if (typeof data.children === 'undefined') {
      return null;
    }
  
    var filteredChildren = [];
    $.each(data.children, function (idx, child) {
      if (child.text.toUpperCase().indexOf(params.term.toUpperCase()) == 0) {
        filteredChildren.push(child);
      }
    });
  
    if (filteredChildren.length) {
      var modifiedData = $.extend({}, data, true);
      modifiedData.children = filteredChildren;
  
      return modifiedData;
    }
  
    return null;
  }

$(document).ready(function() {
    $('.js-example-basic-multiple').select2({
        matcher: matchStart
    });
    for(var interest in interests){
        $('#interests').append(new Option(interest.name, interest.id, false, false)).trigger('change')
    }
});