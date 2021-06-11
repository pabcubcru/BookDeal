package com.pabcubcru.bookdeal.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Image;
import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.repository.BookRepository;
import com.pabcubcru.bookdeal.repository.ImageRepository;
import com.pabcubcru.bookdeal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private BookRepository bookRepository;

    private UserRepository userRepository;

    private ImageRepository imageRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void save(Book book) {
        this.bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return this.bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findAllExceptMine(String username, Pageable pageable) {
        return this.bookRepository.findByUsernameNot(username, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findMyBooks(String username, Pageable pageable) {
        return this.bookRepository.findByUsername(username, pageable);
    }

    @Transactional
    public Book findBookById(String id) {
        return this.bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBookById(String id) {
        this.bookRepository.deleteById(id);
    }

    @Transactional
    public List<Book> findByIds(List<String> bookIds) {
        List<Book> res = new ArrayList<>();
        this.bookRepository.findAllById(bookIds).iterator().forEachRemaining(res::add);

        return res;
    }

    @Transactional
    public List<Book> findByUsername(String username) {
        return this.bookRepository.findByUsername(username);
    }

    @Transactional
    public Page<Book> findNearBooks(User user, Pageable pageable, String showMode) {


        List<User> usersWithSameAddress = null;
        List<String> usernames = new ArrayList<>();

        if (showMode.equals("postCode")) {
            usersWithSameAddress = this.userRepository.findByPostCode(user.getPostCode());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername()))
                    .forEach(x -> usernames.add(x.getUsername()));
        } else if (showMode.equals("province")) {
            usersWithSameAddress = this.userRepository.findByProvince(user.getProvince());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername()))
                    .forEach(x -> usernames.add(x.getUsername()));
        } else if (showMode.equals("genres")) {
            return this.bookRepository.findByGenresLike(user.getGenres(), pageable);
        } else {
            usersWithSameAddress = this.userRepository.findByPostCodeOrProvince(user.getPostCode(), user.getProvince());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername()))
                    .forEach(x -> usernames.add(x.getUsername()));
        }

        Page<Book> books = this.bookRepository.findByUsernameIn(usernames, pageable);

        return books;
    }

    @Transactional
    public List<Image> findImagesByIdBook(String idBook) {
        return this.imageRepository.findByIdBookOrderByPrincipalDesc(idBook);
    }

    @Transactional
    public Image findByIdBookAndPrincipalTrue(String idBook) {
        return this.imageRepository.findByIdBookAndPrincipalTrue(idBook);
    }

    @Transactional
    public void saveImage(Image image) {
        this.imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Image image) {
        this.imageRepository.delete(image);
    }

    @Transactional
    public void deleteAllImages(List<Image> images) {
        this.imageRepository.deleteAll(images);
    }

    @Transactional
    public void deleteImageById(String id) {
        this.imageRepository.deleteById(id);
    }

    @Transactional
    public Image findImageById(String id) {
        return this.imageRepository.findById(id).orElse(null);
    }

}
