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
                <td><c:out value="${frais.idFrais()}"/></td>
                <td><c:out value="${frais.montant1()}"/></td>
                <td><c:out value="${frais.montant2()}"/></td>
                <td><c:out value="${frais.frais()}"/></td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn"
                            data-idfrais="<c:out value='${frais.idFrais()}'/>"
                            data-montant1="<c:out value='${frais.montant1()}'/>"
                            data-montant2="<c:out value='${frais.montant2()}'/>"
                            data-frais="<c:out value='${frais.frais()}'/>"
                            data-toggle="modal"
                            data-target="#fraisModal">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm delete-btn"
                            data-idfrais="<c:out value='${frais.idFrais()}'/>"
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
                <p>Are you sure you want to delete this Frais?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="frais" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteIdfrais" name="idfrais">
                    <button type="submit" class="btn btn-danger">Delete</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
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
            $('#idfrais').val(button.data('idfrais'));
            $('#montant1').val(button.data('montant1'));
            $('#montant2').val(button.data('montant2'));
            $('#frais').val(button.data('frais'));
        });

        // Réinitialiser le formulaire modal à sa fermeture
        $('#fraisModal').on('hidden.bs.modal', function () {
            $('#fraisForm')[0].reset();
            $('#formAction').val('insert');
            $('#idfrais').val('');
        });

        // Soumettre le formulaire frais
        $('#saveFraisBtn').on('click', function() {
            $('#fraisForm').submit();
        });

        // Afficher la boîte de dialogue de confirmation de suppression
        $('.delete-btn').on('click', function() {
            const button = $(this);
            const idfrais = button.data('idfrais');
            $('#deleteIdfrais').val(idfrais);
        });
    });

</script>
</body>
</html>
