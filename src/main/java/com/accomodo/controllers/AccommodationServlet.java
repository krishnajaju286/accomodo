package com.accomodo.controllers;

import com.accomodo.dao.AccommodationDAO;
import com.accomodo.models.Accommodation;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/accommodations")
public class AccommodationServlet extends HttpServlet {
    private AccommodationDAO accommodationDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        accommodationDAO = new AccommodationDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String query = request.getParameter("q");
        List<Accommodation> list;

        if (query != null && !query.trim().isEmpty()) {
            list = accommodationDAO.searchByQuery(query);
        } else {
            list = accommodationDAO.getAllAccommodations();
        }

        PrintWriter out = response.getWriter();
        String jsonResponse = this.gson.toJson(list);
        out.print(jsonResponse);
        out.flush();
    }
}
