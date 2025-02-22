import React from "react";
import {Button} from "@mantine/core";
import {useMediaQuery} from "@mantine/hooks";
import {FiPlus} from "react-icons/fi";

const AddModalButton = ({setAddModalOpen, text}: {
    setAddModalOpen: (open: boolean) => void,
    text: string
}) => {
    const isMobile = useMediaQuery("(max-width: 768px)"); // Check if the screen is mobile-sized

    return (
        <Button
            style={{
                position: "absolute",
                right: isMobile ? "50%" : 20,
                bottom: isMobile ? 20 : "auto",
                top: isMobile ? "auto" : 20,
                width: isMobile ? 60 : "auto", // Round size on mobile
                height: isMobile ? 60 : "auto", // Equal height to width for a perfect circle
                transform: isMobile ? "translateX(50%)" : "none", // Center the button
                borderRadius: isMobile ? "50%" : "5px", // Make it round on mobile
                padding: isMobile ? 0 : "10px 16px", // Remove padding for the + icon
                fontSize: isMobile ? "25px" : "inherit", // Larger font size for the + icon

            }}
            color="resilience"
            onClick={() => setAddModalOpen(true)}
        >
            {isMobile ? <FiPlus size={25}/> : text} {/* Show only + icon on mobile */}
        </Button>
    );
};

export default AddModalButton;
