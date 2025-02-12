import React from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import UserLoginForm from "./form/UserLoginForm";
import AddUserForm from "./form/AddUserForm";
import UserPage from "./pages/UserPage";
import Layout from "./Layout";
import PrivateRoute from "./auth/PrivateRoute";
import {AuthProvider} from "./auth/AuthProvider";

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Layout />}>
                        <Route index element={<Navigate to="/users" replace />} />
                        <Route path="users" element={<PrivateRoute><UserPage /></PrivateRoute>} />
                        <Route path="users/add" element={<PrivateRoute><AddUserForm /></PrivateRoute>} />
                    </Route>
                    <Route path="/login" element={<UserLoginForm />} />
                    <Route path="*" element={<Navigate to="/users" />} />
                </Routes>
            </Router>

        </AuthProvider>
    )
};

export default App;
