import React, {createContext, useContext, useEffect, useState} from "react";
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
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);

    const checkAuthStatus = async (): Promise<boolean> => {
        return await authService.checkAuthStatus();
    };

    useEffect(() => {
        checkAuthStatus()
            .then((isLoggedIn) => {
                setIsLoggedIn(isLoggedIn);
                localStorage.setItem("isLoggedIn", isLoggedIn?.toString());
            })
            .catch((error) => {
                console.error("Error checking auth status:", error);
            })
            .finally(() => setLoading(false));
    }, []);

    const login = () => {
        setIsLoggedIn(true);
        localStorage.setItem("isLoggedIn", "true");
    };

    const logout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem("isLoggedIn");
    };

    if (loading) {
        return null;
    }

    return (
        <AuthContext.Provider value={{isLoggedIn, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};