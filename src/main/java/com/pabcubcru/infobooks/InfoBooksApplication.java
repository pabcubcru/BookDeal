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
import com.pabcubcru.infobooks.models.ProvinceEnum;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.models.UserFavouriteBook;
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

	public void buildBookIndexForTests() {
		Book book = new Book();

		book.setId("book-001");
        book.setTitle("Title test 1");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Autoayuda");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg");
        book.setStatus("COMO NUEVO"); 
		book.setPrice(10.);
        book.setUsername("test001");
		this.bookRepository.save(book);

		book.setId("book-002");
        book.setTitle("Title test 2");
        book.setImage("https://images-na.ssl-images-amazon.com/images/I/81sBQfVzziL.jpg");
        book.setStatus("COMO NUEVO"); 
		book.setPrice(null);
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
        user.setEmail("pablo@us.es"); 
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(1998, 11, 23));
        user.setProvince("Sevilla");
        user.setPostCode("41012");
        user.setUsername("pablo123");
		user.setGenres("Autoayuda,Esoterismo,Ciencia");
        user.setPassword(new BCryptPasswordEncoder().encode("pablo123"));
        user.setEnabled(true);
		this.userRepository.save(user);
		this.buildAuthoritiesForTests(user.getUsername());

		user = new User();

		user.setId("userTest-juan1234");
        user.setName("Juan");
        user.setEmail("juan@us.es"); 
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(1998, 11, 23));
        user.setProvince("Sevilla");
        user.setPostCode("41012");
        user.setUsername("juan1234");
		user.setGenres("Religión,Gastronomía,Cocina");
        user.setPassword(new BCryptPasswordEncoder().encode("juan1234"));
        user.setEnabled(true);
		this.userRepository.save(user);
		this.buildAuthoritiesForTests(user.getUsername());
	}

	public void buildIndexUsersForBooks() {
		ProvinceEnum[] provinces = ProvinceEnum.values();
		for(int i=1; i <= 100; i++) {
			User user = new User();

			user.setId("user"+i);
			user.setName("User "+i);
			user.setEmail("user"+i+"@us.es"); 
			user.setPhone("+34654987321");
			user.setBirthDate(LocalDate.of(1997, 11, 23));
			int numRandomProvince = (int) Math.floor(Math.random()*ProvinceEnum.values().length)-1;
			numRandomProvince = numRandomProvince < 0 ? 0 : numRandomProvince;
			user.setProvince(provinces[numRandomProvince].toString());
			user.setPostCode(""+(int)Math.floor(Math.random()*(50000-1000+1)+1000));
			user.setUsername("username"+i);
			user.setPassword(new BCryptPasswordEncoder().encode("password"+i));
			user.setEnabled(true);
			GenreEnum[] genres = GenreEnum.values();
			int numRandomGenre1 = (int) Math.floor(Math.random()*(GenreEnum.values().length/3));
			int numRandomGenre2 = (int) Math.floor(Math.random()*(GenreEnum.values().length*2/3));
			int numRandomGenre3 = (int) Math.floor(Math.random()*(GenreEnum.values().length/3)) + (GenreEnum.values().length*2/3);
			String genre = genres[numRandomGenre1].toString() + "," + genres[numRandomGenre2].toString() + "," + genres[numRandomGenre3].toString();
			user.setGenres(genre);
			this.userRepository.save(user);
			this.buildAuthoritiesForTests(user.getUsername());
		}
		log.info("================= Added 100 users. =================");
	}

	@PostConstruct
	public void buildBookIndex() {
		this.deleteIndex();
		this.buildIndexUsersForBooks();
		elasticSearchOperations.indexOps(Book.class).refresh();
		List<Book> books = prepareDataset();
		bookRepository.saveAll(books);
		log.info("================= Added " + books.size() + " books. =================");
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
		if(!line.equals("") || !line.equals(null)) {
			String[] s = line.split(";");
			GenreEnum[] genres = GenreEnum.values();
			String isbn = s[0];
			String originalTitle = s[1];
			String title = s[2];
			String author = s[3];
			Integer publicationYear = 2010;
			if(!s[4].equals("")) {
				String r = s[4].replace(".0", "");
				publicationYear = Integer.parseInt(r);
			}
			String publisher = s[5];
			String description = s[6];
			String urlImage = s[7];
			int numRandomGenre1 = (int) Math.floor(Math.random()*GenreEnum.values().length)-1;
			numRandomGenre1 = numRandomGenre1 < 0 ? 0 : numRandomGenre1;
			int numRandomGenre2 = (int) Math.floor(Math.random()*GenreEnum.values().length)-1;
			numRandomGenre2 = numRandomGenre2 < 0 ? 0 : numRandomGenre2;
			String genre = genres[numRandomGenre1].toString();
			if(numRandomGenre1 != numRandomGenre2) {
				genre = genres[numRandomGenre1].toString() + "," + genres[numRandomGenre2].toString();
			}
			String status = "NUEVO";
			Double price = Double.parseDouble(String.valueOf(Math.floor(Math.random()*100)));
			int numRandomUsername = (int) Math.floor(Math.random()*100);
			String username = "username"+numRandomUsername;
			Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genre, author, description, urlImage, status, price, username);
			return book;
		} else {
			return null;
		}
	}

}
