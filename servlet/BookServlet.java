import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BookServlet extends HttpServlet
{
    static ArrayList<String[]> books = new ArrayList<>();

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<h2>Book Management</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Title</th><th>Author</th></tr>");

        for(String[] b : books)
        {
            out.println("<tr>");
            out.println("<td>" + b[0] + "</td>");
            out.println("<td>" + b[1] + "</td>");
            out.println("<td>" + b[2] + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<br><a href='index.html'>Back</a>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String author = req.getParameter("author");

        if("insert".equals(action))
        {
            books.add(new String[]{id, title, author});
            out.println("Book Inserted Successfully");
        }
        else if("delete".equals(action))
        {
            boolean found = books.removeIf(b -> b[0].equals(id));

            out.println(found ? "Book Deleted Successfully" :
                                "Book Not Found");
        }
        else if("update".equals(action))
        {
            String oldId = req.getParameter("oldId");
            boolean found = false;

            for(String[] b : books)
            {
                if(b[0].equals(oldId))
                {
                    b[0] = id;
                    b[1] = title;
                    b[2] = author;
                    found = true;
                    break;
                }
            }

            out.println(found ? "Book Updated Successfully" :
                                "Book Not Found");
        }

        out.println("<br><a href='index.html'>Back</a>");
        out.println("<br><a href='BookServlet'>View Books</a>");
    }
}