const baseUrl = "http://localhost:8080/requests"
import axios from "axios";
const request = {}

request.create = async(state) => {

    const dataPost = {
        username1:"",
        username2:"",
        idBook1: state.fieldIdBook1,
        idBook2:"",
        status:"",
        action:"",
        comment: state.fieldComment
    }

    const urlPost = baseUrl+"/"+state.fieldIdBook2+"/new"
    const res = await axios.post(urlPost, dataPost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

request.listMyRequests = async() => {

    const urlGet = baseUrl+"/my-requests"
    const res = await axios.get(urlGet)
    .then(response => {return response.data})
    .catch(error => {return error.response})

    return res;
}

export default request