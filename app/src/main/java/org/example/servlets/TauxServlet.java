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
    public void init() {
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
        float montant1 = Float.parseFloat(request.getParameter("montant1"));
        float montant2 = Float.parseFloat(request.getParameter("montant2"));
        String pays1 = request.getParameter("pays1");
        String pays2 = request.getParameter("pays2");

        Taux newTaux = new Taux(pays1, pays2, montant1, montant2);
        tauxDAO.insertTaux(newTaux);
        response.sendRedirect("taux");
    }

    private void updateTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String pays1 = request.getParameter("pays1");
        String pays2 = request.getParameter("pays2");
        float montant1 = Float.parseFloat(request.getParameter("montant1"));
        float montant2 = Float.parseFloat(request.getParameter("montant2"));

        Taux taux = new Taux(pays1, pays2, montant1, montant2);
        tauxDAO.updateTaux(taux);
        response.sendRedirect("taux");
    }

    private void deleteTaux(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String idTaux = request.getParameter("idTaux");
        tauxDAO.deleteTaux(idTaux);
        response.sendRedirect("taux");
    }
}
