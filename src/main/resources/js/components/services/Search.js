const baseUrl = "http://localhost:8080/search"
import axios from "axios";
const search = {}

search.searchBook = async (query) => {
    const urlGet = baseUrl+"/q/"+query
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

export default search