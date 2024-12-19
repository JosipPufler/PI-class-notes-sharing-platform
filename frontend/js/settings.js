$(document).ready(function() {
    $('#settings_select').select2({
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
    $('#settings_select').append(new Option("Opcija1", "1", true, true)).trigger('change')
    $('#settings_select').append(new Option("Opcija2", "2", false, false)).trigger('change')
})