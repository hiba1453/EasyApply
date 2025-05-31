import React, { forwardRef } from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helper?: string;
  wrapperClassName?: string;
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, helper, className = '', wrapperClassName = '', ...props }, ref) => {
    return (
      <div className={`input-group ${wrapperClassName}`}>
        {label && (
          <label htmlFor={props.id} className="label">
            {label}
          </label>
        )}
        <input
          ref={ref}
          className={`input ${error ? 'border-error-500 focus:ring-error-500 focus:border-error-500' : ''} ${className}`}
          {...props}
        />
        {error && <p className="mt-1 text-sm text-error-600">{error}</p>}
        {helper && !error && <p className="mt-1 text-sm text-gray-500">{helper}</p>}
      </div>
    );
  }
);

Input.displayName = 'Input';

export default Input;