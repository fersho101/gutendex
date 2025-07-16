package com.example.gutendex.controller;

import com.example.gutendex.model.GutendexResponse;
import com.example.gutendex.service.ApiService;
import com.example.gutendex.service.DatabaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleController implements CommandLineRunner {
    private final ApiService apiService;
    private final DatabaseService databaseService;
    private final Scanner scanner;

    public ConsoleController(ApiService apiService, DatabaseService databaseService) {
        this.apiService = apiService;
        this.databaseService = databaseService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Gutendex Console App ---");
            System.out.println("1. Buscar libro por titulo");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos");
            System.out.println("5. Listar libros por idioma");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsAliveInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Opción no valida");
            }
        }

        System.out.println("Aplicación terminada.");
        scanner.close();
    }


    private void searchBookByTitle() {
        System.out.println("Ingrese el titulo del libro a buscar 🔍: ");
        String title = scanner.nextLine();

        GutendexResponse response = apiService.searchBookByTitle(title);

        if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
            GutendexResponse.BookResult bookResult = response.getResult().get(0);
            System.out.println("\nLibro encontrado 🤓: ");
            System.out.println("Titulo: " + bookResult.getAuthors().stream()
                    .map(a -> a.getName())
                    .collect(java.util.stream.Collectors.joining(", ")));
            System.out.println("Idiomas: " + String.join(", ", bookResult.getLanguages()));
            System.out.println("Descargas: " + bookResult.getDownloadCount());

            System.out.println("\n¿ Desea registrar este libro en la base de datos ? (s/n): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("s")) {
                databaseService.processAndSaveBook(bookResult);
                System.out.println("Libro registrado exitosamente!");
            }
        } else {
            System.out.println(" ❌ Libro no encontrado.");
        }
    }

    private void listRegisteredBooks() {
        var books = databaseService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        System.out.println("\nLibros registrados:");
        books.forEach(book -> {
            System.out.println("\nTitulo: " + book.getTitle());
            System.out.println("Autor(es): " + book.getAuthors().stream()
                    .map(a -> a.getName())
                    .collect(java.util.stream.Collectors.joining(", ")));
            System.out.println("idiomas: " + String.join(", ", book.getLanguages()));
            System.out.println("Descargas: " + book.getDownloadCount());
        });
    }

    private void listRegisteredAuthors() {
        var authors = databaseService.getAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        System.out.println("\nAutores registrados:");
        authors.forEach(author -> {
            System.out.println("\nNombre: " + author.getName());
            System.out.println("Año de nacimiento: " + author.getBirthYear());
            System.out.println("Año de fallecimiento: " +
                    (author.getDeathYear() != null ? author.getDeathYear() : "N/A"));
        });
    }

    private void listAuthorsAliveInYear() {
        System.out.println("Ingrese el año: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        var authors = databaseService.getAuthorsAliveInYear(year);

        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados que estuvieran vivos en " + year);
            return;
        }

        System.out.println("\nAutores vivos en " + year + ":");
        authors.forEach(author -> {
            System.out.println("- " + author.getName() + " (" + author.getBirthYear() + (author.getDeathYear() != null ? " - " + author.getDeathYear() : "") + ")");
        });
    }

    private void listBooksByLanguage() {
        System.out.println("Ingrese el codigo del idioma (ej. en, es, fr): ");
        String language = scanner.nextLine().toLowerCase();

        var books = databaseService.getBooksBylanguage(language);

        if (books.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma '" + language + "'");
            return;
        }

        System.out.println("\nLibros en " + language + ":");
        books.forEach(book -> {
            System.out.println("- " + book.getTitle() +
                    " (Autores: " + book.getAuthors().stream()
                    .map(a -> a.getName())
                    .collect(java.util.stream.Collectors.joining(", ")) + ")");
        });
    }
}
