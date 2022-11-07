
import React, { Component }  from 'react';
import LogoutHeader from './components/Header/LogoutHeader';
import SidebarLogoutComponent from './components/Sidebar/SidebarLogoutComponent';
import { setCookie, getCookie, deleteCookie } from './storage/Cookie';

// import { connect } from 'react-redux';
// import {bindActionCreators} from 'redux';
// import * as userActions from 'redux/modules/user';


 function App(props) {

//  class App extends Component {

  const loggedInfo = getCookie('islogin');

  // const isLoggedIn = props.isLoggedIn;

//   initializeUserInfo = async () => {
//   
//   if(!loggedInfo) return; // 로그인 정보가 없다면 여기서 멈춥니다.

//   const { UserActions } = this.props;
//   UserActions.setLoggedInfo(loggedInfo);
//   try {
//       await UserActions.checkStatus();
//   } catch (e) {
//       deleteCookie('islogin')
//       // window.location.href = '/auth/login?expired';
//   }
// }


//   componentDidMount() {
//     this.initializeUserInfo();
// }




  // render() {
	return (

		<div className='App'>

        <LogoutHeader></LogoutHeader> 

        <SidebarLogoutComponent></SidebarLogoutComponent>

		</div>
	);
 // }
}

export default App;



