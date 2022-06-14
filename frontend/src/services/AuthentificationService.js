import axios from 'axios';

const AuthentificationService = {

    login: function(authData) {
        return axios.post(`http://localhost:8081/auth/login`, authData, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })

    },

    forgotPassword: function(email) {
        return axios.get(`http://localhost:8081/auth/forgot-password/mail/`+email, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })

    },

    changePassword: function(data) {
        return axios.put(`http://localhost:8081/auth/change-password`,data, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })

    },





}
export default AuthentificationService;