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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            insertClient(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Client> listClients = clientDAO.listAllClients();
        request.setAttribute("listClients", listClients);
        request.getRequestDispatcher("client.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/client/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String numtel = request.getParameter("numtel");
        Client existingClient = clientDAO.getClient(numtel);
        request.setAttribute("client", existingClient);
        request.getRequestDispatcher("/client/form.jsp").forward(request, response);
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
