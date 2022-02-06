package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		List<Genre> genres = new ArrayList<Genre>();
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM genre")){
					while(result.next()) {
						genres.add(new Genre(result.getInt("idgenre"), result.getString("name")));
					}
					return genres;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Genre getGenre(String name) {
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM genre WHERE name = ?")){
				statement.setString(1, name);
				try(ResultSet result = statement.executeQuery()){
					if(result.next()) {
						return new Genre(result.getInt("idgenre"), result.getString("name"));
					}
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addGenre(String name) {
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			try(PreparedStatement statement = connection.prepareStatement("INSERT INTO genre(name) VALUES(?)")){
				statement.setString(1, name);
				statement.executeUpdate();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
