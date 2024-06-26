package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.TauxDao;
import org.example.models.Taux;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TauxServlet {
    private TauxDao tauxDAO;

    public void init() {
        tauxDAO = new TauxDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertTaux(request, response);
                    break;
                case "delete":
                    deleteTaux(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateTaux(request, response);
                    break;
                case "list":
                default:
                    listTaux(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Taux> listTaux = tauxDAO.listAllTaux();
        request.setAttribute("listTaux", listTaux);
        request.getRequestDispatcher("/taux/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/taux/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int idtaux = Integer.parseInt(request.getParameter("idtaux"));
        Taux existingTaux = tauxDAO.getTaux(idtaux);
        request.setAttribute("taux", existingTaux);
        request.getRequestDispatcher("/taux/form.jsp").forward(request, response);
    }

    private void insertTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));

        Taux newTaux = new Taux(montant1, montant2);
        tauxDAO.insertTaux(newTaux);
        response.sendRedirect("taux");
    }

    private void updateTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idtaux = Integer.parseInt(request.getParameter("idtaux"));
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));

        Taux taux = new Taux(montant1, montant2);
        tauxDAO.updateTaux(taux);
        response.sendRedirect("taux");
    }

    private void deleteTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idtaux = Integer.parseInt(request.getParameter("idtaux"));
        tauxDAO.deleteTaux(idtaux);
        response.sendRedirect("taux");
    }
}