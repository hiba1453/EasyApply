import React from 'react';

interface TagProps {
  label: string;
  color?: 'blue' | 'green' | 'yellow' | 'red' | 'gray';
  size?: 'sm' | 'md';
  className?: string;
}

const Tag: React.FC<TagProps> = ({ 
  label, 
  color = 'blue', 
  size = 'md',
  className = ''
}) => {
  const colorClasses = {
    blue: 'bg-primary-50 text-primary-700',
    green: 'bg-success-50 text-success-600',
    yellow: 'bg-warning-50 text-warning-600',
    red: 'bg-error-50 text-error-600',
    gray: 'bg-gray-100 text-gray-700'
  };

  const sizeClasses = {
    sm: 'text-xs px-2 py-0.5',
    md: 'text-sm px-2.5 py-1'
  };

  return (
    <span 
      className={`inline-flex items-center font-medium rounded-md ${colorClasses[color]} ${sizeClasses[size]} ${className}`}
    >
      {label}
    </span>
  );
};

export default Tag;