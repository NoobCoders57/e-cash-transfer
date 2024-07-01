<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Clients</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.datatables.net/1.11.3/css/dataTables.bootstrap4.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/dataTables.bootstrap4.min.js"></script>


    <style>
        #sidebar-wrapper {
            width: 220px;
            justify-content: center;
            height: 100vh;
            background-color: #40A578;
        }
        .list-group-item-action:hover {
            background-color: rgb(167, 237, 206, 0.3);
            border-radius: 10px;
            color: ghostwhite;
        }
        .list-group-item i {
            margin-right: 5px;
        }
        .list-group-item {
            background-color: #40A578;
            color: whitesmoke;
            font-size: 16px;
            border: none;
            font-weight: bold;
            text-align: left;
        }
        .sidebar-content {
            padding: 0 1rem;
        }
        hr {
            border: 2px solid rgb(167, 237, 206, 0.3);
            border-radius: 25px;
        }
        #clientsTable th {
            width: 170px;
            text-align: center;
        }
        #clientsTable td {
            text-align: left;
        }
    </style>
</head>
<body>
<div class="d-flex" id="wrapper">
    <!-- Sidebar -->
    <div id="sidebar-wrapper">
        <div class="list-group list-group-flush sidebar-content">
            <div class="nav1 mt-5">
                <a href="client" class="list-group-item list-group-item-action"><i class="bi bi-person-circle"></i>&nbsp; Clients</a>
                <hr class="mt-2 mb-2 w-75">
            </div>
            <div>
                <a href="frais" class="list-group-item list-group-item-action"><i class="bi bi-coin"></i> &nbsp;Frais</a>
                <hr class="mt-2 mb-2 w-75">
            </div>
            <div>
                <a href="envoyer" class="list-group-item list-group-item-action"><i class="bi bi-send-fill"></i> &nbsp;Envoyer</a>
                <hr class="mt-2 mb-2 w-75">
            </div>
            <div>
                <a href="taux" class="list-group-item list-group-item-action"><i class="bi bi-currency-exchange mr-2"></i> Taux</a>
            </div>
        </div>
    </div>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container ml-5">
            <button type="button" class="btn btn-primary mb-4 mt-5" data-toggle="modal" data-target="#clientModal">
                Ajouter <i class="bi bi-person-fill"></i>
            </button>
            <table id="clientsTable" class="table table-striped mx-auto w-100">
                <thead>
                <tr>
                    <th >Téléphone</th>
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
                        <td class="pl-4">
                            <button class="btn btn-warning btn-sm edit-btn rounded-circle"
                                    data-numtel="<c:out value='${client.numtel()}'/>"
                                    data-nom="<c:out value='${client.nom()}'/>"
                                    data-sexe="<c:out value='${client.sexe()}'/>"
                                    data-pays="<c:out value='${client.pays()}'/>"
                                    data-solde="<c:out value='${client.solde()}'/>"
                                    data-mail="<c:out value='${client.mail()}'/>"
                                    data-toggle="modal"
                                    data-target="#clientModal">
                                <i class="bi bi-pen-fill"></i>
                            </button>
                            <button class="btn btn-info btn-sm rounded-circle gen-pdf-btn"
                                    data-numtel="<c:out value='${client.numtel()}'/>"
                                    data-toggle="modal"
                                    data-target="#monthYearModal">
                                <i class="bi bi-file-earmark-text-fill"></i>
                            </button>
                            <button class="btn btn-danger btn-sm delete-btn rounded-circle"
                                    data-numtel="<c:out value='${client.numtel()}'/>"
                                    data-nom="<c:out value='${client.nom()}'/>"
                                    data-toggle="modal"
                                    data-target="#deleteConfirmationModal">
                                <i class="bi bi-trash-fill"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!-- /#page-content-wrapper -->
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

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Téléphone</span>
                        </div>
                        <input type="text" class="form-control" id="numtel" name="numtel" required>
                    </div>

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Nom</span>
                        </div>
                        <input type="text" class="form-control" id="nom" name="nom" required>
                    </div>

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Sexe</span>
                        </div>
                        <div class="form-check form-check-inline ml-2">
                            <input class="form-check-input" type="radio" name="sexe" id="feminin" value="F" required>
                            <label class="form-check-label" for="feminin">F</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="sexe" id="masculin" value="M">
                            <label class="form-check-label" for="masculin">M</label>
                        </div>
                    </div>

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Pays</span>
                        </div>
                        <input type="text" class="form-control" id="pays" name="pays" required>
                    </div>

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Solde</span>
                        </div>
                        <input type="number" class="form-control" id="solde" name="solde" required>
                    </div>

                    <div class="input-group mb-4">
                        <div class="input-group-prepend" style="min-width: 90px;">
                            <span class="input-group-text" style="width: 100%;">Mail</span>
                        </div>
                        <input type="email" class="form-control" id="mail" name="mail" required>
                    </div>

                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveClientBtn">Enregistrer</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
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

