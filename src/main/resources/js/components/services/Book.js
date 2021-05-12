const baseUrl = "http://localhost:8080/books";
import axios from "axios";
import FormData from "form-data";
const book = {};

book.create = async (state) => {
  const datapost = {
    title: state.fieldTitle,
    originalTitle: state.fieldOriginalTitle,
    isbn: state.fieldIsbn,
    publicationYear: state.fieldPublicationYear,
    publisher: state.fieldPublisher,
    genres: state.fieldGenres,
    author: state.fieldAuthor,
    description: state.fieldDescription,
    status: state.fieldStatus,
    price: state.fieldPrice,
    image: state.hasImages,
  };

  const urlPost = baseUrl + "/new";
  const res = await axios
    .post(urlPost, datapost)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  if (res.success == true) {
    const idBook = res.idBook;

    for (let i = 0; i < state.fieldImage.length; i++) {
      var form = new FormData();
      form.append("image", state.fieldImage[i]);

      const urlPost1 =
        "https://api.imgbb.com/1/upload?key=672fcb9727d542f1303d834e6b2a5caa";
      const resUploadImage = await axios
        .post(urlPost1, form)
        .then((response) => {
          return response.data;
        })
        .catch((error) => {
          return error.response;
        });

      if (resUploadImage.success == true) {
        const datapostImage = {
          idBook: idBook,
          fileName: resUploadImage.data.image.filename,
          urlImage: resUploadImage.data.url,
        };

        const urlPostImage = baseUrl + "/images/upload";
        const postUploadImage = await axios
          .post(urlPostImage, datapostImage)
          .then((response) => {
            return response.data;
          })
          .catch((error) => {
            return error.response;
          });
      }
    }
  }
  return res;
};

book.edit = async (state) => {
  const datapost = {
    id: state.id,
    title: state.fieldTitle,
    originalTitle: state.fieldOriginalTitle,
    isbn: state.fieldIsbn,
    publicationYear: state.fieldPublicationYear,
    publisher: state.fieldPublisher,
    genres: state.fieldGenres,
    author: state.fieldAuthor,
    description: state.fieldDescription,
    image: state.image,
    status: state.fieldStatus,
    price: state.fieldPrice,
  };

  const urlPost = baseUrl + "/" + state.id + "/edit";
  const res = await axios
    .put(urlPost, datapost)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  if (res.success == true) {
    const idBook = state.id;

    for (let i = 0; i < state.fieldImage.length; i++) {
      var form = new FormData();
      form.append("image", state.fieldImage[i]);

      const urlPost1 =
        "https://api.imgbb.com/1/upload?key=672fcb9727d542f1303d834e6b2a5caa";
      const resUploadImage = await axios
        .post(urlPost1, form)
        .then((response) => {
          return response.data;
        })
        .catch((error) => {
          return error.response;
        });

      if (resUploadImage.success == true) {
        const datapostImage = {
          idBook: idBook,
          fileName: resUploadImage.data.image.filename,
          urlImage: resUploadImage.data.url,
        };

        const urlPostImage = baseUrl + "/images/upload";
        const postUploadImage = await axios
          .post(urlPostImage, datapostImage)
          .then((response) => {
            return response.data;
          })
          .catch((error) => {
            return error.response;
          });
      }
    }
  }

  return res;
};

book.deleteImage = async (image) => {
  const urlDeleteEntityImage = baseUrl + "/images/" + image.id + "/delete";
  const deleteImageEntity = await axios
    .get(urlDeleteEntityImage)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });
};

book.getBook = async (id) => {
  const urlGet = baseUrl + "/get/" + id;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

book.getBookToEdit = async (id) => {
  const urlGet = baseUrl + "/edit/" + id;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

book.listAllExceptMine = async (page, showMode) => {
  const urlGet =
    baseUrl + "/list/all-me?page=" + page + "&showMode=" + showMode;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

book.listMyBooks = async (page) => {
  const urlGet = baseUrl + "/list/me?page=" + page;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

book.listMyBooksForChange = async () => {
  const urlGet = baseUrl + "/list/me-change";
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

book.getGenres = async () => {
  const urlGet = baseUrl + "/genres";
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });
  return res;
};

book.delete = async (id, images) => {
  const urlDelete = baseUrl + "/" + id + "/delete";
  const res = await axios
    .delete(urlDelete)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  for (let i = 0; i < images.length; i++) {
    const urlDeleteImage = images[i].urlDelete;
    const resDeleteImage = await axios
      .get(urlDeleteImage)
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        return error.response;
      });
  }

  return res;
};

book.recommendBooks = async (page) => {
  const urlGet = baseUrl + "/recommend?page=" + page;
  const res = await axios
    .get(urlGet)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      return error.response;
    });

  return res;
};

export default book;
