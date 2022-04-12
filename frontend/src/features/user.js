import { createSlice } from "@reduxjs/toolkit";

const initialStateValue = {
    accessToken: "",
    expiresIn: 0,
    email: "",
    commonName: "",
    role: "",
    organization: ""
};

export const userSlice = createSlice({
    name: "user",
    initialState: { value: localStorage.getItem("user") == null ? initialStateValue : JSON.parse(localStorage.getItem("user")) },

    reducers: {
        login: (state, action) => {
            state.value = action.payload;
            localStorage.setItem("user", JSON.stringify(action.payload));
        },

        logout: (state) => {
            state.value = initialStateValue;
            localStorage.setItem("user", JSON.stringify(initialStateValue));
        },
    },
});

// const user = useSelector((state) => state.user.value);

export const { login, logout } = userSlice.actions;

export default userSlice.reducer;