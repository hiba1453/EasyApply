import React from 'react';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  padding?: 'none' | 'sm' | 'md' | 'lg';
  onClick?: () => void;
}

const Card: React.FC<CardProps> = ({
  children,
  className = '',
  padding = 'md',
  onClick
}) => {
  const paddingClasses = {
    none: 'p-0',
    sm: 'p-4',
    md: 'p-6',
    lg: 'p-8'
  };

  const clickableClasses = onClick ? 'cursor-pointer hover:shadow-md transition-shadow duration-200' : '';

  return (
    <div 
      className={`bg-white rounded-lg shadow-sm ${paddingClasses[padding]} ${clickableClasses} ${className}`}
      onClick={onClick}
    >
      {children}
    </div>
  );
};

export default Card;