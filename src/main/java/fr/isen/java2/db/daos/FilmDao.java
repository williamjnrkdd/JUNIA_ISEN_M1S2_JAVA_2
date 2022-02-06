package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {

	public List<Film> listFilms() {
		List<Film> films = new ArrayList<Film>();
		try(Connection connection = DataSourceFactory.getConnection()){
			try(Statement statement = connection.createStatement()){
				try(ResultSet result = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre")){
					while(result.next()) {
						films.add(new Film(result.getInt("idfilm"),
								result.getString("title"),
								result.getDate("release_date").toLocalDate(),
								new Genre(result.getInt("genre_id"),result.getString("name")),
								result.getInt("duration"),
								result.getString("director"),
								result.getString("summary")
								));
					}
					return films;
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

	public List<Film> listFilmsByGenre(String genreName) {
		List<Film> films = new ArrayList<Film>();
		try(Connection connection = DataSourceFactory.getConnection()){
			try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = ?")){
				statement.setString(1, genreName);
				try(ResultSet result = statement.executeQuery()){
					while(result.next()) {
						films.add(new Film(result.getInt("idfilm"),
								result.getString("title"),
								result.getDate("release_date").toLocalDate(),
								new Genre(result.getInt("genre_id"),result.getString("name")),
								result.getInt("duration"),
								result.getString("director"),
								result.getString("summary")
								));
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
		return films;
	}

	public Film addFilm(Film film) {
		try(Connection connection = DataSourceFactory.getConnection()){
			try(PreparedStatement statement = connection.prepareStatement("INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, film.getTitle());
				statement.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
				statement.setInt(3, film.getGenre().getId());
				statement.setInt(4, film.getDuration());
				statement.setString(5, film.getDirector());
				statement.setString(6, film.getSummary());
				statement.executeUpdate();
				try(ResultSet result = statement.getGeneratedKeys()){
					if(result.next()) {
						return new Film(result.getInt(1),
									film.getTitle(),
									film.getReleaseDate(),
									film.getGenre(),
									film.getDuration(),
									film.getDirector(),
									film.getSummary()
									);
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
}
