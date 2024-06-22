package org.example.servlets;

import org.example.models.client;
import org.example.DAO.ClientDao;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class clientServlet extends HttpServlet {

    private ClientDao clientDAO;

    @Override
    public void init() {
        clientDAO = new ClientDao();
    }

    @Override
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
                    insertClient(request, response);
                    break;
                case "delete":
                    deleteClient(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateClient(request, response);
                    break;
                case "list":
                default:
                    listClients(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<client> listClients = clientDAO.listAllClients();
        request.setAttribute("listClients", listClients);
        request.getRequestDispatcher("/client/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/client/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String numtel = request.getParameter("numtel");
        client existingClient = clientDAO.getClient(numtel);
        request.setAttribute("client", existingClient);
        request.getRequestDispatcher("/client/form.jsp").forward(request, response);
    }

    private void insertClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        String nom = request.getParameter("nom");
        String sexe = request.getParameter("sexe");
        String pays = request.getParameter("pays");
        int solde = Integer.parseInt(request.getParameter("solde"));
        String mail = request.getParameter("mail");

        client newClient = new client(numtel, nom, sexe, pays, solde, mail);
        clientDAO.insertClient(newClient);
        response.sendRedirect("client");
    }

    private void updateClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        String nom = request.getParameter("nom");
        String sexe = request.getParameter("sexe");
        String pays = request.getParameter("pays");
        int solde = Integer.parseInt(request.getParameter("solde"));
        String mail = request.getParameter("mail");

        client client = new client(numtel, nom, sexe, pays, solde, mail);
        clientDAO.updateClient(client);
        response.sendRedirect("client");
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        clientDAO.deleteClient(numtel);
        response.sendRedirect("client");
    }
}
