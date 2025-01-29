import React, { useState } from "react";
import {Button, ComboboxItem, Container, Input, Select} from "@mantine/core";

const AddUserPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("USER");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch("http://localhost:8081/api/v1/users/add", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password, role: "USER" }),
        });

        if (response.ok) {
            alert("User added successfully!");
            setUsername("");
            setPassword("");
            setRole("USER");
        } else {
            const errorResponse = await response.json();
            let error = errorResponse.errors[0];
            const field = "Invalid " + error.field;
            const errorMessage = (field + ": " + error.defaultMessage) || "Unknown error";
            alert(errorMessage);
        }
    };
    return (
        <Container size="md" style={{ maxWidth: "400px", margin: "50px auto", textAlign: "center" }}>
            <h2>Add User</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <Input radius="lg" style={{marginBottom: "15px"}}
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
                <Button variant="filled" fullWidth={true} color="teal" radius="lg" type="submit">Submit</Button>
            </form>
        </Container>
    );
};

export default AddUserPage;
