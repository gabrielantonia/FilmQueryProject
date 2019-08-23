package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

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

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		System.out.println("started.");
		DatabaseAccessorObject DAO = new DatabaseAccessorObject();
		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter Film ID# :");
//		System.out.println(DAO.findFilmById(sc.nextInt()));
		System.out.println("Enter Actor ID # :");
		System.out.println(DAO.findActorById(sc.nextInt()));
		sc.close();

	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "select * from film where id = ? ;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		film = new Film(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6),
				rs.getDouble(7), rs.getInt(8), rs.getDouble(9), rs.getString(10), rs.getString(11));

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
		actor = new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));
		rs.close();
		stmt.close();
		conn.close();
		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		Actor

		Connection conn = DriverManager.getConnection(URL, userName, password);
		String sql = "select * from actor join film_actor on film_actor.actor_id = actor.id join film on film.id = film_actor.film_id where film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
//		stmt.setInt(1, storeId);
		ResultSet rs = stmt.executeQuery();
		int count = 1;
//		while (rs.next()) {
//			System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
//		}
		while (rs.next() ) {
			
		}
		rs.close();
		stmt.close();
		conn.close();
		return actor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatabaseAccessorObject []");
		return builder.toString();
	}

}
