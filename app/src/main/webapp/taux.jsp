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
    <title>Taux</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h2>Taux</h2>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#tauxModal">
        Add New Taux
    </button>
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>IdTaux</th>
            <th>Montant1</th>
            <th>Montant2</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="taux" items="${listTaux}">
            <tr>
                <td>${taux.idTaux}</td>
                <td>${taux.montant1}</td>
                <td>${taux.montant2}</td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn" data-id="${taux.idTaux}" data-toggle="modal" data-target="#tauxModal">Edit</button>
                    <a href="taux?action=delete&idTaux=${taux.idTaux}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal for Add/Edit Taux -->
<div class="modal fade" id="tauxModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Taux Form</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="tauxForm" action="taux" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <div class="form-group">
                        <label for="idTaux">IdTaux:</label>
                        <input type="text" class="form-control" id="idTaux" name="idTaux">
                    </div>
                    <div class="form-group">
                        <label for="montant1">Montant1:</label>
                        <input type="number" class="form-control" id="montant1" name="montant1">
                    </div>
                    <div class="form-group">
                        <label for="montant2">Montant2:</label>
                        <input type="number" class="form-control" id="montant2" name="montant2">
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveTauxBtn">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // Open modal for editing
        $('.edit-btn').on('click', function() {
            var idTaux = $(this).data('id');
            $.get('taux?action=edit&idTaux=' + idTaux, function(data) {
                $('#formAction').val('update');
                $('#idTaux').val(data.idTaux);
                $('#montant1').val(data.montant1);
                $('#montant2').val(data.montant2);
            });
        });

        // Clear form on modal close
        $('#tauxModal').on('hidden.bs.modal', function () {
            $('#tauxForm')[0].reset();
            $('#formAction').val('insert');
            $('#idTaux').val('');
        });

        // Save taux form
        $('#saveTauxBtn').on('click', function() {
            $('#tauxForm').submit();
        });
    });
</script>
</body>
</html>

