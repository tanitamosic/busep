import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Routes, Navigate, Outlet} from 'react-router-dom';
import {Container} from 'react-bootstrap'
import BootstrapTable from 'react-bootstrap-table-next';
import cellEditFactory from 'react-bootstrap-table2-editor';
import { RegistrationForm } from './components/forms/RegistrationForm';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './assets/styles/style.css';

function App() {
  // const resortForm = <Container><CreateForm userType={user} /></Container>
  // const resortView = <Container><AssetDetailedView /></Container>
  // const assetList = <Container><AssetsPreview isSearch={false}/></Container>
  
  // const resortForm = <Container><CreateForm userType={user} /></Container>
  const registrationForm = <Container><RegistrationForm /></Container>
  // const assetList = <Container><AssetsPreview isSearch={false}/></Container>

  return  (<Router>
              <Routes>
                <Route path=''>
                  <Route path="/register" element={registrationForm} />
                {/* <Route path='' element={<ProtectedRoute isAllowedUser={user}>{chooseNavbar(user)} </ProtectedRoute>}> */}
                  {/* <Route path='/home' element={home} />  */}
                  {/* <Route path="createAsset" element={resortForm} />  */}
                  {/* <Route path="/logout" element={<Container><Logout handleLogout={handleLogout}/></Container>} /> */}
                  {/* <Route exact path="/resorts" element={assetList} />  */}
                  {/* <Route path="/resorts/all" element={assetListAll} />  */}
                  {/* <Route path="/resorts/:id" element={resortView} />  */}
                </Route>
                <Route path='/user' element={"<h1>user</h1>"}>

                </Route>
                <Route path='/admin/certificates/:id' element={"<h1>:id certificate</h1>"}/> {/* preview, validate, remove - 6, 7, 8*/}
                <Route path='/admin/certificates' element={"<h1>all certificates</h1>"}/> {/* list all, button for detailed view - 5 */}
                <Route path='/admin/requests/:id' element={"<h1>:id request</h1>"}/> {/* preview, accept, decline - 2, 3, 4*/} 
                <Route path='/admin/requests' element={"<h1>all requests</h1>"}/> {/* list all, button for detailed view - 1*/}
                <Route path='/admin' element={"<h1>admin</h1>"}>

                </Route>
                {/* <Route path="login" element={login} />
                <Route path="verify/:code" element={confirmation} />
                <Route index element={login} /> */}
                <Route path="*" element={<h1>Sorry, this page is not available :( </h1>} />
              </Routes>
          </Router>);
        
}

export default App;
