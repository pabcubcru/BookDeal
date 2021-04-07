const baseUrl = "http://localhost:8080/favourites"
import axios from "axios";
const userFavouriteBook = {}

userFavouriteBook.findAllFavouritesBooks = async() => {
    const urlGet = baseUrl+"/all"
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

userFavouriteBook.addFavouriteBook = async(id) => {
    const urlGet = baseUrl+"/"+id+"/add"
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

userFavouriteBook.deleteFavouriteBook = async(id) => {
    const urlDelete = baseUrl+"/"+id+"/delete"
    const res = await axios.delete(urlDelete)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

export default userFavouriteBook