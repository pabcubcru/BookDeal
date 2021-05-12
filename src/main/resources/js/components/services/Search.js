const baseUrl = "http://localhost:8080/search"
import axios from "axios";
const search = {}

search.searchBook = async (query, page) => {
    const urlGet = baseUrl+"/q/"+query+"?page="+page
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

search.searchTitles = async (query) => {
    const urlGet = baseUrl+"/titles/"+query
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

search.postSearch = async (state) => {

    const dataPost = {
        startYear: state.fieldStartYear,
        finishYear: state.fieldFinishYear,
        postalCode: state.fieldPostalCode,
        type: state.selectSearch,
        text: state.fieldText
    }

    const res = await axios.post(baseUrl, dataPost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

export default search