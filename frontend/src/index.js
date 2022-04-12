import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import { configureStore } from '@reduxjs/toolkit';
import { Provider } from 'react-redux';

import userReducer from './features/user';

import './index.css';

const container = document.getElementById('root');
const root = createRoot(container);

const store = configureStore({
    reducer: {
        user: userReducer,
    },
});

root.render(
    <BrowserRouter>
        <Provider store={store}>
            <App tab="home" />
        </Provider>
    </BrowserRouter>
);
