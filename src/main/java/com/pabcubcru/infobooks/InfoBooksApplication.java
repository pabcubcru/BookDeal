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
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.ProvinceEnum;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.models.UserFavouriteBook;
import com.pabcubcru.infobooks.repository.AuthoritiesRepository;
import com.pabcubcru.infobooks.repository.BookRepository;
import com.pabcubcru.infobooks.repository.ImageRepository;
import com.pabcubcru.infobooks.repository.RequestRepository;
import com.pabcubcru.infobooks.repository.SearchRepository;
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

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private SearchRepository searchRepository;

	public static void main(String[] args) {
		SpringApplication.run(InfoBooksApplication.class, args);
	}

	public void deleteIndex() {
		requestRepository.deleteAll();
		userFavouriteBookRepository.deleteAll();
		authoritiesRepository.deleteAll();
		bookRepository.deleteAll();
		userRepository.deleteAll();
		imageRepository.deleteAll();
		searchRepository.deleteAll();
	}

	public void buildUserFavouriteBookIndexForTests() {
		UserFavouriteBook ufb = new UserFavouriteBook();

		ufb.setBookId("book-001");
		ufb.setId("ufb-001");
		ufb.setUsername("pablo123");
		this.userFavouriteBookRepository.save(ufb);

		ufb = new UserFavouriteBook();

		ufb.setBookId("book-002");
		ufb.setId("ufb-002");
		ufb.setUsername("pablo123");
		this.userFavouriteBookRepository.save(ufb);

		ufb = new UserFavouriteBook();

		ufb.setBookId("book-001");
		ufb.setId("ufb-003");
		ufb.setUsername("juan1234");
		this.userFavouriteBookRepository.save(ufb);

		ufb = new UserFavouriteBook();

		ufb.setBookId("booktest");
		ufb.setId("ufb-004");
		ufb.setUsername("test001");
		this.userFavouriteBookRepository.save(ufb);
	}

	public void buildRequestsIndexForTests() {
		Request request = new Request();

		request.setId("request-001");
		request.setAction("VENTA");
		request.setComment("comment");
		request.setIdBook2("book-002");
		request.setUsername1("test001");
		request.setUsername2("test002");
		request.setStatus("PENDIENTE");
		request.setPay(10.);
		this.requestRepository.save(request);

		request = new Request();

		request.setId("request-002");
		request.setAction("INTERCAMBIO");
		request.setComment("comment");
		request.setIdBook1("book-001");
		request.setIdBook2("book-002");
		request.setUsername1("test001");
		request.setUsername2("test002");
		request.setStatus("ACEPTADA");
		this.requestRepository.save(request);

		request = new Request();

		request.setId("request-003");
		this.requestRepository.save(request);

		request = new Request();

		request.setId("request-004");
		this.requestRepository.save(request);

		request = new Request();

		request.setId("request-005");
		this.requestRepository.save(request);
	}

	public void buildBookIndexImages(String idBook, String urlImage, Integer id) {
		Image image = new Image();
		image.setId("image-" + id + "-" + idBook);
		image.setUrlImage(urlImage);
		image.setIdBook(idBook);
		image.setFileName("image-" + id + "-" + idBook);
		image.setPrincipal(true);

		this.imageRepository.save(image);
	}

	public void buildBookIndexForTests() {
		Book book = new Book();

		book.setId("book-001");
		book.setTitle("Title test 1");
		book.setIsbn("0-7645-2641-3");
		book.setPublicationYear(2014);
		book.setPublisher("Publisher Test");
		book.setGenres("Religión,Gastronomía,Cocina");
		book.setAuthor("Author Test");
		book.setDescription("Description test");
		book.setStatus("COMO NUEVO");
		book.setPrice(10.);
		book.setUsername("test001");
		this.buildBookIndexImages(book.getId(), "https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg", 1);
		this.bookRepository.save(book);

		book.setId("book-002");
		book.setTitle("Title test 2");
		book.setStatus("COMO NUEVO");
		book.setUsername("test002");
		this.buildBookIndexImages(book.getId(), "https://images-na.ssl-images-amazon.com/images/I/81sBQfVzziL.jpg", 2);
		this.bookRepository.save(book);

		book.setId("book-003");
		book.setTitle("Title test 3");
		book.setStatus("COMO NUEVO");
		book.setUsername("juan1234");
		this.buildBookIndexImages(book.getId(), "https://images-na.ssl-images-amazon.com/images/I/81sBQfVzziL.jpg", 2);
		this.bookRepository.save(book);
	}

	public void buildAuthoritiesForTests(String username) {
		Authorities authorities = new Authorities();
		authorities.setId("authorities-" + username);
		authorities.setUsername(username);
		authorities.setAuthority("user");
		this.authoritiesRepository.save(authorities);
	}

	private void createUser(String username) {
		User user = new User();

		user.setId("userTest-"+username);
		user.setName(username);
		user.setEmail(username+"@us.es");
		user.setPhone("+34654987321");
		user.setBirthDate(LocalDate.of(1998, 11, 23));
		user.setProvince("Sevilla");
		user.setPostCode("41012");
		user.setUsername(username);
		user.setGenres("Religión,Gastronomía,Cocina");
		user.setPassword(new BCryptPasswordEncoder().encode(username));
		user.setEnabled(true);
		this.userRepository.save(user);
		this.buildAuthoritiesForTests(user.getUsername());
	}

	public void buildUserIndexForTests() {
		this.createUser("pablo123");

		this.createUser("juan1234");

		this.createUser("test001");

		this.createUser("test002");
	}

	public void buildIndexUsersForBooks() {
		ProvinceEnum[] provinces = ProvinceEnum.values();
		for (int i = 1; i <= 100; i++) {
			User user = new User();

			user.setId("user" + i);
			user.setName("User " + i);
			user.setEmail("user" + i + "@us.es");
			user.setPhone("+34654987321");
			user.setBirthDate(LocalDate.of(1997, 11, 23));
			int numRandomProvince = (int) Math.floor(Math.random() * ProvinceEnum.values().length) - 1;
			numRandomProvince = numRandomProvince < 0 ? 0 : numRandomProvince;
			user.setProvince(provinces[numRandomProvince].toString());
			user.setPostCode("" + (int) Math.floor(Math.random() * (50000 - 1000 + 1) + 1000));
			user.setUsername("username" + i);
			user.setPassword(new BCryptPasswordEncoder().encode("password" + i));
			user.setEnabled(true);
			GenreEnum[] genres = GenreEnum.values();
			int numRandomGenre1 = (int) Math.floor(Math.random() * (GenreEnum.values().length / 3));
			int numRandomGenre2 = (int) Math.floor(Math.random() * (GenreEnum.values().length * 2 / 3));
			int numRandomGenre3 = (int) Math.floor(Math.random() * (GenreEnum.values().length / 3))
					+ (GenreEnum.values().length * 2 / 3);
			String genre = genres[numRandomGenre1].toString() + "," + genres[numRandomGenre2].toString() + ","
					+ genres[numRandomGenre3].toString();
			user.setGenres(genre);
			this.userRepository.save(user);
			this.buildAuthoritiesForTests(user.getUsername());
		}
		log.info("================= Added 100 users. =================");
	}

	public void buildImagesForBook(List<Book> books) {
		for (int i = 0; i < books.size(); i++) {
			Book b = books.get(i);
			this.buildBookIndexImages(b.getId(), b.getImage(), i);
		}
	}

	@PostConstruct
	public void buildBookIndex() {
		this.deleteIndex();
		/*this.buildIndexUsersForBooks();
		elasticSearchOperations.indexOps(Book.class).refresh();
		List<Book> books = prepareDataset();
		this.bookRepository.saveAll(books);
		this.buildImagesForBook(books);
		log.info("================= Added " + books.size() + " books. =================");*/
		this.buildUserIndexForTests();
		this.buildBookIndexForTests();
		this.buildRequestsIndexForTests();
		this.buildUserFavouriteBookIndexForTests();
	}

	private List<Book> prepareDataset() {
		List<Book> res = new ArrayList<>();

		try {
			File myObj = new File(".\\src\\main\\java\\com\\pabcubcru\\infobooks\\books.csv");
			Scanner scanner = new Scanner(myObj);
			int lineNo = 0;
			while (scanner.hasNextLine()) {
				++lineNo;
				String line = scanner.nextLine();
				if (lineNo == 1)
					continue;
				Book book = csvRowToBookMapper(line);
				if (book != null) {
					res.add(book);
				}
			}
			scanner.close();
		} catch (Exception e) {
			log.error("File read error {}", e.getMessage());
		}

		return res;
	}

	private Book csvRowToBookMapper(final String line) {
		if (!line.equals("") || !line.equals(null)) {
			String[] s = line.split(";");
			GenreEnum[] genres = GenreEnum.values();
			String isbn = s[0];
			String originalTitle = s[1];
			if(originalTitle.length() > 80 ) {
				originalTitle = originalTitle.substring(0, 80);
			}
			String title = s[2];
			if(title.length() > 80 ) {
				title = title.substring(0, 80);
			}
			String author = s[3];
			if(author.length() > 80 ) {
				author = author.substring(0, 80);
			}
			Integer publicationYear = 2010;
			if (!s[4].equals("")) {
				String r = s[4].replace(".0", "");
				publicationYear = Integer.parseInt(r);
			}
			String publisher = s[5];
			if(publisher.length() > 80 ) {
				publisher = publisher.substring(0, 80);
			}
			String description = s[6];
			if(description.length() > 1750 ) {
				description = description.substring(0, 1750);
			}
			String urlImage = s[7];
			int numRandomGenre1 = (int) Math.floor(Math.random() * GenreEnum.values().length) - 1;
			numRandomGenre1 = numRandomGenre1 < 0 ? 0 : numRandomGenre1;
			int numRandomGenre2 = (int) Math.floor(Math.random() * GenreEnum.values().length) - 1;
			numRandomGenre2 = numRandomGenre2 < 0 ? 0 : numRandomGenre2;
			String genre = genres[numRandomGenre1].toString();
			if (numRandomGenre1 != numRandomGenre2) {
				genre = genres[numRandomGenre1].toString() + "," + genres[numRandomGenre2].toString();
			}
			String status = "NUEVO";
			Double price = Double.parseDouble(String.valueOf(Math.floor(Math.random() * 100)));
			int numRandomUsername = (int) Math.floor(Math.random() * 100);
			String username = "username" + numRandomUsername;
			Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genre, author, description,
					urlImage, status, price, username);
			return book;
		} else {
			return null;
		}
	}

}
