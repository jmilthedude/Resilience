import React from "react";
import {Outlet} from "react-router-dom"; // To render child routes
import {AppShell, Burger} from "@mantine/core";
import {useDisclosure} from "@mantine/hooks";
import {useAuth} from "./auth/AuthProvider";

const Layout = () => {
    const [opened, {toggle}] = useDisclosure();
    const {isLoggedIn} = useAuth(); // Check login status

    if (!isLoggedIn) {
        return <Outlet/>
    }

    return (
        <AppShell
            padding="md"
            navbar={{
                width: opened ? 300 : 0,
                breakpoint: 'sm',
                collapsed: {mobile: !opened},
            }}
        >
            <Burger
                style={{
                    position: "absolute",
                    left: 20,
                    top: 20,
                }}
                opened={opened}
                onClick={toggle}
                size="sm"
            />
            <AppShell.Navbar p={opened ? "md" : 0} style={{
                transition: "width 0.3s",
                overflow: "hidden",
            }}>
                <div style={{display: "flex", alignItems: "flex-start", justifyContent: "space-between"}}>
                    <h1 style={{margin: 0, visibility: opened ? "visible" : "hidden"}}>Resilience</h1>
                    <Burger
                        style={{
                            visibility: opened ? "visible" : "hidden",
                            alignSelf: "right"
                        }}
                        opened={opened}
                        onClick={toggle}
                        size="sm"
                    />
                </div>
            </AppShell.Navbar>
            <AppShell.Main><Outlet/></AppShell.Main>
        </AppShell>
    );
};

export default Layout;
