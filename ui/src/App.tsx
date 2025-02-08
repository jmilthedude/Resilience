import React from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import UserLoginForm from "./form/UserLoginForm";
import AddUserForm from "./form/AddUserForm";
import UserPage from "./pages/UserPage";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login"/>}/>
                <Route path="/login" element={<UserLoginForm/>}/>
                <Route path="/users/add" element={<AddUserForm/>}/>
                <Route path="/users" element={<UserPage/>}/>
            </Routes>
        </Router>
    )
};


export default App;
