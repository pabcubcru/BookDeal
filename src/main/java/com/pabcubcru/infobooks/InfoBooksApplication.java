package com.pabcubcru.infobooks;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.GenreEnum;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.repository.AuthoritiesRepository;
import com.pabcubcru.infobooks.repository.BookRepository;
import com.pabcubcru.infobooks.repository.RequestRepository;
import com.pabcubcru.infobooks.repository.UserFavouriteBookRepository;
import com.pabcubcru.infobooks.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class InfoBooksApplication {

	@Autowired
	private ElasticsearchOperations elasticSearchOperations;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserFavouriteBookRepository userFavouriteBookRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	public static void main(String[] args) {
		SpringApplication.run(InfoBooksApplication.class, args);
	}

	public void deleteIndex() {
		requestRepository.deleteAll();
		userFavouriteBookRepository.deleteAll();
		bookRepository.deleteAll();
		userRepository.deleteAll();
	}

	public void buildBookIndexForTests() {
		Book book = new Book();

		book.setId("book-001");
        book.setTitle("Title test 1");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://assets.fireside.fm/file/fireside-images/podcasts/images/b/bc7f1faf-8aad-4135-bb12-83a8af679756/cover.jpg?v=3");
        book.setStatus("COMO NUEVO"); 
        book.setAction("VENTA"); 
		book.setPrice(10.);
        book.setUsername("test001");
		this.bookRepository.save(book);

		book = new Book();

		book.setId("book-002");
        book.setTitle("Title test 2");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://assets.fireside.fm/file/fireside-images/podcasts/images/b/bc7f1faf-8aad-4135-bb12-83a8af679756/cover.jpg?v=3");
        book.setStatus("COMO NUEVO"); 
        book.setAction("INTERCAMBIO");
        book.setUsername("test002");
		this.bookRepository.save(book);
	}

	public void buildAuthoritiesForTests(String username) {
		Authorities authorities = new Authorities();
		authorities.setId("authorities-"+username);
		authorities.setUsername(username);
		authorities.setAuthority("user");
		this.authoritiesRepository.save(authorities);
	}

	public void buildUserIndexForTests() {
		User user = new User();

		user.setId("userTest-pablo123");
        user.setName("Pablo");
        user.setEmail("pablo@us.es"); //No contiene @ ni es correcto
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(2020, 11, 23));
        user.setProvince("Sevilla");
        user.setCity("Sevilla");
        user.setPostCode("41012");
        user.setUsername("pablo123");
        user.setPassword(new BCryptPasswordEncoder().encode("pablo123"));
        user.setEnabled(true);
		this.userRepository.save(user);
		this.buildAuthoritiesForTests(user.getUsername());

		user = new User();

		user.setId("userTest-juan1234");
        user.setName("Juan");
        user.setEmail("juan@us.es"); //No contiene @ ni es correcto
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(2020, 11, 23));
        user.setProvince("Sevilla");
        user.setCity("Sevilla");
        user.setPostCode("41012");
        user.setUsername("juan1234");
        user.setPassword(new BCryptPasswordEncoder().encode("juan1234"));
        user.setEnabled(true);
		this.userRepository.save(user);
		this.buildAuthoritiesForTests(user.getUsername());
	}

	@PostConstruct
	public void buildBookIndex() {
		this.deleteIndex();
		this.buildUserIndexForTests();
		this.buildBookIndexForTests();
		elasticSearchOperations.indexOps(Book.class).refresh();
		List<Book> books = prepareDataset();
		bookRepository.saveAll(books);
		log.info("================= Added " + books.size() + " books. =================");
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
			int numRandomUsername = (int) Math.floor(Math.random()*2);
			String username = "pablo123";
			if(numRandomUsername == 1) {
				username = "juan1234";
			}
			Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genre, author, description, urlImage, status, action, price, username);
			return book;
		} else {
			return null;
		}
	}

}
