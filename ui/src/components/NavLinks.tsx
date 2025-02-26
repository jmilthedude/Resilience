import React from "react";
import {Link} from "react-router-dom";
import {NavLink, SimpleGrid} from "@mantine/core";
import {FiBriefcase, FiDollarSign, FiPieChart, FiSettings} from "react-icons/fi";
import {User} from "../types/user";

interface NavLinksProps {
    user: User | null;
    handleLinkClick: () => void;
}

const NavLinks: React.FC<NavLinksProps> = ({user, handleLinkClick}) => {
    return (
        <SimpleGrid cols={1} spacing="xs" style={{marginTop: 20, paddingLeft: 20}}>
            <NavLink label={"Dashboard"}
                     leftSection={<FiPieChart/>}
                     style={{
                         textDecoration: 'none',
                         display: "flex",
                         alignItems: "center"
                     }}/>
            <NavLink label={"Accounts"}
                     component={Link} to="/accounts"
                     leftSection={<FiDollarSign/>}
                     style={{
                         textDecoration: 'none',
                         display: "flex",
                         alignItems: "center"
                     }}
                     onClick={handleLinkClick}/>
            {user?.role === 'ADMIN' &&
                (
                    <NavLink label={"Users"}
                             component={Link} to="/users"
                             leftSection={<FiBriefcase/>}
                             style={{
                                 textDecoration: 'none'
                             }}
                             onClick={handleLinkClick}/>
                )}
            <NavLink label={"Settings"}
                     leftSection={<FiSettings/>}
                     childrenOffset={40}
                     style={{
                         textDecoration: 'none',
                         display: "flex",
                         alignItems: "center"
                     }}>
            </NavLink>
        </SimpleGrid>
    );
};

export default NavLinks;