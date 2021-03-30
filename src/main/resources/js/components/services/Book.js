const baseUrl = "http://localhost:8080/books"
import axios from "axios";
const book = {}

book.create = async(state) => {

    const datapost = {
        title: state.fieldTitle,
        originalTitle: state.fieldOriginalTitle,
        isbn: state.fieldIsbn,
        publicationYear: state.fieldPublicationYear,
        publisher: state.fieldPublisher,
        genres: state.fieldGenres,
        author: state.fieldAuthor,
        description: state.fieldDescription,
        image: state.fieldImage,
        action: state.fieldAction,
        price: state.fieldPrice
    }

    const urlPost = baseUrl+"/new"
    const res = await axios.post(urlPost, datapost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.edit = async(state) => {

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
        image: state.fieldImage,
        action: state.fieldAction,
        price: state.fieldPrice
    }

    const urlPost = baseUrl+"/"+state.id+"/edit"
    const res = await axios.put(urlPost, datapost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.listAll = async() => {
    const urlGet = baseUrl+"/list/all"
    const res = await axios.get(urlGet)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.listMyBooks = async() => {
    const urlGet = baseUrl+"/list/me"
    const res = await axios.get(urlGet)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.getGenres = async() => {
    const urlGet = baseUrl+"/genres"
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error})
    return res;
}
   

export default book