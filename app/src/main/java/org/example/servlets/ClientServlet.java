package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.ClientDao;
import org.example.models.Client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/client")
public class ClientServlet extends HttpServlet {

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
            if ("list".equals(action)) {
                listClients(request, response);
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
            if ("insert".equals(action)) {
                insertClient(request, response);
            } else if ("update".equals(action)) {
                updateClient(request, response);
            } else if ("delete".equals(action)) {
                deleteClient(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Client> listClients = clientDAO.listAllClients();
        request.setAttribute("listClients", listClients);
        request.getRequestDispatcher("client.jsp").forward(request, response);
    }

    private void insertClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Client newClient = getClientFromParams(request);
        clientDAO.insertClient(newClient);
        response.sendRedirect("client");
    }

    private Client getClientFromParams(HttpServletRequest request) {
        String numtel = request.getParameter("numtel");
        String nom = request.getParameter("nom");
        String sexe = request.getParameter("sexe");
        String pays = request.getParameter("pays");
        int solde = Integer.parseInt(request.getParameter("solde"));
        String mail = request.getParameter("mail");
        return new Client(numtel, nom, sexe, pays, solde, mail);
    }

    private void updateClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String originalNumtel = request.getParameter("originalNumtel");
        Client client = getClientFromParams(request);
        clientDAO.updateClient(client);
        response.sendRedirect("client");
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        clientDAO.deleteClient(numtel);
        response.sendRedirect("client");
    }
}
