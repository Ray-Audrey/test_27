import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet
{
    static ArrayList<String[]> employees = new ArrayList<>();

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<h2>Employee Management</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Salary</th></tr>");

        for(String[] e : employees)
        {
            out.println("<tr>");
            out.println("<td>" + e[0] + "</td>");
            out.println("<td>" + e[1] + "</td>");
            out.println("<td>" + e[2] + "</td>");
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
        String name = req.getParameter("name");
        String salary = req.getParameter("salary");

        if("insert".equals(action))
        {
            employees.add(new String[]{id, name, salary});
            out.println("Employee Inserted Successfully");
        }
        else if("delete".equals(action))
        {
            boolean found = employees.removeIf(e -> e[0].equals(id));

            out.println(found ? "Employee Deleted Successfully" :
                                "Employee Not Found");
        }
        else if("update".equals(action))
        {
            String oldId = req.getParameter("oldId");
            boolean found = false;

            for(String[] e : employees)
            {
                if(e[0].equals(oldId))
                {
                    e[0] = id;
                    e[1] = name;
                    e[2] = salary;
                    found = true;
                    break;
                }
            }

            out.println(found ? "Employee Updated Successfully" :
                                "Employee Not Found");
        }

        out.println("<br><a href='index.html'>Back</a>");
        out.println("<br><a href='EmployeeServlet'>View Employees</a>");
    }
}