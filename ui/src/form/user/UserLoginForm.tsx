import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Container, TextInput} from "@mantine/core";
import useApi from "../../api/axiosInstance";
import {useAuth} from "../../auth/AuthProvider";
import styles from "./UserLoginForm.module.css"


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
                alert(error.message)
            });
    };

    return (
        <Container size="md" style={{marginTop: "50px", maxWidth: "400px"}}>
            <h2 style={{marginBottom: "5px"}}>Please Login</h2>
            <form onSubmit={handleSubmit}>
                <TextInput
                    classNames={{
                        input: styles.input
                    }}
                    style={{marginBottom: "15px"}}
                    radius="lg"
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <TextInput
                    classNames={{
                        input: styles.input
                    }}
                    radius="lg"
                    style={{marginBottom: "15px"}}
                    type="password"
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
