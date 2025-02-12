import React, {createContext, useContext, useEffect} from "react";
import authService from "./AuthService";

type AuthContextType = {
    isLoggedIn: boolean;
    login: () => void;
    logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
    const auth = useContext(AuthContext);
    if (!auth) throw new Error("useAuth must be used within AuthProvider");
    return auth;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [isLoggedIn, setIsLoggedIn] = React.useState<boolean>(() => {
        return localStorage.getItem("isLoggedIn") === "true";
    });

    const checkAuthStatus = async (): Promise<boolean> => {
        const response = await authService.checkAuthStatus();
        return response.isAuthenticated;
    };

    useEffect(() => {
        checkAuthStatus()
            .then((isLoggedIn) => {
                if (isLoggedIn) {
                    login();
                }
            });
    }, []);

    const login = () => {
        setIsLoggedIn(true);
        localStorage.setItem("isLoggedIn", "true");
    };

    const logout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem("isLoggedIn");
    };


    return (
        <AuthContext.Provider value={{isLoggedIn, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};