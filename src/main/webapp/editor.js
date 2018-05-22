
var $alert;
var $uploadModal;

$( document ).ready(function(){
    $alert = $('.alert');
    $uploadModal = $('#uploadModal');
    $('#uploadModal form').on('submit', uploadXlsx);
    $('button[id=export]').click(exportXlsx);
    $('button[id=new]').click(newXlsx);
    $('[data-toggle="tooltip"]').tooltip(); //initialize tooltips
})

var exportXlsx = function(){
    $.post('editor/export', function(result){
        showResult(result);
    });
}

//https://stackoverflow.com/questions/5392344/sending-multipart-formdata-with-jquery-ajax
var uploadXlsx = function(event){
    var fileSelect = document.querySelector('#uploadModal input');

    event.preventDefault();
    //TODO feedback to client
    // Get the selected file from the input.
     var formData = new FormData();
     formData.append("file", fileSelect.files[0], fileSelect.files[0].name);

    $.ajax({
        url: 'editor/upload',
        method: 'POST',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function(data){
            if(typeof data.error === 'undefined'){
             $uploadModal.modal('hide');
            }
            else{
                 // Handle errors here
                 console.log('ERRORS: ' + data.error);
             }
         },
         error: function(jqXHR, textStatus, errorThrown){
             // Handle errors here
             console.log('ERRORS: ' + textStatus);
         },
         complete: function(jqXHR, textStatus){
            showResult(jqXHR.responseJSON);
         }
    });
}

var newXlsx = function(){
    $.post('editor/new', function(response){
        console.log(response);
    });
}

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