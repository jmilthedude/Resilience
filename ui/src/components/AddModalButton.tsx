import React from "react";
import {Button} from "@mantine/core";
import {useMediaQuery} from "@mantine/hooks";
import {FiPlus} from "react-icons/fi";

interface AddModalButtonProps {
    setAddModalOpen: (open: boolean) => void;
    text: string;
    style?: React.CSSProperties;
}

const AddModalButton: React.FC<AddModalButtonProps> = ({setAddModalOpen, text, style}) => {
    const isMobile = useMediaQuery("(max-width: 768px)");

    const defaultStyle: React.CSSProperties = {
        position: "absolute",
        right: isMobile ? "50%" : 0,
        bottom: isMobile ? 20 : "auto",
        top: isMobile ? "auto" : 0,
        width: isMobile ? 60 : "auto",
        height: isMobile ? 60 : "auto",
        transform: isMobile ? "translateX(50%)" : "none",
        borderRadius: isMobile ? "50%" : "5px",
        padding: isMobile ? 0 : "10px 16px",
        fontSize: isMobile ? "25px" : "inherit",
        ...style,
    };

    return (
        <div style={{position: "relative"}}>
            <Button
                style={defaultStyle}
                color="resilience"
                onClick={() => setAddModalOpen(true)}
            >
                {isMobile ? <FiPlus size={25}/> : text}
            </Button>
        </div>
    );
};

export default AddModalButton;