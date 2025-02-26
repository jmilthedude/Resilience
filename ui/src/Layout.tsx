import React, {useEffect} from "react";
import {Outlet} from "react-router-dom"; // To render child routes
import {AppShell, Burger} from "@mantine/core";
import {useDisclosure, useMediaQuery} from "@mantine/hooks";
import {useAuth} from "./auth/AuthProvider";
import userService from "./api/services/UserService";
import {User} from "./types/user";
import NavLinks from "./components/NavLinks";

const Layout = () => {
    const [opened, {toggle, close}] = useDisclosure(true);
    const {isLoggedIn} = useAuth();
    const [user, setUser] = React.useState<User | null>(null);
    const isMobile = useMediaQuery('(max-width: 768px)');

    useEffect(() => {
        if (isLoggedIn) {
            userService.getCurrentUser().then((user) => {
                setUser(user);
            });
        }
    }, [isLoggedIn]);

    const handleLinkClick = () => {
        close();
    };

    if (!isLoggedIn) {
        return <Outlet/>
    }

    return (
        <AppShell
            transitionTimingFunction="ease"
            transitionDuration={2000}
            navbar={{
                width: 0,
                breakpoint: 'sm',
                collapsed: {mobile: !opened},
            }}
        >
            <AppShell.Header style={{height: 60, display: "flex", alignItems: "center", justifyContent: "center"}}>

            </AppShell.Header>
            <Burger
                style={{
                    position: "absolute",
                    left: 20,
                    top: 20,
                    zIndex: 100,
                }}
                opened={opened}
                onClick={toggle}
                size="sm"
            />
            <AppShell.Navbar style={{
                position: "absolute",
                width: opened ? 225 : 0,
                height: "100vh",
                zIndex: 101,
                transition: "width 2s ease",
                overflow: "hidden",
            }}>
                <div style={{
                    display: "flex",
                    alignItems: "flex-start",
                    justifyContent: "space-between",
                    paddingTop: 20,
                    paddingLeft: 20,
                    paddingRight: 20
                }}>
                    <h1 style={{margin: 0}}>Resilience</h1>
                    <Burger
                        style={{
                            visibility: opened ? "visible" : "hidden",
                            alignSelf: "right",
                        }}
                        opened={true}
                        onClick={toggle}
                        size="sm"
                    />
                </div>
                <NavLinks user={user} handleLinkClick={handleLinkClick}/>
            </AppShell.Navbar>
            <AppShell.Main style={
                {
                    paddingTop: 60,
                    filter: opened ? "blur(2px)" : "none",
                    transition: "filter 0.5s ease"
                }
            }><Outlet/></AppShell.Main>
        </AppShell>
    );
};

export default Layout;