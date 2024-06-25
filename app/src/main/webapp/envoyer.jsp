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
    <title>Envoyer</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h2>Envoyer</h2>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#envoyerModal">
        Add New Envoyer
    </button>
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>Id Envoyeur</th>
            <th>NumEnvoyeur</th>
            <th>NumRecepteur</th>
            <th>Montant</th>
            <th>Date</th>
            <th>Raison</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="envoyer" items="${listEnvoyer}">
            <tr>
                <td>${envoyer.idEnv}</td>
                <td>${envoyer.numEnvoyeur}</td>
                <td>${envoyer.numRecepteur}</td>
                <td>${envoyer.montant}</td>
                <td>${envoyer.date}</td>
                <td>${envoyer.raison}</td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn" data-id="${envoyer.idEnv}" data-toggle="modal" data-target="#envoyerModal">Edit</button>
                    <a href="envoyer?action=delete&idEnv=${envoyer.idEnv}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal for Add/Edit Envoyer -->
<div class="modal fade" id="envoyerModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Envoyer Form</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="envoyerForm" action="envoyer" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <div class="form-group">
                        <label for="numEnvoyeur">NumEnvoyeur:</label>
                        <input type="text" class="form-control" id="numEnvoyeur" name="numEnvoyeur">
                    </div>
                    <div class="form-group">
                        <label for="numRecepteur">NumRecepteur:</label>
                        <input type="text" class="form-control" id="numRecepteur" name="numRecepteur">
                    </div>
                    <div class="form-group">
                        <label for="montant">Montant:</label>
                        <input type="number" class="form-control" id="montant" name="montant">
                    </div>
                    <div class="form-group">
                        <label for="date">Date:</label>
                        <input type="datetime-local" class="form-control" id="date" name="date">
                    </div>
                    <div class="form-group">
                        <label for="raison">Raison:</label>
                        <input type="text" class="form-control" id="raison" name="raison">
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveEnvoyerBtn">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // Open modal for editing
        $('.edit-btn').on('click', function() {
            var idEnv = $(this).data('id');
            $.get('envoyer?action=edit&idEnv=' + idEnv, function(data) {
                $('#formAction').val('update');
                $('#numEnvoyeur').val(data.numEnvoyeur);
                $('#numRecepteur').val(data.numRecepteur);
                $('#montant').val(data.montant);
                $('#date').val(data.date);
                $('#raison').val(data.raison);
            });
        });

        // Clear form on modal close
        $('#envoyerModal').on('hidden.bs.modal', function () {
            $('#envoyerForm')[0].reset();
            $('#formAction').val('insert');
        });

        // Save envoyer form
        $('#saveEnvoyerBtn').on('click', function() {
            $('#envoyerForm').submit();
        });
    });
</script>
</body>
</html>


