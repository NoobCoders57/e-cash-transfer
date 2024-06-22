package org.example.servlets;

import org.example.DAO.FraisDao;
import org.example.models.frais;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class fraisServlet {
    private FraisDao fraisDAO;
    
    public void init() {
        fraisDAO = new FraisDao();
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
                    insertFrais(request, response);
                    break;
                case "delete":
                    deleteFrais(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateFrais(request, response);
                    break;
                case "list":
                default:
                    listFrais(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<frais> listFrais = fraisDAO.listAllFrais();
        request.setAttribute("listFrais", listFrais);
        request.getRequestDispatcher("/frais/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/frais/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int idfrais = Integer.parseInt(request.getParameter("idfrais"));
        frais existingFrais = fraisDAO.getFrais(idfrais);
        request.setAttribute("frais", existingFrais);
        request.getRequestDispatcher("/frais/form.jsp").forward(request, response);
    }

    private void insertFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int fraisValue = Integer.parseInt(request.getParameter("frais"));

        frais newFrais = new frais(montant1, montant2, fraisValue);
        fraisDAO.insertFrais(newFrais);
        response.sendRedirect("frais");
    }

    private void updateFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idfrais = Integer.parseInt(request.getParameter("idfrais"));
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int fraisValue = Integer.parseInt(request.getParameter("frais"));

        frais frais = new frais(montant1, montant2, fraisValue);
        fraisDAO.updateFrais(frais);
        response.sendRedirect("frais");
    }

    private void deleteFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idfrais = Integer.parseInt(request.getParameter("idfrais"));
        fraisDAO.deleteFrais(idfrais);
        response.sendRedirect("frais");
    }
}
