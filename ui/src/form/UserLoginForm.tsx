import React, {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Container, Input} from "@mantine/core";

const LoginPage = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const response = await fetch("http://localhost:8081/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                username,
                password,
            }),
        });

        if (response.ok) {
            navigate("/users/add")// Redirect to the homepage
        } else {
            alert("Invalid username or password");
        }
    };

    return (
        <Container size="md" style={{ marginTop: "50px", maxWidth: "400px" }}>
            <h2 style={{marginBottom: "5px"}}>Login</h2>
            <form onSubmit={handleSubmit} >
                <div>
                    <Input style={{marginBottom: "15px"}} radius="lg"
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <Input radius="lg" style={{marginBottom: "15px"}}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <Button variant="filled" fullWidth={true} color="teal" radius="lg" type="submit">Login</Button>
            </form>
        </Container>
    );
};

export default LoginPage;
