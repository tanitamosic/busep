import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Routes, Navigate, Outlet} from 'react-router-dom';

// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }

function App() {
  // const resortForm = <Container><CreateForm userType={user} /></Container>
  // const resortView = <Container><AssetDetailedView /></Container>
  // const assetList = <Container><AssetsPreview isSearch={false}/></Container>
  

  return  (<Router>
              <Routes>
                <Route path=''>
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
