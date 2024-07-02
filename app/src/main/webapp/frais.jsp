<%--
  Created by IntelliJ IDEA.
  User: mario
  Date: 25/06/2024
  Time: 08:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="fr_FR"/>
<!DOCTYPE html>
<html>
<head>
    <title>Frais</title>
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
        #FraisTable th{
            width: 120px;
            text-align: center;
        }
        #FraisTable td{
            text-align: left;
        }
        .Contenu{
            margin-left: 80px;
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
                <a href="envoyer" class="list-group-item list-group-item-action"><i class="bi bi-send-fill"></i> &nbsp;Transaction</a>
                <hr class="mt-2 mb-2  w-75">
            </div>
            <div>
                <a href="taux" class="list-group-item list-group-item-action"><i class="bi bi-currency-exchange mr-2"></i> Taux</a>
            </div>
        </div>
    </div>

    <div class="ml-5">
        <div class="container Contenu">
            <button type="button" class="btn btn-primary mb-4 mt-5" data-toggle="modal" data-target="#fraisModal">
                Ajouter <i class="bi bi-currency-dollar"></i>
            </button>
            <table id="FraisTable" class="table table-striped  mx-auto w-100">
                <thead>
                <tr>
                    <th>IdFrais</th>
                    <th>Montant 1</th>
                    <th>Montant 2</th>
                    <th>Frais</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="frais" items="${listFrais}">
                    <tr>
                        <td><c:out value="${frais.idFrais()}"/></td>
                        <td><fmt:formatNumber value="${frais.montant1()}" type="number" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true"/></td>
                        <td><fmt:formatNumber value="${frais.montant2()}" type="number" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true"/></td>
                        <td><fmt:formatNumber value="${frais.frais()}" type="number" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true"/></td>
                        <td class="pl-5">
                            <button class="btn btn-warning btn-sm edit-btn rounded-circle"
                                    data-idfrais="<c:out value='${frais.idFrais()}'/>"
                                    data-montant1="<c:out value='${frais.montant1()}'/>"
                                    data-montant2="<c:out value='${frais.montant2()}'/>"
                                    data-frais="<c:out value='${frais.frais()}'/>"
                                    data-pays="<c:out value='${frais.pays()}'/>"
                                    data-toggle="modal"
                                    data-target="#fraisModal">
                                <i class="bi bi-pen-fill"></i>
                            </button>
                            <button class="btn btn-danger btn-sm delete-btn rounded-circle"
                                    data-idfrais="<c:out value='${frais.idFrais()}'/>"
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


<!-- Modal for Add/Edit Frais -->
<div class="modal fade" id="fraisModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Frais</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form id="fraisForm" action="frais" method="post">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="idfrais" name="idfrais">
                    <div class="form-group">
                        <label for="pays">Pays:</label>
                        <input type="text" class="form-control" id="pays" name="pays" required>
                    </div>
                    <div class="form-group">
                        <label for="montant1">Montant 1:</label>
                        <input type="number" min="0" class="form-control" id="montant1" name="montant1">
                    </div>
                    <div class="form-group">
                        <label for="montant2">Montant 2:</label>
                        <input type="number" min="0" class="form-control" id="montant2" name="montant2">
                    </div>
                    <div class="form-group">
                        <label for="frais">Frais:</label>
                        <input type="number" min="0" class="form-control" id="frais" name="frais">
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveFraisBtn">Enregistrer</button>
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
                <p>Êtes-vous sûr de vouloir supprimer ?</p>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">
                <form id="deleteForm" action="frais" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="deleteIdfrais" name="idfrais">
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
                </form>
            </div>

        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#FraisTable').DataTable({
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
            $('#idfrais').val(button.data('idfrais'));
            $('#pays').val(button.data('pays'));
            $('#pays').prop('readonly', true); // Empêcher la modification du pays (clé primaire)
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

