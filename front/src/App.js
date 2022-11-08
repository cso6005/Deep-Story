
import React, { Component }  from 'react';
import LogoutHeader from './components/Header/LogoutHeader';
import SidebarLogoutComponent from './components/Sidebar/SidebarLogoutComponent';
import { getCookie } from './storage/Cookie';
import MainPageLogoutComponent from './components/Sub/MainPageLogoutComponent';
import './App.css';


 function App(props) {

  const loggedInfo = getCookie('islogin');

 
	return (

		<div className='App'>

        <LogoutHeader></LogoutHeader> 

        <SidebarLogoutComponent></SidebarLogoutComponent>

				<MainPageLogoutComponent/>

		</div>
	);

}

export default App;



