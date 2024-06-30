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
            color : ghostwhite;
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
        hr{
            border: 2px solid rgb(167, 237, 206, 0.3);
            border-radius: 25px;
        }
        #TauxTable th{
            width: 120px;
            text-align: center;
        }
        #TauxTable td{
            text-align: center;
        }
        .Contenu{
            margin-left: 140px;
        }

    </style>
</head>
<body>

<div class="d-flex" id="wrapper">

    <div id="sidebar-wrapper">
        <div class="list-group list-group-flush sidebar-content">
            <div class="nav1 mt-5">
                <a href="client" class="list-group-item list-group-item-action"><i class="bi bi-person-circle"></i>&nbsp; Clients</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="frais" class="list-group-item list-group-item-action"><i class="bi bi-coin"></i> &nbsp;Frais</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="envoyer" class="list-group-item list-group-item-action"><i class="bi bi-send-fill"></i> &nbsp;Envoyer</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="taux" class="list-group-item list-group-item-action"><i class="bi bi-currency-exchange mr-2"></i> Taux</a>
            </div>
        </div>
    </div>

    <div>
        <div class="container Contenu">
            <button type="button" class="btn btn-primary mb-4 mt-5" data-toggle="modal" data-target="#tauxModal">
                Ajouter <i class="bi bi-currency-exchange"></i>
            </button>
            <table id="TauxTable" class="table table-striped  mx-auto w-100">
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
                            <button class="btn btn-warning btn-sm edit-btn rounded-circle"
                                    data-id="${taux.idTaux()}"
                                    data-montant1="${taux.montant1()}"
                                    data-montant2="${taux.montant2()}"
                                    data-toggle="modal"
                                    data-target="#tauxModal">
                                <i class="bi bi-pen-fill"></i>
                            </button>
                            <button class="btn btn-danger btn-sm delete-btn rounded-circle"
                                    data-id="${taux.idTaux()}"
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
</div>


<!-- Modal for Add/Edit Taux -->
<div class="modal fade" id="tauxModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Taux </h4>
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
                <button type="button" class="btn btn-primary" id="saveTauxBtn">Enregistrer</button>
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
                <p>Êtes-vous sûr de vouloir supprimer ce taux ?</p>
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
        $('#TauxTable').DataTable({
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
