import React from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import UserLoginForm from "./form/user/UserLoginForm";
import UserAdminPage from "./pages/UserAdminPage";
import AccountAdminPage from "./pages/AccountAdminPage";
import Layout from "./Layout";
import PrivateRoute from "./auth/PrivateRoute";
import {AuthProvider} from "./auth/AuthProvider";
import Dashboard from "./pages/Dashboard";

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Layout />}>
                        <Route index element={<Navigate to="/dashboard" replace />} />
                        <Route path="dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
                        <Route path="users" element={<PrivateRoute><UserAdminPage /></PrivateRoute>} />
                        <Route path="accounts" element={<PrivateRoute><AccountAdminPage /></PrivateRoute>} />
                    </Route>
                    <Route path="/login" element={<UserLoginForm />} />
                    <Route path="*" element={<Navigate to="/users" />} />
                </Routes>
            </Router>

        </AuthProvider>
    )
};

export default App;
