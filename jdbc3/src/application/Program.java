package application;
import db.DB;


import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;




public class Program {
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DB.getConnection();
/*
            st = conn.prepareStatement(
                    "INSERT INTO seller"
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                            + "VALUES"
                            + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, "Carl Purple");
            st.setString(2, "carl@gmail.com");
            st.setDate(3, new Date(sdf.parse("01/06/1987").getTime()));
            st.setDouble(4, 4000.0);
            st.setInt(5, 4);

 */
            st = conn.prepareStatement(
                    "insert into department (Name) values ('Tecnologia'), ('Science')",
                    Statement.RETURN_GENERATED_KEYS);

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0)
            {
                ResultSet rs = st.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }
            }
            else{
                System.out.println("No rown affected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            DB.closedStatement(st);
            DB.closeConnection();
        }
    }
}
