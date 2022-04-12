import { createSlice } from "@reduxjs/toolkit";

const initialStateValue = {
    accessToken: "",
    expiresIn: 0,
    role: ""
};

export const userSlice = createSlice({
    name: "user",
    initialState: { value: initialStateValue },

    reducers: {
        login: (state, action) => {
            state.value = action.payload;
        },

        logout: (state) => {
            state.value = initialStateValue;
        },
    },
});

// const user = useSelector((state) => state.user.value);

export const { login, logout } = userSlice.actions;

export default userSlice.reducer;