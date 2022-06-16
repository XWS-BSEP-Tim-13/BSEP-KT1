import { axiosInstance } from "./AxiosInstance";

export async function requestCode(codeRequest) {
    const response = await axiosInstance.post('auth/generate-passwordless', codeRequest);
    return response;
}

export async function passwordlessLogin(loginRequest) {
    const response = await axiosInstance.post('auth/passwordless-login', loginRequest);
    return response;
}