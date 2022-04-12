import axios from 'axios';

const CertificationService = {

    downloadCertificate: function(id) {
        return axios.get(`http://localhost:8081/certificate/download/` + id, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })

    },
    findCertificatesByType: function(type) {
        return axios.get(`http://localhost:8081/certificate/findAllByType/` + type, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })
    },

    findCertificateById: function(id) {
        return axios.get(`http://localhost:8081/certificate/findById/` + id, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })
    },

    findCertificateHierarchy: function(id) {
        return axios.get(`http://localhost:8081/certificate/hierarchy-above/` + id, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
            },
        })
    }
}
export default CertificationService;