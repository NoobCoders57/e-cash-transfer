package org.example.servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.FraisDao;
import org.example.models.Frais;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/frais")
public class FraisServlet extends HttpServlet {

    private FraisDao fraisDAO;

    @Override
    public void init() {
        fraisDAO = new FraisDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || "list".equals(action)) {
            try {
                listFrais(request, response);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else if (action.equals("get_frais")) {
            try {
                getFraisForMontant(request, response);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void getFraisForMontant(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws SQLException, IOException {
        HashMap<String, Integer> hashMap = new HashMap<>();
        int montant = Integer.parseInt(request.getParameter("montant"));

        response.setContentType("application/json");
        hashMap.put("frais", fraisDAO.getFraisValueForMontant(montant));
        response.getWriter().println(new Gson().toJson(hashMap));
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertFrais(request, response);
                    break;
                case "update":
                    updateFrais(request, response);
                    break;
                case "delete":
                    deleteFrais(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Frais> listFrais = fraisDAO.listAllFrais();
        request.setAttribute("listFrais", listFrais);
        request.getRequestDispatcher("/frais.jsp").forward(request, response);
    }

    private void insertFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int frais = Integer.parseInt(request.getParameter("frais"));

        Frais newFrais = new Frais(montant1, montant2, frais);
        fraisDAO.insertFrais(newFrais);
        response.sendRedirect("frais");
    }

    private void updateFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idfrais = Integer.parseInt(request.getParameter("idfrais"));
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int frais = Integer.parseInt(request.getParameter("frais"));

        Frais updatedFrais = new Frais(idfrais, montant1, montant2, frais);
        fraisDAO.updateFrais(updatedFrais);
        response.sendRedirect("frais");
    }

    private void deleteFrais(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idfrais = Integer.parseInt(request.getParameter("idfrais"));
        fraisDAO.deleteFrais(idfrais);
        response.sendRedirect("frais");
    }
}
