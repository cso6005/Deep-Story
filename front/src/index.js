import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import SendText from './SendText';
import LoginComponent from './components/Account/LoginComponent';
import SignComponets from './components/Account/SignComponents';
import LogoutComponent from './components/Account/LogoutComponet';
import PostCreateComponent from './components/Board/PostCreateComponent';
import Detail from './components/Board/Detail';
import PostAll from './components/Board/PostAll';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Route, Routes } from "react-router-dom";

import store from './store';
import { Provider } from 'react-redux';
import { CookiesProvider } from 'react-cookie';

//axios.defaults.withCredentials = true;


const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(

  <CookiesProvider>
    <Provider store={store}>

        <BrowserRouter>
        <Routes>
          <Route path='/' element={<App />} />
          <Route path="/login" exact element={<LoginComponent />} />
          <Route path="/sign" exact element={<SignComponets />} />
          <Route path="/PostCreate" exact element={<PostCreateComponent />} />
          <Route path="/SendText" element={<SendText />} /> 
          <Route path="/Logout" element={<LogoutComponent />} /> 
          <Route path='/Detail' element={<Detail />} />
          <Route path='/PostAll' element={<PostAll/>}/>
				</Routes>
      
      </BrowserRouter>

    </Provider>
  </CookiesProvider>


);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
