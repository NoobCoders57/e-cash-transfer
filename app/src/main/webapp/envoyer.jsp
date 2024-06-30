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
    <link href="https://cdn.datatables.net/1.11.3/css/dataTables.bootstrap4.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/dataTables.bootstrap4.min.js"></script>

    <style>
        #sidebar-wrapper {
            width: 220px; /* Élargir le sidebar */
            justify-content: center;
            height: 100vh; /* Hauteur primaire */
            background-color: #40A578; /* Couleur de fond primaire */
        }

        .list-group-item-action:hover {
            background-color: rgb(167, 237, 206, 0.3); /* Changer le background en info au hover */
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

        #EnvoyerTable th {
            width: 170px;
            text-align: center;
        }

        #EnvoyerTable td {
            text-align: left;
        }

    </style>
</head>
<body>

<div class="d-flex">
    <div id="sidebar-wrapper">
        <div class="list-group list-group-flush sidebar-content">
            <div class="nav1 mt-5">
                <a href="client" class="list-group-item list-group-item-action"><i class="bi bi-person-circle"></i>&nbsp;
                    Clients</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="frais" class="list-group-item list-group-item-action"><i class="bi bi-coin"></i>
                    &nbsp;Frais</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="envoyer" class="list-group-item list-group-item-action"><i class="bi bi-send-fill"></i> &nbsp;Envoyer</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="taux" class="list-group-item list-group-item-action"><i
                        class="bi bi-currency-exchange mr-2"></i> Taux</a>
            </div>
        </div>
    </div>

    <div>
        <div class="container ml-5">
            <button type="button" class="btn btn-primary mb-4 mt-5" data-toggle="modal" data-target="#envoyerModal">
                Ajouter <i class="bi bi-arrow-left-right"></i>
            </button>
            <table id="EnvoyerTable" class="table table-striped  mx-auto w-100">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Envoyeur</th>
                    <th>Récepteur</th>
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
                        <td class="pl-4">
                            <button class="btn btn-warning btn-sm edit-btn rounded-circle"
                                    data-idenv="${envoyer.idEnv()}"
                                    data-numenvoyeur="${envoyer.numEnvoyeur()}"
                                    data-numrecepteur="${envoyer.numRecepteur()}"
                                    data-montant="${envoyer.montant()}"
                                    data-date="${envoyer.date()}"
                                    data-raison="${envoyer.raison()}"
                                    data-toggle="modal"
                                    data-target="#envoyerModal">
                                <i class="bi bi-pen-fill"></i>
                            </button>
                            <button class="btn btn-danger btn-sm delete-btn rounded-circle"
                                    data-idenv="${envoyer.idEnv()}"
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

        <div class="text-center align-content-center align-items-center">

            <h4>
                <i class="bi bi-bank2 m-3"></i>
                RECETTE : <c:out value="${recette}"/> MGA
            </h4>
        </div>
    </div>


</div>


<!-- Modal for Add/Edit Envoyer -->
<div class="modal fade" id="envoyerModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Transaction</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="envoyerForm" action="envoyer" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="idenv" name="idenv">
                    <input type="hidden" id="soldeEnvoyeur" name="soldeEnvoyeur">
                    <div class="form-group">
                        <label for="numEnvoyeur">Envoyeur:</label>
                        <select class="form-control" id="numEnvoyeur" name="numEnvoyeur" required>
                            <c:forEach var="client" items="${listClients}">
                                <option data-solde="<c:out value='${client.solde()}'/>"
                                        value="<c:out value='${client.numtel()}'/>"><c:out
                                        value='${client.nom()}'/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="numRecepteur">Récepteur:</label>
                        <select class="form-control" id="numRecepteur" name="numRecepteur" required>
                            <c:forEach var="client" items="${listClients}">
                                <option value="<c:out value='${client.numtel()}'/>"><c:out
                                        value='${client.nom()}'/></option>
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
                <button type="button" class="btn btn-primary" id="saveEnvoyerBtn">Enregistrer</button>
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
                <p>Êtes-vous sûr de vouloir supprimer cette transaction ?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="envoyer" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteIdenv" name="idenv">
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
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
                <p>Le solde est insuffisant pour effectuer cette transaction.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {

        $('#EnvoyerTable').DataTable({
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

        // Filtrer les options du récepteur en fonction de l'envoyeur sélectionné
        $('#numEnvoyeur').on('change', function () {
            var selectedEnvoyeur = $(this).val();
            var selectedSolde = $(this).find('option:selected').data('solde');
            $('#soldeEnvoyeur').val(selectedSolde);

            $('#numRecepteur option').each(function () {
                if ($(this).val() === selectedEnvoyeur) {
                    $(this).prop('disabled', true);
                } else {
                    $(this).prop('disabled', false);
                }
            });
        });

        // Ouvrir le formulaire modal pour l'édition
        $('.edit-btn').on('click', function () {
            const button = $(this);
            $('#formAction').val('update');
            $('#idenv').val(button.data('idenv'));
            $('#numEnvoyeur').val(button.data('numenvoyeur'));
            $('#numRecepteur').val(button.data('numrecepteur'));
            $('#montant').val(button.data('montant'));
            $('#date').val(button.data('date'));
            $('#raison').val(button.data('raison'));

            // Mettre à jour le solde de l'envoyeur
            var selectedSolde = $('#numEnvoyeur').find('option:selected').data('solde');
            $('#soldeEnvoyeur').val(selectedSolde);
        });

        async function checkSoldeAndSubmit() {
            var montant = parseInt($('#montant').val());
            var soldeEnvoyeur = parseInt($('#soldeEnvoyeur').val());

            console.log($('#soldeEnvoyeur').val());
            $.get('/app/frais?action=get_frais&montant=' + montant, function (data) {
                montant += parseInt(data['frais']);
                // Vérifier si le montant est supérieur au solde de l'envoyeur
                if (montant > soldeEnvoyeur) {
                    // Afficher le modal d'insuffisance de solde
                    $('#insufficientFundsModal').modal('show');
                } else {
                    // Soumettre le formulaire si le solde est suffisant
                    $('#envoyerForm').submit();
                }
            });
        }

        // Réinitialiser le formulaire modal à sa fermeture
        $('#envoyerModal').on('hidden.bs.modal', function () {
            $('#envoyerForm')[0].reset();
            $('#formAction').val('insert');
            $('#idenv').val('');
        });

        // Soumettre le formulaire envoyé
        $('#saveEnvoyerBtn').on('click', function () {
            checkSoldeAndSubmit();
        });

        // Afficher la boîte de dialogue de confirmation de suppression
        $('.delete-btn').on('click', function () {
            const button = $(this);
            const idenv = button.data('idenv');
            $('#deleteIdenv').val(idenv);
        });
    });

</script>
</body>
</html>
