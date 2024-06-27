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
                <td>${taux.idTaux()}</td>
                <td>${taux.montant1()}</td>
                <td>${taux.montant2()}</td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn"
                            data-id="${taux.idTaux()}"
                            data-montant1="${taux.montant1()}"
                            data-montant2="${taux.montant2()}"
                            data-toggle="modal"
                            data-target="#tauxModal">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm delete-btn"
                            data-id="${taux.idTaux()}"
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
                    <input type="hidden" id="idTaux" name="idTaux">
                    <div class="form-group">
                        <label for="montant1">Montant1:</label>
                        <input type="number" class="form-control" id="montant1" name="montant1" required>
                    </div>
                    <div class="form-group">
                        <label for="montant2">Montant2:</label>
                        <input type="number" class="form-control" id="montant2" name="montant2" required>
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
                <p>Êtes-vous sûr de vouloir supprimer ce taux?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="taux" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteIdTaux" name="idTaux">
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                </form>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // Ouvrir le formulaire modal pour l'édition
        $('.edit-btn').on('click', function() {
            const button = $(this);
            $('#formAction').val('update');
            $('#idTaux').val(button.data('id'));
            $('#montant1').val(button.data('montant1'));
            $('#montant2').val(button.data('montant2'));
        });

        // Réinitialiser le formulaire modal à sa fermeture
        $('#tauxModal').on('hidden.bs.modal', function () {
            $('#tauxForm')[0].reset();
            $('#formAction').val('insert');
            $('#idTaux').val('');
        });

        // Soumettre le formulaire de taux
        $('#saveTauxBtn').on('click', function() {
            $('#tauxForm').submit();
        });

        // Afficher la boîte de dialogue de confirmation de suppression
        $('.delete-btn').on('click', function() {
            const button = $(this);
            $('#deleteIdTaux').val(button.data('id'));
        });
    });
</script>
</body>
</html>
