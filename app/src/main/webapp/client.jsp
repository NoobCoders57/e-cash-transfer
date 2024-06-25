<%--
  Created by IntelliJ IDEA.
  User: mario
  Date: 25/06/2024
  Time: 08:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.example.servlets.ClientServlet" %>

<!DOCTYPE html>
<html>
<head>
    <title>Clients</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h2>Clients</h2>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#clientModal">
        Add New Client
    </button>
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>Numtel</th>
            <th>Nom</th>
            <th>Sexe</th>
            <th>Pays</th>
            <th>Solde</th>
            <th>Mail</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="client" items="${listClients}">
            <tr>
                <td>${client.numtel}</td>
                <td>${client.nom}</td>
                <td>${client.sexe}</td>
                <td>${client.pays}</td>
                <td>${client.solde}</td>
                <td>${client.mail}</td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn" data-id="${client.numtel}" data-toggle="modal"
                            data-target="#clientModal">Edit
                    </button>
                    <a href="client?action=delete&numtel=${client.numtel}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal for Add/Edit Client -->
<div class="modal fade" id="clientModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Client Form</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="clientForm" action="client" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <div class="form-group">
                        <label for="numtel">Numtel:</label>
                        <input type="text" class="form-control" id="numtel" name="numtel">
                    </div>
                    <div class="form-group">
                        <label for="nom">Nom:</label>
                        <input type="text" class="form-control" id="nom" name="nom">
                    </div>
                    <div class="form-group">
                        <label for="sexe">Sexe:</label>
                        <input type="text" class="form-control" id="sexe" name="sexe">
                    </div>
                    <div class="form-group">
                        <label for="pays">Pays:</label>
                        <input type="text" class="form-control" id="pays" name="pays">
                    </div>
                    <div class="form-group">
                        <label for="solde">Solde:</label>
                        <input type="number" class="form-control" id="solde" name="solde">
                    </div>
                    <div class="form-group">
                        <label for="mail">Mail:</label>
                        <input type="email" class="form-control" id="mail" name="mail">
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveClientBtn">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        // Open modal for editing
        $('.edit-btn').on('click', function () {
            var numtel = $(this).data('id');
            $.get('client?action=edit&numtel=' + numtel, function (data) {
                $('#formAction').val('update');
                $('#numtel').val(data.numtel).prop('readonly', true);
                $('#nom').val(data.nom);
                $('#sexe').val(data.sexe);
                $('#pays').val(data.pays);
                $('#solde').val(data.solde);
                $('#mail').val(data.mail);
            });
        });

        // Clear form on modal close
        $('#clientModal').on('hidden.bs.modal', function () {
            $('#clientForm')[0].reset();
            $('#formAction').val('insert');
            $('#numtel').prop('readonly', false);
        });

        // Save client form
        $('#saveClientBtn').on('click', function () {
            $('#clientForm').submit();
        });
    });
</script>
</body>
</html>
