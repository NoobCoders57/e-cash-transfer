<%--
  Created by IntelliJ IDEA.
  User: mario
  Date: 25/06/2024
  Time: 08:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
            <th>Téléphone</th>
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
                <td><c:out value="${client.numtel()}"/></td>
                <td><c:out value="${client.nom()}"/></td>
                <td><c:out value="${client.sexe()}"/></td>
                <td><c:out value="${client.pays()}"/></td>
                <td><c:out value="${client.solde()}"/></td>
                <td><c:out value="${client.mail()}"/></td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn"
                            data-numtel="<c:out value='${client.numtel()}'/>"
                            data-nom="<c:out value='${client.nom()}'/>"
                            data-sexe="<c:out value='${client.sexe()}'/>"
                            data-pays="<c:out value='${client.pays()}'/>"
                            data-solde="<c:out value='${client.solde()}'/>"
                            data-mail="<c:out value='${client.mail()}'/>"
                            data-toggle="modal"
                            data-target="#clientModal">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm delete-btn"
                            data-numtel="<c:out value='${client.numtel()}'/>"
                            data-nom="<c:out value='${client.nom()}'/>"
                            data-toggle="modal"
                            data-target="#deleteConfirmationModal">
                        Delete
                    </button>
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
                    <input type="hidden" id="originalNumtel" name="originalNumtel">
                    <div class="form-group">
                        <label for="numtel">Téléphone:</label>
                        <input type="text" class="form-control" id="numtel" name="numtel" required>
                    </div>
                    <div class="form-group">
                        <label for="nom">Nom:</label>
                        <input type="text" class="form-control" id="nom" name="nom" required>
                    </div>
                    <div class="form-group">
                        <label for="sexe">Sexe:</label>
                        <input type="text" class="form-control" id="sexe" name="sexe" required>
                    </div>
                    <div class="form-group">
                        <label for="pays">Pays:</label>
                        <input type="text" class="form-control" id="pays" name="pays" required>
                    </div>
                    <div class="form-group">
                        <label for="solde">Solde:</label>
                        <input type="number" class="form-control" id="solde" name="solde" required>
                    </div>
                    <div class="form-group">
                        <label for="mail">Mail:</label>
                        <input type="email" class="form-control" id="mail" name="mail" required>
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

<!-- Modal for Delete Confirmation -->
<div class="modal fade" id="deleteConfirmationModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h5 class="modal-title">Confirmation de suppression</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <p>Êtes-vous sûr de vouloir supprimer le client : <span id="clientNameToDelete"></span>?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="client" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteNumtel" name="numtel">
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                </form>
            </div>

        </div>
    </div>
</div>

<!-- JavaScript pour la confirmation de suppression -->
<script>
    $(document).ready(function() {
        // Ouvrir le formulaire modal pour l'édition
        $('.edit-btn').on('click', function() {
            const button = $(this);
            $('#formAction').val('update');
            $('#originalNumtel').val(button.data('numtel'));
            $('#numtel').val(button.data('numtel'));
            $('#nom').val(button.data('nom'));
            $('#sexe').val(button.data('sexe'));
            $('#pays').val(button.data('pays'));
            $('#solde').val(button.data('solde'));
            $('#mail').val(button.data('mail'));
        });

        // Réinitialiser le formulaire modal à sa fermeture
        $('#clientModal').on('hidden.bs.modal', function () {
            $('#clientForm')[0].reset();
            $('#formAction').val('insert');
            $('#numtel').prop('readonly', false);
        });

        // Soumettre le formulaire client
        $('#saveClientBtn').on('click', function() {
            $('#clientForm').submit();
        });

        // Afficher la boîte de dialogue de confirmation de suppression
        $('.delete-btn').on('click', function() {
            const button = $(this);
            const numtel = button.data('numtel');
            const nom = button.data('nom');
            $('#clientNameToDelete').text(nom);
            $('#deleteNumtel').val(numtel);
        });
    });

</script>
</body>
</html>
