
var $alert;

$( document ).ready(function(){
    $alert = $('.alert');
})


/** show action result with an alert
* result: a json object converted from Result.java
*/
function showResult(result){
    $alert.html(result.message);
    $alert.removeClass();
    switch (result.state){
        case 'SUCCESS':
            $alert.addClass('alert alert-success')
            setTimeout(function(){ $alert.hide()}, 2000);
            break;
        case 'ERROR':
            $alert.addClass('alert alert-danger')
            break;
    }
    $alert.show();
}