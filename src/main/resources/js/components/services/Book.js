const baseUrl = "http://localhost:8080/books"
import axios from "axios";
import FormData from 'form-data'
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
        status: state.fieldStatus,
        price: state.fieldPrice
    }

    const urlPost = baseUrl+"/new"
    const res = await axios.post(urlPost, datapost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    if(res.success == true) {
        const idBook = res.idBook
        let data = new FormData();

        let files = []

        files.push(state.fieldImage[0])
        files.push(state.fieldImage[1])
        alert(JSON.stringify(state.fieldImage))
        alert(JSON.stringify(files))
        data.append('files', state.fieldImage[0]);
        data.append('idBook', idBook);

        const urlPost1 = baseUrl+"/new/image"
        const res1 = await axios.post(urlPost1, data)
        .then(response => {return response.data;})
        .catch(error => {return error.response;})
    }
    
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
        status: state.fieldStatus,
        price: state.fieldPrice
    }

    const urlPost = baseUrl+"/"+state.id+"/edit"
    const res = await axios.put(urlPost, datapost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.getBook = async(id) => {
    const urlGet = baseUrl+"/get/"+id
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    
    return res;
}

book.getBookToEdit = async(id) => {
    const urlGet = baseUrl+"/edit/"+id
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})
    
    return res;
}

book.listAllExceptMine = async(page, showMode) => {
    const urlGet = baseUrl+"/list/all-me?page="+page+"&showMode="+showMode
    const res = await axios.get(urlGet)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.listMyBooks = async(page) => {
    const urlGet = baseUrl+"/list/me?page="+page
    const res = await axios.get(urlGet)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.listMyBooksForChange = async() => {
    const urlGet = baseUrl+"/list/me-change"
    const res = await axios.get(urlGet)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

book.getGenres = async() => {
    const urlGet = baseUrl+"/genres"
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})
    return res;
}

book.delete = async(id) => {
    const urlDelete = baseUrl+"/"+id+"/delete"
    const res = await axios.delete(urlDelete)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

book.recommendBooks = async(page) => {
    const urlGet = baseUrl+"/recommend?page="+page
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}
   
export default book