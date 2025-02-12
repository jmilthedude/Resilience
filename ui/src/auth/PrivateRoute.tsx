import React from "react";
import {Navigate} from "react-router-dom";
import {useAuth} from "./AuthProvider";

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const {isLoggedIn} = useAuth();

    if (!isLoggedIn) {
        console.log("User is not logged in. Redirecting to login page.");
        return <Navigate to="/login" replace/>;
    }
    return <>{children}</>;
};

export default PrivateRoute;