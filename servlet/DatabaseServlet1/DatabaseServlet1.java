import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;

public class DatabaseServlet1 extends HttpServlet{
    protected void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/STUDENT",
                "root",
                "Root12345!"
            );
            PreparedStatement ps=con.prepareStatement("SELECT * FROM Students");
            ResultSet rs=ps.executeQuery();
            res.setContentType("text/html");
            PrintWriter out=res.getWriter();
            out.println("<table border='1'>");
            out.println("<tr><th>Name</th><th>Id</th></tr>");
            while(rs.next()){
                out.println("<tr>");
                out.println("<th>"+rs.getString("name")+"</th>");
                out.println("<th>"+rs.getInt("id")+"</th>");
                out.println("</tr>");
            }
            out.println("</table>");

            con.close();
        }
        catch(Exception e){
            res.setContentType("text/html");
            PrintWriter out=res.getWriter();
            out.println(e.getMessage());
        }
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        String action=req.getParameter("action");
        if("insert".equals(action)){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/STUDENT",
                    "root",
                    "Root12345!"
                );
                String newName=req.getParameter("name");
                int newId=Integer.parseInt(req.getParameter("id"));

                PreparedStatement ps=con.prepareStatement("INSERT INTO Students(name,id) VALUES(?,?)");
                ps.setString(1,newName);
                ps.setInt(2,newId);
                int rows=ps.executeUpdate();
                res.setContentType("text/html");
                PrintWriter out=res.getWriter();
                if(rows > 0)
                    out.println("Record Inserted Successfully");
                else
                    out.println("Insert Failed");
                con.close();
            }
            catch(Exception e){
                res.setContentType("text/html");
                PrintWriter out=res.getWriter();
                out.println(e.getMessage());
            }
        }
        
        else if("delete".equals(action)){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/STUDENT",
                    "root",
                    "Root12345!"
                );

                int id = Integer.parseInt(req.getParameter("id"));

                PreparedStatement ps =
                    con.prepareStatement("DELETE FROM Students WHERE id=?");

                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                res.setContentType("text/html");
                PrintWriter out = res.getWriter();

                if (rows > 0)
                    out.println("Record Deleted Successfully");
                else
                    out.println("Record Not Found");

                con.close();
            }
            catch (Exception e) {
                res.setContentType("text/html");
                res.getWriter().println(e.getMessage());
            }

        }
        else if("update".equals(action)){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/STUDENT",
                    "root",
                    "Root12345!"
                );

                int oldId = Integer.parseInt(req.getParameter("oldId"));
                String name = req.getParameter("name");
                int id = Integer.parseInt(req.getParameter("id"));

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE Students SET name=?, id=? WHERE id=?"
                );

                ps.setString(1, name);
                ps.setInt(2, id);
                ps.setInt(3, oldId);

                int rows = ps.executeUpdate();

                res.setContentType("text/html");
                PrintWriter out = res.getWriter();

                if (rows > 0)
                    out.println("Record Updated Successfully");
                else
                    out.println("Record Not Found");

                con.close();
            }
            catch (Exception e) {
                res.setContentType("text/html");
                res.getWriter().println(e.getMessage());
            }
        }
    }
}


