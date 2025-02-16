import React from "react";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import UserLoginForm from "./form/user/UserLoginForm";
import AddUserForm from "./form/user/AddUserForm";
import UserAdminPage from "./pages/UserAdminPage";
import AccountAdminPage from "./pages/AccountAdminPage";
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
