package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		DatabaseAccessorObject dAO = new DatabaseAccessorObject();
		Scanner sc = new Scanner(System.in);
		boolean quit = false;
		while (quit == false) {
			quit = dAO.menu1(sc, dAO);
		}
	}

	private Boolean menu1(Scanner sc, DatabaseAccessorObject dAO) throws SQLException {

		boolean quit = false;
		System.out.println("---------------------------------------");
		System.out.println("Welcome, what would you like to do?");
		System.out.println("1. Look up a film by its ID. (1-1000)");
		System.out.println("2. Look up a film by a search keyword.");
		System.out.println("3. Exit the application.");
		System.out.println("---------------------------------------");
		int choice = 0;
		boolean valid = false;
		do {
			try {
				choice = sc.nextInt();
				valid = true;
			} catch (Exception e) {
				System.out.println("invalid input");
				sc.nextLine();
			}
		} while (valid == false);

		switch (choice) {
		case 1:
			System.out.println("Enter Film ID# :");
			valid = false;
			do {
				try {
					choice = sc.nextInt();
					valid = true;
				} catch (Exception e) {
					System.out.println("invalid input");
					sc.nextLine();
				}
			} while (valid == false);

			if (choice > 0 && choice <= 1000) {
				Film film = dAO.findFilmById(choice, dAO);
				System.out.println("---------------------------------------");
				System.out.println("Title: " + film.getTitle() + "\nRelease Year: " + film.getReleaseYear()
						+ "\nRating: " + film.getRating() + "\nDescription: " + film.getDescription() + ". "
						+ "\nLanguage: " + film.getLanguage() + "\nCategory: " + film.getCategory());
				System.out.print("Actors: ");
				for (Actor actor : film.getListOfActors()) {
					System.out.print(actor.getFirstName() + " " + actor.getLastName() + " | ");
				}
				System.out.println("---------------------------------------");
				System.out.println();
				dAO.menu2(sc, film);
			} else {
				System.out.println("This film is not in stock");
			}
			break;
		case 2:
			System.out.println("Enter a keyword :");
			String keyword = null;
			valid = false;
			do {
				try {
					keyword = sc.next();
					valid = true;
				} catch (Exception e) {
					System.out.println("invalid input");
					sc.nextLine();
				}
			} while (valid == false);
			List<Film> list = dAO.findFilmByKeyword(keyword, dAO);
			if (list == null || list.isEmpty()) {
				System.out.println("No titles were found containing " + keyword + ".");
			} else {
				for (Film film : list) {
					System.out.println("---------------------------------------");
					System.out.println("Title: " + film.getTitle() + "\nRelease Year: " + film.getReleaseYear()
							+ "\nRating: " + film.getRating() + "\nDescription: " + film.getDescription() + ". "
							+ "\nLanguage: " + film.getLanguage() + "\nCategory: " + film.getCategory());
					System.out.print("Actors: ");
					for (Actor actor : film.getListOfActors()) {
						System.out.print(actor.getFirstName() + " " + actor.getLastName() + " | ");
					}
					System.out.println("---------------------------------------");
					System.out.println();
					dAO.menu2(sc, film);
				}
			}
			break;
		case 3:
			System.out.println("Exiting application..");
			quit = true;
			break;
		default:
			break;
		}
		return quit;

	}

	private void menu2(Scanner sc, Film film) {
		System.out.println("1. See more details ");
		System.out.println("2. continue");
		int choice = 0;
		try {
			choice = sc.nextInt();
		} catch (Exception e) {
			System.out.println("invalid option");
		}
		boolean quit = false;
		
		do {
			switch (choice) {
			case 1:
				System.out.println(film.toString());
				quit = true;
				break;
			case 2:
				quit = true;
				break;
			default:
				System.out.println("invalid option");
				break;
			}
		} while (quit == false);
	}

	@Override
	public Film findFilmById(int filmId, DatabaseAccessorObject dAO) throws SQLException {
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

		List<Actor> la = null;

		film = new Film(rs.getInt("film.id"), rs.getString("film.title"), rs.getString("film.description"),
				rs.getInt("film.release_year"), rs.getInt("film.language_id"), rs.getInt("film.rental_duration"),
				rs.getDouble("film.rental_rate"), rs.getInt("film.length"), rs.getDouble("film.replacement_cost"),
				rs.getString("film.rating"), rs.getString("film.special_features"), rs.getString("language"),
				la = dAO.findActorsByFilmId(rs.getInt("film.id")), rs.getString("category name"));

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

	@Override
	public List<Film> findFilmByKeyword(String keyword, DatabaseAccessorObject dAO) throws SQLException {
		List<Film> filmlist = new ArrayList<Film>();
		List<Actor> la = null;
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
					la = dAO.findActorsByFilmId(rs.getInt("film.id")), rs.getString("category name")));
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
