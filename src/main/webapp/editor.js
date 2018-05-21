
var $alert;
var $uploadModal;

$( document ).ready(function(){
    $alert = $('#template .alert');
    $uploadModal = $('#uploadModal');
//    $('button[id=new]').click();
    $('#uploadModal form').on('submit', uploadXlsx);
    $('button[id=export]').click(exportXlsx);

})

var exportXlsx = function(){
    $.post('editor/export', function(response){
        console.log(response);
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
         }
    });
}
