import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Container} from "@mantine/core";
import useApi from "../../api/axiosInstance";
import {useAuth} from "../../auth/AuthProvider";
import StyledTextInput from "../../components/StyledTextInput";


const LoginPage = () => {

    const {isLoggedIn, login} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (isLoggedIn) {
            navigate("/users");
        }
    }, [isLoggedIn, navigate]);

    const api = useApi();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        api.postForm("/login", {username, password})
            .then(() => {
                login();
                navigate("/users")
            })
            .catch(error => {
                console.error(error.message)
            });
    };

    return (
        <Container size="md" style={{marginTop: "50px", maxWidth: "400px"}}>
            <h2 style={{marginBottom: "5px"}}>Please Login</h2>
            <form onSubmit={handleSubmit}>
                <StyledTextInput
                    inputType="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <StyledTextInput
                    inputType="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <Button variant="filled" fullWidth={true} color={"resilience"} radius="lg" type="submit">Login</Button>
            </form>
        </Container>
    );
};

export default LoginPage;
