import axios from 'axios';

export function jwtInterceptor() {
    axios.interceptors.request.use(request => {
        request.headers.common.Authorization = `Bearer ${''}`;
        return request;
    });
}