/* eslint-disable jsx-a11y/alt-text */
import React, { useEffect, useState} from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import SidebarAll from '../Sidebar/SidebarAllComponent';
import LoginHeader from '../Header/LoginHeader';
import ModalView from '../Friendrequest/ModalView';
import './Detail.css';

function Detail(){
  const location = useLocation();
  const [title, setTitle] = useState("");
  const [contents, setContents] = useState("");
  const [image, setImage] = useState("");
  const [postId, setPostId] = useState(location.state.postId);
  const [accountId, setaccountId] = useState("");
  const [likes, addLikes] = useState(0);
  //사용자 정보 
  const [userEmail, setUserEmail] = useState("");
  // 모달창 노출 여부 state
  const [modalOpen, setModalOpen] = useState(false);
  
  const navigate = useNavigate();

  // 모달창 노출
  const openModal = () => {
    setModalOpen(true);
  };
  //모달창 닫기 
  const closeModal = () => {
    setModalOpen(false);
  };

  const getPost = async() => {
    await axios.post("http://localhost:8080/postDetail", {
      postId:postId
      })
      .then(
        data => {
          console.log(data.data); 
          setTitle(data.data.title);
          setContents(data.data.content);
          setImage(data.data.image);
          setUserEmail(data.data.email);
        }
      )  
    }; 
  
  useEffect(() => {
    console.log(postId);
    getPost(); 
  },[] );


  const updatePost = () => {
    navigate("/update",{
      state: {
        postId : postId
      }
    },{
      replace: false})
    }; 
    

  const deletePost = async() => {
    await axios.post("http://localhost:8080/postDelete", {
      postId:postId
      })
      .then(
        response => {
          console.log(response.data);
          response.data.result === "true" ?  navigate("/",{
          replace: false}) :  alert("다시 시도해 주세요")
        }
      )  
    }; 
  const LovePost = async() => {
    await axios.post("http://localhost:8080/postLove", {
    accountId : accountId,
    postId : postId
    })
    .then(
      data => {
        console.log(data.data); 
        addLikes(data.data.result)
      }
    )  
  }; 

  return (
    <div className='Mains'>
        
      <LoginHeader></LoginHeader>     
      <SidebarAll></SidebarAll>
    <div>
      
      <h1>게시물 상세 페이지</h1>
      <button className='user' onClick={openModal}>{userEmail}</button>
      <ModalView open={modalOpen} close={closeModal} header="공유 다이어리 친구 신청" data={userEmail}>
      </ModalView>
      <h3> title : {title}</h3>
      <img key={image} style={{
                height: -100,
                width: 500
            }} src={"/static/image/"+image+".png"}/><br/>
      
      <div dangerouslySetInnerHTML={{ __html: contents }} />
      
      <div>
        <h2>좋아요
          <span onClick= {()=>
          LovePost(likes + 1)
        }
        > ❤️ </span> { likes }
          </h2> 
      </div>
      <button type='button' onClick={() => updatePost()}> 포스트 수정 </button> <button type='button' onClick={() => deletePost()}> 포스트 삭제 </button>
    </div>
    </div>
  ) 
}

export default Detail;