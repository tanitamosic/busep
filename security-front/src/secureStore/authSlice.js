import { createSlice } from '@reduxjs/toolkit';
import Cookies from 'js-cookie';

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    token: Cookies.get('token') || null,
    cookie: Cookies.get('cookie') || null,
    wsLogs: null,
  },
  reducers: {
    setAuthToken: (state, action) => {
      state.token = action.payload;
    },
    resetToken: (state) => {
        state.token = null;
    },
    setAuthCookie: (state, action) => {
        state.cookie = action.payload;
      },
    resetCookie: (state) => {
        state.cookie = null;
    },
    setWsLogs: (state, action) => {
        state.wsLogs = action.payload;
    },
    resetWsLogs: (state) => {
        state.wsLogs = null;
    },
  },
});

export const { setAuthToken, resetToken, setAuthCookie, resetCookie, setWsLogs, resetWsLogs  } = authSlice.actions;
export default authSlice.reducer;