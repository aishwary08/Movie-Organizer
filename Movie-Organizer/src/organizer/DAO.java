package organizer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DAO {
	DBConnection db = new DBConnection();

	void insertMovie(Movie m) throws SQLException {
		if (doesExist(m)) {
			JOptionPane.showMessageDialog(null, m.getName() + " exists with " + m.getOwner(), "Already There...",
					JOptionPane.ERROR_MESSAGE);
		} else {
			String query = "insert into Collection(Name,Owner,Date) values(?,?,?)";
			PreparedStatement stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, m.getName());
			stmt.setString(2, m.getOwner());
			stmt.setDate(3, java.sql.Date.valueOf(m.getDate()));
			int success = stmt.executeUpdate();
			if (success == 1)
				System.out.println("Data Inserted Successfully");
			else
				System.out.println("Error Occurred");
		}
	}

	/*
	 * void deleteEmp(Employee e) throws SQLException { String query =
	 * "delete from details where name=?"; PreparedStatement stmt =
	 * db.getConnection().prepareStatement(query); stmt.setString(1,
	 * e.getName()); int success = stmt.executeUpdate(); if(success==1)
	 * System.out.println("Data Deleted Successfully"); else System.out.println(
	 * "Employee Not Found!"); }
	 */

	boolean doesExist(Movie m) throws SQLException {
		boolean exist = false;
		String query = "select * from Collection where name=?";
		PreparedStatement stmt = db.getConnection().prepareStatement(query);
		stmt.setString(1, m.getName());
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			exist = true;
			m.setOwner(rs.getString(3));
		}
		return exist;
	}

	/*
	 * void displayAll() throws SQLException { String query =
	 * "select * from details"; PreparedStatement stmt =
	 * db.getConnection().prepareStatement(query); ResultSet rs =
	 * stmt.executeQuery(); if(!rs.next()) System.out.println("Database Empty!"
	 * ); else{ do{ print(rs); }while(rs.next()); } }
	 * 
	 * void print(ResultSet rs) throws SQLException { System.out.println(
	 * "Name: "+rs.getString(1)); System.out.println("Department: "
	 * +rs.getString(2)); System.out.println("Age: "+rs.getInt(3));
	 * System.out.println("Salary: "+rs.getDouble(4)); }
	 */
}
