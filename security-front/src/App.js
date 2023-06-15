import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Routes, Navigate, Outlet} from 'react-router-dom';
import {Container, Navbar} from 'react-bootstrap'
import { RegistrationForm } from './components/forms/RegistrationForm';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './assets/styles/style.css';
import RequestsList from './components/business/RequestsList';
import RequestPreview from './components/business/RequestPreview';
import CertificatesList from './components/business/CertificatesList';
import CertificatePreview from './components/business/CertificatePreview';
import GuestNavbar from './layouts/GuestNavbar';
import AdminNavbar from './layouts/AdminNavbar';
import ClientNavbar from './layouts/ClientNavbar';
import { LoginForm } from './components/forms/LoginForm';
import AdminFirstPage from './components/business/AdminFirstPage';
import ClientFirstPage from './components/business/ClientFirstPage';
import LogoutPage from './layouts/LogoutPage';
import UnavailablePage from './components/business/UnavailablePage';
import { useEffect, useState } from 'react';
import UserObjectsList from './components/business/UserObjectsList';
import AllObjectsList from './components/business/AllObjectsList';
import { CreateObjectForm } from './components/forms/CreateObjectForm';
import { getRole } from './services/utils/AuthService';
import ObjectPreview from './components/business/ObjectPreview';
import MyObjectsList from './components/business/MyObjectsList';

function App() {
  const registrationForm = <Container><RegistrationForm /></Container>
  const loginForm = <Container><LoginForm /></Container>
  const requestsList = <Container><RequestsList /></Container>
  const requestPreview = <Container><RequestPreview /></Container>
  const certificatesList = <Container><CertificatesList /></Container>
  const certificatePreview = <Container><CertificatePreview /></Container>
  const adminFirstPage = <Container><AdminFirstPage /></Container>
  const clientFirstPage = <Container><ClientFirstPage /></Container>
  const logoutPage = <Container><LogoutPage /></Container>
  const unavailablePage = <Container><UnavailablePage /></Container>
  const userObjectsList = <Container><UserObjectsList /></Container>
  const allObjectsList = <Container><AllObjectsList /></Container>
  const createObjectForm = <Container><CreateObjectForm /></Container>
  const objectPreview = <Container><ObjectPreview /></Container>
  const myObjects = <Container><MyObjectsList /></Container>

  const [navBar, setNavBar] = useState(getNavbarByUserRole());

  window.addEventListener('userRoleUpdated', () => {
    setNavBar(getNavbarByUserRole())
  })

  function getNavbarByUserRole(){
    let userRole = getRole();

    if(userRole === "client"){
      return <ClientNavbar />;
    }
    else if(userRole === "admin"){
      return <AdminNavbar />;
    }
    else{
      return <GuestNavbar />;
    }
  }

  return  (<Router>
              {navBar}
              <Routes>
                <Route path="" >
                  <Route path="/register" element={registrationForm} />
                  <Route path="/login" element={loginForm} />
                  <Route path="/logout" element={logoutPage} />

                  <Route path='/client/my-objects/:id' element={objectPreview} />
                  <Route path='/client/my-objects' element={myObjects} />
                  <Route path='/client' element={clientFirstPage} /> 

                  <Route path='/admin/certificates/:id' element={certificatePreview}/> {/* preview, validate, remove - 6, 7, 8*/}
                  <Route path='/admin/certificates' element={certificatesList}/> {/* list all, button for detailed view - 5 */}

                  <Route path='/admin/requests/:email' element={requestPreview}/> {/* preview, accept, decline - 2, 3, 4*/} 
                  <Route path='/admin/requests' element={requestsList}/> {/* list all, button for detailed view - 1*/}

                  <Route path='/admin/clients/:email' element={unavailablePage}/> {/* TODO*/}
                  <Route path='/admin/clients' element={unavailablePage}/> {/* TODO*/}

                  <Route path='/admin/all-objects' element={allObjectsList}/>
                  <Route path='/admin/objects/:email' element={userObjectsList}/>
                  <Route path='/admin/object/:id' element={objectPreview}/>
                  
                  <Route path='/admin/new-object' element={createObjectForm}/>

                  <Route path='/admin' element={adminFirstPage}/>

                  <Route path="*" element={unavailablePage} />
                </Route>
              </Routes>
          </Router>);
}

export default App;
