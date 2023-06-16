import { createSlice } from '@reduxjs/toolkit';
import Cookies from 'js-cookie';

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    token: Cookies.get('token') || null,
  },
  reducers: {
    setAuthToken: (state, action) => {
      state.token = action.payload;
    },
    resetToken: (state) => {
        state.token = null;
    },
  },
});

export const { setAuthToken, resetToken } = authSlice.actions;
export default authSlice.reducer;