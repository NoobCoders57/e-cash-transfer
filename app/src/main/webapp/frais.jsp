<%--
  Created by IntelliJ IDEA.
  User: mario
  Date: 25/06/2024
  Time: 08:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Frais</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h2>Frais</h2>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#fraisModal">
        Add New Frais
    </button>
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>Idfrais</th>
            <th>Montant1</th>
            <th>Montant2</th>
            <th>Frais</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="frais" items="${listFrais}">
            <tr>
                <td>${frais.idfrais}</td>
                <td>${frais.montant1}</td>
                <td>${frais.montant2}</td>
                <td>${frais.frais}</td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn" data-id="${frais.idfrais}" data-toggle="modal" data-target="#fraisModal">Edit</button>
                    <a href="frais?action=delete&idfrais=${frais.idfrais}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal for Add/Edit Frais -->
<div class="modal fade" id="fraisModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Frais Form</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="fraisForm" action="frais" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="idfrais" name="idfrais">
                    <div class="form-group">
                        <label for="montant1">Montant1:</label>
                        <input type="number" class="form-control" id="montant1" name="montant1">
                    </div>
                    <div class="form-group">
                        <label for="montant2">Montant2:</label>
                        <input type="number" class="form-control" id="montant2" name="montant2">
                    </div>
                    <div class="form-group">
                        <label for="frais">Frais:</label>
                        <input type="number" class="form-control" id="frais" name="frais">
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveFraisBtn">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // Open modal for editing
        $('.edit-btn').on('click', function() {
            var idfrais = $(this).data('id');
            $.get('frais?action=edit&idfrais=' + idfrais, function(data) {
                $('#formAction').val('update');
                $('#idfrais').val(data.idfrais);
                $('#montant1').val(data.montant1);
                $('#montant2').val(data.montant2);
                $('#frais').val(data.frais);
            });
        });

        // Clear form on modal close
        $('#fraisModal').on('hidden.bs.modal', function () {
            $('#fraisForm')[0].reset();
            $('#formAction').val('insert');
            $('#idfrais').val('');
        });

        // Save frais form
        $('#saveFraisBtn').on('click', function() {
            $('#fraisForm').submit();
        });
    });
</script>
</body>
</html>

