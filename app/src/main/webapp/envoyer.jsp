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
            <th>Id Envoyer</th>
            <th>Num Envoyeur</th>
            <th>Num Recepteur</th>
            <th>Montant</th>
            <th>Date</th>
            <th>Raison</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="envoyer" items="${listEnvoyer}">
            <tr>
                <td><c:out value="${envoyer.idEnv()}"/></td>
                <td><c:out value="${envoyer.numEnvoyeur()}"/></td>
                <td><c:out value="${envoyer.numRecepteur()}"/></td>
                <td><c:out value="${envoyer.montant()}"/></td>
                <td><c:out value="${envoyer.date()}"/></td>
                <td><c:out value="${envoyer.raison()}"/></td>
                <td>
                    <button class="btn btn-warning btn-sm edit-btn"
                            data-idenv="${envoyer.idEnv()}"
                            data-numenvoyeur="${envoyer.numEnvoyeur()}"
                            data-numrecepteur="${envoyer.numRecepteur()}"
                            data-montant="${envoyer.montant()}"
                            data-date="${envoyer.date()}"
                            data-raison="${envoyer.raison()}"
                            data-toggle="modal"
                            data-target="#envoyerModal">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm delete-btn"
                            data-idenv="${envoyer.idEnv()}"
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
                    <input type="hidden" id="idenv" name="idenv">
                    <div class="form-group">
                        <label for="numEnvoyeur">Num Envoyeur:</label>
                        <select class="form-control" id="numEnvoyeur" name="numEnvoyeur" required>
                            <c:forEach var="client" items="${listClients}">
                                <option value="<c:out value='${client.numtel()}'/>"><c:out value='${client.nom()}'/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="numRecepteur">Num Recepteur:</label>
                        <select class="form-control" id="numRecepteur" name="numRecepteur" required>
                            <c:forEach var="client" items="${listClients}">
                                <option value="<c:out value='${client.numtel()}'/>"><c:out value='${client.nom()}'/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="montant">Montant:</label>
                        <input type="number" class="form-control" id="montant" name="montant" required>
                    </div>
                    <div class="form-group">
                        <label for="date">Date:</label>
                        <input type="datetime-local" class="form-control" id="date" name="date" required>
                    </div>
                    <div class="form-group">
                        <label for="raison">Raison:</label>
                        <input type="text" class="form-control" id="raison" name="raison" required>
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
                <p>Are you sure you want to delete this Envoyer?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="envoyer" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteIdenv" name="idenv">
                    <button type="submit" class="btn btn-danger">Delete</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                </form>
            </div>

        </div>
    </div>
</div>

<!-- Modal d'insuffisance de solde -->
<div class="modal fade" id="insufficientFundsModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Solde Insuffisant</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <p>Le solde de l'envoyeur est insuffisant pour effectuer cette transaction.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function() {
        // Filtrer les options du récepteur en fonction de l'envoyeur sélectionné
        $('#numEnvoyeur').on('change', function() {
            var selectedEnvoyeur = $(this).val();
            $('#numRecepteur option').each(function() {
                if ($(this).val() === selectedEnvoyeur) {
                    $(this).prop('disabled', true);
                } else {
                    $(this).prop('disabled', false);
                }
            });
        });

        // Ouvrir le formulaire modal pour l'édition
        $('.edit-btn').on('click', function() {
            const button = $(this);
            $('#formAction').val('update');
            $('#idenv').val(button.data('idenv'));
            $('#numEnvoyeur').val(button.data('numenvoyeur'));
            $('#numRecepteur').val(button.data('numrecepteur'));
            $('#montant').val(button.data('montant'));
            $('#date').val(button.data('date'));
            $('#raison').val(button.data('raison'));
        });

        function checkSoldeAndSubmit() {
            var montant = parseInt($('#montant').val());
            var soldeEnvoyeur = parseInt($('#soldeEnvoyeur').val());

            // Vérifier si le montant est supérieur au solde de l'envoyeur
            if (montant > soldeEnvoyeur) {
                // Afficher le modal d'insuffisance de solde
                $('#insufficientFundsModal').modal('show');
            } else {
                // Soumettre le formulaire si le solde est suffisant
                $('#envoyerForm').submit();
            }
        }

        // Réinitialiser le formulaire modal à sa fermeture
        $('#envoyerModal').on('hidden.bs.modal', function () {
            $('#envoyerForm')[0].reset();
            $('#formAction').val('insert');
            $('#idenv').val('');
        });

        // Soumettre le formulaire envoyer
        $('#saveEnvoyerBtn').on('click', function() {
            $('#envoyerForm').submit();
        });

        // Afficher la boîte de dialogue de confirmation de suppression
        $('.delete-btn').on('click', function() {
            const button = $(this);
            const idenv = button.data('idenv');
            $('#deleteIdenv').val(idenv);
        });
    });

</script>
</body>
</html>
