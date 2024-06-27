package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.TauxDao;
import org.example.models.Taux;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/taux")
public class TauxServlet extends HttpServlet {

    private TauxDao tauxDAO;

    @Override
    public void init() throws ServletException {
        tauxDAO = new TauxDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            if ("list".equals(action)) {
                listTaux(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertTaux(request, response);
                    break;
                case "update":
                    updateTaux(request, response);
                    break;
                case "delete":
                    deleteTaux(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Taux> listTaux = tauxDAO.listAllTaux();
        request.setAttribute("listTaux", listTaux);
        request.getRequestDispatcher("/taux.jsp").forward(request, response);
    }

    private void insertTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));

        Taux newTaux = new Taux(montant1, montant2);
        tauxDAO.insertTaux(newTaux);
        response.sendRedirect("taux");
    }

    private void updateTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idTaux = Integer.parseInt(request.getParameter("idTaux"));
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));

        Taux taux = new Taux(idTaux, montant1, montant2);
        tauxDAO.updateTaux(taux);
        response.sendRedirect("taux");
    }

    private void deleteTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idTaux = Integer.parseInt(request.getParameter("idTaux"));
        tauxDAO.deleteTaux(idTaux);
        response.sendRedirect("taux");
    }
}
