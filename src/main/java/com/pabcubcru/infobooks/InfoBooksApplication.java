package com.pabcubcru.infobooks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.GenreEnum;
import com.pabcubcru.infobooks.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class InfoBooksApplication {

	@Autowired
	private ElasticsearchOperations elasticSearchOperations;

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(InfoBooksApplication.class, args);
	}

	@PostConstruct
	public void buildBookIndex() {
		bookRepository.deleteAll();
		elasticSearchOperations.indexOps(Book.class).refresh();
		List<Book> books = prepareDataset();
		bookRepository.saveAll(books);
		log.info("============== Added " + books.size() + " books. =================================================================================");
	}

	private List<Book> prepareDataset() {
		List<Book> res = new ArrayList<>();

		try {
			File myObj = new File("src\\main\\java\\com\\pabcubcru\\infobooks\\books.csv");
      		Scanner scanner = new Scanner(myObj);
			int lineNo = 0;
			while (scanner.hasNextLine()) {
				++lineNo;
				String line = scanner.nextLine();
				if(lineNo == 1) continue;
				Book book = csvRowToBookMapper(line);
				if(book != null) {
					res.add(book);
				}
			}
			scanner.close();
		} catch (Exception e) {
			log.error("File read error {}",e.getMessage());
		}

		return res;
	}

	private Book csvRowToBookMapper(final String line) {
		if(line!="" || line != null) {
			String[] s = line.split(";");
			List<String> genres = new ArrayList<>();
			List.of(GenreEnum.values()).stream().forEach(x -> genres.add(x.toString()));
			String isbn = s[0];
			String originalTitle = s[1];
			String title = s[2];
			String author = s[3];
			Integer publicationYear = 2010;
			if(s[4] != "") {
				String r = s[4].replace(".0", "");
				publicationYear = Integer.parseInt(r);
			}
			String publisher = s[5];
			String description = s[6];
			String urlImage = s[7];
			int numRandomGenre1 = (int) Math.floor(Math.random()*GenreEnum.values().length);
			int numRandomGenre2 = (int) Math.floor(Math.random()*GenreEnum.values().length);
			String genre = genres.get(numRandomGenre1);
			if(numRandomGenre1 != numRandomGenre2) {
				genre = genres.get(numRandomGenre1) + "," + genres.get(numRandomGenre2);
			}
			String status = "NUEVO";
			int numRandomAction = (int) Math.floor(Math.random()*2);
			String action = "INTERCAMBIO";
			Double price = null;
			if(numRandomAction == 1) {
				action = "VENTA";
				price = Double.parseDouble(String.valueOf(Math.floor(Math.random()*100)));
			}
			Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genre, author, description, urlImage, status, action, price, "carmen12");
			return book;
		} else {
			return null;
		}
	}

}
