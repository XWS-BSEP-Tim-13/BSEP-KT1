import { createSlice } from "@reduxjs/toolkit";

const initialStateValue = {};

export const templateSlice = createSlice({
  name: "template",
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

export const { login, logout } = templateSlice.actions;

export default templateSlice.reducer;