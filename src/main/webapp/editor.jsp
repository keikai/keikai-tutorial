<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel='stylesheet' href='webjars/bootstrap/4.1.0/dist/css/bootstrap.min.css'/>
    <script src='webjars/jquery/3.3.1/jquery.min.js'></script>
    <script src='webjars/bootstrap/4.1.0/dist/js/bootstrap.min.js'></script>
    <script id="keikai" async defer src="${keikaiJs}"></script>
    <script src='editor.js'></script>
    <title>Keikai Editor</title>
</head>
<body>
<div class="container-fluid d-flex flex-column flex-row" height="100%">
    <div class="navbar navbar-expand-md">
        <button id="upload" type="button" class="btn btn-primary" data-toggle="modal" data-target="#uploadModal">Upload</button>
        <button id="new" type="button" class="btn btn-primary">New</button>
        <button id="export" type="button" class="btn btn-primary">Export</button>
    </div>
    <div class="row p-1" >
      <div class="col-md-12 " >
        <div id="spreadsheet" class="" style="height:100%" >
        </div>
        </div>
    </div>
</div>
<div id="uploadModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Upload</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
        <form method="POST" action="editor/upload" enctype="multipart/form-data" >
          <div class="modal-body">
            <input type="file" name="file" id="file" accept=".xlsx">
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">Upload</button>
          </div>
        </form>
    </div>
  </div>
</div>
<div id="template" style="display:none">
    <div class="alert" role="alert">
      This is a primary alert—check it out!
    </div>
</div>
</body>
</html>
