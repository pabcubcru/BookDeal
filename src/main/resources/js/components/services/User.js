const baseUrl = "http://localhost:8080"
import axios from "axios";
const user = {}

user.create = async(state) => {

    const datapost = {
        name: state.fieldName,
        email: state.fieldEmail,
        phone: state.fieldPhone,
        birthDate: state.fieldBirthDate,
        username: state.fieldUsername,
        password: state.fieldPassword,
    }

    const urlPost = baseUrl+"/register"
    const res = await axios.post(urlPost, datapost)
    .then(response => {return response.data;})
    .catch(error => {return error.response;})

    return res;
}

user.getPrincipal = async() => {
    const urlGetPrincipal = baseUrl+"/principal"
    const res = await axios.get(urlGetPrincipal)
    .then(response => {return response.data})
    .catch(error => {return error})

    return res;
}

export default user