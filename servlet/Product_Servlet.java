import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProductServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PRODUCTDB",
                "root",
                "Root12345!"
            );

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Products");
            ResultSet rs = ps.executeQuery();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            out.println("<h2>Product Details</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Price</th></tr>");

            while(rs.next())
            {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            con.close();
        }
        catch(Exception e)
        {
            res.setContentType("text/html");
            res.getWriter().println(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        String action = req.getParameter("action");

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PRODUCTDB",
                "root",
                "Root12345!"
            );

            PreparedStatement ps = null;
            int rows = 0;

            if("insert".equals(action))
            {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                double price = Double.parseDouble(req.getParameter("price"));

                ps = con.prepareStatement(
                    "INSERT INTO Products(id, name, price) VALUES(?, ?, ?)"
                );

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setDouble(3, price);

                rows = ps.executeUpdate();
            }
            else if("delete".equals(action))
            {
                int id = Integer.parseInt(req.getParameter("id"));

                ps = con.prepareStatement("DELETE FROM Products WHERE id=?");
                ps.setInt(1, id);

                rows = ps.executeUpdate();
            }
            else if("update".equals(action))
            {
                int oldId = Integer.parseInt(req.getParameter("oldId"));
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                double price = Double.parseDouble(req.getParameter("price"));

                ps = con.prepareStatement(
                    "UPDATE Products SET id=?, name=?, price=? WHERE id=?"
                );

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setDouble(3, price);
                ps.setInt(4, oldId);

                rows = ps.executeUpdate();
            }

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            if(rows > 0)
                out.println("Operation Successful");
            else
                out.println("Record Not Found or Operation Failed");

            out.println("<br><a href='index.html'>Back</a>");
            out.println("<br><a href='ProductServlet'>View Products</a>");

            con.close();
        }
        catch(Exception e)
        {
            res.setContentType("text/html");
            res.getWriter().println(e.getMessage());
        }
    }
}