<!-- Modal for Month/Year Selection -->
<div class="modal fade" id="monthYearModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Générer un relevé d'opération</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="monthYearForm">
                    <div class="form-group">
                        <label for="numtel_modal">Numéro de téléphone :</label>
                        <input type="text" class="form-control" id="numtel_modal" name="numtel" readonly>
                    </div>
                    <div class="form-group">
                        <label for="monthYear">Mois :</label>
                        <input type="month" class="form-control" id="monthYear" name="monthYear" required>
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="genPdfButton" data-dismiss="modal">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            </div>

        </div>
    </div>
</div>

<!-- JavaScript pour la confirmation de suppression -->
<script>
    $(document).ready(function() {
        // Initialisation de DataTables
        $('#clientsTable').DataTable({
            "pagingType": "simple_numbers",
            "language": {
                "lengthMenu": "Afficher _MENU_ enregistrements par page",
                "zeroRecords": "Aucun enregistrement trouvé",
                "info": "Affichage de _PAGE_ sur _PAGES_",
                "infoEmpty": "Aucun enregistrement disponible",
                "infoFiltered": "",
                "search": "Recherche:",
                "paginate": {
                    "first": "Premier",
                    "last": "Dernier",
                    "next": "Suivant",
                    "previous": "Précédent"
                }
            }
        });

        // Ouvrir le formulaire modal pour l'édition
        $('.edit-btn').on('click', function() {
            const button = $(this);
            $('#formAction').val('update');
            $('#numtel').val(button.data('numtel'));
            $('#numtel').prop('readonly', true);
            $('#nom').val(button.data('nom'));
            // Sélectionner le sexe correspondant
            const sexe = button.data('sexe');
            if (sexe === 'F') {
                $('#feminin').prop('checked', true);
            } else {
                $('#masculin').prop('checked', true);
            }
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

        // Ouvrir le formulaire modal pour générer le relevé d'opération
        $('.gen-pdf-btn').on('click', function() {
            const button = $(this);
            $('#numtel_modal').val(button.data('numtel'));
        });

        // Soumettre le formulaire pour générer le relevé d'opération
        $('#genPdfButton').on('click', function() {
            $('#monthYearForm').submit();
        });

        // Nettoyer le formulaire de suppression à sa fermeture
        $('#deleteConfirmationModal').on('hidden.bs.modal', function () {
            $('#deleteForm')[0].reset();
        });

        // Redirect to pdf generation servlet
        $('#monthYearForm').on('submit', function(e) {
            e.preventDefault();
            const numtel = $('#numtel_modal').val();
            const [year, month] = $('#monthYear').val().split('-');
            console.log(month, year);
            window.location.href = 'gen-pdf?client=' + numtel + '&month=' + month + '&year=' + year;
        });

    });
</script>
</body>
</html>
