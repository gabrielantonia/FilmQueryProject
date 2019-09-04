package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
	}

	private void launch() throws SQLException {

		System.out.println("started.");
		DatabaseAccessorObject dAO = new DatabaseAccessorObject();
		Scanner sc = new Scanner(System.in);
		boolean quit = false;
		while (quit == false) {
			quit = menu1(sc, dAO);
		}
		sc.close();
	}

	private Boolean menu1(Scanner sc, DatabaseAccessorObject dAO) throws SQLException {

		boolean quit = false;
		System.out.println("---------------------------------------");
		System.out.println("Welcome, what would you like to do?");
		System.out.println("1. Look up a film by its ID. ");
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

				Film film = dAO.findFilmById(choice);
				if(film == null) {
					System.out.println("This film is not in stock");
				}
				else {
				System.out.println("---------------------------------------");
				System.out.println("Title: " + film.getTitle() + "\nRelease Year: " + film.getReleaseYear()
						+ "\nRating: " + film.getRating() + "\nDescription: " + film.getDescription() + ". "
						+ "\nLanguage: " + film.getLanguage() + "\nCategory: " + film.getCategory());
				System.out.print("Actors: ");
				for (Actor actor : film.getListOfActors()) {
					System.out.print(actor.getFirstName() + " " + actor.getLastName() + " | ");
				}
				System.out.println("\n---------------------------------------");
				System.out.println();
				menu2(sc, film);
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
			List<Film> list = dAO.findFilmByKeyword(keyword);
			if (list == null || list.isEmpty()) {
				System.out.println("No titles were found containing " + keyword + ".");
			} else {

				for (Film film1 : list) {
					System.out.println("---------------------------------------");
					System.out.println("Title: " + film1.getTitle() + "\nRelease Year: " + film1.getReleaseYear()
							+ "\nRating: " + film1.getRating() + "\nDescription: " + film1.getDescription() + ". "
							+ "\nLanguage: " + film1.getLanguage() + "\nCategory: " + film1.getCategory());
					System.out.print("Actors: ");
					for (Actor actor : film1.getListOfActors()) {
						System.out.print(actor.getFirstName() + " " + actor.getLastName() + " | ");
					}
					System.out.println("\n---------------------------------------");
					System.out.println();
					if (menu2(sc, film1) == true) {
						break;
					}
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

	private boolean menu2(Scanner sc, Film film) {
		System.out.println("1. See more details ");
		System.out.println("2. continue");
		System.out.println("3. back");
		int choice = 0;
		try {
			choice = sc.nextInt();
		} catch (Exception e) {
			System.out.println("invalid option");
		}
		boolean quit = false;
		boolean tostart = false;
		do {
			switch (choice) {
			case 1:
				System.out.println(film.toString());
				quit = true;
				break;
			case 2:
				quit = true;
				break;
			case 3:
				quit = true;
				tostart = true;
				break;
			default:
				System.out.println("invalid option");
				break;
			}
		} while (quit == false);
		return tostart;

	}
}
