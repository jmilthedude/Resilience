import React from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import LoginPage from "./pages/UserLogin";
import AddUserPage from "./pages/AddUser";

const App = () => {

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login"/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/add-user" element={<AddUserPage/>}/>
            </Routes>
        </Router>
    )

};


export default App;
