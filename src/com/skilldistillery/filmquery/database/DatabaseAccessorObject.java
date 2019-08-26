package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private final String userName = "student";
	private final String password = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";


	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = "select film.id, film.title, film.description, film.release_year, film.language_id, film.rental_duration, film.rental_rate,film.length, film.replacement_cost, film.rating, film.special_features,language.name as 'language', category.name as 'category name' from film join language on language.id = film.language_id join film_category on film.id = film_category.film_id join category on category.id = film_category.category_id where film.id = ?;";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		rs.next();


		film = new Film(rs.getInt("film.id"), rs.getString("film.title"), rs.getString("film.description"),
				rs.getInt("film.release_year"), rs.getInt("film.language_id"), rs.getInt("film.rental_duration"),
				rs.getDouble("film.rental_rate"), rs.getInt("film.length"), rs.getDouble("film.replacement_cost"),
				rs.getString("film.rating"), rs.getString("film.special_features"), rs.getString("language"),
				findActorsByFilmId(rs.getInt("film.id")), rs.getString("category name"));

		rs.close();
		stmt.close();
		conn.close();
		return film;
	}

	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from actor where id = ? ;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		if(actorId>0 && actorId<1000) {
		actor = new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));
		}
		rs.close();
		stmt.close();
		conn.close();
		return actor;
	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		List<Film> filmlist = new ArrayList<Film>();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {

		}
		String sql = "select film.id, film.title, film.description, film.release_year, film.language_id, film.rental_duration, film.rental_rate,film.length, film.replacement_cost, film.rating, film.special_features,language.name as 'language', category.name as 'category name' from film join language on language.id = film.language_id join film_category on film.id = film_category.film_id join category on category.id = film_category.category_id where film.title like ?;";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + keyword + "%");
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			filmlist.add(new Film(rs.getInt("film.id"), rs.getString("film.title"), rs.getString("film.description"),
					rs.getInt("film.release_year"), rs.getInt("film.language_id"), rs.getInt("film.rental_duration"),
					rs.getDouble("film.rental_rate"), rs.getInt("film.length"), rs.getDouble("film.replacement_cost"),
					rs.getString("film.rating"), rs.getString("film.special_features"), rs.getString("language"),
					findActorsByFilmId(rs.getInt("film.id")), rs.getString("category name")));
		}
		rs.close();
		stmt.close();
		conn.close();
		return filmlist;
	}

	public List<Actor> findActorsByFilmId(int filmID) throws SQLException {
		List<Actor> list = new ArrayList<>();

		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "select * from actor join film_actor on film_actor.actor_id = actor.id join film on film.id = film_actor.film_id where film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmID);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			list.add(new Actor(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatabaseAccessorObject []");
		return builder.toString();
	}

}
