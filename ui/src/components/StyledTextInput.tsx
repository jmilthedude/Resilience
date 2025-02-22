import React from 'react';
import {TextInput, TextInputProps} from '@mantine/core';

interface StyledTextInputProps extends TextInputProps {
    placeholder: string;
    inputType?: string;
    radius?: string;
    mb?: string | number;
    label?: string;  // Optional label prop
}

const StyledTextInput: React.FC<StyledTextInputProps> = (
    {
        placeholder,
        inputType = 'text',
        radius = 'md',
        mb = 'md',
        label,  // Optional label
        ...props
    }) => {
    return (
        <TextInput
            {...props}
            placeholder={placeholder}
            type={inputType}
            radius={radius}
            mb={mb}
            label={label || undefined}  // Conditionally add label
            classNames={{input: 'styled-text-input'}}
        />
    );
};

export default StyledTextInput;
