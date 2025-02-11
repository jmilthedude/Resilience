import React from "react";
import { Button } from "@mantine/core";
import { useMediaQuery } from "@mantine/hooks";
import { FiPlus } from "react-icons/fi";

const AddUserButton = ({ setAddModalOpen }: { setAddModalOpen: (open: boolean) => void }) => {
    const isMobile = useMediaQuery("(max-width: 768px)"); // Check if the screen is mobile-sized

    return (
        <Button
            style={{
                position: "absolute",
                right: 20,
                top: 20,
                width: isMobile ? 34 : "auto", // Round size on mobile
                height: isMobile ? 34 : "auto", // Equal height to width for a perfect circle
                borderRadius: isMobile ? "50%" : "5px", // Make it round on mobile
                padding: isMobile ? 0 : "10px 16px", // Remove padding for the + icon
                fontSize: isMobile ? "25px" : "inherit", // Larger font size for the + icon

            }}
            color="teal"
            onClick={() => setAddModalOpen(true)}
        >
            {isMobile ? <FiPlus size={25} /> : "Add User"} {/* Show only + icon on mobile */}
        </Button>
    );
};

export default AddUserButton;
