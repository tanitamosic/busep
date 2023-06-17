import { createSlice } from '@reduxjs/toolkit';
import Cookies from 'js-cookie';
import { useSelector, useDispatch } from 'react-redux';

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
        console.log("new log ws: " + action.payload)
        state.wsLogs = action.payload;
    },
    resetWsLogs: (state) => {
        state.wsLogs = null;
    },
  },
});

export const { setAuthToken, resetToken, setAuthCookie, resetCookie, setWsLogs, resetWsLogs  } = authSlice.actions;
export default authSlice.reducer;

export const useWsLogs = () => {
  const wsLogs = useSelector((state) => state.auth.wsLogs);
  const dispatch = useDispatch();

  const updateWsLogs = (newLogs) => {
    dispatch(setWsLogs(newLogs));
  };

  return { wsLogs, updateWsLogs };
};